package RestAssured;



import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import io.restassured.response.Response;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ActivityPet {

    static long petId;

    // :one: POST - Create Pet
    @Test
    @Order(1)
    public void createPet() {

        long requestedId = 12345L;

        Response response = given()
            .baseUri("https://petstore.swagger.io/v2/pet")
            .header("Content-Type", "application/json")
            .body("{ \"id\": " + requestedId + ", \"name\": \"RohanPet\", \"status\": \"available\" }")
            .when().post();

        // Extract petId
        petId = response.jsonPath().getLong("id");

        System.out.println("Pet Created: " + response.asPrettyString());

        response.then().body("id", equalTo((int)requestedId));
        response.then().body("name", equalTo("RohanPet"));
        response.then().body("status", equalTo("available"));
    }

    // :two: GET - Get Pet Details
    @Test
    @Order(2)
    public void getPet() {

        Response response = given()
            .baseUri("https://petstore.swagger.io/v2/pet")
            .pathParam("id", petId)
            .when().get("/{id}");

        System.out.println("Pet Details: " + response.asPrettyString());

        response.then().body("id", equalTo((int) petId));
    }

    // :three: PUT - Update Pet Status
    @Test
    @Order(3)
    public void updatePet() {

        Response response = given()
            .baseUri("https://petstore.swagger.io/v2/pet")
            .header("Content-Type", "application/json")
            .body("{ \"id\": " + petId + ", \"name\": \"RohanPet\", \"status\": \"sold\" }")
            .when().put();

        System.out.println(":arrows_counterclockwise: Pet Updated: " + response.asPrettyString());

        response.then().body("status", equalTo("sold"));
    }

    // :four: GET - Verify Update
    @Test
    @Order(4)
    public void verifyUpdate() {

        Response response = given()
            .baseUri("https://petstore.swagger.io/v2/pet")
            .pathParam("id", petId)
            .when().get("/{id}");

        System.out.println(" Verify Updated Pet: " + response.asPrettyString());

        response.then().body("status", equalTo("sold"));
    }

    // :five: DELETE - Delete Pet
    @Test
    @Order(5)
    public void deletePet() {

        Response response = given()
            .baseUri("https://petstore.swagger.io/v2/pet")
            .pathParam("id", petId)
            .when().delete("/{id}");

        System.out.println(":wastebasket: Pet Deleted");

        response.then().body("code", equalTo(200));
    }

    // :six: GET - Verify Deletion
    @Test
    @Order(6)
    public void verifyDeletion() {

        Response response = given()
            .baseUri("https://petstore.swagger.io/v2/pet")
            .pathParam("id", petId)
            .when().get("/{id}");

        System.out.println(" After Deletion Response: " + response.asPrettyString());

        response.then().body("message", equalTo("Pet not found"));
    }
}