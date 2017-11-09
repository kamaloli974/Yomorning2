package com.yomorning.lavafood.yomorning.filters;

import android.content.Context;
import android.widget.Filter;

import com.yomorning.lavafood.yomorning.adapters.RailRestroMenuAdapter;
import com.yomorning.lavafood.yomorning.models.RailRestroMenuModel;

import java.util.ArrayList;

/**
 * Created by KAMAL OLI on 27/10/2017.
 */

public class RailRestroFoodMenuSearchFilter extends Filter {
    Context context;
    ArrayList<RailRestroMenuModel> menuModelArrayList;
    static RailRestroMenuAdapter menuAdapter;


    private RailRestroFoodMenuSearchFilter(Context context, ArrayList<RailRestroMenuModel> menuModelArrayList,RailRestroMenuAdapter
                                           menuAdapter){
        this.context=context;
        this.menuModelArrayList=menuModelArrayList;
        RailRestroFoodMenuSearchFilter.menuAdapter=menuAdapter;
    }

    public static Filter newInstance(Context context, ArrayList<RailRestroMenuModel> menuModelArrayList
            ,RailRestroMenuAdapter menuAdapter){
        return new RailRestroFoodMenuSearchFilter(context,menuModelArrayList,menuAdapter);
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filteredResults=new FilterResults();
        ArrayList<RailRestroMenuModel> foundResults=new ArrayList<>();
        if(charSequence!=null&&charSequence.length()>0){
            charSequence=charSequence.toString().toUpperCase();
            RailRestroMenuModel singleModel;
            for(int i=0;i<menuModelArrayList.size();i++){
                singleModel=menuModelArrayList.get(i);
                if(singleModel.getItemName().toUpperCase().contains(charSequence)){
                    foundResults.add(singleModel);
                }
            }
            filteredResults.count=foundResults.size();
            filteredResults.values=foundResults;
        }
        else{
            filteredResults.count=menuModelArrayList.size();
            filteredResults.values=menuModelArrayList;
        }
        return filteredResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        menuAdapter.displayDataForSearchResult((ArrayList<RailRestroMenuModel>) filterResults.values);
        menuAdapter.notifyDataSetChanged();
    }
}
