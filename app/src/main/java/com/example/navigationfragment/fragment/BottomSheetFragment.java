package com.example.navigationfragment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.navigationfragment.DAO.ContractDAO;
import com.example.navigationfragment.action.AddContract;
import com.example.navigationfragment.action.AddHoaDon;
import com.example.navigationfragment.action.AddKhach;
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

public class BottomSheetFragment extends BottomSheetDialogFragment {
    public static  final String TAG="roomsheet";
    BottomSheetRoomBinding binding;
    private RoomEntity room;
    private ContractDAO contractDAO;
    private String soPhong;
    public BottomSheetFragment(){
    }
    public static BottomSheetFragment newInstance(RoomEntity room){
        BottomSheetFragment fragment=new BottomSheetFragment();
        Bundle bundle= new Bundle();
        bundle.putSerializable(TAG, (Serializable) room);
        fragment.setArguments(bundle);
        return fragment;
    }


    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        binding= BottomSheetRoomBinding.inflate(inflater,container,false);

        if(getArguments()!=null){
            room=(RoomEntity) getArguments().getSerializable(TAG);
            binding.tvRoomTitle.setText("Phòng: "+room.getSoPhong());
        }
        // Thêm khách thuê - kiểm tra nếu phòng đã có khách
        binding.btnAddTenant.setOnClickListener(v -> {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("khach");
            ref.orderByChild("roomId").equalTo(room.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(getContext(), "Phòng đã có khách thuê!", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getActivity(), AddKhach.class);
                        intent.putExtra("SO_PHONG", room.getSoPhong());
                        startActivity(intent);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        });

        binding.btnXemttphong.setOnClickListener(v -> {
            if (getActivity()!=null){
                Intent intent=new Intent(getActivity(), RoomDetailActivity.class);
                intent.putExtra("room", room);
                startActivity(intent);
                dismiss();
            }
        });
        // Thêm hợp đồng - kiểm tra nếu phòng đã có hợp đồng
        binding.btnAddcontract.setOnClickListener(v -> {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("contracts");
            ref.orderByChild("roomId").equalTo(room.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(getContext(), "Phòng đã có hợp đồng!", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getActivity(), AddContract.class);
                        intent.putExtra("SO_PHONG", room.getSoPhong());
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
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
                        intent.putExtra("SO_PHONG", room.getSoPhong());
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        });

        return binding.getRoot();
    }

}
