package cb.dynamodb.browser.service;

import cb.dynamodb.browser.aws.DatabaseConfiguration;
import cb.dynamodb.browser.constants.Operators;
import cb.dynamodb.browser.dao.SearchDao;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    private SearchDao searchDao;

    public List<String> queryByHashKey(String table, String hashKey, String value, String operator) {
        return searchDao.searchByHashKey(table, hashKey, value, operator);
    }

    public List<String> queryByHashKeyAndRangeKey(String table, String hashKey, String value, String operator, String rangeKey, String rangeKeyValue, String operatorRangeKey) {
        return searchDao.searchByHashKeyAndRangeKey(table, hashKey, value, operator, rangeKey, rangeKeyValue, operatorRangeKey);
    }

    public Map<String, String> getHashKey(String table) {
        TableDescription tableSchema = getAmazonDynamodbClient().describeTable(table).getTable();

        Map<String, String> map = new HashMap<>();
        map.put("key", getAttributeName(tableSchema, KeyType.HASH));
        map.put("data_type", getKeyType(tableSchema, KeyType.HASH));
        return map;
    }

    public List<String> getTableNames() {
        return getAmazonDynamodbClient().listTables().getTableNames();
    }

    public Map<String, String> getRangeKey(String table) {
        TableDescription tableSchema = getAmazonDynamodbClient().describeTable(table).getTable();

        Map<String, String> map = new HashMap<>();
        map.put("key", getAttributeName(tableSchema, KeyType.RANGE));
        map.put("data_type", getKeyType(tableSchema, KeyType.RANGE));
        return map;
    }

    public List<String> queryAllByTable(String table) {
        return searchDao.searchAllByTable(table);
    }

    public String getSecondaryIndexRangeKey(String table) {
        return searchDao.getSecondaryIndexRangeKey(table);
    }

    public Operators[] getOperations() {
//        List<String> operationsList = new ArrayList<>();
//        for (Operators operator : Operators.values()) {
//            operationsList.add(operator.getOperator());
//        }
//        return operationsList;
        return Operators.values();
    }

    public DescribeTableResult getTableDetails(String table) {
        return getAmazonDynamodbClient().describeTable(table);
    }


    private AmazonDynamoDB getAmazonDynamodbClient() {
        return new DatabaseConfiguration().getAmazonDynamoDbClient();
    }


    private String getAttributeName(TableDescription tableSchema, KeyType hash) {
        return tableSchema.getKeySchema().stream()
                .filter(x -> x.getKeyType().equals(hash.toString()))
                .findFirst().get().getAttributeName();
    }

    private String getKeyType(TableDescription tableSchema, KeyType hash) {
        return tableSchema.getKeySchema().stream()
                .filter(x -> x.getKeyType().equals(hash.toString()))
                .findFirst().get().getAttributeName();
    }
}
