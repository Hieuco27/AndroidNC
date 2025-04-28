package com.example.navigationfragment.adapter;

import static android.content.Intent.getIntent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationfragment.action.KhachDetail;
import com.example.navigationfragment.databinding.ItemAddKhachBinding;
import com.example.navigationfragment.entity.KhachWithRoom;
import com.example.navigationfragment.entity.KhachWithRoom;

import java.util.ArrayList;
import java.util.List;

public class KhachAdapter extends RecyclerView.Adapter<KhachAdapter.KhachViewHolder> {
    private List<KhachWithRoom> khachList;// Danh sách khách thuê
    private final Context context;


    public KhachAdapter(List<KhachWithRoom> khachList, Context context) {
        this.khachList = khachList != null ? khachList : new ArrayList<>(); // Tránh null
        this.context = context;
    }
    // Cập nhật dữ liệu mới
    public void updateData(List<KhachWithRoom> khachList) {
        this.khachList.clear();
        if (khachList != null) {
            this.khachList.addAll(khachList);
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

        KhachWithRoom item= khachList.get(position);

        if (item != null && item.khach != null) {
            holder.binding.tvStt.setText(position + 1 + ". ");
            holder.binding.tvSdt.setText("Sđt : " + item.khach.getSdt());
            holder.binding.tvCccd.setText("CCCD : " + item.khach.getCccd());
            holder.binding.tvKhachthue.setText("Khách thuê : " + item.khach.getTenKhach());
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
           intent.putExtra("KHACH_ID", item.khach.getKhachId());
           intent.putExtra("TEN_KHACH", item.khach.getTenKhach());
           intent.putExtra("SDT", item.khach.getSdt());
           intent.putExtra("CCCD", item.khach.getCccd());
           intent.putExtra("SO_PHONG", item.room.getSoPhong());

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
