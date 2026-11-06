package model;

public class UserSubscription extends Subscription {

    public UserSubscription(String ownerName, double basePrice, String tier) {
        super(ownerName, basePrice, tier);
    }

    @Override
    public double calculateBillingPrice() {
        //Базовая цена с учетом тарифа
        double totalPrice = getBasePrice() * getTierCoefficient();

        //Проходим по всем добавленным пропускам
        for (FanZonePass option : getAppliedOptions()) {
            totalPrice += option.getPrice();
        }

        return totalPrice;
    }
}