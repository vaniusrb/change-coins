package com.vanius.change.services.change;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import com.vanius.change.model.Coin;

/**
 * The type Coins Amount, to store coins with their amounts.
 * Could be used to store the free amount and the amount to return (change).
 */
public class CoinsAmount {
    HashMap<BigDecimal, Integer> returningCoins;

    public HashMap<BigDecimal, Integer> getMap() {
        return returningCoins;
    }

    public HashMap<BigDecimal, Integer> getMapWithoutZeros() {
        returningCoins.entrySet().removeIf(map -> map.getValue().equals(0));
        return returningCoins;
    }

    public ArrayList<Coin> getList() {
        ArrayList<Coin> list = new ArrayList<>();
        for (var entry : getMap().entrySet()) {
            Coin coin = new Coin(entry.getKey(), entry.getValue());
            list.add(coin);
        }
        return list;
    }

    public CoinsAmount() {
        this.returningCoins = new HashMap<BigDecimal, Integer>();
    }

    public CoinsAmount(ArrayList<Coin> coins) {
        returningCoins = new HashMap<BigDecimal, Integer>();
        for (Coin c : coins) {
            returningCoins.put(c.getCoin(), c.getAmount());
        }
    }

    public CoinsAmount(HashMap<BigDecimal, Integer> returningCoins) {
        this.returningCoins = returningCoins;
    }

    public void setAmount(BigDecimal coin, Integer amount) {
        returningCoins.put(coin, amount);
    }

    public int amountCoins() {
        int amount = 0;
        for (var entry : returningCoins.entrySet()) {
            amount += entry.getValue().intValue();
        }
        return amount;
    }

    @Override
    public CoinsAmount clone() {
        var newMap = new HashMap<BigDecimal, Integer>();
        for (var entry : returningCoins.entrySet()) {
            newMap.put(entry.getKey(), entry.getValue());
        }
        return new CoinsAmount(newMap);
    }

    public void assignFrom(CoinsAmount source) {
        this.returningCoins = source.returningCoins;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CoinsAmount))
            return false;
        CoinsAmount other = (CoinsAmount) o;

        if (this.returningCoins.size() != other.returningCoins.size()) {
            return false;
        }

        return this.returningCoins.entrySet().stream()
                .allMatch(e -> e.getValue().equals(other.returningCoins.get(e.getKey())));
    }
}
