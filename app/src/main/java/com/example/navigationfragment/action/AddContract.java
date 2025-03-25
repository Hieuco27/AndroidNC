package com.example.navigationfragment.action;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.navigationfragment.AppDatabase;
import com.example.navigationfragment.DAO.ContractDAO;
import com.example.navigationfragment.DAO.KhachDAO;
import com.example.navigationfragment.DAO.RoomDAO;
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
    private ContractDAO contractDAO;
    private KhachDAO khachDAO;
    private RoomDAO roomDAO;
    private DatabaseReference contractRef;
    private DatabaseReference khachRef;
    private ExecutorService executorService;
    private ArrayAdapter<String> khachAdapter;
    private List<KhachEntity> khachList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddContractBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        contractDAO = AppDatabase.getInstance(this).contractDao();
        khachDAO = AppDatabase.getInstance(this).khachDao();
        roomDAO = AppDatabase.getInstance(this).roomDao();
        contractRef = FirebaseDatabase.getInstance().getReference("contracts");
        khachRef = FirebaseDatabase.getInstance().getReference("khach");
        executorService = Executors.newSingleThreadExecutor();

        // Thiết lập Spinner cho khách
        khachAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        khachAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTenkhach.setAdapter(khachAdapter);
        loadKhachList();

        syncFirebaseToRoom();

        String soPhong = getIntent().getStringExtra("SO_PHONG");
        if (soPhong != null) {
            binding.edtSophong.setText(soPhong);
            binding.edtSophong.setEnabled(false);
        }

        binding.btnThemcontract.setOnClickListener(v -> addContract());
        binding.btnHuy.setOnClickListener(v -> finish());
        binding.edtStartdate.setOnClickListener(v -> showDatePicker(true));
        binding.edtEnddate.setOnClickListener(v -> showDatePicker(false));
    }

    private void loadKhachList() {
        executorService.execute(() -> {
            khachList = khachDAO.getAllKhachSync();

            runOnUiThread(() -> {
                khachAdapter.clear();
                for (KhachEntity khach : khachList) {
                    khachAdapter.add(khach.getKhachId() + " - " + khach.getTenKhach());
                }
                khachAdapter.notifyDataSetChanged();
            });
        });
    }

    private void addContract() {
        String soPhong = binding.edtSophong.getText().toString().trim();
        int selectedPosition = binding.spinnerTenkhach.getSelectedItemPosition();
        if (selectedPosition == -1) {
            Toast.makeText(this, "Vui lòng chọn khách thuê!", Toast.LENGTH_SHORT).show();
            return;
        }
        String khachId = khachList.get(selectedPosition).getKhachId(); // Lấy khachId từ danh sách
        String startDate = binding.edtStartdate.getText().toString().trim();
        String endDate = binding.edtEnddate.getText().toString().trim();
        String numberOfGuestsStr = binding.edtNguoio.getText().toString().trim();
        String numberOfCarsStr = binding.edtCar.getText().toString().trim();

        if (soPhong.isEmpty() || khachId.isEmpty() || startDate.isEmpty() || endDate.isEmpty() ||
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
            RoomEntity room = roomDAO.getRoomBySoPhongSync(soPhong);
            if (room == null) {
                runOnUiThread(() -> Toast.makeText(this, "Phòng không tồn tại!", Toast.LENGTH_SHORT).show());
                return;
            }
            KhachEntity khach = khachDAO.getKhachByIdSync(khachId);
            if (khach == null) {
                runOnUiThread(() -> Toast.makeText(this, "Khách thuê không tồn tại!", Toast.LENGTH_SHORT).show());
                return;
            }

            ContractEntity contract = new ContractEntity(
                    UUID.randomUUID().toString(), // Tạo contractId
                    room.getId(),
                    khachId,
                    startDate,
                    endDate,
                    numberOfGuests,
                    numberOfCars,
                    0.0, // totalAmount, bạn có thể thêm logic tính sau
                    true  // status
            );
            contractDAO.insert(contract);
            contractRef.child(contract.getContractId()).setValue(contract);
            runOnUiThread(() -> {
                Toast.makeText(this, "Thêm hợp đồng thành công", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private void syncFirebaseToRoom() {
        khachRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                executorService.execute(() -> {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        KhachEntity khach = data.getValue(KhachEntity.class);
                        if (khach != null) {
                            khachDAO.insert(khach);
                            Log.d("Sync", "Đã đồng bộ khách: " + khach.getKhachId());
                        }
                    }
                    loadKhachList(); // Cập nhật lại Spinner sau khi đồng bộ
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Sync", "Lỗi đồng bộ: " + error.getMessage());
            }
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



