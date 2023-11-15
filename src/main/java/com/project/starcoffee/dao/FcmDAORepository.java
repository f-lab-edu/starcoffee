package com.project.starcoffee.dao;

import com.google.firebase.messaging.Message;

import java.util.List;

public interface FcmDAORepository {
    void addMemberToken(String token, String memberId);
    void addStoreToken(String token, long storeId);
    List<String> getMemberTokens(String memberId);
    List<String> getStoreTokens(long storeId);
    void addMemberErrorPush(String memberId, List<Message> messages);
    void addStoreErrorPush(long storeId, List<Message> messages);

}
