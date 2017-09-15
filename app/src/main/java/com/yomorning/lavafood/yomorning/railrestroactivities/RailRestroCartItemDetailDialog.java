package com.yomorning.lavafood.yomorning.railrestroactivities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yomorning.lavafood.yomorning.MainActivity;
import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.models.RailRestroMenuModel;
import com.yomorning.lavafood.yomorning.models.RailRestroOrderModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class RailRestroCartItemDetailDialog extends AppCompatActivity implements View.OnClickListener{
    HashMap<Integer,RailRestroOrderModel> cartItemInfo;
    double totalPrice;
    int totalItems;
    int day,month,year,hour,minute;
    EditText deliveryDate,deliveryTime;
    Calendar calendar;
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
        deliveryDate=(EditText)findViewById(R.id.date_value);
        deliveryTime=(EditText)findViewById(R.id.delivery_time_value);
        deliveryTime.setText(hour+":"+minute);
        deliveryDate.setText(day+"/"+getOrganizedMonth(month+"")+"/"+year);
        deliveryDate.setOnClickListener(this);
        deliveryTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.date_value:
                setUserSelectedDate();
                break;
            case R.id.delivery_time_value:
                setUserSelectedTime();
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
            deliveryTime.setText(hour+":"+minute);
        }
    };
}
