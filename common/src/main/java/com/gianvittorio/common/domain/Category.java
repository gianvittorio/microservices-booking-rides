package com.gianvittorio.common.domain;

public enum Category {
    STANDARD("standard"),
    COMFORT("comfort");


    private String name;

    Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
