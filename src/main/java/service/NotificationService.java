package service;

public interface NotificationService {
    void sendNotification(String message, String recipient);
}

//Интерфейс должен уметь реализовывать класс, который в него дают.
//Классы sms и email используя этот интерфейс выводят сообщение об успешной/неуспешной отправке.