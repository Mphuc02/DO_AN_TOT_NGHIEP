package dev.common.constant;

public class ApiConstant {
    public static final class AUTHENTICATION_URL {
        public static final String URL = "/api/v1/authentication";
        public static final String REGISTER = "/register";
        public static final String AUTHENTICATE = "/authenticate";
        public static final String EMPLOYEE = "/employee";
    }
    public static final class HOSPITAL_INFORMATION {
        public static final class PROVINCE_URL {
            public static final String URL = "/api/v1/provinces";
            public static final String CHECK_ADDRESS = "/check-address";
            public static final String PROVINCE_ID_PARAM = "province-id";
            public static final String DISTRICT_ID_PARAM = "district-id";
            public static final String COMMUNE_ID_PARAM = "commune-id";

        }
    }

    public static final class FACULTY_URL{
        public static final String URL = "/api/v1/faculties";
        public static final String ID = "/{id}";
        public static final String POSITION_URL = "/api/v1/positions";
        public static final String FACULTY_ID = "/faculty/{id}";
    }

    public static final class EMPLOYEE_URL{
        public static final String URL = "/api/v1/employees";
    }
}