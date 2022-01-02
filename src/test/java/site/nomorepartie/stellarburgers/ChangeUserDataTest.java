package site.nomorepartie.stellarburgers;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static site.nomorepartie.stellarburgers.UserData.login;

public class ChangeUserDataTest {
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

    @DisplayName("Можно изменить логин авторизированного пользователя")
    @Test
    public void changeEmailUserWithRegistration() {
        ChangeData ref = () -> userData.changeEmail();
        String change = change(ref, userData, "user.email", 200, true, true);
        Assert.assertEquals(change, userData.getEmail());
        new User().loginUser(login(userData)).assertThat().statusCode(200);
    }

    @DisplayName("Можно изменить пароль авторизированного пользователя")
    @Test
    public void changePasswordUserWithRegistration() {
        ChangeData ref = () -> userData.changePassword();
        String change = change(ref, userData, "user.email", 200, true, true);
        Assert.assertEquals(change, userData.getEmail());
        new User().loginUser(login(userData)).assertThat().statusCode(200);
    }

    @DisplayName("Можно изменить имя авторизированного пользователя")
    @Test
    public void changeNameUserWithRegistration() {
        ChangeData ref = () -> userData.changeName();
        String change = change(ref, userData, "user.name", 200, true, true);
        Assert.assertEquals(change, userData.getName());
        new User().loginUser(login(userData)).assertThat().statusCode(200);
    }

    @DisplayName("Нельзя изменить емейл пользователя, если такой уже есть в системе")
    @Test
    public void changeIncorrectEmailUserWithRegistration() {
        ValidatableResponse changeResponse = new User().createUser(userData);
        String email = changeResponse.extract().path("user.email");
        ChangeData ref = () -> userData.setEmail(email);
        String change = change(ref, userData, "message", 403, false, true);
        Assert.assertEquals(change, "User with such email already exists");
    }

    @DisplayName("Нельзя изменить емейл неавторизированного пользователя")
    @Test
    public void changeEmailUserNotRegistration() {
        ChangeData ref = () -> userData.changeEmail();
        String change = change(ref, userData, "message", 401, false, false);
        Assert.assertEquals(change, "You should be authorised");
    }

    @DisplayName("Нельзя изменить пароль неавторизированного пользователя")
    @Test
    public void changePasswordUserNotRegistration() {
        ChangeData ref = () -> userData.changePassword();
        String change = change(ref, userData, "message", 401, false, false);
        Assert.assertEquals(change, "You should be authorised");
    }

    @DisplayName("Нельзя изменить имя неавторизированного пользователя")
    @Test
    public void changeNameUserNotRegistration() {
        ChangeData ref = () -> userData.changeName();
        String change = change(ref, userData, "message", 401, false, false);
        Assert.assertEquals(change, "You should be authorised");
    }

    @Step
    public String change(ChangeData ref, UserData userData, String path, int expectedStatus, boolean expectedSuccess, boolean isToken) {
        String token = "";
        if (isToken) {
            ValidatableResponse loginResponse = new User().loginUser(login(userData));
            token = (loginResponse.extract().path("accessToken")).toString().replace("Bearer ", "");
        }
        ValidatableResponse changeResponse = new User().changeUserData(token, ref.isChange());
        boolean actualSuccess = changeResponse.extract().path("success");
        int actualStatusCode = changeResponse.extract().statusCode();
        Assert.assertEquals(actualStatusCode, expectedStatus);
        Assert.assertEquals(actualSuccess, expectedSuccess);
        return changeResponse.extract().path(path);
    }
}
