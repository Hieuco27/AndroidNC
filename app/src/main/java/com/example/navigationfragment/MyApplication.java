package com.example.navigationfragment;

import android.app.Application;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo Firebase
        FirebaseApp.initializeApp(this);
    }
}