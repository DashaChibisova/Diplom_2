package site.nomorepartie.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static site.nomorepartie.stellarburgers.UserData.login;

public class GetOrderClientTest {
    private UserData userData;

    @Before
    public void setUp() {
        userData = new UserData().getRandom();
        new User().createUser(userData);
    }
    // удаление пользователя ручки еще нет
//    @After
//    public void tearDown() {
//    }
    @DisplayName("Можно получить список заказов авторизированного пользователя")
    @Test
    public void checkThatReturnOrderClientWithRegistration() {
        ValidatableResponse loginResponse = new User().loginUser(login(userData)).assertThat().statusCode(200);
        String token = loginResponse.extract().path("accessToken").toString().replace("Bearer ", "");
        ValidatableResponse getOrderResponse = new Order().getOrderClient(token);
        int actualStatusCode = getOrderResponse.extract().statusCode();
        boolean actualSuccess = getOrderResponse.extract().path("success");
        int actualTotal = getOrderResponse.extract().path("total");

        Assert.assertEquals(actualStatusCode, 200);
        Assert.assertTrue(actualSuccess);
        Assert.assertThat(actualTotal, notNullValue());
    }
    @DisplayName("Нельзя получить список заказов неавторизированного пользователя")
    @Test
    public void checkThatNotReturnOrderClientWithoutRegistration() {
        ValidatableResponse getOrderResponse = new Order().getOrderClient("");
        int actualStatusCode = getOrderResponse.extract().statusCode();
        boolean actualSuccess = getOrderResponse.extract().path("success");
        String actualMessage = getOrderResponse.extract().path("message");

        Assert.assertEquals(actualStatusCode, 401);
        Assert.assertFalse(actualSuccess);
        Assert.assertEquals(actualMessage, "You should be authorised");
    }
}
