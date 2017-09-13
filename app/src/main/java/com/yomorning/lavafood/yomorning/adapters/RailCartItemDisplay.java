package com.yomorning.lavafood.yomorning.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yomorning.lavafood.yomorning.models.RailRestroOrderModel;

import java.util.ArrayList;
import java.util.HashMap;


public class RailCartItemDisplay extends RecyclerView.Adapter<RailCartItemDisplay.ItemViewHolder> {
    Context context;
    HashMap<Integer,RailRestroOrderModel> cartDetail;
    ArrayList<Integer> keySet;
    OnCartItemChangedListener cartItemChangedListener;
    public RailCartItemDisplay(Context context, HashMap<Integer,RailRestroOrderModel> hashMap){
        this.context=context;
        cartDetail=hashMap;
        keySet=new ArrayList<>();
        for(Integer key: cartDetail.keySet()){
            keySet.add(key);
        }
        if(context instanceof OnCartItemChangedListener){
            cartItemChangedListener=(OnCartItemChangedListener)context;
        }
        else{
            throw new RuntimeException(context.toString()+" must implement OnCartItemChangedListener");
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return keySet.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnCartItemChangedListener{
        void getChangedCartDetail(HashMap<Integer,RailRestroOrderModel> changedOrder);
    }
}
