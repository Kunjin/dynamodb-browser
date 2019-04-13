package cb.dynamodb.browser.service;

import cb.dynamodb.browser.dao.TransactionalDao;
import cb.dynamodb.browser.dto.ExclusiveKeys;
import cb.dynamodb.browser.dto.ItemDto;
import cb.dynamodb.browser.dto.RecordResult;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionalService {

    @Autowired
    private TransactionalDao transactionalDao;

    public PutItemOutcome insert(ItemDto itemDto) {
        return transactionalDao.insert(itemDto);
    }

    public DeleteItemOutcome delete(ItemDto itemDto) {
        return transactionalDao.delete(itemDto);
    }

    public RecordResult read(String table, ExclusiveKeys exclusiveKeys) {
        return transactionalDao.read(table, exclusiveKeys);
    }
}
