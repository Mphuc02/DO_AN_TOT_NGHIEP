package dev.common.constant;

public class ExceptionConstant {
    public static final class AUTHENTICATION_EXCEPTION {
        public static final String FAIL_AUTHENTICATION = "Tài khoản hoặc mật khẩu không chính xác";
        public static final String FAIL_VALIDATION_ACCOUNT = "Lỗi khi kiểm tra thuộc tính của Account";
    }
    public static final class HOSPITAL_INFORMATION{
        public static final String FACULTY_NOT_FOUND = "Khoa với id không tồn tại";
        public static final String FAIL_VALIDATION_FACULTY = "Lỗi khi kiểm tra thuộc tính của Faculty";
    }
}
