package com.example.gbc_android_advanced_project.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gbc_android_advanced_project.R;
import com.example.gbc_android_advanced_project.databinding.ActivitySignInBinding;
import com.example.gbc_android_advanced_project.viewmodels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignInActivity";

    private ActivitySignInBinding binding;

    private FirebaseAuth mAuth;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.signIn.setOnClickListener(this);
        this.binding.btnLogin.setOnClickListener(this);


        this.mAuth = FirebaseAuth.getInstance();
        this.userViewModel = UserViewModel.getInstance(this.getApplication());
    }

    @Override
    public void onClick(View view) {
        if (view != null){
            switch (view.getId()){
                case R.id.signIn:
                {
                    this.singIn();
                    break;
                }
                case R.id.btnLogin:
                {
                    this.validate();
                    break;
                }
            }

        }
    }
    private void singIn()
    {
        Log.d(TAG,"ABc");
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void login(String email,String password)
    {
        this.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.e(TAG, "onComplete: Login In Successful");
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Log.e(TAG, "onComplete: Sign In Failed" + task.getException().getLocalizedMessage() );
                    Toast.makeText(SignInActivity.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void validate() {
        Boolean validData = true;
        String email = "";
        String password = "";

        if (this.binding.email.getText().toString().isEmpty()){
            this.binding.email.setError("Email Cannot be Empty");
            validData = false;
        }else{
            email = this.binding.email.getText().toString();
        }

        if (this.binding.password.getText().toString().isEmpty()){
            this.binding.password.setError("Password Cannot be Empty");
            validData = false;
        }else {
            password = this.binding.password.getText().toString();
        }

        if (validData){
            this.login(email, password);
        }else{
            Toast.makeText(this, "Please provide correct inputs", Toast.LENGTH_SHORT).show();
        }

    }

}