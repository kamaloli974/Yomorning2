package com.yomorning.lavafood.yomorning.user;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.VolleySingletonPattern;
import com.yomorning.lavafood.yomorning.rectifier.BasicFunctionHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRegistration extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{
    private Button submit;
    private EditText fName,lName,email,password,rePassword,mblNumber,answer;
    String firstName,lastName,emailAddress,pwd,repwd,mobileNumber;
    private BasicFunctionHandler handler;
    final String registrationUrl="http://ec2-54-69-105-54.us-west-2.compute.amazonaws.com/yomorning/api/v1/register";
    String securityQuestion,securityAnswer="Select security question";
    ProgressDialog progressDialog;
    Spinner multipleChoiceQuestions;
    JSONObject dataToBeSent,responseReceived;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        handler=new BasicFunctionHandler(UserRegistration.this);
        submit=(Button)findViewById(R.id.register);
        fName=(EditText)findViewById(R.id.first_name);
        lName=(EditText)findViewById(R.id.last_name);
        email=(EditText)findViewById(R.id.email_address);
        password=(EditText)findViewById(R.id.password);
        answer=(EditText)findViewById(R.id.security_answer);
        rePassword=(EditText)findViewById(R.id.confirm_password);
        mblNumber=(EditText)findViewById(R.id.mobile_number);
        multipleChoiceQuestions=(Spinner)findViewById(R.id.security_questions);
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(UserRegistration.this,R.array.security_questions,
                R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        multipleChoiceQuestions.setOnItemSelectedListener(this);
        multipleChoiceQuestions.setAdapter(arrayAdapter);
        progressDialog=new ProgressDialog(UserRegistration.this);
        dataToBeSent=new JSONObject();
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
                    securityAnswer=answer.getText().toString().trim();
                    if(firstName.isEmpty()||lastName.isEmpty()||emailAddress.isEmpty()
                            ||pwd.isEmpty()||repwd.isEmpty()||
                            mobileNumber.isEmpty()||securityQuestion.isEmpty()||securityAnswer.isEmpty()){
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
                                if(securityQuestion.equals("Select security question")){
                                    handler.showAlertDialog("Invalid Security Question","Please select proper security question and try again. Thank your");
                                }
                                else
                                {
                                    sendRequestToServer();
                                }
                                //handler.showAlertDialog("Congratulation!!","Every fields are filled correctly.");
                            }
                        }
                    }
                }
                else{
                    handler.showAlertDialog("Network Error!!","You are not connected to Internet please try again.");
                }
                break;

        }
    }
    void sendRequestToServer(){
        try {
            securityAnswer=securityAnswer.trim().toLowerCase();
            dataToBeSent.put("fname",firstName);
            dataToBeSent.put("lname",lastName);
            dataToBeSent.put("mail",emailAddress);
            dataToBeSent.put("pswd",pwd);
            dataToBeSent.put("mbl_number",mobileNumber);
            dataToBeSent.put("remark","direct");
            dataToBeSent.put("sqn",securityQuestion);
            dataToBeSent.put("sans",securityAnswer);
            progressDialog.setMessage("Processing your request...");
            progressDialog.show();
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, registrationUrl, dataToBeSent, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.hide();
                    responseReceived=response;
                    try {
                        if(Integer.parseInt(responseReceived.getString("code"))==401||Integer.parseInt(
                                responseReceived.getString("code"))==501){
                            handler.showAlertDialog("Error Occured!!",responseReceived.getString("message"));
                        }
                        if(Integer.parseInt(responseReceived.getString("code"))==201){
                            handler.showAlertDialog("Congratulations!!",responseReceived.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.hide();
                    Log.e("errorInVolley",error.getMessage()+"");
                }
            });

            VolleySingletonPattern.getInstance(UserRegistration.this).addToRequestQueue(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        securityQuestion=adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
