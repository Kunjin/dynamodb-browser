package cb.dynamodb.browser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class ScanResults {
    @JsonProperty
    private ExclusiveKeys exclusiveKeys;

    @JsonProperty
    private List<String> records;

    public ExclusiveKeys getExclusiveKeys() {
        return exclusiveKeys;
    }


    public void setExclusiveKeys(ExclusiveKeys exclusiveKeys) {
        this.exclusiveKeys = exclusiveKeys;
    }

    public List<String> getRecords() {
        return records;
    }

    public void setRecords(List<String> records) {
        this.records = records;
    }
}
