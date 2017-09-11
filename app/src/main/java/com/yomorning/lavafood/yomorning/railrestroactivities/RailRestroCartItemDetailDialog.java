package com.yomorning.lavafood.yomorning.railrestroactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.yomorning.lavafood.yomorning.MainActivity;
import com.yomorning.lavafood.yomorning.R;

public class RailRestroCartItemDetailDialog extends AppCompatActivity implements View.OnClickListener{
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.railrestro_cart_item_detail);
        textView=(TextView)findViewById(R.id.textView1);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.textView1){
            Intent intent=new Intent(RailRestroCartItemDetailDialog.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
