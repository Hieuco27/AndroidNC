package com.example.navigationfragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationfragment.databinding.ItemAddHoadonBinding;
import com.example.navigationfragment.entity.HoaDonEntity;
import com.example.navigationfragment.entity.HoaDonWithRoom;
import com.example.navigationfragment.entity.KhachWithRoom;

import java.util.List;

public class HoaDonAdapter extends RecyclerView.Adapter<HoaDonAdapter.HoaDonViewHolder> {
    private List<HoaDonWithRoom> hoaDonList;
    private Context context;
    public HoaDonAdapter(List<HoaDonWithRoom> hoaDonList, Context context) {
        this.hoaDonList = hoaDonList;
        this.context = context;
    }
    public void upDateData(List<HoaDonWithRoom> hoaDonList){
        this.hoaDonList = hoaDonList;
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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HoaDonAdapter.HoaDonViewHolder holder, int position) {

        HoaDonWithRoom item= hoaDonList.get(position);

        double tienDienNuoc = (item.soDien * item.giaDien) + (item.soNuoc * item.giaNuoc);

        holder.binding.tvHoadon.setText("Tên hóa đơn: "+item.tenHoaDon);
        holder.binding.tvThanhtien.setText(String.format("%,.0fđ", item.tongTien));
        holder.binding.tvTienphong.setText("Tiền phòng: " + String.format("%,.0fđ", item.giaPhong));
        holder.binding.tvDichvu.setText("Tổng dịch vụ: " + String.format("%,.0fđ", item.giaDichVu ));
        holder.binding.tvDiennuoc.setText("Tiền điện nước: " + String.format("%,.0fđ", tienDienNuoc));
        holder.binding.tvTrangthai.setText("Trạng thái: " + (item.daThanhToan ? "Đã thanh toán" : "Chưa thanh toán"));
        holder.binding.tvGhichu.setText("Ghi chú: " + item.ghiChu);


    }
    @Override
    public int getItemCount() {
            return hoaDonList.size();
    }
    public static class HoaDonViewHolder extends RecyclerView.ViewHolder{
        ItemAddHoadonBinding binding;
        public HoaDonViewHolder(ItemAddHoadonBinding binding) {
            super(binding.getRoot());
        }
    }


}
