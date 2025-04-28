package com.example.navigationfragment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationfragment.action.KhachDetail;
import com.example.navigationfragment.databinding.ItemAddKhachBinding;
import com.example.navigationfragment.entity.KhachDisplay;

import java.util.ArrayList;
import java.util.List;

public class KhachAdapter extends RecyclerView.Adapter<KhachAdapter.KhachViewHolder> {
    private List<KhachDisplay> khachList;// Danh sách khách thuê
    private final Context context;


    public KhachAdapter(List<KhachDisplay> khachList, Context context) {
        this.khachList = khachList != null ? khachList : new ArrayList<>(); // Tránh null
        this.context = context;
    }
    // Cập nhật dữ liệu mới
    public void updateData(List<KhachDisplay> newList) {
        khachList.clear();
        if (newList != null) {
            this.khachList.addAll(newList);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public KhachAdapter.KhachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddKhachBinding binding = ItemAddKhachBinding.inflate(LayoutInflater.from(context),parent,false);

        return new KhachViewHolder(binding);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull KhachAdapter.KhachViewHolder holder, int position) {

        KhachDisplay item= khachList.get(position);

        if (item != null && item.getKhach() != null) {
            holder.binding.tvStt.setText(position + 1 + ". ");
            holder.binding.tvSdt.setText("Sđt : " + item.getKhach().getSdt());
            holder.binding.tvCccd.setText("CCCD : " + item.getKhach().getCccd());
            holder.binding.tvKhachthue.setText("Khách thuê : " + item.getKhach().getTenKhach());
        } else {
            holder.binding.tvStt.setText(position + 1 + ". ");
            holder.binding.tvSdt.setText("Sđt : Chưa có");
            holder.binding.tvCccd.setText("CCCD : Chưa có");
            holder.binding.tvKhachthue.setText("Khách thuê : Chưa có");
        }
        if (item != null) {
           /* holder.binding.tvPhongtro.setText("Phòng: " + item.getSoPhong());*/
        } else {
            holder.binding.tvPhongtro.setText("Phòng: Chưa có");
        }
       holder.itemView.setOnClickListener(v -> {
           Intent intent = new Intent(holder.itemView.getContext(), KhachDetail.class);
           intent.putExtra("KHACH_ID", item.getKhach().getKhachId());
           intent.putExtra("TEN_KHACH", item.getKhach().getTenKhach());
           intent.putExtra("SDT", item.getKhach().getSdt());
           intent.putExtra("CCCD", item.getKhach().getCccd());
           intent.putExtra("SO_PHONG", item.getRoomName());

           holder.itemView.getContext().startActivity(intent);
       });

    }
    @Override
    public int getItemCount() {
        return khachList.size();
    }

    public static class KhachViewHolder extends RecyclerView.ViewHolder {
        ItemAddKhachBinding binding;
        public KhachViewHolder(ItemAddKhachBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
