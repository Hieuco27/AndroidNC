package com.example.navigationfragment.action;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.navigationfragment.AppDatabase;
import com.example.navigationfragment.DAO.KhachDAO;
import com.example.navigationfragment.DAO.RoomDAO;
import com.example.navigationfragment.databinding.ActivityXemttKhachthueBinding;
import com.example.navigationfragment.entity.KhachEntity;

import java.util.concurrent.Executors;

public class KhachDetail   extends AppCompatActivity {
    ActivityXemttKhachthueBinding binding;
    private KhachEntity khach;
    private KhachDAO khachDAO;
    private RoomDAO roomDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXemttKhachthueBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        // Khởi tạo DAO
        khachDAO = AppDatabase.getInstance(this).khachDao();
        roomDAO = AppDatabase.getInstance(this).roomDao();
        // Lấy dữ liệu từ Intent
        khach = (KhachEntity) getIntent().getSerializableExtra("khach");
        if (khach == null) {
            Toast.makeText(this, "Không tìm thấy thông tin khách thuê!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Hiển thị thông tin khách thue
        displayKhachInfo();

        binding.btnDong.setOnClickListener(v -> {
            finish();
        });
    }
    private void displayKhachInfo() {
        if (khach == null) {
            Toast.makeText(this, "Thông tin khách thuê không khả dụng!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Hiển thị thông tin cơ bản
        binding.tvTenKhach.setText( "Tên : "+ khach.getTenKhach());
        binding.tvSdt.setText("SĐT : "+khach.getSdt());
        binding.tvCccd.setText( "CCCD : "+ khach.getCccd());

        // Truy vấn Room ở background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            String soPhong = roomDAO.getRoomIdBySoPhong(khach.getRoomId());

            runOnUiThread(() -> {
                binding.tvSoPhong.setText(soPhong != null ? soPhong : "Không xác định");
            });
        });
    }

}
