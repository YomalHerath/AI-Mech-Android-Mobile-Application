package com.example.aimech.user.service_reminders;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.aimech.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

public class AddServiceReminders extends AppCompatActivity {

    //initialize variable
    ImageView back_btn;
    TextInputEditText reminder_date, reminder_time;
    TextInputLayout reminder_text;
    Button set_reminder;
    SwitchCompat reminder;
    String timeToNotify;
    long maxId;

    ProgressDialog progressDialog;
    DatabaseReference databaseReference;

    NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_service_reminders);

        //initialize notification manager
        notificationManager = NotificationManagerCompat.from(this);

        //initialize reference to views
        back_btn = findViewById(R.id.back_btn);
        reminder_text = findViewById(R.id.reminder_text);
        reminder_date = findViewById(R.id.reminder_date);
        reminder_time = findViewById(R.id.reminder_time);
        set_reminder = findViewById(R.id.set_reminder_btn);
        reminder = findViewById(R.id.reminder);

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

        //back icon image click
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddServiceReminders.super.onBackPressed();
                finish();
            }
        });

        //get new progress dialog
        progressDialog = new ProgressDialog(AddServiceReminders.this);

        String current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Service_Reminders").child(current_uid);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxId = (snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //saving button click
        set_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call saving method
                saveReminderDetails();
            }
        });

    }

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

    //details saving method
    public void saveReminderDetails() {

        //get all the values from edit text fields
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String reminder_et = reminder_text.getEditText().getText().toString();
        String time_et = reminder_time.getText().toString();
        String date_et = reminder_date.getText().toString();
        boolean reminder_s = reminder.isChecked();

        //show progress dialog in waiting time
        progressDialog.setTitle("Setting Reminder");
        progressDialog.show();

        //call to helper class
        ReminderDetails helperClass = new ReminderDetails(uid, reminder_et, date_et, time_et, reminder_s);

        databaseReference
                .child(String.valueOf(maxId+1))
                .setValue(helperClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //successfully save data in realtime database
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                            Toast.makeText(AddServiceReminders.this, "Successfully Added the Reminder Details!", Toast.LENGTH_LONG).show();

                            //go back to reminder all view
                            Intent intent = new Intent(getApplicationContext(), VehicleServices.class);
                            startActivity(intent);
                            finish();

                            if (reminder.isChecked()) {
                                //create method to set alarm
                                setAlarm(reminder_et, date_et, time_et);
                            }
                        }

                        //fail saving data in realtime database
                        else {
                            Toast.makeText(AddServiceReminders.this, "Failed! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void setAlarm(String reminder_et, String date_et, String time_et) {

        //Initialize alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        String reminderKey = String.valueOf(databaseReference.child(String.valueOf(maxId+1)));

        //sending data to broadcast receiver to create channel and notification
        Intent intent = new Intent(getApplicationContext(), ReminderBroadcastReceiver.class);
        intent.putExtra("event", reminder_et);
        intent.putExtra("time", date_et);
        intent.putExtra("date", time_et);
        intent.putExtra("reminder_key", reminderKey);

        //create a flag for use only one time to indicate the pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date_et + " " + timeToNotify;//save user selected date and time in variable

        //format the date and time to simple date format
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);//set the alarm to the formatted time with alarm manager
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