package cb.dynamodb.browser.dto;

public class ExclusiveKeys {
    private String hashKeyName;
    private Object hashKeyValue;
    private String rangeKeyName;
    private Object rangeKeyValue;

    public ExclusiveKeys() { }

    public ExclusiveKeys(String hashKeyName, Object hashKeyValue, String rangeKeyName, Object rangeKeyValue) {
        this.hashKeyName = hashKeyName;
        this.hashKeyValue = hashKeyValue;
        this.rangeKeyName = rangeKeyName;
        this.rangeKeyValue = rangeKeyValue;
    }

    public String getHashKeyName() {
        return hashKeyName;
    }

    public void setHashKeyName(String hashKeyName) {
        this.hashKeyName = hashKeyName;
    }

    public Object getHashKeyValue() {
        return hashKeyValue;
    }

    public void setHashKeyValue(Object hashKeyValue) {
        this.hashKeyValue = hashKeyValue;
    }

    public String getRangeKeyName() {
        return rangeKeyName;
    }

    public void setRangeKeyName(String rangeKeyName) {
        this.rangeKeyName = rangeKeyName;
    }

    public Object getRangeKeyValue() {
        return rangeKeyValue;
    }

    public void setRangeKeyValue(Object rangeKeyValue) {
        this.rangeKeyValue = rangeKeyValue;
    }

    @Override
    public String toString() {
        return "ExclusiveKeys{" +
                "hashKeyName='" + hashKeyName + '\'' +
                ", hashKeyValue=" + hashKeyValue +
                ", rangeKeyName='" + rangeKeyName + '\'' +
                ", rangeKeyValue=" + rangeKeyValue +
                '}';
    }
}
