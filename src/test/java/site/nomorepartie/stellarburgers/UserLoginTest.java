package site.nomorepartie.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static site.nomorepartie.stellarburgers.UserData.*;

public class UserLoginTest {
    private UserData userData;
    private ValidatableResponse response;

    @Before
    public void setUp() {
        userData = new UserData().getRandom();
        response = new User().createUser(userData);
    }

    // удаление пользователя ручки еще нет
//    @After
//    public void tearDown() {
//    }
    @DisplayName("Можно зайти в систему под существующим пользователем")
    @Test
    public void loginUserWithValidData() {
        ValidatableResponse loginResponse = new User().loginUser(login(userData));
        boolean actualSuccess = loginResponse.extract().path("success");
        int actualStatusCode =  response.extract().statusCode();
        String actualBody =  response.extract().path("accessToken");

        Assert.assertEquals(actualStatusCode, 200);
        Assert.assertThat(actualBody, notNullValue());
        Assert.assertTrue(actualSuccess);
    }
    @DisplayName("Нельзя зайти в систему с неверным емейлом")
    @Test
    public void loginUserWithInvalidEmailData() {
        ValidatableResponse loginResponse = new User().loginUser(loginWithInvalidEmail(userData));
        boolean actualSuccess = loginResponse.extract().path("success");
        String actualMessage = loginResponse.extract().path("message");
        int actualStatusCode = loginResponse.extract().statusCode();

        Assert.assertEquals(401,actualStatusCode);
        Assert.assertFalse(actualSuccess);
        Assert.assertEquals("email or password are incorrect", actualMessage);
    }
    @DisplayName("Нельзя зайти в систему с неверным паролем")
    @Test
    public void loginUserWithInvalidPasswordData() {
        ValidatableResponse loginResponse = new User().loginUser(loginWithInvalidPassword(userData));
        boolean actualSuccess = loginResponse.extract().path("success");
        String actualMessage = loginResponse.extract().path("message");
        int actualStatusCode = loginResponse.extract().statusCode();

        Assert.assertEquals(401,actualStatusCode);
        Assert.assertFalse(actualSuccess);
        Assert.assertEquals("email or password are incorrect", actualMessage);
    }
}
