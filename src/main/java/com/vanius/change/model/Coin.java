package com.vanius.change.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

/**
 * The type Coin.
 */
@Entity
@Table(name = "coins")
@EntityListeners(AuditingEntityListener.class)
public class Coin implements Comparable<Coin> {

    public Coin() {
    }

    public Coin(@Digits(integer = 3, fraction = 2) BigDecimal coin, int amount) {
        this.coin = coin;
        this.amount = amount;
    }

    public Coin clone() {
        return new Coin(this.coin, this.amount);
    }

    @Id
    @Digits(integer = 3, fraction = 2)
    @Column(name = "coin", nullable = false)
    private BigDecimal coin;

    @Column(name = "amount", nullable = false)
    private int amount;

    public BigDecimal getCoin() {
        return coin;
    }

    public void setCoin(BigDecimal coin) {
        this.coin = coin;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "coin=" + coin +
                ", amount='" + amount +
                '}';
    }

    @Override
    public int compareTo(Coin arg0) {
        return this.getCoin().compareTo(arg0.getCoin());
    }

}
