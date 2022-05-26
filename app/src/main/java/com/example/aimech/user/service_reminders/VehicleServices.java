package com.example.aimech.user.service_reminders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimech.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class VehicleServices extends AppCompatActivity {

    //initialize variable
    ImageView back_btn;
    Button set_reminder;
    DatabaseReference databaseReference;
    String userId;

    RecyclerView recyclerView;

    //Get Model class to firebase recycler options
    FirebaseRecyclerOptions<ReminderDetails> options;
    FirebaseRecyclerAdapter<ReminderDetails, ReminderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vehicle_services);

        //initialize reference to views
        back_btn = findViewById(R.id.back_btn);
        set_reminder = findViewById(R.id.set_reminder_btn);
        recyclerView = findViewById(R.id.reminderRecyclerView);

        //Pass new Layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        //get current user's user id from authentication
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Service_Reminders").child(userId);

        //back icon image click
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VehicleServices.super.onBackPressed();
                finish();
            }
        });

        //add new service reminders
        set_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddServiceReminders.class);
                startActivity(intent);
            }
        });

        ReminderView();

    }

    private void ReminderView() {

        Query query = databaseReference.orderByChild("reminder_date");

        options = new FirebaseRecyclerOptions.Builder<ReminderDetails>().setQuery(query, ReminderDetails.class).build();
        adapter = new FirebaseRecyclerAdapter<ReminderDetails, ReminderViewHolder>(options) {
            @NonNull
            @Override
            public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_card, parent, false);
                return new ReminderViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ReminderViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull ReminderDetails model) {
                holder.text.setText(model.getReminder_text());
                holder.date.setText(model.getReminder_date());
                holder.time.setText(model.getReminder_time());
                //edit btn click and move the intent
                holder.edit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(VehicleServices.this, UpdateReminders.class);
                        intent.putExtra("ReminderKey", getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}