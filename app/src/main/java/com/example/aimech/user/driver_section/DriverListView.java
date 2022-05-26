package com.example.aimech.user.driver_section;

import android.Manifest;
import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimech.R;
import com.example.aimech.drivers.create_driver.DriverModel;
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

public class DriverListView extends AppCompatActivity {

    //initialize variable
    ImageView back_btn;
    EditText search_drivers;
    RecyclerView recyclerView;
    String address;

    //Get Model class to firebase recycler options
    FirebaseRecyclerOptions<DriverModel> options;
    FirebaseRecyclerAdapter<DriverModel, DriverViewHolder> adapter;

    DatabaseReference databaseReference;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_driver_list_view);

        //initialize reference to views
        back_btn = findViewById(R.id.back_btn);
        search_drivers = findViewById(R.id.search_drivers);
        recyclerView = findViewById(R.id.recyclerView);

        //initialize fused location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Pass new Layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("Drivers");

        //check permission
        checkLocationPermission();

        //get search value
        search_drivers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString() != null) {
                    DriverView(editable.toString());
                } else {
                    getLocation();
                }
            }
        });

        //back btn
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverListView.super.onBackPressed();
                finish();
            }
        });
    }

    //check permission method
    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(DriverListView.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //When permission granted
            getLocation();
        } else {
            ActivityCompat.requestPermissions(DriverListView.this,
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
                        Geocoder geocoder = new Geocoder(DriverListView.this,
                                Locale.getDefault());
                        //Initialize address list
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        String city = addressList.get(0).getLocality();
                        address = addressList.get(0).getAddressLine(0);

                        //create function to get Shop Details
                        DriverView(city);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void DriverView(String data) {

        //Create Query to Search Data
        Query query = databaseReference.orderByChild("city").startAt(data).endAt(data + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<DriverModel>().setQuery(query, DriverModel.class).build();
        adapter = new FirebaseRecyclerAdapter<DriverModel, DriverViewHolder>(options) {

            @NonNull
            @Override
            public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_details_card_view, parent, false);
                return new DriverViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull DriverViewHolder holder, int position, @NonNull DriverModel model) {
                holder.Name.setText(model.getFull_name());
                holder.Address.setText(model.getAddress());
                holder.City.setText(model.getCity());
                holder.Email.setText(model.getEmail());
                holder.Mobile_no.setText(model.getMobile_no());
                holder.Birth_Year.setText(model.getBirth_year());
                holder.License_no.setText(model.getLicense_no());

                //make call on cl btn click
                holder.cl_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobile_no = model.getMobile_no();
                        String call = "tel:" +mobile_no.trim();

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(call));
                        startActivity(intent);
                    }
                });

            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}