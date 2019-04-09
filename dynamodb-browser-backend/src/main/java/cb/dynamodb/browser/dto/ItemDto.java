package cb.dynamodb.browser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ItemDto {

    @JsonProperty("table_name")
    private String table;

    @JsonProperty("attributes")
    private List<AttributeDto> attributeDtoList;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<AttributeDto> getAttributeDtoList() {
        return attributeDtoList;
    }

    public void setAttributeDtoList(List<AttributeDto> attributeDtoList) {
        this.attributeDtoList = attributeDtoList;
    }
}
