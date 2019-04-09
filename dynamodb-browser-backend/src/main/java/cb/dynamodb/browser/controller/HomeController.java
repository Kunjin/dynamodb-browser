package cb.dynamodb.browser.controller;

import cb.dynamodb.browser.constants.Operators;
import cb.dynamodb.browser.dto.ConfigurationDto;
import cb.dynamodb.browser.dto.ExclusiveKeys;
import cb.dynamodb.browser.dto.ScanResults;
import cb.dynamodb.browser.service.ConfigurationsService;
import cb.dynamodb.browser.service.DynamodbService;
import cb.dynamodb.browser.service.SearchService;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/")
public class HomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private SearchService searchService;

    @Autowired
    private DynamodbService dynamodbService;

    @Autowired
    private ConfigurationsService configurationsService;


    @GetMapping("tables")
    public List<String> listTables() {
        return searchService.getTableNames();
    }

    @RequestMapping(value = "hashkey/{table}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getHashKeyByTableName(@PathVariable String table) {
        return searchService.getHashKey(table);
    }

    @RequestMapping(value = "rangekey/{table}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getRangeKeyByTableName(@PathVariable String table) {
        return searchService.getRangeKey(table);
    }

    @RequestMapping("data/{table}")
    public List<String> queryTableByHashKey(@PathVariable String table,
                                            @RequestParam String hashKey,
                                            @RequestParam String hashValue,
                                            @RequestParam String operator,
                                            @RequestParam(required = false) String rangeKey,
                                            @RequestParam(required = false) String rangeValue,
                                            @RequestParam(required = false) String operatorRangeKey) {

        if (!StringUtils.isNullOrEmpty(rangeKey) && !StringUtils.isNullOrEmpty(rangeValue) && !StringUtils.isNullOrEmpty(operatorRangeKey)) {
            return searchService.queryByHashKeyAndRangeKey(table, hashKey, hashValue.trim(), operator, rangeKey, rangeValue.trim(), operatorRangeKey);
        }

        return searchService.queryByHashKey(table, hashKey, hashValue.trim(), operator);
    }

    @RequestMapping("operations")
    public Operators[] getScanOperations() {
        return searchService.getOperations();
    }

    @RequestMapping("records/{table}")
    public ScanResults scanTable(@PathVariable String table, ExclusiveKeys exclusiveKeys) {
        LOGGER.info("{} ", searchService.scanByTable(table, exclusiveKeys));
        return searchService.scanByTable(table, exclusiveKeys);
    }

    @RequestMapping("secondaryIndexRangeKey/{table}")
    public ResponseEntity<Map<String, String>> getSecondaryIndexRangeKey(@PathVariable String table) {
        Map<String, String> map = new HashMap<>();
        map.put("rangeKey", searchService.getSecondaryIndexRangeKey(table));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping("regions")
    public List<String> getRegions() {
        return dynamodbService.getRegions();
    }

    @RequestMapping("awsProfiles")
    public Set<String> getAwsProfile() {
        return dynamodbService.getProfilesInCredentials();
    }

    @PostMapping("settings")
    public ResponseEntity<Map<String, String>>  saveConfigurationSettings(@RequestBody ConfigurationDto configurationDto) {
        configurationsService.createUpdateConfigFile(configurationDto);
        Map<String, String> map = new HashMap<>();
        map.put("responseCode", HttpStatus.OK.toString());
        map.put("message", "successful");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("settings")
    @ResponseBody
    public ConfigurationDto loadConfigurationSettings() {
        return configurationsService.readConfigFile();
    }

    @GetMapping("details/{table}")
    @ResponseBody
    public DescribeTableResult getTableDetails(@PathVariable String table) {
        return searchService.getTableDetails(table);
    }

}
