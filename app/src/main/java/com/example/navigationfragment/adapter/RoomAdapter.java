package com.example.navigationfragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationfragment.databinding.ItemAddRoomBinding;
import com.example.navigationfragment.entity.RoomEntity;
import com.example.navigationfragment.fragment.BottomSheetFragment;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<RoomEntity> roomList;
    private final Context context;
    public RoomAdapter(List<RoomEntity> roomList , Context context) {
        this.roomList = roomList;
        this.context = context;
    }
    public void upDateData(List<RoomEntity> roomList){
        this.roomList = roomList;
        notifyDataSetChanged();
    }
    public  RoomAdapter(Context context){
        this.context=context;

    }

    @NonNull
    @Override
    public RoomAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddRoomBinding binding = ItemAddRoomBinding.inflate(LayoutInflater.from(context),parent,false);
        RoomViewHolder viewHolder = new RoomViewHolder(binding);
        return  new RoomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.RoomViewHolder holder, int position) {
        RoomEntity room = roomList.get(position);
        if(room == null){
            return;
        }
        holder.binding.txtRoomNumber.setText("Phòng: "+room.getSoPhong());
        holder.itemView.setOnClickListener(v -> {
            BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance(room);
            bottomSheetFragment.show(((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager(), bottomSheetFragment.getTag());

        });

        holder.binding.imgEdit.setOnClickListener(v ->  {

        });
    }
    @Override
    public int getItemCount() {
        return roomList.size();
    }
    public void addRoom(RoomEntity room){
        roomList.add(room);
        notifyDataSetChanged();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        ItemAddRoomBinding binding;
        public RoomViewHolder(ItemAddRoomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
