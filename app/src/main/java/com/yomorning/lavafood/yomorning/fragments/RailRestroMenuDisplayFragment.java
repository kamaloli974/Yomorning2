package com.yomorning.lavafood.yomorning.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.adapters.RailRestroMenuAdapter;
import com.yomorning.lavafood.yomorning.credentials.CredentialProviderClass;
import com.yomorning.lavafood.yomorning.models.RailRestroMenuModel;
import com.yomorning.lavafood.yomorning.rectifier.BasicFunctionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RailRestroMenuDisplayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RailRestroMenuDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RailRestroMenuDisplayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private BasicFunctionHandler basicFunctionHandler;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RailRestroMenuDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RailRestroMenuDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RailRestroMenuDisplayFragment newInstance(String param1, String param2) {
        RailRestroMenuDisplayFragment fragment = new RailRestroMenuDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_rail_restro_menu_display, container, false);
        recyclerView=view.findViewById(R.id.menu_recycler_view);
        basicFunctionHandler=new BasicFunctionHandler(getActivity());
        return view;
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

    private void setAdapterToPopulateMenus(ArrayList<RailRestroMenuModel> menu){
        RailRestroMenuAdapter adapter=new RailRestroMenuAdapter(getActivity(),menu);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
    }
    private void menuJsonDataParser(int outletId){
        String menuUrl= CredentialProviderClass.RAIL_RESTRO_BASIC_API+"menu/"+outletId+CredentialProviderClass.BACK_SLASH+
                CredentialProviderClass.RAIL_RESTRO_USER+CredentialProviderClass.BACK_SLASH+
                CredentialProviderClass.RAIL_RESTRO_API_KEY;
        StringRequest request=new StringRequest(Request.Method.GET, menuUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<RailRestroMenuModel> menuList=getRailRestroMenuArrayList(response);
                setAdapterToPopulateMenus(menuList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                basicFunctionHandler.showAlertDialog("Error!","VolleyError Occure and message is "+error.getCause());
            }
        });
    }

    private ArrayList<RailRestroMenuModel> getRailRestroMenuArrayList(String response) {
        ArrayList<RailRestroMenuModel> menuArrayList=new ArrayList<>();
        try {
            JSONObject object=new JSONObject(response);
            if(object.getBoolean("Status")){
                JSONArray data=object.getJSONArray("Data");
                for(int i=0;i<data.length();i++){
                    int categoryId=data.getJSONObject(i).getInt("category_id");
                    String categoryName=data.getJSONObject(i).getString("category_name");
                    JSONArray menuArray=data.getJSONArray(i);
                    for (int j=0;j<menuArray.length();j++){
                        RailRestroMenuModel model=new RailRestroMenuModel();
                        JSONObject indivisualItem=menuArray.getJSONObject(j);
                        model.setCategoryId(categoryId);
                        model.setCategoryName(categoryName);
                        model.setBasePrice(Integer.parseInt(indivisualItem.getString("base_price")));
                        model.setItemId(Integer.parseInt(indivisualItem.getString("item_id")));
                        model.setItemName(indivisualItem.getString("item_name"));
                        model.setServiceTax(Integer.parseInt(indivisualItem.getString("service_tax")));
                        model.setVat(Integer.parseInt(indivisualItem.getString("vat")));
                        model.setItemType(Integer.parseInt(indivisualItem.getString("itemtype_id")));
                        model.setStatus(Integer.parseInt(indivisualItem.getString("status")));
                        model.setSellingPrice(indivisualItem.getInt("selling_price"));
                        model.setTimeSlot(indivisualItem.getJSONObject("menu_tags").getString("time_slot"));
                        model.setFoodItemDescription(indivisualItem.getString("description"));
                        model.setFoodCategory(indivisualItem.getJSONObject("menu_tags").getString("type"));
                        model.setActive(indivisualItem.getBoolean("is_active"));
                        model.setOpeningTime(indivisualItem.getString("opening_time"));
                        model.setClosingTime(indivisualItem.getString("closing_time"));
                        menuArrayList.add(model);
                    }
                }
                return menuArrayList;
            }
            else{
                basicFunctionHandler.showAlertDialog("Sorry!","No food items are available from this provider.Please go back and " +
                        "select another provider");
                return menuArrayList;
            }
        } catch (JSONException e) {
            basicFunctionHandler.showAlertDialog("JSONException","JSONException occured and message is "+e.getMessage());
            return menuArrayList;
        }
    }
}
