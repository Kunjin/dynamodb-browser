package cb.dynamodb.browser.dto;

import org.codehaus.jackson.annotate.JsonProperty;

public class ConfigurationDto {

    @JsonProperty
    private String region;

    @JsonProperty
    private String profile;

    @JsonProperty
    private Boolean isAwsProfileUsed;

    @JsonProperty
    private String accessKey;

    @JsonProperty
    private String secretKey;

    public ConfigurationDto() {}

    public ConfigurationDto(String region, String profile, Boolean isAwsProfileUsed, String accessKey, String secretKey) {
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

    public Boolean getIsAwsProfileUsed() {
        return isAwsProfileUsed;
    }

    public void setIsAwsProfileUsed(Boolean awsProfileUsed) {
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
