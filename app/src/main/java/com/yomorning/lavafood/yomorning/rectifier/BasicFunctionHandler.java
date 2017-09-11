package com.yomorning.lavafood.yomorning.rectifier;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

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
}
