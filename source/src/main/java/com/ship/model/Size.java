package com.ship.model;

public enum Size {
    S(2),
    M(4),
    L(8);
    public static final int MAX_POSSIBLE_FIT = L.value / S.value;
    private final int value;

    Size(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }
}
