package com.example.navigationfragment.action;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.navigationfragment.AppDatabase;
import com.example.navigationfragment.DAO.ContractDAO;
import com.example.navigationfragment.DAO.RoomDAO;
import com.example.navigationfragment.databinding.ActivityXemttPhongBinding;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.RoomEntity;
import com.example.navigationfragment.entity.KhachEntity;

public class RoomDetailActivity extends AppCompatActivity {
    private ActivityXemttPhongBinding binding;
    private RoomEntity room;
    private RoomDAO roomDAO;
    private ContractDAO contractDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXemttPhongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo DAO
        roomDAO = AppDatabase.getInstance(this).roomDao();
        contractDAO = AppDatabase.getInstance(this).contractDao();

        // Lấy dữ liệu từ Intent
        room = (RoomEntity) getIntent().getSerializableExtra("room");
        if (room == null) {
            Toast.makeText(this, "Không tìm thấy thông tin phòng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayRoomInfo();


        // Nút Hủy
        binding.btnHuy.setOnClickListener(v -> finish());
    }
    private void displayRoomInfo() {
        // Hiển thị thông tin phòng
        binding.edtSophong.setText(room.getSoPhong());
        binding.edtGiaphong.setText(String.valueOf(room.getGiaPhong()));
        binding.edtGiadien.setText(String.valueOf(room.getGiaDien()));
        binding.edtGianuoc.setText(String.valueOf(room.getGiaNuoc()));
        binding.edtGiadv.setText(String.valueOf(room.getGiaDichVu()));

        // Hiển thị trạng thái phòng
        String trangThaiText = room.isTrangThai() ? "Trạng thái: Đã thuê" : "Trạng thái: Còn trống";
        binding.edtTrangthai.setText(trangThaiText);

        contractDAO.getActiveContractByRoom(91).observe(this, contract -> {
            if (contract != null) {
                Log.d("DEBUG", "Lấy hợp đồng thành công: " + contract.toString());
                String tenantId = contract.getKhachId();

                if (tenantId == null || tenantId.isEmpty()) {
                    Log.e("ERROR", "Hợp đồng không có tenantId!");
                    binding.edtNguoithue.setText("Người thuê: Chưa có");
                    return;
                }

                AppDatabase.getInstance(this).khachDao().getTenantById(tenantId).observe(this, tenant -> {
                    if (tenant != null) {
                        Log.d("DEBUG", "Lấy khách thuê thành công: " + tenant.toString());
                        binding.edtNguoithue.setText("Người thuê: " + tenant.getTenKhach());
                    } else {
                        Log.e("ERROR", "Không tìm thấy khách thuê với ID: " + tenantId);
                        binding.edtNguoithue.setText("Người thuê: Chưa có");
                    }
                });
            } else {
                Log.e("ERROR", "Không tìm thấy hợp đồng đang hoạt động cho phòng ID: " + room.getId());
                binding.edtNguoithue.setText("Người thuê: Chưa có");
            }
        });
    }

}

