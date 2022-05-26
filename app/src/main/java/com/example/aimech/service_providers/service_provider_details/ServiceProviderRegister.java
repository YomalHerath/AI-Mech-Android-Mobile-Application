package com.example.aimech.service_providers.service_provider_details;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aimech.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ServiceProviderRegister extends AppCompatActivity {

    //initialize variable
    Button sign_in, sign_up;
    ImageView back_btn;
    TextInputLayout reg_full_name, reg_username, reg_email, reg_mobile_no, reg_password, reg_confirm_password;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_service_provider_register);

        //initialize reference to views
        sign_in = findViewById(R.id.login_sign_in);
        sign_up = findViewById(R.id.reg_sign_up_btn);
        back_btn = findViewById(R.id.back_btn);
        reg_full_name = findViewById(R.id.reg_sp_full_name_ed);
        reg_username = findViewById(R.id.reg_sp_username_ed);
        reg_email = findViewById(R.id.reg_sp_email_ed);
        reg_mobile_no = findViewById(R.id.reg_sp_mobile_ed);
        reg_password = findViewById(R.id.reg_sp_password_ed);
        reg_confirm_password = findViewById(R.id.reg_sp_confirm_pass_ed);

        //get new progress dialog
        progressDialog = new ProgressDialog(ServiceProviderRegister.this);

        //call authentication
        auth = FirebaseAuth.getInstance();

        //sign in text click
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ServiceProviderLogin.class));
                finish();
            }
        });

        //back icon btn click
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceProviderRegister.super.onBackPressed();
            }
        });

        //sign up btn click
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the root node of realtime database
                firebaseDatabase = FirebaseDatabase.getInstance();
                //call the reference of realtime database
                databaseReference = firebaseDatabase.getReference("Users");

                //method to register new user with authentication
                registerUser();
            }
        });
    }

    //validate fields
    //full name validation
    private Boolean validateFullName() {
        String val = reg_full_name.getEditText().getText().toString();

        if (val.isEmpty()) {
            reg_full_name.setError("Field cannot be empty");
            return false;
        } else {
            reg_full_name.setError(null);
            reg_full_name.setErrorEnabled(false);
            return true;
        }
    }

    //username validation
    private Boolean validateUserName() {
        String val = reg_username.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            reg_full_name.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            reg_full_name.setError("White spaces are not allowed");
            return false;
        } else {
            reg_full_name.setError(null);
            reg_full_name.setErrorEnabled(false);
            return true;
        }
    }

    //email validation
    private Boolean validateEmail() {
        String val = reg_email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            reg_email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            reg_email.setError("Invalid email address");
            return false;
        } else {
            reg_email.setError(null);
            reg_email.setErrorEnabled(false);
            return true;
        }
    }

    //phone no validation
    private Boolean validatePhoneNo() {
        String val = reg_mobile_no.getEditText().getText().toString();

        if (val.isEmpty()) {
            reg_mobile_no.setError("Field cannot be empty");
            return false;
        } else {
            reg_mobile_no.setError(null);
            reg_mobile_no.setErrorEnabled(false);
            return true;
        }
    }

    //password validation
    private Boolean validatePassword() {
        String val = reg_password.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])" + //at least 1 digit
                "(?=.*[a-zA-Z])" + //any letter
                "(?=.*[@#$%^&+=])" + //at least 1 special character
                ".{4,}" + //at least 4 characters
                "$";

        if (val.isEmpty()) {
            reg_password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            reg_password.setError("Password too weak");
            return false;
        } else {
            reg_password.setError(null);
            reg_password.setErrorEnabled(false);
            return true;
        }
    }

    //confirm password validation
    private Boolean validateConfirmPassword() {
        String val = reg_confirm_password.getEditText().getText().toString();

        if (val.isEmpty()) {
            reg_confirm_password.setError("Field cannot be empty");
            return false;
        } else if (!val.equals(reg_password.getEditText().getText().toString())) {
            reg_confirm_password.setError("Password and confirm password doesn't match");
            return false;

        } else {
            reg_confirm_password.setError(null);
            reg_confirm_password.setErrorEnabled(false);
            return true;
        }
    }

    //this function execute when user click on sign in btn to save data in firebase
    public void registerUser() {

        //performing validation by calling validation functions
        if (!validateFullName() | !validateUserName() | !validateEmail() | !validatePhoneNo() | !validatePassword() | !validateConfirmPassword()) {
            return;
        }

        //get all the values from edit text fields
        String full_name = reg_full_name.getEditText().getText().toString();
        String username = reg_username.getEditText().getText().toString();
        String email = reg_email.getEditText().getText().toString();
        String phone_no = reg_mobile_no.getEditText().getText().toString();
        String password = reg_password.getEditText().getText().toString();

        //show progress dialog in waiting time
        progressDialog.setTitle("Service Provider registration is in progress");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //registration successful in authentication
                if (task.isSuccessful()) {

                    FirebaseUser user;

                    //get current user's user id from authentication
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    String uid = user.getUid();

                    //call to helper class
                    ServiceProviderDetails helperClass = new ServiceProviderDetails(uid, full_name, username, email, phone_no, 2);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //register successful in saving data in realtime database
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();

                                Toast.makeText(ServiceProviderRegister.this, "Your Account has been created successfully", Toast.LENGTH_LONG).show();

                                //calling to login page
                                Intent intent = new Intent(getApplicationContext(), ServiceProviderLogin.class);
                                startActivity(intent);
                                finish();
                            }
                            //register fail in saving data in realtime database
                            else {
                                Toast.makeText(ServiceProviderRegister.this, "Failed Registration! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                //register failed in authentication
                else {
                    progressDialog.dismiss();
                    Toast.makeText(ServiceProviderRegister.this, "Failed Registration! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
