## A Spring Boot service that exposes a REST API that allows a user to request change for a given bill. ##

The service assume there are a finite number of coins. The change is made by utilizing the least amount of coins.

At first glance it appears to be a simple algorithm, just use the currency with the highest value first.

Example:

Amount of bill/coin available

```
U$ 50 | 10
U$ 20 | 10
U$ 10 | 10
U$  5 |  0
U$  1 | 20
```

the best result is: 1 * U$ 50 + 10 * U$ 10

However, there is a major complexity.

In the following condition of bill/coin available

```
U$ 50 | 10
U$ 20 | 10
U$ 10 |  0
U$  5 |  0
U$  1 | 20
```

the best result will be to avoid using US$50, with 3 * US $20

The algorithm uses a recursive function that tests an alternative path of possibility using the smaller coins.

## Running ##

To run the application just execute:

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.

## Rest APIs to CRUD of the coin

The app defines the following APIs.

    GET /api/v1/coins
    
    POST /api/v1/coins
    
    GET /api/v1/coins/{coinId}
    
    PUT /api/v1/coins/{coinId}
    
    DELETE /api/v1/coins/{coinId}

Example: execute a PUT command `http://localhost:8080/api/v1/coins/50.00` using the json content:

```json
  {
    "coin": 50.00,
    "amount": 0.00
  }
```

By default, the app starts with an amount of 100 for each coin.

### Executing the change

To request a change to a 60.00 execute a GET command `http://localhost:8080/api/v1/change/60.00`

The response will be:

```json
{
   "value":60.00,
   "returning":{
      "50.00":1,
      "10.00":1
   },
   "freeAmount":{
      "0.01":100,
      "20.00":100,
      "100.00":100,
      "50.00":99,
      "0.10":100,
      "0.25":100,
      "10.00":99,
      "2.00":100,
      "0.05":100,
      "5.00":100,
      "1.00":100
   }
}
```
### Customizing initial data

Create a file `coins.properties` in the directory of the application.

Should have a format like that:
```
100.00 = 200
10.00 = 200
1.00 = 200
5.00 = 200
```
