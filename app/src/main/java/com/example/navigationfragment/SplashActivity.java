package com.example.navigationfragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        // Hiệu ứng Fade-In cho Logo
        ImageView logo = findViewById(R.id.imgLogo);
        TextView appName = findViewById(R.id.txtAppName);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(2000);
        logo.startAnimation(fadeIn);
        appName.startAnimation(fadeIn);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                nextActivity();
            }
        }, 3000);
    }
    private void nextActivity() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Intent intent= new Intent(this, SignInActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent= new Intent(this, MainActivity.class);
            startActivity(intent);

        }
        finish();
    }
}


