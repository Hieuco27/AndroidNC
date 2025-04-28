package com.example.navigationfragment.action;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.navigationfragment.adapter.AssetAdapter;
import com.example.navigationfragment.databinding.ActivityAddRoomBinding;
import com.example.navigationfragment.entity.Asset;
import com.example.navigationfragment.entity.RoomEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddRoom extends AppCompatActivity {
    ActivityAddRoomBinding binding;
    private DatabaseReference roomRef,assetRef;
    private ExecutorService executorService;
    private AssetAdapter assetAdapter;
    private List<Asset> assetList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Room Database và Firebase
        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        assetRef=FirebaseDatabase.getInstance().getReference("assets");
        executorService= Executors.newSingleThreadExecutor();



        // Thiết lập adapter cho RecyclerView
        assetAdapter = new AssetAdapter(this, assetList);
        binding.rcvTaiSan.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvTaiSan.setAdapter(assetAdapter);

        binding.btnThem.setOnClickListener(v -> {
            addRoom();
        });
        binding.btnHuy.setOnClickListener(v -> {
            finish();
        });
        fetchAssets();
    }

    private void fetchAssets() {

        assetRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assetList.clear(); // Xóa danh sách phòng cũ
                for (DataSnapshot assetSnapshot : snapshot.getChildren()) {
                    Asset asset = assetSnapshot.getValue(Asset.class);
                    if (asset != null) {
                        asset.setId(assetSnapshot.getKey());
                        assetList.add(asset); // Thêm phòng vào danh sách
                    }
                }
                assetAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView sau khi lấy dữ liệu
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseSync", "Lỗi khi lấy dữ liệu từ Firebase: " + error.getMessage());
            }
        });
    }

    private void addRoom() {
        String soPhong = binding.edtSophong.getText().toString().trim();
        if (soPhong.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số phòng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra sự tồn tại của số phòng trong Firebase
        roomRef.orderByChild("soPhong").equalTo(soPhong).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Nếu số phòng đã tồn tại
                    Toast.makeText(getApplicationContext(), "Phòng này đã tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    // Nếu số phòng chưa tồn tại, thêm phòng mới
                    try {
                        double giaPhong = Double.parseDouble(binding.edtGiaphong.getText().toString().trim());
                        double giaDien = Double.parseDouble(binding.edtGiadien.getText().toString().trim());
                        double giaNuoc = Double.parseDouble(binding.edtGianuoc.getText().toString().trim());
                        double giaDichVu = Double.parseDouble(binding.edtGiadv.getText().toString().trim());

                        executorService.execute(() -> {
                            String roomId = roomRef.push().getKey();
                            Map<String, Boolean> selectedAsset = new HashMap<>();
                            selectedAsset = assetAdapter.getSelectedAssets();

                            RoomEntity room = new RoomEntity(roomId, soPhong, giaPhong, giaDien, giaNuoc, giaDichVu, false);
                            room.setAssets(selectedAsset);
                            roomRef.child(roomId).setValue(room);

                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), "Thêm phòng thành công: " + soPhong, Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        });
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Vui lòng nhập đúng định dạng số", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Lỗi khi kiểm tra phòng: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng ExecutorService để tránh rò rỉ tài nguyên
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}


