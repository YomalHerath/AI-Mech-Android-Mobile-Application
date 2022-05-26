package com.example.aimech.drivers.driver_details;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aimech.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverLogin extends AppCompatActivity {

    //initialize variable
    Button sign_up, sign_in, forgot_password;
    TextInputLayout email, password;
    ImageView back_btn;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_driver_login);

        //initialize reference to views
        sign_up = findViewById(R.id.reg_sign_up);
        sign_in = findViewById(R.id.sign_in_btn);
        forgot_password = findViewById(R.id.forgot_password_btn);
        back_btn = findViewById(R.id.back_btn);
        email = findViewById(R.id.login_email_ed);
        password = findViewById(R.id.login_password_ed);

        progressDialog = new ProgressDialog(DriverLogin.this);

        //Get Authentication
        auth = FirebaseAuth.getInstance();

        //Check User Already Logged In
        if (auth.getCurrentUser() != null) {

            //show progress dialog in waiting time
            progressDialog.setTitle("Please Wait");
            progressDialog.show();

            String userID = auth.getCurrentUser().getUid();

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.getReference().child("Users").child(userID).child("user_type")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int usertype = snapshot.getValue(Integer.class);

                            progressDialog.dismiss();

                            if (usertype == 2) {
                                startActivity(new Intent(getApplicationContext(), DriverDashboard.class));
                                finish();
                            } else {
                                Toast.makeText(DriverLogin.this, "Your account type is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

        //sign up btn click
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DriverRegister.class));
                finish();
            }
        });

        //back icon image click
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverLogin.super.onBackPressed();
            }
        });

        //sign in btn click
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get values from edit text fields
                String userEnteredEmail = email.getEditText().getText().toString().trim();
                String userEnteredPassword = password.getEditText().getText().toString().trim();

                if (userEnteredEmail.isEmpty()) {
                    email.setError("Field cannot be empty");
                } else if (userEnteredPassword.isEmpty()) {
                    password.setError("Field cannot be empty");
                } else {

                    //show progress dialog in waiting time
                    progressDialog.setTitle("User login is in progress");
                    progressDialog.show();

                    //authenticate the user
                    auth.signInWithEmailAndPassword(userEnteredEmail, userEnteredPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //login successful
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                if (user.isEmailVerified()) {

                                    progressDialog.show();
                                    String userID = task.getResult().getUser().getUid();

                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    firebaseDatabase.getReference().child("Users").child(userID).child("user_type")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    int usertype = snapshot.getValue(Integer.class);

                                                    progressDialog.dismiss();

                                                    if (usertype == 2) {
                                                        Toast.makeText(DriverLogin.this, "Login Successful", Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(getApplicationContext(), DriverDashboard.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(DriverLogin.this, "Login failed, your account type is incorrect", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                } else {

                                    user.sendEmailVerification();
                                    Toast.makeText(DriverLogin.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                                }

                            }
                            //login failed
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(DriverLogin.this, "Logging Failed" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        //forgot password
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create a alert dialog
                final EditText resetMail = new EditText(view.getContext());
                final AlertDialog.Builder passwordRestDialog = new AlertDialog.Builder(view.getContext());
                passwordRestDialog.setTitle("Reset Password?");
                passwordRestDialog.setMessage("Enter Your Email To Received Reset Link");
                passwordRestDialog.setView(resetMail);

                //click yes in alert dialog
                passwordRestDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and send the rest link

                        //get the email from user
                        String mail = resetMail.getText().toString();

                        //send password rest link to user
                        auth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DriverLogin.this, "Reset Link sent to your mail", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DriverLogin.this, "Error ! Rest Link Not Sent " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                passwordRestDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close the dialog
                    }
                });
                //display the alert dialog
                passwordRestDialog.create().show();
            }
        });

    }
}