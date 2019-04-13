package cb.dynamodb.browser.dao;

import cb.dynamodb.browser.dto.ExclusiveKeys;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import org.springframework.stereotype.Repository;

@Repository
public class DeleteDao {

    public void deleteRecord(String table, ExclusiveKeys exclusiveKeys) {
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey(exclusiveKeys.getHashKeyName(), exclusiveKeys.getHashKeyValue(),
                        exclusiveKeys.getRangeKeyName(), exclusiveKeys.getRangeKeyValue()));
    }
}
