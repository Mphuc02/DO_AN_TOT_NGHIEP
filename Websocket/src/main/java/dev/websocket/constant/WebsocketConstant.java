package dev.websocket.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebsocketConstant {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Topic {
        public static String createEmployeeTopic(UUID id){
            return String.format("/topic/create-employee/%s", id);
        }

        public static String createdEmployeeTopic(UUID id){
            return String.format("/topic/created-employee/%s", id);
        }
        public static String processedImageTopic(UUID id) {return String.format("/topic/processed-image/%s", id);}
        public static String updatedNumberExaminationFormTopic(UUID id){
            return String.format("/topic/updated-number-examination-form/%s", id);
        }
        public static String chattingTopic(UUID receiverId){
            return String.format("/topic/chat/user/%s", receiverId);
        }
        public static String completedInvoice(UUID id){
            return String.format("/topic/completed-invoice/%s", id);
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class MessageTemplate {
        public static final String CREATED_EMPLOYEE_ACCOUNT = "Đã tạo thành công tài khoản nhân viên";
        public static final String CREATED_EMPLOYEE = "Đã tạo thành công thông tin nhân viên";
        public static final String PROCESSED_IMAGE = "Kết qủa chuẩn đoán";
        public static final String UPDATED_NUMBER_EXAMINATION_FORM = "Tạo thành công phiếu khám bệnh";
        public static final String DETECTED_IMAGE_MESSAGE = "Chuẩn đoán thành công hình ảnh của tin nhắn";
        public static final String NEW_MESSAGE = "Bạn có tin nhắn mới";
        public static final String COMPLETED_PAYMENT_INVOICE = "Thanh toán hóa đơn thành công";
    }
}