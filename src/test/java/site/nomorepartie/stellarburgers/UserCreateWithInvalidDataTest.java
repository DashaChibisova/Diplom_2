package site.nomorepartie.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


@RunWith(Parameterized.class)
public class UserCreateWithInvalidDataTest {

    private final UserData userData;

    public UserCreateWithInvalidDataTest(UserData userData) {
        this.userData = userData;
    }

    @Parameterized.Parameters
    public static Object[] getTestData() {
        return new Object[][]{
                {UserData.getWithoutEmail()},
                {UserData.getWithoutPassword()},
                {UserData.getWithoutName()}
        };
    }
    @DisplayName("Нельзя создать пользователя, если не введен один из параметров")
    @Test
    public void invalidRequestIsNotAllowed() {
        ValidatableResponse response = new User().createUser(userData);
        boolean actualSuccess = response.extract().path("success");
        String actualMessage = response.extract().path("message");
        int actualStatusCode =  response.extract().statusCode();

        Assert.assertEquals(actualStatusCode, 403);
        Assert.assertFalse(actualSuccess);
        Assert.assertEquals("Email, password and name are required fields", actualMessage);
    }
}