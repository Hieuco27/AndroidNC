package com.example.navigationfragment.action;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.navigationfragment.databinding.ActivityAddHoadonBinding;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.HoaDonEntity;
import com.example.navigationfragment.entity.RoomEntity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class AddHoaDon extends AppCompatActivity {
    private ActivityAddHoadonBinding binding;

    private DatabaseReference hoaDonRef;
    private String soPhong;
    private double giaPhong = 0;
    private double giaDien = 0;
    private double giaNuoc = 0;
    private double giaDichVu=0;
    private String roomId="";
    private String khachId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddHoadonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        roomId=getIntent().getStringExtra("ID_PHONG");

        hoaDonRef = FirebaseDatabase.getInstance().getReference("hoadon");

        // Nhận số phòng từ Intent
        soPhong = getIntent().getStringExtra("SO_PHONG");
        khachId=getIntent().getStringExtra("KHACH_ID");
        if (soPhong != null) {
            binding.tvPhong.setText("Phòng: " + soPhong);
            binding.tvPhong.setEnabled(false);
        }
        // Chọn ngày
        binding.edtNgayTao.setOnClickListener(v -> showDatePicker());
        // Tính tổng
        binding.btnTinhTong.setOnClickListener(v -> tinhTongTien());
        // Lưu hóa đơn
        binding.btnThem.setOnClickListener(v -> addHoaDon());
        // Hủy
        binding.btnHuy.setOnClickListener(v -> finish());
        binding.edtSodien.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tinhTongTien();
            }
        });

        binding.edtSonuoc.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tinhTongTien();
            }
        });

    }


    private void tinhTongTien() {
        try {
            int soDien = Integer.parseInt(binding.edtSodien.getText().toString());
            int soNuoc = Integer.parseInt(binding.edtSonuoc.getText().toString());

            double tienDichVu= giaDichVu;
            double tienDien = giaDien * soDien;
            double tienNuoc = giaNuoc * soNuoc;
            double thanhTien = giaPhong + tienDien + tienNuoc + tienDichVu;

            // Tính tổng tiền
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formatted = formatter.format(thanhTien);
            binding.tvtTongTien.setText("Tổng tiền: " + formatted + " VNĐ");
        } catch (Exception e) {
            Toast.makeText(this, "Vui lòng nhập đúng số điện nước", Toast.LENGTH_SHORT).show();
        }
    }

    private void addHoaDon() {
        String tenHoaDon = binding.edtTenhoadon.getText().toString().trim();
        String ngayTao = binding.edtNgayTao.getText().toString().trim();
        String ghiChu = binding.edtGhichu.getText().toString().trim();
        boolean daThanhToan = binding.checkbox.isChecked();

        if (tenHoaDon.isEmpty() || ngayTao.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ tên hóa đơn và ngày tạo", Toast.LENGTH_SHORT).show();
            return;
        }

        int soDien = Integer.parseInt(binding.edtSodien.getText().toString());
        int soNuoc = Integer.parseInt(binding.edtSonuoc.getText().toString());

        double tienDien = giaDien * soDien;
        double tienNuoc = giaNuoc * soNuoc;
        double tongTien = giaPhong + tienDien + tienNuoc;

        String idHoaDon = UUID.randomUUID().toString();

        HoaDonEntity hoaDon = new HoaDonEntity();
        hoaDon.setHoaDonId(idHoaDon);
        hoaDon.setRoomId(roomId);
        hoaDon.setSoPhong(soPhong);
        hoaDon.setTenHoaDon(tenHoaDon);
        hoaDon.setNgayTao(ngayTao);
        hoaDon.setSoDien(soDien);
        hoaDon.setSoNuoc(soNuoc);
        hoaDon.setGhiChu(ghiChu);
        hoaDon.setTongTien(tongTien);
        hoaDon.setDaThanhToan(daThanhToan);
        hoaDon.setKhachId(khachId);
        // Lưu vào Room và Firebase
        Executors.newSingleThreadExecutor().execute(() -> {
            hoaDonRef.child(idHoaDon).setValue(hoaDon);

            runOnUiThread(() -> {
                Toast.makeText(this, "Thêm hoá đơn thành công", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, y, m, d) -> binding.edtNgayTao.setText(d + "/" + (m + 1) + "/" + y),
                year, month, day);
        datePickerDialog.show();
    }
}


