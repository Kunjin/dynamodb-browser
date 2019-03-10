package cb.dynamodb.browser.service;

import cb.dynamodb.browser.dao.SearchDao;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private SearchDao searchDao;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;


    public List<String> queryByHashKey(String table, String hashKey, String value, String operator) {
        return searchDao.searchByHashKey(table, hashKey, value, operator);
    }

    public List<String> queryByHashKeyAndRangeKey(String table, String hashKey, String value, String operator, String rangeKey, String rangeKeyValue, String operatorRangeKey) {
        return searchDao.searchByHashKeyAndRangeKey(table, hashKey, value, operator, rangeKey, rangeKeyValue, operatorRangeKey);
    }

    public String getHashKey(String table) {
        TableDescription tableSchema = amazonDynamoDB.describeTable(table).getTable();
        return tableSchema.getKeySchema().stream()
                .filter(x -> x.getKeyType().equals(KeyType.HASH.toString()))
                .findFirst().get().getAttributeName();
    }

    public List<String> getTableNames() {
        return amazonDynamoDB.listTables().getTableNames();
    }

    public String getRangeKey(String table) {
        TableDescription tableSchema = amazonDynamoDB.describeTable(table).getTable();
        return tableSchema.getKeySchema().stream()
                .filter(x -> x.getKeyType().equals(KeyType.RANGE.toString()))
                .findFirst().get().getAttributeName();
    }

    public List<String> queryAllByTable(String table) {
        return searchDao.searchAllByTable(table);
    }

    public String getSecondaryIndexRangeKey(String table) {
        return searchDao.getSecondaryIndexRangeKey(table);
    }
}
