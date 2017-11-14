package com.yomorning.lavafood.yomorning.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.filters.RailRestroFoodMenuSearchFilter;
import com.yomorning.lavafood.yomorning.models.RailRestroMenuModel;

import java.util.ArrayList;

/**
 * Created by KAMAL OLI on 06/09/2017.
 */

public class RailRestroMenuAdapter extends RecyclerView.Adapter<RailRestroMenuAdapter.MenuViewHolder> implements Filterable{
    ArrayList<RailRestroMenuModel> menuModelArrayList,originalData,filterAppliedData;
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
        this.originalData=menuModelArrayList;
        this.filterAppliedData=menuModelArrayList;
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

    public void displayDataForSearchResult(ArrayList<RailRestroMenuModel> searchedItems) {
        menuModelArrayList=searchedItems;

    }

    @Override
    public Filter getFilter() {
        return RailRestroFoodMenuSearchFilter.newInstance(context,originalData,this);
    }

    public void showAllData() {
        menuModelArrayList=originalData;
        Log.e("showAllData","showAllData");
    }

    public void showVegItems() {
        filterAppliedData=new ArrayList<>();
        for(int i=0;i<originalData.size();i++){
            if(originalData.get(i).getCategoryId()==0){
                filterAppliedData.add(originalData.get(i));
            }
        }
        menuModelArrayList=filterAppliedData;
        Log.e("showVegItems","showVegItems");
    }

    public void showNonVegItems() {
        filterAppliedData=new ArrayList<>();
        for(int i=0;i<originalData.size();i++){
            if(originalData.get(i).getCategoryId()==1){
                filterAppliedData.add(originalData.get(i));
            }
        }
        menuModelArrayList=filterAppliedData;
        Log.e("showNonVegItems","showNonVegItems");
    }

    public void showNothing() {
        filterAppliedData=new ArrayList<>();
        menuModelArrayList=filterAppliedData;
        Log.e("showNothing","showNothing");
    }

    class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView foodCategory,foodItemName,foodItemDescription,foodItemPrice,singleItemCount;
        ImageView addItem,removeItem,foodCategoryImage;
        public MenuViewHolder(View rootView) {
            super(rootView);
            foodCategory=rootView.findViewById(R.id.food_category);
            foodItemName=rootView.findViewById(R.id.food_item_name);
            foodItemDescription=rootView.findViewById(R.id.food_item_description_value);
            foodItemPrice=rootView.findViewById(R.id.food_item_price);
            addItem=rootView.findViewById(R.id.add_item);
            removeItem=rootView.findViewById(R.id.remove_item);
            foodCategoryImage=rootView.findViewById(R.id.category_image_view);
            singleItemCount=rootView.findViewById(R.id.single_item_count);
            removeItem.setOnClickListener(this);
            addItem.setOnClickListener(this);
        }

        public void populateView(int position) {
            RailRestroMenuModel model=menuModelArrayList.get(getAdapterPosition());
            foodCategory.setText(model.getCategoryName());
            foodItemName.setText(model.getItemName());
            singleItemCount.setText("0");
            foodItemPrice.setText(context.getString(R.string.rupee_symbol)+" "+model.getSellingPrice());
            if(model.getFoodItemDescription().isEmpty()){
                foodItemDescription.setVisibility(View.GONE);

            }
            else{
                foodItemDescription.setVisibility(View.VISIBLE);
                foodItemDescription.setText(model.getFoodItemDescription());
            }
            if (model.getCategoryId()==0){
                foodCategoryImage.setImageDrawable(context.getResources().getDrawable(R.mipmap.vegetarian_icon));
            }
            else{
                foodCategoryImage.setImageDrawable(context.getResources().getDrawable(R.mipmap.non_vegeterian_icon));
            }
        }

        @Override
        public void onClick(View view) {

            switch(view.getId()){
                case R.id.add_item:
                    int count=Integer.parseInt(singleItemCount.getText().toString().trim());
                    count=count+1;
                    singleItemCount.setText(count+"");
                    menuItemClickedListner.addItemToCart(menuModelArrayList.get(getAdapterPosition()));
                    break;
                case R.id.remove_item:
                    int itemCount=Integer.parseInt(singleItemCount.getText().toString().trim());
                    if (itemCount==0){

                    }
                    else{
                        itemCount=itemCount-1;
                        singleItemCount.setText(itemCount+"");
                        menuItemClickedListner.removeItemFromCart(menuModelArrayList.get(getAdapterPosition()));
                    }
                    break;
            }
        }
    }

    public interface OnMenuItemClickedListener{
        void addItemToCart(RailRestroMenuModel model);
        void removeItemFromCart(RailRestroMenuModel model);
    }
}
