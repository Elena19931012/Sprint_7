package utils;

import config.ApiConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class RestAssuredSetup {
    
    public static void setup() {
        RestAssured.baseURI = ApiConfig.BASE_URL;
        RestAssured.filters(
            new AllureRestAssured(), 
            new RequestLoggingFilter(),
            new ResponseLoggingFilter()
        );
    }
}
