package RestAssured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Activity3 {
    // Declare request specification
    RequestSpecification requestSpec;
    // Declare response specification
    ResponseSpecification responseSpec;

    @BeforeClass
    public void setUp() {
        // Create request specification
        requestSpec = new RequestSpecBuilder()
            .addHeader("Content-Type", "application/json")
            .setBaseUri("https://petstore.swagger.io/v2/pet")
            .build();

        // Create response specification (only common checks)
        responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectContentType("application/json")
            .build();
    }

    @DataProvider(name = "petInfo")
    public Object[][] petInfoProvider() {
        // Setting parameters to pass to test case
        return new Object[][] {
            { 77232, "Riley", "alive" },
            { 77233, "Hansel", "alive" }
        };
    }

    @Test(priority = 1, dataProvider = "petInfo")
    public void addPets(int petId, String petName, String petStatus) {
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", petId);
        reqBody.put("name", petName);
        reqBody.put("status", petStatus);

        given().spec(requestSpec)
            .body(reqBody)
        .when()
            .post()
        .then().spec(responseSpec)
            .body("name", equalTo(petName))
            .body("status", equalTo(petStatus)); // Assertion only here
    }

    @Test(priority = 2, dataProvider = "petInfo")
    public void getPets(int petId, String petName, String petStatus) {
        given().spec(requestSpec)
            .pathParam("petId", petId)
            .log().all()
        .when()
            .get("/{petId}")
        .then().spec(responseSpec)
            .body("name", equalTo(petName))
            .body("status", equalTo(petStatus)) // Assertion only here
            .log().all();
    }

    @Test(priority = 3, dataProvider = "petInfo")
    public void deletePets(int petId, String petName, String petStatus) {
        given().spec(requestSpec)
            .pathParam("petId", petId)
        .when()
            .delete("/{petId}")
        .then()
            .statusCode(200) // DELETE returns code/message, not status
            .body("code", equalTo(200))
            .body("message", equalTo("" + petId));
    }
}
