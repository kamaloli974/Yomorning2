package com.yomorning.lavafood.yomorning.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.adapters.RailCartItemDisplay;
import com.yomorning.lavafood.yomorning.models.RailRestroOrderModel;

import java.util.HashMap;

/**
 * Created by KAMAL OLI on 05/09/2017.
 */

public class RailRestroFoodOrderSystem extends DialogFragment {
    View indivisualView;
    HashMap<Integer,RailRestroOrderModel> orderModelHashMap;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        orderModelHashMap=(HashMap<Integer,RailRestroOrderModel>)savedInstanceState.getSerializable("cartDetail");
        view=inflater.inflate(R.layout.railrestro_cart_item_detail,container,false);
        setOrderDetailToRecyclerView(view);
        return view;
    }

    private void setOrderDetailToRecyclerView(View view){
        RailCartItemDisplay cartItemDisplayAdapter=new RailCartItemDisplay(getActivity(),orderModelHashMap);
        RecyclerView cartDetailRecyclerView=view.findViewById(R.id.cart_detail_recycler_view);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        cartDetailRecyclerView.setLayoutManager(manager);
        cartDetailRecyclerView.setAdapter(cartItemDisplayAdapter);
    }
}
