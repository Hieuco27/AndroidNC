package com.example.navigationfragment.action;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.navigationfragment.adapter.CSVCAdapter;
import com.example.navigationfragment.databinding.ActivityAddContractBinding;
import com.example.navigationfragment.databinding.ActivityCsvcBinding;
import com.example.navigationfragment.entity.Asset;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.KhachEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CSVCActivity extends AppCompatActivity {
    private ActivityCsvcBinding binding;
    private ExecutorService executorService;
    private Map<String, Boolean> assetList = new HashMap<>();
    private String idPhong;
    private CSVCAdapter adapter;
    private List<Asset> selectedAssets = new ArrayList<>();
    private int totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCsvcBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        executorService = Executors.newSingleThreadExecutor();

        idPhong = getIntent().getStringExtra("ID_PHONG");
        String soPhong = getIntent().getStringExtra("SO_PHONG");
        binding.tvRoomInfo.setText(soPhong);
        assetList = (Map<String, Boolean>) getIntent().getSerializableExtra("ASSET_LIST");

        // Xử lý chọn ngày
        binding.etDate.setOnClickListener(v -> showDatePicker());

        // Lắng nghe sự kiện nhấn nút lưu
        binding.btnSave.setOnClickListener(v -> saveCompensationRecord());

        fetchAsset();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CSVCActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    binding.etDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void fetchAsset() {
        if (assetList == null || assetList.isEmpty()) {
            Toast.makeText(this, "Không có tài sản nào.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference assetRef = FirebaseDatabase.getInstance().getReference("assets");
        List<Asset> assets = new ArrayList<>();

        for (String assetId : assetList.keySet()) {
            assetRef.child(assetId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Asset asset = snapshot.getValue(Asset.class);
                        asset.setId(assetId);
                        if (asset != null) {
                            assets.add(asset);
                            // Khi đủ số lượng, mới set Adapter (để tránh load thiếu)
                            if (assets.size() == assetList.size()) {
                                updateUI(assets);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("CSVCActivity", "Lỗi fetch Asset: " + error.getMessage());
                }
            });
        }
    }

    private void updateUI(List<Asset> assetList) {
        // Gán danh sách tài sản cho Adapter
        binding.rvFacilities.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new CSVCAdapter(this, assetList, this::onAssetSelected);
        binding.rvFacilities.setAdapter(adapter);
    }

    private void onAssetSelected(Asset asset, boolean isSelected) {
        // Cập nhật tài sản đã chọn và tính tổng tiền đền bù
        if (isSelected) {
            selectedAssets.add(asset);
            totalAmount += asset.getCompensationfee(); // Giả sử mỗi tài sản có giá trị "price"
        } else {
            selectedAssets.remove(asset);
            totalAmount -= asset.getCompensationfee();
        }
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        binding.tvTotalAmount.setText(totalAmount + " VNĐ");
    }

    private void saveCompensationRecord() {
        String date = binding.etDate.getText().toString();
        String note = binding.etNote.getText().toString();

        // Kiểm tra ngày và thông tin
        if (date.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày lập biên bản.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedAssets.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn tài sản.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo biên bản đền bù
        Map<String, Object> compensationRecord = new HashMap<>();
        compensationRecord.put("roomId", idPhong);
        compensationRecord.put("date", date);
        compensationRecord.put("note", note);
        compensationRecord.put("totalAmount", totalAmount);
        compensationRecord.put("assets", selectedAssets);

        // Lưu vào Firebase
        DatabaseReference compensationRef = FirebaseDatabase.getInstance().getReference("compensationRecords");
        compensationRef.push().setValue(compensationRecord).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CSVCActivity.this, "Lưu biên bản thành công.", Toast.LENGTH_SHORT).show();
                finish(); // Quay lại hoặc chuyển hướng
            } else {
                Toast.makeText(CSVCActivity.this, "Lưu biên bản thất bại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}



