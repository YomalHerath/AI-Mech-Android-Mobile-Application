package com.example.aimech.user.service_provider;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimech.R;
import com.example.aimech.service_providers.service_center.MechanicsModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ServiceProviderListView extends AppCompatActivity {

    //initialize variable
    ImageView back_btn;
    EditText search_shop;
    RecyclerView recyclerView;
    String address;

    //Get Model class to firebase recycler options
    FirebaseRecyclerOptions<MechanicsModel> options;
    FirebaseRecyclerAdapter<MechanicsModel, ServiceProviderViewHolder> adapter;

    DatabaseReference databaseReference;

    LocationRequest locationRequest;
    final int REQUEST_CHECK_SETTING = 1001;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_service_provider_list_view);

        //initialize reference to views
        back_btn = findViewById(R.id.back_btn);
        search_shop = findViewById(R.id.search_shops);
        recyclerView = findViewById(R.id.recyclerView);

        //initialize fused location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Pass new Layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("Service_Providers");

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
                    MechanicView(editable.toString());
                } else {
                    getLocation();
                }
            }
        });

        //back btn
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceProviderListView.super.onBackPressed();
                finish();
            }
        });
    }

    //check permission method
    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(ServiceProviderListView.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //When permission granted
            getLocation();
        } else {
            ActivityCompat.requestPermissions(ServiceProviderListView.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 23);
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
                        Geocoder geocoder = new Geocoder(ServiceProviderListView.this,
                                Locale.getDefault());
                        //Initialize address list
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        String city = addressList.get(0).getLocality();
                        address = addressList.get(0).getAddressLine(0);
                        //create function to get mechanic Details
                        MechanicView(city);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void MechanicView(String data) {

        //Create Query to Search Data
        Query query = databaseReference.orderByChild("located").startAt(data).endAt(data + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<MechanicsModel>().setQuery(query, MechanicsModel.class).build();
        adapter = new FirebaseRecyclerAdapter<MechanicsModel, ServiceProviderViewHolder>(options) {

            @NonNull
            @Override
            public ServiceProviderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mechanic_details_card_view, parent, false);
                return new ServiceProviderViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ServiceProviderViewHolder holder, int position, @NonNull MechanicsModel model) {
                holder.provider_name.setText(model.getMechanic_name());
                holder.provider_address.setText(model.getMechanic_address());
                holder.provider_located.setText(model.getLocated());
                holder.provider_contact.setText(model.getContact_no());
                holder.provider_details.setText(model.getService_center_details());
                holder.provider_email.setText(model.getEmail());
                holder.service_center_name.setText(model.getService_center_name());

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

                String destination = model.getMechanic_address();

                //show gps location
                holder.gps_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //move to map
                        showLocation(address, destination);
                    }
                });

            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void showLocation(String data, String destination) {
        //If the device does not have install the map
        try {
            //When google map installed
            Uri uri = Uri.parse("https://www.google.com/maps/dir/" + data + "/" + destination);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);//add intent with action
            intent.setPackage("com.google.android.apps.maps");//set package to google map application
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//set flag
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            //when google map not installed
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//set flag
            startActivity(intent);
        }
    }

}