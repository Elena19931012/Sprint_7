package tests;

import api.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import models.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.OrderGenerator;

import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

@Epic("Заказы")
@Feature("Получение списка заказов")
public class OrderListTest {

    private final OrderClient orderClient = new OrderClient();
    private int trackNumber;

    @Before
    @Step("Создание тестового заказа")
    public void setUp() {
        Order order = OrderGenerator.getBaseOrder();
        trackNumber = orderClient.createOrder(order)
                .statusCode(SC_CREATED)
                .extract().path("track");
    }
    
    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что эндпоинт списка заказов возвращает список заказов")
    public void getOrderListShouldReturnOrders() {
        orderClient.getOrderList()
                .statusCode(SC_OK)
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
    }

    @After
    @Step("Отмена созданного заказа")
    public void cleanUp() {
        if (trackNumber != 0) {
            try {
                orderClient.cancelOrder(trackNumber)
                    .statusCode(anyOf(is(SC_OK), is(SC_ACCEPTED)))
                    .log().ifValidationFails();
                System.out.println("Заказ с номером " + trackNumber + " успешно отменен");
            } catch (Exception e) {
                System.out.println("Ошибка при отмене заказа с номером " + trackNumber + ": " + e.getMessage());
            }
        }
    }
}