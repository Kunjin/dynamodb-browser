package cb.dynamodb.browser.dao;

import cb.dynamodb.browser.aws.DatabaseConfiguration;
import cb.dynamodb.browser.constants.Operators;
import cb.dynamodb.browser.dto.ExclusiveKeys;
import cb.dynamodb.browser.dto.KeysAttribute;
import cb.dynamodb.browser.dto.RecordResult;
import cb.dynamodb.browser.dto.ScanResults;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Repository
public class SearchDao {
    private static final String RANGE = "RANGE";

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchDao.class);

    @Value("${dynamodb.pageSize}")
    private int dynamoPageSize;

    public List<String> searchByHashKey(String table, String hashKey, String value, String operator) {
        List<String> results = new ArrayList<>();
        Table dynamoDBTable = getTable(table);

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression(String.format("%s %s :%s", hashKey, Operators.from(operator), hashKey))
                .withValueMap(new ValueMap()
                        .withString(":" + hashKey, value));

        ItemCollection<QueryOutcome> items;
        Iterator<Item> iterator;
        try {
            items = dynamoDBTable.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                results.add(iterator.next().toJSON());
            }
        } catch (Exception e) {
            LOGGER.error("Unable to query from table {} where {} = {} due to: ", table, hashKey, value, e);
        }
        return results;
    }

    public List<String> searchByHashKeyAndRangeKey(String table, String hashKey, String hashKeyValue, String operator, String rangeKey, String rangeKeyValue, String operatorRangeKeyValue) {
        List<String> results = new ArrayList<>();
        Table dynamoDBTable = getTable(table);

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression(String.format("%s %s :%s and %s %s :%s", hashKey, Operators.from(operator), hashKey, rangeKey, Operators.from(operatorRangeKeyValue), rangeKey))
                .withValueMap(new ValueMap()
                        .withString(":" + hashKey, hashKeyValue)
                        .withString(":" + rangeKey, rangeKeyValue));

        ItemCollection<QueryOutcome> items;
        Iterator<Item> iterator;
        try {
            items = dynamoDBTable.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                results.add(iterator.next().toJSON());
            }
        } catch (AmazonDynamoDBException e) {
            try {
                return searchBySecondaryIndex(table, hashKey, hashKeyValue, operator, rangeKey, rangeKeyValue, operatorRangeKeyValue);
            } catch (Exception ex) {
                LOGGER.error("Unable to query from table {} where {} = {} due to:", table, hashKey, hashKeyValue, ex);
            }
        }
        return results;
    }

    public ScanResults scan(String table, KeysAttribute keysAttribute, ExclusiveKeys exclusiveKeys) {

        Table dynamoDBTable = getTable(table);
        ScanSpec spec;

        if (exclusiveKeys.getHashKeyName() == null || exclusiveKeys.getRangeKeyName() == null) {
            spec = new ScanSpec().withMaxPageSize(dynamoPageSize).withMaxResultSize(dynamoPageSize);
        } else {

            if (keysAttribute.getRangeKey() != null) {
                spec = new ScanSpec().withMaxPageSize(dynamoPageSize)
                        .withExclusiveStartKey(
                                exclusiveKeys.getHashKeyName(),
                                exclusiveKeys.getHashKeyValue(),
                                exclusiveKeys.getRangeKeyName(),
                                exclusiveKeys.getRangeKeyValue())
                        .withMaxResultSize(dynamoPageSize);
            } else {
                spec = new ScanSpec().withMaxPageSize(dynamoPageSize)
                        .withExclusiveStartKey(
                                exclusiveKeys.getHashKeyName(),
                                exclusiveKeys.getHashKeyValue())
                        .withMaxResultSize(dynamoPageSize);
            }
        }

        ItemCollection<ScanOutcome> items;
        ScanResults scanResults = new ScanResults();
        List<String> results = new ArrayList<>();
        try {
            items = dynamoDBTable.scan(spec);

            for (Page<Item, ScanOutcome> page : items.pages()) {

                // Process each item on the current page
                Iterator<Item> item = page.iterator();
                Map<String, AttributeValue> lastEvaluatedKeyMap = page.getLowLevelResult().getScanResult().getLastEvaluatedKey();

                ExclusiveKeys keys = new ExclusiveKeys();

                if (lastEvaluatedKeyMap != null) {
                    for (Map.Entry<String, AttributeValue> key : lastEvaluatedKeyMap.entrySet()) {

                        if (key.getKey().equals(keysAttribute.getHashKey())) {
                            keys.setHashKeyName(key.getKey());
                            String keyAttribute = key.getValue().toString().split(":")[1].replaceAll(",}", "")
                                    .replaceAll("}", "").trim();
                            keys.setHashKeyValue(keyAttribute);
                        } else {
                            keys.setRangeKeyName(key.getKey());
                            String keyAttribute = key.getValue().toString().split(":")[1].replaceAll(",}", "")
                                    .replaceAll("}", "").trim();
                            keys.setRangeKeyValue(keyAttribute);
                        }
                    }
                }
                scanResults.setExclusiveKeys(keys);
                while (item.hasNext()) {
                    Item next = item.next();
                    RecordResult recordResult = new RecordResult();
                    recordResult.setRecord(next);
                    results.add(next.toJSON());
                }

            }
        } catch (Exception e) {
            LOGGER.error("Unable to query from table {} due to:", table, e);
        }

        scanResults.setRecords(results);
        return scanResults;
    }

    public String getSecondaryIndexRangeKey(String table) {
        String results = "";
        Table dynamoDBTable = getTable(table);
        TableDescription tableDescription = dynamoDBTable.describe();

        List<LocalSecondaryIndexDescription> localSecondaryIndexes = tableDescription.getLocalSecondaryIndexes();
        Iterator<LocalSecondaryIndexDescription> lsiIter = localSecondaryIndexes.iterator();
        while (lsiIter.hasNext()) {

            LocalSecondaryIndexDescription lsiDescription = lsiIter.next();
            LOGGER.info("Info for index " + lsiDescription.getIndexName() + ":");
            Iterator<KeySchemaElement> kseIter = lsiDescription.getKeySchema().iterator();
            while (kseIter.hasNext()) {
                KeySchemaElement kse = kseIter.next();
                if (RANGE.equals(kse.getKeyType())) {
                    results = kse.getAttributeName();
                }
            }
        }
        return results;
    }

    private Table getTable(String table) {
        DynamoDB dynamoDB = new DatabaseConfiguration().amazonDynamoDB();
        return dynamoDB.getTable(table);
    }

    private List<String> searchBySecondaryIndex(String table, String hashKey, String hashKeyValue, String operator, String rangeKey, String rangeKeyValue, String operatorRangeKeyValue) {
        List<String> results = new ArrayList<>();
        Table dynamoDBTable = getTable(table);
        TableDescription tableDescription = dynamoDBTable.describe();

        List<LocalSecondaryIndexDescription> localSecondaryIndexes = tableDescription.getLocalSecondaryIndexes();

        for (LocalSecondaryIndexDescription localIndex : localSecondaryIndexes) {
            String indexName = localIndex.getIndexName();
            Index index = dynamoDBTable.getIndex(indexName);
            QuerySpec spec = new QuerySpec()
                    .withKeyConditionExpression(String.format("%s %s :%s and %s %s :%s", hashKey, Operators.from(operator), hashKey, rangeKey, Operators.from(operatorRangeKeyValue), rangeKey))
                    .withValueMap(new ValueMap()
                            .withString(":" + hashKey, hashKeyValue)
                            .withString(":" + rangeKey, rangeKeyValue));

            ItemCollection<QueryOutcome> items = index.query(spec);
            Iterator<Item> itemsIter = items.iterator();
            while (itemsIter.hasNext()) {
                results.add(itemsIter.next().toJSON());
            }
        }
        return results;
    }
}
