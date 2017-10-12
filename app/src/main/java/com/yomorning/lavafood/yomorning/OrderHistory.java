package com.yomorning.lavafood.yomorning;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yomorning.lavafood.yomorning.adapters.PlaceOrder;
import com.yomorning.lavafood.yomorning.credentials.CredentialProviderClass;
import com.yomorning.lavafood.yomorning.models.OrderHistoryModel;
import com.yomorning.lavafood.yomorning.rectifier.BasicFunctionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<OrderHistoryModel> models;
    SharedPreferences preferences;
    ProgressDialog dialog;
    BasicFunctionHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        dialog=new ProgressDialog(this);
        preferences=getApplicationContext().getSharedPreferences("UserCredential",MODE_PRIVATE);
        handler=new BasicFunctionHandler(this);
        models=new ArrayList<>();
        sendRequestToServer();
    }

    private void sendRequestToServer() {
        dialog.setMessage("processing...");
        dialog.show();
        String url= CredentialProviderClass.HOST_NAME+"/show_product_history?token="+preferences.getString("token","");
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.hide();
                jsonParse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                handler.showAlertDialog(getString(R.string.volley_exception_title),getString(R.string.volley_exception_message));

            }
        });
        VolleySingletonPattern.getInstance(this).addToRequestQueue(request);
    }
    private void jsonParse(JSONObject object){
        try {
            int code=object.getInt("code");
            if(code==201){
                JSONArray array=object.getJSONArray("values");
                if(array.length()==0){
                    handler.showAlertDialog("No Order History","You haven't bought any food items now. So  product " +
                            "history is empty.");
                }
                else{
                    for(int i=0;i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        OrderHistoryModel model=new OrderHistoryModel();
                        model.setDeliveryTime(obj.getString("delivery_date"));
                        model.setNames(obj.getString("products"));
                        model.setTotalPrice(obj.getString("total_price"));
                        model.setOrderedTime(obj.getString("order_date"));
                        models.add(model);
                    }
                    recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
                    PlaceOrder placeOrder=new PlaceOrder(models,OrderHistory.this);
                    LinearLayoutManager manager=new LinearLayoutManager(OrderHistory.this);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(placeOrder);
                }
            }
            else{
                handler.showAlertDialog(getString(R.string.server_error_title),
                        getString(R.string.server_error_message));
            }
        } catch (JSONException e) {
            Log.e("JSONOBJECT",e.getMessage());
        }
    }

}
