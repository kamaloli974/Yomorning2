package com.yomorning.lavafood.yomorning.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by KAMAL OLI on 01/09/2017.
 */
public class RailRestroVendorsModel implements Parcelable {
    private int orderTiming,minimumAmount,deliveryCost,cancelCutOffTime;
    private String companyName,vendorId;
    private String vendorsName;
    private String openingTime;
    private String closingTime;
    private String address;
    private String city;
    private String state;
    private String menuTypes;
    private String mealTypes;
    private String mobileNumber;
    private String vendorImageUrl;
    private String emailAddress;
    private boolean dataFetched,isActive;
    private String foodTypes;

    public void setFoodTypes(String foodTypes){
        this.foodTypes=foodTypes;
    }

    public String getVendorId() {
        return vendorId;
    }

    public int getOrderTiming() {
        return orderTiming;
    }

    public int getMinimumAmount() {
        return minimumAmount;
    }

    public int getDeliveryCost() {
        return deliveryCost;
    }

    public int getCancelCutOffTime() {
        return cancelCutOffTime;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getVendorsName() {
        return vendorsName;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getMenuTypes() {
        return menuTypes;
    }

    public String getMealTypes() {
        return mealTypes;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getVendorImageUrl() {
        return vendorImageUrl;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public boolean isDataFetched() {
        return dataFetched;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public void setOrderTiming(int orderTiming) {
        this.orderTiming = orderTiming;
    }

    public void setMinimumAmount(int minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public void setDeliveryCost(int deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public void setCancelCutOffTime(int cancelCutOffTime) {
        this.cancelCutOffTime = cancelCutOffTime;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setVendorsName(String vendorsName) {
        this.vendorsName = vendorsName;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setMenuTypes(String menuTypes) {
        this.menuTypes = menuTypes;
    }

    public void setMealTypes(String mealTypes) {
        this.mealTypes = mealTypes;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setVendorImageUrl(String vendorImageUrl) {
        this.vendorImageUrl = vendorImageUrl;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setDataFetched(boolean dataFetched) {
        this.dataFetched = dataFetched;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    public String getFoodTypes() {
        return foodTypes;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.orderTiming);
        dest.writeInt(this.minimumAmount);
        dest.writeInt(this.deliveryCost);
        dest.writeInt(this.cancelCutOffTime);
        dest.writeString(this.companyName);
        dest.writeString(this.vendorId);
        dest.writeString(this.vendorsName);
        dest.writeString(this.openingTime);
        dest.writeString(this.closingTime);
        dest.writeString(this.address);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.menuTypes);
        dest.writeString(this.mealTypes);
        dest.writeString(this.mobileNumber);
        dest.writeString(this.vendorImageUrl);
        dest.writeString(this.emailAddress);
        dest.writeByte(this.dataFetched ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
        dest.writeString(this.foodTypes);
    }
    public RailRestroVendorsModel() {
        super();
    }
    protected RailRestroVendorsModel(Parcel in) {
        this.orderTiming = in.readInt();
        this.minimumAmount = in.readInt();
        this.deliveryCost = in.readInt();
        this.cancelCutOffTime = in.readInt();
        this.companyName = in.readString();
        this.vendorId = in.readString();
        this.vendorsName = in.readString();
        this.openingTime = in.readString();
        this.closingTime = in.readString();
        this.address = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.menuTypes = in.readString();
        this.mealTypes = in.readString();
        this.mobileNumber = in.readString();
        this.vendorImageUrl = in.readString();
        this.emailAddress = in.readString();
        this.dataFetched = in.readByte() != 0;
        this.isActive = in.readByte() != 0;
        this.foodTypes = in.readString();
    }

    public static final Parcelable.Creator<RailRestroVendorsModel> CREATOR = new Parcelable.Creator<RailRestroVendorsModel>() {
        @Override
        public RailRestroVendorsModel createFromParcel(Parcel source) {
            return new RailRestroVendorsModel(source);
        }

        @Override
        public RailRestroVendorsModel[] newArray(int size) {
            return new RailRestroVendorsModel[size];
        }
    };
}
