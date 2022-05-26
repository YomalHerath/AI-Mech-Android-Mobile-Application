package com.example.aimech.spare_part_shops.shop_details;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimech.R;

public class SparePartShopViewHolder extends RecyclerView.ViewHolder {

    TextView shop_name, shop_address, shop_details;
    View v;

    public SparePartShopViewHolder(@NonNull View itemView) {
        super(itemView);

        shop_name = itemView.findViewById(R.id.shop_card_name);
        shop_address = itemView.findViewById(R.id.shop_card_address);
        shop_details = itemView.findViewById(R.id.shop_card_detail);
        v = itemView;

    }
}
