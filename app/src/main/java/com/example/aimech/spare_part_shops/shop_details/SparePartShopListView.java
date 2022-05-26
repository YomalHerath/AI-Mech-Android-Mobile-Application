package com.example.aimech.spare_part_shops.shop_details;

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

public class SparePartShopListView extends AppCompatActivity {

    //initialize variable
    ImageView back_btn;
    RecyclerView recyclerView;

    //Get Model class to firebase recycler options
    FirebaseRecyclerOptions<ShopModel> options;
    FirebaseRecyclerAdapter<ShopModel, SparePartShopViewHolder> adapter;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_spare_part_shop_list_view);

        //initialize reference to views
        back_btn = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.recyclerView_shop);

        databaseReference = FirebaseDatabase.getInstance().getReference("Spare_Part_Shops");

        //Pass new Layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        //back btn
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparePartShopListView.super.onBackPressed();
                finish();
            }
        });

        ShopdetailView();

    }

    private void ShopdetailView() {

        //get user id
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //create query to search data
        Query query = databaseReference.orderByChild("uid").equalTo(uid);

        options = new FirebaseRecyclerOptions.Builder<ShopModel>().setQuery(query, ShopModel.class).build();
        adapter = new FirebaseRecyclerAdapter<ShopModel, SparePartShopViewHolder>(options) {
            @NonNull
            @Override
            public SparePartShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.spare_part_shop_card_view, parent, false);
                return new SparePartShopViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull SparePartShopViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull ShopModel model) {
                holder.shop_name.setText(model.getShop_name());
                holder.shop_address.setText(model.getShop_address());
                holder.shop_details.setText(model.getShop_details());

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SparePartShopListView.this, UpdateShopDetails.class);
                        intent.putExtra("ShopKey", getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}