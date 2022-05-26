package com.example.aimech.user.service_reminders;

public class ReminderDetails {

    String uid, reminder_text, reminder_date, reminder_time;
    Boolean reminder;

    public ReminderDetails() {

    }

    public ReminderDetails(String uid, String reminder_text, String reminder_date, String reminder_time, Boolean reminder) {
        this.uid = uid;
        this.reminder_text = reminder_text;
        this.reminder_date = reminder_date;
        this.reminder_time = reminder_time;
        this.reminder = reminder;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getReminder_text() {
        return reminder_text;
    }

    public void setReminder_text(String reminder_text) {
        this.reminder_text = reminder_text;
    }

    public String getReminder_date() {
        return reminder_date;
    }

    public void setReminder_date(String reminder_date) {
        this.reminder_date = reminder_date;
    }

    public String getReminder_time() {
        return reminder_time;
    }

    public void setReminder_time(String reminder_time) {
        this.reminder_time = reminder_time;
    }

    public Boolean getReminder() {
        return reminder;
    }

    public void setReminder(Boolean reminder) {
        this.reminder = reminder;
    }
}
