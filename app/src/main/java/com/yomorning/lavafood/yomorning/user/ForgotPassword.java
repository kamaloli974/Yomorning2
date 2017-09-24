package com.yomorning.lavafood.yomorning.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.credentials.CredentialProviderClass;
import com.yomorning.lavafood.yomorning.rectifier.BasicFunctionHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener{
    EditText newPassword,confirmPassword;
    Button changePassword;
    String nPassword,cPassword;
    BasicFunctionHandler handler;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        newPassword=(EditText)findViewById(R.id.new_password);
        confirmPassword=(EditText)findViewById(R.id.confirm_password);
        changePassword=(Button)findViewById(R.id.change_password);

        email=getIntent().getStringExtra("email");

        handler=new BasicFunctionHandler(this);

        changePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_password:
                nPassword=newPassword.getText().toString().trim();
                cPassword=confirmPassword.getText().toString().trim();
                if(cPassword.isEmpty()||nPassword.isEmpty()){
                    handler.showAlertDialog(getString(R.string.empty_field_title),getString(R.string.empty_field_message));
                }
                else{
                    if(nPassword.equals(cPassword)){
                        sendDataToServer();
                    }
                    else{
                        handler.showAlertDialog(getString(R.string.password_mismatch_title),
                                getString(R.string.password_mismatch_heading));
                    }
                }
        }
    }

    private void sendDataToServer() {
        String url= CredentialProviderClass.HOST_NAME+"/forgot_password";
        JSONObject object=new JSONObject();
        try {
            object.put("password",nPassword);
            object.put("mail",email);
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handler.showAlertDialog(getResources().getString(R.string.volley_exception_title),
                            getResources().getString(R.string.volley_exception_message));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
