package com.yomorning.lavafood.yomorning.railrestroactivities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
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
        View.OnClickListener,RailRestroFoodOrderSystem.OnCartItemChangedListener,SearchView.OnQueryTextListener,
        CompoundButton.OnCheckedChangeListener {
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

    AppCompatCheckBox veg,nonVeg;

    SearchView searchFoodItems;
    RailRestroMenuAdapter adapter;

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

        menuJsonDataParser(vendorsModel.getVendorId());

        searchFoodItems=(SearchView)findViewById(R.id.search_menu);

        veg=(AppCompatCheckBox)findViewById(R.id.veg);
        nonVeg=(AppCompatCheckBox)findViewById(R.id.none_veg);
        shoppingCart.setOnClickListener(this);
        basicFunctionHandler=new BasicFunctionHandler(RailRestroMenuActivity.this);
        searchFoodItems.setOnQueryTextListener(this);

        veg.setOnClickListener(this);
        nonVeg.setOnClickListener(this);
        veg.setOnCheckedChangeListener(this);
        nonVeg.setOnCheckedChangeListener(this);
    }

    private void setAdapterToPopulateMenus(ArrayList<RailRestroMenuModel> menu){
        adapter=new RailRestroMenuAdapter(this,menu);
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

                basicFunctionHandler.showAlertDialog(getString(R.string.volley_exception_title),
                        getString(R.string.volley_exception_message));
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
                        if(indivisualItem.getBoolean("is_active")){
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
        switch (view.getId()){
            case R.id.shopping_cart:
                if(totalNumberOfItemsInCart==0){
                    basicFunctionHandler.showAlertDialog("Opps!!","You don't have anything in cart. Please" +
                            " select some food items from menu.");
                }
                else{
                    RailRestroFoodOrderSystem orderSystem= RailRestroFoodOrderSystem.newInstance(orderModelHashMap,
                            totalNumberOfItemsInCart, totalPrice,vendorsModel);
                    orderSystem.show(getFragmentManager(),"cartDisplayFragment");
                }
                break;
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

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.rail_restro_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        Log.e("TextOnSearch",newText);
        return false;
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.veg:
                Log.e("Veg","Veg Checked");
                if (veg.isChecked()&&nonVeg.isChecked()){
                    adapter.showAllData();
                    adapter.notifyDataSetChanged();
                }
                else if(veg.isChecked()&&!nonVeg.isChecked()){
                    adapter.showVegItems();
                    Log.e("Veg","Veg Checked");
                    adapter.notifyDataSetChanged();
                }
                else if(nonVeg.isChecked()){
                    adapter.showNonVegItems();
                    adapter.notifyDataSetChanged();
                }
                else {
                    adapter.showNothing();
                    adapter.notifyDataSetChanged();

                }
                break;
            case R.id.none_veg:
                if (veg.isChecked()&&nonVeg.isChecked()){
                    adapter.showAllData();
                    adapter.notifyDataSetChanged();
                }
                else if(veg.isChecked()&&!nonVeg.isChecked()){
                    adapter.showVegItems();
                    adapter.notifyDataSetChanged();
                }
                else if(nonVeg.isChecked()){
                    adapter.showNonVegItems();
                    adapter.notifyDataSetChanged();
                }
                else {
                    adapter.showNothing();
                    adapter.notifyDataSetChanged();
                }
                break;
        }
        Log.e("Checked","Checked");
    }
}

