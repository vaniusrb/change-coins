package com.vanius.change.services.change;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import com.vanius.change.model.Coin;

/**
 * The type Coins Walker, to navigate through a coin list.
 */
public class CoinsWalker {
    int index = 0;
    ArrayList<Coin> listCoins;

    public CoinsWalker(CoinsAmount coinsAmount) {
        listCoins = new ArrayList<>();
        for (var entry : coinsAmount.getMap().entrySet()) {
            var coin = new Coin(entry.getKey(), entry.getValue().intValue());
            listCoins.add(coin);
        }
        new CoinsWalker(listCoins);
    }

    public CoinsWalker(ArrayList<Coin> coinsAmount) {
        this.listCoins = coinsAmount;
        this.listCoins.sort(Comparator.reverseOrder());
    }

    public CoinsWalker(ArrayList<Coin> coinsAmount, int index) {
        this.listCoins = coinsAmount;
        this.index = index;
        new CoinsWalker(listCoins);
    }

    public Optional<Coin> nextCoin() {
        if (index >= listCoins.size()) {
            return Optional.empty();
        }
        return Optional.of(listCoins.get(index++));
    }

    @Override
    public CoinsWalker clone() {
        var newList = new ArrayList<Coin>();
        for (Coin c : listCoins) {
            newList.add(c.clone());
        }
        return new CoinsWalker(newList, index);
    }

    public void assignFrom(CoinsWalker source) {
        this.listCoins = source.listCoins;
    }

    public CoinsAmount toCoinsAmount() {
        return new CoinsAmount(listCoins);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CoinsWalker))
            return false;
        CoinsWalker other = (CoinsWalker) o;
        return this.listCoins.equals(other.listCoins);
    }
}
