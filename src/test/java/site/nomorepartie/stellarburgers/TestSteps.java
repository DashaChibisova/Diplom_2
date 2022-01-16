package site.nomorepartie.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;

import static site.nomorepartie.stellarburgers.UserData.login;

public class TestSteps {

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
