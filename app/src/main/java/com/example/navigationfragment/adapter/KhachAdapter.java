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

        // Gán thong tin khách thuê
        holder.binding.tvStt.setText(position+1+". ");
        holder.binding.tvSdt.setText("Sđt : "+ item.khach.getSdt());
        holder.binding.tvCccd.setText("CCCD : "+item.khach.getCccd());
        holder.binding.tvKhachthue.setText("Khách thuê : " +item.khach.getTenKhach());

        if (item.room != null) {
            holder.binding.tvPhongtro.setText("Phòng: " + item.room.getSoPhong());
        } else {
            holder.binding.tvPhongtro.setText("Phòng: Chưa có");
        }
       holder.itemView.setOnClickListener(v -> {
           Intent intent = new Intent(holder.itemView.getContext(), KhachDetail.class);
           intent.putExtra("khach", item.khach); // KhachEntity implements Serializable
           intent.putExtra("phong", item.room);   // Nếu RoomEntity cũng implements Serializable
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
