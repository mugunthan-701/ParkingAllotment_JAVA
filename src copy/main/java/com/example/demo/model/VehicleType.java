package com.example.demo.model;

import java.math.BigDecimal;

public enum VehicleType {
    FOUR_WHEELER(new BigDecimal("30.0")),
    TWO_WHEELER(new BigDecimal("20.0"));

    private final BigDecimal rate;

    VehicleType(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
