package com.example.aimech.user.spare_part_shop;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimech.R;

public class ShopViewHolder extends RecyclerView.ViewHolder{

    TextView shop_name, shop_details, shop_address, shop_located, shop_contact;
    ImageButton cl_btn, gps_btn, view_btn;
    View v;


    public ShopViewHolder(@NonNull View itemView) {
        super(itemView);

        shop_name = itemView.findViewById(R.id.shop_name_tv);
        shop_address = itemView.findViewById(R.id.shop_address_tv);
        shop_details = itemView.findViewById(R.id.shop_details_tv);
        shop_located = itemView.findViewById(R.id.shop_city_tv);
        shop_contact = itemView.findViewById(R.id.shop_contact_tv);
        cl_btn = itemView.findViewById(R.id.shop_cl_btn);
        gps_btn = itemView.findViewById(R.id.shop_location_btn);
        view_btn = itemView.findViewById(R.id.shop_view_btn);
        v = itemView;

    }
}
