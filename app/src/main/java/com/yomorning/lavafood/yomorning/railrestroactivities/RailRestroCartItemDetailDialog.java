package com.yomorning.lavafood.yomorning.railrestroactivities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yomorning.lavafood.yomorning.MainActivity;
import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.VolleySingletonPattern;
import com.yomorning.lavafood.yomorning.credentials.CredentialProviderClass;
import com.yomorning.lavafood.yomorning.models.RailRestroMenuModel;
import com.yomorning.lavafood.yomorning.models.RailRestroOrderModel;
import com.yomorning.lavafood.yomorning.models.RailRestroVendorsModel;
import com.yomorning.lavafood.yomorning.rectifier.BasicFunctionHandler;
import com.yomorning.lavafood.yomorning.user.UserLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RailRestroCartItemDetailDialog extends AppCompatActivity implements View.OnClickListener{
    HashMap<Integer,RailRestroOrderModel> cartItemInfo;
    double totalPrice;
    int totalItems;
    int day,month,year,hour,minute;
    String cday,cmonth,cyear,chour,cminutes;
    TextView deliveryDate,deliveryTime;
    EditText trainNumber,pnrNumber,coach,seatNumber,fullName,emailAddress,mobileNumber;
    String train,pnr,coa,seat,name,email,mobile,date,time;
    RadioButton cashOnDelivery;
    Button submitOrder;
    TextView totalAmountToBePaid,totalNumberOfItems;
    Calendar calendar;
    BasicFunctionHandler basicFunctionHandler;
    RailRestroVendorsModel vendorsModel;
    String[] orderDetail;
    String orderEndPoint,itemNames,indiVisualPrices;
    SharedPreferences preferences;

    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_order_placing);
        cartItemInfo=(HashMap<Integer,RailRestroOrderModel>)getIntent().getSerializableExtra("cartDetail");
        totalPrice=getIntent().getDoubleExtra("totalPrice",0);
        totalItems=getIntent().getIntExtra("totalItems",0);
        vendorsModel=getIntent().getParcelableExtra("vendorsModel");
        orderDetail=formatOrder(cartItemInfo);

        preferences=getApplicationContext().getSharedPreferences("UserCredential",MODE_PRIVATE);

        calendar=Calendar.getInstance();
        day=calendar.get(Calendar.DAY_OF_MONTH);
        month=calendar.get(Calendar.MONTH);
        year=calendar.get(Calendar.YEAR);
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        minute=calendar.get(Calendar.MINUTE);

        basicFunctionHandler=new BasicFunctionHandler(this);
        deliveryDate=(TextView)findViewById(R.id.date_value);
        deliveryTime=(TextView)findViewById(R.id.delivery_time_value);
        trainNumber=(EditText)findViewById(R.id.train_no);
        pnrNumber=(EditText)findViewById(R.id.pnr_number);

        fullName=(EditText)findViewById(R.id.full_name);
        emailAddress=(EditText)findViewById(R.id.email_address);
        mobileNumber=(EditText)findViewById(R.id.mobile_number);
        coach=(EditText)findViewById(R.id.coach);
        seatNumber=(EditText)findViewById(R.id.seat);

        fullName.setText(preferences.getString("firstName","")+" "+preferences.getString("lastName",""));
        emailAddress.setText(preferences.getString("emailAddress",""));
        mobileNumber.setText(preferences.getString("mobileNumber",""));



        totalAmountToBePaid=(TextView)findViewById(R.id.total_amount_to_be_paid);
        totalNumberOfItems=(TextView)findViewById(R.id.total_number_of_items);

        cashOnDelivery=(RadioButton)findViewById(R.id.cash_on_delivery);
        submitOrder=(Button)findViewById(R.id.submit_order);

        cday=getOrganizedMonth(day+"");
        cmonth=getOrganizedMonth(month+"");
        cyear=year+"";
        chour=getOrganizedMonth(hour+"");
        cminutes=getOrganizedMonth(minute+"");

        deliveryTime.setText(getOrganizedMonth(hour+"")+":"+getOrganizedMonth(minute+"")+":"+"00");
        deliveryDate.setText(year+"-"+getOrganizedMonth(month+"")+"-"+getOrganizedMonth(day+""));
        totalNumberOfItems.setText(totalItems+"");
        totalAmountToBePaid.setText("Rs."+totalPrice+"");

        deliveryDate.setOnClickListener(this);
        deliveryTime.setOnClickListener(this);
        submitOrder.setOnClickListener(this);

        dialog=new ProgressDialog(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.date_value:
                setUserSelectedDate();
                break;
            case R.id.delivery_time_value:
                setUserSelectedTime();
                break;
            case R.id.submit_order:
                train=trainNumber.getText().toString().trim();
                pnr=pnrNumber.getText().toString().trim();
                name=fullName.getText().toString().trim();
                email=emailAddress.getText().toString().trim();
                mobile=mobileNumber.getText().toString().trim();
                time=deliveryTime.getText().toString().trim();
                date=deliveryDate.getText().toString().trim();
                coa=coach.getText().toString().trim();
                seat=seatNumber.getText().toString().trim();
                if(train.isEmpty()||pnr.isEmpty()||name.isEmpty()||email.isEmpty()||mobile.isEmpty()||time.isEmpty()||
                        date.isEmpty()||coa.isEmpty()||seat.isEmpty()){
                    basicFunctionHandler.showAlertDialog("Opps!!","Please make sure that you have entered value in  every " +
                            "fields. Thank you");
                }
                else{
                    if(pnrNumber.getText().toString().trim().length()==10){
                        if(mobileNumber.getText().toString().trim().length()==10){
                            if(cashOnDelivery.isChecked()){
                                placeOrder();
                             //   basicFunctionHandler.showAlertDialog("Hello","Now we are ready to place order to RailRestro. But we have to " +
                                 //       " inform them saying we are ready to place order. Once we receive confirmation then we will be able to place order");
                            }
                            else{
                                basicFunctionHandler.showAlertDialog("Select Payment Method","Please select payment method to proceed" +
                                        " to make payment. Thank you.");
                            }
                        }
                        else{
                            basicFunctionHandler.showAlertDialog("Invalid Mobile Number","Mobile must be of length 10 digit.Please" +
                                    " try again. Thank you.");
                        }

                    }
                    else {
                        basicFunctionHandler.showAlertDialog("Invalid PNR Number","Please make sure that you have entered 10 " +
                                " digit PNR number correctly, Thank you.");
                    }

                }
        }
    }

    public void setUserSelectedDate(){
        DatePickerDialog datePickerDialog=new DatePickerDialog(this,datePickerListener,year,month,day);
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            String sday=day+"",smonth=month+1+"";
            smonth=getOrganizedMonth(smonth);
            if (sday.length()!=2){
                sday="0"+sday;
            }
            deliveryDate.setText(year+"-"+smonth+"-"+sday);
        }
    };
    private String getOrganizedMonth(String month){
        if (month.length()!=2){
            month="0"+month;
        }
        return month;
    }

    private void setUserSelectedTime(){
        TimePickerDialog timePickerDialog=new TimePickerDialog(this,timePickerListener,hour,minute,true);
        timePickerDialog.show();
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            deliveryTime.setText(getOrganizedMonth(hour+"")+":"+getOrganizedMonth(minute+"")+":"+"00");
        }
    };


    private String[] formatOrder(HashMap<Integer,RailRestroOrderModel> orderModel){
        int initialCount=0,finalCount=orderModel.size();
        String itemIds="",quantities="",sellingPrices="",itemVats="",itemServiceTax="";
        itemNames="";
        orderModel.size();
        for(int key:orderModel.keySet()){
            RailRestroMenuModel model=orderModel.get(key).getModel();
            if(initialCount==finalCount-1){
                itemIds=itemIds+key;
                itemNames=itemNames+orderModel.get(key).getModel().getItemName();
                quantities=quantities+orderModel.get(key).getItemCount();
                sellingPrices=sellingPrices+model.getSellingPrice();
                itemVats=itemVats+model.getVat();
                itemServiceTax=itemServiceTax+model.getServiceTax();
            }
            else{
                itemNames=itemNames+orderModel.get(key).getModel().getItemName()+"/";
                itemIds=itemIds+key+"/";
                quantities=quantities+orderModel.get(key).getItemCount()+"/";
                sellingPrices=sellingPrices+model.getSellingPrice()+"/";
                itemVats=itemVats+model.getVat()+"/";
                itemServiceTax=itemServiceTax+model.getServiceTax()+"/";
                initialCount++;
            }
        }
        indiVisualPrices=sellingPrices;
        String[] details={itemIds,quantities,sellingPrices,itemVats,itemServiceTax};
        return details;
//        String url="https://www.railrestro.com/api/create_order_test/bornb/bornb$3445@/?orderid=45124&pnr=7845789546" +
//                "&delivery_date=2016-05-11&item_id="+itemIds+"&qty="+quantities+"&sellprice="+sellingPrices+"&delivery_time="+
//                "14:00:00&train_no=98493&station=BPL&coach=S3&seat=25&outlet_id=52&discount=0&Modeofpayment=" +
//                "COD&passenger_name=test&passenger_mobile=1234567890&email_id=abc@gmail.com&paidamount=0&" +
//                "dueamount="+totalPrice+"&order_total="+totalPrice+"&elivery_charges=0&item_VAT="+itemVats+"&item_service_tax="+
//                itemServiceTax;
//        Log.e("Format order",url);

    }

    private void placeOrder(){

        orderEndPoint="&pnr="+pnr+"&delivery_date="+date+"&item_id="+orderDetail[0]+"&qty="+orderDetail[1]+"&sellprice="+
                orderDetail[2]+"&delivery_time="+time+"&train_no="+train.toUpperCase()+"&coach="+coa.toUpperCase()+"&outlet_id" +
                "="+vendorsModel.getVendorId()+"&discount=0&Modeofpayment=COD&passenger_name="+name+"&passenger_mobile="+mobile+"&email_id="+
                email+"&paidamount=0&dueamount="+totalPrice+"&order_total="+totalPrice+"&delivery_charges=0&item_VAT="+
                orderDetail[3]+"&item_service_tax="+orderDetail[4];
        orderEndPoint=orderEndPoint.replace(" ","%20");
        Log.e("Final End point",orderEndPoint);


        if(totalPrice<vendorsModel.getMinimumAmount()){
            basicFunctionHandler.showAlertDialog("Less Amount","You can't order food if the total price is less than Rs."+
            vendorsModel.getMinimumAmount());
        }
        else{
            SimpleDateFormat dateFormat=new SimpleDateFormat("yy-MM-dd,hh:mm:ss");
            try {
                Date deliveryDate=dateFormat.parse(date+","+time);
                Date currentDate=dateFormat.parse(cyear+"-"+cmonth+"-"+cday+","+chour+":"+cminutes+":00");
                long time=deliveryDate.getTime()-currentDate.getTime();
                long minimumTime=vendorsModel.getOrderTiming()*60*1000;
                if(time>=minimumTime){
                    sendRequestToServer();
                }
                else{
                    basicFunctionHandler.showAlertDialog("Order Timing","Sorry you can't order food for this time. You have to order " +
                            " food "+vendorsModel.getOrderTiming()+" minutes before reaching the food delivery station. Please try again." +
                            " Thank you.");

                }

            } catch (ParseException e) {
                Log.e("ParseException",e.getMessage());
            }
        }

    }

    private void sendRequestToServer() {
        dialog.setMessage("processing...");
        dialog.show();
        String token=preferences.getString("token",null);
        String url= CredentialProviderClass.HOST_NAME+"/rail_restro_buy?token="+token;
        JSONObject object=new JSONObject();
        try {
            object.put("food_order_url",orderEndPoint);
            object.put("food_item_names",itemNames);
            object.put("prices",indiVisualPrices);
            object.put("total_price",totalPrice);
            object.put("delivery_date",date+","+time);
            object.put("order_date",cyear+"-"+cmonth+"-"+cday+", "+chour+":"+cminutes+":"+"00");
            Log.e("order",object.toString());
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    parseJson(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    basicFunctionHandler.showAlertDialog(getString(R.string.volley_exception_title),
                            getString(R.string.volley_exception_message));
                    Log.e("VolleyError",error.getMessage());
                }
            });
            VolleySingletonPattern.getInstance(RailRestroCartItemDetailDialog.this).addToRequestQueue(request);
        } catch (JSONException e) {
            dialog.dismiss();
           Log.e("JSONEXception",e.getMessage());
        }
    }

    private void parseJson(JSONObject response) {
        try {
            int code=response.getInt("code");
            if(code==201){
                dialogForVerificationInput("Success",response.getString("message"));
            }
            else{
                basicFunctionHandler.showAlertDialog(getString(R.string.server_error_title),
                        getString(R.string.server_error_message));
            }
        } catch (JSONException e) {
            Log.e("JSONException",e.getMessage());
        }
    }
    private void dialogForVerificationInput(String title,String message){
        final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(RailRestroCartItemDetailDialog.this, MainActivity.class));
                finish();
            }
        });
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
    }
}
