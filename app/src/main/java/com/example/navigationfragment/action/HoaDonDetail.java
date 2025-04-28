package com.example.navigationfragment.action;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.navigationfragment.databinding.ActivityXemttHoadonBinding;

import java.io.IOException;
import java.io.OutputStream;


public class HoaDonDetail extends AppCompatActivity {
    ActivityXemttHoadonBinding binding;
    // Biến tạm để lưu dữ liệu từ Intent
    private String tenHoaDon, soPhong, tenKhach, ngayTao, ghiChu;
    private int soNguoi, soDien, soNuoc;
    private double giaPhong, giaDichVu, giaDien, giaNuoc, tienNuoc, tienDien, tongTien;
    private boolean daThanhToan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXemttHoadonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        getDataFromIntent();
        showDataToViews();
        // Bill gửi hóa đơn
        binding.btnSendMessage.setOnClickListener(v -> {
            String message = "Hóa đơn: " + tenHoaDon + "\n"
                    + "Phòng: " + soPhong + "\n"
                    + "Khách: " + tenKhach + "\n"
                    + "Ngày tạo: " + ngayTao + "\n"
                    + "Tiền phòng: " + formatCurrency(giaPhong) + "\n"
                    + "Tiền dịch vụ: " + formatCurrency(giaDichVu) + "\n"
                    + "Điện: " + soDien + " x " + formatCurrency(giaDien) + " = " + formatCurrency(tienDien) + "\n"
                    + "Nước: " + soNuoc + " x " + formatCurrency(giaNuoc) + " = " + formatCurrency(tienNuoc) + "\n"
                    + "Tổng tiền: " + formatCurrency(tongTien) + "\n"
                    + "Trạng thái: " + (daThanhToan ? "Đã thanh toán" : "Chưa thanh toán") + "\n"
                    + "Ghi chú: " + ghiChu;

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(intent, "Chia sẻ hóa đơn qua..."));
        });

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        binding.btnSaveImg.setOnClickListener(v -> {
            binding.getRoot().setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(binding.getRoot().getDrawingCache());
            binding.getRoot().setDrawingCacheEnabled(false);
            saveImageToGallery(bitmap);
        });
        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        tenHoaDon = intent.getStringExtra("tenHoaDon");
        soPhong = intent.getStringExtra("soPhong");
        tenKhach = intent.getStringExtra("tenKhach");
        ngayTao = intent.getStringExtra("ngayTao");
        ghiChu = intent.getStringExtra("ghiChu");

        soNguoi = intent.getIntExtra("soNguoi", 0);
        giaPhong = intent.getDoubleExtra("giaPhong", 0);
        giaDichVu = intent.getDoubleExtra("giaDichVu", 0);
        soDien = intent.getIntExtra("soDien", 0);
        soNuoc = intent.getIntExtra("soNuoc", 0);
        giaDien = intent.getDoubleExtra("giaDien", 0);
        giaNuoc = intent.getDoubleExtra("giaNuoc", 0
        );

        tienNuoc = intent.getDoubleExtra("tienNuoc", 0);
        tienDien = intent.getDoubleExtra("tienDien", 0);
        tongTien = giaPhong + giaDichVu + tienDien + tienNuoc;
        daThanhToan = intent.getBooleanExtra("daThanhToan", false);
    }

    // Hien thi du lieu len man hinh
    private void showDataToViews() {
        binding.tvTenHoaDon.setText(tenHoaDon);
        binding.tvSoPhong.setText("Số phòng: " + soPhong);
        binding.tvTenKhach.setText("Tên khách: " + tenKhach);
        binding.tvSoNguoi.setText("Số người: " + soNguoi);

        binding.tvGiaPhong.setText(formatCurrency(giaPhong));
        binding.tvTongGiaPhong.setText(formatCurrency(giaPhong));
        binding.tvGiaDichVu.setText(formatCurrency(giaDichVu));
        binding.tvTongDichVu.setText(formatCurrency(giaDichVu));

        binding.tvSoDien.setText(String.valueOf(soDien));
        binding.tvSoNuoc.setText(String.valueOf(soNuoc));

        binding.tvGiaDien.setText(formatCurrency(giaDien));
        binding.tvGiaNuoc.setText(formatCurrency(giaNuoc));

        binding.tvTongDien.setText(formatCurrency(tienDien));
        binding.tvTongNuoc.setText(formatCurrency(tienNuoc));
        binding.tvTongSoTien.setText(formatCurrency(tongTien));

        binding.tvGhiChu.setText("Nội Dung : " + ghiChu);
    }

    private String formatCurrency(double value) {
        return String.format("%,.0fđ", value);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Đã cấp quyền ghi bộ nhớ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền để lưu ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveImageToGallery(Bitmap bitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "hoadon_" + System.currentTimeMillis() + ".png");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            // API 29+ dùng RELATIVE_PATH
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/HoaDonApp");
        } else {
            // API < 29 không hỗ trợ RELATIVE_PATH
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            values.put(MediaStore.Images.Media.DATA, path + "/hoadon_" + System.currentTimeMillis() + ".png");
        }

        ContentResolver resolver = getContentResolver();
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        if (uri != null) {
            try {
                OutputStream outputStream = resolver.openOutputStream(uri);
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.close();
                    Toast.makeText(this, "Đã lưu ảnh vào thư viện", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Không thể mở stream để lưu ảnh", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Không thể tạo Uri để lưu ảnh (có thể thiếu quyền)", Toast.LENGTH_SHORT).show();
        }
    }


}

