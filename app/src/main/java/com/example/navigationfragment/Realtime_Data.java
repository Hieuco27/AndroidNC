package com.example.navigationfragment;

import static android.service.controls.ControlsProviderService.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.navigationfragment.database.Job;
import com.example.navigationfragment.database.User;
import com.example.navigationfragment.databinding.ActivityRealtimeDataBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;

public class Realtime_Data extends AppCompatActivity {

    ActivityRealtimeDataBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityRealtimeDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);


        binding.btnPushData.setOnClickListener(v -> {
            onCLickPushData();
        });
        binding.btnGetData.setOnClickListener(v -> {
            onClickGetData();
        });
        binding.btnDeleteData.setOnClickListener(v ->{
            RemoveData();
        });
    }
    public void onCLickPushData() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user_infor");

        User user = new User( 1, "hieu", new Job(1, "Developer"));
        user.setAddress("HN");


        myRef.setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(Realtime_Data.this, "Data pushed successfully", Toast.LENGTH_SHORT).show();
            }
        });
        database.getReference("user_infor/age").setValue("30");
    }

    public  void onClickGetData(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user_infor");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    binding.tvData.setText(user.toString()); // Hiển thị dữ liệu lên giao diện
                } else {
                    Log.d(TAG, "No data found at 'message' reference.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        });
    }
    private void RemoveData(){
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("user_info");
        myRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(Realtime_Data.this,"Data removed successfully",Toast.LENGTH_SHORT).show();
            }
        });

    }
}