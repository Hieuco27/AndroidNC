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
import com.example.navigationfragment.entity.ContractDisplay;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.HoaDonEntity;
import com.example.navigationfragment.entity.HoaDonDisplay;
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

public class HoaDonFragment extends Fragment {
    FragmentHoadonBinding binding;
    private HoaDonAdapter hoaDonAdapter;
    private List<HoaDonDisplay> hoaDonList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference hoaDonRef,khachRef,roomRef;
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
        khachRef = firebaseDatabase.getReference("khach");
        roomRef=firebaseDatabase.getReference("rooms");
        //khởi ta adapter
        hoaDonAdapter = new HoaDonAdapter(hoaDonList, getContext());
        binding.rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcv.setAdapter(hoaDonAdapter);

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

    private void fetchHoaDonDataFromFirebase(){
        hoaDonRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hoaDonList.clear(); // Xóa danh sách phòng cũ

                for (DataSnapshot hoadonSnapshot : snapshot.getChildren()) {
                    HoaDonEntity hoadon = hoadonSnapshot.getValue(HoaDonEntity.class);
                    if (hoadon != null) {
                        HoaDonDisplay hoaDonDisplay = new HoaDonDisplay();
                        hoaDonDisplay.setHoaDon(hoadon);

                        // Lấy dữ liệu khách và phòng đồng thời
                        Task<DataSnapshot> khachTask = khachRef.child(hoadon.getKhachId()).get();
                        Task<DataSnapshot> roomTask = roomRef.child(hoadon.getRoomId()).get();

                        // Khi cả 2 tác vụ đều hoàn thành
                        Tasks.whenAllSuccess(khachTask, roomTask)
                                .addOnSuccessListener(results -> {
                                    // Lấy dữ liệu khách và phòng từ kết quả trả về
                                    DataSnapshot khachSnapshot = (DataSnapshot) results.get(0);
                                    DataSnapshot roomSnapshot = (DataSnapshot) results.get(1);
                                    KhachEntity khachEntity = khachSnapshot.getValue(KhachEntity.class);
                                    RoomEntity roomEntity = roomSnapshot.getValue(RoomEntity.class);
                                    hoaDonDisplay.setRoom(roomEntity);
                                    hoaDonDisplay.setKhach(khachEntity);
                                    // Thêm contractDisplay vào danh sách và cập nhật giao diện
                                    hoaDonList.add(hoaDonDisplay);
                                    if(hoaDonList.size()==snapshot.getChildrenCount()){
                                        hoaDonAdapter.updateData(hoaDonList);}
                                })
                                .addOnFailureListener(e -> {
                                    // Xử lý khi có lỗi
                                    Log.e("FirebaseSync", "Lỗi khi lấy dữ liệu khách hoặc phòng: " + e.getMessage());
                                    hoaDonDisplay.setKhach(null);
                                    hoaDonDisplay.setRoom(null);
                                    hoaDonList.add(hoaDonDisplay);
                                    if(hoaDonList.size()==snapshot.getChildrenCount()){
                                        hoaDonAdapter.updateData(hoaDonList);}
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
        if (hoaDonListener != null) {
            hoaDonRef.removeEventListener(hoaDonListener);
        }
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
