package com.yomorning.lavafood.yomorning.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.models.RailRestroOrderModel;

import java.util.HashMap;

/**
 * Created by KAMAL OLI on 05/09/2017.
 */

public class RailRestroFoodOrderSystem extends DialogFragment {
    View indivisualView;
    HashMap<Integer,RailRestroOrderModel> orderModelHashMap;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
