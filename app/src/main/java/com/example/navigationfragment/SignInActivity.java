package com.example.navigationfragment;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.navigationfragment.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog =new ProgressDialog(this);
        binding.layoutSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
        binding.btnSignIn.setOnClickListener(v -> {
            onClickSignIn();
        });
    }
    private void onClickSignIn(){
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignInActivity.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                           Intent intent= new Intent(SignInActivity.this, MainActivity.class);
                           startActivity(intent);
                           finishAffinity();
                        } else {

                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}