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
import com.example.navigationfragment.R;
import com.example.navigationfragment.adapter.KhachAdapter;
import com.example.navigationfragment.databinding.FragmentKhachBinding;
import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.KhachWithRoom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class KhachFragment extends Fragment {
    private FragmentKhachBinding binding;
    private KhachAdapter khachAdapter;
    private List<KhachWithRoom> khachList = new ArrayList<>();
    private KhachDAO khachDAO;
    private  KhachEntity khach;
    private DatabaseReference khachRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentKhachBinding.inflate(inflater, container, false);
        khachDAO = AppDatabase.getInstance(getContext()).khachDao();
        // Khởi tạo Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        khachRef = firebaseDatabase.getReference("khach");
        // Khởi tạo Adapter
        khachAdapter = new KhachAdapter(khachList, getContext());
        RecyclerView recyclerView = binding.rcv;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(khachAdapter);

        // Lắng nghe dữ liệu từ Room (LiveData)
        observeKhachData();

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



}
