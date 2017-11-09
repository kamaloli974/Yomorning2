package com.yomorning.lavafood.yomorning.models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by KAMAL OLI on 03/09/2017.
 */
public class ServiceSelectionChoiceModel{
    private int choiceId;
    private String label;
    private Bitmap backgroundImage;
    private int imageId;
    private Drawable drawable;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getChoiceId() {
        return choiceId;
    }

    public String getLabel() {
        return label;
    }

    public Bitmap getBackgroundImage() {
        return backgroundImage;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setBackgroundImage(Bitmap backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
    public void setImage(Drawable drawable){
        this.drawable=drawable;
    }
    public Drawable getImage(){
        return this.drawable;
    }
}
