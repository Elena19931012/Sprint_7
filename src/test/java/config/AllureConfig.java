package config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.BeforeClass;

public class AllureConfig {
    
    @BeforeClass
    public static void configAllure() {
        RestAssured.filters(
            new AllureRestAssured(),
            new RequestLoggingFilter(),
            new ResponseLoggingFilter()
        );
    }
}
