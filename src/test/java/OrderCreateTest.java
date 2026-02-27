import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.runners.Parameterized;
import io.restassured.RestAssured;
import io.qameta.allure.restassured.AllureRestAssured;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.notNullValue;
import model.Order;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final List<String> color;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.filters(new AllureRestAssured());
    }

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Arrays.asList()},
        };
    }

    @Test
    public void createOrderWithDifferentColors() {
        Order order = new Order("Naruto", "Uzumaki", "Konoha", "4", "+7 800 355", 5, "2024-10-10", "Comment", color);
        RestAssured.given()
                .header("Content-type", "application/json")
                .body(order)
                .post("/api/v1/orders")
                .then().statusCode(201)
                .body("track", notNullValue());
    }
}
