package com.example.navigationfragment.action;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.navigationfragment.AppDatabase;
import com.example.navigationfragment.DAO.KhachDAO;
import com.example.navigationfragment.DAO.RoomDAO;
import com.example.navigationfragment.databinding.ActivityAddKhachBinding;
import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.RoomEntity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddKhach extends AppCompatActivity {
    private ActivityAddKhachBinding binding;
    private KhachDAO khachDAO;
    private RoomDAO roomDAO;
    private DatabaseReference khachRef;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddKhachBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo DAO
        khachDAO = AppDatabase.getInstance(this).khachDao();
        roomDAO = AppDatabase.getInstance(this).roomDao();
        khachRef = FirebaseDatabase.getInstance().getReference("khach");


        // Khởi tạo ExecutorService
        executorService = Executors.newSingleThreadExecutor();
        // Lấy dữ liệu số phòng từ Intent
        String soPhong = getIntent().getStringExtra("SO_PHONG");

        if (soPhong != null) {
            binding.edtSophong.setText(soPhong);
            binding.edtSophong.setEnabled(false);
        }
        binding.btnThem.setOnClickListener(v -> addKhach());
        binding.btnHuy.setOnClickListener(v -> {
            finish();
        });
    }

    private void addKhach() {
        String soPhong = binding.edtSophong.getText().toString().trim();
        String tenKhach = binding.edtTenkhach.getText().toString().trim();
        String sdt = binding.edtSdt.getText().toString().trim();
        String cccd = binding.edtCccd.getText().toString().trim();

        if (tenKhach.isEmpty() || sdt.isEmpty() || cccd.isEmpty() || soPhong.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

       /* if (!sdt.matches("^\\d{10}$")) {
            Toast.makeText(this, "Số điện thoại phải có đúng 10 chữ số", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cccd.matches("^\\d{12}$")) {
            Toast.makeText(this, "CCCD phải có đúng 12 chữ số", Toast.LENGTH_SHORT).show();
            return;
        }*/

        // Thêm dữ liệu trong luồng nền
        executorService.execute(() -> {
            RoomEntity room = roomDAO.getRoomBySoPhongSync(soPhong);
            if (room == null) {
                runOnUiThread(() -> Toast.makeText(this, "Phòng không tồn tại!", Toast.LENGTH_SHORT).show());
                return;
            }

            // Tạo khachId bằng UUID
            String khachId = UUID.randomUUID().toString();
            KhachEntity khach = new KhachEntity(khachId, room.getId(), tenKhach, sdt, cccd);
            khachDAO.insert(khach);
            khachRef.child(khachId).setValue(khach);

            runOnUiThread(() -> {
                Toast.makeText(this, "Thêm khách thành công: " + tenKhach, Toast.LENGTH_SHORT).show();
                finish();
            });
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
