package steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Setter;
import model.Courier;

import static io.restassured.RestAssured.given;

@Setter
public class CourierApiSteps {
    private Courier courier;

    // Статический блок для настройки RestAssured
    static {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/courier";
    }

    private Response sendRequest(String endpoint, String method) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .request(method, endpoint)
                .then().log().all()
                .extract().response();
    }

    @Step("Создание курьера")
    public Response createCourier() {
        return sendRequest("", "POST");
    }

    @Step("Авторизация курьера")
    public Response loginCourier() {
        return sendRequest("/login", "POST");
    }

    @Step("Удаление курьера")
    public void deleteCourier() {
        Response loginResponse = loginCourier();
        Integer courierId = loginResponse.path("id");

        if (courierId != null) {
            Response deleteResponse = sendRequest("/" + courierId, "DELETE");
            if (deleteResponse.statusCode() == 200) {
                System.out.println("Курьер успешно удалён.");
            } else {
                System.err.println("Ошибка при удалении курьера: " + deleteResponse.statusLine());
            }
        } else {
            System.err.println("Не удалось получить ID курьера для удаления.");
        }
    }
}
