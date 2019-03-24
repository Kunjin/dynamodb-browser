package cb.dynamodb.browser.service;

import cb.dynamodb.browser.dto.ConfigurationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class ConfigurationsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationsService.class);
    public static final String CONFIG_FILE = System.getProperty("user.home") + System.getProperty("file.separator") + "dynamodb-browser.config";
    public static final String SEPARATOR = "=";

    public boolean isConfigFileExists() {
        File file = new File(CONFIG_FILE);
        return file.exists();
    }

    public void updateConfigFile(ConfigurationDto configurationDto) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            bw.write(String.format("%s=%s", ConfigurationsEnum.REGION.key(), configurationDto.getRegion()));
            bw.write(System.lineSeparator());
            bw.write(String.format("%s=%s", ConfigurationsEnum.PROFILE.key(), configurationDto.getProfile()));
            bw.write(System.lineSeparator());
            bw.write(String.format("%s=%s", ConfigurationsEnum.IS_AWS_PROFILE_USED.key(), configurationDto.isAwsProfileUsed()));
            bw.write(System.lineSeparator());
            bw.write(String.format("%s=%s", ConfigurationsEnum.ACCESS_KEY.key(), configurationDto.getAccessKey()));
            bw.write(System.lineSeparator());
            bw.write(String.format("%s=%s", ConfigurationsEnum.SECRET_KEY.key(), configurationDto.getSecretKey()));
            bw.flush();

            LOGGER.info("Updated configFile {}", configurationDto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigurationDto readConfigFile() {
        if (isConfigFileExists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
                String line = reader.readLine();
                Map<String, String> configurationsMap = new HashMap<>();
                while (line != null) {
                    String[] split = line.split(SEPARATOR);
                    configurationsMap.put(split[0], split[1]);
                }

                return new ConfigurationDto(
                        configurationsMap.get(ConfigurationsEnum.REGION.key()),
                        configurationsMap.get(ConfigurationsEnum.PROFILE.key()),
                        Boolean.parseBoolean(configurationsMap.get(ConfigurationsEnum.IS_AWS_PROFILE_USED.key())),
                        configurationsMap.get(ConfigurationsEnum.ACCESS_KEY.key()),
                        configurationsMap.get(ConfigurationsEnum.SECRET_KEY.key()));
            } catch (FileNotFoundException e) {
                LOGGER.error("config file does not exists. {}", e);
            } catch (IOException e) {
                LOGGER.error("Encountered exception {}", e);
            }
        }
        //TODO:
        return null;
    }
}
