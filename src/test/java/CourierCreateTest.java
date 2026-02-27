import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import client.CourierClient;
import model.Courier;
import model.CourierCredentials;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.qameta.allure.restassured.AllureRestAssured;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CourierCreateTest {
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
             RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
          RestAssured.filters(new AllureRestAssured());
    }


    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    public void courierCanBeCreated() {
        Courier courier = new Courier("ninja_ivan_123", "1234", "Ivan");
        courierClient.create(courier)
                .assertThat().statusCode(201)
                .body("ok", is(true));

        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");
    }

    @Test
    public void cannotCreateTwoIdenticalCouriers() {
        Courier courier = new Courier("double_ninja", "1234", "Ivan");
        courierClient.create(courier);
        courierClient.create(courier)
                .assertThat().statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");
    }

    @Test
    public void cannotCreateCourierWithoutLogin() {
        Courier courier = new Courier(null, "1234", "Ivan");
        courierClient.create(courier)
                .assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
