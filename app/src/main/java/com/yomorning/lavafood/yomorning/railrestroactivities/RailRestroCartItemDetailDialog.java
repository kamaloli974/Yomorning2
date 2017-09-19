package com.yomorning.lavafood.yomorning.railrestroactivities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.models.RailRestroOrderModel;
import com.yomorning.lavafood.yomorning.rectifier.BasicFunctionHandler;

import java.util.Calendar;
import java.util.HashMap;

public class RailRestroCartItemDetailDialog extends AppCompatActivity implements View.OnClickListener{
    HashMap<Integer,RailRestroOrderModel> cartItemInfo;
    double totalPrice;
    int totalItems;
    int day,month,year,hour,minute;
    EditText deliveryDate,deliveryTime;
    EditText trainNumber,pnrNumber,coach,seatNumber,fullName,emailAddress,mobileNumber;
    RadioButton cashOnDelivery;
    Button submitOrder;
    TextView totalAmountToBePaid,totalNumberOfItems;
    Calendar calendar;
    BasicFunctionHandler basicFunctionHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_order_placing);

        cartItemInfo=(HashMap<Integer,RailRestroOrderModel>)getIntent().getSerializableExtra("cartDetail");
        totalPrice=getIntent().getDoubleExtra("totalPrice",0);
        totalItems=getIntent().getIntExtra("totalItems",0);
        calendar=Calendar.getInstance();
        day=calendar.get(Calendar.DAY_OF_MONTH);
        month=calendar.get(Calendar.MONTH);
        year=calendar.get(Calendar.YEAR);
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        minute=calendar.get(Calendar.MINUTE);
        basicFunctionHandler=new BasicFunctionHandler(this);
        deliveryDate=(EditText)findViewById(R.id.date_value);
        deliveryTime=(EditText)findViewById(R.id.delivery_time_value);
        trainNumber=(EditText)findViewById(R.id.train_no);
        pnrNumber=(EditText)findViewById(R.id.pnr_number);
        fullName=(EditText)findViewById(R.id.full_name);
        emailAddress=(EditText)findViewById(R.id.email_address);
        mobileNumber=(EditText)findViewById(R.id.mobile_number);
        coach=(EditText)findViewById(R.id.coach);
        seatNumber=(EditText)findViewById(R.id.seat);


        totalAmountToBePaid=(TextView)findViewById(R.id.total_amount_to_be_paid);
        totalNumberOfItems=(TextView)findViewById(R.id.total_number_of_items);

        cashOnDelivery=(RadioButton)findViewById(R.id.cash_on_delivery);
        submitOrder=(Button)findViewById(R.id.submit_order);

        deliveryTime.setText(getOrganizedMonth(hour+"")+":"+getOrganizedMonth(minute+""));
        deliveryDate.setText(day+"/"+getOrganizedMonth(month+"")+"/"+year);
        totalNumberOfItems.setText(totalItems+"");
        totalAmountToBePaid.setText("Rs."+totalPrice+"");

        deliveryDate.setOnClickListener(this);
        deliveryTime.setOnClickListener(this);
        submitOrder.setOnClickListener(this);
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
                if(trainNumber.getText().toString().trim().isEmpty()||pnrNumber.getText().toString().trim().isEmpty()||
                        fullName.getText().toString().trim().isEmpty()||emailAddress.getText().toString().trim().isEmpty()||
                        mobileNumber.getText().toString().trim().isEmpty()||deliveryTime.getText().toString().trim().isEmpty()||
                        deliveryDate.getText().toString().trim().isEmpty()||coach.getText().toString().trim().isEmpty()
                        ||seatNumber.getText().toString().trim().isEmpty()){
                    basicFunctionHandler.showAlertDialog("Opps!!","Please make sure that you have entered value in  every " +
                            "fields. Thank you");
                }
                else{
                    if(pnrNumber.getText().toString().trim().length()==10){
                        if(mobileNumber.getText().toString().trim().length()==10){
                            if(cashOnDelivery.isChecked()){
                                basicFunctionHandler.showAlertDialog("Hello","Now we are ready to place order to RailRestro. But we have to " +
                                        " inform them saying we are ready to place order. Once we receive confirmation then we will be able to place order");
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
            deliveryDate.setText(sday+"/"+smonth+"/"+year);
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
            deliveryTime.setText(getOrganizedMonth(hour+"")+":"+getOrganizedMonth(minute+""));
        }
    };
}

//https://www.railrestro.com/api/create_order_test/YOUR_USER/YOUR_KEY/?orderid=12345&pnr=1
//        234567890&delivery_date=2016-05-11&item_id=1080/1096&qty=2/3&sellprice=95/175&delivery_ti
//        me=14:00:00&train_no=12137&station=BPL&coach=S3&seat=25&outlet_id=52&discount=0&Modeo
//        fpayment=COD&passenger_name=test&passenger_mobile=1234567890&email_id=abc@gmail.com
//&paidamount=0&dueamount=789&order_total=789&delivery_charges=0&item_VAT=5/9&item_serv
//        ice_tax=5/9
