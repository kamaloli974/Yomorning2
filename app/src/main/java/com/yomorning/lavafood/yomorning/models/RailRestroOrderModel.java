package com.yomorning.lavafood.yomorning.models;

/**
 * Created by KAMAL OLI on 11/09/2017.
 */

public class RailRestroOrderModel {
    private RailRestroMenuModel model;
    private int itemCount;
    private int itemId;

    public RailRestroMenuModel getModel() {
        return model;
    }

    public int getItemCount() {
        return itemCount;
    }

    public int getItemId() {
        return itemId;
    }

    public void setModel(RailRestroMenuModel model) {
        this.model = model;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
