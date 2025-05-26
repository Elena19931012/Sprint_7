package tests;

import api.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import models.CourierCreate;
import models.CourierCredentials;
import org.junit.After;
import org.junit.Test;
import utils.CourierGenerator;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

@Epic("Курьеры")
@Feature("Создание курьера")
public class CourierCreateTest extends BaseTest {

    private final CourierClient courierClient = new CourierClient();
    private CourierCreate courier;
    private int courierId;

    @After
    @Step("Удаление тестовых данных")
    public void tearDown() {
        if (courierId != 0) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Создание нового курьера")
    @Description("Проверка, что курьер может быть создан с валидными данными и возвращает ok: true")
    public void createCourierWithValidData() {
        courier = CourierGenerator.getRandomCourier();
        
        courierClient.createCourier(courier)
                .statusCode(201)
                .body("ok", is(true));
        
        CourierCredentials credentials = courier.toCredentials();
        courierId = courierClient.loginCourier(credentials)
                .statusCode(200)
                .extract().path("id");

        assertTrue("Идентификатор курьера должен быть больше 0", courierId > 0);
    }

    @Test
    @DisplayName("Создание дубликата курьера")
    @Description("Проверка, что создание курьера с существующим логином возвращает ошибку 409")
    public void createDuplicateCourierShouldReturnError() {
        courier = CourierGenerator.getRandomCourier();
        
        courierClient.createCourier(courier)
                .statusCode(201);
        
        CourierCredentials credentials = courier.toCredentials();
        courierId = courierClient.loginCourier(credentials)
                .statusCode(200)
                .extract().path("id");
        
        CourierCreate duplicateCourier = new CourierCreate(
            courier.getLogin(), 
            "differentPassword", 
            "differentName"
        );
        
        courierClient.createCourier(duplicateCourier)
                .statusCode(409)
                .body("message", is("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка, что создание курьера без логина возвращает ошибку 400")
    public void createCourierWithoutLoginShouldReturnError() {
        courier = CourierGenerator.getCourierWithoutLogin();
        
        courierClient.createCourier(courier)
                .statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка, что создание курьера без пароля возвращает ошибку 400")
    public void createCourierWithoutPasswordShouldReturnError() {
        courier = CourierGenerator.getCourierWithoutPassword();
        
        courierClient.createCourier(courier)
                .statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без имени")
    @Description("Проверка, что создание курьера без имени все равно работает")
    public void createCourierWithoutFirstNameShouldWork() {
        courier = CourierGenerator.getCourierWithoutFirstName();
        
        courierClient.createCourier(courier)
                .statusCode(201)
                .body("ok", is(true));

        CourierCredentials credentials = courier.toCredentials();
        courierId = courierClient.loginCourier(credentials)
                .statusCode(200)
                .extract().path("id");

        assertTrue("Идентификатор курьера должен быть больше 0", courierId > 0);
    }
}
