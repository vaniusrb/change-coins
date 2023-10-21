package com.vanius.change;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.vanius.change.model.Coin;
import com.vanius.change.services.change.ChangeResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChangeTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    private void updateCoin(BigDecimal coin, int amount) {
        Coin c = new Coin(coin, amount);
        restTemplate.put(getRootUrl() + "/coins/" + c.getCoin(), c);
    }

    @Test
    public void testChange60_20_3() {
        updateCoin(BigDecimal.valueOf(50), 0);
        updateCoin(BigDecimal.valueOf(20), 3);
        updateCoin(BigDecimal.valueOf(10), 0);

        ChangeResponse change = restTemplate.getForObject(getRootUrl() + "/change/60",
                ChangeResponse.class);
        Assert.assertNotNull(change);

        HashMap<BigDecimal, Integer> map = new HashMap<>();
        map.put(new BigDecimal("20.00"), 3);
        assertEquals(map, change.getReturning());
    }

    @Test
    public void testChange60_50_1_10_1() {
        updateCoin(BigDecimal.valueOf(50), 3);
        updateCoin(BigDecimal.valueOf(20), 3);
        updateCoin(BigDecimal.valueOf(10), 3);

        ChangeResponse change = restTemplate.getForObject(getRootUrl() + "/change/60",
                ChangeResponse.class);
        Assert.assertNotNull(change);

        HashMap<BigDecimal, Integer> map = new HashMap<>();
        map.put(new BigDecimal("50.00"), 1);
        map.put(new BigDecimal("10.00"), 1);
        assertEquals(map, change.getReturning());
    }

    @Test
    public void testChange60_50_0_20_3() {
        updateCoin(BigDecimal.valueOf(50), 3);
        updateCoin(BigDecimal.valueOf(20), 3);
        updateCoin(BigDecimal.valueOf(10), 0);
        updateCoin(BigDecimal.valueOf(5), 0);
        updateCoin(BigDecimal.valueOf(1), 10);

        ChangeResponse change = restTemplate.getForObject(getRootUrl() + "/change/60",
                ChangeResponse.class);
        Assert.assertNotNull(change);

        HashMap<BigDecimal, Integer> map = new HashMap<>();
        map.put(new BigDecimal("20.00"), 3);
        assertEquals(map, change.getReturning());
    }

    @Test
    public void testChange160_50_2_20_3() {
        updateCoin(BigDecimal.valueOf(100), 0);
        updateCoin(BigDecimal.valueOf(50), 3);
        updateCoin(BigDecimal.valueOf(20), 3);
        updateCoin(BigDecimal.valueOf(10), 0);
        updateCoin(BigDecimal.valueOf(5), 0);
        updateCoin(BigDecimal.valueOf(1), 10);

        ChangeResponse change = restTemplate.getForObject(getRootUrl() + "/change/160",
                ChangeResponse.class);
        Assert.assertNotNull(change);

        HashMap<BigDecimal, Integer> map = new HashMap<>();
        map.put(new BigDecimal("50.00"), 2);
        map.put(new BigDecimal("20.00"), 3);
        assertEquals(map, change.getReturning());
    }

    @Test
    public void testChange160_100_1_20_3() {
        updateCoin(BigDecimal.valueOf(100), 1);
        updateCoin(BigDecimal.valueOf(50), 3);
        updateCoin(BigDecimal.valueOf(20), 3);
        updateCoin(BigDecimal.valueOf(10), 0);
        updateCoin(BigDecimal.valueOf(5), 0);
        updateCoin(BigDecimal.valueOf(1), 10);

        ChangeResponse change = restTemplate.getForObject(getRootUrl() + "/change/160",
                ChangeResponse.class);
        Assert.assertNotNull(change);

        HashMap<BigDecimal, Integer> map = new HashMap<>();
        map.put(new BigDecimal("100.00"), 1);
        map.put(new BigDecimal("20.00"), 3);
        assertEquals(map, change.getReturning());
    }

    @Test
    public void testChangeValueNotEnough() {
        updateCoin(BigDecimal.valueOf(100), 0);
        updateCoin(BigDecimal.valueOf(50), 0);
        updateCoin(BigDecimal.valueOf(20), 2);
        updateCoin(BigDecimal.valueOf(10), 0);
        updateCoin(BigDecimal.valueOf(5), 0);
        updateCoin(BigDecimal.valueOf(2), 0);
        updateCoin(BigDecimal.valueOf(1), 0);

        ResponseEntity<ChangeResponse> result = restTemplate.exchange(getRootUrl() + "/change/500", HttpMethod.GET,
                null,
                ChangeResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testChangeValueZero() {
        ResponseEntity<ChangeResponse> result = restTemplate.exchange(getRootUrl() + "/change/0", HttpMethod.GET,
                null,
                ChangeResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testChangeValueNegative() {
        ResponseEntity<ChangeResponse> result = restTemplate.exchange(getRootUrl() + "/change/-50.00", HttpMethod.GET,
                null,
                ChangeResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}