package com.yomorning.lavafood.yomorning.organization;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.adapters.HomeAdapter;
import com.yomorning.lavafood.yomorning.adapters.ServiceSelectionChoiceAdapter;
import com.yomorning.lavafood.yomorning.models.ServiceSelectionChoiceModel;

import java.util.ArrayList;

public class Home extends Fragment implements android.widget.SearchView.OnQueryTextListener {


    private OnCommunicationWithHomeFragment mListener;

    private RecyclerView recyclerView;
    private String [] ourService={"Order Food From Home","Order Delicious Food While You Are Travelling","Order Ready To Cook Food and " +
            "Make Delicious Food At Home"};
    private int[] imageId={R.drawable.home_delivery_food,R.drawable.travelling_food_background,R.drawable.ready_cook};

    private ArrayList<ServiceSelectionChoiceModel> listOfServices;

    private HomeAdapter adapter;

    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance() {
        Home fragment = new Home();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        listOfServices=new ArrayList<>();
        recyclerView=view.findViewById(R.id.home_recycler_view);
        setAdapterForPresentation();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCommunicationWithHomeFragment(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCommunicationWithHomeFragment) {
            mListener = (OnCommunicationWithHomeFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void setAdapterForPresentation() {
        adapter=new HomeAdapter(getActivity(),
                initializeDataToArrayList());
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public interface OnCommunicationWithHomeFragment {
        // TODO: Update argument type and name
        void onCommunicationWithHomeFragment(Uri uri);
    }

    public ArrayList<ServiceSelectionChoiceModel> initializeDataToArrayList(){
        for(int i=0;i<ourService.length;i++){
            final ServiceSelectionChoiceModel model=new ServiceSelectionChoiceModel();
            model.setLabel(ourService[i]);
            model.setChoiceId(i);
            model.setImageId(R.drawable.ready_cook);
            model.setImageId(imageId[i]);
            listOfServices.add(model);
        }
        return listOfServices;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.search_view);
        android.widget.SearchView searchView=(android.widget.SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        Log.e("HomeOptionMenu","HomeOptionMenuCalled");

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<ServiceSelectionChoiceModel> model=new ArrayList<>();
        if(newText!=null&&newText.length()>0){
            newText=newText.toLowerCase();
            for(int i=0;i<listOfServices.size();i++){
                if (listOfServices.get(i).getLabel().toLowerCase().contains(newText)){
                    model.add(listOfServices.get(i));
                }
            }
            Log.e("OntextHOme",newText);
            adapter.setUserFilter(model);
        }
        else{
            adapter.setUserFilter(listOfServices);
        }
        return true;
    }
}
