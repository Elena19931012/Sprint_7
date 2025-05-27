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
import org.junit.Before;
import org.junit.Test;
import utils.CourierGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Epic("Курьеры")
@Feature("Авторизация курьера")
public class CourierLoginTest extends BaseTest {

    private final CourierClient courierClient = new CourierClient();
    private CourierModel courier; 
    private int courierId;

    @Before
    @Step("Создание тестового курьера для проверки авторизации")
    public void setUp() {
        courier = CourierGenerator.getRandomCourier();
        courierClient.createCourier(courier); 
        try {
            CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
            this.courierId = courierClient.loginCourier(credentials).extract().path("id");
        } catch (Exception e) {
            System.out.println("Не удалось получить ID курьера в setUp: " + e.getMessage());
        }
    }

    @After
    @Step("Удаление тестовых данных")
    public void tearDown() {
        if (courierId == 0 && courier != null && courier.getLogin() != null && courier.getPassword() != null) {
            try {
                CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
                courierId = courierClient.loginCourier(credentials)
                        .extract().path("id");
                System.out.println("Получен ID курьера для очистки в tearDown: " + courierId);
            } catch (Exception e) {
                System.out.println("Не удалось получить ID курьера для очистки в tearDown: " + e.getMessage());
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
    @DisplayName("Авторизация с валидными данными")
    @Description("Проверка, что курьер может авторизоваться с валидными данными и получает ID")
    public void loginWithValidCredentials() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        
        int responseId = courierClient.loginCourier(credentials)
                .statusCode(SC_OK)
                .body("$", hasKey("id"))
                .extract().path("id");
        
        if (this.courierId == 0) {
            this.courierId = responseId;
        }
    }

    @Test
    @DisplayName("Авторизация без поля логин")
    @Description("Проверка, что авторизация без поля логин возвращает ошибку 400")
    public void loginWithoutLoginShouldReturnError() {
        CourierCredentials credentials = new CourierCredentials(null, courier.getPassword());
        
        courierClient.loginCourier(credentials)
                .statusCode(SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без поля пароль")
    @Description("Проверка, что авторизация без поля пароль возвращает ошибку 400")
    public void loginWithoutPasswordShouldReturnError() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), null);
        
        courierClient.loginCourier(credentials)
                .statusCode(SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверка, что авторизация с неверным паролем возвращает ошибку 404")
    public void loginWithWrongPasswordShouldReturnError() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), "неверныйПароль123");
        
        courierClient.loginCourier(credentials)
                .statusCode(SC_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с несуществующим курьером")
    @Description("Проверка, что авторизация с несуществующим курьером возвращает ошибку 404")
    public void loginWithNonExistentCourierShouldReturnError() {
        CourierCredentials credentials = new CourierCredentials("несуществующийЛогин123", "любойПароль123");
        
        courierClient.loginCourier(credentials)
                .statusCode(SC_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
    }
}