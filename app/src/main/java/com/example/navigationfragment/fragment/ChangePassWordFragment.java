package com.example.navigationfragment.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.navigationfragment.databinding.FragmentChangePasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChangePassWordFragment extends Fragment {
    FragmentChangePasswordBinding binding;
    private ProgressDialog progressDialog;
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        binding= FragmentChangePasswordBinding.inflate(getLayoutInflater());
        progressDialog =new ProgressDialog(getContext());
        binding.btnChangePassword.setOnClickListener(v -> {
            onClickChangePassword();
        });
        return binding.getRoot();
    }
    private void onClickChangePassword(){
        String password = binding.edtPassword.getText().toString().trim();
        progressDialog.setMessage("Đang đổi mật khẩu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if (password.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
