package com.example.aimech.user.spare_part_shop;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aimech.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShopView extends AppCompatActivity {

    //initialize variables
    TextView shop_name, address, details, located, name, contact, email, open, close;
    ImageView back_btn;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shop_view);

        shop_name = findViewById(R.id.shop_name);
        details = findViewById(R.id.shop_detail);
        address = findViewById(R.id.shop_address);
        located = findViewById(R.id.shop_located);
        name = findViewById(R.id.shop_owner);
        contact = findViewById(R.id.shop_contact_no);
        email = findViewById(R.id.shop_email);
        open = findViewById(R.id.shop_open);
        close = findViewById(R.id.shop_close);
        back_btn = findViewById(R.id.back_btn);

        //Get Key with Intent
        String ShopKey = getIntent().getStringExtra("ShopKey");

        databaseReference = FirebaseDatabase.getInstance().getReference("Spare_Part_Shops").child(ShopKey);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Get values from Firebase Realtime Database
                if (snapshot.exists()) {

                    String vclose = snapshot.child("close_time").getValue().toString();
                    String vcontact = snapshot.child("contact_no").getValue().toString();
                    String vperson = snapshot.child("contact_person").getValue().toString();
                    String vemail = snapshot.child("email").getValue().toString();
                    String vopen = snapshot.child("open_time").getValue().toString();
                    String vaddress = snapshot.child("shop_address").getValue().toString();
                    String vdetails = snapshot.child("shop_details").getValue().toString();
                    String vlocated = snapshot.child("shop_located").getValue().toString();
                    String vshop_name = snapshot.child("shop_name").getValue().toString();

                    shop_name.setText(vshop_name);
                    details.setText(vdetails);
                    address.setText(vaddress);
                    name.setText(vperson);
                    close.setText(vclose);
                    open.setText(vopen);
                    contact.setText(vcontact);
                    email.setText(vemail);
                    located.setText(vlocated);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //back btn Press
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopView.super.onBackPressed();
                finish();
            }
        });
    }
}