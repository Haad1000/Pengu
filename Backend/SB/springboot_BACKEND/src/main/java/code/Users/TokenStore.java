package code.Users;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenStore {

    private Map<String, User> tokenUserMap = new HashMap<>();

    public void storeUser(String token, User user) {
        tokenUserMap.put(token, user);
    }

    public User getUser(String token) {
        return tokenUserMap.get(token);
    }

    public void removeUser(String token) {
        tokenUserMap.remove(token);
    }
}

