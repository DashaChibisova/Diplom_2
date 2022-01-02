package site.nomorepartie.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static site.nomorepartie.stellarburgers.IngredientData.getIngredient;
import static site.nomorepartie.stellarburgers.UserData.login;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private boolean isLogin;
    private ArrayList<String> ingredient;
    private String path;
    private int expectedCode;
    private boolean expectedSuccess;
    private Matcher expected;


    public OrderCreateTest(boolean isLogin, ArrayList<String> ingredient, String path, int expectedCode, boolean expectedSuccess, Matcher expected) {
        this.isLogin = isLogin;
        this.ingredient = ingredient;
        this.path = path;
        this.expectedCode = expectedCode;
        this.expectedSuccess = expectedSuccess;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Object[] getTestData() {
        return new Object[][]{
                {true, getIngredient(2), "order.number", 200, true, notNullValue()},
                {false, getIngredient(2), "order.number", 200, true, notNullValue()},
                {true, null, "message", 400, false, containsString("Ingredient ids must be provided")},
                {false, null, "message", 400, false, containsString("Ingredient ids must be provided")}

        };
    }

    @DisplayName("Создание заказа с авторизацией и без")
    @Test
    public void createOrder() {
        if (isLogin) {
            UserData userData = new UserData().getRandom();
            new User().createUser(userData);
            new User().loginUser(login(userData)).assertThat().statusCode(200);
        }
        IngredientData ingredientData = new IngredientData(getIngredient(1)).setIngredients(ingredient);
        ValidatableResponse response = new Order().createOrder(ingredientData);
        boolean actualSuccess = response.extract().path("success");
        var actualOrderNumber = response.extract().path(path);
        int actualStatusCode = response.extract().statusCode();

        Assert.assertEquals(actualStatusCode, expectedCode);
        Assert.assertEquals(expectedSuccess, actualSuccess);
        Assert.assertThat(actualOrderNumber, expected);
    }
}
