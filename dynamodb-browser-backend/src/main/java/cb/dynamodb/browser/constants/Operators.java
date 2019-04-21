package cb.dynamodb.browser.constants;

public enum Operators {
    EQUALS("=");

    private String operator;

    Operators(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public static String from(String value) {
        String result = null;
        for (Operators operator: Operators.values()) {
            if (value.equalsIgnoreCase(operator.name())) {
                result = operator.getOperator();
                break;
            }
        }
        return result;
    }
}
