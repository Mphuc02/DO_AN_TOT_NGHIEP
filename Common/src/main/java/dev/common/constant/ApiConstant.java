package dev.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public class ApiConstant {
    public static final class AUTHENTICATION_URL {
        public static final String SERVICE_NAME = "AUTHENTICATION";
        public static final String URL = "/api/v1/authentication";
        public static final String REGISTER = "/register";
        public static final String AUTHENTICATE = "/authenticate";
        public static final String EMPLOYEE_AUTHENTICATION = "/authentication-employee";
        public static final String EMPLOYEE = "/create-employee";
        public static final String EXCHANGE_TOKEN = "/exchange-token";
        public static final String LOGOUT = "/logout";
        public static final class INTERNAL_URL{
            public static final String URL = "/api/v1/internal/authentication";
            public static final String CHECK_PHONE_NUMBER_EXIST = "/check-phone-number";
        }
    }
    public static final class HOSPITAL_INFORMATION {
        public static final String SERVICE_NAME = "HOSPITALINFORMATION";
        public static final String PROVINCE_URL = "/api/v1/provinces";
        public static final String CHECK_ADDRESS = "/check-address";
        public static final String EXAMINATION_ROOM_URL = "/api/v1/examination-rooms";
        public static final String CHECK_ROOM_EXIST = "/check-room/{id}";
        public static final String ID = "{id}";
        public static final String GET_BY_IDS = "/find-by-ids";
        public static final String CHECK_DISEASES_EXIST = "/check";
        public static final String DISEASE_URL = "/api/v1/diseases";
    }

    public static final class FACULTY_URL{
        public static final String URL = "/api/v1/faculties";
        public static final String ID = "/{id}";
        public static final String POSITION_URL = "/api/v1/positions";
        public static final String FACULTY_ID = "/faculty/{id}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class EmployeeUrl {
        public static final String URL = "/api/v1/employees";
        public static final String SERVICE_NAME = "EMPLOYEE";
        public static final String ROLE_URL = "/api/v1/roles";
        public static final String ID = "/{id}";
        public static final String FIND_BY_IDS = "/find-by-ids";
        public static final String GET_LOGGED_USER_INFORMATION = "/get-logged-user-information";
    }

    public static final class GREETING_URL{
        public static final String EXAMINATION_FORM_URL = "/api/v1/greeting";
        public static final String TICKET_URL = "/api/v1/tickets";
        public static final String PRINT = "/print";
        public static final String FIRST_TIME = "/first-time";
        public static final String WITH_APPOINTMENT = "/with-appointment";
        public static final String FIND_RECEIVED_PATIENTS_TODAY = "find/received-patients-today";
    }

    public static final class WORKING_SCHEDULE_URL{
        public static final String URL = "/api/v1/working-schedules";
        public static final String SERVICE_NAME = "WORKINGSCHEDULE";
        public static final String ID = "/{id}";
        public static final String SEARCH = "/search";
        public static final String CHECK_SCHEDULE_TODAY = "/check-schedule-today/{id}";
        public static final String GET_SCHEDULE_TODAY_OF_EMPLOYEE = "/today/{id}";
        public static final String GET_SCHEDULES_IN_MONTH_OF_EMPLOYEE = "/schedules-in-month";
        public static final String GET_SCHEDULES_BY_DATE = "/find/by-date";
    }

    public static final class EXAMINATION_RESULT_URL{
        public static final String RESULT_URL = "/api/examination-results";
        public static final String ID = "/{id}";
        public static final String CONSULTATION_FORM = "/api/consultation-form";

        public static final String APPOINTMENT_FORM_NEXT_EXAMINATION = "/api/v1/appointment-form-next-examination";
        public static final String FIND_WAITING_EXAMINATION_PATIENTS = "/find/waiting-examination-patients";
    }

    public static final class MEDICINE_URL{
        public static final String SERVICE_NAME = "MEDICINE";
        public static final String MEDICINE_URL = "/api/v1/medicines";
        public static final String CREATE_PATIENT_MEDICINE_INVOICE = "/create-patient-medicine-invoice";
        public static final String ID = "/{id}";
        public static final String CHECK_MEDICINES_EXIST = "/check";
        public static final String ORIGIN_URL = "/api/v1/origins";
        public static final String SUPPLIER_URL = "/api/v1/suppliers";
        public static final String IMPORT_INVOICE_URL = "/api/v1/import-invoices";
        public static final String SEARCH = "/search";
    }

    public static final class PATIENT{
        public static final String SERVICE_NAME = "PATIENT";
        public static final String PATIENT_URL = "/api/patients";
        public static final String GET_BY_IDS = "/get/by-ids";
        public static final String ID = "/{id}";
        public static final String CHECK_EXIST_PATIENT = "/check/{id}";
        public static final String APPOINTMENT_URL = "/api/appointments";
        public static final String GET_APPOINTMENTS_OF_TODAY = "/today";
        public static final String GET_EXAMINED_APPOINTMENTS_OF_TODAY = "/today/examined";
        public static final String FIND_DETAILS_BY_APPOINTMENT_ID = "/find-detail-by-appointment-id/{id}";
        public static final String DOCTOR_CREATE_APPOINTMENT = "/create-appointment-by-doctor";
        public static final String GET_LOGGED_USER_INFORMATION = "/get-logged-user-information";
    }

    public static final class PAYMENT{
        public static final String EXAMINATION_COST_URL = "/api/examination-costs";
        public static final String ID = "/{id}";

        public static final String INVOICE_URL = "/api/invoices";
    }

    public static final class CHAT{
        public static final String CHAT_URL = "/api/chat";
        public static final String RECEIVER_ID = "/with/{id}";
        public static final String SEND_IMAGE = "/send-image/{id}";
        public static final String RELATION_SHIP_URL = "/api/relation-ship";
        public static final String FIND_RELATION_SHIPS_OF_DOCTOR = "/find-relation-ships-of-doctor";
        public static final String FIND_RELATION_SHIPS_OF_PATIENT = "/find-relation-ships-of-patient";
    }
}