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

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Log and Toast for debugging
                Log.d("TAG", "onClick: Register Button Clicked");
                Toast.makeText(RegisterActivity.this, "User Registered:" + Objects.requireNonNull(binding.emailInput.getText()).toString(), Toast.LENGTH_SHORT).show();

                String email = Objects.requireNonNull(binding.emailInput.getText()).toString();
                String password = Objects.requireNonNull(binding.passwordInput.getText()).toString();
                registerUser(email, password);
            }
        });

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void registerUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Registration Successful" + user.getUid(), Toast.LENGTH_SHORT).show();

                            Intent intentObj = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intentObj);

                        } else {

                            Log.d("tag", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

}