import billing.Billing;
import exception.DuplicateSubscriptionException;
import exception.InvalidSubscriptionException;
import exception.SubscriptionNotFoundException;
import model.CorporateSubscription;
import model.FanZonePass;
import model.Subscription;
import model.UserSubscription;
import service.impl.EmailNotification;

public class Main {
    public static void main(String[] args) {
        Billing billing = new Billing(new EmailNotification());

        // --- Подготовка успешных данных ---
        //Различные опции
        FanZonePass fastTrack = new FanZonePass("Fast Track", 500.0);
        FanZonePass vipBar = new FanZonePass("VIP Bar", 1500.0);

        // Успешная подписка (билет) 1 (Физлицо, тариф FAN_ZONE)
        Subscription sub1 = new UserSubscription("Иван Иванов", 2000.0, "FAN_ZONE");
        sub1.addOption(fastTrack);
        sub1.addOption(vipBar);

        // Успешная подписка (билет) 2 (Корпоратив на 5 лицензий, тариф DANCE_FLOOR)
        Subscription sub2 = new CorporateSubscription("ООО 'Вектор'", 3000.0, "DANCE_FLOOR", 5);
        sub2.addOption(fastTrack);

        Subscription sub3 = new UserSubscription("Дима Долинин", 2000.0, "FAN_ZONE");
        Subscription sub4 = new UserSubscription("Вова Долинин", 2000.0, "FAN_ZONE");


        System.out.println("");

        // Регистрация в базе
        try {
            billing.registerSubscription("ivanov@mail.ru", sub1);
            billing.registerSubscription("corporate@vector.com", sub2);
            billing.registerSubscription("dolinin@mail.ru", sub3);
            billing.registerSubscription("dolinin@mail.ru", sub4);
        } catch (DuplicateSubscriptionException e) {
            System.out.println("[ОШИБКА РЕГИСТРАЦИИ]: " + e.getMessage());
            System.err.println("[ОШИБКА РЕГИСТРАЦИИ]: " + e.getMessage());
        }

        // Сценарий 1: Успех
        System.out.println("\n--- Сценарий 1: Успешная оплата ---");
        try {
            billing.processPaymentByEmail("ivanov@mail.ru");
            billing.processPaymentByEmail("corporate@vector.com");
        } catch (SubscriptionNotFoundException e) {
            System.out.println("Эта строчка не должна выполниться: " + e.getMessage());
        }

        // Сценарий 2: Проверяемое исключение (SubscriptionNotFoundException)
        System.out.println("\n--- Сценарий 2: Попытка оплаты несуществующего Email ---");
        try {
            billing.processPaymentByEmail("fake@mail.com");
        } catch (SubscriptionNotFoundException e) {
            System.out.println("[ПЕРЕХВАЧЕНО ИСКЛЮЧЕНИЕ]: " + e.getMessage());
            System.err.println("[ПЕРЕХВАЧЕНО ИСКЛЮЧЕНИЕ]: " + e.getMessage());
        }

        // Сценарий 3: Непроверяемое исключение (InvalidSubscriptionException)
        System.out.println("\n--- Сценарий 3.1: Создание некорректной подписки (отрицательная цена) ---");
        try {
            Subscription badSub = new UserSubscription("Петр Петров", -100.0, "FAN_ZONE");
        } catch (InvalidSubscriptionException e) {
            System.out.println("[ПЕРЕХВАЧЕНО ИСКЛЮЧЕНИЕ]: " + e.getMessage());
            System.err.println("[ПЕРЕХВАЧЕНО ИСКЛЮЧЕНИЕ]: " + e.getMessage());
        }

        System.out.println("\n--- Сценарий 3.2: Создание некорректной подписки (пустое имя) ---");
        try {
            Subscription badSub2 = new UserSubscription("   ", 500.0, "FAN_ZONE");
        } catch (InvalidSubscriptionException e) {
            System.out.println("[ПЕРЕХВАЧЕНО ИСКЛЮЧЕНИЕ]: " + e.getMessage());
            System.err.println("[ПЕРЕХВАЧЕНО ИСКЛЮЧЕНИЕ]: " + e.getMessage());
        }


        System.out.println();
        Billing.printGlobalStats();
        System.out.println("");
    }
}