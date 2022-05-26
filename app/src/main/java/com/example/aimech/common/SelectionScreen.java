package com.example.aimech.common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aimech.R;
import com.example.aimech.drivers.driver_details.DriverLogin;
import com.example.aimech.service_providers.service_provider_details.ServiceProviderLogin;
import com.example.aimech.spare_part_shops.shop_owner_details.ShopLogin;
import com.example.aimech.user.user_detail.UserLogin;

public class SelectionScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_selection_screen);

        //check connection is availability
        if (!isConnectNetwork()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("You're Offline")
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Check", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            finish();
                        }
                    })
                    .show();
        }

    }

    //user btn click
    public void userLogin(View view) {
        startActivity(new Intent(getApplicationContext(), UserLogin.class));
    }

    //service provider btn click
    public void serviceLogin(View view) {
        startActivity(new Intent(getApplicationContext(), ServiceProviderLogin.class));
    }

    //driver btn click
    public void driverLogin(View view) {
        startActivity(new Intent(getApplicationContext(), DriverLogin.class));
    }

    //shop btn click
    public void shopLogin(View view) {
        startActivity(new Intent(getApplicationContext(), ShopLogin.class));
    }

    //Check Network Connection
    protected boolean isConnectNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null) ? true : false;
    }

}