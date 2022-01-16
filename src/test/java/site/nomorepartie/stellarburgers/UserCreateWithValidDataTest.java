package site.nomorepartie.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class UserCreateWithValidDataTest {
    private UserData userData;
    private ValidatableResponse response;

    @Before
    public void setUp() {
        userData = new UserData().getRandom();
        response = new User().createUser(userData);
    }

    //    удаление пользователя ручки еще нет
//    @After
//    public void tearDown() {
//    }
    @DisplayName("Можно создать пользователя с валидными данными")
    @Test
    public void userCanBeCreatedWithValidData() {
        boolean actualSuccess = response.extract().path("success");
        int actualStatusCode = response.extract().statusCode();
        String actualBody = response.extract().path("accessToken");

        Assert.assertEquals(actualStatusCode, 200);
        Assert.assertTrue(actualSuccess);
        Assert.assertNotNull(actualBody);
    }

    @DisplayName("Нельзя создать пользователя, если такой емейл уже есть в системе")
    @Test
    public void userShouldNotBeCreated() {
        userData.changePassword().changeName();
        response = new User().createUser(userData);

        boolean actualSuccess = response.extract().path("success");
        String actualMessage = response.extract().path("message");
        int actualStatusCode = response.extract().statusCode();

        Assert.assertEquals(actualStatusCode, 403);
        Assert.assertFalse(actualSuccess);
        Assert.assertEquals("User already exists", actualMessage);
    }
}
