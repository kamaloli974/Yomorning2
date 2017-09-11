package com.yomorning.lavafood.yomorning.models;

import android.graphics.Bitmap;
/**
 * Created by KAMAL OLI on 03/09/2017.
 */
public class ServiceSelectionChoiceModel{
    private int choiceId;
    private String label;
    private Bitmap backgroundImage;
    private int imageId;

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


}
