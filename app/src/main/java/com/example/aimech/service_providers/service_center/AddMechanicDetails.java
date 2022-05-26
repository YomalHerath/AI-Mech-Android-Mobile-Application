package com.example.aimech.service_providers.service_center;

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
import com.example.aimech.service_providers.service_provider_details.ServiceProviderDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMechanicDetails extends AppCompatActivity {

    //initialize variable
    ImageView back_btn;
    Button save_btn;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextInputLayout service_center_name, provider_name, provider_address, contact_no, email, service_center_details, service_center_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_mechanic_details);

        //initialize reference to views
        back_btn = findViewById(R.id.back_btn);
        service_center_name = findViewById(R.id.service_center_name);
        provider_name = findViewById(R.id.provider_name);
        provider_address = findViewById(R.id.service_center_address);
        contact_no = findViewById(R.id.provider_contact_no);
        email = findViewById(R.id.provider_email);
        service_center_details = findViewById(R.id.service_center_detail);
        service_center_city = findViewById(R.id.service_center_city);
        save_btn = findViewById(R.id.save_provider_btn);

        //get new progress dialog
        progressDialog = new ProgressDialog(AddMechanicDetails.this);

        //back icon image click
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMechanicDetails.super.onBackPressed();
                finish();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call saving method
                saveProviderDetails();
            }
        });

    }

    //validate fields
    //service center name validation
    private Boolean validateServiceCenterName() {
        String val = service_center_name.getEditText().getText().toString();

        if (val.isEmpty()) {
            service_center_name.setError("Field cannot be empty");
            return false;
        } else {
            service_center_name.setError(null);
            service_center_name.setErrorEnabled(false);
            return true;
        }
    }

    //validate address
    private Boolean validateAddress() {
        String val = provider_address.getEditText().getText().toString();

        if (val.isEmpty()) {
            provider_address.setError("Field cannot be empty");
            return false;
        } else {
            provider_address.setError(null);
            provider_address.setErrorEnabled(false);
            return true;
        }
    }

    //validate provider name
    private Boolean validateProviderName() {
        String val = provider_name.getEditText().getText().toString();

        if (val.isEmpty()) {
            provider_name.setError("Field cannot be empty");
            return false;
        } else {
            provider_name.setError(null);
            provider_name.setErrorEnabled(false);
            return true;
        }
    }

    //validate city
    private Boolean validateCity() {
        String val = service_center_city.getEditText().getText().toString();

        if (val.isEmpty()) {
            service_center_city.setError("Field cannot be empty");
            return false;
        } else {
            service_center_city.setError(null);
            service_center_city.setErrorEnabled(false);
            return true;
        }
    }

    //validate details page
    private Boolean validateDetails() {
        String val = service_center_details.getEditText().getText().toString();

        if (val.isEmpty()) {
            service_center_details.setError("Field cannot be empty");
            return false;
        } else {
            service_center_details.setError(null);
            service_center_details.setErrorEnabled(false);
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
        String val = contact_no.getEditText().getText().toString();

        if (val.isEmpty()) {
            contact_no.setError("Field cannot be empty");
            return false;
        } else {
            contact_no.setError(null);
            contact_no.setErrorEnabled(false);
            return true;
        }
    }

    //details saving method
    public void saveProviderDetails() {

        //performing validation by calling validation functions
        if (!validateServiceCenterName() | !validateProviderName() | !validateCity() | !validateAddress() | !validateDetails() | !validatePhoneNo() | !validateEmail()) {
            return;
        }

        //get all the values from edit text fields
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String s_name = service_center_name.getEditText().getText().toString();
        String s_address = provider_address.getEditText().getText().toString();
        String s_email = email.getEditText().getText().toString();
        String s_contact_no = contact_no.getEditText().getText().toString();
        String s_provider = provider_name.getEditText().getText().toString();
        String s_details = service_center_details.getEditText().getText().toString();
        String s_city = service_center_city.getEditText().getText().toString();

        //show progress dialog in waiting time
        progressDialog.setTitle("Service center details saving in progress");
        progressDialog.show();

        //call to helper class
        MechanicsModel helperClass = new MechanicsModel(uid, s_name, s_provider, s_address, s_contact_no, s_email, s_details, s_city);

        FirebaseDatabase.getInstance().getReference("Service_Providers")
                .push()
                .setValue(helperClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //successfully save data in realtime database
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                            Toast.makeText(AddMechanicDetails.this, "Successfully Add Service Center Details", Toast.LENGTH_LONG).show();

                            //go back to dashboard page
                            Intent intent = new Intent(getApplicationContext(), ServiceProviderDashboard.class);
                            startActivity(intent);
                            finish();
                        }
                        //fail saving data in realtime database
                        else {
                            Toast.makeText(AddMechanicDetails.this, "Failed! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}