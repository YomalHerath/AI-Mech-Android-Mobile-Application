package com.example.aimech.user.user_detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.aimech.R;
import com.example.aimech.ai_chatbot.Chatbot;
import com.example.aimech.common.SelectionScreen;
import com.example.aimech.faq_chat.FaqSection;
import com.example.aimech.user.driver_section.DriverListView;
import com.example.aimech.user.service_provider.ServiceProviderListView;
import com.example.aimech.user.spare_part_shop.ShopListView;
import com.example.aimech.user.service_reminders.VehicleServices;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //initialize variables
    ImageView profile_icon, menu_icon;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout contentView;
    static final float END_SCALE = 0.7f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);

        //initialize reference to views
        profile_icon = findViewById(R.id.profile_icon);
        menu_icon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        profile_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDashboard.this, UserProfile.class);
                startActivity(intent);
            }
        });
        Navigation();
    }

    private void Navigation() {
        //Navigation
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout.setScrimColor(getResources().getColor(R.color.FCF7FF));

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                break;
            case R.id.nav_service:
                startActivity(new Intent(getApplicationContext(), ServiceProviderListView.class));
                break;
            case R.id.nav_shop:
                startActivity(new Intent(getApplicationContext(), ShopListView.class));
                break;
            case R.id.nav_driver:
                startActivity(new Intent(getApplicationContext(), DriverListView.class));
                break;
            case R.id.nav_reminder:
                startActivity(new Intent(getApplicationContext(), VehicleServices.class));
                break;
            case R.id.nav_faq:
                startActivity(new Intent(getApplicationContext(), FaqSection.class));
                break;
            case R.id.nav_helper:
                startActivity(new Intent(getApplicationContext(), Chatbot.class));
                break;
            case R.id.nav_logout:
                //Logout
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), SelectionScreen.class));
                finish();
                break;
        }
        return true;
    }

    public void myVehicle(View view) {
        startActivity(new Intent(getApplicationContext(), VehicleServices.class));
    }

    public void faq_section(View view) {
        startActivity(new Intent(getApplicationContext(), FaqSection.class));
    }

    public void driver_list(View view) {
        startActivity(new Intent(getApplicationContext(), DriverListView.class));
    }

    public void ai_helper(View view) {
        startActivity(new Intent(getApplicationContext(), Chatbot.class));
    }

    public void service_provider(View view) {
        startActivity(new Intent(getApplicationContext(), ServiceProviderListView.class));
    }

    public void shop_view(View view) {
        startActivity(new Intent(getApplicationContext(), ShopListView.class));
    }
}