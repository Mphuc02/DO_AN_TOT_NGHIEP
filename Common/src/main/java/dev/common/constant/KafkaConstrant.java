package dev.common.constant;

public class KafkaConstrant {
    public static final class TOPICS{
        public static final String CREATE_EMPLOYEE_TOPIC = "${kafka.topics.create-employee-topic}";
    }
    public static final class GROUP_ID{
        public static final String ACCOUNT_GROUP = "${kafka.group-id.account}";
    }
}
