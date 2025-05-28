package tests;

import api.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import models.CourierModel; 
import models.CourierCredentials;
import org.junit.After;
import org.junit.Test;
import utils.CourierGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

@Epic("Курьеры")
@Feature("Создание курьера")
public class CourierCreateTest {

    private final CourierClient courierClient = new CourierClient();
    private CourierModel courier; 
    private int courierId;

    @After
    @Step("Очистка тестовых данных")
    public void tearDown() {
        if (courierId == 0 && courier != null && courier.getLogin() != null && courier.getPassword() != null) {
            try {
                CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
                courierId = courierClient.loginCourier(credentials)
                        .extract().path("id");
                System.out.println("Получен ID курьера для очистки: " + courierId);
            } catch (Exception e) {
                System.out.println("Не удалось получить ID курьера для очистки: " + e.getMessage());
            }
        }

        if (courierId != 0) {
            try {
                courierClient.deleteCourier(courierId)
                    .statusCode(anyOf(is(SC_OK), is(SC_ACCEPTED)));
                System.out.println("Курьер с ID " + courierId + " успешно удален");
            } catch (Exception e) {
                System.out.println("Ошибка при удалении курьера с ID " + courierId + ": " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Создание нового курьера")
    @Description("Проверка, что курьер может быть создан с валидными данными и возвращает ok: true")
    public void createCourierWithValidData() {
        courier = CourierGenerator.getRandomCourier();
        
        courierClient.createCourier(courier)
                .statusCode(SC_CREATED)
                .body("ok", is(true));
        
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        int tempCourierId = courierClient.loginCourier(credentials)
                .statusCode(SC_OK)
                .extract().path("id");
        this.courierId = tempCourierId; 

        assertTrue("Идентификатор курьера должен быть больше 0", tempCourierId > 0);
    }

    @Test
    @DisplayName("Создание дубликата курьера")
    @Description("Проверка, что создание курьера с существующим логином возвращает ошибку 409")
    public void createDuplicateCourierShouldReturnError() {
        courier = CourierGenerator.getRandomCourier(); 
        
        courierClient.createCourier(courier) 
                .statusCode(SC_CREATED);

        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        this.courierId = courierClient.loginCourier(credentials)
                .statusCode(SC_OK)
                .extract().path("id");
        
        CourierModel duplicateCourier = new CourierModel( 
            courier.getLogin(), 
            "differentPassword", 
            "differentName"
        );
        
        courierClient.createCourier(duplicateCourier) 
                .statusCode(SC_CONFLICT)
                .body("message", is("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка, что создание курьера без логина возвращает ошибку 400")
    public void createCourierWithoutLoginShouldReturnError() {
        courier = CourierGenerator.getCourierWithoutLogin(); 
        
        courierClient.createCourier(courier) 
                .statusCode(SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка, что создание курьера без пароля возвращает ошибку 400")
    public void createCourierWithoutPasswordShouldReturnError() {
        courier = CourierGenerator.getCourierWithoutPassword(); 
        
        courierClient.createCourier(courier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без имени")
    @Description("Проверка, что создание курьера без имени все равно работает")
    public void createCourierWithoutFirstNameShouldWork() {
        courier = CourierGenerator.getCourierWithoutFirstName(); 
        
        courierClient.createCourier(courier)
                .statusCode(SC_CREATED)
                .body("ok", is(true));

        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        this.courierId = courierClient.loginCourier(credentials)
                .statusCode(SC_OK)
                .extract().path("id");

        assertTrue("Идентификатор курьера должен быть больше 0", this.courierId > 0);
    }
}