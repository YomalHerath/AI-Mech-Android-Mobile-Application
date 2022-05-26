package com.example.aimech.faq_chat;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimech.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.HolderFaq> {

    private static final int MEG_TYPE_LEFT = 0;
    private static final int MEG_TYPE_RIGHT = 1;

    private Context context;
    private ArrayList<FaqModel> faqModelList;

    private FirebaseAuth firebaseAuth;

    public FaqAdapter(Context context, ArrayList<FaqModel> faqModelList) {
        this.context = context;
        this.faqModelList = faqModelList;

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public HolderFaq onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        if (viewType == MEG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.faq_row_right, parent, false);
            return new HolderFaq(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.faq_row_left, parent, false);
            return new HolderFaq(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HolderFaq holder, int position) {

        //get data
        FaqModel model = faqModelList.get(position);
        String timestamp = model.getTimestamp();
        String message = model.getMessage();
        String senderUid = model.getSender();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp));
        String dataTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();

        //set data
        holder.messageTv.setText(message);
        holder.timeTv.setText(dataTime);

        setUserName(model, holder);
    }

    private void setUserName(FaqModel model, HolderFaq holder) {
        //get sender info from model
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(model.getSender())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String name = "" + ds.child("username").getValue();

                            holder.nameTv.setText(name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return faqModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (faqModelList.get(position).getSender().equals(firebaseAuth.getUid())) {
            return MEG_TYPE_RIGHT;
        } else {
            return MEG_TYPE_LEFT;
        }
    }

    class HolderFaq extends RecyclerView.ViewHolder {

        private TextView nameTv, messageTv, timeTv;

        public HolderFaq(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameTv);
            messageTv = itemView.findViewById(R.id.messageTv);
            timeTv = itemView.findViewById(R.id.timeTv);

        }
    }
}














