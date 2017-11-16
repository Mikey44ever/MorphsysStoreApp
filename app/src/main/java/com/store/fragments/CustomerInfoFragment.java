package com.store.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.store.R;
import com.store.activities.CreateOrderActivity;
import com.store.pojos.CustomerPOJO;

import java.util.ArrayList;

import pos.store.morphsys.com.morphsysstoreapp.pojo.product.ProductPOJO;

import static com.store.constants.Constants.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomerInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TextView txtDate,txtCustomer,txtAddress,txtBrgy,txtCityTown,txtMobileNo,txtLandmarks,txtDeliveryDate,txtNote;
    private Spinner pOptionSpinner,paymentBranch,shipViaSpinner;
    private CustomerPOJO customerPOJO;
    private ArrayList<ProductPOJO> productPOJOs;
    private Bundle bundle;

    public CustomerInfoFragment() {
        // Required empty public constructor
    }

    public static CustomerInfoFragment newInstance(String param1, String param2) {
        CustomerInfoFragment fragment = new CustomerInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_info, container, false);

        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtCustomer = (TextView) view.findViewById(R.id.txtCustomer);
        txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        txtBrgy = (TextView) view.findViewById(R.id.txtBrgy);
        txtCityTown = (TextView) view.findViewById(R.id.txtCityTown);
        txtMobileNo = (TextView) view.findViewById(R.id.txtMobileNo);
        txtLandmarks = (TextView) view.findViewById(R.id.txtLandmarks);
        txtDeliveryDate = (TextView) view.findViewById(R.id.txtDeliveryDate);
        txtNote = (TextView) view.findViewById(R.id.txtNote);

        customerPOJO=(CustomerPOJO) bundle.getSerializable(CUSTOMER_POJO_SERIAL_KEY);
        productPOJOs=(ArrayList<ProductPOJO>)bundle.getSerializable(PRODUCT_POJO_SERIAL_KEY);
        txtCustomer.setText(customerPOJO.getFirstName()+" "+customerPOJO.getLastName());
        txtAddress.setText(customerPOJO.getAddress());
        txtBrgy.setText(customerPOJO.getBrgy());
        txtCityTown.setText(customerPOJO.getCityTown().trim());
        txtMobileNo.setText(customerPOJO.getMobileNo());
        txtLandmarks.setText(customerPOJO.getLandmark());
        txtNote.setText(customerPOJO.getNote());

        setArguments(bundle);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
