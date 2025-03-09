package com.example.navigationfragment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationfragment.databinding.ItemAddKhachBinding;
import com.example.navigationfragment.databinding.ItemAddRoomBinding;
import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.KhachWithRoom;

import java.util.List;

public class KhachAdapter extends RecyclerView.Adapter<KhachAdapter.KhachViewHolder> {
    private List<KhachWithRoom> khachList;
    private final Context context;
    public KhachAdapter(List<KhachWithRoom> khachList, Context context) {
        this.khachList = khachList;
        this.context = context;
    }
    public void upDateData(List<KhachWithRoom> khachList){
        this.khachList = khachList;
        notifyDataSetChanged();
    }

    public KhachAdapter(Context context) {
        this.context = context;
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


        holder.binding.tvSdt.setText("Sđt :"+item.khach.getSdt());
        holder.binding.tvCccd.setText("CCCD :"+item.khach.getCccd());
        holder.binding.tvKhachthue.setText("Khách thuê :" +item.khach.getTenKhach());
        holder.binding.tvPhongtro.setText("Phòng: " + item.room.getSoPhong()); // Hiển thị số phòng
        if (item.room != null) {
            holder.binding.tvPhongtro.setText("Phòng: " + item.room.getSoPhong());
        } else {
            holder.binding.tvPhongtro.setText("Phòng: Chưa có");
        }

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
