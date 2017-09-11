package com.yomorning.lavafood.yomorning;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.yomorning.lavafood.yomorning.adapters.ServiceSelectionChoiceAdapter;
import com.yomorning.lavafood.yomorning.models.ServiceSelectionChoiceModel;

import java.util.ArrayList;

public class PresentationActivity extends AppCompatActivity{
    String [] ourService={"Order Food From Home","Order Delicious Food While You Are Travelling","Order Ready To Cook Food and " +
            "Make Delicious Food At Home"};
    String[] imageUrlArray={"ht","ht","ht"};
    int[] imageId={R.drawable.home_delivery_food,R.drawable.travelling_food_background,R.drawable.ready_cook};
    ArrayList<ServiceSelectionChoiceModel> choiceArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        SharedPreferences preferences=getApplicationContext().getSharedPreferences("Yomorning", Context.MODE_PRIVATE);
        if(preferences.contains("AppOpenedFirstTime")){
            startActivity(new Intent(PresentationActivity.this,MainActivity.class));
            finish();
        }
        else{
            choiceArrayList=new ArrayList<>();
            setAdapterForPresentation();
        }

    }

    private void setAdapterForPresentation() {
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view_choice);
        ServiceSelectionChoiceAdapter adapter=new ServiceSelectionChoiceAdapter(PresentationActivity.this,
                initializeDataToArrayList());
        LinearLayoutManager manager=new LinearLayoutManager(PresentationActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<ServiceSelectionChoiceModel> initializeDataToArrayList(){
        for(int i=0;i<ourService.length;i++){
            final ServiceSelectionChoiceModel model=new ServiceSelectionChoiceModel();
            model.setLabel(ourService[i]);
            model.setChoiceId(i);
            model.setImageId(R.drawable.ready_cook);
            model.setImageId(imageId[i]);
            //String url=imageUrlArray[i];
//            ImageRequest imageRequest=new ImageRequest(url, new Response.Listener<Bitmap>() {
//                @Override
//                public void onResponse(Bitmap response) {
//                    model.setBackgroundImage(response);
//                }
//            }, 0, 0, null, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("VolleyError","message="+error.getMessage()+" "+error.getCause());
//                }
//            });
//            VolleySingletonPattern.getInstance(PresentationActivity.this).addToRequestQueue(imageRequest);
            choiceArrayList.add(model);
        }
        return choiceArrayList;
    }
}
