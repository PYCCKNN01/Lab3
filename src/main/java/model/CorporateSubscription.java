package model;

public class CorporateSubscription extends Subscription {
    private final int activeLicensesCount;

    public CorporateSubscription(String ownerName, double basePrice, String tier, int activeLicensesCount) {
        super(ownerName, basePrice, tier);
        if (activeLicensesCount < 1) {
            throw new IllegalArgumentException("Количество лицензий не может быть меньше 1!");
        }
        this.activeLicensesCount = activeLicensesCount;
    }

    @Override
    public double calculateBillingPrice() {
        //Базовая цена с учетом тарифа и количества лицензий
        double totalPrice = (getBasePrice() * getTierCoefficient()) * activeLicensesCount;

        //Проходим по всем добавленным пропускам
        for (FanZonePass option : getAppliedOptions()) {
            totalPrice += (option.getPrice() * activeLicensesCount);
        }

        return totalPrice;
    }
}

