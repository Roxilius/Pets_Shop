package com.example.server.services.email;

import java.util.Map;

public interface ThymeleafService {
    String createContext(String template, Map<String, Object> variables);
}
