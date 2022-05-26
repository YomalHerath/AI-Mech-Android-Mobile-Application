package com.example.aimech.drivers.create_driver;

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
import com.example.aimech.drivers.driver_details.DriverDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDriverDetails extends AppCompatActivity {

    //initialize variable
    ImageView back_btn;
    TextInputLayout full_name, address, city, mobile_no, email, birth_year, license_no;
    Button save_btn;
    ProgressDialog progressDialog;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_driver_details);

        //initialize reference to views
        full_name = findViewById(R.id.driver_full_name);
        address = findViewById(R.id.driver_address);
        city = findViewById(R.id.driver_city);
        mobile_no = findViewById(R.id.driver_phone_no);
        email = findViewById(R.id.driver_email);
        birth_year = findViewById(R.id.driver_birth_year);
        license_no = findViewById(R.id.driver_license);
        back_btn = findViewById(R.id.back_btn);
        save_btn = findViewById(R.id.save_driver_btn);

        //get new progress dialog
        progressDialog = new ProgressDialog(AddDriverDetails.this);

        String current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Drivers").child(current_uid);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call saving method
                saveDriverDetails();
            }
        });

        //back icon image click
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDriverDetails.super.onBackPressed();
                finish();
            }
        });
    }

    //validate fields
    //full name validation
    private Boolean validateFullName() {
        String val = full_name.getEditText().getText().toString();

        if (val.isEmpty()) {
            full_name.setError("Field cannot be empty");
            return false;
        } else {
            full_name.setError(null);
            full_name.setErrorEnabled(false);
            return true;
        }
    }

    //address validation
    private Boolean validateAddress() {
        String val = address.getEditText().getText().toString();

        if (val.isEmpty()) {
            address.setError("Field cannot be empty");
            return false;
        } else {
            address.setError(null);
            address.setErrorEnabled(false);
            return true;
        }
    }

    //city validation
    private Boolean validateCity() {
        String val = city.getEditText().getText().toString();

        if (val.isEmpty()) {
            city.setError("Field cannot be empty");
            return false;
        } else {
            city.setError(null);
            city.setErrorEnabled(false);
            return true;
        }
    }

    //email validation
    private Boolean validateEmail() {
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    //phone no validation
    private Boolean validatePhoneNo() {
        String val = mobile_no.getEditText().getText().toString();

        if (val.isEmpty()) {
            mobile_no.setError("Field cannot be empty");
            return false;
        } else {
            mobile_no.setError(null);
            mobile_no.setErrorEnabled(false);
            return true;
        }
    }

    //birth year validation
    private Boolean validateBirthYear() {
        String val = birth_year.getEditText().getText().toString();

        if (val.isEmpty()) {
            birth_year.setError("Field cannot be empty");
            return false;
        } else {
            birth_year.setError(null);
            birth_year.setErrorEnabled(false);
            return true;
        }
    }

    //license no validation
    private Boolean validateLicenseNo() {
        String val = license_no.getEditText().getText().toString();

        if (val.isEmpty()) {
            license_no.setError("Field cannot be empty");
            return false;
        } else {
            license_no.setError(null);
            license_no.setErrorEnabled(false);
            return true;
        }
    }

    //details saving method
    public void saveDriverDetails() {

        //performing validation by calling validation functions
        if (!validateFullName() | !validateAddress() | !validateCity() | !validatePhoneNo() | !validateEmail() | !validateBirthYear() | !validateLicenseNo()) {
            return;
        }

        //get all the values from edit text fields
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String d_full_name = full_name.getEditText().getText().toString();
        String d_address = address.getEditText().getText().toString();
        String d_city = city.getEditText().getText().toString();
        String d_phone_no = mobile_no.getEditText().getText().toString();
        String d_email = email.getEditText().getText().toString();
        String d_birth_year = birth_year.getEditText().getText().toString();
        String d_license_no = license_no.getEditText().getText().toString();

        //show progress dialog in waiting time
        progressDialog.setTitle("Driver details saving in progress");
        progressDialog.show();

        //call to helper class
        DriverModel helperClass = new DriverModel(uid, d_full_name, d_address, d_city, d_email, d_phone_no, d_birth_year, d_license_no);

        FirebaseDatabase.getInstance().getReference("Drivers")
                .child(uid)
                .setValue(helperClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //successfully save data in realtime database
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                            Toast.makeText(AddDriverDetails.this, "Successfully Add Driver Details", Toast.LENGTH_LONG).show();

                            //go back to vehicle details page
                            Intent intent = new Intent(getApplicationContext(), DriverDashboard.class);
                            startActivity(intent);
                            finish();
                        }
                        //fail saving data in realtime database
                        else {
                            Toast.makeText(AddDriverDetails.this, "Failed! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}