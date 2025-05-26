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
import org.junit.Before;
import org.junit.Test;
import utils.CourierGenerator;

import static org.hamcrest.Matchers.*;

@Epic("Курьеры")
@Feature("Авторизация курьера")
public class CourierLoginTest extends BaseTest {

    private final CourierClient courierClient = new CourierClient();
    private CourierCreate courier;
    private int courierId;

    @Before
    @Step("Создание тестового курьера для проверки авторизации")
    public void setUp() {
        courier = CourierGenerator.getRandomCourier();
        courierClient.createCourier(courier);
    }

    @After
    @Step("Удаление тестовых данных")
    public void tearDown() {
        if (courierId != 0) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Авторизация с валидными данными")
    @Description("Проверка, что курьер может авторизоваться с валидными данными и получает ID")
    public void loginWithValidCredentials() {
        CourierCredentials credentials = courier.toCredentials();
        
        courierId = courierClient.loginCourier(credentials)
                .statusCode(200)
                .body("$", hasKey("id"))
                .extract().path("id");
    }

    @Test
    @DisplayName("Авторизация без поля логин")
    @Description("Проверка, что авторизация без поля логин возвращает ошибку 400")
    public void loginWithoutLoginShouldReturnError() {
        CourierCredentials credentials = new CourierCredentials(null, courier.getPassword());
        
        courierClient.loginCourier(credentials)
                .statusCode(400)
                .body("message", is("Недостаточно данных для входа"));
        
        courierId = courierClient.loginCourier(courier.toCredentials()).extract().path("id");
    }

    @Test
    @DisplayName("Авторизация без поля пароль")
    @Description("Проверка, что авторизация без поля пароль возвращает ошибку 400")
    public void loginWithoutPasswordShouldReturnError() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), null);
        
        courierClient.loginCourier(credentials)
                .statusCode(400)
                .body("message", is("Недостаточно данных для входа"));
        
        courierId = courierClient.loginCourier(courier.toCredentials()).extract().path("id");
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверка, что авторизация с неверным паролем возвращает ошибку 404")
    public void loginWithWrongPasswordShouldReturnError() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), "неверныйПароль");
        
        courierClient.loginCourier(credentials)
                .statusCode(404)
                .body("message", is("Учетная запись не найдена"));
        
        courierId = courierClient.loginCourier(courier.toCredentials()).extract().path("id");
    }

    @Test
    @DisplayName("Авторизация с несуществующим курьером")
    @Description("Проверка, что авторизация с несуществующим курьером возвращает ошибку 404")
    public void loginWithNonExistentCourierShouldReturnError() {
        CourierCredentials credentials = new CourierCredentials("несуществующийЛогин", "любойПароль");
        
        courierClient.loginCourier(credentials)
                .statusCode(404)
                .body("message", is("Учетная запись не найдена"));
        
        courierId = courierClient.loginCourier(courier.toCredentials()).extract().path("id");
    }
}
