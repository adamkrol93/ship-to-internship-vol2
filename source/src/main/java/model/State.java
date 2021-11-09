package model;

public enum State {
    FREE("O"),
    TAKEN("X");

    private final String value;

    State(String val) {
        this.value = val;
    }

    public String getValue() {
        return value;
    }

    public static State valueOfLabel(String label) {
        for (State e : values()) {
            if (e.value.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
