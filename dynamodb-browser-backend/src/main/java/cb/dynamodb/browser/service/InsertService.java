package cb.dynamodb.browser.service;

import cb.dynamodb.browser.dao.InsertDao;
import cb.dynamodb.browser.dto.ItemDto;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsertService {

    @Autowired
    private InsertDao insertDao;

    public PutItemOutcome insert(ItemDto itemDto) {
        return insertDao.insert(itemDto);
    }
}
