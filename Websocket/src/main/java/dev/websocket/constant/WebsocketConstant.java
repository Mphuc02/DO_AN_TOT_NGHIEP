package dev.websocket.constant;

import java.util.UUID;

public class WebsocketConstant {
    public static final class TOPIC{
        public static String CREATE_EMPLOYEE_TOPIC(UUID id){
            return String.format("/topic/create-employee/%s", id);
        }

        public static String CREATED_EMPLOYEE_TOPIC(UUID id){
            return String.format("/topic/created-employee/%s", id);
        }
        public static String PROCESSED_IMAGE(UUID id) {return String.format("/topic/processed-image/%s", id);}
        public static String UPDATED_NUMBER_EXAMINATION_FORM(UUID id){
            return String.format("/topic/updated-number-examination-form/%s", id);
        }
        public static String chattingTopic(UUID receiverId){
            return String.format("/topic/chat/user/%s", receiverId);
        }
    }

    public static final class MESSAGE{
        public static final String CREATED_EMPLOYEE_ACCOUNT = "Đã tạo thành công tài khoản nhân viên";
        public static final String CREATED_EMPLOYEE = "Đã tạo thành công thông tin nhân viên";
        public static final String PROCESSED_IMAGE = "Kết qủa chuẩn đoán";
        public static final String UPDATED_NUMBER_EXAMINATION_FORM = "Tạo thành công phiếu khám bệnh";
        public static final String DETECTED_IMAGE_MESSAGE = "Chuẩn đoán thành công hình ảnh của tin nhắn";
        public static final String NEW_MESSAGE = "Bạn có tin nhắn mới";
    }
}