package com.yomorning.lavafood.yomorning.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yomorning.lavafood.yomorning.MainActivity;
import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.models.ServiceSelectionChoiceModel;

import java.util.ArrayList;

/**
 * Created by KAMAL OLI on 03/09/2017.
 */

public class ServiceSelectionChoiceAdapter extends RecyclerView.Adapter<ServiceSelectionChoiceAdapter.ViewHolderForOurService>{
    Context context;
    LayoutInflater inflater;
    ArrayList<ServiceSelectionChoiceModel> choiceArrayList;
    public ServiceSelectionChoiceAdapter(Context c,ArrayList<ServiceSelectionChoiceModel> choiceArrayList){
        context=c;
        this.choiceArrayList=choiceArrayList;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public ViewHolderForOurService onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.presentation_single_view,parent,false);
        ViewHolderForOurService holder=new ViewHolderForOurService(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolderForOurService holder, int position) {
        holder.initializeView(position);
    }

    @Override
    public int getItemCount() {
        return choiceArrayList.size();
    }

    class ViewHolderForOurService extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout layout;
        TextView label;
        public ViewHolderForOurService(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            layout=itemView.findViewById(R.id.linear_layout);
            label=itemView.findViewById(R.id.display_lebel);
        }
        public void initializeView(int position) {
            layout.setBackgroundResource(choiceArrayList.get(position).getImageId());
            label.setText(choiceArrayList.get(position).getLabel());
        }

        @Override
        public void onClick(View view) {
            SharedPreferences sharedPreferences=context.getApplicationContext().getSharedPreferences("Yomorning",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("AppOpenedFirstTime",true);
            editor.apply();
            editor.commit();
            editor.clear();
            Intent intent=new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
    }
}
