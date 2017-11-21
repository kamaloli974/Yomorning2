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

public class FbGoogleSignUp extends BottomSheetDialogFragment implements View.OnClickListener {
    private FbGoogleSignUpCommunication fbListener;

    Context context;
    String userName;
    EditText password,rePassword,mobileNumber;
    TextView firstName;
    Button proceed;

    BasicFunctionHandler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userName=getArguments().getString("firstName");
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view=View.inflate(context,R.layout.fb_google_sign_up,null);
        firstName=view.findViewById(R.id.first_name);
        firstName.setText("Hello "+userName+" !! We are almost ready...");
        password=view.findViewById(R.id.password);
        rePassword=view.findViewById(R.id.re_password);
        mobileNumber=view.findViewById(R.id.mobile_number);
        proceed=view.findViewById(R.id.proceed);
        handler=new BasicFunctionHandler(getActivity());
        proceed.setOnClickListener(this);
        dialog.setContentView(view);
    }

    public static FbGoogleSignUp getInstance(String firstName){
        FbGoogleSignUp fbGoogleSignUp=new FbGoogleSignUp();
        Bundle bundle=new Bundle();
        bundle.putString("firstName",firstName);
        fbGoogleSignUp.setArguments(bundle);
        return fbGoogleSignUp;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FbGoogleSignUpCommunication){
            this.fbListener=(FbGoogleSignUpCommunication) context;
        }
        else{
            Log.e("ClassCastException","UserRegistration Must Implement Interface");
        }
        this.context=context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.proceed:
                String mobile=mobileNumber.getText().toString().trim();
                String pwd=password.getText().toString().trim();
                String rePwd=rePassword.getText().toString().trim();
                if (mobile.isEmpty()||pwd.isEmpty()||rePwd.isEmpty()){
                    handler.showAlertDialog(getActivity().getString(R.string.empty_field_title),getActivity().getString(R.string.empty_field_message));
                }
                else {
                    if (mobile.length()!=10){
                        handler.showAlertDialog("Incorrect Mobile Number!!","Please make sure that mobile number is of length 10 digit. Thank you");
                    }
                    else{
                        if (pwd.equals(rePwd)){
                            fbListener.publishMobileAndPassword(mobile,pwd);
                        }
                        else{
                            handler.showAlertDialog("Password Mismatch!!","The password you entered does't match. Please try again. Thank you");
                        }
                    }
                }
        }
    }

    public interface FbGoogleSignUpCommunication{
        void publishMobileAndPassword(String mobileNumber,String password);

    }
}
