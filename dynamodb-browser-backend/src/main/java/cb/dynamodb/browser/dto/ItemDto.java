package cb.dynamodb.browser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

@JsonTypeName("item")
@JsonTypeInfo(include= JsonTypeInfo.As.WRAPPER_OBJECT,use= JsonTypeInfo.Id.NAME)
public class ItemDto {

    @JsonProperty("table_name")
    private String table;

    @JsonProperty("hash_key")
    private AttributeDto hashKey;

    @JsonProperty("range_key")
    private AttributeDto rangeKey;

    @JsonProperty("attributes")
    private List<AttributeDto> attributeDtoList;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public AttributeDto getHashKey() {
        return hashKey;
    }

    public void setHashKey(AttributeDto hashKey) {
        this.hashKey = hashKey;
    }

    public AttributeDto getRangeKey() {
        return rangeKey;
    }

    public void setRangeKey(AttributeDto rangeKey) {
        this.rangeKey = rangeKey;
    }

    public List<AttributeDto> getAttributeDtoList() {
        return attributeDtoList;
    }

    public void setAttributeDtoList(List<AttributeDto> attributeDtoList) {
        this.attributeDtoList = attributeDtoList;
    }
}
