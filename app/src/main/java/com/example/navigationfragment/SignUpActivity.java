package com.example.navigationfragment;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.navigationfragment.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {


   ActivitySignUpBinding binding;
   private FirebaseAuth firebaseAuth;
   private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        progressDialog = new ProgressDialog(this);

        binding.btnsignup.setOnClickListener(v -> {
            onClickSignUp();
        });
        }
        public  void onClickSignUp(){
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent= new Intent(SignUpActivity.this,
                                        MainActivity.class);
                                startActivity(intent);
                                finishAffinity();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    private void loginUser() {
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.setError("Valid email required");
            binding.edtEmail.requestFocus();
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            binding.edtPassword.setError("Password must be at least 6 characters");
            binding.edtPassword.requestFocus();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                            // Navigate to MainActivity or Dashboard
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        }
                    } else {
                        Log.e("Login", "Login failed: " + task.getException().getMessage());
                        Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    }