package com.example.aimech.faq_chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimech.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FaqSection extends AppCompatActivity {

    //initialize variables
    RecyclerView chatRv;
    EditText etMessage;
    ImageButton send_btn;
    ImageView back_btn;

    ArrayList<FaqModel> faqList;
    FaqAdapter faqAdapter;

    FirebaseAuth firebaseAuth;

    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_faq_section);

        //initialize reference to views
        chatRv = findViewById(R.id.chatRv);
        etMessage = findViewById(R.id.faq_et_message);
        send_btn = findViewById(R.id.faq_send_btn);
        back_btn = findViewById(R.id.back_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();

        //load faq messages
        loadFaqMessages();

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input data
                String message = etMessage.getText().toString().trim();
                //validate
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(FaqSection.this, "Can't send empty message..", Toast.LENGTH_SHORT).show();
                } else {
                    //send message
                    sendMessage(message);
                }
            }
        });

        //icon back btn click
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FaqSection.super.onBackPressed();
            }
        });

    }

    //load message
    private void loadFaqMessages() {

        faqList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FAQ");
        ref.child("Messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        faqList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            FaqModel model = ds.getValue(FaqModel.class);
                            faqList.add(model);
                        }
                        //adapter
                        faqAdapter = new FaqAdapter(FaqSection.this, faqList);
                        //set to recyclerview
                        chatRv.setAdapter(faqAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    //send message
    private void sendMessage(String message) {
        String timestamp = "" + System.currentTimeMillis();

        //setup message data
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", "" + firebaseAuth.getUid());
        hashMap.put("message", "" + message);
        hashMap.put("timestamp", "" + timestamp);
        hashMap.put("type", "" + "text"); //text//image//file

        //add message to database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FAQ");
        ref.child("Messages").child(timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //message sent
                        //clear messageEt
                        etMessage.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //sending failed
                        Toast.makeText(FaqSection.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}