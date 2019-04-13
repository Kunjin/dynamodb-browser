package cb.dynamodb.browser.dao;

import cb.dynamodb.browser.aws.DatabaseConfiguration;
import cb.dynamodb.browser.dto.AttributeDto;
import cb.dynamodb.browser.dto.ItemDto;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InsertDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsertDao.class);
    public static final String HASH_KEY = "hash_key";
    public static final String RANGE_KEY = "range_key";


    public PutItemOutcome insert(ItemDto itemDto) {
        Table table = getTable(itemDto.getTable());

        Item item = new Item();
        Map<String, AttributeDto> primaryKeyAttributesMap = new HashMap<>();
        AttributeDto hashKeyAttribute = getPrimaryKeyAttribute(itemDto, HASH_KEY);
        AttributeDto rangeKeyAttribute = getPrimaryKeyAttribute(itemDto, RANGE_KEY);

        buildPrimaryKeys(item, primaryKeyAttributesMap, hashKeyAttribute, rangeKeyAttribute);
        buildAttributes(itemDto, item);

        return table.putItem(item);
    }

    private void buildAttributes(ItemDto itemDto, Item item) {
        List<AttributeDto> attributeDtoList = itemDto.getAttributeDtoList();

        for (AttributeDto attribute : attributeDtoList) {
            if ("string".equals(attribute.getDataType())) {
                item.withString(attribute.getAttributeName(), attribute.getValue());
            } else if ("boolean".equals(attribute.getDataType())) {
                item.withBoolean(attribute.getAttributeName(), Boolean.parseBoolean(attribute.getValue()));
            } else if ("number".equals(attribute.getDataType())) {
                item.withNumber(attribute.getAttributeName(), Integer.parseInt(attribute.getValue()));
            }
        }
    }

    private void buildPrimaryKeys(Item item, Map<String, AttributeDto> primaryKeyAttributesMap, AttributeDto hashKeyAttribute, AttributeDto rangeKeyAttribute) {
        primaryKeyAttributesMap.put(HASH_KEY, hashKeyAttribute);

        if (rangeKeyAttribute != null) {
            primaryKeyAttributesMap.put(RANGE_KEY, rangeKeyAttribute);
        }

        if ((primaryKeyAttributesMap.containsKey(RANGE_KEY) &&
                primaryKeyAttributesMap.get(RANGE_KEY).getAttributeName() == null) ||
                !primaryKeyAttributesMap.containsKey(RANGE_KEY)) {
            item.withPrimaryKey(primaryKeyAttributesMap.get(HASH_KEY).getAttributeName(),
                    primaryKeyAttributesMap.get(HASH_KEY).getValue());
        } else {
            item.withPrimaryKey(primaryKeyAttributesMap.get(HASH_KEY).getAttributeName(),
                    primaryKeyAttributesMap.get(HASH_KEY).getValue(),
                    primaryKeyAttributesMap.get(RANGE_KEY).getAttributeName(),
                    primaryKeyAttributesMap.get(RANGE_KEY).getValue());
        }
    }

    private AttributeDto getPrimaryKeyAttribute(ItemDto itemDto, String key) {
        if (HASH_KEY.equals(key)) {
            return itemDto.getHashKey();
        }

        return itemDto.getRangeKey();
    }


    private Table getTable(String table) {
        DynamoDB dynamoDB = new DatabaseConfiguration().amazonDynamoDB();
        return dynamoDB.getTable(table);
    }
}
