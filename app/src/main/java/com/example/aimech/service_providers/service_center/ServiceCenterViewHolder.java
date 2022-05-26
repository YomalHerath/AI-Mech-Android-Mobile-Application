package com.example.aimech.service_providers.service_center;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimech.R;

public class ServiceCenterViewHolder extends RecyclerView.ViewHolder {

    TextView owner_name, name, address, details;
    View v;

    public ServiceCenterViewHolder(@NonNull View itemView) {
        super(itemView);

        owner_name = itemView.findViewById(R.id.mechanic_name_service);
        name = itemView.findViewById(R.id.service_center_name_serivce_card);
        address = itemView.findViewById(R.id.mechanic_address_service_center);
        details = itemView.findViewById(R.id.mechanic_details_service_center);
        v = itemView;

    }
}
