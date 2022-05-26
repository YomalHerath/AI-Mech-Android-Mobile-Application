package com.example.aimech.ai_chatbot;

import android.os.AsyncTask;
import android.util.Log;

import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;

public class ChatBotSendMsg extends AsyncTask<Void, Void, DetectIntentResponse> {

    //initialize variables
    SessionName sessionName;
    SessionsClient sessionsClient;
    QueryInput queryInput;
    String TAG = "async";
    ChatBotReplyMsg chatBotReplyMsg;

    public ChatBotSendMsg(ChatBotReplyMsg chatBotReplyMsg, SessionName sessionName, SessionsClient sessionsClient, QueryInput queryInput) {
        this.chatBotReplyMsg = chatBotReplyMsg;
        this.sessionName = sessionName;
        this.sessionsClient = sessionsClient;
        this.queryInput = queryInput;
    }

    @Override
    protected DetectIntentResponse doInBackground(Void... voids) {
        try {
            DetectIntentRequest detectIntentRequest =
                    DetectIntentRequest.newBuilder()
                            .setSession(sessionName.toString())
                            .setQueryInput(queryInput)
                            .build();
            return sessionsClient.detectIntent(detectIntentRequest);
        } catch (Exception e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(DetectIntentResponse response) {

        //handle chat bot response
        chatBotReplyMsg.callback(response);

    }
}