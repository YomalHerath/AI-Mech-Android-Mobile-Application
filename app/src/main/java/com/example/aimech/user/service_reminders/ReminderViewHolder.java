package com.example.aimech.user.service_reminders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimech.R;

public class ReminderViewHolder extends RecyclerView.ViewHolder {

    TextView text, date, time;
    ImageView edit_btn;
    View v;

    public ReminderViewHolder(@NonNull View itemView) {
        super(itemView);

        text = itemView.findViewById(R.id.reminder_text_card);
        date = itemView.findViewById(R.id.reminder_date_card);
        time = itemView.findViewById(R.id.reminder_time_card);
        edit_btn = itemView.findViewById(R.id.edit_reminder_btn);
        v = itemView;
    }
}
