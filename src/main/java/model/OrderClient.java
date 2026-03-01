package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Order;
import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Создание заказа")
    public ValidatableResponse create(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Отмена заказа по трек-номеру {track}")
    public ValidatableResponse cancel(int track) {
        return given()
                .queryParam("track", track)
                .when()
                .put(ORDER_PATH + "/cancel")
                .then();
    }
}