package cb.dynamodb.browser.aws;

import cb.dynamodb.browser.dto.ConfigurationDto;
import cb.dynamodb.browser.service.ConfigurationsService;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfiguration.class);


    public AmazonDynamoDB getAmazonDynamoDbClient() {
        ConfigurationsService configurationsService = new ConfigurationsService();
        ConfigurationDto configurationDto = configurationsService.readConfigFile();
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.fromName(configurationDto.getRegion()))
                .withCredentials(profileCredentialsProvider(configurationDto.getProfile()))
                .build();
    }

    public ProfileCredentialsProvider profileCredentialsProvider(String awsProfile) {
        ProfileCredentialsProvider profileCredentialsProvider = new ProfileCredentialsProvider();
        if (!awsProfile.isEmpty()) {
            LOGGER.info("AWS awsProfile used: {}", awsProfile);
            profileCredentialsProvider = new ProfileCredentialsProvider(awsProfile);
        }
        return profileCredentialsProvider;
    }

    public DynamoDB amazonDynamoDB() {
        return new DynamoDB(getAmazonDynamoDbClient());
    }
}
