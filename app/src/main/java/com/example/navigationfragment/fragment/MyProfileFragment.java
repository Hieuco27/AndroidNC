package com.example.navigationfragment.fragment;

import static android.content.ContentValues.TAG;
import static com.example.navigationfragment.MainActivity.MY_REQUEST_CODE;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.navigationfragment.MainActivity;
import com.example.navigationfragment.databinding.FragmentMyprofileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MyProfileFragment extends Fragment {
    FragmentMyprofileBinding binding;
    private Uri mImageUri;
    private MainActivity mMainActivity;
    ProgressDialog progressDialog;
    @Nullable
    @Override
    public   View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        binding= FragmentMyprofileBinding.inflate(inflater,container,false);
        mMainActivity=(MainActivity) getActivity();
        progressDialog=new ProgressDialog(getContext());
        setUserInformation();
        initListener();
        return binding.getRoot();
    }


    private void setUserInformation(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (user==null) return;
        String name=user.getDisplayName();
        String email=user.getEmail();
        binding.edtFullName.setText(name);
        binding.edtEmail.setText(email);
        Glide.with(this).load(user.getPhotoUrl()).into(binding.imgAvatar);
    }
    private void initListener(){
        binding.imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });
        binding.btnUpdateProfile.setOnClickListener(v -> {
            onClickUpdateProfile();
        });
      /*  binding.btnUpdateEmail.setOnClickListener(v -> {
            onClickUpdateEmail();
        });*/
    }
    private  void onClickRequestPermission(){

        if(mMainActivity==null) {
            return;
        }
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.M){

            mMainActivity.openGallery();
            return;
        }
        if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            mMainActivity.openGallery();
            return;
        }
        else{
            String[] premissions={Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(premissions,MY_REQUEST_CODE);
        }

    }
    public void setBitmapImage(Bitmap bitmap){
        binding.imgAvatar.setImageBitmap(bitmap);
    }

    public void setmImageUri(Uri mImageUri) {
        this.mImageUri = mImageUri;
    }

    private void onClickUpdateProfile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog.show();
        if (user == null) {
            return;
        }
        String fullname= binding.edtFullName.getText().toString();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullname)
                .setPhotoUri(mImageUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            mMainActivity.showUserInformation();
                        } else {
                            Log.e(TAG, "Error updating profile", task.getException());
                            Toast.makeText(getActivity(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void onClickUpdateEmail(){
        String newEmail=  binding.edtEmail.getText().toString().trim();
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"Email updated successfully",Toast.LENGTH_SHORT).show();
                            mMainActivity.showUserInformation();
                        }
                        else {
                            Log.e(TAG,"Error updating email",task.getException());
                            Toast.makeText(getActivity(),"Failed to update email",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}
