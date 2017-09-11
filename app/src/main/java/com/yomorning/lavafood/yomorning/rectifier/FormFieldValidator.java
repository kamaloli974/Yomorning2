package com.yomorning.lavafood.yomorning.rectifier;

/**
 * Created by KAMAL OLI on 03/08/2017.
 */

public class FormFieldValidator {
    public String[] checkEmptyness(String[] array){
        for(int i=0;i<array.length;i++){
            array[i]=array[i].trim();
        }
        return array;
    }
    public String trimer(String value){
        return value.trim();
    }
}
