package com.example.navigationfragment.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.navigationfragment.R;
import com.example.navigationfragment.adapter.ContractAdapter;
import com.example.navigationfragment.databinding.FragmentHopdongBinding;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.ContractDisplay;
import com.example.navigationfragment.entity.KhachDisplay;
import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.RoomEntity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class HopDongFragment extends Fragment {
    private FragmentHopdongBinding binding;
    private ContractAdapter contractAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference contractRef, khachRef,roomRef;
    private ExecutorService executorService;
    private List<ContractDisplay> contractList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHopdongBinding.inflate(inflater, container, false);


        executorService = Executors.newSingleThreadExecutor();

        // Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        contractRef = firebaseDatabase.getReference("contracts");
        khachRef = firebaseDatabase.getReference("khach");
        roomRef=firebaseDatabase.getReference("rooms");
        contractAdapter = new ContractAdapter(
                getContext(),
                contractList
        );
        binding.rcv.setAdapter(contractAdapter);
        binding.rcv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Swipe to Refresh
        binding.swipeRefresh.setOnRefreshListener(() -> {
            loadContracts(); // Reload from Room
            binding.swipeRefresh.setRefreshing(false);
        });


        // Đồng bộ từ Firebase -> Room
        fetchContractsFromFirebase();

        return binding.getRoot();
    }


    private void loadContracts() {

    }


    private void fetchContractsFromFirebase() {
        contractRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contractList.clear(); // Xóa danh sách phòng cũ

                for (DataSnapshot contractSnapshot : snapshot.getChildren()) {
                    ContractEntity contract = contractSnapshot.getValue(ContractEntity.class);
                    if (contract != null) {
                        ContractDisplay contractDisplay = new ContractDisplay();
                        contractDisplay.setContract(contract);

                        // Lấy dữ liệu khách và phòng đồng thời
                        Task<DataSnapshot> khachTask = khachRef.child(contract.getKhachId()).get();
                        Task<DataSnapshot> roomTask = roomRef.child(contract.getRoomId()).get();

                        // Khi cả 2 tác vụ đều hoàn thành
                        Tasks.whenAllSuccess(khachTask, roomTask)
                                .addOnSuccessListener(results -> {
                                    // Lấy dữ liệu khách và phòng từ kết quả trả về
                                    DataSnapshot khachSnapshot = (DataSnapshot) results.get(0);
                                    DataSnapshot roomSnapshot = (DataSnapshot) results.get(1);

                                    contractDisplay.setKhach(khachSnapshot.getValue(KhachEntity.class));
                                    contractDisplay.setRoom(roomSnapshot.getValue(RoomEntity.class));

                                    // Thêm contractDisplay vào danh sách và cập nhật giao diện
                                    contractList.add(contractDisplay);
                                    if(contractList.size()==snapshot.getChildrenCount()){
                                    contractAdapter.updateData(contractList);}
                                })
                                .addOnFailureListener(e -> {
                                    // Xử lý khi có lỗi
                                    Log.e("FirebaseSync", "Lỗi khi lấy dữ liệu khách hoặc phòng: " + e.getMessage());
                                    contractDisplay.setKhach(null);
                                    contractDisplay.setRoom(null);
                                    contractList.add(contractDisplay);
                                    if(contractList.size()==snapshot.getChildrenCount()){
                                        contractAdapter.updateData(contractList);}
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseSync", "Lỗi khi lấy dữ liệu từ Firebase: " + error.getMessage());
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        loadContracts(); // luôn load lại khi quay lại Fragment
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
