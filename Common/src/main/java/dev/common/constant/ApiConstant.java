package dev.common.constant;

public class ApiConstant {
    public static final class AUTHENTICATION_URL {
        public static final String SERVICE_NAME = "AUTHENTICATION";
        public static final String URL = "/api/v1/authentication";
        public static final String REGISTER = "/register";
        public static final String AUTHENTICATE = "/authenticate";
        public static final String EMPLOYEE_AUTHENTICATION = "/authentication-employee";
        public static final String EMPLOYEE = "/employee";
        public static final class INTERNAL_URL{
            public static final String URL = "/api/v1/internal/authentication";
            public static final String CHECK_PHONE_NUMBER_EXIST = "/check-phone-number";
        }
    }
    public static final class HOSPITAL_INFORMATION {
        public static final String SERVICE_NAME = "HOSPITALINFORMATION";
        public static final String PROVINCE_URL = "/api/v1/provinces";
        public static final String CHECK_ADDRESS = "/check-address";
        public static final String EXAMINATION_ROOM_URL = "/api/v1/examination-room";
        public static final String CHECK_ROOM_EXIST = "/check-room/{id}";
        public static final String ID = "{id}";
        public static final String CHECK_DISEASES_EXIST = "/check";
        public static final String DISEASE_URL = "/api/v1/diseases";
    }

    public static final class FACULTY_URL{
        public static final String URL = "/api/v1/faculties";
        public static final String ID = "/{id}";
        public static final String POSITION_URL = "/api/v1/positions";
        public static final String FACULTY_ID = "/faculty/{id}";
    }

    public static final class EMPLOYEE_URL{
        public static final String URL = "/api/v1/employees";
        public static final String SERVICE_NAME = "EMPLOYEE";
        public static final String ROLE_URL = "/api/v1/roles";
        public static final String ID = "/{id}";
    }

    public static final class GREETING_URL{
        public static final String EXAMINATION_FORM_URL = "/api/v1/greeting";
        public static final String TICKET_URL = "/api/v1/tickets";
        public static final String PRINT = "/print";
    }

    public static final class WORKING_SCHEDULE_URL{
        public static final String URL = "/api/v1/working-schedules";
        public static final String SERVICE_NAME = "WORKINGSCHEDULE";
        public static final String ID = "/{id}";
        public static final String SEARCH = "/search";
        public static final String CHECk_SCHEDULE_TODAY = "/check-schedule-today/{id}";
        public static final String GET_SCHEDULE_TODAY_OF_EMPLOYEE = "/schedule/today/{id}";
    }

    public static final class EXAMINATION_RESULT_URL{
        public static final String RESULT_URL = "/api/examination-results";
        public static final String ID = "/{id}";
        public static final String CONSULTATION_FORM = "/api/consultation-form";
    }

    public static final class MEDICINE_URL{
        public static final String SERVICE_NAME = "MEDICINE";
        public static final String MEDICINE_URL = "/api/v1/medicines";
        public static final String ID = "/{id}";
        public static final String CHECK_MEDICINES_EXIST = "/check";
        public static final String ORIGIN_URL = "/api/v1/origins";
        public static final String SUPPLIER_URL = "/api/v1/suppliers";
        public static final String IMPORT_INVOICE_URL = "/api/v1/import-invoices";
    }

    public static final class PATIENT{
        public static final String SERVICE_NAME = "PATIENT";
        public static final String PATIENT_URL = "/api/patients";
        public static final String ID = "/{id}";
        public static final String CHECK_EXIST_PATIENT = "/check/{id}";
    }

    public static final class PAYMENT{
        public static final String EXAMINATION_COST_URL = "/api/v1/examination-costs";
        public static final String ID = "/{id}";
    }
}