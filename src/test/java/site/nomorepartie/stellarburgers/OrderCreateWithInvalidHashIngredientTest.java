package site.nomorepartie.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import static site.nomorepartie.stellarburgers.IngredientData.invalidDataIngr;

public class OrderCreateWithInvalidHashIngredientTest {
    @DisplayName("Нельзя создать заказ с неверным хэшем ингридиента")
    @Test
    public void orderShouldNotBeCreateWithInvalidIngredient() {
        IngredientData ingredientData = new IngredientData(invalidDataIngr);
        Response response = new Order().createOrder(ingredientData).extract().response();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
    }
}
