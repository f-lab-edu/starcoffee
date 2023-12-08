package com.project.starcoffee.service;

import com.google.firebase.messaging.*;
import com.project.starcoffee.dao.FcmDAO;
import com.project.starcoffee.dto.message.PushMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
    public void addMemberToken(String memberId, String token) {
        fcmDAO.addMemberToken(memberId, token);
    }

    /**
     * 가게 토큰 정보를 저장한다.
     * @param token 토큰 정보
     * @param storeId 가게의 고유 아이디
     */
    public void addStoreToken(long storeId, String token) {
        fcmDAO.addStoreToken(storeId, token);
    }

    /**
     * 고객 토큰정보를 조회한다.
     * @param memberId 고객의 고유 아이디
     * @return
     */
    public String getMemberTokens(String memberId) {
        return fcmDAO.getMemberTokens(memberId);
    }


    /**
     * 사장님 토큰 정보를 조회한다.
     *
     * @param storeId 사장님의 고유 아이디
     * @return
     */
    public String getStoreTokens(long storeId) {
        return fcmDAO.getStoreTokens(storeId);
    }

    /**
     * 고객 아이디로 로그인한 기기에 메세지를 보낸다.
     *
     * @param messageInfo 전송할 푸시 정보
     */
    @Async("Async-Executor")
    public void sendByMember(PushMessage messageInfo, String memberId) {
        String memberToken = fcmDAO.getMemberTokens(memberId);

        Message message = Message.builder()
                .setToken(memberToken)
                .setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300")
                        .setNotification(new WebpushNotification(messageInfo.getTitle(), messageInfo.getMessage())
                        ).build()).build();
        String response;
        try {
            response = FirebaseMessaging.getInstance().sendAsync(message).get();
            log.info("Member Sent message : " + response);
        } catch (ExecutionException e) {
            log.error("토큰정보에 의해 메세지가 전송되지 않았습니다. : {}", e.getMessage());
            addMemberErrorPush(memberId, message);
        } catch (InterruptedException e) {
            log.error("토큰정보에 의해 메세지가 전송되지 않았습니다. : {}", e.getMessage());
            addMemberErrorPush(memberId, message);
        }

    }


    /**
     * 가게에 푸시 메세지를 전송한다.
     * @param messageInfo
     * @param storeId
     */
    @Async("Async-Executor")
    public void sendByStore(PushMessage messageInfo, long storeId) {
        String storeTokens = fcmDAO.getStoreTokens(storeId);

        Message message = Message.builder()
                .setToken(storeTokens)
                .setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300")
                        .setNotification(new WebpushNotification(messageInfo.getTitle(), messageInfo.getMessage())
                        ).build()).build();

        String response;
        try {
            response = FirebaseMessaging.getInstance().sendAsync(message).get();
            log.info("Store Sent message : " + response);
        } catch (ExecutionException e) {
            log.error("토큰정보에 의해 메세지가 전송되지 않았습니다. : {}", e.getMessage());
            addStoreErrorPush(storeId, message);
        } catch (InterruptedException e) {
            log.error("토큰정보에 의해 메세지가 전송되지 않았습니다. : {}", e.getMessage());
            addStoreErrorPush(storeId, message);
        }
    }

    public void deleteMemberToken(String memberId) {
        fcmDAO.deleteToken(memberId);
    }
    public void addMemberErrorPush(String memberId, Message message){
        fcmDAO.addMemberErrorPush(memberId, message);
    }
    public void addStoreErrorPush(long storeId, Message message) {
        fcmDAO.addStoreErrorPush(storeId,message);
    }

}
