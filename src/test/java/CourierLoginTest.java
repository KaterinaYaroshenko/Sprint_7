import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import client.CourierClient;
import model.Courier;
import model.CourierCredentials;
import static org.hamcrest.Matchers.*;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.qameta.allure.restassured.AllureRestAssured;

public class CourierLoginTest {
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        Courier courier = new Courier("login_test_777", "password", "Ivan");
        courierClient.create(courier);
        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void courierCanLogin() {
        CourierCredentials creds = new CourierCredentials("login_test_777", "password");

        courierId = courierClient.login(creds)
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @Test
    public void loginWithWrongPasswordReturnsError() {
        CourierCredentials creds = new CourierCredentials("login_test_777", "wrong_pass");
        courierClient.login(creds)
                .assertThat().statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        if (courierId != 0) courierClient.delete(courierId);
    }
}