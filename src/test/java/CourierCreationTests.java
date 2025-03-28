import io.qameta.allure.junit4.DisplayName;
import model.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.restassured.RestAssured;
import steps.CourierApiSteps;

import java.net.HttpURLConnection;

import static constants.UrlConstants.BASE_URI;
import static constants.UrlConstants.COURIER_BASE_PATH;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

public class CourierCreationTests {

    private final Courier courier = new Courier("Natali", "password", "Natalia");
    private final CourierApiSteps courierApiSteps = new CourierApiSteps();

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.basePath = COURIER_BASE_PATH;
        courierApiSteps.setCourier(courier);
    }

    private void verifyResponse(int expectedStatusCode, String expectedMessage) {
        courierApiSteps.createCourier()
                .then().log().all()
                .assertThat()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }

    @Test
    @DisplayName("Успешное создание нового курьера при корректных данных")
    public void testCreateCourierSuccessfully() {
        boolean isCourierCreated = courierApiSteps.createCourier()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .extract()
                .path("ok");
        assertTrue(isCourierCreated);
    }

    @Test
    @DisplayName("Ошибка при создании курьера с уже существующим логином")
    public void testCreateDuplicateCourierFails() {
        courierApiSteps.createCourier();
        verifyResponse(HttpURLConnection.HTTP_CONFLICT, "Этот логин уже используется. Попробуйте другой.");
    }

    @Test
    @DisplayName("Ошибка при создании курьера без указания логина")
    public void testCreateCourierWithoutLoginFails() {
        courierApiSteps.setCourier(new Courier("", "password", "Natalia"));
        verifyResponse(HttpURLConnection.HTTP_BAD_REQUEST, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Ошибка при создании курьера без указания пароля")
    public void testCreateCourierWithoutPasswordFails() {
        courierApiSteps.setCourier(new Courier("Natali", "", "Natalia"));
        verifyResponse(HttpURLConnection.HTTP_BAD_REQUEST, "Недостаточно данных для создания учетной записи");
    }

    @After
    public void tearDown() {
        courierApiSteps.deleteCourier();
    }
}