package com.example.navigationfragment.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.navigationfragment.adapter.HoaDonAdapter;
import com.example.navigationfragment.databinding.FragmentHoadonBinding;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= FragmentHoadonBinding.inflate(inflater,container,false);

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

        binding.btnXoa.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        hoaDonRef.removeValue()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("PhongFragment", "Đã xoá tất cả trên Firebase");
                                        // Xoá dữ liệu trong Room DB
                                        executorService.execute(() -> {

                                            requireActivity().runOnUiThread(() -> {
                                                Toast.makeText(requireContext(), "Đã xoá hóa đơn", Toast.LENGTH_SHORT).show();
                                                // Có thể cập nhật lại UI ở đây nếu cần
                                            });
                                        });
                                    } else {
                                        Log.e("PhongFragment", "Xoá trên Firebase thất bại");
                                    }
                                })
                                .addOnFailureListener(e ->
                                        Log.e("PhongFragment", "Lỗi khi xoá Firebase: " + e.getMessage())
                                );
                    })
                    .setNegativeButton("Huỷ", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        return binding.getRoot();
    }
    private void observeHoaDonData(){

    }
    private void fetchHoaDonDataFromFirebase(){
        hoaDonListener= new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                HoaDonEntity newHoaDon = snapshot.getValue(HoaDonEntity.class);
                if (newHoaDon != null) {
                    executorService.execute(() -> {

                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                HoaDonEntity updatedHoaDon = snapshot.getValue(HoaDonEntity.class);
                if (updatedHoaDon != null) {
                    executorService.execute(() -> {
                    });
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                HoaDonEntity deletedHoaDon = snapshot.getValue(HoaDonEntity.class);
                if (deletedHoaDon != null) {
                    executorService.execute(() -> {
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
