package com.example.navigationfragment.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.navigationfragment.databinding.FragmentDoanhthuBinding;

public class DoanhThuFragment extends Fragment {
    FragmentDoanhthuBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding= FragmentDoanhthuBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
}
