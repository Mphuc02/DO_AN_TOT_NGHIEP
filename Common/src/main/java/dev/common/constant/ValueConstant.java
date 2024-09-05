package dev.common.constant;

public class ValueConstant {
    public static final class JWT{
        public static final String SECRET_KEY = "${jwt.secret-key}";
        public static final String ACCESS_TOKEN_EXPIRATION = "${jwt.access-token=expiration}";
    }
}
