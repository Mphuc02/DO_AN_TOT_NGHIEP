package dev.common.constant;

public class ExceptionConstant {
    public static final class AUTHENTICATION_EXCEPTION {
        public static final String FAIL_AUTHENTICATION = "Tài khoản hoặc mật khẩu không chính xác";
        public static final String FAIL_VALIDATION_ACCOUNT = "Lỗi khi kiểm tra thuộc tính của Account";
    }
    public static final class HOSPITAL_INFORMATION_EXCEPTION {
        public static final String PROVINCE_ID_NOT_FOUND = "Tỉnh với id không tồn tại";
    }
    public static final class FACULTY_EXCEPTION{
        public static final String FAIL_VALIDATION_FACULTY = "Lỗi khi kiểm tra thuộc tính của Faculty";
        public static final String FACULTY_NOT_FOUND = "Khoa với id không tồn tại";
        public static final String POSITION_NOT_FOUND = "Vị trí với id không tồn tại";
        public static final String FAIL_VALIDATION_POSITION = "Lỗi khi kiểm tra thuộc tính của Position";
    }

    public static final class EMPLOYEE_EXCEPTION{
        public static final String FAIL_VALIDATION_EMPLOYEE = "Lỗi khi kiểm tra thuộc tính của Employee";
        public static final String EMPLOYEE_NOT_FOUND = "Không tìm thấy nhân viên với id này";
        public static final String EMPLOYEE_HAD_ROLES = "Nhân viên đã có các vai trò sau";
        public static final String EMPLOYEE_DID_NOT_HAD_ROLES = "Nhân viên không có các vai trò sau";
        public static final String FULL_NAME_NOT_FOUND = "Họ và tên của nhân viên không tồn tại";
        public static final String NOT_PERMISSION = "Bạn không có quyền thao tác";
    }

    public static final class GREETING_EXCEPTION{
        public static final String FAIL_VALIDATION_FORM = "Lôi khi kiểm tra thuộc tính của Form";
    }
}
