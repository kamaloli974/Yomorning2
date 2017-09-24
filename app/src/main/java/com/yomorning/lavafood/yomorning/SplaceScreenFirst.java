package com.yomorning.lavafood.yomorning;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplaceScreenFirst extends AppCompatActivity {
    TextView brandHeading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splace_screen_presentation);
        brandHeading=(TextView)findViewById(R.id.brand_heading);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.splace_screen_first);
        brandHeading.startAnimation(animation);
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    startActivity(new Intent(SplaceScreenFirst.this,PresentationActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    Log.e("InterruptedException",e.getMessage());
                }
            }
        });
        thread.start();
    }
}
