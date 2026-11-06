package service.impl;

import service.NotificationService;

public class EmailNotification implements NotificationService { //Класс реализует методы интерфейса
    @Override
    public void sendNotification(String message, String recipient) {
        // Проверка корректности email
        if (recipient == null || !recipient.contains("@") || !recipient.contains(".")) {
            System.out.println("Некорректный email: " + recipient + ". Письмо не отправлено.");
            return;
        }
        System.out.println("[EMAIL] На адрес " + recipient + " отправлено письмо: " + message);
    }
}