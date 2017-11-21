package com.yomorning.lavafood.yomorning.bottomsheets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.rectifier.BasicFunctionHandler;

/**
 * Created by KAMAL OLI on 16/11/2017.
 */

public class FBGooglePassword extends BottomSheetDialogFragment implements View.OnClickListener {
    Context context;
    TextView firstName;
    EditText password;
    String name,media;

    Button proceed;

    FbGoogleSignInCommunication signInListener;

    BasicFunctionHandler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.name=getArguments().getString("firstName");
        this.media=getArguments().getString("media");
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view=View.inflate(context, R.layout.fb_google_password_btmsheet,null);
        firstName=view.findViewById(R.id.first_name);
        password=view.findViewById(R.id.password);
        firstName.setText("Hello "+name+" !! Please enter password for Bornbhukkad");
        proceed=view.findViewById(R.id.proceed);
        handler=new BasicFunctionHandler(getActivity());
        proceed.setOnClickListener(this);
        dialog.setContentView(view);
    }

    public static FBGooglePassword getInstance(String firstName,String media){
        FBGooglePassword fbGooglePassword=new FBGooglePassword();
        Bundle bundle=new Bundle();
        bundle.putString("firstName",firstName);
        bundle.putString("media",media);
        fbGooglePassword.setArguments(bundle);
        return fbGooglePassword;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        if (context instanceof FbGoogleSignInCommunication){
            this.signInListener=(FbGoogleSignInCommunication)context;
        }
        else {
            Log.e("ClassCastSignIn","UserLogin Must Implement FBSIgnIn");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.proceed:
                String pwd=password.getText().toString().trim();
                if (pwd.isEmpty()){
                    handler.showAlertDialog("Empty password!!","Password field can not be empty. Please try again. Thank you");
                }
                else{
                    signInListener.getFbGoogleSignInPassword(pwd,this.media);
                }
        }
    }

    public interface FbGoogleSignInCommunication {
        void getFbGoogleSignInPassword(String password,String media);
    }
}
