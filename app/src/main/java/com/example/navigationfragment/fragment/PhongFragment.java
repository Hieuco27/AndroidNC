package com.example.navigationfragment.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationfragment.AppDatabase;
import com.example.navigationfragment.DAO.RoomDAO;
import com.example.navigationfragment.R;
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

public class PhongFragment extends Fragment {
    private FragmentPhongBinding binding;
    private RoomAdapter roomAdapter;
    private List<RoomEntity> roomList = new ArrayList<>();
    private static final int REQUEST_CODE_ADD_ROOM = 1;
    private  RoomDAO roomDAO;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference roomRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPhongBinding.inflate(inflater, container, false);

        roomDAO = AppDatabase.getInstance(getContext()).roomDao();
        //Khởi tạo firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        roomRef = firebaseDatabase.getReference("rooms");

        // Khởi tạo adapter
        roomAdapter = new RoomAdapter(roomList, getContext());

        // Thiết lập RecyclerView hiển thị 2 cột
        int numberOfColumns = 2;
        RecyclerView recyclerView = binding.rcv;
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), numberOfColumns));
        recyclerView.setAdapter(roomAdapter);
        // Lắng nghe dữ liệu từ Room (LiveData)
        observeRoomData();

        // Đồng bộ dữ liệu từ Firebase về Room khi mở app
        syncFirebaseToRoom();



        binding.addfab.setOnClickListener(v -> {
            openThemPhongActivity();
        });
        return binding.getRoot();
    }

    private  void openThemPhongActivity(){
        Intent intent= new Intent(getActivity(), AddRoom.class);
        addRoomLauncher.launch(intent);

    }
    // Đăng ký ActivityResultLauncher để nhận dữ liệu từ AddRoom
    private final ActivityResultLauncher<Intent> addRoomLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Không cần gọi loadRoomDataFromDatabase() vì observeRoomData() đã tự động cập nhật
                    Log.d("PhongFragment", "Phòng mới đã được thêm!");
                }
            }
    );

    private void observeRoomData() {
        roomDAO.getAllRooms().observe(getViewLifecycleOwner(), rooms -> {
            roomList.clear();
            roomList.addAll(rooms);
            roomAdapter.notifyDataSetChanged();
        });
    }

    private void syncFirebaseToRoom() {
        roomRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RoomEntity room = snapshot.getValue(RoomEntity.class);
                if (room != null) {
                    new Thread(() -> {
                        // Thêm vào Room nếu chưa có
                        roomDAO.insert(room);

                        // Cập nhật UI trên Main Thread
                        requireActivity().runOnUiThread(() -> {
                            roomList.add(room);
                            roomAdapter.notifyDataSetChanged();
                        });
                    }).start();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RoomEntity updatedRoom = snapshot.getValue(RoomEntity.class);
                if (updatedRoom != null) {
                    new Thread(() -> {
                        // Update vào Room DB
                        roomDAO.update(updatedRoom);

                        // Cập nhật UI trên Main Thread
                        requireActivity().runOnUiThread(() -> {
                            // Tìm và update item trong roomList
                            for (int i = 0; i < roomList.size(); i++) {
                                if (roomList.get(i).getId() == updatedRoom.getId()) {
                                    roomList.set(i, updatedRoom);
                                    break;
                                }
                            }
                            roomAdapter.notifyDataSetChanged();
                        });
                    }).start();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                RoomEntity deletedRoom = snapshot.getValue(RoomEntity.class);
                if (deletedRoom != null) {
                    new Thread(() -> {
                        // Xóa khỏi Room DB
                        roomDAO.delete(deletedRoom);

                        // Xóa khỏi danh sách và cập nhật UI
                        requireActivity().runOnUiThread(() -> {
                            for (int i = 0; i < roomList.size(); i++) {
                                if (roomList.get(i).getId() == deletedRoom.getId()) {
                                    roomList.remove(i);
                                    break;
                                }
                            }
                            roomAdapter.notifyDataSetChanged();
                        });
                    }).start();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Trường hợp này ít dùng, Firebase báo khi 1 node thay đổi vị trí
                // Nếu không sắp xếp theo key/priority thì có thể bỏ qua
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseSync", "Lỗi ChildEventListener: " + error.getMessage());
            }
        });
    }



}

