package com.example.aimech.service_providers.service_center;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class ServiceCenterList extends AppCompatActivity {

    //initialize variable
    ImageView back_btn;
    RecyclerView recyclerView;

    //Get Model class to firebase recycler options
    FirebaseRecyclerOptions<MechanicsModel> options;
    FirebaseRecyclerAdapter<MechanicsModel, ServiceCenterViewHolder> adapter;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_service_center_list);

        //initialize reference to views
        back_btn = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.recyclerView_service_center);

        databaseReference = FirebaseDatabase.getInstance().getReference("Service_Providers");

        //Pass new Layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        //back btn
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceCenterList.super.onBackPressed();
                finish();
            }
        });

        MechanicView();

    }

    private void MechanicView() {

        //get user id
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //create query to search data
        Query query = databaseReference.orderByChild("uid").equalTo(uid);

        options = new FirebaseRecyclerOptions.Builder<MechanicsModel>().setQuery(query, MechanicsModel.class).build();
        adapter = new FirebaseRecyclerAdapter<MechanicsModel, ServiceCenterViewHolder>(options) {
            @NonNull
            @Override
            public ServiceCenterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_center_card_view, parent, false);
                return new ServiceCenterViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ServiceCenterViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull MechanicsModel model) {
                holder.owner_name.setText(model.getMechanic_name());
                holder.address.setText(model.getMechanic_address());
                holder.details.setText(model.getService_center_details());
                holder.name.setText(model.getService_center_name());
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ServiceCenterList.this, UpdateMechanicDetails.class);
                        intent.putExtra("ServiceKey", getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}