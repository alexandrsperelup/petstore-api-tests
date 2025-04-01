package org.example.petstore.tests;

import org.example.petstore.builders.OrderBuilder;
import org.example.petstore.clients.StoreClient;
import org.example.petstore.data.Order;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.example.petstore.hooks.TestListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Listeners({TestListener.class})
@Epic("Petstore API")
@Feature("Store")
@Story("Delete Orders API Tests")
@Owner("qa-team")
@Severity(SeverityLevel.NORMAL)
public class StoreOrderDeletionTests {

    private final StoreClient storeClient = new StoreClient();

    @Flaky
    @Feature("Store")
    @Story("Order Delete API Tests")
    @Test(description = "Delete an existing order successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteOrderSuccess() {
        Order order = OrderBuilder.buildDefaultOrder();
        storeClient.createOrder(order).then().statusCode(200);

        Response deleteResponse = storeClient.deleteOrderById(order.getId());
        deleteResponse.then().log().all()
                .body("message", equalTo(order.getId().toString()));

        storeClient.getOrderById(order.getId())
                .then()
                .statusCode(404)
                .body("message", equalTo("Order not found"))
                .body("type", equalTo("error"));
    }

    @Feature("Store")
    @Story("Order Delete API Tests")
    @Test(description="DELETE /store/order with no ID in path should return 404 or 405.")
    public void testDeleteOrderNoId() {
        given()
                .when()
                .delete("/store/order/")
                .then()
                .statusCode(anyOf(equalTo(404), equalTo(405)));
    }

    @Feature("Store")
    @Story("Order Delete API Tests")
    @Test(description = "Negative test: Delete non-existing order")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteNonExistingOrder() {
        long nonExistingId = 999999L;
        storeClient.deleteOrderById(nonExistingId)
                .then()
                .statusCode(equalTo(404))
                .body("message", equalTo("Order Not Found"))
                .body("type", equalTo("unknown"));
    }
}
