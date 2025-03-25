package com.example.navigationfragment.action;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.navigationfragment.AppDatabase;
import com.example.navigationfragment.DAO.RoomDAO;
import com.example.navigationfragment.databinding.ActivityAddRoomBinding;
import com.example.navigationfragment.entity.RoomEntity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddRoom extends AppCompatActivity {
    ActivityAddRoomBinding binding;
    private RoomDAO roomDAO;
    private DatabaseReference roomRef;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Room Database và Firebase
        roomDAO = AppDatabase.getInstance(this).roomDao();
        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        executorService= Executors.newSingleThreadExecutor();
        binding.btnThem.setOnClickListener(v -> {
            addRoom();
        });
        binding.btnHuy.setOnClickListener(v -> {
            finish();
        });
    }
    private void addRoom(){
        String soPhong = binding.edtSophong.getText().toString().trim();
        if (soPhong.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số phòng", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            double giaPhong = Double.parseDouble(binding.edtGiaphong.getText().toString().trim());
            double giaDien = Double.parseDouble(binding.edtGiadien.getText().toString().trim());
            double giaNuoc = Double.parseDouble(binding.edtGianuoc.getText().toString().trim());
            double giaDichVu = Double.parseDouble(binding.edtGiadv.getText().toString().trim());


            executorService.execute(() -> {
                RoomEntity existingRoom = roomDAO.getRoomBySoPhongSync(soPhong);
                if (existingRoom != null) {
                    runOnUiThread(() -> Toast.makeText(this, "Phòng đã tồn tại!", Toast.LENGTH_SHORT).show());
                    return;
                }
                String roomId = UUID.randomUUID().toString();
                RoomEntity room = new RoomEntity(roomId,soPhong, giaPhong, giaDien, giaNuoc, giaDichVu,false);
                roomDAO.insert(room);
                roomRef.child(roomId).setValue(room);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Thêm phòng thành công: " + soPhong, Toast.LENGTH_SHORT).show();
                    finish();
                });


            });

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập đúng định dạng số", Toast.LENGTH_SHORT).show();
        }
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


