package com.example.navigationfragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationfragment.DAO.RoomDAO;
import com.example.navigationfragment.databinding.ItemAddHopdongBinding;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.RoomEntity;

import java.util.ArrayList;
import java.util.List;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ContractViewHolder> {

    private List<ContractEntity> contractList;
    private Context context;
    private RoomDAO roomDAO;

    // Constructor hiện tại: chỉ nhận Context, khởi tạo contractList rỗng
    public ContractAdapter(Context context) {
        this.context = context;
        this.contractList = new ArrayList<>();
    }


    // Constructor mới: nhận Context và danh sách contractList ban đầu
    public ContractAdapter(Context context, List<ContractEntity> contractList, RoomDAO roomDAO) {
        this.context = context;
        this.contractList = (contractList != null) ? contractList : new ArrayList<>();
        this.roomDAO= roomDAO;
    }


    public void setContractList(List<ContractEntity> contractList) {
        this.contractList.clear();
        if (contractList != null) {
            this.contractList.addAll(contractList);
        }
        notifyDataSetChanged();
    }

    public void updateData(List<ContractEntity> contractList) {
        this.contractList.clear();
        if (contractList != null) {
            this.contractList.addAll(contractList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddHopdongBinding binding = ItemAddHopdongBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ContractViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractViewHolder holder, int position) {
        ContractEntity contract = contractList.get(position);
        if (contract == null) {
            return;
        }
        /*RoomEntity room= roomDAO.getRoomByIdSync(contract.getRoomId());
        if(room!=null)
        {
            holder.binding.tvHopdong.setText("Phòng" + room.getSoPhong());
        }*/

        // Gán dữ liệu
        holder.binding.tvHopdong.setText("1. Hợp đồng phòng: " + contract.getRoomId());
        holder.binding.tvKhachthue.setText("Người thuê: " + contract.getKhachId());
        holder.binding.tvDatestart.setText("Ngày bắt đầu: " + contract.getStartDate());
        holder.binding.tvDateend.setText("Ngày kết thúc: " + contract.getEndDate());
        holder.binding.tvNguoio.setText("Số người ở: " + contract.getNumberOfGuests());
        holder.binding.tvCar.setText("Số lượng xe: " + contract.getNumberOfCars());
    }

    @Override
    public int getItemCount() {
        return (contractList != null) ? contractList.size() : 0;
    }

    public static class ContractViewHolder extends RecyclerView.ViewHolder {
        ItemAddHopdongBinding binding;

        public ContractViewHolder(ItemAddHopdongBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
