package api;

import config.ApiClientConfig;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.Order;
import models.OrderCancelRequest;

import static io.restassured.RestAssured.given;
import static config.ApiConfig.*;

public class OrderClient {

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(ApiClientConfig.getBaseRequestSpec())
                .body(order)
                .when()
                .post(ORDERS_CREATE)
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrderList() {
        return given()
                .spec(ApiClientConfig.getBaseRequestSpec())
                .when()
                .get(ORDERS_CREATE)
                .then();
    }

    @Step("Получение заказа по номеру трека: {trackNumber}")
    public ValidatableResponse getOrderByTrackNumber(int trackNumber) {
        return given()
                .spec(ApiClientConfig.getBaseRequestSpec())
                .queryParam("t", trackNumber)
                .when()
                .get(ORDERS_TRACK)
                .then();
    }
    
    @Step("Отмена заказа")
    public ValidatableResponse cancelOrder(int trackNumber) {
        OrderCancelRequest cancelRequest = new OrderCancelRequest(trackNumber);
    
        return given()
                .spec(ApiClientConfig.getBaseRequestSpec())
                .log().all()
                .body(cancelRequest) 
                .when()
                .put(ORDERS_CANCEL)
                .then()
                .log().all(); 
    }
}
