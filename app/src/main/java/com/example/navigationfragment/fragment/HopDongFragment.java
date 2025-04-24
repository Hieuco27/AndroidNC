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

import com.example.navigationfragment.AppDatabase;
import com.example.navigationfragment.DAO.ContractDAO;
import com.example.navigationfragment.DAO.KhachDAO;
import com.example.navigationfragment.DAO.RoomDAO;
import com.example.navigationfragment.adapter.ContractAdapter;
import com.example.navigationfragment.databinding.FragmentHopdongBinding;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.ContractWithDetails;
import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.RoomEntity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HopDongFragment extends Fragment {
    private FragmentHopdongBinding binding;
    private ContractAdapter contractAdapter;
    private ContractDAO contractDAO;
    private RoomDAO roomDAO;
    private KhachDAO khachDAO;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference contractRef;
    private ExecutorService executorService;
    private ChildEventListener contractListener;
    private List<ContractWithDetails> contractList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHopdongBinding.inflate(inflater, container, false);

        // Init Room DB + DAO
        AppDatabase db = AppDatabase.getInstance(getContext());
        contractDAO = db.contractDao();
        roomDAO = db.roomDao();
        khachDAO = db.khachDao();
        executorService = Executors.newSingleThreadExecutor();

        // Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        contractRef = firebaseDatabase.getReference("contracts");

        // Adapter
        contractAdapter = new ContractAdapter(
                getContext(),
                contractList,
                roomDAO,
                contractDAO,
                khachDAO
        );
        binding.rcv.setAdapter(contractAdapter);
        binding.rcv.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Swipe to Refresh
        binding.swipeRefresh.setOnRefreshListener(() -> {
            loadContracts(); // Reload from Room
            binding.swipeRefresh.setRefreshing(false);
        });

        // Chỉ observe LiveData 1 lần khi tạo View
        observeContractData();

        // Đồng bộ từ Firebase -> Room
        fetchContractsFromFirebase();

        return binding.getRoot();
    }

    // Quan sát LiveData để cập nhật danh sách hợp đồng
    private void observeContractData() {
        contractDAO.getAllContractsWithDetails().observe(getViewLifecycleOwner(), contracts -> {
            if (contracts != null) {
                contractAdapter.updateData(contracts);
                Log.d("HopDongFragment", "Số hợp đồng lấy được: " + contracts.size());
            }
        });
    }


    private void loadContracts() {
        executorService.execute(() -> {
            List<ContractWithDetails> contracts =  contractDAO.getAllContractsSync1(); // Tạo hàm mới này trong DAO
            requireActivity().runOnUiThread(() -> {
                contractAdapter.updateData(contracts); // hoặc setContractList(contracts)
            });
        });
    }


    private void fetchContractsFromFirebase() {
        contractListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ContractEntity newContract = snapshot.getValue(ContractEntity.class);
                executorService.execute(() -> {
                    RoomEntity room = roomDAO.getRoomByIdSync(newContract.getRoomId());
                    KhachEntity khach = khachDAO.getKhachByIdSync(newContract.getKhachId());

                    if (room != null && khach != null) {
                        contractDAO.insert(newContract);
                        Log.d("ContractSync", "Đã insert hợp đồng: " + newContract.getContractId());
                    } else {
                        Log.e("ContractSync", "Không thể insert contract vì thiếu Room hoặc Khách");
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ContractEntity updatedContract = snapshot.getValue(ContractEntity.class);
                if (updatedContract != null) {
                    executorService.execute(() -> contractDAO.update(updatedContract));
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ContractEntity deletedContract = snapshot.getValue(ContractEntity.class);
                if (deletedContract != null) {
                    executorService.execute(() -> {
                        contractDAO.delete(deletedContract);
                    });
                }
            }


            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        };
        contractRef.addChildEventListener(contractListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadContracts(); // luôn load lại khi quay lại Fragment
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (contractListener != null) {
            contractRef.removeEventListener(contractListener);
        }
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
