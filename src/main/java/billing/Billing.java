package billing;

import exception.DuplicateSubscriptionException;
import exception.InvalidEmailException;
import exception.SubscriptionNotFoundException;
import model.Subscription;
import service.NotificationService;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Сервисный слой (имитация базы данных).
 * Хранит билеты в HashMap и управляет оплатой.
 */
public class Billing {

    //Статистика
    private static int totalSubscribersCount = 0;
    private static double totalRevenue = 0.0;

    //Внедрение зависимости
    private final NotificationService notificationService;

    //База данных пользователей. Ключ - email, Значение - подписка (билет).
    private final Map<String, Subscription> database = new HashMap<>();

    //Регулярное выражение для простой проверки Email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public Billing(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Регистрация нового билета в базе данных.
     * Выбрасывает DuplicateSubscriptionException, если email уже занят.
     */
    public void registerSubscription(String email, Subscription sub) throws DuplicateSubscriptionException {
        //Проверка формата email (Непроверяемое исключение)
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailException("Некорректный формат email: " + email);
        }

        //Проверка на дубликат (Проверяемое исключение)
        if (database.containsKey(email)) {
            throw new DuplicateSubscriptionException("Пользователь с email " + email + " уже зарегистрирован в системе!");
        }

        //Сохранение в HashMap
        database.put(email, sub);
        System.out.println("[INFO] Подписка для " + email + " успешно зарегистрирована.");
    }

    /**
     * Проведение оплаты по Email.
     * Выбрасывает SubscriptionNotFoundException, если пользователя нет в базе.
     */
    public void processPaymentByEmail(String email) throws SubscriptionNotFoundException {
        //Пытаемся достать подписку из HashMap
        Subscription sub = database.get(email);

        //Кидаем проверяемое исключение
        if (sub == null) {
            throw new SubscriptionNotFoundException("Пользователь с email " + email + " не найден в базе!");
        }

        //Считаем цену
        double finalPrice = sub.calculateBillingPrice();

        //Обновляем глобальную статику
        totalSubscribersCount++;
        totalRevenue += finalPrice;

        //Отправляем чек
        String message = "Оплата прошла успешно. Списано: " + finalPrice + " руб. за " + sub.getOwnerName();
        notificationService.sendNotification(message, email);
    }

    public static void printGlobalStats() {
        System.out.println("=== ГЛОБАЛЬНАЯ СТАТИСТИКА ===");
        System.out.println("Количество оплат: " + totalSubscribersCount);
        System.out.println("Общая выручка: " + totalRevenue + " руб.");
        System.out.println("=============================");
    }
}