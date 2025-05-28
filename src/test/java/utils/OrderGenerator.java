package utils;

import models.Order;
import io.qameta.allure.Step;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrderGenerator {

    @Step("Создание базового заказа")
    public static Order getBaseOrder() {
        String firstName = RandomGenerator.generateRandomString(8);
        String lastName = RandomGenerator.generateRandomString(8);
        String address = RandomGenerator.generateRandomString(15);
        String metroStation = String.valueOf(RandomGenerator.generateRandomInt(1, 100)); 
        String phone = RandomGenerator.generateRandomPhone();
        int rentTime = RandomGenerator.generateRandomInt(1, 7); // 1-7 дней
        String deliveryDate = RandomGenerator.generateFutureDate(1);
        String comment = "Тест: " + RandomGenerator.generateRandomString(10);
        
        // Базовый заказ без цвета
        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, null);
    }

    @Step("Создание заказа с цветом BLACK")
    public static Order getOrderWithBlackColor() {
        Order order = getBaseOrder();
        order.setColor(Collections.singletonList("BLACK"));
        return order;
    }

    @Step("Создание заказа с цветом GREY")
    public static Order getOrderWithGreyColor() {
        Order order = getBaseOrder();
        order.setColor(Collections.singletonList("GREY"));
        return order;
    }

    @Step("Создание заказа с обоими цветами")
    public static Order getOrderWithBothColors() {
        Order order = getBaseOrder();
        order.setColor(Arrays.asList("BLACK", "GREY"));
        return order;
    }
}
