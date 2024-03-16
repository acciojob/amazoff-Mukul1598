package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id = id;
        this.deliveryTime = convertDeliveryTimeToMinutes(deliveryTime);
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }
    private int convertDeliveryTimeToMinutes(String deliveryTime) {
        String[] timeSplit = deliveryTime.split(":");
        int hours = Integer.parseInt(timeSplit[0]);
        int minutes = Integer.parseInt(timeSplit[1]);
        return hours * 60 + minutes;
    }
}
