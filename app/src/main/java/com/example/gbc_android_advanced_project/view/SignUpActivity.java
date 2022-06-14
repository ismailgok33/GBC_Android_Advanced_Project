package com.example.gbc_android_advanced_project.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gbc_android_advanced_project.R;
import com.example.gbc_android_advanced_project.databinding.ActivitySignUpBinding;
import com.example.gbc_android_advanced_project.models.User;
import com.example.gbc_android_advanced_project.viewmodels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";
    private ActivitySignUpBinding binding;

    private FirebaseAuth mAuth;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.btnSignUp.setOnClickListener(this);
        this.userViewModel = userViewModel.getInstance(this.getApplication());
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.btnSignUp:
                {
                    this.validateData();
                    break;
                }
            }
        }
    }

    private void validateData()
    {
        Boolean validData = true;
        String email = "";
        String password = "";
        String firstName = "";
        String lastName = "";

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

        firstName = this.binding.etFirstname.getText().toString();
        lastName = this.binding.etLastname.getText().toString();

        if (validData){
            this.singUp(email, password, firstName, lastName);
        }else{
            Toast.makeText(this, "Please provide correct inputs", Toast.LENGTH_SHORT).show();
        }
    }
    private void singUp(String email, String password, String firstName, String lastName){

        this.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.e(TAG, "onComplete: Sign In Successful");
                    String uid = mAuth.getCurrentUser().getUid();
                    User newUser = new User(uid, email, firstName, lastName);
                    userViewModel.addUser(newUser);
                    goToMain();
                }else{
                    Log.e(TAG, "onComplete: Sign Up Failed" + task.getException().getLocalizedMessage() );
                    Toast.makeText(SignUpActivity.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void goToMain(){
        this.finishAffinity();
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

}