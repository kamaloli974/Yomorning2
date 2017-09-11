package com.yomorning.lavafood.yomorning.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.models.RailRestroMenuModel;

import java.util.ArrayList;

/**
 * Created by KAMAL OLI on 06/09/2017.
 */

public class RailRestroMenuAdapter extends RecyclerView.Adapter<RailRestroMenuAdapter.MenuViewHolder> {
    ArrayList<RailRestroMenuModel> menuModelArrayList;
    Context context;
    LayoutInflater inflater;
    OnMenuItemClickedListener menuItemClickedListner;
    public RailRestroMenuAdapter(Context context,ArrayList<RailRestroMenuModel> menuModelArrayList){
        this.context=context;
        if(context instanceof OnMenuItemClickedListener){
            menuItemClickedListner=(OnMenuItemClickedListener)context;
        }
        else{
            throw new RuntimeException(context.toString()+" must implement OnMenuItemClickedListener");
        }
        this.menuModelArrayList=menuModelArrayList;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root=inflater.inflate(R.layout.railrestro_menu_single_item_view,parent,false);
        return new MenuViewHolder(root);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        holder.populateView(position);
    }

    @Override
    public int getItemCount() {
        return menuModelArrayList.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView foodCategory,foodItemName,foodItemDescription,foodItemPrice,foodItemDescriptionTag;
        Button addItem;
        public MenuViewHolder(View rootView) {
            super(rootView);
            foodCategory=rootView.findViewById(R.id.food_category);
            foodItemName=rootView.findViewById(R.id.food_item_name);
            foodItemDescription=rootView.findViewById(R.id.food_item_description_value);
            foodItemPrice=rootView.findViewById(R.id.food_item_price);
            foodItemDescriptionTag=rootView.findViewById(R.id.food_item_description);
            addItem=rootView.findViewById(R.id.add_item);
            addItem.setOnClickListener(this);
        }

        public void populateView(int position) {
            RailRestroMenuModel model=menuModelArrayList.get(getAdapterPosition());
            foodCategory.setText(model.getCategoryName());
            foodItemName.setText(model.getItemName());
            foodItemPrice.setText("  Rs "+model.getSellingPrice());
            if(model.getFoodItemDescription().isEmpty()){
                foodItemDescription.setVisibility(View.GONE);
                foodItemDescriptionTag.setVisibility(View.GONE);
            }
            else{
                foodItemDescription.setText(model.getFoodItemDescription());
            }
        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.add_item){
                menuItemClickedListner.getSelectedItem(menuModelArrayList.get(getAdapterPosition()));
            }
            else{
                Log.e("Other item ","clicked");
            }
        }
    }

    public interface OnMenuItemClickedListener{
        void getSelectedItem(RailRestroMenuModel model);
    }
}
