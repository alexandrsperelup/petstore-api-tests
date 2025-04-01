package org.example.petstore.tests;

import org.example.petstore.builders.OrderBuilder;
import org.example.petstore.clients.StoreClient;
import org.example.petstore.data.Order;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.example.petstore.hooks.TestListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Listeners({TestListener.class})
@Epic("Petstore API")
@Feature("Store")
@Story("Store Orders API Tests")
@Owner("qa-team")
@Severity(SeverityLevel.NORMAL)
public class StoreOrderTests {

    private final StoreClient storeClient = new StoreClient();

    @Feature("Store")
    @Story("Order API Tests")
    @Test(description = "Create a new order and verify the response")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateOrderSuccess() {
        Order order = OrderBuilder.buildDefaultOrder();

        Response createResponse = storeClient.createOrder(order);
        createResponse.then().log().all()
                .statusCode(200)
                .body("id", equalTo(order.getId().intValue()))
                .body("petId", equalTo(order.getPetId().intValue()))
                .body("status", equalTo(order.getStatus()));

        // Retrieve the order and validate
        Response getResponse = storeClient.getOrderById(order.getId());
        getResponse.then().log().all()
                .statusCode(200)
                .body("id", equalTo(order.getId().intValue()))
                .body("petId", equalTo(order.getPetId().intValue()))
                .body("status", equalTo(order.getStatus()));
    }

    @Feature("Store")
    @Story("Order API Tests")
    @Test(description="POST /store/order with extremely large quantity - Success")
    public void testCreateOrderExcessiveQuantity() {
        Map<String, Object> order = new HashMap<>();
        order.put("id", (int) (Math.random() * 1_000_000) + 20_000);
        order.put("petId", (int) (Math.random() * 1_000_000) + 1);
        order.put("quantity", 10_000_000);
        order.put("status", "placed");

        given()
                .contentType("application/json")
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200)
                .body("status", equalTo("placed"));
    }

    @Feature("Store")
    @Story("Order API Tests")
    @Test(description="POST /store/order without content-type header returns 415 or 400.")
    public void testCreateOrderWithoutContentType() {
        Map<String, Object> order = new HashMap<>();
        order.put("id", (int) (Math.random() * 1_000_000));
        order.put("petId", (int) (Math.random() * 1_000_000) + 1);
        order.put("quantity", 2);

        given()
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(anyOf(equalTo(400), equalTo(415)));
    }

    @Feature("Store")
    @Story("Order API Tests")
    @Test(description="GET /store/order/{orderId} with method PUT returns 405 (Method Not Allowed).")
    public void testGetOrderWithPutMethod() {
        given()
                .when()
                .put("/store/order/{orderId}", 1)
                .then()
                .statusCode(405);
    }

    @Feature("Store")
    @Story("Order API Tests")
    @Test(description="POST /store/order using GET method by mistake returns 405 Method Not Allowed.")
    public void testCreateOrderWithGetMethod() {
        Map<String, Object> order = new HashMap<>();
        order.put("id", 99999);
        order.put("petId", 1111);
        order.put("quantity", 2);

        given()
                .contentType("application/json")
                .body(order)
                .when()
                .get("/store/order")
                .then()
                .statusCode(405);
    }

    @Feature("Store")
    @Story("Order API Tests")
    @Test(description = "Negative test: Create order with invalid data")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateOrderInvalidData() {
        Order invalidOrder = OrderBuilder.buildWithParams(9999L, -5, "invalid");
        assert storeClient.createOrder(invalidOrder).jsonPath().get("status").equals("invalid");
    }
}
