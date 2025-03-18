package com.example.navigationfragment.action;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.navigationfragment.AppDatabase;
import com.example.navigationfragment.DAO.ContractDAO;
import com.example.navigationfragment.databinding.ActivityAddHopdongBinding;
import com.example.navigationfragment.entity.ContractEntity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddContract extends AppCompatActivity {

    ActivityAddHopdongBinding binding;
    private ContractDAO contractDAO;
    private DatabaseReference contractRef;
    private int day,month,year,hour,minutes;


    @Override
    protected  void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        binding = ActivityAddHopdongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        contractDAO = AppDatabase.getInstance(this).contractDao();
        contractRef = FirebaseDatabase.getInstance().getReference("contracts");

        binding.btnThemcontract.setOnClickListener(v -> {
            addContract();
        });
        binding.btnHuy.setOnClickListener(v -> {
            finish();
            });

        binding.edtStartdate.setOnClickListener(v -> {
            startdate();
        });
        binding.edtEnddate.setOnClickListener(v -> {
            enddate();
        });
    }
    private void addContract(){
        String roomId = binding.edtSophong.getText().toString().trim();
        String khachId = binding.edtTenkhach.getText().toString().trim();
        String startDate = binding.edtStartdate.getText().toString().trim();
        String endDate = binding.edtEnddate.getText().toString().trim();
        String numberOfGuests = binding.edtNguoio.getText().toString().trim();
        String numberOfCars = binding.edtCar.getText().toString().trim();
        String deposit = binding.edtCoc.getText().toString().trim();

        if (roomId.isEmpty() || khachId.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || numberOfGuests.isEmpty() || numberOfCars.isEmpty() || deposit.isEmpty()) {
            binding.edtSophong.setError("Vui lòng nhập số phòng");
        }
        try{
            int numberOfGuestsInt = Integer.parseInt(numberOfGuests);
            int numberOfCarsInt = Integer.parseInt(numberOfCars);
            double depositDouble = Double.parseDouble(deposit);

            // Tạo đối tượng hợp đồng (ContractEntity)
            ContractEntity contract = new ContractEntity();
            contract.setRoomId(Integer.parseInt(roomId));
            contract.setKhachId(khachId);
            contract.setStartDate(startDate);
            contract.setEndDate(endDate);
            contract.setNumberOfGuests(Integer.parseInt(numberOfGuests));
            contract.setNumberOfCars(Integer.parseInt(numberOfCars));
            contract.setTotalAmount(Double.parseDouble(deposit));

            // Thực hiện thêm vào Room và đồng bộ Firebase trên Thread phụ
            new Thread(() -> {
                // Thêm vào Room Database
                contractDAO.insert(contract);

                // Đồng bộ lên Firebase Realtime Database
                contractRef.child(roomId + "_" + khachId).setValue(contract);

                // Cập nhật UI trên Main Thread
                runOnUiThread(() -> {
                    Toast.makeText(this, "Thêm hợp đồng thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng màn hình sau khi thêm xong
                });
            }).start();
        }catch ( NumberFormatException e){
            Toast.makeText(this, "Vui lòng nhập đúng định dạng số", Toast.LENGTH_SHORT).show();

        }

    }
    private void startdate(){
        DatePickerDialog datePickerDialog=new DatePickerDialog(this);
        datePickerDialog.show();
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            binding.edtStartdate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            day=dayOfMonth;
            this.month=month;
            this.year=year;
        });
    }
    private void enddate(){
        final Calendar c = Calendar.getInstance();
        int hour2 = c.get(Calendar.HOUR_OF_DAY);
        int minute2 = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog=new TimePickerDialog(AddContract.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
                binding.edtEnddate.setText(formattedTime);
                hour=hourOfDay;
                minutes=minute;
            }
        },hour2,minute2,true);
        timePickerDialog.show();
    }

}
