package com.yomorning.lavafood.yomorning.user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.yomorning.lavafood.yomorning.R;

public class UserProfile extends AppCompatActivity {
    ImageView profileImage;
    TextView name,email,mobileNumber;
    String firstName,lastName;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        name=(TextView)findViewById(R.id.full_name);
        email=(TextView)findViewById(R.id.email_address);
        mobileNumber=(TextView)findViewById(R.id.mobile_number);
        profileImage=(ImageView)findViewById(R.id.profile_image);
        setViews();
    }

    private void setViews() {
        preferences=getApplicationContext().getSharedPreferences("UserCredential",MODE_PRIVATE);

        Resources resource=getResources();
        Bitmap bitmap= BitmapFactory.decodeResource(resource, R.drawable.home_delivery_food);
        RoundedBitmapDrawable factory= RoundedBitmapDrawableFactory.create(resource,bitmap);
        factory.setCornerRadius(Math.min(bitmap.getWidth(),bitmap.getHeight())/2.0f);
        factory.setCircular(true);
        profileImage.setImageDrawable(factory);

        firstName=preferences.getString("firstName","Guest").toUpperCase();
        lastName= preferences.getString("lastName","User").toUpperCase();
        name.setText(firstName+" "+lastName);
        email.setText(preferences.getString("emailAddress","No email"));
        mobileNumber.setText(preferences.getString("mobileNumber","No mobile Number"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.edit_profile:
                Intent intent=new Intent(UserProfile.this,EditProfile.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            if(resultCode==Activity.RESULT_OK){
                setViews();
//                name.setText(data.getStringExtra("firstName")+" "+data.getStringExtra("lastName"));
//                mobileNumber.setText(data.getStringExtra("mobileNumber"));
            }
        }
    }
}
