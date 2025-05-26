package config;

public class ApiConfig {
    // Base URL для Яндекс.Самокат API
    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    
    // API эндпоинты для работы с курьерами
    public static final String COURIER_CREATE = "/api/v1/courier";
    public static final String COURIER_LOGIN = "/api/v1/courier/login";
    public static final String COURIER_DELETE = "/api/v1/courier/{id}";
    
    // API эндпоинты для работы с заказами
    public static final String ORDERS_CREATE = "/api/v1/orders";
    public static final String ORDERS_TRACK = "/api/v1/orders/track";
    public static final String ORDERS_ACCEPT = "/api/v1/orders/accept/{id}";
    public static final String ORDERS_FINISH = "/api/v1/orders/finish/{id}";
    public static final String ORDERS_CANCEL = "/api/v1/orders/cancel";
    
    public static final String ORDERS_COUNT = "/api/v1/courier/{id}/ordersCount";
    
    // Другие эндпоинты
    public static final String STATIONS_SEARCH = "/api/v1/stations/search";
    public static final String PING = "/api/v1/ping";
}
