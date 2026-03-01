import client.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.Courier;
import model.CourierCredentials;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CourierCreateTest extends BaseTest {
    private CourierClient courierClient;
    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {

        if (courier != null && courier.getLogin() != null && courier.getPassword() != null) {
            var loginResponse = courierClient.login(CourierCredentials.from(courier));
            if (loginResponse.extract().statusCode() == HttpStatus.SC_OK) {
                courierId = loginResponse.extract().path("id");
                courierClient.delete(courierId);
            }
        }
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Успешное создание нового курьера со всеми обязательными полями")
    public void courierCanBeCreatedTest() {
        courier = new Courier("ninja_ivan_123", "1234", "Ivan");
        courierClient.create(courier)
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Проверка ошибки при попытке создать курьера с уже существующим логином")
    public void cannotCreateTwoIdenticalCouriersTest() {
        courier = new Courier("double_ninja", "1234", "Ivan");
        courierClient.create(courier);

        courierClient.create(courier)
                .assertThat()
                .statusCode(HttpStatus.SC_CONFLICT) // 409
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка ошибки 400 при отсутствии поля login")
    public void cannotCreateCourierWithoutLoginTest() {
        courier = new Courier(null, "1234", "Ivan");
        courierClient.create(courier)
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST) // 400
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка ошибки 400 при отсутствии поля password")
    public void cannotCreateCourierWithoutPasswordTest() {
        courier = new Courier("ninja_ivan_123", null, "Ivan");
        courierClient.create(courier)
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
