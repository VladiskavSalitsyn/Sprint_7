import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.OrderApiSteps;
import static constants.UrlConstants.BASE_URI;
import static constants.UrlConstants.ORDERS_BASE_PATH;
import java.net.HttpURLConnection;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTests {

    @Parameterized.Parameter
    public Order order;
    private OrderApiSteps orderApiSteps;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.basePath = ORDERS_BASE_PATH;
        orderApiSteps = new OrderApiSteps();
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {new Order("John", "Doe", "Maple Street, 123", "12", "123-456-7890", 3, "2025-10-10", "Handle with care", new String[]{})},
                {new Order("Alice", "Brown", "Oak Avenue, 56", "23", "098-765-4321", 4, "2025-11-15", "Leave at the front door", new String[]{"BLACK"})},
                {new Order("Emma", "Green", "Pine Road, 89", "34", "567-890-1234", 2, "2025-12-05", "Ring the bell twice", new String[]{"GREY"})},
                {new Order("Michael", "White", "Birch Lane, 101", "45", "234-567-8901", 6, "2025-09-20", "Do not bend", new String[]{"BLACK", "GREY"})}
        };
    }

    @Test
    @DisplayName("Тест на создание заказа")
    public void testCreateOrder() {
        orderApiSteps.setOrder(order);
        orderApiSteps.createOrder()
                .then().log().all()
                .assertThat().statusCode(HttpURLConnection.HTTP_CREATED)
                .body("track", notNullValue());
    }
}