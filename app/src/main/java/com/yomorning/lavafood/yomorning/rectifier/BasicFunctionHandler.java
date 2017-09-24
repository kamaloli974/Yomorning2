package com.yomorning.lavafood.yomorning.rectifier;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by KAMAL OLI on 05/08/2017.
 */

public class BasicFunctionHandler {
    private Context context;
    public BasicFunctionHandler(){}
    public BasicFunctionHandler(Context c){
        context=c;
    }
    public void showAlertDialog(String title,String body){
        final AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(body);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }
   public boolean isConnectedToNetwork(){
        ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnectedOrConnecting()){
            return true;
        }
        else
            return false;
    }

    public String getHashValue(byte[] byteArray,String algorithName) {
        String hashValue="";

        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(algorithName);
            messageDigest.update(byteArray);
            byte[] digestedByte=messageDigest.digest();
            StringBuilder builder=new StringBuilder();
            for(byte b: digestedByte){
                builder.append(String.format("%02x",b));
            }
            hashValue=builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashValue;
    }
    public boolean isEmailValid(String email){
        return true;
    }
    public boolean isPasswordLengthValid(String password){
        if(password.length()>=8){
            return true;
        }
        else{
            return false;
        }
    }

    public String getRandomValue(){
        String randomValue;
        randomValue=((int)(1000000*Math.random()))+"";
        return randomValue;
    }

}
