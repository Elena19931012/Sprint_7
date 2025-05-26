package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.CourierCreate;
import models.CourierCredentials;

import static io.restassured.RestAssured.given;
import static config.ApiConfig.*;

public class CourierClient {

    @Step("Создание курьера")
    public ValidatableResponse createCourier(CourierCreate courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_CREATE)
                .then();
    }

    @Step("Авторизация курьера")
    public ValidatableResponse loginCourier(CourierCredentials credentials) {
        return given()
                .header("Content-type", "application/json")
                .body(credentials)
                .when()
                .post(COURIER_LOGIN)
                .then();
    }

    @Step("Удаление курьера по id: {courierId}")
    public ValidatableResponse deleteCourier(int courierId) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(COURIER_DELETE.replace("{id}", String.valueOf(courierId)))
                .then();
    }
}
