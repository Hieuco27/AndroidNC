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
import com.example.navigationfragment.entity.HoaDonDisplay;
import com.example.navigationfragment.entity.HoaDonEntity;
import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.RoomEntity;

import java.io.IOException;
import java.io.OutputStream;


public class HoaDonDetail extends AppCompatActivity {
    ActivityXemttHoadonBinding binding;
    private HoaDonDisplay hoaDonDisplay;
    private HoaDonEntity hoadon;
    private RoomEntity room;
    private KhachEntity khach;
    private Double tienDien;
    private Double tienNuoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXemttHoadonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hoaDonDisplay= (HoaDonDisplay) getIntent().getSerializableExtra("HOADONDISPLAY");
        room=hoaDonDisplay.getRoom();
        khach=hoaDonDisplay.getKhach();
        hoadon=hoaDonDisplay.getHoaDon();
        tienDien=hoadon.getSoDien()*room.getGiaDien();
        tienNuoc=hoadon.getSoNuoc()*room.getGiaNuoc();
        showDataToViews();
        // Bill gửi hóa đơn
        binding.btnSendMessage.setOnClickListener(v -> {
            String message = "Hóa đơn: " + hoadon.getTenHoaDon() + "\n"
                    + "Phòng: " + room.getSoPhong() + "\n"
                    + "Khách: " + khach.getTenKhach() + "\n"
                    + "Ngày tạo: " + hoadon.getNgayTao() + "\n"
                    + "Tiền phòng: " + formatCurrency(room.getGiaPhong()) + "\n"
                    + "Tiền dịch vụ: " + formatCurrency(room.getGiaDichVu()) + "\n"
                    + "Điện: " + hoadon.getSoDien() + " x " + formatCurrency(room.getGiaDien()) + " = " + formatCurrency(tienDien) + "\n"
                    + "Nước: " + hoadon.getSoNuoc() + " x " + formatCurrency(room.getGiaNuoc()) + " = " + formatCurrency(tienNuoc) + "\n"
                    + "Tổng tiền: " + formatCurrency(hoadon.getTongTien()) + "\n"
                    + "Trạng thái: " + (hoadon.isDaThanhToan() ? "Đã thanh toán" : "Chưa thanh toán") + "\n"
                    + "Ghi chú: " + hoadon.getGhiChu();

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



    // Hien thi du lieu len man hinh
    private void showDataToViews() {
        binding.tvTenHoaDon.setText("Hóa đơn: " + hoadon.getTenHoaDon());
        binding.tvSoPhong.setText("Phòng: " + room.getSoPhong());
        binding.tvTenKhach.setText("Khách: " + khach.getTenKhach());
//        binding.tvNgayTao.setText("Ngày tạo: " + hoadon.getNgayTao());

        binding.tvGiaPhong.setText("Tiền phòng: " + formatCurrency(room.getGiaPhong()));
        binding.tvTongGiaPhong.setText(formatCurrency(room.getGiaPhong()));

        binding.tvGiaDichVu.setText("Tiền dịch vụ: " + formatCurrency(room.getGiaDichVu()));
        binding.tvTongDichVu.setText(formatCurrency(room.getGiaDichVu()));

        binding.tvSoDien.setText("Điện: " + hoadon.getSoDien() + " x " + formatCurrency(room.getGiaDien()) + " = " + formatCurrency(tienDien));
        binding.tvSoNuoc.setText("Nước: " + hoadon.getSoNuoc() + " x " + formatCurrency(room.getGiaNuoc()) + " = " + formatCurrency(tienNuoc));

        binding.tvTongDien.setText(formatCurrency(tienDien));
        binding.tvTongNuoc.setText(formatCurrency(tienNuoc));

        binding.tvTongSoTien.setText("Tổng tiền: " + formatCurrency(hoadon.getTongTien()));

//        binding.tvTrangThai.setText(hoadon.getTrangThai() ? "Đã thanh toán" : "Chưa thanh toán");

        binding.tvGhiChu.setText("Ghi chú: " + hoadon.getGhiChu());

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

