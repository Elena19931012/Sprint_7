package api;

import config.ApiClientConfig;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.CourierCredentials;
import models.CourierModel;

import static config.ApiConfig.*;
import static io.restassured.RestAssured.given;

public class CourierClient {

    @Step("Создание курьера")
    public ValidatableResponse createCourier(CourierModel courierModel) { 
        return given()
                .spec(ApiClientConfig.getBaseRequestSpec()) 
                .body(courierModel) 
                .when()
                .post(COURIER_CREATE)
                .then();
    }

    @Step("Авторизация курьера")
    public ValidatableResponse loginCourier(CourierCredentials credentials) {
        return given()
                .spec(ApiClientConfig.getBaseRequestSpec()) 
                .body(credentials)
                .when()
                .post(COURIER_LOGIN)
                .then();
    }

    @Step("Удаление курьера по id: {courierId}")
    public ValidatableResponse deleteCourier(int courierId) {
        return given()
                .spec(ApiClientConfig.getBaseRequestSpec()) 
                .when()
                .delete(COURIER_DELETE.replace("{id}", String.valueOf(courierId)))
                .then();
    }
}