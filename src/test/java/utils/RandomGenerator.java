package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class RandomGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new Random();

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static String generateRandomPhone() {
        return "+7" + generateRandomNumbers(10);
    }

    public static String generateRandomNumbers(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String generateFutureDate(int daysToAdd) {
        return LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static int generateRandomInt(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }
}
