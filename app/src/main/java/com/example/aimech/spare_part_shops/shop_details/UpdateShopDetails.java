package com.example.aimech.spare_part_shops.shop_details;

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
import com.example.aimech.spare_part_shops.shop_owner_details.ShopDashboard;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateShopDetails extends AppCompatActivity {

    //initialize variable
    ImageView back_btn;
    Button update_btn, remove_btn;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextInputEditText shop_name, shop_address, open_time, close_time, contact_person, contact_no, email, shop_details, shop_city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_shop_details);

        //initialize reference to views
        back_btn = findViewById(R.id.back_btn);
        shop_name = findViewById(R.id.update_shop_name);
        shop_address = findViewById(R.id.update_shop_address);
        open_time = findViewById(R.id.update_shop_open_time);
        close_time = findViewById(R.id.update_shop_close_time);
        contact_person = findViewById(R.id.update_shop_contact_person);
        contact_no = findViewById(R.id.update_shop_contact_no);
        email = findViewById(R.id.update_shop_email);
        shop_details = findViewById(R.id.update_shop_detail);
        shop_city = findViewById(R.id.update_shop_city);
        update_btn = findViewById(R.id.update_shop_btn);
        remove_btn = findViewById(R.id.remove);

        //get new progress dialog
        progressDialog = new ProgressDialog(UpdateShopDetails.this);

        //Get Key with intent
        String ShopKey = getIntent().getStringExtra("ShopKey");

        databaseReference = FirebaseDatabase.getInstance().getReference("Spare_Part_Shops").child(ShopKey);

        //back icon image click
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateShopDetails.super.onBackPressed();
                finish();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Get Values from Firebase Realtime Database
                if (snapshot.exists()) {
                    String u_shop_name = snapshot.child("shop_name").getValue().toString();
                    String u_shop_address = snapshot.child("shop_address").getValue().toString();
                    String u_open_time = snapshot.child("open_time").getValue().toString();
                    String u_close_time = snapshot.child("close_time").getValue().toString();
                    String u_contact_no = snapshot.child("contact_no").getValue().toString();
                    String u_contact_person = snapshot.child("contact_person").getValue().toString();
                    String u_email = snapshot.child("email").getValue().toString();
                    String u_shop_details = snapshot.child("shop_details").getValue().toString();
                    String u_shop_city = snapshot.child("shop_located").getValue().toString();

                    //set values to fields
                    shop_name.setText(u_shop_name);
                    shop_address.setText(u_shop_address);
                    open_time.setText(u_open_time);
                    close_time.setText(u_close_time);
                    email.setText(u_email);
                    contact_no.setText(u_contact_no);
                    contact_person.setText(u_contact_person);
                    shop_details.setText(u_shop_details);
                    shop_city.setText(u_shop_city);

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
                String u_shop_name = shop_name.getText().toString();
                String u_shop_address = shop_address.getText().toString();
                String u_open_time = open_time.getText().toString();
                String u_close_time = close_time.getText().toString();
                String u_contact_no = contact_no.getText().toString();
                String u_contact_person = contact_person.getText().toString();
                String u_email = email.getText().toString();
                String u_shop_details = shop_details.getText().toString();
                String u_shop_city = shop_city.getText().toString();

                //Set Values to Update the Firebase Realtime database value using HashMap
                HashMap hashMap = new HashMap();
                hashMap.put("shop_name", u_shop_name);
                hashMap.put("shop_address", u_shop_address);
                hashMap.put("open_time", u_open_time);
                hashMap.put("close_time", u_close_time);
                hashMap.put("contact_no", u_contact_no);
                hashMap.put("contact_person", u_contact_person);
                hashMap.put("email", u_email);
                hashMap.put("shop_details", u_shop_details);
                hashMap.put("shop_located", u_shop_city);

                databaseReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(UpdateShopDetails.this, "Data was Successfully Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ShopDashboard.class));
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
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateShopDetails.this);

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

                                startActivity(new Intent(getApplicationContext(), ShopDashboard.class));
                                Toast.makeText(UpdateShopDetails.this, "Successfully Shop Details Deleted", Toast.LENGTH_SHORT).show();
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