package com.example.navigationfragment.action;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.navigationfragment.databinding.ActivityXemttHoadonBinding;


public class HoaDonDetail extends AppCompatActivity {

    ActivityXemttHoadonBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXemttHoadonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Nhận dữ liệu từ Intent
        String hoaDonId = getIntent().getStringExtra("hoaDonId");
        String tenHoaDon = getIntent().getStringExtra("tenHoaDon");
        String soPhong = getIntent().getStringExtra("soPhong");
        String tenKhach = getIntent().getStringExtra("tenKhach");
        int soNguoi = getIntent().getIntExtra("soNguoi", 0);
        String ngayTao = getIntent().getStringExtra("ngayTao");
        double giaPhong = getIntent().getDoubleExtra("giaPhong", 0);
        double giaDichVu = getIntent().getDoubleExtra("giaDichVu", 0);

        int soDien = getIntent().getIntExtra("soDien", 0);
        int soNuoc = getIntent().getIntExtra("soNuoc", 0);
        double giaDien = getIntent().getDoubleExtra("giaDien", 0);
        double giaNuoc = getIntent().getDoubleExtra("giaNuoc", 0);

        double tienNuoc = getIntent().getDoubleExtra("tienNuoc", 0);
        double tienDien = getIntent().getDoubleExtra("tienDien", 0);

        double tongTien = getIntent().getDoubleExtra("tongTien", 0);
        boolean daThanhToan = getIntent().getBooleanExtra("daThanhToan", false);
        String ghiChu = getIntent().getStringExtra("ghiChu");


        // Tính tiền điện nước
        double tienDienNuoc = (soDien * giaDien) + (soNuoc * giaNuoc);
        double tienDichVu = giaDichVu;
        double tienPhong = giaPhong;

        // Gán dữ liệu lên View bằng binding
        binding.tvTenHoaDon.setText(tenHoaDon);
        binding.tvSoPhong.setText("Số phòng: " + soPhong);
        binding.tvTenKhach.setText("Tên khách: " + tenKhach);
        binding.tvSoNguoi.setText("Số người: " + soNguoi);

        binding.tvGiaPhong.setText(String.format("%,.0fđ", giaPhong));
        binding.tvTongGiaPhong.setText(String.format("%,.0fđ", giaPhong));
        binding.tvGiaDichVu.setText(String.format("%,.0fđ",giaDichVu));
        binding.tvTongDichVu.setText(String.format("%,.0fđ",giaDichVu));

        // Dùng số điện và số nước để tính tiền điện nước
        binding.tvSoDien.setText("" + soDien);
        binding.tvSoNuoc.setText("" + soNuoc);

        // Hiển thị giá điện giá nước
        binding.tvGiaNuoc.setText(String.format("%,.0fđ", giaNuoc));
        binding.tvGiaDien.setText(String.format("%,.0fđ", giaDien));

        // tính thành tiền điện và nước
        binding.tvTongNuoc.setText(String.format("%,.0fđ", tienNuoc));
        binding.tvTongDien.setText(String.format("%,.0fđ", tienDien));
        //Tổng Thanhf tiền tất cả
        binding.tvTongSoTien.setText(String.format("%,.0fđ", tongTien));
        binding.tvGhiChu.setText("Nội Dung :" + ghiChu);







    }
}
