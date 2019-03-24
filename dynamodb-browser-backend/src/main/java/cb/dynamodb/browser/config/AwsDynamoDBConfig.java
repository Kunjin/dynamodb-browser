package cb.dynamodb.browser.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@PropertySource("classpath:application-dev.properties")
@ComponentScan(value = "cb.dynamodb.browser")
@Configuration
public class AwsDynamoDBConfig {

    @Value("${aws.profile}")
    private String awsProfile;

    @Value("${aws.region}")
    private String awsRegion;

    private static final Logger LOGGER = LoggerFactory.getLogger(AwsDynamoDBConfig.class);

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public AWSCredentials awsCredentials(ProfileCredentialsProvider profileCredentialsProvider) {
        return profileCredentialsProvider.getCredentials();
    }

    @Bean
    public ProfileCredentialsProvider profileCredentialsProvider() {
        ProfileCredentialsProvider profileCredentialsProvider = new ProfileCredentialsProvider();
        if (!awsProfile.isEmpty()) {
            LOGGER.info("AWS awsProfile used: {}", awsProfile);
            profileCredentialsProvider = new ProfileCredentialsProvider(awsProfile);
        }
        return profileCredentialsProvider;
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDbClient(AWSCredentials awsCredentials) {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.fromName(awsRegion))
                .withCredentials(profileCredentialsProvider())
                .build();
    }

    @Bean
    public DynamoDB amazonDynamoDB(AmazonDynamoDB amazonDynamoDb) {
        return new DynamoDB(amazonDynamoDb);
    }
}
