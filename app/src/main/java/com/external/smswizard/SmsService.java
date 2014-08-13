package com.external.smswizard;

import com.external.smswizard.model.Message;
import com.external.smswizard.model.Token;

import java.util.List;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface SmsService {
    @FormUrlEncoded
    @POST("/get_token")
    Token getToken(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/get_outgoing_messages")
    List<Message> getOutgoingMessages(@Field("token") String token, @Field("email") String email);

    @FormUrlEncoded
    @POST("/set_incoming_message")
    String setIncomingMessage(@Field("token") String token, @Field("message_id") String messageId);
}
