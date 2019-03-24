package cb.dynamodb.browser.dto;

public class ConfigurationDto {

    private String region;
    private String profile;
    private boolean isAwsProfileUsed;
    private String accessKey;
    private String secretKey;

    public ConfigurationDto() {}

    public ConfigurationDto(String region, String profile, boolean isAwsProfileUsed, String accessKey, String secretKey) {
        this.region = region;
        this.profile = profile;
        this.isAwsProfileUsed = isAwsProfileUsed;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public boolean isAwsProfileUsed() {
        return isAwsProfileUsed;
    }

    public void setAwsProfileUsed(boolean awsProfileUsed) {
        isAwsProfileUsed = awsProfileUsed;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String toString() {
        return "ConfigurationDto{" +
                "region='" + region + '\'' +
                ", profile='" + profile + '\'' +
                ", isAwsProfileUsed=" + isAwsProfileUsed +
                ", accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                '}';
    }
}
