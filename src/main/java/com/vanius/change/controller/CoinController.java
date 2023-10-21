package com.vanius.change.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vanius.change.exception.ResourceNotFoundException;
import com.vanius.change.model.Coin;
import com.vanius.change.repository.CoinRepository;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Coin controller.
 */
@RestController
@RequestMapping("/api/v1")
public class CoinController {

    @Autowired
    private CoinRepository coinRepository;

    /**
     * Get all coins list.
     *
     * @return the list
     */
    @GetMapping("/coins")
    public List<Coin> getAllCoins() {
        return coinRepository.findAll();
    }

    /**
     * Gets coins by id.
     *
     * @param coinId the coin id
     * @return the coins by id
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping("/coins/{id}")
    public ResponseEntity<Coin> getCoinsById(@PathVariable(value = "id") BigDecimal coin)
            throws ResourceNotFoundException {
        Coin c = coinRepository
                .findByCoin(coin)
                .orElseThrow(() -> new ResourceNotFoundException("Coin not found on " + coin));
        return ResponseEntity.ok().body(c);
    }

    /**
     * Create coin.
     *
     * @param coin the coin
     * @return the coin
     */
    @PostMapping("/coins")
    public Coin createCoin(@Valid @RequestBody Coin coin) {
        return coinRepository.save(coin);
    }

    /**
     * Update coin response entity.
     *
     * @param coin        the coin id
     * @param coinDetails the coin details
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PutMapping("/coins/{id}")
    public ResponseEntity<Coin> updateCoin(
            @PathVariable(value = "id") BigDecimal coin, @Valid @RequestBody Coin coinDetails)
            throws ResourceNotFoundException {
        Coin c = coinRepository
                .findByCoin(coin)
                .orElseThrow(() -> new ResourceNotFoundException("Coin not found on " + coin));
        c.setAmount(coinDetails.getAmount());
        final Coin updatedCoin = coinRepository.save(c);
        return ResponseEntity.ok(updatedCoin);
    }

    /**
     * Delete coin map.
     *
     * @param coinId the coin id
     * @return the map
     * @throws Exception the exception
     */
    @DeleteMapping("/coin/{id}")
    public Map<String, Boolean> deleteCoin(@PathVariable(value = "id") BigDecimal coin) throws Exception {
        Coin c = coinRepository
                .findByCoin(coin)
                .orElseThrow(() -> new ResourceNotFoundException("Coin not found on " + coin));

        coinRepository.delete(c);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
