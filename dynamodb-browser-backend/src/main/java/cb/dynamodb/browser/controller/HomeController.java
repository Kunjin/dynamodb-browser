package cb.dynamodb.browser.controller;

import cb.dynamodb.browser.constants.Operators;
import cb.dynamodb.browser.dto.ConfigurationDto;
import cb.dynamodb.browser.service.ConfigurationsService;
import cb.dynamodb.browser.service.DynamodbService;
import cb.dynamodb.browser.service.SearchService;
import com.amazonaws.regions.Regions;
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

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
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
    public ResponseEntity<Map<String, String>> getHashKeyByTableName(@PathVariable String table) {
        Map<String, String> map = new HashMap<>();
        map.put("hashKey", searchService.getHashKey(table));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping(value = "rangekey/{table}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getRangeKeyByTableName(@PathVariable String table) {
        Map<String, String> map = new HashMap<>();
        map.put("rangeKey", searchService.getRangeKey(table));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping("data/{table}")
    public List<String> queryTableByHashKey(@PathVariable String table,
                                            @RequestParam String hashKey,
                                            @RequestParam String hashValue,
                                            @RequestParam String operator,
                                            @RequestParam(required = false) String rangeKey,
                                            @RequestParam(required = false) String rangeValue,
                                            @RequestParam(required = false) String operatorRangeKey) {

        if (rangeKey != null && rangeValue != null && operatorRangeKey != null) {
            return searchService.queryByHashKeyAndRangeKey(table, hashKey, hashValue, operator, rangeKey, rangeValue, operatorRangeKey);
        }

        return searchService.queryByHashKey(table, hashKey, hashValue, operator);
    }

    @RequestMapping("operations")
    public Operators[] getScanOperations() {
        return Operators.values();
    }

    @RequestMapping("records/{table}")
    public List<String> queryAllByTable(@PathVariable String table) {
        return searchService.queryAllByTable(table);
    }

    @RequestMapping("secondaryIndexRangeKey/{table}")
    public ResponseEntity<Map<String, String>> getSecondaryIndexRangeKey(@PathVariable String table) {
        Map<String, String> map = new HashMap<>();
        map.put("rangeKey", searchService.getSecondaryIndexRangeKey(table));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping("regions")
    public Regions[] getRegions() {
        return dynamodbService.getRegions();
    }

    @RequestMapping("awsProfiles")
    public Set<String> getAwsProfile() {
        return dynamodbService.getProfilesInCredentials();
    }

    @PostMapping("settings")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Map<String, String>>  saveConfigurationSettings(@RequestBody ConfigurationDto configurationDto) {
        configurationsService.updateConfigFile(configurationDto);
        Map<String, String> map = new HashMap<>();
        map.put("responseCode", HttpStatus.OK.toString());
        map.put("message", "successful");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
