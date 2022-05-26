package com.example.aimech.ai_chatbot;

import com.google.cloud.dialogflow.v2.DetectIntentResponse;

public interface ChatBotReplyMsg {

    //call back for response
    public void callback(DetectIntentResponse returnResponse);

}
