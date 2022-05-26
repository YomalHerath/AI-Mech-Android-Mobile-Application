package com.example.aimech.user.service_reminders;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.aimech.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class UpdateReminders extends AppCompatActivity {

    //initialize variable
    ImageView back_btn;
    TextInputEditText reminder_text, reminder_date, reminder_time;
    Button set_reminder, remove;
    SwitchCompat reminder;

    String timeToNotify;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_reminders);

        //initialize reference to views
        back_btn = findViewById(R.id.back_btn);
        reminder_text = findViewById(R.id.reminder_text);
        reminder_date = findViewById(R.id.reminder_date);
        reminder_time = findViewById(R.id.reminder_time);
        set_reminder = findViewById(R.id.set_reminder_btn);
        reminder = findViewById(R.id.reminder);
        remove = findViewById(R.id.remove);

        //add date picker for text field click
        reminder_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a method to select date
                selectDate();
            }
        });

        //add time picker for text field click
        reminder_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a method to select time
                selectTime();
            }
        });

        //get new progress dialog
        progressDialog = new ProgressDialog(UpdateReminders.this);

        //Get Key with intent
        String ReminderKey = getIntent().getStringExtra("ReminderKey");

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Service_Reminders").child(user_id).child(ReminderKey);

        //back icon image click
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateReminders.super.onBackPressed();
                finish();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Get Values from Firebase Realtime Database
                if (snapshot.exists()) {
                    String ureminder_date = snapshot.child("reminder_date").getValue().toString();
                    String ureminder_text = snapshot.child("reminder_text").getValue().toString();
                    String ureminder_time = snapshot.child("reminder_time").getValue().toString();
                    String ureminder = snapshot.child("reminder").getValue().toString();

                    if (ureminder.equals("true")) {
                        reminder.setChecked(true);
                    }

                    reminder_date.setText(ureminder_date);
                    reminder_text.setText(ureminder_text);
                    reminder_time.setText(ureminder_time);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Update Function
        set_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get Values from Edit Fields
                String reminder_et = reminder_text.getText().toString();
                String time_et = reminder_time.getText().toString();
                String date_et = reminder_date.getText().toString();
                boolean reminder_s = reminder.isChecked();

                //Set Values to Update the Firebase Realtime database value using HashMap
                HashMap hashMap = new HashMap();
                hashMap.put("reminder_text", reminder_et);
                hashMap.put("reminder_date", date_et);
                hashMap.put("reminder_time", time_et);
                hashMap.put("reminder", reminder_s);

                databaseReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(UpdateReminders.this, "Data was Successfully Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), VehicleServices.class));
                        finish();

                        //check the reminder is set
                        if (reminder.isChecked()) {
                            //create method to set alarm
                            setAlarm(reminder_et, date_et, time_et);
                        } else {
//                            cancelAlarm();
                        }
                    }
                });
            }
        });
        //Delete Function
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create Alert Button
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateReminders.this);

                //Alert Messages to show
                builder.setTitle("Delete");
                builder.setMessage("Are you sure, You want to Delete this ?");

                //Delete in Click Yes
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                startActivity(new Intent(getApplicationContext(), VehicleServices.class));
                                Toast.makeText(UpdateReminders.this, "Successfully Remove the Reminder", Toast.LENGTH_SHORT).show();
                                finish();
//                                cancelAlarm();
                            }
                        });
                    }
                });

                //When Click No button
                builder.setNegativeButton("N0", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                builder.create().show();
            }
        });
    }

    //cancel Alarm
//    private void cancelAlarm() {
//        //Initialize alarm manager
//        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(getApplicationContext(), ReminderBroadcastReceiver.class);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        if (am == null) {
//            am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        }
//        am.cancel(pendingIntent);
//        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
//    }

    //get date from time picker
    private void selectTime() {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                //temp variable to store the time to set alarm
                timeToNotify = i + ":" + i1;
                reminder_time.setText(FormatTime(i, i1));
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    //create a method to format the time
    //converts the time into 12hr format and assigns am or pm
    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }

        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }

        return time;
    }

    //get date from date picker
    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                reminder_date.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    //set alarm
    private void setAlarm(String reminder_et, String date_et, String time_et) {

        //Initialize alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        String reminderKey = databaseReference.push().getKey();

        //sending data to broadcast receiver to create channel and notification
        Intent intent = new Intent(getApplicationContext(), ReminderBroadcastReceiver.class);
        intent.putExtra("event", reminder_et);
        intent.putExtra("time", date_et);
        intent.putExtra("date", time_et);
        intent.putExtra("reminder_key", reminderKey);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date_et + " " + timeToNotify;

        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Toast.makeText(this, "Successfully Set the Alarm", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //navigates from to all view
        Intent intentBack = new Intent(getApplicationContext(), VehicleServices.class);
        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentBack);
    }

}