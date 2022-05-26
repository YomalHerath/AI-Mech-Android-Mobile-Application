package com.example.aimech.drivers.create_driver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aimech.R;
import com.example.aimech.drivers.driver_details.DriverDashboard;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateDriverDetails extends AppCompatActivity {

    //initialize variable
    ImageView back_btn;
    TextInputEditText u_full_name, u_address, u_city, u_mobile_no, u_email, u_birth_year, u_license_no;
    Button update_btn, remove_btn;
    ProgressDialog progressDialog;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_driver_details);

        //initialize reference to views
        u_full_name = findViewById(R.id.u_driver_full_name);
        u_address = findViewById(R.id.u_driver_address);
        u_city = findViewById(R.id.u_driver_city);
        u_mobile_no = findViewById(R.id.u_driver_phone_no);
        u_email = findViewById(R.id.u_driver_email);
        u_birth_year = findViewById(R.id.u_driver_birth_year);
        u_license_no = findViewById(R.id.u_driver_license);
        back_btn = findViewById(R.id.back_btn);
        update_btn = findViewById(R.id.update_driver_btn);
        remove_btn = findViewById(R.id.remove);

        //get new progress dialog
        progressDialog = new ProgressDialog(UpdateDriverDetails.this);

        String current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Drivers").child(current_uid);

        //back icon image click
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateDriverDetails.super.onBackPressed();
                finish();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Get Values from Firebase Realtime Database
                if (snapshot.exists()) {
                    String full_name = snapshot.child("full_name").getValue().toString();
                    String address = snapshot.child("address").getValue().toString();
                    String city = snapshot.child("city").getValue().toString();
                    String mobile_no = snapshot.child("mobile_no").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    String birth_year = snapshot.child("birth_year").getValue().toString();
                    String license_no = snapshot.child("license_no").getValue().toString();

                    //set values to fields
                    u_full_name.setText(full_name);
                    u_address.setText(address);
                    u_city.setText(city);
                    u_mobile_no.setText(mobile_no);
                    u_email.setText(email);
                    u_birth_year.setText(birth_year);
                    u_license_no.setText(license_no);
                } else {
                    Toast.makeText(UpdateDriverDetails.this, "First You have to add Driver Details", Toast.LENGTH_SHORT).show();
                    //go back to dashboard
                    Intent intent = new Intent(getApplicationContext(), DriverDashboard.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Update Function
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get Values from Edit Fields
                String update_full_name = u_full_name.getText().toString();
                String update_address = u_address.getText().toString();
                String update_city = u_city.getText().toString();
                String update_mobile_no = u_mobile_no.getText().toString();
                String update_email = u_email.getText().toString();
                String update_birth_year = u_birth_year.getText().toString();
                String update_license_no = u_license_no.getText().toString();

                //Set Values to Update the Firebase Realtime database value using HashMap
                HashMap hashMap = new HashMap();
                hashMap.put("full_name", update_full_name);
                hashMap.put("address", update_address);
                hashMap.put("city", update_city);
                hashMap.put("mobile_no", update_mobile_no);
                hashMap.put("email", update_email);
                hashMap.put("birth_year", update_birth_year);
                hashMap.put("license_no", update_license_no);

                databaseReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(UpdateDriverDetails.this, "Data was Successfully Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), DriverDashboard.class));
                        finish();
                    }
                });
            }
        });

        //Delete Function
        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create Alert Button
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDriverDetails.this);

                //Alert Messages to show
                builder.setTitle("Delete");
                builder.setMessage("Are you sure, You want to Delete this ?");

                //Delete in Click Yes
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                startActivity(new Intent(getApplicationContext(), DriverDashboard.class));
                                Toast.makeText(UpdateDriverDetails.this, "Successfully Driver Details Deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });

                //When Click No button
                builder.setNegativeButton("N0", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                builder.create().show();
            }
        });

    }
}