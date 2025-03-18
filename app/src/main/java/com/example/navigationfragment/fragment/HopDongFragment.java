package com.example.navigationfragment.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.navigationfragment.AppDatabase;
import com.example.navigationfragment.DAO.ContractDAO;
import com.example.navigationfragment.DAO.RoomDAO;
import com.example.navigationfragment.adapter.ContractAdapter;
import com.example.navigationfragment.databinding.FragmentHopdongBinding;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.RoomEntity;
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

public class HopDongFragment extends Fragment {
    private FragmentHopdongBinding binding;
    private ContractAdapter contractAdapter;
    private List<ContractEntity> contractList = new ArrayList<>();
    private ContractDAO contractDAO;
    private RoomDAO roomDAO;
    private ContractEntity contract;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference contractRef;
    private ExecutorService executorService;
    private ChildEventListener childEventListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= FragmentHopdongBinding.inflate(inflater,container,false);


        contractDAO = AppDatabase.getInstance(getContext()).contractDao();
        // Khởi tạo Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        contractRef = firebaseDatabase.getReference("contracts");
        // Khởi tạo Adapter
        contractAdapter = new ContractAdapter(contractList, getContext());
        binding.rcv.setAdapter(contractAdapter);
        fetchContractsFromFirebase();
        // Lắng nghe dữ liệu từ Room (LiveData)
        observeContractData();


        return binding.getRoot();
    }

    private void observeContractData() {
        contractDAO.getAllContracts().observe(getViewLifecycleOwner(), contracts -> {
            if (contracts != null) {
                contractList.clear();
                contractList.addAll(contracts);
                contractAdapter.notifyDataSetChanged();
            }
        });
    }
    private void fetchContractsFromFirebase() {

        contractRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ContractEntity newContract = snapshot.getValue(ContractEntity.class);

                if (newContract != null) {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        // thêm hợp đồng vào room
                        contractDAO.insert(newContract);

                        // Cập nhật trạng thái phòng đã cho thuê
                        int  roomId = newContract.getRoomId();  // giả sử trường này lưu mã phòng

                        // Lấy phòng từ Room Database
                        RoomEntity room = roomDAO.getRoomById(roomId);

                        if (room != null) {
                            room.setTrangThai(true);

                            roomDAO.update(room);    // cập nhật phòng vào Room DB
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Xử lý xóa hợp đồng và cập nhật phòng thành chưa thuê
                ContractEntity removedContract = snapshot.getValue(ContractEntity.class);

                if (removedContract != null) {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        contractDAO.delete(removedContract); // Xóa hợp đồng

                        // Cập nhật trạng thái phòng về chưa thuê
                        int roomId = removedContract.getRoomId();
                        RoomEntity room = roomDAO.getRoomById(roomId);

                        if (room != null) {
                            room.setTrangThai(false);  // hoặc "Trống"
                            roomDAO.update(room);
                        }
                    });
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Lỗi: " + error.getMessage());
            }
        });

        // Đăng ký listener
        contractRef.addChildEventListener(childEventListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Xóa listener khi view destroy
        if (childEventListener != null) {
            contractRef.removeEventListener(childEventListener);
        }

        // Shutdown executor service để giải phóng tài nguyên
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
