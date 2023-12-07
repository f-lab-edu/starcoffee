package com.project.starcoffee.dao;

import com.google.firebase.messaging.Message;

import java.util.List;

public interface FcmDAORepository {
    void addMemberToken(String memberId, String token);
    void addStoreToken(long storeId, String token);
    String getMemberTokens(String memberId);
    String getStoreTokens(long storeId);
    void addMemberErrorPush(String memberId, Message message);
    void addStoreErrorPush(long storeId, Message message);

}
