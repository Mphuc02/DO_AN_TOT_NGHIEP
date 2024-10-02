package dev.common.constant;

public class ExceptionConstant {
    public static final class AUTHENTICATION_EXCEPTION {
        public static final String FAIL_AUTHENTICATION = "Tài khoản hoặc mật khẩu không chính xác";
        public static final String FAIL_VALIDATION_ACCOUNT = "Lỗi khi kiểm tra thuộc tính của Account";
        public static final String NUMBER_PHONE_EXISTED = "Đã tồn tại tài khoản với số điện thoại này";
    }
    public static final class HOSPITAL_INFORMATION_EXCEPTION {
        public static final String PROVINCE_ID_NOT_FOUND = "Tỉnh với id không tồn tại";
        public static final String EXAMINATION_ROOM_NOT_FOUND = "Phòng khám với id không tồn tại";
        public static final String FAIL_VALIDATION_ROOM = "Lỗi khi kiểm tra thuộc tính ExaminationRoom";

        public static final String DISEASE_NOT_FOUND = "Bệnh với id không tồn tại";
        public static final String FAIL_VALIDATION_DISEASE = "Lỗi khi kiểm tra thuộc tính DISEASE";
        public static final String FAIL_CHECK_ADDRESS = "Lỗi khi kiểm tra địa chỉ";
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
        public static final String FORM_NOT_FOUND = "Không tìm thấy ExaminationForm với id";
        public static final String FAIL_VALIDATION_FORM = "Lôi khi kiểm tra thuộc tính của Form";
    }

    public static final class WORKING_SCHEDULE_EXCEPTION{
        public static final String FAIL_VALIDATION_SCHEDULE = "Lỗi khi kiểm tra thuộc tính CreateWorkingSchedule";
        public static final String ROOM_HAS_BEEN_SELECTED = "Phòng khám đã được lên lịch làm việc";
        public static final String WORKING_SCHEDULE_NOT_FOUND = "Lịch làm việc không tồn tại";
        public static final String NOT_PERMISSION_WITH_SCHEDULE = "Không có quyền thao tác với lịch làm việc này";
        public static final String CAN_NOT_UPDATE_OLD_SCHEDULE = "Không thể chỉnh sửa ngày làm việc đã qua";
    }

    public static final class EXAMINATION_RESULT_EXCEPTION {
        public static final String RESULT_NOT_FOUND = "Không tìm thấy kết quả khám với id";
        public static final String FAIL_VALIDATION_RESULT = "Lỗi khi kiểm tra thuộc tính ExaminationResult";
        public static final String NOT_RESULT_OWNER = "Bạn không có quyền chỉnh sửa với kết quả khám này";
        public static final String FINISHED_RESULT = "Kết quả khám đã được hoàn thành, không thể chỉnh sửa thêm";
        public static final String FAIL_VALIDATION_CONSULTATION_DETAIL = "Lỗi khi kiểm tra chi tiết phiếu tư vấn thuốc";
        public static final String CONSULTATION_FORM_NOT_FOUND = "Không tìm thấy phiếu tư vấn với id này";
        public static final String NOT_CONSULTATION_FORM_OWNER = "Bạn không có quyền chỉnh sửa nội dung phiếu tư vấn này";
        public static final String NOT_HAVE_SCHEDULE_TODAY = "Không thể hoàn thành do hôm nay không có lịch làm việc của bạn";
        public static final String APPOINTMENT_FORM_NOT_FOUND = "Không tìm thấy lịch hẹn này";
        public static final String OUT_OF_TIME_DELETE_APPOINTMENT_FORM = "Hết thời gian xóa lịch khám lại";
        public static final String NOT_OWN_APPOINTMENT_FORM = "Không thể thao tác với lịch hẹn khám lại này";
        public static final String FAIL_VALIDATION_APPOINTMENT_FORM = "Lỗi khi kiểm tra thuộc tính Appointment form";
    }

    public static final class MEDICINE_EXCEPTION {
        public static final String MEDICINE_NOT_FOUND = "Thuốc với id không tồn tại";
        public static final String FAIL_VALIDATION_MEDICINE = "Lỗi khi kiểm tra thuộc tính của Medicine";
        public static final String ORIGIN_NOT_FOUND = "Suất xứ không tồn tại";
        public static final String FAIL_VALIDATION_ORIGIN = "Lỗi khi kiểm tra thuộc tính của Origin";
        public static final String SUPPLIER_NOT_FOUND = "Không tồn tại nhà cung cấp";
        public static final String FAIL_VALIDATION_SUPPLIER = "Lỗi khi kiểm tra Supplier";
        public static final String FAIL_VALIDATION_INVOICE = "Lỗi khi kiểm tra hóa đơn";
        public static final String INVOICE_NOT_FOUND = "Không tìm thấy hóa đơn với id này";
        public static final String FAIL_VALIDATION_PATIENT_MEDICINE_INVOICE = "Lỗi khi kiểm tra thuộc tính của hóa đơn thuốc bệnh nhân";
        public static final String QUANTITY_REQUEST_EXCEED_STOCK = "Thuốc trong kho không đủ";
    }

    public static final class PAYMENT_EXCEPTION {
        public static final String EXAMINATION_COST_NOT_FOUND = "Không tìm thấy chi phí khám bệnh";
        public static final String APPLIED_DAY_MUST_AFTER_TODAY = "Ngày áp dụng phải sau hôm nay";
        public static final String APPLIED_DAY_PASSED = "Đã quá thời gian chỉnh sửa";
        public static final String FAIL_VALIDATION_EXAMINATION_COST = "Lỗi khi kiểm tra thuộc tính ExaminationCost";

        public static final String FAIL_VALIDATION_INVOICE = "Lỗi khi kiểm tra hóa đơn";
    }
}
