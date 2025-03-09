package com.example.navigationfragment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.navigationfragment.action.AddKhach;
import com.example.navigationfragment.action.RoomDetailActivity;
import com.example.navigationfragment.databinding.BottomSheetRoomBinding;
import com.example.navigationfragment.entity.RoomEntity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.Serializable;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    public static  final String TAG="roomsheet";
    BottomSheetRoomBinding binding;
    private RoomEntity room;
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

        binding.btnAddTenant.setOnClickListener(v -> {
            if (getActivity()!=null){
                Intent intent=new Intent(getActivity(), AddKhach.class);
                startActivity(intent);
            }

        });
        binding.btnXemttphong.setOnClickListener(v -> {
            if (getActivity()!=null){
                Intent intent=new Intent(getActivity(), RoomDetailActivity.class);
                intent.putExtra("room", room);
                startActivity(intent);
                dismiss();
            }
        });
        return binding.getRoot();
    }

}
