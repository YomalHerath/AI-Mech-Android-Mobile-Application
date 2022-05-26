package com.example.aimech.service_providers.service_center;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateMechanicDetails extends AppCompatActivity {

    //initialize variable
    ImageView back_btn;
    Button update_btn, remove_btn;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextInputEditText service_center_name, provider_name, provider_address, contact_no, email, service_center_details, service_center_city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_mechanic_details);

        //initialize reference to views
        back_btn = findViewById(R.id.back_btn);
        service_center_name = findViewById(R.id.u_service_center_name);
        provider_name = findViewById(R.id.u_provider_name);
        provider_address = findViewById(R.id.u_service_center_address);
        contact_no = findViewById(R.id.u_provider_contact_no);
        email = findViewById(R.id.u_provider_email);
        service_center_details = findViewById(R.id.u_service_center_detail);
        service_center_city = findViewById(R.id.u_service_center_city);
        update_btn = findViewById(R.id.update_provider_btn);
        remove_btn = findViewById(R.id.remove);

        //get new progress dialog
        progressDialog = new ProgressDialog(UpdateMechanicDetails.this);

        //Get Key with intent
        String ServiceKey = getIntent().getStringExtra("ServiceKey");

        databaseReference = FirebaseDatabase.getInstance().getReference("Service_Providers").child(ServiceKey);

        //back icon image click
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateMechanicDetails.super.onBackPressed();
                finish();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Get Values from Firebase Realtime Database
                if (snapshot.exists()) {
                    String u_contact_no = snapshot.child("contact_no").getValue().toString();
                    String u_email = snapshot.child("email").getValue().toString();
                    String u_located = snapshot.child("located").getValue().toString();
                    String u_address = snapshot.child("mechanic_address").getValue().toString();
                    String u_name = snapshot.child("mechanic_name").getValue().toString();
                    String u_service_details = snapshot.child("service_center_details").getValue().toString();
                    String u_service_name = snapshot.child("service_center_name").getValue().toString();

                    //set values to fields
                    service_center_name.setText(u_service_name);
                    provider_name.setText(u_name);
                    provider_address.setText(u_address);
                    contact_no.setText(u_contact_no);
                    email.setText(u_email);
                    service_center_details.setText(u_service_details);
                    service_center_city.setText(u_located);
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
                String u_contact_no = contact_no.getText().toString();
                String u_email = email.getText().toString();
                String u_located = service_center_city.getText().toString();
                String u_address = provider_address.getText().toString();
                String u_name = provider_name.getText().toString();
                String u_service_details = service_center_details.getText().toString();
                String u_service_name = service_center_name.getText().toString();

                //Set Values to Update the Firebase Realtime database value using HashMap
                HashMap hashMap = new HashMap();
                hashMap.put("contact_no", u_contact_no);
                hashMap.put("email", u_email);
                hashMap.put("located", u_located);
                hashMap.put("mechanic_address", u_address);
                hashMap.put("mechanic_name", u_name);
                hashMap.put("service_center_details", u_service_details);
                hashMap.put("service_center_name", u_service_name);

                databaseReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(UpdateMechanicDetails.this, "Data was Successfully Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ServiceCenterList.class));
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
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMechanicDetails.this);

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

                                startActivity(new Intent(getApplicationContext(), ServiceCenterList.class));
                                Toast.makeText(UpdateMechanicDetails.this, "Successfully Service Provider Details Deleted", Toast.LENGTH_SHORT).show();
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