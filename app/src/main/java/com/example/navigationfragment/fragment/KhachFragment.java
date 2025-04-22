package com.example.navigationfragment.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationfragment.AppDatabase;
import com.example.navigationfragment.DAO.KhachDAO;
import com.example.navigationfragment.adapter.KhachAdapter;
import com.example.navigationfragment.databinding.FragmentKhachBinding;
import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.KhachWithRoom;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KhachFragment extends Fragment {
    private FragmentKhachBinding binding;
    private KhachAdapter khachAdapter;
    private List<KhachWithRoom> khachList = new ArrayList<>();
    private KhachDAO khachDAO;
    private KhachEntity khach;
    private DatabaseReference khachRef;
    private ChildEventListener khachListener;
    private ExecutorService executorService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentKhachBinding.inflate(inflater, container, false);
        executorService = Executors.newSingleThreadExecutor();
        // Khởi tạo DAO
        khachDAO = AppDatabase.getInstance(getContext()).khachDao();
        // Khởi tạo Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        khachRef = firebaseDatabase.getReference("khach");
        // Khởi tạo Adapter
        khachAdapter = new KhachAdapter(khachList, getContext());
        RecyclerView recyclerView = binding.rcv;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(khachAdapter);


        // dữ liệu từ LiveData
        observeKhachData();
        // Đồng bộ dữ liệu từ firebase
        fetchKhachFromFirebase();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        observeKhachData(); // Gọi lại danh sách khách mỗi khi Fragment hiển thị lại
    }

    private void observeKhachData() {
        khachDAO.getAllKhachWithRoom().observe(getViewLifecycleOwner(), khachs -> {
            if (khachs != null) {
                khachList.clear();
                khachList.addAll(khachs);
                khachAdapter.notifyDataSetChanged();
            }
        });
    }

    private void fetchKhachFromFirebase(){
        khachListener =new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                KhachEntity newKhach = snapshot.getValue(KhachEntity.class);
                if (newKhach != null) {
                    executorService.execute(() -> {
                        khachDAO.insert(newKhach);
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                KhachEntity updatedKhach = snapshot.getValue(KhachEntity.class);
                if (updatedKhach != null) {
                    executorService.execute(() -> {
                        khachDAO.update(updatedKhach);
                    });
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                KhachEntity deletedKhach = snapshot.getValue(KhachEntity.class);
                if (deletedKhach != null) {
                    String khachId = deletedKhach.getKhachId();
                    executorService.execute(() -> {
                        khachDAO.delete(String.valueOf(deletedKhach));

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
        khachRef.addChildEventListener(khachListener);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (khachListener != null) {
            khachRef.removeEventListener(khachListener);
        }
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        binding = null;
    }
}