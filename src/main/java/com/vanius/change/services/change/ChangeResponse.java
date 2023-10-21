package com.vanius.change.services.change;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * The type Change Response, to return details of the change executed.
 */
public class ChangeResponse {
    BigDecimal value;
    HashMap<BigDecimal, Integer> returning;
    HashMap<BigDecimal, Integer> freeAmount;

    public ChangeResponse(BigDecimal value, HashMap<BigDecimal, Integer> returning,
            HashMap<BigDecimal, Integer> freeAmount) {
        this.value = value;
        this.returning = returning;
        this.freeAmount = freeAmount;
    }

    public BigDecimal getValue() {
        return value;
    }

    public HashMap<BigDecimal, Integer> getReturning() {
        return returning;
    }

    public HashMap<BigDecimal, Integer> getFreeAmount() {
        return freeAmount;
    }
}
