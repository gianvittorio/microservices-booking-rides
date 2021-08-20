package com.gianvittorio.orderservice.domain;

public enum OrderStatus {
    PENDING("pending"),
    CANCELED("canceled"),
    SUCCESSFUL("SUCCESSFUL");

    private final String status;

    OrderStatus(final String status) {
        this.status = status;
    }

    public String value() {
        return this.status;
    }


    @Override
    public String toString() {
        return this.value();
    }
}
