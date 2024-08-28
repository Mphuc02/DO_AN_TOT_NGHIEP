package dev.common.constant;

public class ExceptionConstant {
    public static final class AUTHENTICATION_EXCEPTION {
        public static final String FAIL_AUTHENTICATION = "Tài khoản hoặc mật khẩu không chính xác";
        public static final String FAIL_VALIDATION_ACCOUNT = "Lỗi khi kiểm tra thuộc tính của Account";
    }
    public static final class HOSPITAL_INFORMATION_EXCEPTION {

    }
    public static final class FACULTY_EXCEPTION{
        public static final String FAIL_VALIDATION_FACULTY = "Lỗi khi kiểm tra thuộc tính của Faculty";
        public static final String FACULTY_NOT_FOUND = "Khoa với id không tồn tại";
        public static final String POSITION_NOT_FOUND = "Vị trí với id không tồn tại";
        public static final String FAIL_VALIDATION_POSITION = "Lỗi khi kiểm tra thuộc tính của Position";
    }
}
