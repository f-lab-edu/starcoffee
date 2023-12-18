package com.project.starcoffee.utils;

import java.util.UUID;

public class RedisKeyFactory {

    public enum Key {
        CART, FCM_MEMBER, FCM_MEMBER_ERROR, FCM_STORE, FCM_STORE_ERROR
    }

    public RedisKeyFactory() {
    }

    private static String generateKey(String id, Key key) {
        return id + ":" + key;
    }

    public static String generateCartKey(UUID cartId) {
        String key = cartId.toString();
        return generateKey(key, Key.CART);
    }

    public static String generateFcmMemberKey(String memberId) {
        return generateKey(memberId, Key.FCM_MEMBER);
    }

    public static String generateFcmStoreKey(long storeId) {
        return generateKey(String.valueOf(storeId), Key.FCM_STORE);
    }

    public static String getIdFromKey(String key) {
        return key.substring(0, key.indexOf(":"));
    }

    public static String generateFcmMemberErrorKey(String memberId) {
        return generateKey(memberId, Key.FCM_MEMBER_ERROR);
    }

    public static String generateFcmStoreErrorKey(long storeId) {
        return generateKey(String.valueOf(storeId), Key.FCM_STORE_ERROR);
    }
}
