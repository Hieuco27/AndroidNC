package com.example.navigationfragment.action;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.navigationfragment.databinding.ActivityXemttHopdongBinding;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.ContractDisplay;
import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.RoomEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HopDongDetail extends AppCompatActivity {
    ActivityXemttHopdongBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXemttHopdongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String contractId = getIntent().getStringExtra("contractId");



        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        
    }
    private void loadContractDetails(ContractDisplay detail){
//        ContractEntity contract = detail.contract;
//        RoomEntity room = detail.room;
//        KhachEntity khach = detail.khach;
//
//        binding.tvHopdongid.setText(contract.getContractId());
//        binding.tvTrangthai.setText(contract.isStatus() ? "Đang hiệu lực" : "Đã chấm dứt");
//
//        binding.tvRoomid.setText(room.getSoPhong());
//        binding.tvGiaphong.setText(room.getGiaPhong() + " VNĐ");
//
//        binding.tvTenkhach.setText(khach.getTenKhach());
//        binding.tvCccd.setText(khach.getCccd());
//        binding.tvSdt.setText(khach.getSdt());
//
//        binding.tvStartdate.setText(contract.getStartDate());
//        binding.tvEnddate.setText(contract.getEndDate());
//        binding.tvTiencoc.setText(contract.getTotalAmount() + " VNĐ");


    }
}
