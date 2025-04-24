package com.example.navigationfragment.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationfragment.DAO.ContractDAO;
import com.example.navigationfragment.DAO.KhachDAO;
import com.example.navigationfragment.DAO.RoomDAO;
import com.example.navigationfragment.R;
import com.example.navigationfragment.databinding.ItemAddHopdongBinding;
import com.example.navigationfragment.entity.ContractEntity;
import com.example.navigationfragment.entity.ContractWithDetails;
import com.example.navigationfragment.entity.KhachEntity;
import com.example.navigationfragment.entity.RoomEntity;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ContractViewHolder> {

    private List<ContractWithDetails> contractList;
    private Context context;
    private RoomDAO roomDAO;
    private ContractDAO contractDAO;
    private KhachDAO khachDAO;
    private RoomEntity room;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    // Constructor hiện tại: chỉ nhận Context, khởi tạo contractList rỗng
    public ContractAdapter(Context context) {
        this.context = context;
        this.contractList = new ArrayList<>();
        this.roomDAO = null;
        this.contractDAO = null;
        this.khachDAO = null;
    }

    // Constructor mới: nhận Context và danh sách contractList ban đầu
    public ContractAdapter(Context context, List<ContractWithDetails> contractList, RoomDAO roomDAO, ContractDAO contractDAO, KhachDAO khachDAO) {
        this.context = context;
        this.contractList = (contractList != null) ? contractList : new ArrayList<>();
        this.roomDAO = roomDAO;
        this.contractDAO = contractDAO;
        this.khachDAO= khachDAO;
    }


    public void updateData(List<ContractWithDetails> contractList) {
        this.contractList= contractList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddHopdongBinding binding = ItemAddHopdongBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ContractViewHolder(binding);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull ContractViewHolder holder, int position) {
        ContractWithDetails itemcontract = contractList.get(position);

        ContractEntity contract = itemcontract.contract;
        RoomEntity  room = itemcontract.room;
        KhachEntity khach = itemcontract.khach;
        if (room== null || itemcontract == null|| khach == null)
        {
            return;
        }

        holder.binding.tvStt.setText(String.valueOf(position + 1));

        // Gán dữ liệu
        holder.binding.tvHopdong.setText(". Hợp đồng phòng : " + room.getSoPhong());
        holder.binding.tvKhachthue.setText("Người thuê: " + khach.getTenKhach());
        holder.binding.tvDatestart.setText("Ngày bắt đầu: " + contract.getStartDate());
        holder.binding.tvDateend.setText("Ngày kết thúc: " + contract.getEndDate());
        holder.binding.tvNguoio.setText("Số người ở: " + contract.getNumberOfGuests());
        holder.binding.tvCar.setText("Số lượng xe: " + contract.getNumberOfCars());

        // Cập nhật màu nền ban đầu dựa trên trạng thái hợp đồng
        updateCardBackground(holder, contract);

        // Xử lý nút kiểm tra trạng thái
        holder.binding.btnCheckStatus.setOnClickListener(v -> {
            boolean isExpired = isContractExpired(contract.getEndDate());
            boolean isTerminated = !contract.isStatus();

            if (isExpired || isTerminated) {
                holder.binding.cardView.setCardBackgroundColor(Color.parseColor("#FF6666")); // Màu đỏ khi hết hiệu lực
                Toast.makeText(context, "Hợp đồng đã hết hiệu lực", Toast.LENGTH_SHORT).show();
            } else {
                holder.binding.cardView.setCardBackgroundColor(Color.parseColor("#66FF66")); // Màu xanh khi còn hiệu lực
                Toast.makeText(context, "Hợp đồng vẫn còn hiệu lực", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý menu
        holder.binding.btnMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, holder.binding.btnMenu);
            popup.getMenuInflater().inflate(R.menu.menu_contract, popup.getMenu());

            // Vô hiệu hóa tùy chọn "Chấm dứt hợp đồng" nếu hợp đồng đã hết hiệu lực hoặc đã chấm dứt
            MenuItem terminateItem = popup.getMenu().findItem(R.id.menu_chamdut);
            boolean isExpired = isContractExpired(contract.getEndDate());
            if (isExpired || !contract.isStatus()) {
                terminateItem.setEnabled(false);
            } else {
                terminateItem.setEnabled(true);
            }

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_giahan) {
                    showDatePickerDialog(contract.getEndDate(), newDate -> {
                        contract.setEndDate(newDate);
                        updateContract(contract);
                        updateCardBackground(holder, contract); // Cập nhật màu sau khi gia hạn
                        Toast.makeText(context, "Gia hạn thành công", Toast.LENGTH_SHORT).show();
                    });
                    return true;
                } else if (item.getItemId() == R.id.menu_chamdut) {
                    new AlertDialog.Builder(context)
                            .setTitle("Chấm dứt hợp đồng")
                            .setMessage("Bạn chắc chắn muốn chấm dứt hợp đồng này?")
                            .setPositiveButton("Đồng ý", (dialog, which) -> {
                                contract.setStatus(false);
                                updateContract(contract);
                                if (room != null) {
                                    room.setTrangThai(false); // Phòng trống sau khi chấm dứt
                                    updateRoom(room);
                                }
                                updateCardBackground(holder, contract); // Cập nhật màu sau khi chấm dứt
                                Toast.makeText(context, "Đã chấm dứt hợp đồng", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                    return true;
                } else if (item.getItemId() == R.id.menu_khoiphuc) {
                    MenuItem restoreItem = popup.getMenu().findItem(R.id.menu_khoiphuc);
                    restoreItem.setEnabled(!contract.isStatus());
                    new AlertDialog.Builder(context)
                            .setTitle("Khôi phục hợp đồng")
                            .setMessage("Bạn chắc chắn muốn khôi phục hợp đồng này?")
                            .setPositiveButton("Đồng ý", (dialog, which) -> {
                                contract.setStatus(true);
                                updateContract(contract);
                                if (room != null) {
                                    room.setTrangThai(true); // Phòng có người ở sau khi khôi phục
                                    updateRoom(room);
                                }
                                updateCardBackground(holder, contract); // Cập nhật màu sau khi khôi phục
                                Toast.makeText(context, "Đã khôi phục hợp đồng", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    // Hàm kiểm tra hợp đồng có hết hiệu lực không
    private boolean isContractExpired(String endDate) {
        try {
            Date end = dateFormat.parse(endDate);
            Date today = new Date();
            return today.after(end); // Hết hiệu lực nếu ngày hiện tại sau ngày kết thúc
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm cập nhật màu nền của CardView dựa trên trạng thái hợp đồng
    private void updateCardBackground(ContractViewHolder holder, ContractEntity contract) {
        boolean isExpired = isContractExpired(contract.getEndDate());
        boolean isTerminated = !contract.isStatus();
        if (isExpired || isTerminated) {
            ViewCompat.setBackgroundTintList(holder.binding.cardView, ColorStateList.valueOf(Color.parseColor("#FF6666")));

        } else {
            ViewCompat.setBackgroundTintList(holder.binding.cardView, ColorStateList.valueOf(Color.parseColor("#66FF66")));
        }
    }

    public void updateContract(ContractEntity contract) {
        FirebaseDatabase.getInstance().getReference("contracts")
                .child(contract.getContractId())
                .setValue(contract)
                .addOnSuccessListener(aVoid -> {
                    if (contractDAO != null) {
                        executorService.execute(() -> contractDAO.update(contract));
                    } else {
                        Toast.makeText(context, "ContractDAO is null", Toast.LENGTH_SHORT).show();
                    }

                    notifyDataSetChanged(); // Cập nhật lại danh sách
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Lỗi cập nhật hợp đồng trên Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void updateRoom(RoomEntity room) {
        FirebaseDatabase.getInstance().getReference("rooms")
                .child(room.getId())
                .setValue(room)
                .addOnSuccessListener(aVoid -> {
                    executorService.execute(() -> roomDAO.update(room));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Lỗi cập nhật phòng trên Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showDatePickerDialog(String oldDate, OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
            String newDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            listener.onDateSelected(newDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    interface OnDateSelectedListener {
        void onDateSelected(String newDate);
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