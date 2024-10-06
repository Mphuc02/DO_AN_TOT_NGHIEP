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
    }

    public static final class MESSAGE{
        public static final String CREATED_EMPLOYEE_ACCOUNT = "Đã tạo thành công tài khoản nhân viên";
        public static final String CREATED_EMPLOYEE = "Đã tạo thành công thông tin nhân viên";
    }
}