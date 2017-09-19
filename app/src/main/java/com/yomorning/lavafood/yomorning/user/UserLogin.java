package com.yomorning.lavafood.yomorning.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.yomorning.lavafood.yomorning.rectifier.BasicFunctionHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class UserLogin extends AppCompatActivity implements View.OnClickListener {
    EditText email,password;
    Button submit;
    TextView dAccount;
    String emailAddress,pwd;
    BasicFunctionHandler handler;
    JSONObject object;
    final String loginUrl="http://ec2-54-69-105-54.us-west-2.compute.amazonaws.com/yomorning/api/v1/login";
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        email=(EditText)findViewById(R.id.email_address);
        password=(EditText)findViewById(R.id.password);
        submit=(Button)findViewById(R.id.login_button);
        object=new JSONObject();
        dAccount=(TextView)findViewById(R.id.dont_have_account);
        handler=new BasicFunctionHandler(UserLogin.this);
        submit.setOnClickListener(UserLogin.this);
        dialog=new ProgressDialog(UserLogin.this);
        dAccount.setOnClickListener(UserLogin.this);
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
                //finish();
                break;
        }
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
                dialog.hide();
                try {
                    if(Integer.parseInt(response.getString("code"))==400){
                        handler.showAlertDialog("Invalid Credentials !!",response.getString("message"));
                    }
                    else if(Integer.parseInt(response.getString("code"))==501){
                        handler.showAlertDialog("Server Error",response.getString("message"));
                    }
                    else if(Integer.parseInt(response.getString("code"))==201){
                        handler.showAlertDialog("Success",response.getString("message")+" and Your authorization token is "
                        +response.getString("token"));
                    }
                    else{
                        handler.showAlertDialog("Error !!","Unexpected error occurred please report Error.");
                    }
                } catch (JSONException e) {
                    Log.e("JSONException","Json parsing error occured");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                Log.e("VolleyError",error.getMessage()+"");
            }
        });
        VolleySingletonPattern.getInstance(UserLogin.this).addToRequestQueue(request);

    }
}
