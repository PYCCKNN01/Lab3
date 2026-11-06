package model;

public class FanZonePass {
    private final String name;
    private double price;

    public FanZonePass(String name, double price) {
        this.name = name;
        if (price < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
        this.price = price;
    }

    public double getPrice() { return price; }
}