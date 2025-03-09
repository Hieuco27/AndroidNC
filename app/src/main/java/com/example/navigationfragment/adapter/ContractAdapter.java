package com.example.navigationfragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationfragment.databinding.ItemAddHopdongBinding;
import com.example.navigationfragment.entity.ContractEntity;

import java.util.List;

public class ContractAdapter extends  RecyclerView.Adapter<ContractAdapter.ContractViewHolder>{

    private List<ContractEntity> contractList;
    private Context context;
    public ContractAdapter(List<ContractEntity> contractList, Context context) {
        this.contractList = contractList;
        this.context = context;
    }

    public void updateData(List<ContractEntity> contractList) {
        this.contractList = contractList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ContractAdapter.ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddHopdongBinding binding = ItemAddHopdongBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ContractViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ContractAdapter.ContractViewHolder holder, int position) {
        ContractEntity contract = contractList.get(position);
        if(contract == null){
            return;
        }

        holder.binding.tvHopdong.setText("1. Hợp đồng phòng: "+contract.getRoomId());
        holder.binding.tvKhachthue.setText("Người thuê: "+contract.getKhachId());
        holder.binding.tvDatestart.setText("Ngày bắt đầu: "+contract.getStartDate());
        holder.binding.tvDateend.setText("Ngày kết thúc: "+contract.getEndDate());

        holder.binding.tvNguoio.setText("Số người ở: "+contract.getNumberOfGuests());
        holder.binding.tvCar.setText("Số lượng xe: "+contract.getNumberOfCars());


    }

    @Override
    public int getItemCount() {
        return contractList.size();


    }
    public  static  class ContractViewHolder extends RecyclerView.ViewHolder{
        ItemAddHopdongBinding binding;
        public ContractViewHolder(ItemAddHopdongBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
