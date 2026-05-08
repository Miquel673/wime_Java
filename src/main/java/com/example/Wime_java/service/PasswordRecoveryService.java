package com.example.Wime_java.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class PasswordRecoveryService {

    private static final int TOKEN_MINUTES = 30;

    private final Map<String, RecoveryData> tokens = new ConcurrentHashMap<>();

    public String createToken(String email) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, new RecoveryData(email, LocalDateTime.now().plusMinutes(TOKEN_MINUTES)));
        return token;
    }

    public String getEmailIfTokenValid(String token) {
        RecoveryData data = tokens.get(token);
        if (data == null) {
            return null;
        }

        if (LocalDateTime.now().isAfter(data.expireAt())) {
            tokens.remove(token);
            return null;
        }

        return data.email();
    }

    public String consumeToken(String token) {
        String email = getEmailIfTokenValid(token);
        if (email != null) {
            tokens.remove(token);
        }
        return email;
    }

    private record RecoveryData(String email, LocalDateTime expireAt) {
    }
} 
    
