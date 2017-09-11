package com.yomorning.lavafood.yomorning.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by KAMAL OLI on 01/09/2017.
 */

public class RailRestroMenuModel implements Parcelable {
    private int categoryId,itemId,itemType,status;
    private String categoryName,openingTime,closingTime,itemName,timeSlot,foodCategory,foodItemDescription;
    private double serviceTax,vat,sellingPrice;
    private boolean isActive;
    private int basePrice;

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public void setServiceTax(double serviceTax) {
        this.serviceTax = serviceTax;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public void setFoodItemDescription(String foodItemDescription){
        this.foodItemDescription=foodItemDescription;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getItemType() {
        return itemType;
    }

    public int getStatus() {
        return status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public String getItemName() {
        return itemName;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public double getServiceTax() {
        return serviceTax;
    }

    public double getVat() {
        return vat;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getFoodItemDescription() {
        return foodItemDescription;
    }

    public int getBasePrice(){
        return basePrice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.categoryId);
        dest.writeInt(this.itemId);
        dest.writeInt(this.itemType);
        dest.writeInt(this.status);
        dest.writeString(this.categoryName);
        dest.writeString(this.openingTime);
        dest.writeString(this.closingTime);
        dest.writeString(this.itemName);
        dest.writeString(this.timeSlot);
        dest.writeString(this.foodCategory);
        dest.writeString(this.foodItemDescription);
        dest.writeDouble(this.serviceTax);
        dest.writeDouble(this.vat);
        dest.writeDouble(this.sellingPrice);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
        dest.writeInt(this.basePrice);
    }

    public RailRestroMenuModel() {
    }

    protected RailRestroMenuModel(Parcel in) {
        this.categoryId = in.readInt();
        this.itemId = in.readInt();
        this.itemType = in.readInt();
        this.status = in.readInt();
        this.categoryName = in.readString();
        this.openingTime = in.readString();
        this.closingTime = in.readString();
        this.itemName = in.readString();
        this.timeSlot = in.readString();
        this.foodCategory = in.readString();
        this.foodItemDescription = in.readString();
        this.serviceTax = in.readDouble();
        this.vat = in.readDouble();
        this.sellingPrice = in.readDouble();
        this.isActive = in.readByte() != 0;
        this.basePrice = in.readInt();
    }

    public static final Parcelable.Creator<RailRestroMenuModel> CREATOR = new Parcelable.Creator<RailRestroMenuModel>() {
        @Override
        public RailRestroMenuModel createFromParcel(Parcel source) {
            return new RailRestroMenuModel(source);
        }
        @Override
        public RailRestroMenuModel[] newArray(int size) {
            return new RailRestroMenuModel[size];
        }
    };

}
