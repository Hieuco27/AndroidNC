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
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationfragment.AppDatabase;
import com.example.navigationfragment.DAO.ContractDAO;
import com.example.navigationfragment.DAO.KhachDAO;
import com.example.navigationfragment.DAO.RoomDAO;
import com.example.navigationfragment.adapter.ContractAdapter;
import com.example.navigationfragment.databinding.FragmentHopdongBinding;
import com.example.navigationfragment.entity.ContractEntity;
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
    private List<ContractEntity> contractList = new ArrayList<>();
    private ContractDAO contractDAO;
    private RoomDAO roomDAO;
    private KhachDAO khachDAO;
    private ContractEntity contract;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference contractRef;
    private ExecutorService executorService;
    private ChildEventListener constactListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= FragmentHopdongBinding.inflate(inflater,container,false);

        contractDAO = AppDatabase.getInstance(getContext()).contractDao();
        roomDAO = AppDatabase.getInstance(getContext()).roomDao();

        khachDAO = AppDatabase.getInstance(getContext()).khachDao();

        executorService = Executors.newSingleThreadExecutor();

        // Khởi tạo Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        contractRef = firebaseDatabase.getReference("contracts");
        // Khởi tạo Adapter
        contractAdapter = new ContractAdapter( getContext());
        RecyclerView recyclerView = binding.rcv;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(contractAdapter);


        observeContractData();
        // Đồng bộ dữ liệu từ Firebase
        fetchContractsFromFirebase();

        return binding.getRoot();
    }

    private  void observeContractData(){
        contractDAO.getAllContracts().observe(getViewLifecycleOwner(), contracts -> {
            if (contracts != null) {
                contractList.clear();
                contractList.addAll(contracts);
                contractAdapter.notifyDataSetChanged();
                Log.d("FragmentContract", "Số lượng hợp đồng: " + contracts.size());
            }
        });
    }
    private void fetchContractsFromFirebase() {
        constactListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ContractEntity newContract = snapshot.getValue(ContractEntity.class);
                if (newContract != null) {
                    executorService.execute(() -> {
                        contractDAO.insert(newContract);
                        Log.d("ContractSync", "Thêm hợp đồng thành công: " + newContract.getContractId());
                    });
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ContractEntity updatedContract = snapshot.getValue(ContractEntity.class);
                if (updatedContract != null) {
                    executorService.execute(() -> {
                        contractDAO.update(updatedContract);
                    });
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ContractEntity deletedContract = snapshot.getValue(ContractEntity.class);

                if (deletedContract != null) {
                    executorService.execute(() -> {
                        contractDAO.delete(deletedContract);
                        Log.d("ContractSync", "Đã xóa hợp đồng: " + deletedContract.getContractId());
                    });
                }
            }


            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        contractRef.addChildEventListener(constactListener);
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (constactListener != null) {
            contractRef.removeEventListener(constactListener);
        }
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

}
