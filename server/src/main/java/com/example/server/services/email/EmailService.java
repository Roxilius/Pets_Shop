package com.example.server.services.email;

public interface EmailService {
    void sendSimpleMessage(String  to,String subject, String text);
    void emailRegistration(String to, String name);
}
