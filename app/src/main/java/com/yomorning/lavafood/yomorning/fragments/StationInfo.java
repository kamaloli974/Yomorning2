package com.yomorning.lavafood.yomorning.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.VolleySingletonPattern;
import com.yomorning.lavafood.yomorning.credentials.CredentialProviderClass;
import com.yomorning.lavafood.yomorning.models.RailRestroVendorsModel;
import com.yomorning.lavafood.yomorning.rectifier.BasicFunctionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StationInfo extends Fragment implements View.OnClickListener {
    EditText stationCode;
    View indivisualView;
    Button submitStationCode;
    BasicFunctionHandler basicFunctionHandler;

    private receiveListOfVendors mListener;

    ProgressDialog dialog;

    public StationInfo() {
        // Required empty public constructor
    }

    public static StationInfo newInstance() {
        StationInfo fragment = new StationInfo();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        indivisualView=inflater.inflate(R.layout.fragment_station_info, container, false);
        stationCode=indivisualView.findViewById(R.id.station_code);
        basicFunctionHandler=new BasicFunctionHandler(getActivity());
        submitStationCode=indivisualView.findViewById(R.id.submit_station_code);
        mListener=(receiveListOfVendors)getActivity();
        submitStationCode.setOnClickListener(this);
        dialog=new ProgressDialog(getActivity());
        return indivisualView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof receiveListOfVendors) {
            mListener = (receiveListOfVendors) context;
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
        switch (view.getId()){
            case R.id.submit_station_code:
                if(stationCode.getText().toString().trim().isEmpty()){
                    basicFunctionHandler.showAlertDialog("Opps!","Station Code value can't be empty. Please try again.");
                }
                else{
                    if (basicFunctionHandler.isConnectedToNetwork()){
                        JSONParserForVendors(stationCode.getText().toString().trim());
                        Log.e("StationCode",stationCode.getText().toString());
                    }
                    else{
                        basicFunctionHandler.showAlertDialog("Network Error","You are not connected to Internet. Please " +
                                "check your network and try again. Thank you.");
                    }

                }
                break;
        }
    }

    private void JSONParserForVendors(String stationCode){
        String backSlash="/";
        dialog.setMessage("Processing your request...");
        dialog.show();
        String vendorsUrl="https://www.railrestro.com/api/stores/"+stationCode+backSlash+ CredentialProviderClass.RAIL_RESTRO_USER+
                backSlash+CredentialProviderClass.RAIL_RESTRO_API_KEY;
        StringRequest request=new StringRequest(Request.Method.GET, vendorsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.hide();
                Log.e("Response",response);
                ArrayList<RailRestroVendorsModel> list=parseJson(response);
                if(list!=null){
                    mListener.onFragmentInteraction(list);
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
                    if(jsonObject.getBoolean("is_active")){
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


    public interface receiveListOfVendors {
        // TODO: Update argument type and name
        void onFragmentInteraction(ArrayList<RailRestroVendorsModel> list);
    }
}
