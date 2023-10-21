package com.vanius.change.services.change;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.vanius.change.exception.ChangeNotEnoughException;
import com.vanius.change.model.Coin;
import com.vanius.change.repository.CoinRepository;

/**
 * The type Change service.
 * It read from data base, execute the change and update the free amount of the
 * coins.
 */
@Service
public class ChangeService {

    @Autowired
    private CoinRepository coinRepository;

    public ChangeResponse change(BigDecimal value, Optional<Integer> max) throws ChangeNotEnoughException {

        // Read coins from database
        HashMap<BigDecimal, Integer> freeMap = new HashMap<>();
        for (Coin coin : coinRepository.findAll(Sort.by(Sort.Direction.DESC, "coin"))) {
            freeMap.put(coin.getCoin(), coin.getAmount());
        }

        CoinsAmount coinsFree = new CoinsAmount(freeMap);
        CoinsAmount coinsReturning = Change.change(value, coinsFree, max);
        ChangeResponse changeResponse = new ChangeResponse(value, coinsReturning.getMapWithoutZeros(),
                coinsFree.getMap());

        // Update free amount of coins
        for (var coin : coinsFree.getList()) {
            coinRepository.save(coin);
        }
        return changeResponse;
    }
}
