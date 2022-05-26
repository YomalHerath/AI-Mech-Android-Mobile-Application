package com.example.aimech.ai_chatbot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimech.R;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Lists;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Chatbot extends AppCompatActivity implements ChatBotReplyMsg {

    //initialize variables
    ImageView back_btn;
    ChatBotAdapter chatBotAdapter;
    RecyclerView recyclerView;
    List<ChatBotMessage> messageList = new ArrayList<>();
    EditText message;
    ImageButton send_btn;

    //dialogFlow
    SessionsClient sessionsClient;
    SessionName sessionName;
    String uuid = UUID.randomUUID().toString();
    String TAG = "mainactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chatbot);

        //initialize reference to views
        back_btn = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.AIRecyclerView);
        message = findViewById(R.id.ai_message);
        send_btn = findViewById(R.id.ai_send_btn);
        back_btn = findViewById(R.id.back_btn);

        //set chat bot layout to recycler view
        chatBotAdapter = new ChatBotAdapter(messageList, this);
        recyclerView.setAdapter(chatBotAdapter);

        //icon back btn click
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chatbot.super.onBackPressed();
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String send_message = message.getText().toString();
                if (!send_message.isEmpty()) {
                    messageList.add(new ChatBotMessage(send_message, false));
                    message.setText("");
                    sendMessage(send_message);
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                    Objects.requireNonNull(recyclerView.getLayoutManager())
                            .scrollToPosition(messageList.size() - 1);
                } else {
                    Toast.makeText(Chatbot.this, "Please enter text!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createChatBot();
    }

    //set up the AI chatbot
    private void createChatBot() {
        try {
            //referring to the credential json file
            InputStream stream = this.getResources().openRawResource(R.raw.chatbot_credential);
            //add google credentials
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            //fetch only the project id in the Input Stream
            String projectId = ((ServiceAccountCredentials) googleCredentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(
                    FixedCredentialsProvider.create(googleCredentials)).build();
            //create new session client
            sessionsClient = SessionsClient.create(sessionsSettings);
            sessionName = SessionName.of(projectId, uuid);

        } catch (Exception e) {
            Log.d(TAG, "ChatBot: " + e.getMessage());
        }
    }


    private void sendMessage(String send_message) {
        QueryInput input = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(send_message).setLanguageCode("en-US")).build();
        new ChatBotSendMsg(this, sessionName, sessionsClient, input).execute();
    }

    @Override
    public void callback(DetectIntentResponse returnResponse) {
        if (returnResponse != null) {
            String botReply = returnResponse.getQueryResult().getFulfillmentText();
            if (!botReply.isEmpty()) {
                messageList.add(new ChatBotMessage(botReply, true));
                chatBotAdapter.notifyDataSetChanged();
                Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
            } else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "failed to connect!", Toast.LENGTH_SHORT).show();
        }
    }
}