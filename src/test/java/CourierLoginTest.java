import client.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.Courier;
import model.CourierCredentials;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest extends BaseTest {
    private CourierClient courierClient;
    private int courierId;
    private final String login = "login_test_777";
    private final String password = "password";

    @Before
    public void setUp() {
        super.setUp();
        courierClient = new CourierClient();

        Courier courier = new Courier(login, password, "Ivan");
        courierClient.create(courier);
    }

    @After
    public void tearDown() {

        var loginResponse = courierClient.login(new CourierCredentials(login, password));
        if (loginResponse.extract().statusCode() == HttpStatus.SC_OK) {
            courierId = loginResponse.extract().path("id");
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Логин курьера в системе")
    @Description("Успешный логин курьера с валидными данными")
    public void courierCanLoginTest() {
        CourierCredentials creds = new CourierCredentials(login, password);
        courierClient.login(creds)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Проверка ошибки 404 при вводе некорректного пароля")
    public void loginWithWrongPasswordReturnsErrorTest() {
        CourierCredentials creds = new CourierCredentials(login, "wrong_pass");
        courierClient.login(creds)
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND) // 404
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин с неверным логином")
    @Description("Проверка ошибки 404 при вводе несуществующего логина")
    public void loginWithWrongLoginReturnsErrorTest() {
        CourierCredentials creds = new CourierCredentials("wrong_login_999", password);
        courierClient.login(creds)
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND) // 404
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
