package com.project.starcoffee.dao;

import com.google.firebase.messaging.Message;

import java.util.List;

public interface FcmDAORepository {
    void addMemberToken(String memberId, String token);
    void addStoreToken(long storeId, String token);
    List<String> getMemberTokens(String memberId);
    List<String> getStoreTokens(long storeId);
    void addMemberErrorPush(String memberId, List<Message> messages);
    void addStoreErrorPush(long storeId, List<Message> messages);

}
