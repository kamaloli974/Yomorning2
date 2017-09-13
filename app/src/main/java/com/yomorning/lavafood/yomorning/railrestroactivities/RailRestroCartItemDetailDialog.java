package com.yomorning.lavafood.yomorning.railrestroactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yomorning.lavafood.yomorning.MainActivity;
import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.models.RailRestroMenuModel;
import com.yomorning.lavafood.yomorning.models.RailRestroOrderModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class RailRestroCartItemDetailDialog extends AppCompatActivity implements View.OnClickListener{
    HashMap<Integer,RailRestroOrderModel> cartValue;
    Button view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.railrestro_cart_item_detail);
        view=(Button)findViewById(R.id.textView1);
        view.setOnClickListener(this);
        cartValue= (HashMap<Integer, RailRestroOrderModel>) getIntent().getSerializableExtra("cart");
        Collection<Integer> iterator=cartValue.keySet();
        int count=-1;
        for(Integer keys: iterator){
            count++;
            RailRestroOrderModel model=cartValue.get(keys);
            Log.e("item Id",model.getItemId()+"");
            Log.e("Number of items",model.getItemCount()+"");
            RailRestroMenuModel mo=model.getModel();
            Log.e("MenuObject",mo.getItemName());
            if(count==cartValue.size()){
                cartValue.remove(keys);
            }
        }
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.textView1){
            Intent intent=new Intent();
            intent.putExtra("orderDetail",cartValue);
            setResult(1,intent);
        }

    }

}
