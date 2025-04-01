package org.example.petstore.clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static java.lang.Thread.sleep;

import lombok.SneakyThrows;
import org.example.petstore.config.ConfigManager;
import org.example.petstore.data.Order;

public class StoreClient {

    private final String baseUrl;
    //private final String token; - once needed, we can either get from properties, or write custom retrieve in Manager

    public StoreClient() {
        ConfigManager configManager = ConfigManager.getInstance();
        this.baseUrl = configManager.getBaseUrl();
        RestAssured.baseURI = this.baseUrl;
    }

    public Response getInventory() {
        return given()
                .get("/store/inventory");
    }

    public Response createOrder(Order order) {
        return given()
                .contentType("application/json")
                .body(order)
                .post("/store/order");
    }

    public Response getOrderById(Long orderId) {
        return given()
                .get("/store/order/" + orderId);
    }

    @SneakyThrows
    public Response deleteOrderById(Long orderId) {
        sleep(5000); // We can add fluent waiters, of course, it is a piece of work. This endpoint unstable,
        // require time for entity to appear in DB
        return given()
                .delete("/store/order/" + orderId);
    }
}
