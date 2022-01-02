package site.nomorepartie.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class Order extends RestAssuredClient{

    @Step
    public ValidatableResponse createOrder(IngredientData ingredient) {
        return given()
                .spec(getBaseSpec())
                .body(ingredient)
                .when()
                .post("api/orders/")
                .then();
    }

    @Step
    public  ValidatableResponse getIngredient() {
        return given()
                .spec(getBaseSpec())
                .get("api/ingredients/")
                .then();
    }

    @Step
    public  ValidatableResponse getOrderClient(String token) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .get("api/orders/")
                .then();
    }
}
