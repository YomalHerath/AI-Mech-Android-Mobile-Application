package com.example.aimech.ai_chatbot;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimech.R;

import java.util.List;

public class ChatBotAdapter extends RecyclerView.Adapter<ChatBotAdapter.ViewHolder> {

    List<ChatBotMessage> messageList;
    Activity activity;

    public ChatBotAdapter(List<ChatBotMessage> messageList, Activity activity) {
        this.messageList = messageList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.chatbot_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String message = messageList.get(position).getMessage();
        boolean isReceived = messageList.get(position).getIsReceived();
        if (isReceived) {
            holder.receiveMsg.setVisibility(View.VISIBLE);
            holder.sendMsg.setVisibility(View.GONE);
            holder.receiveMsg.setText(message);
        } else {
            holder.sendMsg.setVisibility(View.VISIBLE);
            holder.receiveMsg.setVisibility(View.GONE);
            holder.sendMsg.setText(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView sendMsg, receiveMsg;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            sendMsg = itemView.findViewById(R.id.ai_msg_send);
            receiveMsg = itemView.findViewById(R.id.ai_msg_receive);
        }
    }

}