package com.yomorning.lavafood.yomorning.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.models.RailRestroOrderModel;

import java.util.ArrayList;
import java.util.HashMap;


public class RailCartItemDisplay extends RecyclerView.Adapter<RailCartItemDisplay.ItemViewHolder> {
    Context context;
    HashMap<Integer,RailRestroOrderModel> cartDetail;
    ArrayList<Integer> keySet;
    OnCartItemChangedListenerFragment fragmentCommunicator;
    int totalItems;
    private LayoutInflater inflater;
    double totalPrice;
    View rootView;
    public RailCartItemDisplay(Context context, HashMap<Integer,RailRestroOrderModel> hashMap,int totalItems,
                               double totalPrice,OnCartItemChangedListenerFragment fragmentCommunicator){
        this.context=context;
        this.totalItems=totalItems;
        this.totalPrice=totalPrice;
        this.fragmentCommunicator=fragmentCommunicator;
        cartDetail=hashMap;
        keySet=new ArrayList<>();
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(Integer key: cartDetail.keySet()){
            keySet.add(key);
        }

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rootView=inflater.inflate(R.layout.itmes_added_to_cart_detail_single,parent,false);
        return new ItemViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.displayCartItem(position);
    }

    @Override
    public int getItemCount() {
        return keySet.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView itemName,itemCount,itemPrice;
        ImageButton addItem,removeItem;
        double price;
        int count,key;
        RailRestroOrderModel orderModel;
        public ItemViewHolder(View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.item_name);
            itemCount=itemView.findViewById(R.id.item_count);
            itemPrice=itemView.findViewById(R.id.item_price);
            addItem=itemView.findViewById(R.id.add_item);
            removeItem=itemView.findViewById(R.id.remove_item);
            addItem.setOnClickListener(this);
            removeItem.setOnClickListener(this);
        }
        public void displayCartItem(int position){
            key=keySet.get(position);
            orderModel=cartDetail.get(key);
            count=orderModel.getItemCount();
            price=orderModel.getModel().getSellingPrice();
            itemName.setText(orderModel.getModel().getItemName());
            itemCount.setText(count+"");
            itemPrice.setText("Rs."+(price*count));
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.add_item:
                    count=count+1;
                    totalItems=totalItems+1;
                    totalPrice=totalPrice+orderModel.getModel().getSellingPrice();
                    orderModel.setItemCount(count);
                    cartDetail.put(key,orderModel);
                    fragmentCommunicator.getChangedCartDetail(cartDetail,totalItems,totalPrice);
                    itemCount.setText(count+"");
                    itemPrice.setText("Rs."+(price*count));
                    break;
                case R.id.remove_item:
                    if(count-1==0){
                        totalItems=totalItems-1;
                        totalPrice=totalPrice-orderModel.getModel().getSellingPrice();
                        fragmentCommunicator.getChangedCartDetail(cartDetail,totalItems,totalPrice);
                        cartDetail.remove(keySet.get(getAdapterPosition()));
                        keySet.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(),keySet.size());
                    }
                    else{
                        count=count-1;
                        totalItems=totalItems-1;
                        totalPrice=totalPrice-orderModel.getModel().getSellingPrice();
                        orderModel.setItemCount(count);
                        cartDetail.put(key,orderModel);
                        fragmentCommunicator.getChangedCartDetail(cartDetail,totalItems,totalPrice);
                        itemCount.setText(count+"");
                        itemPrice.setText("Rs."+(price*count));
                    }
                    break;
            }
        }
    }

    public interface OnCartItemChangedListenerFragment{
        void getChangedCartDetail(HashMap<Integer,RailRestroOrderModel> changedOrder,int totalItems,double totalPrice);
    }
}
