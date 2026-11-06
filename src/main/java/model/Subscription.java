package model;

import exception.InvalidSubscriptionException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class Subscription {
    private final String ownerName;
    private final double basePrice;
    private final String tier;

    // Список дополнительных опций (наши пропуска)
    private final List<FanZonePass> appliedOptions = new ArrayList<>();

    // Регулярное выражение: проверяет, что строка содержит хотя бы один видимый символ
    private static final Pattern EMPTY_PATTERN = Pattern.compile("^\\s*$");

    public Subscription(String ownerName, double basePrice, String tier) {
        if (basePrice <= 0) {
            throw new InvalidSubscriptionException("Базовая стоимость подписки должна быть больше нуля! Передано: " + basePrice);
        }
        if (ownerName == null || EMPTY_PATTERN.matcher(ownerName).matches()) {
            throw new InvalidSubscriptionException("Имя владельца подписки не может быть пустым!");
        }
        this.ownerName = ownerName;
        this.basePrice = basePrice;
        this.tier = tier;
    }

    public void addOption(FanZonePass option) {
        if (option == null) {
            throw new IllegalArgumentException("Нельзя добавить пустую опцию (null)!");
        }
        this.appliedOptions.add(option);
    }

    // Вспомогательный метод для получения коэффициента тарифа
    protected double getTierCoefficient() {
        return switch (tier) {
            case "DANCE_FLOOR" -> 1.0;
            case "FAN_ZONE" -> 1.6;
            case "VIP_LOGE" -> 3.5;
            default -> 1.0;
        };
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public List<FanZonePass> getAppliedOptions() {
        return appliedOptions;
    }

    public abstract double calculateBillingPrice();
}