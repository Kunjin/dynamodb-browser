package cb.dynamodb.browser.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")
@ComponentScan(value = "cb.dynamodb.browser")
@Configuration
public class AwsDynamoDBConfig {
}
