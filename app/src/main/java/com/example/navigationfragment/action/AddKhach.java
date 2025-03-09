package com.example.navigationfragment.action;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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

        // Chạy nền để thêm dữ liệu vào Room
        executorService = Executors.newSingleThreadExecutor();

        binding.btnThem.setOnClickListener(v -> addKhach());
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

        if (!sdt.matches("^\\d{9}$")) {
            Toast.makeText(this, "Số điện thoại phải có đúng 9 chữ số", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cccd.matches("^\\d{12}$")) {
            Toast.makeText(this, "CCCD phải có đúng 12 chữ số", Toast.LENGTH_SHORT).show();
            return;
        }

        // Dùng getRoomBySoPhong() dưới dạng Single hoặc suspend để tránh mất dữ liệu khi Activity bị hủy
        executorService.execute(() -> {
            RoomEntity room = roomDAO.getRoomBySoPhongSync(soPhong);
            if (room == null) {
                runOnUiThread(() -> Toast.makeText(AddKhach.this, "Phòng không tồn tại!", Toast.LENGTH_SHORT).show());
                return;
            }

            // Tạo ID khách hàng
            String khachId = UUID.randomUUID().toString();
            KhachEntity khach = new KhachEntity(khachId, room.getId(), tenKhach, sdt, cccd);

            // Chèn vào Room
            khachDAO.insert(khach);

            // Cập nhật Firebase
            khachRef.child(khachId).setValue(khach)
                    .addOnSuccessListener(aVoid -> Log.d("DEBUG_FIREBASE", "Thêm Firebase thành công: " + khachId))
                    .addOnFailureListener(e -> Log.e("DEBUG_FIREBASE", "Lỗi Firebase: " + e.getMessage()));

            // Hiển thị thông báo trên UI
            runOnUiThread(() -> {
                Toast.makeText(AddKhach.this, "Thêm khách thành công!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }



}
