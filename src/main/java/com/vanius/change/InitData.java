package com.vanius.change;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.vanius.change.model.Coin;
import com.vanius.change.repository.CoinRepository;

/**
 * The type InitData, that initialize records if exists the file
 * coins.properties.
 */
@Component
public class InitData {

    @Autowired
    private CoinRepository coinRepository;

    @PostConstruct
    private void postConstruct() throws IOException {
        var coinsProperties = "coins.properties";
        if (!new File(coinsProperties).exists()) {
            return;
        }
        FileReader reader = new FileReader("coins.properties");

        Properties properties = new Properties();
        properties.load(reader);

        for (Coin c : coinRepository.findAll()) {
            coinRepository.delete(c);
        }

        var enuKeys = properties.keys();
        while (enuKeys.hasMoreElements()) {
            String coin = (String) enuKeys.nextElement();
            String amount = properties.getProperty(coin);
            var c = new Coin(new BigDecimal(coin), Integer.parseInt(amount));
            coinRepository.save(c);
        }
    }
}
