import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierApiSteps;

import java.net.HttpURLConnection;

import static constants.UrlConstants.BASE_URI;
import static constants.UrlConstants.COURIER_BASE_PATH;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTests {

    private final Courier validCourier = new Courier("Natali", "Password");
    private final CourierApiSteps courierApiSteps = new CourierApiSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.basePath = COURIER_BASE_PATH;
        courierApiSteps.setCourier(validCourier);
    }

    private void verifyLoginResponse(Courier courier, int expectedStatusCode, String expectedMessage) {
        courierApiSteps.setCourier(courier);
        courierApiSteps.loginCourier()
                .then().log().all()
                .assertThat()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }

    @Test
    @DisplayName("Успешный вход курьера при корректных данных")
    public void testSuccessfulCourierLogin() {
        courierApiSteps.createCourier();
        courierApiSteps.loginCourier()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Ошибка при входе курьера без указания логина")
    public void testCourierLoginWithoutLogin() {
        verifyLoginResponse(new Courier("", "Password"), HttpURLConnection.HTTP_BAD_REQUEST, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Ошибка при входе курьера без указания пароля")
    public void testCourierLoginWithoutPassword() {
        verifyLoginResponse(new Courier("Login", ""), HttpURLConnection.HTTP_BAD_REQUEST, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Ошибка при входе несуществующего курьера")
    public void testLoginUnknownCourier() {
        courierApiSteps.deleteCourier();
        verifyLoginResponse(validCourier, HttpURLConnection.HTTP_NOT_FOUND, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Ошибка при входе курьера с неверным логином")
    public void testCourierLoginWithIncorrectLogin() {
        courierApiSteps.createCourier();
        verifyLoginResponse(new Courier("InvalidLogin123", "Password"), HttpURLConnection.HTTP_NOT_FOUND, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Ошибка при входе курьера с неверным паролем")
    public void testCourierLoginWithIncorrectPassword() {
        courierApiSteps.createCourier();
        verifyLoginResponse(new Courier("Natali", "WrongPassword"), HttpURLConnection.HTTP_NOT_FOUND, "Учетная запись не найдена");
    }

    @After
    public void tearDown() {
        courierApiSteps.deleteCourier();
    }
}
