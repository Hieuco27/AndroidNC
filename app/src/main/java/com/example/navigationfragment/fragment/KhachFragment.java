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

import com.example.navigationfragment.adapter.KhachAdapter;
import com.example.navigationfragment.databinding.FragmentKhachBinding;
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
    private DatabaseReference khachRef;
    private ChildEventListener khachListener;
    private ExecutorService executorService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentKhachBinding.inflate(inflater, container, false);
        executorService = Executors.newSingleThreadExecutor();

        // Khởi tạo Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        khachRef = firebaseDatabase.getReference("khach");

        // Khởi tạo Adapter
        khachAdapter = new KhachAdapter(khachList, getContext());
        binding.rcv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rcv.setAdapter(khachAdapter);

        // Đồng bộ dữ liệu từ Firebase
        fetchKhachFromFirebase();

        return binding.getRoot();
    }

    private void fetchKhachFromFirebase(){
        khachListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                KhachWithRoom newKhach = snapshot.getValue(KhachWithRoom.class);
                if (newKhach != null) {
                    // Thêm khách vào danh sách và cập nhật RecyclerView
                    khachList.add(newKhach);
                    khachAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                KhachWithRoom updatedKhach = snapshot.getValue(KhachWithRoom.class);
                if (updatedKhach != null) {
                    // Cập nhật khách trong danh sách nếu cần
                    // Tìm khách trong danh sách để cập nhật, sau đó notifyAdapter()
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                KhachWithRoom deletedKhach = snapshot.getValue(KhachWithRoom.class);
                if (deletedKhach != null) {
                    // Xóa khách khỏi danh sách nếu cần
                    khachList.remove(deletedKhach);
                    khachAdapter.notifyDataSetChanged();
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
            khachRef.removeEventListener(khachListener);  // Hủy listener khi fragment bị hủy
        }
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        binding = null;
    }
}
