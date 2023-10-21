package com.vanius.change;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import com.vanius.change.model.Coin;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CoinsTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testGetAllCoins() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() +
                "/coins",
                HttpMethod.GET, entity, String.class);

        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void testGetCoinById() {
        Coin coin = restTemplate.getForObject(getRootUrl() + "/coins/1", Coin.class);
        Assert.assertNotNull(coin);
    }

    @Test
    public void testCreateCoin() {
        Coin coin = new Coin();
        coin.setAmount(10);
        ResponseEntity<Coin> postResponse = restTemplate.postForEntity(getRootUrl() +
                "/coins", coin, Coin.class);
        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
    }

    @Test
    public void testUpdatePost() {
        int id = 1;
        Coin coin = restTemplate.getForObject(getRootUrl() + "/coins/" + id,
                Coin.class);
        coin.setAmount(20);

        restTemplate.put(getRootUrl() + "/coins/" + id, coin);

        Coin updatedCoin = restTemplate.getForObject(getRootUrl() + "/coins/" + id,
                Coin.class);
        Assert.assertNotNull(updatedCoin);
    }

    @Test
    public void testDeletePost() {
        int id = 2;
        Coin coin = restTemplate.getForObject(getRootUrl() + "/coins/" + id,
                Coin.class);
        Assert.assertNotNull(coin);
        restTemplate.delete(getRootUrl() + "/coins/" + id);
        try {
            coin = restTemplate.getForObject(getRootUrl() + "/coins/" + id, Coin.class);
        } catch (final HttpClientErrorException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }

}
