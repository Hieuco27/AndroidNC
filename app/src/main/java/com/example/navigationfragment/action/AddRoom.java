package com.example.navigationfragment.action;

import static android.app.PendingIntent.getActivity;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.navigationfragment.AppDatabase;
import com.example.navigationfragment.DAO.RoomDAO;
import com.example.navigationfragment.databinding.ActivityAddRoomBinding;
import com.example.navigationfragment.entity.RoomEntity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddRoom extends AppCompatActivity {
    ActivityAddRoomBinding binding;
    private RoomDAO roomDAO;
    private DatabaseReference roomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Room Database và Firebase
        roomDAO = AppDatabase.getInstance(this).roomDao();
        roomRef = FirebaseDatabase.getInstance().getReference("rooms");
        binding.btnThem.setOnClickListener(v -> {
            addRoom();
        });
        binding.btnHuy.setOnClickListener(v -> {
            finish();
        });
    }
    private void addRoom(){
        String soPhong = binding.edtSophong.getText().toString().trim();
        if (soPhong.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số phòng", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double giaPhong = Double.parseDouble(binding.edtGiaphong.getText().toString().trim());
            double giaDien = Double.parseDouble(binding.edtGiadien.getText().toString().trim());
            double giaNuoc = Double.parseDouble(binding.edtGianuoc.getText().toString().trim());
            double giaDichVu = Double.parseDouble(binding.edtGiadv.getText().toString().trim());

            RoomEntity room = new RoomEntity(soPhong, giaPhong, giaDien, giaNuoc, giaDichVu,false);

            new Thread(() -> {

                roomDAO.insert(room);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Thêm phòng thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                });

                roomRef.child(soPhong).setValue(room);
            }).start();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập đúng định dạng số", Toast.LENGTH_SHORT).show();
        }
    }

}


