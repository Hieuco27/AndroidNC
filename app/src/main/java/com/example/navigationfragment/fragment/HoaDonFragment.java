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
import com.example.navigationfragment.DAO.HoaDonDAO;
import com.example.navigationfragment.adapter.HoaDonAdapter;
import com.example.navigationfragment.databinding.FragmentHoadonBinding;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.HoaDonEntity;
import com.example.navigationfragment.entity.HoaDonWithRoom;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HoaDonFragment extends Fragment {
    FragmentHoadonBinding binding;
    private HoaDonAdapter hoaDonAdapter;
    private List<HoaDonWithRoom> hoaDonList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private HoaDonEntity hoaDon;
    private DatabaseReference hoaDonRef;
    private ExecutorService executorService;
    private ChildEventListener hoaDonListener;
    private HoaDonDAO hoaDonDAO;
    private ContractDAO contractDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= FragmentHoadonBinding.inflate(inflater,container,false);

        hoaDonDAO= AppDatabase.getInstance(getContext()).hoaDonDao();
        contractDAO=AppDatabase.getInstance(getContext()).contractDao();
        // Khởi tạo ExecutorService
        executorService = Executors.newSingleThreadExecutor();
        // Khởi tạo Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        hoaDonRef = firebaseDatabase.getReference("hoadon");



        //khởi ta adapter
        hoaDonAdapter = new HoaDonAdapter(hoaDonList, getContext());
        binding.rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcv.setAdapter(hoaDonAdapter);



        observeHoaDonData();
        // Lấy dữ liệu từ Firebase
        fetchHoaDonDataFromFirebase();

        return binding.getRoot();
    }
    private void observeHoaDonData(){
        hoaDonDAO.getAllHoaDonWithRoom().observe(getViewLifecycleOwner(), hoaDon -> {
            if (hoaDon != null) {
                hoaDonAdapter.upDateData(hoaDon);
                Log.d("FragmentContract", "Số lượng hợp đồng: " + hoaDon.size());
            }
        });
    }
    private void fetchHoaDonDataFromFirebase(){
        hoaDonListener= new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                HoaDonEntity newHoaDon = snapshot.getValue(HoaDonEntity.class);
                if (newHoaDon != null) {
                    executorService.execute(() -> {
                        hoaDonDAO.insert(newHoaDon);

                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                HoaDonEntity updatedHoaDon = snapshot.getValue(HoaDonEntity.class);
                if (updatedHoaDon != null) {
                    executorService.execute(() -> {
                        hoaDonDAO.update(updatedHoaDon);
                    });
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                HoaDonEntity deletedHoaDon = snapshot.getValue(HoaDonEntity.class);
                if (deletedHoaDon != null) {
                    executorService.execute(() -> {
                        hoaDonDAO.delete(deletedHoaDon);
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
        hoaDonRef.addChildEventListener(hoaDonListener);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (hoaDonListener != null) {
            hoaDonRef.removeEventListener(hoaDonListener);
        }
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
