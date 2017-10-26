package com.yomorning.lavafood.yomorning.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.VolleySingletonPattern;
import com.yomorning.lavafood.yomorning.adapters.RailRestroVendorDisplayAdapter;
import com.yomorning.lavafood.yomorning.credentials.CredentialProviderClass;
import com.yomorning.lavafood.yomorning.models.RailRestroVendorsModel;
import com.yomorning.lavafood.yomorning.rectifier.BasicFunctionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RailRestroFoodOrder.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RailRestroFoodOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RailRestroFoodOrder extends Fragment implements View.OnClickListener {
    Button submitStationCode;
    RelativeLayout stationInfo;
    RecyclerView vendorListDisplayer;
    Context context;
    BasicFunctionHandler basicFunctionHandler;
    ArrayList<RailRestroVendorsModel> vendorsList;
    String stationCode;
    private View indivisualView;
    private OnFragmentInteractionListener mListener;
    public RailRestroFoodOrder() {

    }

    public static RailRestroFoodOrder newInstance(ArrayList<RailRestroVendorsModel> vendorsList) {
        RailRestroFoodOrder fragment = new RailRestroFoodOrder();
        Bundle args = new Bundle();
        args.putSerializable("railRestroVendorsList",vendorsList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.vendorsList=(ArrayList<RailRestroVendorsModel>)getArguments().getSerializable("railRestroVendorsList");
            Log.e("FoodOrderFragment",vendorsList.size()+"");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        indivisualView= inflater.inflate(R.layout.fragment_rail_restro_food_order, container, false);
        basicFunctionHandler=new BasicFunctionHandler(getActivity());
        vendorListDisplayer=indivisualView.findViewById(R.id.rail_restro_recycler_view);
        setAdapterToFragment(vendorsList);
        return indivisualView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    @Override
    public void onClick(View view) {
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setAdapterToFragment(ArrayList<RailRestroVendorsModel> modelArrayList){
        Log.e("setAdapter",modelArrayList.size()+"");
        RailRestroVendorDisplayAdapter adapter=new RailRestroVendorDisplayAdapter(getActivity(),modelArrayList);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        vendorListDisplayer.setLayoutManager(manager);
        vendorListDisplayer.setAdapter(adapter);
    }



}
