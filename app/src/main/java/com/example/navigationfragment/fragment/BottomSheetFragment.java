package com.example.navigationfragment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.navigationfragment.action.AddContract;
import com.example.navigationfragment.action.AddHoaDon;
import com.example.navigationfragment.action.AddKhach;
import com.example.navigationfragment.action.CSVCActivity;
import com.example.navigationfragment.action.RoomDetailActivity;
import com.example.navigationfragment.databinding.BottomSheetRoomBinding;
import com.example.navigationfragment.entity.RoomEntity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    public static final String TAG = "roomsheet";
    BottomSheetRoomBinding binding;
    private RoomEntity room;
    private String soPhong;

    public BottomSheetFragment() {
    }

    public static BottomSheetFragment newInstance(RoomEntity room) {
        BottomSheetFragment fragment = new BottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, (Serializable) room);
        fragment.setArguments(bundle);
        return fragment;
    }


    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetRoomBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            room = (RoomEntity) getArguments().getSerializable(TAG);
            binding.tvRoomTitle.setText("Phòng: " + room.getSoPhong());
        }
        // Thêm khách thuê - kiểm tra nếu phòng đã có khách
        binding.btnAddTenant.setOnClickListener(v -> {
            if (room.getKhachId() != null) {
                Toast.makeText(getContext(), "Phòng đã có khách thuê!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), AddKhach.class);
                intent.putExtra("ID_PHONG", room.getId());
                intent.putExtra("SO_PHONG", room.getSoPhong());
                startActivity(intent);
            }
            dismiss();

        });
        binding.btnFee.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), CSVCActivity.class);
            intent.putExtra("ASSET_LIST", (Serializable) room.getAssets());
            intent.putExtra("SO_PHONG", room.getSoPhong());
            startActivity(intent);
            dismiss();

        });
        binding.btnXemttphong.setOnClickListener(v -> {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), RoomDetailActivity.class);
                intent.putExtra("room", room);
                startActivity(intent);
            }
            dismiss();

        });
        // Thêm hợp đồng - kiểm tra nếu phòng đã có hợp đồng
        binding.btnAddcontract.setOnClickListener(v -> {
            if(room.getKhachId()==null){
                Toast.makeText(getContext(),"Phòng chưa có khach",Toast.LENGTH_SHORT).show();
                return;
            }
            DatabaseReference contractsRef = FirebaseDatabase.getInstance().getReference("contracts");
            contractsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean isValidAddContract = true;
                    for (DataSnapshot contractSnapshot : snapshot.getChildren()) {
                        String roomId = contractSnapshot.child("roomId").getValue(String.class);
                        String endDateStr = contractSnapshot.child("endDate").getValue(String.class);
                        Log.d("ContractCheck",roomId+"|"+room.getId());
                        Log.d("ContractCheck",endDateStr);
                        if ( roomId != null && endDateStr != null) {
                            if ( roomId.equals(room.getId())) {
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                    Date endDate = sdf.parse(endDateStr);
                                    Date today = new Date(); // Ngày hôm nay
                                    Log.d("ContractCheck", "Ngày kết thúc hợp đồng: " + endDate+"|"+today);
                                    if (endDate != null) {
                                        if (endDate.before(today)) {
                                            isValidAddContract = true;
                                            Log.d("ContractCheck", "Hợp đồng đã hết hạn.");
                                        } else {
                                            isValidAddContract = false;
                                            Log.d("ContractCheck", "ợp đồng còn hiệu lực");

                                            Toast.makeText(getContext(), "Hợp đồng còn hiệu lực.", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    isValidAddContract = false;
                                    Toast.makeText(getContext(),"Khong the xac dinh ngay hop dong",Toast.LENGTH_SHORT).show();
                                    Log.e("ContractCheck", "Lỗi định dạng ngày.");
                                }
                            }
                        }
                    }
                    if (isValidAddContract == true) {
                        Intent intent = new Intent(getActivity(), AddContract.class);
                        intent.putExtra("ID_PHONG", room.getId());
                        intent.putExtra("ID_KHACH", room.getKhachId());
                        intent.putExtra("SO_PHONG", room.getSoPhong());
                        startActivity(intent);
                        dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", "Lỗi truy vấn hợp đồng", error.toException());
                }
            });

        });
        // Thêm hóa đơn - kiểm tra nếu có hợp đồng thì mới cho thêm
        binding.btnAddbill.setOnClickListener(v -> {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("contracts");
            ref.orderByChild("roomId").equalTo(room.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        Toast.makeText(getContext(), "Phòng chưa có hợp đồng!", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getActivity(), AddHoaDon.class);
                        intent.putExtra("ID_PHONG", room.getId());
                        intent.putExtra("SO_PHONG", room.getSoPhong());
                        intent.putExtra("KHACH_ID",room.getKhachId());
                        startActivity(intent);
                    }
                    dismiss();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        });

        return binding.getRoot();
    }

}
