package com.yomorning.lavafood.yomorning.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.yomorning.lavafood.yomorning.ChangePassword;
import com.yomorning.lavafood.yomorning.PresentationActivity;
import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.VolleySingletonPattern;
import com.yomorning.lavafood.yomorning.credentials.CredentialProviderClass;
import com.yomorning.lavafood.yomorning.rectifier.BasicFunctionHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class UserLogin extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    EditText email,password,fEmail;
    Button submit;
    TextView dAccount,forgotPassword;
    AlertDialog.Builder dialogBuilder;
    String emailAddress,pwd;
    BasicFunctionHandler handler;
    JSONObject object;
    final String loginUrl= CredentialProviderClass.HOST_NAME+"/login";
    ProgressDialog dialog;
    SharedPreferences preferences;

    SignInButton googleSignInButton;
    GoogleApiClient googleApiClient;

    LoginButton facebookLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        email=(EditText)findViewById(R.id.email_address);
        password=(EditText)findViewById(R.id.password);
        submit=(Button)findViewById(R.id.login_button);
        object=new JSONObject();
        dAccount=(TextView)findViewById(R.id.dont_have_account);
        forgotPassword=(TextView)findViewById(R.id.forgot_password);
        preferences=getApplicationContext().getSharedPreferences("UserCredential", Context.MODE_PRIVATE);
        handler=new BasicFunctionHandler(UserLogin.this);

        forgotPassword.setOnClickListener(this);
        submit.setOnClickListener(UserLogin.this);
        dialog=new ProgressDialog(UserLogin.this);
        dialog.setCancelable(false);
        dAccount.setOnClickListener(UserLogin.this);

        facebookLoginButton=(LoginButton)findViewById(R.id.login_button_facebook);

        GoogleSignInOptions signInOption=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOption)
                .build();

        googleSignInButton=(SignInButton)findViewById(R.id.login_button_google);
        googleSignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button:
                if(handler.isConnectedToNetwork()){
                    emailAddress=email.getText().toString().trim();
                    pwd=password.getText().toString().trim();
                    if(emailAddress.isEmpty()||pwd.isEmpty()){
                        handler.showAlertDialog("Opps!! Some filds are empty.",
                                "Please make sure that no fields are empty and try again.");
                    }
                    else{
                        processUserRequest();
                    }
                }
                else{
                    handler.showAlertDialog("Network Error!","You are not connected to the network. Please try again.");
                }
                break;
            case R.id.dont_have_account:
                startActivity(new Intent(UserLogin.this,UserRegistration.class));
                finish();
                break;
            case R.id.forgot_password:
                dialogForVerificationInput("Email","Please Enter your registered email");
                break;
            case R.id.login_button_google:
                signInToGoogle();
        }
    }

    private void signInToGoogle() {
        Intent googleSignInIntent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(googleSignInIntent,CredentialProviderClass.GOOGLE_SIGN_IN_REQUEST_CODE);
    }

    void processUserRequest(){
        try {
            object.put("EMAIL_ID",emailAddress);
            object.put("password",pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dialog.setMessage("Please wait...");
        dialog.show();
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, loginUrl, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("login reply",response.toString());
                dialog.hide();
                try {
                    if(response.getInt("code")==400){
                        handler.showAlertDialog("Invalid Credentials !!",response.getString("message"));
                    }
                    else if(response.getInt("code")==501){
                        handler.showAlertDialog("Server Error",response.getString("message"));
                    }
                    else if(response.getInt("code")==201){
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("token",response.getString("token"));
                        editor.putString("emailAddress",response.getString("email"));
                        editor.putString("firstName",response.getString("fname"));
                        editor.putString("lastName",response.getString("lname"));
                        editor.putString("mobileNumber",response.getString("mobile_number"));
                        editor.putInt("isEmailVerified",Integer.parseInt(response.getString("is_email_verified")));
                        editor.putInt("isMobileVerified",Integer.parseInt(response.getString("is_mobile_verified")));
                        editor.apply();
                        startActivity(new Intent(UserLogin.this, PresentationActivity.class));
                        finish();
//                        handler.showAlertDialog("Success",response.getString("message")+" and Your authorization token is "
//                        +response.getString("token"));
                    }
                    else{
                        handler.showAlertDialog("Error !!","Unexpected error occurred please report Error.");
                    }
                } catch (JSONException e) {
                    Log.e("JSONException","Json parsing error occured"+e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                handler.showAlertDialog("Network Error!","We have encountered some network problem. Please try again after" +
                        " some time. Thank you.");
                Log.e("VolleyError",error.getMessage()+"");
            }
        });
        VolleySingletonPattern.getInstance(UserLogin.this).addToRequestQueue(request);

    }

    private void dialogForVerificationInput(String title,String message){
        final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        final EditText inputValue=new EditText(UserLogin.this);
        inputValue.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        inputValue.setGravity(Gravity.CENTER);
        inputValue.setHint("Email");
        fEmail=inputValue;
        dialog.setView(inputValue);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!fEmail.getText().toString().trim().isEmpty()){
                    sendDataToServer(fEmail.getText().toString().trim());
                    dialogInterface.dismiss();
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
    }

    private void sendDataToServer(String email) {
        dialog.setMessage("Verifying your email...");
        dialog.show();
        final String mail=email;
        JSONObject object=new JSONObject();
        try {
            object.put("mail",email);
            String url=CredentialProviderClass.HOST_NAME+"/forgot_password";
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    try {
                        int code=response.getInt("code");
                        if (code==404){
                            handler.showAlertDialog("Incorrect Email","The Email you entered is not registered with us." +
                                    " Please try again. Thank you.");
                        }
                        else if (code==205){
                            Intent intent=new Intent(UserLogin.this, ChangePassword.class);
                            intent.putExtra("email",mail);
                            intent.putExtra("code",response.getString("hashCode"));
                            startActivityForResult(intent,1);
                            //TODO:: code for re-setting password
                        }
                        else{
                            handler.showAlertDialog(getResources().getString(R.string.server_error_title),
                                    getResources().getString(R.string.server_error_message));
                        }
                    } catch (JSONException e) {
                        dialog.dismiss();
                        Log.e("JSONERRO",e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    handler.showAlertDialog(getResources().getString(R.string.volley_exception_title),
                            getResources().getString(R.string.volley_exception_message));
                }
            });
            VolleySingletonPattern.getInstance(UserLogin.this).addToRequestQueue(request);
        } catch (JSONException e) {
            Log.e("JSONException",e.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if (resultCode== Activity.RESULT_OK){
                email.setText(data.getStringExtra("email"));
            }
        }

        if(requestCode==CredentialProviderClass.GOOGLE_SIGN_IN_REQUEST_CODE){
            GoogleSignInResult signInResult=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(signInResult);
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult signInResult) {
        if(signInResult.isSuccess()){
            GoogleSignInAccount account=signInResult.getSignInAccount();
            Log.e("UserName",account.getDisplayName()+" UserEmail= "+account.getEmail());
        }
        else{
            Log.e("SignIn Error","Could't sign In ");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
