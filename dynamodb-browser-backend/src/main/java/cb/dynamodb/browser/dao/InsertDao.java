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
import java.util.stream.Collectors;

@Repository
public class InsertDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsertDao.class);


    public PutItemOutcome insert(ItemDto itemDto) {
        Table table = getTable(itemDto.getTable());

        Item item = new Item();
        Map<String, AttributeDto> primaryKeyAttributesMap = new HashMap<>();
        AttributeDto hashKeyAttribute = getPrimaryKetAttribute(itemDto, "hash_key");
        AttributeDto rangeKeyAttribute = getPrimaryKetAttribute(itemDto, "range_key");

        buildPrimaryKeys(item, primaryKeyAttributesMap, hashKeyAttribute, rangeKeyAttribute);
        buildAttributes(itemDto, item);

        return table.putItem(item);
    }

    private void buildAttributes(ItemDto itemDto, Item item) {
        List<AttributeDto> attributeDtoList = itemDto.getAttributeDtoList().stream()
                .filter(p -> "attribute".equals(p.getKey())).collect(Collectors.toList());

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
        primaryKeyAttributesMap.put("hash_key", hashKeyAttribute);

        if (rangeKeyAttribute != null) {
            primaryKeyAttributesMap.put("range_key", rangeKeyAttribute);
        }

        if (!primaryKeyAttributesMap.containsKey("range_key")) {
            item.withPrimaryKey(primaryKeyAttributesMap.get("hash_key").getAttributeName(),
                    primaryKeyAttributesMap.get("hash_key").getValue());
        } else {
            item.withPrimaryKey(primaryKeyAttributesMap.get("hash_key").getAttributeName(),
                    primaryKeyAttributesMap.get("hash_key").getValue(),
                    primaryKeyAttributesMap.get("range_key").getAttributeName(),
                    primaryKeyAttributesMap.get("range_key").getValue());
        }
    }

    private AttributeDto getPrimaryKetAttribute(ItemDto itemDto, String hash_key) {
        return itemDto.getAttributeDtoList().stream()
                .filter(p -> hash_key.equals(p.getKey())).findFirst().orElse(null);
    }


    private Table getTable(String table) {
        DynamoDB dynamoDB = new DatabaseConfiguration().amazonDynamoDB();
        return dynamoDB.getTable(table);
    }
}
