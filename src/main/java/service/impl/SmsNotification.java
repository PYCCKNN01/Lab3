package service.impl;

import service.NotificationService;

public class SmsNotification implements NotificationService { //Класс реализует методы интерфейса
    @Override
    public void sendNotification(String message, String recipient) {
        // Проверка коррекстности номера
        if (recipient == null || (!recipient.startsWith("+7") && !recipient.startsWith("8")) || recipient.length() < 11) {
            System.out.println("Некорректный номер телефона: " + recipient + ". SMS не отправлено.");
            return;
        }
        System.out.println("[SMS] На номер " + recipient + " отправлено сообщение: " + message);
    }
}