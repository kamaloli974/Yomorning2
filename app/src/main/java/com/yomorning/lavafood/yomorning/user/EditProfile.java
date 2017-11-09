package com.yomorning.lavafood.yomorning.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    EditText oldPassword,newPassword,reNewPassword,firstName,lastName,mobileNumber;
    TextView changePassword;
    String oPassword,nPassword,rnPassword,fname,lname,mblNumber;
    LinearLayout passwordContainer;
    Button save;
    BasicFunctionHandler basicFunctionHandler;
    SharedPreferences preference;

    Pattern alphabetPattern;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        oldPassword=(EditText)findViewById(R.id.old_password);
        newPassword=(EditText)findViewById(R.id.new_password);
        reNewPassword=(EditText)findViewById(R.id.re_new_password);
        firstName=(EditText)findViewById(R.id.first_name);
        lastName=(EditText)findViewById(R.id.last_name);
        mobileNumber=(EditText)findViewById(R.id.mobile_number);
        progressDialog=new ProgressDialog(this);

        alphabetPattern=Pattern.compile("^[a-zA-Z ]+$");

        changePassword=(TextView)findViewById(R.id.change_password);

        passwordContainer=(LinearLayout)findViewById(R.id.password_container);

        preference=getApplicationContext().getSharedPreferences("UserCredential", Context.MODE_PRIVATE);
        firstName.setText(preference.getString("firstName","Guest"));
        lastName.setText(preference.getString("lastName","User"));
        mobileNumber.setText(preference.getString("mobileNumber","No mobile number"));


        basicFunctionHandler=new BasicFunctionHandler(this);

        mobileNumber=(EditText)findViewById(R.id.mobile_number);
        save=(Button)findViewById(R.id.save);
        save.setOnClickListener(this);
        changePassword.setOnClickListener(this);
    }

    private void updateUserDetail(){
        if(oldPassword.getText().toString().trim().isEmpty()){

        }
        else{

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save:
                oPassword=oldPassword.getText().toString().trim();
                nPassword=newPassword.getText().toString().trim();
                rnPassword=reNewPassword.getText().toString().trim();
                fname=firstName.getText().toString().trim();
                lname=lastName.getText().toString().trim();
                mblNumber=mobileNumber.getText().toString().trim();

                Matcher firstNameMatcher=alphabetPattern.matcher(fname);
                Matcher lastNameMatcher=alphabetPattern.matcher(lname);
                if(firstNameMatcher.matches()&&lastNameMatcher.matches()){
                    if(passwordContainer.getVisibility()==View.GONE){
                        if(fname.isEmpty()||fname.isEmpty()||mblNumber.isEmpty()){
                            basicFunctionHandler.showAlertDialog("Field are empty","Please make to fill all the fields correctly. Thank" +
                                    " you.");
                        }
                        else{
                            if(mblNumber.length()==10){
                                sendDataToServer(0);
                            }
                            else{
                                basicFunctionHandler.showAlertDialog("Invalid Mobile","Please make sure mobile number of 10 digit");
                            }

                        }
                    }
                    else{
                        if(oPassword.isEmpty()||nPassword.isEmpty()||rnPassword.isEmpty()){
                            basicFunctionHandler.showAlertDialog("Empty fields","No fields should be left empty. Please try again" +
                                    "filling the blank fields. Thank you.");
                        }
                        else{
                            if(nPassword.equals(rnPassword)){
                                sendDataToServer(1);
                            }
                            else{
                                basicFunctionHandler.showAlertDialog("Password Mismatch","Then new password you entered does't " +
                                        " match with each other. Please try again. Thank you.");
                            }
                        }
                    }
                }
                else{
                    basicFunctionHandler.showAlertDialog("Invalid Name!", "First Name and Last Name should only contain" +
                            " letters. Please try again. Thank you. ");
                }

                break;
            case R.id.change_password:
                if(passwordContainer.getVisibility()==View.GONE){
                    passwordContainer.setVisibility(View.VISIBLE);
                    changePassword.setText(getResources().getString(R.string.dont_change_password));
                }
                else{
                    passwordContainer.setVisibility(View.GONE);
                    oPassword=rnPassword=nPassword="";
                    oldPassword.setText("");
                    newPassword.setText("");
                    reNewPassword.setText("");
                    changePassword.setText(R.string.want_to_change_password);
                }
                break;
        }
    }

    private void sendDataToServer(int i) {
        String url=CredentialProviderClass.HOST_NAME+"/update_user?token="+preference.getString("token","null");
        JSONObject object=new JSONObject();
        try {
            object.put("fname",fname);
            object.put("lname",lname);
            object.put("mbl_number",mblNumber);
            if(i==1) {
                object.put("pswd", oPassword);
                object.put("new_pswd", newPassword);
            }
            Log.e("date to be","sent json"+object);
            progressDialog.setMessage("processing...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url,object,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    parseJsonReply(response);
                    Log.e("response",response+"");

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    basicFunctionHandler.showAlertDialog(getResources().getString(R.string.volley_exception_title),
                            getResources().getString(R.string.volley_exception_message));
                }
            });
            VolleySingletonPattern.getInstance(EditProfile.this).addToRequestQueue(request);
        } catch (JSONException e) {
            progressDialog.dismiss();
            Log.e("JSONException",e.getMessage());
        }

    }

    private void parseJsonReply(JSONObject response) {
        try {
            if(response.has("error")){
                showAlertDialog("Credential Expired","Your credential has expired. Please login again. Thank you.");
                SharedPreferences.Editor editor=preference.edit();
                editor.clear().apply();
            }
            else{
                int code=response.getInt("code");
                if(code==201){
                    SharedPreferences.Editor editor=preference.edit();
                    editor.putString("firstName",fname);
                    editor.putString("lastName",lname);
                    editor.putString("mobileNumber",mblNumber);
                    editor.apply();
                    Intent intent=new Intent();
//                    intent.putExtra("firstName",fname);
//                    intent.putExtra("lastName",lname);
//                    intent.putExtra("mobileNumber",mblNumber);
                    setResult(Activity.RESULT_OK,intent);
                    Toast.makeText(EditProfile.this,"Profile has been changed",Toast.LENGTH_LONG).show();
                    finish();
                } else if (code==404) {
                    showAlertDialog("Credential Expired","Your credential has expired. Please login again. Thank you.");
                    SharedPreferences.Editor editor=preference.edit();
                    editor.clear().apply();
                }else if (code==400){
                    basicFunctionHandler.showAlertDialog("Wrong password","The old password you entered is incorrect");
                }
                else {
                        basicFunctionHandler.showAlertDialog(getResources().getString(R.string.server_error_title)
                                , getResources().getString(R.string.server_error_message));
                }
            }


        } catch (JSONException e) {
            Log.e("JSONException",e.getMessage());
        }
    }

    public void showAlertDialog(String title,String body){
        final AlertDialog.Builder dialog=new AlertDialog.Builder(EditProfile.this);
        dialog.setTitle(title);
        dialog.setMessage(body);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(EditProfile.this,UserLogin.class));
                finish();
                dialogInterface.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }
}
