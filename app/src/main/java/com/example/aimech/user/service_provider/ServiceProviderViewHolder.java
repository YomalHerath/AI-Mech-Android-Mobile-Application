package com.example.aimech.user.service_provider;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimech.R;

public class ServiceProviderViewHolder extends RecyclerView.ViewHolder{

    TextView provider_name, service_center_name, provider_email, provider_details, provider_address, provider_located, provider_contact;
    ImageButton cl_btn, gps_btn;
    View v;

    public ServiceProviderViewHolder(@NonNull View itemView) {
        super(itemView);

        provider_name = itemView.findViewById(R.id.mechanic_name_tv);
        service_center_name = itemView.findViewById(R.id.service_center_name_tv);
        provider_address = itemView.findViewById(R.id.mechanic_address_tv);
        provider_details = itemView.findViewById(R.id.mechanic_details_tv);
        provider_located = itemView.findViewById(R.id.mechanic_city_tv);
        provider_contact = itemView.findViewById(R.id.mechanic_contact_tv);
        provider_email = itemView.findViewById(R.id.provider_email_tv);
        gps_btn = itemView.findViewById(R.id.mechanic_location_btn);
        cl_btn = itemView.findViewById(R.id.mechanic_cl_btn);
        v = itemView;

    }
}
