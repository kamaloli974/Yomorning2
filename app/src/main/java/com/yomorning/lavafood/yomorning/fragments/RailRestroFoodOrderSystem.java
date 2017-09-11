package com.yomorning.lavafood.yomorning.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yomorning.lavafood.yomorning.R;

/**
 * Created by KAMAL OLI on 05/09/2017.
 */

public class RailRestroFoodOrderSystem extends Fragment {
    View indivisualView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        indivisualView= inflater.inflate(R.layout.fragment_rail_restro_food_order, container, false);
        return indivisualView;
    }
}
