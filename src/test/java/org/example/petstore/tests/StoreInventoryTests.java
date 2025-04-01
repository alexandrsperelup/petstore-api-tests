package org.example.petstore.tests;

import org.example.petstore.clients.StoreClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.example.petstore.hooks.TestListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

@Listeners({TestListener.class})
@Epic("Petstore API")
@Feature("Store")
@Story("Inventory API Tests")
@Owner("qa-team")
@Severity(SeverityLevel.NORMAL)
public class StoreInventoryTests {

    private final StoreClient storeClient = new StoreClient();

    @Feature("Store")
    @Story("Inventory API Tests")
    @Test(description = "Verify that store inventory returns success status code")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetInventorySuccess() {
        Response response = storeClient.getInventory();
        response.then().log().all()
                .statusCode(200)
                .body("pending", greaterThanOrEqualTo(0));
    }


    @Feature("Store")
    @Story("Inventory API Tests")
    @Test(description = "Negative test: Check unauthorized access scenario")
    @Severity(SeverityLevel.NORMAL)
    public void testGetInventoryUnauthorized() {
        // Intentionally pass invalid token or remove token
        Response response = storeClient.getInventory()
                .then().extract().response();
        // Example negative scenario: if token is invalid or missing
        // we expect a 401 or 403
        // This is a placeholder; implement as per your actual API's behavior
        // response.then().statusCode(401);
    }
}
