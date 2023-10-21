package com.vanius.change.services.change;

import java.math.BigDecimal;
import java.util.Optional;

import com.vanius.change.exception.ChangeInvalidValue;
import com.vanius.change.exception.ChangeNotEnoughException;
import com.vanius.change.model.Coin;

/**
 * The type Change. The main logic of the application.
 * Try to execute the change, test for an alternative path of possibility using
 * the smaller coins.
 */
public class Change {

    public static CoinsAmount change(BigDecimal value, CoinsAmount coinsFree, Optional<Integer> max)
            throws ChangeNotEnoughException {

        // Verify if the value is valid
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ChangeInvalidValue(value);
        }

        CoinsWalker coinsWalker = new CoinsWalker(coinsFree);
        var coin = coinsWalker.nextCoin().get();
        CoinsAmount coinsReturning = new CoinsAmount();
        var remainder = processCoin(coin, value, coinsWalker, coinsReturning, false);

        // Verify if the change was fully executed
        if (remainder.compareTo(BigDecimal.ZERO) != 0) {
            throw new ChangeNotEnoughException(value, coinsFree);
        }

        // Verify if the amount is higher than max limit
        if (max.isPresent() && max.get() > coinsReturning.amountCoins()) {
            throw new ChangeNotEnoughException(value, coinsFree);
        }

        coinsFree.assignFrom(coinsWalker.toCoinsAmount());

        return coinsReturning;
    }

    private static BigDecimal processCoin(Coin coinObj, BigDecimal value, CoinsWalker coinsFree,
            CoinsAmount coinsReturning,
            boolean alternative) {
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return value;
        }

        var coin = coinObj.getCoin();
        var amount = BigDecimal.valueOf(coinObj.getAmount());

        // If coin is greater than the value go to next coin
        if (coin.compareTo(value) > 0) {
            var coinOpt = coinsFree.nextCoin();
            if (coinOpt.isEmpty()) {
                return value;
            }
            return processCoin(coinOpt.get(), value, coinsFree, coinsReturning, alternative);
        }

        var auxCoinObj = coinObj.clone();
        var auxCoinsFree = coinsFree.clone();
        var auxCoinsReturning = coinsReturning.clone();

        var missing = value.divideToIntegralValue(coin);

        // If should use alternative, using less amount of this kind of coin
        if (alternative && missing.compareTo(BigDecimal.ZERO) > 0) {
            missing = missing.subtract(BigDecimal.ONE);
        }

        var divide = missing.min(amount);
        var reducer = divide.multiply(coin);
        var newValue = value.subtract(reducer);
        coinsReturning.setAmount(coin, divide.intValue());

        var newAmount = amount.subtract(divide);

        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new Error("Unexpected negative new amount");
        }

        coinObj.setAmount(newAmount.intValue());

        var coinOpt = coinsFree.nextCoin();
        if (coinOpt.isEmpty()) {
            return newValue;
        }

        newValue = processCoin(coinOpt.get(), newValue, coinsFree, coinsReturning, false);

        if (!alternative) {
            // Execute alternative path
            var alternativeValue = processCoin(auxCoinObj, value, auxCoinsFree,
                    auxCoinsReturning,
                    true);
            // If the alternative path has less coins usage
            if ((alternativeValue.compareTo(BigDecimal.ZERO) == 0)
                    && (auxCoinsReturning.amountCoins() < coinsReturning.amountCoins())) {
                coinsReturning.assignFrom(auxCoinsReturning);
                coinsFree.assignFrom(auxCoinsFree);
                newValue = alternativeValue;
            }
        }

        return newValue;
    }

}
