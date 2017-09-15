package com.yomorning.lavafood.yomorning.railrestroactivities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.VolleySingletonPattern;
import com.yomorning.lavafood.yomorning.adapters.RailRestroMenuAdapter;
import com.yomorning.lavafood.yomorning.credentials.CredentialProviderClass;
import com.yomorning.lavafood.yomorning.fragments.RailRestroFoodOrderSystem;
import com.yomorning.lavafood.yomorning.models.RailRestroMenuModel;
import com.yomorning.lavafood.yomorning.models.RailRestroOrderModel;
import com.yomorning.lavafood.yomorning.models.RailRestroVendorsModel;
import com.yomorning.lavafood.yomorning.rectifier.BasicFunctionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RailRestroMenuActivity extends AppCompatActivity implements RailRestroMenuAdapter.OnMenuItemClickedListener,
        View.OnClickListener,RailRestroFoodOrderSystem.OnCartItemChangedListener{
    RailRestroVendorsModel vendorsModel;
    private RecyclerView recyclerView;
    private BasicFunctionHandler basicFunctionHandler;
    TextView shoppingItemCount;
    ProgressDialog dialog;
    ImageView shoppingCart;
    HashMap<Integer,RailRestroOrderModel> orderModelHashMap;
    int totalNumberOfItemsInCart=0;
    double totalPrice;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rail_restro_menu);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialog=new ProgressDialog(this);
        orderModelHashMap=new HashMap<>();
        vendorsModel= getIntent().getParcelableExtra("railRestroVendorsModel");
        recyclerView=(RecyclerView)findViewById(R.id.menu_recycler_view);
        shoppingCart=(ImageView)findViewById(R.id.shopping_cart);
        shoppingCart.setOnClickListener(this);
        basicFunctionHandler=new BasicFunctionHandler(RailRestroMenuActivity.this);
        menuJsonDataParser(vendorsModel.getVendorId());
    }

    private void setAdapterToPopulateMenus(ArrayList<RailRestroMenuModel> menu){
        RailRestroMenuAdapter adapter=new RailRestroMenuAdapter(this,menu);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        shoppingItemCount=(TextView) findViewById(R.id.shopping_item_count);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void menuJsonDataParser(String outletId){
        dialog.setMessage("Processing your request...");
        dialog.show();
        String menuUrl= CredentialProviderClass.RAIL_RESTRO_BASIC_API+"menu/"+outletId+CredentialProviderClass.BACK_SLASH+
                CredentialProviderClass.RAIL_RESTRO_USER+CredentialProviderClass.BACK_SLASH+
                CredentialProviderClass.RAIL_RESTRO_API_KEY;
        StringRequest request=new StringRequest(Request.Method.GET, menuUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.hide();
                ArrayList<RailRestroMenuModel> menuList=getRailRestroMenuArrayList(response);
                setAdapterToPopulateMenus(menuList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();

                basicFunctionHandler.showAlertDialog("Error!","VolleyError Occure and message is "+error.getCause());
            }
        });

        VolleySingletonPattern.getInstance(this).addToRequestQueue(request);
    }

    private ArrayList<RailRestroMenuModel> getRailRestroMenuArrayList(String response) {
        ArrayList<RailRestroMenuModel> menuArrayList=new ArrayList<>();
        try {
            JSONObject object=new JSONObject(response);
            if(object.getBoolean("Status")){
                JSONArray data=object.getJSONArray("Data");
                for(int i=0;i<data.length();i++){
                    JSONObject jsonObject=data.getJSONObject(i);
                    int categoryId=jsonObject.getInt("category_id");
                    String categoryName=jsonObject.getString("category_name");
                    JSONArray menuArray=jsonObject.getJSONArray("menu");
                    for (int j=0;j<menuArray.length();j++){
                        RailRestroMenuModel model=new RailRestroMenuModel();
                        JSONObject indivisualItem=menuArray.getJSONObject(j);
                        model.setCategoryId(categoryId);
                        model.setCategoryName(categoryName);
                        model.setBasePrice(Integer.parseInt(indivisualItem.getString("base_price")));
                        model.setItemId(Integer.parseInt(indivisualItem.getString("item_id")));
                        model.setItemName(indivisualItem.getString("item_name"));
                        model.setServiceTax(Double.parseDouble(indivisualItem.getString("service_tax")));
                        model.setVat(Integer.parseInt(indivisualItem.getString("vat")));
                        model.setItemType(Integer.parseInt(indivisualItem.getString("itemtype_id")));
                        model.setStatus(Integer.parseInt(indivisualItem.getString("status")));
                        model.setSellingPrice(indivisualItem.getInt("selling_price"));
                        model.setTimeSlot(indivisualItem.getJSONObject("menu_tags").getString("time_slot"));
                        model.setFoodItemDescription(indivisualItem.getString("description"));
                        model.setFoodCategory(indivisualItem.getJSONObject("menu_tags").getString("type"));
                        model.setActive(indivisualItem.getBoolean("is_active"));
                        model.setOpeningTime(indivisualItem.getString("opening_time"));
                        model.setClosingTime(indivisualItem.getString("closing_time"));
                        menuArrayList.add(model);
                    }
                }
                return menuArrayList;
            }
            else{
                basicFunctionHandler.showAlertDialog("Sorry!","No food items are available from this provider.Please go back and " +
                        "select another provider");
                return menuArrayList;
            }
        } catch (JSONException e) {
            basicFunctionHandler.showAlertDialog("JSONException","JSONException occured and message is "+e.getCause());
            Log.e("JSONException",e.getMessage());
            return menuArrayList;
        }
    }

    @Override
    public void getSelectedItem(RailRestroMenuModel model) {
        if (orderModelHashMap.containsKey(model.getItemId())){
            RailRestroOrderModel orderModel=orderModelHashMap.get(model.getItemId());
            orderModel.setItemCount(orderModel.getItemCount()+1);
            Log.e(""+orderModel.getItemCount()+model.getItemName(),"Item Id"+model.getItemId());
            totalNumberOfItemsInCart=totalNumberOfItemsInCart+1;
            totalPrice=totalPrice+model.getSellingPrice();
            shoppingItemCount.setText(totalNumberOfItemsInCart+"");
        }
        else{
            totalNumberOfItemsInCart=totalNumberOfItemsInCart+1;
            totalPrice=totalPrice+model.getSellingPrice();
            RailRestroOrderModel order=new RailRestroOrderModel();
            order.setItemId(model.getItemId());
            order.setItemCount(1);
            order.setModel(model);
            orderModelHashMap.put(model.getItemId(),order);
            Log.e("First Time","Item id"+model.getItemName());
            shoppingItemCount.setText(totalNumberOfItemsInCart+"");
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.shopping_cart){
            if(totalNumberOfItemsInCart==0){
                basicFunctionHandler.showAlertDialog("Opps!!","You don't have anything in cart. Please" +
                        " select some food items from menu.");
            }
            else{
                RailRestroFoodOrderSystem orderSystem= RailRestroFoodOrderSystem.newInstance(orderModelHashMap,
                        totalNumberOfItemsInCart, totalPrice);
                orderSystem.show(getFragmentManager(),"cartDisplayFragment");
            }
        }
    }

    @Override
    public void getChangedCartDetail(HashMap<Integer, RailRestroOrderModel> changedOrder, int totalItems,
                                     double totalPrice) {
        this.totalPrice=totalPrice;
        this.totalNumberOfItemsInCart=totalItems;
        this.orderModelHashMap=changedOrder;
        shoppingItemCount.setText(totalNumberOfItemsInCart+"");
    }
}

