package com.example.aimech.spare_part_shops.shop_details;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aimech.R;
import com.example.aimech.spare_part_shops.shop_owner_details.ShopDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddShopDetails extends AppCompatActivity {

    //initialize variable
    ImageView back_btn;
    Button save_btn;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int pick_hour, pick_minute;
    TextInputLayout shop_name, shop_address, contact_person, contact_no, email, shop_details, shop_city;
    TextInputEditText open_time, close_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_shop_details);

        //initialize reference to views
        back_btn = findViewById(R.id.back_btn);
        shop_name = findViewById(R.id.shop_name);
        shop_address = findViewById(R.id.shop_address);
        open_time = findViewById(R.id.shop_open_time);
        close_time = findViewById(R.id.shop_close_time);
        contact_person = findViewById(R.id.shop_contact_person);
        contact_no = findViewById(R.id.shop_contact_no);
        email = findViewById(R.id.shop_email);
        shop_details = findViewById(R.id.shop_detail);
        shop_city = findViewById(R.id.shop_city);
        save_btn = findViewById(R.id.save_shop_btn);

        //get new progress dialog
        progressDialog = new ProgressDialog(AddShopDetails.this);

        //back icon image click
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddShopDetails.super.onBackPressed();
                finish();
            }
        });

        //add time picker for text field click
        open_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize time picker
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddShopDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Initialize hrs and min
                        pick_hour = hourOfDay;
                        pick_minute = minute;

                        //Initialize Calendar
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(0, 0, 0, pick_hour, pick_minute);
                        //set selected time
                        open_time.setText(DateFormat.format("hh:mm aa", calendar1));
                    }
                }, 12, 0, false
                );
                //Display previous selected time
                timePickerDialog.updateTime(pick_hour, pick_minute);
                //show dialog
                timePickerDialog.show();
            }
        });

        //add time picker for text field click
        close_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize time picker
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddShopDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Initialize hrs and min
                        pick_hour = hourOfDay;
                        pick_minute = minute;

                        //Initialize Calendar
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(0, 0, 0, pick_hour, pick_minute);
                        //set selected time
                        close_time.setText(DateFormat.format("hh:mm aa", calendar1));
                    }
                }, 12, 0, false
                );
                //Display previous selected time
                timePickerDialog.updateTime(pick_hour, pick_minute);
                //show dialog
                timePickerDialog.show();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call saving method
                saveShopDetails();
            }
        });

    }

    //validate fields
    //shop name validation
    private Boolean validateShopName() {
        String val = shop_name.getEditText().getText().toString();

        if (val.isEmpty()) {
            shop_name.setError("Field cannot be empty");
            return false;
        } else {
            shop_name.setError(null);
            shop_name.setErrorEnabled(false);
            return true;
        }
    }

    //shop address validation
    private Boolean validateAddress() {
        String val = shop_address.getEditText().getText().toString();

        if (val.isEmpty()) {
            shop_address.setError("Field cannot be empty");
            return false;
        } else {
            shop_address.setError(null);
            shop_address.setErrorEnabled(false);
            return true;
        }
    }

    //shop open time validation
    private Boolean validateOpenTime() {
        String val = open_time.getText().toString();

        if (val.isEmpty()) {
            open_time.setError("Field cannot be empty");
            return false;
        } else {
            open_time.setError(null);
            return true;
        }
    }

    //shop close time validation
    private Boolean validateCloseTime() {
        String val = close_time.getText().toString();

        if (val.isEmpty()) {
            close_time.setError("Field cannot be empty");
            return false;
        } else {
            close_time.setError(null);
            return true;
        }
    }

    //shop contact person validation
    private Boolean validateContactPerson() {
        String val = contact_person.getEditText().getText().toString();

        if (val.isEmpty()) {
            contact_person.setError("Field cannot be empty");
            return false;
        } else {
            contact_person.setError(null);
            contact_person.setErrorEnabled(false);
            return true;
        }
    }

    //shop shop details validation
    private Boolean validateShopDetails() {
        String val = shop_details.getEditText().getText().toString();

        if (val.isEmpty()) {
            shop_details.setError("Field cannot be empty");
            return false;
        } else {
            shop_details.setError(null);
            shop_details.setErrorEnabled(false);
            return true;
        }
    }

    //email validation
    private Boolean validateEmail() {
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    //phone no validation
    private Boolean validatePhoneNo() {
        String val = contact_no.getEditText().getText().toString();

        if (val.isEmpty()) {
            contact_no.setError("Field cannot be empty");
            return false;
        } else {
            contact_no.setError(null);
            contact_no.setErrorEnabled(false);
            return true;
        }
    }

    //city validation
    private Boolean validateCity() {
        String val = shop_city.getEditText().getText().toString();

        if (val.isEmpty()) {
            shop_city.setError("Field cannot be empty");
            return false;
        } else {
            shop_city.setError(null);
            shop_city.setErrorEnabled(false);
            return true;
        }
    }

    //details saving method
    public void saveShopDetails() {

        //performing validation by calling validation functions
        if (!validateShopName() | !validateContactPerson() | !validateCity() | !validateAddress() | !validateOpenTime() | !validatePhoneNo() | !validateEmail() | !validateCloseTime() | !validateShopDetails()) {
            return;
        }

        //get all the values from edit text fields
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String s_name = shop_name.getEditText().getText().toString();
        String s_address = shop_address.getEditText().getText().toString();
        String s_open_time = open_time.getText().toString();
        String s_close_time = close_time.getText().toString();
        String s_email = email.getEditText().getText().toString();
        String s_contact_no = contact_no.getEditText().getText().toString();
        String s_contact_person = contact_person.getEditText().getText().toString();
        String s_shop_details = shop_details.getEditText().getText().toString();
        String s_city = shop_city.getEditText().getText().toString();

        //show progress dialog in waiting time
        progressDialog.setTitle("Shop details saving in progress");
        progressDialog.show();

        //call to helper class
        ShopModel helperClass = new ShopModel(uid, s_name, s_address, s_open_time, s_close_time, s_contact_person, s_contact_no, s_email, s_shop_details, s_city);

        FirebaseDatabase.getInstance().getReference("Spare_Part_Shops")
                .push()
                .setValue(helperClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //successfully save data in realtime database
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                            Toast.makeText(AddShopDetails.this, "Successfully Add Spare Part Shop Details", Toast.LENGTH_LONG).show();

                            //go back to dashboard page
                            Intent intent = new Intent(getApplicationContext(), ShopDashboard.class);
                            startActivity(intent);
                            finish();
                        }
                        //fail saving data in realtime database
                        else {
                            Toast.makeText(AddShopDetails.this, "Failed! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}