package dev.common.constant;

public class KafkaTopicsConstrant {
    public static final String CREATE_PATIENT_ACCOUNT_FROM_GREETING_TOPIC = "${kafka.topics.create-patient-account-from-greeting}";
    public static final String CREATED_PATIENT_ACCOUNT_SUCCESS_TOPIC = "${kafka.topics.created-patient-account-from-greeting-success}";
    public static final String CREATED_PATIENT_INFORMATION_TOPIC = "${kafka.topics.created-patient-information-topic}";
    public static final String CREATE_EXAMINATION_RESULT_FROM_GREETING_TOPIC = "${kafka.topics.create-examination-topic}";
    public static final String FAIL_CREATE_PATIENT_FROM_GREETING_TOPIC = "${kafka.topics.fail-create-patient-from-greeting-topic}";

    public static final String CREATE_EMPLOYEE_TOPIC = "${kafka.topics.create-employee-topic}";
    public static final String CREATED_EMPLOYEE_TOPIC = "${kafka.topics.created-employee-topic}";
    public static final String PROCESSED_IMAGE = "${kafka.topics.processed-image}";

    public static final String PATIENT_GROUP = "${kafka.group-id.patient}";
    public static final String AUTHENTICATION_GROUP = "${kafka.group-id.authentication}";
    public static final String EXAMINATION_RESULT_GROUP = "${kafka.group-id.examination-result}";
    public static final String CREATED_EXAMINATION_RESULT_SUCCESS = "${kafka.topics.created-examination-result-success}";
    public static final String PAYMENT_GROUP = "${kafka.group-id.payment-group}";

    public static final String GREETING_GROUP = "${kafka.group-id.greeting}";
    public static final String WEBSOCKET_GROUP = "${kafka.group-id.websocket}";
}