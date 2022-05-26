package com.example.aimech.user.spare_part_shop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimech.R;
import com.example.aimech.spare_part_shops.shop_details.ShopModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ShopListView extends AppCompatActivity {

    //initialize variable
    ImageView back_btn;
    EditText search_shop;
    RecyclerView recyclerView;
    String address;

    //Get Model class to firebase recycler options
    FirebaseRecyclerOptions<ShopModel> options;
    FirebaseRecyclerAdapter<ShopModel, ShopViewHolder> adapter;

    DatabaseReference databaseReference;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shop_list_view);

        //initialize reference to views
        back_btn = findViewById(R.id.back_btn);
        search_shop = findViewById(R.id.search_shops);
        recyclerView = findViewById(R.id.recyclerView);

        //initialize fused location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Pass new Layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("Spare_Part_Shops");

        //check permission
        checkLocationPermission();

        //get search value
        search_shop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString() != null) {
                    ShopView(editable.toString());
                } else {
                    getLocation();
                }
            }
        });

        //back btn
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopListView.super.onBackPressed();
                finish();
            }
        });
    }

    //check permission method
    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(ShopListView.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //When permission granted
            getLocation();
        } else {
            ActivityCompat.requestPermissions(ShopListView.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            checkLocationPermission();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //Initialize location
                Location location = task.getResult();
                if (location != null) {
                    try {
                        //Initialize Geocoder
                        Geocoder geocoder = new Geocoder(ShopListView.this,
                                Locale.getDefault());
                        //Initialize address list
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        String city = addressList.get(0).getLocality();
                        address = addressList.get(0).getAddressLine(0);

                        //create function to get Shop Details
                        ShopView(city);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void ShopView(String data) {

        //Create Query to Search Data
        Query query = databaseReference.orderByChild("shop_located").startAt(data).endAt(data + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<ShopModel>().setQuery(query, ShopModel.class).build();
        adapter = new FirebaseRecyclerAdapter<ShopModel, ShopViewHolder>(options) {

            @NonNull
            @Override
            public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_details_card_view, parent, false);
                return new ShopViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ShopViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull ShopModel model) {
                holder.shop_name.setText(model.getShop_name());
                holder.shop_address.setText(model.getShop_address());
                holder.shop_located.setText(model.getShop_located());
                holder.shop_contact.setText(model.getContact_no());
                holder.shop_details.setText(model.getShop_details());

                //make call on cl btn click
                holder.cl_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobile_no = model.getContact_no();
                        String call = "tel:" + mobile_no.trim();

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(call));
                        startActivity(intent);
                    }
                });

                holder.view_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopListView.this, ShopView.class);
                        intent.putExtra("ShopKey", getRef(position).getKey());
                        startActivity(intent);
                    }
                });

                String destination = model.getShop_address();

                //show gps location
                holder.gps_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ShopListView.this, ""+address+" : "+destination, Toast.LENGTH_SHORT).show();
                        showlocation(address, destination);

                    }
                });

            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void showlocation(String data, String destination) {
        //If the device does not have install the map
        try {

            //When google map installed
            Uri uri = Uri.parse("https://www.google.com/maps/dir/" + data + "/" + destination);
            //add intent with action
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //set package
            intent.setPackage("com.google.android.apps.maps");
            //set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } catch (ActivityNotFoundException e) {

            //when google map not installed
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }

}