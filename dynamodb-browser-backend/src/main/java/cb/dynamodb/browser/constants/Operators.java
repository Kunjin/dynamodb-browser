package cb.dynamodb.browser.constants;

public enum Operators {
    EQUALS("="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUALS("<="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUALS(">="),
    BEGINS_WITH("begins_with");

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
            }
        }
        return result;
    }
}
