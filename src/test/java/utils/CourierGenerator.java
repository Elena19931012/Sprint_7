package utils;

import models.CourierCreate;
import io.qameta.allure.Step;

public class CourierGenerator {

    @Step("Генерация случайного курьера")
    public static CourierCreate getRandomCourier() {
        String login = RandomGenerator.generateRandomString(10);
        String password = RandomGenerator.generateRandomString(10);
        String firstName = RandomGenerator.generateRandomString(10);
        
        return new CourierCreate(login, password, firstName);
    }

    @Step("Генерация курьера без логина")
    public static CourierCreate getCourierWithoutLogin() {
        return new CourierCreate(null, RandomGenerator.generateRandomString(10), RandomGenerator.generateRandomString(10));
    }

    @Step("Генерация курьера без пароля")
    public static CourierCreate getCourierWithoutPassword() {
        return new CourierCreate(RandomGenerator.generateRandomString(10), null, RandomGenerator.generateRandomString(10));
    }

    @Step("Генерация курьера без имени")
    public static CourierCreate getCourierWithoutFirstName() {
        return new CourierCreate(RandomGenerator.generateRandomString(10), RandomGenerator.generateRandomString(10), null);
    }
}
