package utils;

import models.CourierModel; 
import io.qameta.allure.Step;

public class CourierGenerator {

    @Step("Генерация случайного курьера")
    public static CourierModel getRandomCourier() { 
        String login = RandomGenerator.generateRandomString(10);
        String password = RandomGenerator.generateRandomString(10);
        String firstName = RandomGenerator.generateRandomString(10);
        return new CourierModel(login, password, firstName);
    }

    @Step("Генерация курьера без логина")
    public static CourierModel getCourierWithoutLogin() { 
        String password = RandomGenerator.generateRandomString(10);
        String firstName = RandomGenerator.generateRandomString(10);
        return new CourierModel(null, password, firstName);
    }

    @Step("Генерация курьера без пароля")
    public static CourierModel getCourierWithoutPassword() { 
        String login = RandomGenerator.generateRandomString(10);
        String firstName = RandomGenerator.generateRandomString(10);
        return new CourierModel(login, null, firstName);
    }

    @Step("Генерация курьера без имени")
    public static CourierModel getCourierWithoutFirstName() { 
        String login = RandomGenerator.generateRandomString(10);
        String password = RandomGenerator.generateRandomString(10);
        return new CourierModel(login, password, null);
    }
}