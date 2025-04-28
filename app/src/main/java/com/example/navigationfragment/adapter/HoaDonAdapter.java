package com.example.navigationfragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationfragment.action.HoaDonDetail;
import com.example.navigationfragment.databinding.ItemAddHoadonBinding;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.HoaDonEntity;
import com.example.navigationfragment.entity.HoaDonDisplay;
import com.example.navigationfragment.entity.RoomEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HoaDonAdapter extends RecyclerView.Adapter<HoaDonAdapter.HoaDonViewHolder> {
    private List<HoaDonDisplay> hoaDonList;
    private Context context;
    public HoaDonAdapter(List<HoaDonDisplay> hoaDonList, Context context) {
        this.hoaDonList = new ArrayList<>(hoaDonList);
        this.context = context;
    }
    public void updateData(List<HoaDonDisplay> hoaDonList){
        if(hoaDonList==null){
            return;
        }
        this.hoaDonList.clear();

        this.hoaDonList.addAll(hoaDonList);
        notifyDataSetChanged();
    }
    public  HoaDonAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HoaDonAdapter.HoaDonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemAddHoadonBinding binding = ItemAddHoadonBinding.inflate(LayoutInflater.from(context),parent,false);
        HoaDonViewHolder viewHolder = new HoaDonViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HoaDonAdapter.HoaDonViewHolder holder, int position) {
      try{
        HoaDonDisplay item = hoaDonList.get(position);

        HoaDonEntity hoaDon = item.hoaDon;
        RoomEntity room = item.room;
        if (hoaDon == null || room == null) {
            return;
        }


            double tienDienNuoc = (hoaDon.getSoDien() * room.getGiaDien()) + (hoaDon.getSoNuoc() * room.getGiaNuoc());
            double tienNuoc= hoaDon.getSoNuoc()*room.getGiaNuoc();
            double tienDien= hoaDon.getSoDien()*room.getGiaDien();
            double tongTien= hoaDon.getTongTien() + room.getGiaDichVu();




        holder.binding.tvStt.setText(String.valueOf(position + 1));
        holder.binding.tvHoadon.setText(". Tên hóa đơn : " + hoaDon.getTenHoaDon());
        holder.binding.tvThanhtien.setText(String.format("%,.0fđ", tongTien));
        holder.binding.tvTienphong.setText("Tiền phòng : " + String.format("%,.0fđ", room.getGiaPhong()));
        holder.binding.tvDichvu.setText("Tổng dịch vụ : " + String.format("%,.0fđ", room.getGiaDichVu()));
        holder.binding.tvDiennuoc.setText("Tiền điện nước : " + String.format("%,.0fđ", tienDienNuoc));
        holder.binding.tvTrangthai.setText("Trạng thái : " + (hoaDon.isDaThanhToan() ? "Đã thanh toán" : "Chưa thanh toán"));
        holder.binding.tvGhichu.setText("Ghi chú : " + hoaDon.getGhiChu());

        holder.itemView.setOnClickListener(v -> {
            Intent intent= new Intent(context, HoaDonDetail.class);
            intent.putExtra("HOADONDISPLAY",(Serializable) item);
            context.startActivity(intent);
        });}
      catch (Exception e){};
    }

    @Override
    public int getItemCount() {
            return hoaDonList.size();
    }
    public static class HoaDonViewHolder extends RecyclerView.ViewHolder{
        ItemAddHoadonBinding binding;
        public HoaDonViewHolder(ItemAddHoadonBinding binding) {

            super(binding.getRoot());
            this.binding=binding;
        }
    }


}
