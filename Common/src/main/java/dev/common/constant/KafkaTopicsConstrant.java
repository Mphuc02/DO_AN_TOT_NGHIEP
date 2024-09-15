package dev.common.constant;

public class KafkaTopicsConstrant {
    public static final String CREATE_PATIENT_FROM_GREETING_TOPIC = "${kafka.topics.create-patient-from-greeting}";
    public static final String CREATE_EXAMINATION_RESULT_FROM_GREETING_TOPIC = "${kafka.topics.create-examination-topic}";
    public static final String FAIL_CREATE_PATIENT_FROM_GREETING_TOPIC = "${kafka.topics.fail-create-patient-from-greeting-topic}";
    public static final String CREATE_EMPLOYEE_TOPIC = "${kafka.topics.create-employee-topic}";
    public static final String PATIENT_GROUP = "${kafka.group-id.patient}";
    public static final String AUTHENTICATION_GROUP = "${kafka.group-id.authentication}";
    public static final String EXAMINATION_RESULT_GROUP = "${kafka.group-id.examination-result}";
    public static final String ACCOUNT_GROUP = "${kafka.group-id.account}";
}