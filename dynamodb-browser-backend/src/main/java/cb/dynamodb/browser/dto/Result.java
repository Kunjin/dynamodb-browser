package cb.dynamodb.browser.dto;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.fasterxml.jackson.annotation.JsonRawValue;

public class Result {

    @JsonRawValue
    private Item record;

    public Item getRecord() {
        return record;
    }

    public void setRecord(Item record) {
        this.record = record;
    }
}
