package utils;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RandomGenerator {
    private static final Faker faker = new Faker(new Locale("ru"));
    
    public static String generateRandomString(int length) {
        return faker.regexify("[A-Za-z0-9]{" + length + "}");
    }

    public static String generateRandomPhone() {
        return "+7" + faker.numerify("##########");
    }

    public static String generateRandomNumbers(int length) {
        return faker.numerify("#".repeat(length));
    }

    public static String generateFutureDate(int daysToAdd) {
        return LocalDate.now()
                .plusDays(daysToAdd)
                .format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static int generateRandomInt(int min, int max) {
        return faker.number().numberBetween(min, max);
    }
    
    public static String generateName() {
        return faker.name().fullName();
    }
    
    public static String generateEmail() {
        return faker.internet().emailAddress();
    }
    
    public static String generateAddress() {
        return faker.address().fullAddress();
    }
}