package com.vanius.change.exception;

import java.math.BigDecimal;

public class ChangeInvalidValue extends RuntimeException {
    BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    private static String formatMessage(BigDecimal value) {
        return "Invalid value to be requested to change " + value;
    }

    public ChangeInvalidValue(BigDecimal value) {
        super(formatMessage(value));
    }
}
