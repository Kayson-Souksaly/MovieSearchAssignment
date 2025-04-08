package com.example.moviesearchassignment.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moviesearchassignment.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Check if the fields are empty and if the passwords match
                String username = binding.usernameInput.getText() != null ? binding.usernameInput.getText().toString().trim() : "";
                String email = binding.emailInput.getText() != null ? binding.emailInput.getText().toString().trim() : "";
                String password = binding.passwordInput.getText() != null ? binding.passwordInput.getText().toString().trim() : "";
                String confirmPassword = binding.confirmPasswordInput.getText() != null ? binding.confirmPasswordInput.getText().toString().trim() : "";

//                if the fields are empty, show a toast
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if the passwords do not match, show a toast
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

//                register the user
                registerUser(username, email, password);
            }
        });

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void registerUser(String username, String email, String password) {
        Log.d("tag", "registerUser: " + email + " " + password);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("username", username);
                            userMap.put("email", email);


                            db.collection("users")
                                    .document(uid)
                                    .set(userMap)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intentObj = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intentObj);
                                        finish();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(RegisterActivity.this, "Failed to save username", Toast.LENGTH_SHORT).show());
                        } else {

                            Log.d("tag", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

}