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

import com.example.navigationfragment.adapter.KhachAdapter;
import com.example.navigationfragment.databinding.FragmentKhachBinding;
import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.KhachDisplay;
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

public class KhachFragment extends Fragment {
    private FragmentKhachBinding binding;
    private KhachAdapter khachAdapter;
    private List<KhachDisplay> khachDisplays = new ArrayList<>();
    private DatabaseReference khachRef, roomRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentKhachBinding.inflate(inflater, container, false);

        // Khởi tạo Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        khachRef = firebaseDatabase.getReference("khach");
        roomRef = firebaseDatabase.getReference("rooms");
        // Khởi tạo Adapter
        khachAdapter = new KhachAdapter(khachDisplays, getContext());
        binding.rcv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rcv.setAdapter(khachAdapter);

        // Đồng bộ dữ liệu từ Firebase
        fetchKhachFromFirebase();

        return binding.getRoot();
    }

    private void fetchKhachFromFirebase() {
        khachRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                khachDisplays.clear(); // Xóa danh sách phòng cũ
                for (DataSnapshot khachSnapshot : snapshot.getChildren()) {
                    KhachEntity khach = khachSnapshot.getValue(KhachEntity.class);
                    if (khach != null) {
                        KhachDisplay khachDisplay = new KhachDisplay();
                        khachDisplay.setKhach(khach);
                        roomRef.child(khach.getRoomId()).child("name").get()
                                .addOnSuccessListener(dataSnapshot -> {
                                    khachDisplay.setRoomName(dataSnapshot.getValue(String.class));
                                    khachDisplays.add(khachDisplay);
                                    if (khachDisplays.size() == snapshot.getChildrenCount()) {
                                        khachAdapter.updateData(khachDisplays);
                                        khachAdapter.notifyDataSetChanged();
                                    }
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
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
