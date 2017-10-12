package com.yomorning.lavafood.yomorning.models;

/**
 * Created by KAMAL OLI on 27/09/2017.
 */

public class OrderHistoryModel {
    String names;
    String totalPrice;
    String orderedTime;
    String deliveryTime;

    public String getNames() {
        return names;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getOrderedTime() {
        return orderedTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderedTime(String orderedTime) {
        this.orderedTime = orderedTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

}
