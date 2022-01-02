package site.nomorepartie.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static site.nomorepartie.stellarburgers.UserData.login;

public class User extends RestAssuredClient{

    @Step
    public ValidatableResponse createUser(UserData userData) {
        return given()
                .spec(getBaseSpec())
                .body(userData)
                .when()
                .post("api/auth/register/")
                .then();
    }

    @Step
    public ValidatableResponse loginUser(UserData userData) {
        return given()
                .spec(getBaseSpec())
                .body(userData)
                .when()
                .post("api/auth/login/")
                .then();
    }

    @Step
    public ValidatableResponse changeUserData(String token, UserData userData) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .and()
                .body(userData)
                .when()
                .patch("api/auth/user/")
                .then();
    }
}
