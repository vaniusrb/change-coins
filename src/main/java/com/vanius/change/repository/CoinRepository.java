package com.vanius.change.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import com.vanius.change.model.Coin;

/**
 * The interface Coin repository.
 */
@Repository
public interface CoinRepository extends JpaRepository<Coin, Long> {

    Optional<Coin> findByCoin(BigDecimal coin);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Coin> findAll();
}
