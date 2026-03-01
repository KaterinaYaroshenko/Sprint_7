import client.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.Order;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest extends BaseTest {
    private final List<String> color;
    private OrderClient orderClient;
    private int track;

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        super.setUp();
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        if (track != 0) {
            orderClient.cancel(track);
        }
    }

    @Parameterized.Parameters(name = "Тестовые данные: цвета {0}")
    public static Object[][] data() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()},
        };
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    @Description("Проверка, что можно создать заказ с любым набором цветов или без них")
    public void createOrderWithDifferentColorsTest() {
        Order order = new Order("Naruto", "Uzumaki", "Konoha", "4", "+7 800 355", 5, "2024-10-10", "Comment", color);

        track = orderClient.create(order)
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("track", notNullValue())
                .extract()
                .path("track");
    }
}
