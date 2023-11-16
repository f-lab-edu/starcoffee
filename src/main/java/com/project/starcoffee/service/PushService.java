package com.project.starcoffee.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.project.starcoffee.dao.FcmDAO;
import com.project.starcoffee.dto.message.PushMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PushService {

    private final FcmDAO fcmDAO;

    @Value("${fcm.key.path}")
    private String FCM_KEY_PATH;

    @Autowired
    public PushService(FcmDAO fcmDAO) {
        this.fcmDAO = fcmDAO;
    }

    /**
     * 회원 토큰정보를 저장한다.
     *
     * @param token 토큰 정보
     * @param memberId 고객의 고유 아이디
     */
    public void addMemberToken(String token, String memberId) {
        fcmDAO.addMemberToken(token, memberId);
    }

    /**
     * 가게 토큰 정보를 저장한다.
     * @param token 토큰 정보
     * @param StoreId 가게의 고유 아이디
     */
    public void addStoreToken(String token, long StoreId) {
        fcmDAO.addStoreToken(token, StoreId);
    }

    /**
     * 고객 토큰정보를 조회한다.
     * @param memberId 고객의 고유 아이디
     * @return
     */
    public List<String> getMemberTokens(String memberId) {
        return fcmDAO.getMemberTokens(memberId);
    }


    /**
     * 사장님 토큰 정보를 조회한다.
     *
     * @param storeId 사장님의 고유 아이디
     * @return
     */
    public List<String> getStoreTokens(long storeId) {
        return fcmDAO.getStoreTokens(storeId);
    }


    /**
     * 고객 아이디로 로그인한 기기에 메세지를 보낸다.
     *
     * @param messageInfo 전송할 푸시 정보
     */
    public void sendByMember(PushMessage messageInfo, String memberId) {
        List<String> tokens = fcmDAO.getMemberTokens(memberId);

        List<Message> messages = tokens.stream()
                .map(token -> Message.builder()
                        .putData("title", messageInfo.getTitle())
                        .putData("message", messageInfo.getMessage())
                        .putData("time", String.valueOf(Timestamp.valueOf(LocalDateTime.now())))
                        .setToken(token)
                        .build()).collect(Collectors.toList());

        BatchResponse response;
        try {
            response = FirebaseMessaging.getInstance().sendEach(messages);
            log.info("Member Sent message : " + response);
        } catch (FirebaseMessagingException e) {
            log.error("토큰정보에 의해 메세지가 전송되지 않았습니다. : {}", e.getMessage());
            addMemberErrorPush(memberId, messages);
        }

    }


    /**
     * 가게에 푸시 메세지를 전송한다.
     * @param messageInfo
     * @param storeId
     */
    public void sendByStore(PushMessage messageInfo, long storeId) {
        List<String> tokens = fcmDAO.getStoreTokens(storeId);

        List<Message> messages = tokens.stream()
                .map(token -> Message.builder()
                        .putData("title", messageInfo.getTitle())
                        .putData("message", messageInfo.getMessage())
                        .putData("time", String.valueOf(Timestamp.valueOf(LocalDateTime.now())))
                        .setToken(token)
                        .build()).collect(Collectors.toList());

        BatchResponse response;
        try {
            response = FirebaseMessaging.getInstance().sendEach(messages);
            log.info("Store Sent message : " + response);
        } catch (FirebaseMessagingException e) {
            log.error("토큰정보에 의해 메세지가 전송되지 않았습니다. : {}", e.getMessage());
            addStoreErrorPush(storeId, messages);
        }
    }

    public void deleteMemberToken(String memberId) {
        fcmDAO.deleteToken(memberId);
    }

    public void addMemberErrorPush(String memberId, List<Message> messages){
        fcmDAO.addMemberErrorPush(memberId, messages);
    }
    public void addStoreErrorPush(long storeId, List<Message> messages) {
        fcmDAO.addStoreErrorPush(storeId,messages);
    }

}
