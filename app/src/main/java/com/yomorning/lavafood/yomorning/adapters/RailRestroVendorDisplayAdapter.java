package com.yomorning.lavafood.yomorning.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yomorning.lavafood.yomorning.MainActivity;
import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.VolleySingletonPattern;
import com.yomorning.lavafood.yomorning.models.RailRestroMenuModel;
import com.yomorning.lavafood.yomorning.models.RailRestroVendorsModel;
import com.yomorning.lavafood.yomorning.railrestroactivities.RailRestroMenuActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KAMAL OLI on 05/09/2017.
 */

public class RailRestroVendorDisplayAdapter extends RecyclerView.Adapter<RailRestroVendorDisplayAdapter.VendorViewHolder> {
    Context context;
    ArrayList<RailRestroVendorsModel> railRestroVendorsList;
    LayoutInflater inflater;
    RailRestroVendorsModel model;
    public RailRestroVendorDisplayAdapter(Context context,ArrayList<RailRestroVendorsModel> railRestroVendorsList){
        this.context=context;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.railRestroVendorsList=railRestroVendorsList;
    }
    @Override
    public VendorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VendorViewHolder(inflater.inflate(R.layout.rail_restros_vendors_single,parent,false));
    }
    @Override
    public void onBindViewHolder(VendorViewHolder holder, int position) {
        holder.setViews(position);
    }

    @Override
    public int getItemCount() {
        return railRestroVendorsList.size();
    }

    class VendorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vendorsName,openingTime,closingTime,minimumOrderAmount,contactNumber,foodTypesAvailable,delivery_time;
        Button selectVendor;
        ImageView categoryImageView;

        public VendorViewHolder(View itemView) {
            super(itemView);
            vendorsName=itemView.findViewById(R.id.vendors_name);
            closingTime=itemView.findViewById(R.id.closing_time_value);
            minimumOrderAmount=itemView.findViewById(R.id.value);
            contactNumber=itemView.findViewById(R.id.contact_value);
            foodTypesAvailable=itemView.findViewById(R.id.food_types_value);
            categoryImageView=itemView.findViewById(R.id.category_image_view);
            selectVendor=itemView.findViewById(R.id.select_vendor);
            delivery_time=itemView.findViewById(R.id.delivery_time_value);
            selectVendor.setOnClickListener(this);
        }

        public void setViews(int position) {
            model=railRestroVendorsList.get(position);
            vendorsName.setText(model.getCompanyName());
            closingTime.setText(model.getOpeningTime()+"-"+model.getClosingTime());
            minimumOrderAmount.setText(context.getString(R.string.rupee_symbol)+" "+model.getMinimumAmount()+"");
            contactNumber.setText("+91 "+model.getMobileNumber());
            delivery_time.setText(model.getOrderTiming()+" min");
            if(model.getMealTypes().equals("Both")){
                foodTypesAvailable.setText("Veg/Non-Veg");
                categoryImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.non_vegeterian_icon));
            }
            else if (model.getMealTypes().equals("Veg")){
                foodTypesAvailable.setText("Veg");
                categoryImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.vegetarian_icon));

            }
            else{
                foodTypesAvailable.setText(model.getMealTypes());
                categoryImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.non_vegeterian_icon));
            }

        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.select_vendor){
                Intent intent=new Intent(context, RailRestroMenuActivity.class);
                intent.putExtra("railRestroVendorsModel",railRestroVendorsList.get(getAdapterPosition()));
                context.startActivity(intent);
                Log.e("position i",railRestroVendorsList.get(getAdapterPosition()).getVendorId()+" "+getAdapterPosition());
            }
        }
    }
}
