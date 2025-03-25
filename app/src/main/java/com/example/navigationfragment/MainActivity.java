package com.example.navigationfragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.navigationfragment.databinding.ActivityMainBinding;
import com.example.navigationfragment.databinding.LayoutHearderNavBinding;
import com.example.navigationfragment.fragment.ChangePassWordFragment;
import com.example.navigationfragment.fragment.DoanhThuFragment;
import com.example.navigationfragment.fragment.HoaDonFragment;
import com.example.navigationfragment.fragment.HopDongFragment;
import com.example.navigationfragment.fragment.KhachFragment;
import com.example.navigationfragment.fragment.MyProfileFragment;
import com.example.navigationfragment.fragment.PhongFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final  int MY_REQUEST_CODE=10;
    private static final int PHONG_FRAGMENT = 0;
    private static final int KHACH_FRAGMENT = 1;
    private static final int HOPDONG_FRAGMENT = 2;
    private static final int HOADON_FRAGMENT = 3;
    private static final int DOANHTHU_FRAGMENT = 4;
    private static final int MYPROFILE_FRAGMENT = 5;
    private static final int CHANGEPASSWORD_FRAGMENT = 6;

    private int mCurrentFragment = PHONG_FRAGMENT;
    final private MyProfileFragment mMyProfileFragment = new MyProfileFragment();

    private ActivityMainBinding binding;
    private LayoutHearderNavBinding headerBinding;
    final private ActivityResultLauncher <Intent>  mActivityResultLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK){
                        Intent intent= result.getData();
                        if(intent==null){
                            return;
                        }
                        Uri uri=intent.getData();
                        mMyProfileFragment.setmImageUri(uri);
                        try {
                            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            mMyProfileFragment.setBitmapImage(bitmap);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        setupDrawer();
        setupNavigationHeader();

        // Load fragment mặc định
        replaceFragment(new PhongFragment());
        mCurrentFragment = PHONG_FRAGMENT;setTitle("Quản lý trọ");

        binding.navigationView.getMenu().findItem(R.id.nav_phong).setCheckable(true);
        showUserInformation();
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupNavigationHeader() {
        View headerView = binding.navigationView.getHeaderView(0);
        headerBinding = LayoutHearderNavBinding.bind(headerView);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_phong && mCurrentFragment != PHONG_FRAGMENT) {
            replaceFragment(new PhongFragment());
            mCurrentFragment = PHONG_FRAGMENT;
            setTitle("Phòng");
        } else if (id == R.id.nav_khach && mCurrentFragment != KHACH_FRAGMENT) {
            replaceFragment(new KhachFragment());
            mCurrentFragment = KHACH_FRAGMENT;
            setTitle("Khách thuê");
        } else if (id == R.id.nav_hopdong && mCurrentFragment != HOPDONG_FRAGMENT) {
            replaceFragment(new HopDongFragment());
            mCurrentFragment = HOPDONG_FRAGMENT;
            setTitle("Hợp đồng");
        } else if (id == R.id.nav_hoadon && mCurrentFragment != HOADON_FRAGMENT) {
            replaceFragment(new HoaDonFragment());
            mCurrentFragment = HOADON_FRAGMENT;
            setTitle("Hóa đơn ");
        } else if (id == R.id.nav_doanhthu && mCurrentFragment != DOANHTHU_FRAGMENT) {
            replaceFragment(new DoanhThuFragment());
            mCurrentFragment = DOANHTHU_FRAGMENT;
            setTitle("Doanh thu");
        } else if (id == R.id.nav_dangxuat) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
        else if (id == R.id.nav_myprofile && mCurrentFragment!=MYPROFILE_FRAGMENT) {
            replaceFragment(mMyProfileFragment);
            mCurrentFragment = MYPROFILE_FRAGMENT;
            setTitle("Profile");
        }
        else if (id == R.id.nav_changepassword && mCurrentFragment!=CHANGEPASSWORD_FRAGMENT) {
            replaceFragment(new ChangePassWordFragment());
            mCurrentFragment = CHANGEPASSWORD_FRAGMENT;
            setTitle("Đổi mật khẩu");
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    public void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if (name == null || name.isEmpty()) {
            headerBinding.tvName.setVisibility(View.GONE);
        } else {
            headerBinding.tvName.setVisibility(View.VISIBLE);
            headerBinding.tvName.setText(name);
        }
        headerBinding.tvEmail.setText(email != null ? email : "No email available");

        if (photoUrl != null) {
            Glide.with(this).load(photoUrl).into(headerBinding.imgAvatar);
        } else {
            headerBinding.imgAvatar.setImageResource(R.drawable.ic_avartar_defaul);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
            else {
                Toast.makeText(this, "Vui Lòng Cấp Quyền", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void openGallery(){
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent,"Select Picture"));

    }
}
