package client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private static final String COURIER_PATH = "api/v1/courier/";

    @Step("Создание курьера {courier.login}")
    public ValidatableResponse create(Courier courier) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then()
                .log().all();
    }

    @Step("Логин курьера с учетными данными {credentials.login}")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(credentials)
                .when()
                .post(COURIER_PATH + "login")
                .then()
                .log().all();
    }

    @Step("Удаление курьера с id {courierId}")
    public ValidatableResponse delete(int courierId) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .when()
                .delete(COURIER_PATH + courierId)
                .then()
                .log().all();
    }
}
