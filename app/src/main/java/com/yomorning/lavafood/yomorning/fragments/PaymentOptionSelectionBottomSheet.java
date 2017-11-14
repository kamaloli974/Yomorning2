package com.yomorning.lavafood.yomorning.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yomorning.lavafood.yomorning.R;
import com.yomorning.lavafood.yomorning.rectifier.BasicFunctionHandler;

/**
 * Created by KAMAL OLI on 14/11/2017.
 */

public class PaymentOptionSelectionBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener{
    LayoutInflater inflater;
    Double totalPrice;
    Dialog dialog;

    Button payAmount;
    RadioButton cashOnDelivery;

    TextView cancel;
    BasicFunctionHandler handler;

    PaymentBottomSheetInterface paymentBottomSheetListener;
    Context context;
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        inflater= (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView=View.inflate(getActivity(),R.layout.payment_selection_bottom_sheet,null);
        dialog.setContentView(rootView);

        handler=new BasicFunctionHandler(getActivity());

        payAmount=rootView.findViewById(R.id.pay_amount);
        payAmount.setText("Pay "+context.getString(R.string.rupee_symbol)+" "+totalPrice);
        cashOnDelivery=rootView.findViewById(R.id.cash_on_delivery);
        cancel=rootView.findViewById(R.id.cancel_button);

        payAmount.setOnClickListener(this);
        cancel.setOnClickListener(this);
        this.dialog=dialog;
    }
    public PaymentOptionSelectionBottomSheet(){}
    public static PaymentOptionSelectionBottomSheet getInstance(Double totalPrice){
        PaymentOptionSelectionBottomSheet bottomSheet=new PaymentOptionSelectionBottomSheet();
        Bundle bundle=new Bundle();
        bundle.putDouble("totalPrice",totalPrice);
        bottomSheet.setArguments(bundle);
        return bottomSheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            this.totalPrice=getArguments().getDouble("totalPrice");
        }
        else {
            Log.e("Total price","Total Price is null");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_button:
                dialog.dismiss();
                break;
            case R.id.pay_amount:
                if (cashOnDelivery.isChecked()){
                    paymentBottomSheetListener.cashOnDeliveryPaymentMethod("COD");
                    dialog.dismiss();
                }
                else{
                    handler.showAlertDialog("Payment Selection","Please Provide provide proper payment method. Thank you");
                }
        }
    }

    public interface PaymentBottomSheetInterface{
        void cashOnDeliveryPaymentMethod(String modeOfPayment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        if(context instanceof PaymentBottomSheetInterface){
            paymentBottomSheetListener=(PaymentBottomSheetInterface)context;
        }
        else{
            throw new RuntimeException(context.toString()+" must implement OnCartItemChangedListener");
        }

    }
}
