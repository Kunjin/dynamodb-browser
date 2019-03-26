package cb.dynamodb.browser.dao;

import cb.dynamodb.browser.aws.DatabaseConfiguration;
import cb.dynamodb.browser.constants.Operators;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndexDescription;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class SearchDao {
    private static final String RANGE = "RANGE";

    private DynamoDB dynamoDB;

    private DatabaseConfiguration databaseConfiguration;

//    private AmazonDynamoDB amazonDynamoDbClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchDao.class);

    public SearchDao() {
        this.dynamoDB = new DatabaseConfiguration().amazonDynamoDB();
    }

    public List<String> searchByHashKey(String table, String hashKey, String value, String operator) {
        List<String> results = new ArrayList<>();
        Table dynamoDBTable = dynamoDB.getTable(table);

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
        }  catch (Exception e) {
            LOGGER.error("Unable to query from table {} where {} = {} due to: {}", table, hashKey, value, e);
        }
        return results;
    }

    public List<String> searchByHashKeyAndRangeKey(String table, String hashKey, String hashKeyValue, String operator, String rangeKey, String rangeKeyValue, String operatorRangeKeyValue) {
        List<String> results = new ArrayList<>();
        Table dynamoDBTable = dynamoDB.getTable(table);

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
        }  catch (AmazonDynamoDBException e) {
            try {
                return searchBySecondaryIndex(table, hashKey, hashKeyValue, operator, rangeKey, rangeKeyValue, operatorRangeKeyValue);
            } catch (Exception ex){
                LOGGER.error("Unable to query from table {} where {} = {} due to:", table, hashKey, hashKeyValue, ex);
            }
        }
        return results;
    }

    public List<String> searchAllByTable(String table) {

        List<String> results = new ArrayList<>();
        Table dynamoDBTable = dynamoDB.getTable(table);

        ScanSpec spec = new ScanSpec().withMaxResultSize(20);

        ItemCollection<ScanOutcome> items;
        Iterator<Item> iterator;
        try {
            items = dynamoDBTable.scan(spec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                results.add(iterator.next().toJSON());
            }
        }  catch (Exception e) {
            LOGGER.error("Unable to query from table {} due to:", table, e);
        }
        return results;
    }

    public String getSecondaryIndexRangeKey(String table) {
        String results = "";
        Table dynamoDBTable = dynamoDB.getTable(table);
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

    private List<String> searchBySecondaryIndex(String table, String hashKey, String hashKeyValue, String operator, String rangeKey, String rangeKeyValue, String operatorRangeKeyValue) {
        List<String> results = new ArrayList<>();
        Table dynamoDBTable = dynamoDB.getTable(table);
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
