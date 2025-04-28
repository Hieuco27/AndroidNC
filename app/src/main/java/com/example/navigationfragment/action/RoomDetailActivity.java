package com.example.navigationfragment.action;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.navigationfragment.databinding.ActivityXemttPhongBinding;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.RoomEntity;

public class RoomDetailActivity extends AppCompatActivity {

    private ActivityXemttPhongBinding binding;
    private RoomEntity room;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXemttPhongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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
        String trangThaiText = (room.getKhachId()==null) ? "Trạng thái: Đã thuê" : "Trạng thái: Còn trống";
        binding.edtTrangthai.setText(trangThaiText);


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

    }
}
