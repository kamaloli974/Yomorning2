package com.yomorning.lavafood.yomorning;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yomorning.lavafood.yomorning.credentials.CredentialProviderClass;
import com.yomorning.lavafood.yomorning.rectifier.BasicFunctionHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener{
    LinearLayout codeContainer,passwordContainer;
    EditText newPassword,reNewPassword,securityCode;
    TextView codeInfo;
    Button confirm;
    String email,code;
    BasicFunctionHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        email=getIntent().getStringExtra("email");
        code=getIntent().getStringExtra("code");
        handler=new BasicFunctionHandler(this);

        codeContainer=(LinearLayout)findViewById(R.id.code_container);
        passwordContainer=(LinearLayout)findViewById(R.id.password_container);
        newPassword=(EditText)findViewById(R.id.new_password);
        reNewPassword=(EditText)findViewById(R.id.confirm_password);
        //codeInfo=(TextView)findViewById(R.id.);
        securityCode=(EditText)findViewById(R.id.code);
        confirm=(Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        codeInfo=(TextView)findViewById(R.id.code_info);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.confirm:
                if(codeContainer.getVisibility()==View.VISIBLE){
                    //codeInfo.setText(String.format("Enter security code sent to %s", email));
                    String codeValue=securityCode.getText().toString().trim();
                    if(!codeValue.isEmpty()){
                        String hashValue=handler.getHashValue(codeValue.getBytes(),"SHA-512");
                        if (hashValue.equals(code)){
                            securityCode.setText("");
                            codeInfo.setText("");
                            codeContainer.setVisibility(View.GONE);
                            passwordContainer.setVisibility(View.VISIBLE);
                        }
                        else {
                            handler.showAlertDialog("Invalid Security Code","The security code you entered is incorrect. " +
                                    "Please try again. Thank  you.");
                        }
                    }
                }
                else{
                    String password=newPassword.getText().toString().trim();
                    String rePassword=reNewPassword.getText().toString().trim();
                    if (password.isEmpty()||rePassword.isEmpty()){
                        handler.showAlertDialog(getString(R.string.empty_field_title),getString(R.string.empty_field_message));
                    }
                    else{
                        if(password.equals(rePassword)){
                            sendRequestToChangePassword(password,email);
                        }
                        else{
                            handler.showAlertDialog(getString(R.string.password_mismatch_title),getString(R.string.password_mismatch_heading));
                        }
                    }
                }
        }
    }

    private void sendRequestToChangePassword(String password,String email) {
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Changing your password...");
        dialog.show();
        JSONObject object=new JSONObject();
        String url= CredentialProviderClass.HOST_NAME+"/change_password";
        try {
            object.put("pswd",password);
            object.put("mail",email);
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    try {
                        int responseCode=response.getInt("code");
                        if(responseCode==201){
                            showAlertDialog("Success",response.getString("message"));
                        }
                        else{
                            handler.showAlertDialog("Sever Error",response.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    handler.showAlertDialog(getString(R.string.volley_exception_title),getString(R.string.volley_exception_message));
                }
            });
            VolleySingletonPattern.getInstance(ChangePassword.this).addToRequestQueue(request);
        } catch (JSONException e) {
            dialog.dismiss();
            Log.e("JSONException",e.getMessage());
        }
    }

    public void showAlertDialog(String title,String body){
        final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(body);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                setResult(Activity.RESULT_OK,new Intent().putExtra("email",email));
                finish();

            }
        });
        dialog.create();
        dialog.show();
    }
}
