package com.yomorning.lavafood.yomorning.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.models.OrderHistoryModel;

import java.util.ArrayList;

/**
 * Created by KAMAL OLI on 27/09/2017.
 */

public class PlaceOrder extends RecyclerView.Adapter<PlaceOrder.OrderViewHolder>{
    ArrayList<OrderHistoryModel> models;
    Context context;
    LayoutInflater inflater;

    public PlaceOrder(ArrayList<OrderHistoryModel> models,Context context){
        this.context=context;
        this.models=models;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public PlaceOrder.OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.place_order_single,parent,false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceOrder.OrderViewHolder holder, int position) {
        holder.setViews();
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView deliveryDate,orderedDate,itemNames,totalPrice;
        public OrderViewHolder(View itemView) {
            super(itemView);
            deliveryDate=itemView.findViewById(R.id.delivery_date);
            orderedDate=itemView.findViewById(R.id.order_date);
            itemNames=itemView.findViewById(R.id.food_items);
            totalPrice=itemView.findViewById(R.id.total_price);
        }
        public void setViews(){
            OrderHistoryModel model=models.get(getAdapterPosition());
            deliveryDate.setText(model.getDeliveryTime());
            orderedDate.setText(model.getOrderedTime());
            itemNames.setText(model.getNames());
            totalPrice.setText("Rs."+model.getTotalPrice());
        }
    }
}
