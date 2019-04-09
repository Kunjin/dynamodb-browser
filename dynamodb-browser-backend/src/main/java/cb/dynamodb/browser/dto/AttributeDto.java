package cb.dynamodb.browser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttributeDto {

    @JsonProperty("attribute_name")
    private String attributeName;

    @JsonProperty("data_type")
    private String dataType;

    @JsonProperty("attribute_value")
    private String value;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AttributeDto{" +
                "attributeName='" + attributeName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
