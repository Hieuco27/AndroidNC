package com.example.navigationfragment.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.navigationfragment.action.AddRoom;
import com.example.navigationfragment.adapter.RoomAdapter;
import com.example.navigationfragment.databinding.FragmentPhongBinding;
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

public class PhongFragment extends Fragment {
    private FragmentPhongBinding binding;
    private RoomAdapter roomAdapter;
    private List<RoomEntity> roomList;
    private DatabaseReference roomRef;
    private ExecutorService executorService;
    private ChildEventListener roomListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executorService = Executors.newSingleThreadExecutor();
        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        roomList = new ArrayList<>();
        roomAdapter = new RoomAdapter(roomList, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPhongBinding.inflate(inflater, container, false);

        // Thiết lập RecyclerView hiển thị 2 cột
        binding.rcv.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rcv.setAdapter(roomAdapter);


        // Đồng bộ dữ liệu từ Firebase
        fetchRoomsFromFirebase();

        // Sự kiện thêm phòng
        binding.addfab.setOnClickListener(v -> openThemPhongActivity());

       /* binding.btnXoa.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Xác nhận xoá tất cả?")
                    .setMessage("Bạn có chắc chắn muốn xoá hết tất cả phòng không?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        // Xoá Firebase
                        roomRef.removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("PhongFragment", "Đã xoá tất cả trên Firebase");
                                    // Xoá Room DB
                                    executorService.execute(() -> {
                                        requireActivity().runOnUiThread(this::clearAllRoomsUI);
                                    });
                                })
                                .addOnFailureListener(e -> Log.e("PhongFragment", "Lỗi xoá Firebase: " + e.getMessage()));
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });*/

        return binding.getRoot();
    }
    private void clearAllRoomsUI() {
        roomList.clear();
        roomAdapter.notifyDataSetChanged();
        Log.d("PhongFragment", "Đã xoá toàn bộ UI");
    }

    private void openThemPhongActivity() {
        Intent intent = new Intent(getActivity(), AddRoom.class);
        addRoomLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> addRoomLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {

                }
            }
    );


    private void fetchRoomsFromFirebase() {

        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomList.clear(); // Xóa danh sách phòng cũ
                for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                    RoomEntity room = roomSnapshot.getValue(RoomEntity.class);
                    if (room != null) {
                        roomList.add(room); // Thêm phòng vào danh sách
                    }
                }
                roomAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView sau khi lấy dữ liệu
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
        if (roomListener != null) {
            roomRef.removeEventListener(roomListener);
        }
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        binding = null;
    }
}