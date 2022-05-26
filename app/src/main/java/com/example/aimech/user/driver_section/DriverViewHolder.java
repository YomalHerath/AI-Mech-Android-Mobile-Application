package com.example.aimech.user.driver_section;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimech.R;

public class DriverViewHolder extends RecyclerView.ViewHolder{

    TextView Name, Address, City, Mobile_no, Email, Birth_Year, License_no;
    ImageButton cl_btn;
    View v;

    public DriverViewHolder(@NonNull View itemView) {
        super(itemView);

        Name = itemView.findViewById(R.id.driver_name_tv);
        Address = itemView.findViewById(R.id.driver_address_tv);
        City = itemView.findViewById(R.id.driver_city_tv);
        Mobile_no = itemView.findViewById(R.id.driver_mobile_no_tv);
        Email = itemView.findViewById(R.id.driver_email_tv);
        Birth_Year = itemView.findViewById(R.id.driver_birth_year_tv);
        License_no = itemView.findViewById(R.id.driver_license_no_tv);
        cl_btn = itemView.findViewById(R.id.driver_call_btn);
        v = itemView;

    }
}
