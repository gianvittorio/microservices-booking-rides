package com.gianvittorio.common.domain;

public enum Category {
    STANDARD("standard"),
    COMFORT("comfort"),
    LUXURY("luxury");

    private final String category;

    Category(final String category) {
        this.category = category;
    }

    public String value() {
        return this.category;
    }

    @Override
    public String toString() {
        return this.value();
    }
}
