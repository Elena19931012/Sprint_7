package tests;

import config.AllureConfig;
import config.ApiConfig;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.BeforeClass;

public abstract class BaseTest {
    
    @BeforeClass
    @Step("Настройка базового URL и фильтров REST-assured")
    public static void globalSetup() {
        RestAssured.baseURI = ApiConfig.BASE_URL;
        AllureConfig.configAllure();
        
        // Настройка фильтров для логирования запросов и ответов
        RestAssured.filters(
            new AllureRestAssured(), 
            new RequestLoggingFilter(),
            new ResponseLoggingFilter()
        );
    }
}
