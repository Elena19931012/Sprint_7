package tests;

import api.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import models.Order;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.OrderGenerator;

import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

@Epic("Заказы")
@Feature("Создание заказа")
@RunWith(Parameterized.class)
public class OrderCreateTest extends BaseTest {

    private final OrderClient orderClient = new OrderClient();
    private final Order order;
    private final String testDescription;

    private static final List<Integer> createdOrderTracks = new ArrayList<>();

    public OrderCreateTest(String testDescription, Order order) {
        this.testDescription = testDescription;
        this.order = order;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Заказ с цветом BLACK", OrderGenerator.getOrderWithBlackColor()},
                {"Заказ с цветом GREY", OrderGenerator.getOrderWithGreyColor()},
                {"Заказ с обоими цветами", OrderGenerator.getOrderWithBothColors()},
                {"Заказ без цвета", OrderGenerator.getBaseOrder()}
        });
    }

    @Test
    @DisplayName("Создание заказа с разными вариантами цвета")
    @Description("Проверка, что создание заказа работает с разными комбинациями цветов")
    @Step("Тестирование создания заказа: {0}")
    public void createOrderWithDifferentColors() {
        int trackNumber = orderClient.createOrder(order)
                .statusCode(SC_CREATED)
                .body("$", hasKey("track"))
                .body("track", not(nullValue()))
                .extract().path("track");
        
        createdOrderTracks.add(trackNumber);
    }
    
    @After
    @Step("Отмена созданных заказов")
    public void cleanUp() {
        for (Integer track : createdOrderTracks) {
            try {
                orderClient.cancelOrder(track)
                    .statusCode(anyOf(is(SC_OK), is(SC_ACCEPTED)))
                    .log().ifValidationFails();
            } catch (Exception e) {
                System.out.println("Ошибка при отмене заказа с номером " + track + ": " + e.getMessage());
            }
        }
        createdOrderTracks.clear();
    }
}