package com.vanius.change.exception;

import java.math.BigDecimal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vanius.change.services.change.CoinsAmount;

public class ChangeNotEnoughException extends RuntimeException {
    BigDecimal value;

    CoinsAmount freeAmount;

    public BigDecimal getValue() {
        return value;
    }

    public CoinsAmount getFreeAmount() {
        return freeAmount;
    }

    private static String formatMessage(BigDecimal value, CoinsAmount freeAmount) {
        ObjectMapper objectMapper = new ObjectMapper();
        String freeAmountJson;
        try {
            freeAmountJson = objectMapper.writeValueAsString(freeAmount.getMap());
        } catch (JsonProcessingException e) {
            freeAmountJson = "";
        }
        return "Not enough amount for the value of " + value + ", free amount are " + freeAmountJson;
    }

    public ChangeNotEnoughException(BigDecimal value, CoinsAmount freeAmount) {
        super(formatMessage(value, freeAmount));
    }
}
