import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import steps.OrderApiSteps;

import java.net.HttpURLConnection;

import static constants.UrlConstants.BASE_URI;
import static constants.UrlConstants.ORDERS_BASE_PATH;
import static org.hamcrest.Matchers.notNullValue;

public class OrderGetTest {
    OrderApiSteps orderApiSteps;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.basePath = ORDERS_BASE_PATH;
        orderApiSteps = new OrderApiSteps();
    }

    @Test
    @DisplayName("Тест на получение списка заказов")
    public void orderGetTest() {
        orderApiSteps.getOrder()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("orders", notNullValue());

    }
}