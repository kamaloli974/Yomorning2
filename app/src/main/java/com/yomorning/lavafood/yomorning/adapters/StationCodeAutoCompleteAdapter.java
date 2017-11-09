package com.yomorning.lavafood.yomorning.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yomorning.lavafood.yomorning.models.StationCodeModel;

import java.util.ArrayList;

/**
 * Created by KAMAL OLI on 29/10/2017.
 */

public class StationCodeAutoCompleteAdapter extends BaseAdapter {

    ArrayList<StationCodeModel> stationList;
    Context context;
    LayoutInflater inflater;

    public StationCodeAutoCompleteAdapter(Context context,ArrayList<StationCodeModel> stationList){
        this.context=context;
        this.stationList=stationList;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return stationList.size();
    }

    @Override
    public Object getItem(int i) {
        return stationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
