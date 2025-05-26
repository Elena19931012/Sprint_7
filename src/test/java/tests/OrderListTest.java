package tests;

import api.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import models.Order;
import org.junit.Before;
import org.junit.Test;
import utils.OrderGenerator;

import static org.hamcrest.Matchers.*;

@Epic("Заказы")
@Feature("Получение списка заказов")
public class OrderListTest extends BaseTest {

    private final OrderClient orderClient = new OrderClient();
    private int trackNumber;

    @Before
    @Step("Создание тестового заказа")
    public void setUp() {
        Order order = OrderGenerator.getBaseOrder();
        trackNumber = orderClient.createOrder(order)
                .statusCode(201)
                .extract().path("track");
    }
    
    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что эндпоинт списка заказов возвращает список заказов")
    public void getOrderListShouldReturnOrders() {
        orderClient.getOrderList()
                .statusCode(200)
                .body("orders", not(emptyArray()))
                .body("orders", hasSize(greaterThan(0)))
                .body("orders[0]", hasKey("id"))
                .body("orders[0]", hasKey("firstName"))
                .body("orders[0]", hasKey("lastName"))
                .body("orders[0]", hasKey("address"))
                .body("orders[0]", hasKey("metroStation"))
                .body("orders[0]", hasKey("phone"))
                .body("orders[0]", hasKey("rentTime"))
                .body("orders[0]", hasKey("deliveryDate"))
                .body("orders[0]", hasKey("track"))
                .body("orders[0]", hasKey("status"))
                .body("pageInfo", hasKey("page"))
                .body("pageInfo", hasKey("total"))
                .body("pageInfo", hasKey("limit"))
                .body("availableStations", not(emptyArray()));
                
        try {
            orderClient.cancelOrder(trackNumber);
        } catch (Exception ignored) {
        }
    }
}
