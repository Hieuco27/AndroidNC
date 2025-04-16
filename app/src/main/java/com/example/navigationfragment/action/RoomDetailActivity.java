package com.example.navigationfragment.action;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.navigationfragment.AppDatabase;
import com.example.navigationfragment.DAO.ContractDAO;
import com.example.navigationfragment.DAO.KhachDAO;
import com.example.navigationfragment.DAO.RoomDAO;
import com.example.navigationfragment.databinding.ActivityXemttPhongBinding;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.RoomEntity;

public class RoomDetailActivity extends AppCompatActivity {

    private ActivityXemttPhongBinding binding;
    private RoomEntity room;

    private RoomDAO roomDAO;
    private ContractDAO contractDAO;
    private KhachDAO khachDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXemttPhongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo DAO
        roomDAO = AppDatabase.getInstance(this).roomDao();
        contractDAO = AppDatabase.getInstance(this).contractDao();
        khachDAO = AppDatabase.getInstance(this).khachDao();

        // Lấy dữ liệu từ Intent
        room = (RoomEntity) getIntent().getSerializableExtra("room");

        if (room == null) {
            Toast.makeText(this, "Không tìm thấy thông tin phòng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        /*// Hiển thị thông tin phòng ban đầu
        displayRoomInfo();*/

        // Xử lý nút Hủy
        binding.btnHuy.setOnClickListener(v -> finish());
    }

    private void displayRoomInfo() {
        if (room == null) {
            Toast.makeText(this, "Thông tin phòng không khả dụng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị thông tin chi tiết phòng
        binding.edtSophong.setText(room.getSoPhong());
        binding.edtGiaphong.setText(String.valueOf(room.getGiaPhong()));
        binding.edtGiadien.setText(String.valueOf(room.getGiaDien()));
        binding.edtGianuoc.setText(String.valueOf(room.getGiaNuoc()));
        binding.edtGiadv.setText(String.valueOf(room.getGiaDichVu()));

        // Hiển thị trạng thái phòng
        String trangThaiText = room.isTrangThai() ? "Trạng thái: Đã thuê" : "Trạng thái: Còn trống";
        binding.edtTrangthai.setText(trangThaiText);

        // Lấy hợp đồng đang hoạt động của phòng này
        contractDAO.getActiveContractByRoom(room.getId()).observe(this, new Observer<ContractEntity>() {
            @Override
            public void onChanged(ContractEntity contract) {
                if (contract != null) {
                    String tenantId = contract.getKhachId();

                    if (tenantId == null || tenantId.isEmpty()) {
                        binding.edtNguoithue.setText("Người thuê: Chưa có");
                        return;
                    }

                    // Tìm thông tin khách thuê
                    khachDAO.getTenantById(tenantId).observe(RoomDetailActivity.this, new Observer<KhachEntity>() {
                        @Override
                        public void onChanged(KhachEntity tenant) {
                            if (tenant != null) {
                                binding.edtNguoithue.setText("Người thuê: " + tenant.getTenKhach());
                            } else {
                                binding.edtNguoithue.setText("Người thuê: Chưa có");
                            }
                        }
                    });

                } else {
                    binding.edtNguoithue.setText("Người thuê: Chưa có");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (room == null) {
            Toast.makeText(this, "Không có thông tin phòng để cập nhật!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Quan sát sự thay đổi của phòng để cập nhật UI
        roomDAO.getRoomById(room.getId()).observe(this, updatedRoom -> {
            if (updatedRoom != null) {
                room = updatedRoom;
                displayRoomInfo(); // Chỉ hiển thị sau khi lấy đúng room từ Room DB
            } else {
                Toast.makeText(RoomDetailActivity.this, "Không thể cập nhật thông tin phòng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
