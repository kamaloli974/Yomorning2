package com.yomorning.lavafood.yomorning.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.adapters.RailCartItemDisplay;
import com.yomorning.lavafood.yomorning.models.RailRestroOrderModel;
import com.yomorning.lavafood.yomorning.models.RailRestroVendorsModel;
import com.yomorning.lavafood.yomorning.railrestroactivities.RailRestroCartItemDetailDialog;

import java.util.HashMap;

/**
 * Created by KAMAL OLI on 05/09/2017.
 */

public class RailRestroFoodOrderSystem extends DialogFragment implements
        RailCartItemDisplay.OnCartItemChangedListenerFragment{

    HashMap<Integer,RailRestroOrderModel> orderModelHashMap;
    View view;
    TextView totalItemsInCart,totalPriceTextView;
    LayoutInflater inflater;
    double totalPrice;
    int totalItems;
    Context context;
    RailRestroVendorsModel vendorsModel;
    OnCartItemChangedListener cartItemChangedListener;
    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        view=inflater.inflate(R.layout.railrestro_cart_item_detail,container);
//        getDialog().setTitle("Order Summary");
//        //setOrderDetailToRecyclerView();
//        return view;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(getArguments()!=null){
            orderModelHashMap=(HashMap<Integer,RailRestroOrderModel>)getArguments().getSerializable("cartDetail");
            totalItems=getArguments().getInt("totalItems");
            totalPrice=getArguments().getDouble("totalPrice");
            vendorsModel=getArguments().getParcelable("vendorsModel");
            Log.e("Cart Detail",orderModelHashMap.size()+"");
        }
    }

    private void setOrderDetailToRecyclerView(){
        RailCartItemDisplay cartItemDisplayAdapter=new RailCartItemDisplay(this.getActivity(),orderModelHashMap,totalItems,
                totalPrice,this);
        RecyclerView cartDetailRecyclerView=view.findViewById(R.id.cart_detail_recycler_view);
        LinearLayoutManager manager=new LinearLayoutManager(this.getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        cartDetailRecyclerView.setLayoutManager(manager);
        cartDetailRecyclerView.setAdapter(cartItemDisplayAdapter);
    }

    public static RailRestroFoodOrderSystem newInstance(HashMap<Integer,RailRestroOrderModel> order,int totalItems,
                                                       double totalPrice,RailRestroVendorsModel model) {
        RailRestroFoodOrderSystem orderSystem = new RailRestroFoodOrderSystem();
        Bundle args = new Bundle();
        args.putSerializable("cartDetail",order);
        args.putInt("totalItems",totalItems);
        args.putDouble("totalPrice",totalPrice);
        args.putParcelable("vendorsModel",model);
        Log.e("newInstance",order.size()+"");
        orderSystem.setArguments(args);
        return orderSystem;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Order Summary");
        dialogBuilder.setIcon(R.mipmap.shopping_cart);
        dialogBuilder.setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(getActivity(), RailRestroCartItemDetailDialog.class);
                intent.putExtra("cartDetail",orderModelHashMap);
                intent.putExtra("totalPrice",totalPrice);
                intent.putExtra("totalItems",totalItems);
                intent.putExtra("vendorsModel",vendorsModel);
                dialogInterface.dismiss();
                startActivity(intent);
            }
        });
        dialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        view=inflater.inflate(R.layout.railrestro_cart_item_detail,null);
        totalItemsInCart=view.findViewById(R.id.total_items);
        totalPriceTextView=view.findViewById(R.id.total_price);
        setOrderDetailToRecyclerView();
        totalItemsInCart.setText(totalItems+"");
        totalPriceTextView.setText("Rs."+totalPrice+"");
        dialogBuilder.setView(view);
        Dialog dialog=dialogBuilder.create();
        return dialog;
    }

    @Override
    public void getChangedCartDetail(HashMap<Integer, RailRestroOrderModel> changedOrder, int
            totalItems, double totalPrice) {
        this.totalItems=totalItems;
        this.totalPrice=totalPrice;
        this.orderModelHashMap=changedOrder;
        totalItemsInCart.setText(totalItems+"");
        totalPriceTextView.setText("Rs."+totalPrice+"");
        cartItemChangedListener.getChangedCartDetail(changedOrder,totalItems,totalPrice);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        if(context instanceof OnCartItemChangedListener){
            cartItemChangedListener=(OnCartItemChangedListener)context;
        }
        else{
            throw new RuntimeException(context.toString()+" must implement OnCartItemChangedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cartItemChangedListener=null;
    }

    public interface OnCartItemChangedListener{
        void getChangedCartDetail(HashMap<Integer,RailRestroOrderModel> changedOrder,int totalItems,double totalPrice);
    }

}

//dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//@Override
//public void onShow(DialogInterface dialogInterface) {
//        Button positive=((AlertDialog)dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
//        Button negative=((AlertDialog)dialogInterface).getButton(AlertDialog.BUTTON_NEGATIVE);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//        LinearLayout.LayoutParams.WRAP_CONTENT,
//        LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        params.setMargins(20,0,0,0);
//        positive.setLayoutParams(params);
//        negative.setLayoutParams(params);
//        positive.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
//        negative.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
//        positive.setBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
//        negative.setBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
//        positive.setTextSize(16);
//        negative.setTextSize(16);
//        }
//        });
