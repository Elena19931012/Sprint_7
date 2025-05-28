package config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.Filter;
import org.junit.BeforeClass;

public class AllureListener {
    
    public static Filter withCustomTemplates() {
        return new AllureRestAssured()
            .setRequestTemplate("request.ftl")
            .setResponseTemplate("response.ftl");
    }
    
    @BeforeClass
    public static void setup() {
        // Настройка стандартного фильтра Allure для RestAssured
        io.restassured.RestAssured.filters(new AllureRestAssured());
    }
}
