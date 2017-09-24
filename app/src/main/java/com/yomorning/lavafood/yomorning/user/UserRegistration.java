package com.yomorning.lavafood.yomorning.user;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.VolleySingletonPattern;
import com.yomorning.lavafood.yomorning.credentials.CredentialProviderClass;
import com.yomorning.lavafood.yomorning.rectifier.BasicFunctionHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRegistration extends AppCompatActivity implements View.OnClickListener,DialogInterface.OnClickListener{
    private Button submit;
    private EditText fName,lName,email,password,rePassword,mblNumber,verificationCodeValue;
    private TextView alreadyHaveAccount;
    String firstName,lastName,emailAddress,pwd,repwd,mobileNumber,verificationCode;
    private BasicFunctionHandler handler;
    final String registrationUrl= CredentialProviderClass.HOST_NAME+"/register";
    SharedPreferences preferences;
    //String securityQuestion,securityAnswer="Select security question";
    ProgressDialog progressDialog;
   // Spinner multipleChoiceQuestions;
    JSONObject dataToBeSent,responseReceived;
    AlertDialog.Builder dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        handler=new BasicFunctionHandler(UserRegistration.this);
        Log.e("hash value of kamal",handler.getHashValue("kamal".getBytes(),"SHA-512"));
        submit=(Button)findViewById(R.id.register);
        fName=(EditText)findViewById(R.id.first_name);
        lName=(EditText)findViewById(R.id.last_name);
        email=(EditText)findViewById(R.id.email_address);
        password=(EditText)findViewById(R.id.password);
       // answer=(EditText)findViewById(R.id.security_answer);
        rePassword=(EditText)findViewById(R.id.confirm_password);
        mblNumber=(EditText)findViewById(R.id.mobile_number);
        alreadyHaveAccount=(TextView)findViewById(R.id.already_have_account);
        preferences=getApplicationContext().getSharedPreferences("Credential",MODE_PRIVATE);


//        multipleChoiceQuestions=(Spinner)findViewById(R.id.security_questions);
//        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(UserRegistration.this,R.array.security_questions,
//                R.layout.support_simple_spinner_dropdown_item);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//
//        multipleChoiceQuestions.setOnItemSelectedListener(this);
//        multipleChoiceQuestions.setAdapter(arrayAdapter);
        progressDialog=new ProgressDialog(UserRegistration.this);
        progressDialog.setCancelable(false);
        dataToBeSent=new JSONObject();

        alreadyHaveAccount.setOnClickListener(this);
        submit.setOnClickListener(UserRegistration.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                if(handler.isConnectedToNetwork()){
                    firstName=fName.getText().toString().trim();
                    lastName=lName.getText().toString().trim();
                    emailAddress=email.getText().toString().trim();
                    pwd=password.getText().toString().trim();
                    repwd=rePassword.getText().toString().trim();
                    mobileNumber=mblNumber.getText().toString().trim();
                    //securityAnswer=answer.getText().toString().trim();
                    if(firstName.isEmpty()||lastName.isEmpty()||emailAddress.isEmpty()
                            ||pwd.isEmpty()||repwd.isEmpty()||
                            mobileNumber.isEmpty()/*securityQuestion.isEmpty()||securityAnswer.isEmpty()*/){
                        handler.showAlertDialog("Oops!!","Please make sure that no fields are left blank.");
                    }
                    else{
                        if(!pwd.equals(repwd)){
                            handler.showAlertDialog("Password Mismatch!!","The password you entered doesn't match. Please check" +
                                    " and try again.");
                        }
                        else{
                            if(mobileNumber.length()!=10){
                                handler.showAlertDialog("Invalid Mobile Number!!","Phone number must be of 10 digit");
                            }
                            else{
                                if(handler.isEmailValid(emailAddress)){
                                    if (handler.isPasswordLengthValid(pwd)){
                                        isMobileAndEmailAlreadyRegistered();
                                    }
                                    else{
                                        handler.showAlertDialog("Invalid Password","Password must be at least 8 of length and alpha-numeric format. Please " +
                                                "try again. Thank you.");
                                    }
                                }
                                else{
                                    handler.showAlertDialog("Incorrect Email","Please make sure that you have entered correct email format. Thank you.");
                                }
                                //sendRequestToServer();
//                                if(securityQuestion.equals("Select security question")){
//                                    handler.showAlertDialog("Invalid Security Question","Please select proper security question and try again. Thank your");
//                                }
//                                else
//                                {
//
//                                }
                                //handler.showAlertDialog("Congratulation!!","Every fields are filled correctly.");
                            }
                        }
                    }
                }
                else{
                    handler.showAlertDialog("Network Error!!","You are not connected to Internet please try again.");
                }
                break;
            case R.id.already_have_account:
                startActivity(new Intent(UserRegistration.this,UserLogin.class));
                finish();

        }
    }

    void sendRequestToServer(){
        try {
            //securityAnswer=securityAnswer.trim().toLowerCase();
            dataToBeSent.put("fname",firstName);
            dataToBeSent.put("lname",lastName);
            dataToBeSent.put("mail",emailAddress);
            dataToBeSent.put("pswd",pwd);
            dataToBeSent.put("mbl_number",mobileNumber);
            dataToBeSent.put("remark","direct");
            dataToBeSent.put("is_eVerified",1);
//            dataToBeSent.put("sqn",securityQuestion);
//            dataToBeSent.put("sans",securityAnswer);
            progressDialog.setMessage("Processing your request...");
            progressDialog.show();
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, registrationUrl, dataToBeSent, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.hide();
                    responseReceived=response;
                    try {
                        if(responseReceived.getInt("code")==501){
                            handler.showAlertDialog(getString(R.string.server_error_title),responseReceived.getString("message"));
                        }
                        else if (responseReceived.getInt("code")==401){
                            handler.showAlertDialog("Server Error",responseReceived.getString("message"));
                        }
                        else if(responseReceived.getInt("code")==100){
                            handler.showAlertDialog(getString(R.string.already_registered),responseReceived.getString("message"));
                        }
                        else if (response.getInt("code")==101){
                            handler.showAlertDialog(getString(R.string.already_registered),responseReceived.getString("message"));
                        }
                        else if(responseReceived.getInt("code")==201){
                            successDialog("Congratulations!!",responseReceived.getString("message")+"\n"+
                                    " Please login with your Email and password to proceed.");
                        }
                    } catch (JSONException e) {
                        Log.e("JSONException",e.getMessage());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.hide();
                    handler.showAlertDialog(getString(R.string.volley_exception_title),getString(R.string.
                            volley_exception_message));
                }
            });

            VolleySingletonPattern.getInstance(UserRegistration.this).addToRequestQueue(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void isMobileAndEmailAlreadyRegistered(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("mail",emailAddress);
            jsonObject.put("mbl_number",mobileNumber);
            progressDialog.setMessage(getString(R.string.account_existence_checking));
            progressDialog.show();
        } catch (JSONException error) {
            progressDialog.hide();
            Log.e(getString(R.string.volley_exception_title),getString(R.string.volley_exception_message));
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,CredentialProviderClass.
                HOST_NAME+"/check_account_existence",jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDialog.hide();
                    int code=response.getInt("code");
                    String message=response.getString("message");
                    if(code==100){
                        handler.showAlertDialog(getString(R.string.already_registered),message);
                    }
                    else if(code==101){
                        handler.showAlertDialog(getString(R.string.already_registered),message);
                    }
                    else if(code==501){
                        handler.showAlertDialog(getString(R.string.server_error_title),message);
                    }
                    else if (code==205){
                        verificationCode=response.getString("hashCode");
                        dialogForVerificationInput(verificationCode).show();
//                        handler.showAlertDialog("Required",message+" "+verificationCode);
                    }
                    else {
                        handler.showAlertDialog(getString(R.string.server_error_title),"Internal server error occurred. " +
                                getString(R.string.server_error_message));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Log.e("VOlley eroor",error.getMessage());
                handler.showAlertDialog(getString(R.string.volley_exception_title),getString(R.string.volley_exception_message));
            }
        });
        VolleySingletonPattern.getInstance(UserRegistration.this).addToRequestQueue(jsonObjectRequest);
    }

    private void successDialog(String title,String message){
        final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent=new Intent(UserRegistration.this,UserLogin.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
    }

    private AlertDialog.Builder dialogForVerificationInput(final String hashCode){
        final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Verify Email");
        dialog.setMessage("Please enter verification code sent to this "+emailAddress+" address.");
        final EditText inputValue=new EditText(UserRegistration.this);
        inputValue.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inputValue.setGravity(Gravity.CENTER);
        inputValue.setHint("Code");
        verificationCodeValue=inputValue;
        dialog.setView(inputValue);
        dialog.setPositiveButton("OK",this);
        dialog.setNegativeButton("Cancel", this);
        dialog.setCancelable(false);
        dialog.create();
        this.dialog=dialog;
        return dialog;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i){
            case -1:
                if(verificationCode.equals(handler.getHashValue(verificationCodeValue.getText().toString().getBytes(),
                        "SHA-512"))){
                    dialogInterface.dismiss();
                    dialog.setView(null);
                    dialog=null;
                    sendRequestToServer();
                    Log.e("Verification code","is correct"+"inteface id"+i);
                }
                else{
                    dialogForVerificationInput(verificationCode).setMessage("Incorrect Verification code.Please try again.").
                    show();
                }
                break;
            case -2:
                dialogInterface.dismiss();
                dialog.setView(null);
                dialog=null;
                Log.e("Cancel","Dialog Cancel button is pressed");
                break;
        }
    }
//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        securityQuestion=adapterView.getItemAtPosition(i).toString();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//    }
//    new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialogInterface, int i) {
//            Log.e("cancel button id",""+i);
//        }
//    }
//
//    new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialogInterface, int i) {
//            String value=inputValue.getText().toString().trim();
//            if(hashCode.equals(handler.getHashValue(value.getBytes(),"SHA-512"))){
//                // sendRequestToServer();
//                Log.e("Verification code","is correct"+"inteface id"+i);
//                dialogInterface.dismiss();
//            }
//            else{
//                Log.e("Invalid Code"," negative button id"+i);
//                //dialog.setMessage("Invalid verification code. Please try again.");
//            }
////                dialogInterface.dismiss();
////                Intent intent=new Intent(UserRegistration.this,UserLogin.class);
////                startActivity(intent);
////                finish();
//        }
//    });
}
