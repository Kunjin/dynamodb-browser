package cb.dynamodb.browser.service;

public enum ConfigurationsEnum {
    REGION("region"),
    PROFILE("profile"),
    IS_AWS_PROFILE_USED("isAwsProfileUsed"),
    ACCESS_KEY("accessKey"),
    SECRET_KEY("secretKey");

    private String key;

    ConfigurationsEnum(String name) {
        this.key = name;
    }

    public String key() {
        return key;
    }
}
