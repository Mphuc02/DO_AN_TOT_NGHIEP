package dev.common.constant;

public class RedisPrefixConstant {
    public static String USER_PHONE_PREFIX(String phone){
        return String.format("User:Phone:%s", phone);
    }
}