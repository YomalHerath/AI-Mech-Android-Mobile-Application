package com.example.aimech.user.user_detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aimech.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    //initialize variables
    TextView field_full_name, field_name, field_email, field_phoneNo;
    ImageView back_btn;

    FirebaseUser user;
    DatabaseReference databaseReference;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);

        //initialize reference to views
        field_full_name = findViewById(R.id.profile_field_full_name);
        field_name = findViewById(R.id.profile_field_user_name);
        field_email = findViewById(R.id.profile_field_email);
        field_phoneNo = findViewById(R.id.profile_field_phoneNo);
        back_btn = findViewById(R.id.back_btn);

        //get current user's user id from authentication
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        //get user details from realtime database
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails userProfile = snapshot.getValue(UserDetails.class);

                if (userProfile != null) {
                    String fullName = userProfile.getFull_name();
                    String username = userProfile.getUsername();
                    String email = userProfile.getEmail();
                    String phoneNo = userProfile.getPhone_no();

                    field_full_name.setText(fullName);
                    field_name.setText(username);
                    field_email.setText(email);
                    field_phoneNo.setText(phoneNo);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UserProfile.this, "Something Wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        //icon back btn click
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserProfile.super.onBackPressed();
            }
        });
    }
}