package code.Users;

import java.util.UUID;

public class TokenUtility {

    public static String generateNewToken() {
        // Generates a random UUID
        return UUID.randomUUID().toString();
    }
}

