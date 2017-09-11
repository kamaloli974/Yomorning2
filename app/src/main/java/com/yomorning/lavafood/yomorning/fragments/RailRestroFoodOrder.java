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
    EditText stationCode;
    RelativeLayout stationInfo;
    RecyclerView vendorListDisplayer;
    ProgressDialog dialog;
    Context context;
    BasicFunctionHandler basicFunctionHandler;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View indivisualView;
    private OnFragmentInteractionListener mListener;
    public RailRestroFoodOrder() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RailRestroFoodOrder.
     */
    // TODO: Rename and change types and number of parameters
    public static RailRestroFoodOrder newInstance(String param1, String param2) {
        RailRestroFoodOrder fragment = new RailRestroFoodOrder();
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
        indivisualView= inflater.inflate(R.layout.fragment_rail_restro_food_order, container, false);
        basicFunctionHandler=new BasicFunctionHandler(getActivity());
        dialog=new ProgressDialog(getActivity());
        submitStationCode=indivisualView.findViewById(R.id.submit_station_code);
        stationCode=indivisualView.findViewById(R.id.station_code);
        stationInfo=indivisualView.findViewById(R.id.station_info);
        stationInfo.setVisibility(View.VISIBLE);
        vendorListDisplayer=indivisualView.findViewById(R.id.rail_restro_recycler_view);
        submitStationCode.setOnClickListener(this);
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
        if(view.getId()==R.id.submit_station_code){
            if(basicFunctionHandler.isConnectedToNetwork()){
                String stnCode=stationCode.getText().toString().toLowerCase();
                Log.e("Station Code",stnCode);
                stationInfo.setVisibility(View.GONE);
                vendorListDisplayer.setVisibility(View.VISIBLE);
                JSONParserForVendors(stnCode);
            }
            else{
                basicFunctionHandler.showAlertDialog("Network Error","You are not connected to Internet. Please " +
                        "check your network and try again. Thank you.");
            }
        }
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
        RailRestroVendorDisplayAdapter adapter=new RailRestroVendorDisplayAdapter(getActivity(),modelArrayList);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        vendorListDisplayer.setLayoutManager(manager);
        vendorListDisplayer.setAdapter(adapter);
    }
    private void JSONParserForVendors(String stationCode){
        String backSlash="/";
        dialog.setMessage("Processing your request...");
        dialog.show();
        String vendorsUrl="https://www.railrestro.com/api/stores/"+stationCode+backSlash+CredentialProviderClass.RAIL_RESTRO_USER+
                backSlash+CredentialProviderClass.RAIL_RESTRO_API_KEY;
        StringRequest request=new StringRequest(Request.Method.GET, vendorsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.hide();
                ArrayList<RailRestroVendorsModel> list=parseJson(response);
                if(list!=null){
                    setAdapterToFragment(list);
                }
                else{
                    Log.e("No data ","To display");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
              basicFunctionHandler.showAlertDialog("Error!","VolleyError Occure and message is "+error.getCause());
            }
        });
        VolleySingletonPattern.getInstance(getActivity()).addToRequestQueue(request);
    }
    ArrayList<RailRestroVendorsModel> parseJson(String response) {
        ArrayList<RailRestroVendorsModel> arrayList=new ArrayList<>();
        try {
            JSONObject object=new JSONObject(response);
            if(object.getBoolean("Status")){
                JSONArray jsonArray=object.getJSONArray("Data");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    RailRestroVendorsModel vendorsModel=new RailRestroVendorsModel();
                    vendorsModel.setVendorsName(jsonObject.getString("outletDname"));
                    vendorsModel.setVendorId(jsonObject.getString("outlet_id"));
                    vendorsModel.setOrderTiming(Integer.parseInt(jsonObject.getString("order_timing")));
                    vendorsModel.setOpeningTime(jsonObject.getString("opening_time"));
                    vendorsModel.setClosingTime(jsonObject.getString("closing_time"));
                    vendorsModel.setMinimumAmount(Integer.parseInt(jsonObject.getString("min_order_amount")));
                    vendorsModel.setDeliveryCost(Integer.parseInt(jsonObject.getString("delivery_cost")));
                    vendorsModel.setAddress(jsonObject.getString("address"));
                    vendorsModel.setCity(jsonObject.getString("city"));
                    vendorsModel.setState(jsonObject.getString("state"));
                    vendorsModel.setCompanyName(jsonObject.getString("company_name"));
                    vendorsModel.setMenuTypes(jsonObject.getJSONObject("tags").getString("menu_type"));
                    vendorsModel.setVendorId(jsonObject.getString("id"));
                    vendorsModel.setMobileNumber(jsonObject.getString("outlet_mobile"));
                    vendorsModel.setActive(jsonObject.getBoolean("is_active"));
                    vendorsModel.setVendorImageUrl(jsonObject.getString("vendor_logo_image"));
                    vendorsModel.setEmailAddress(jsonObject.getString("outlet_email"));
                    vendorsModel.setCancelCutOffTime(Integer.parseInt(jsonObject.getString("cancel_cut_off_time")));
                    vendorsModel.setMealTypes(jsonObject.getJSONObject("tags").getString("meal_type"));
                    arrayList.add(vendorsModel);
                }
                Log.e("Success",object.toString());
                return arrayList;
            }
            else{
                basicFunctionHandler.showAlertDialog("Sorry!","Our service is not available to the station you entered.");
                return arrayList;
            }
        } catch (JSONException e) {
            basicFunctionHandler.showAlertDialog("JSONException","JSONException occured and message is "+e.getMessage());
            return null;
        }

    }
}
