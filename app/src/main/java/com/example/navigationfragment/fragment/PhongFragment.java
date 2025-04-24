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

import com.example.navigationfragment.AppDatabase;
import com.example.navigationfragment.DAO.RoomDAO;
import com.example.navigationfragment.action.AddRoom;
import com.example.navigationfragment.adapter.RoomAdapter;
import com.example.navigationfragment.databinding.FragmentPhongBinding;
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

public class PhongFragment extends Fragment {
    private FragmentPhongBinding binding;
    private RoomAdapter roomAdapter;
    private List<RoomEntity> roomList = new ArrayList<>();
    private RoomDAO roomDAO;
    private DatabaseReference roomRef;
    private ExecutorService executorService;
    private ChildEventListener roomListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executorService = Executors.newSingleThreadExecutor();
        roomDAO = AppDatabase.getInstance(requireContext()).roomDao();
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

        // Quan sát dữ liệu từ Room
        observeRoomData();

        // Đồng bộ dữ liệu từ Firebase
        fetchRoomsFromFirebase();

        // Sự kiện thêm phòng
        binding.addfab.setOnClickListener(v -> openThemPhongActivity());

        binding.btnXoa.setOnClickListener(v -> {
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
                                        roomDAO.deleteAllRooms();
                                        requireActivity().runOnUiThread(this::clearAllRoomsUI);
                                    });
                                })
                                .addOnFailureListener(e -> Log.e("PhongFragment", "Lỗi xoá Firebase: " + e.getMessage()));
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });

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

    private void observeRoomData() {
        roomDAO.getAllRooms().observe(getViewLifecycleOwner(), rooms -> {
            if (rooms != null) {
                roomList.clear();
                roomList.addAll(rooms);
                roomAdapter.notifyDataSetChanged();
            }

        });
    }

    private void fetchRoomsFromFirebase() {

        roomListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RoomEntity newRoom = snapshot.getValue(RoomEntity.class);
                if (newRoom != null) {
                    executorService.execute(() -> {
                            roomDAO.insert(newRoom);
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RoomEntity updatedRoom = snapshot.getValue(RoomEntity.class);
                if (updatedRoom != null) {
                    executorService.execute(() -> {
                        try {
                            roomDAO.update(updatedRoom);
                            Log.d("PhongFragment", "Đã cập nhật phòng: " + updatedRoom.getSoPhong());
                        } catch (Exception e) {
                            Log.e("PhongFragment", "Lỗi khi xử lý onChildChanged: " + e.getMessage());
                        }
                    });
                }
            }


            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                RoomEntity deletedRoom = snapshot.getValue(RoomEntity.class);
                if (deletedRoom != null) {
                    String soPhong = deletedRoom.getSoPhong();
                    executorService.execute(() -> {
                        try {
                            roomDAO.deleteRoomBySoPhong(soPhong);
                            Log.d("PhongFragment", "Đã xóa phòng theo số phòng: " + soPhong);
                        } catch (Exception e) {
                            Log.e("PhongFragment", "Lỗi xóa phòng theo số phòng: " + e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Không xử lý
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseSync", "Lỗi ChildEventListener: " + error.getMessage());
            }
        };
        roomRef.addChildEventListener(roomListener);
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