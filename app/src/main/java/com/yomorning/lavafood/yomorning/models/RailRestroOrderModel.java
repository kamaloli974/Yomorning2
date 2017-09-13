package com.yomorning.lavafood.yomorning.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KAMAL OLI on 11/09/2017.
 */

public class RailRestroOrderModel implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.model, flags);
        dest.writeInt(this.itemCount);
        dest.writeInt(this.itemId);
    }

    public RailRestroOrderModel() {
    }

    protected RailRestroOrderModel(Parcel in) {
        this.model = in.readParcelable(RailRestroMenuModel.class.getClassLoader());
        this.itemCount = in.readInt();
        this.itemId = in.readInt();
    }

    public static final Parcelable.Creator<RailRestroOrderModel> CREATOR = new Parcelable.Creator<RailRestroOrderModel>() {
        @Override
        public RailRestroOrderModel createFromParcel(Parcel source) {
            return new RailRestroOrderModel(source);
        }

        @Override
        public RailRestroOrderModel[] newArray(int size) {
            return new RailRestroOrderModel[size];
        }
    };
}
