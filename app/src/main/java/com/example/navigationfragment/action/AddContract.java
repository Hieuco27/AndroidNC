package com.example.navigationfragment.action;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.navigationfragment.databinding.ActivityAddContractBinding;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.RoomEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddContract extends AppCompatActivity {
    private ActivityAddContractBinding binding;
    private DatabaseReference contractRef;
    private DatabaseReference khachRef;
    private ExecutorService executorService;
    private ArrayAdapter<String> khachAdapter;
    private List<KhachEntity> khachList = new ArrayList<>();
    private String idPhong;
    private String idKhach;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddContractBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        contractRef = FirebaseDatabase.getInstance().getReference("contracts");
        khachRef = FirebaseDatabase.getInstance().getReference("khach");
        executorService = Executors.newSingleThreadExecutor();

        idPhong = getIntent().getStringExtra("ID_PHONG");
        idKhach=getIntent().getStringExtra("ID_KHACH");
        String soPhong = getIntent().getStringExtra("SO_PHONG");
        if (idPhong != null) {
            binding.edtSoPhong.setText(soPhong);
        }
          fetchKhachInfo();
        binding.btnThemcontract.setOnClickListener(v -> addContract());
        binding.btnHuy.setOnClickListener(v -> finish());
        binding.edtStartdate.setOnClickListener(v -> showDatePicker(true));
        binding.edtEnddate.setOnClickListener(v -> showDatePicker(false));
    }

    private void fetchKhachInfo(){

        khachRef.child(idKhach).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                binding.tvKhachName.setText(dataSnapshot.child("tenKhach").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to load room", databaseError.toException());
            }
        });

    }


    private void addContract() {

        String startDate = binding.edtStartdate.getText().toString().trim();
        String endDate = binding.edtEnddate.getText().toString().trim();
        String numberOfGuestsStr = binding.edtNguoio.getText().toString().trim();
        String numberOfCarsStr = binding.edtCar.getText().toString().trim();

        if (idPhong.isEmpty() || startDate.isEmpty() || endDate.isEmpty() ||
                numberOfGuestsStr.isEmpty() || numberOfCarsStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int numberOfGuests;
        int numberOfCars;
        try {
            numberOfGuests = Integer.parseInt(numberOfGuestsStr);
            numberOfCars = Integer.parseInt(numberOfCarsStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số người ở và số xe phải là số!", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            
            String contractId=contractRef.push().getKey();
            ContractEntity contract = new ContractEntity(
                    contractId, // Tạo contractId
                   idPhong,
                    idKhach,
                    startDate,
                    endDate,
                    numberOfGuests,
                    numberOfCars,
                    0.0, // totalAmount, bạn có thể thêm logic tính sau
                    true  // status
            );
            contractRef.child(contract.getContractId()).setValue(contract);
            runOnUiThread(() -> {
                Toast.makeText(this, "Thêm hợp đồng thành công", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }



    private void showDatePicker(boolean isStartDate) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    if (isStartDate) {
                        binding.edtStartdate.setText(date);
                    } else {
                        binding.edtEnddate.setText(date);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}



