package com.example.aplicatieandroidip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class AutomaticOrderFragment extends Fragment {

    private TextView tvPacientName, tvPacientCnp, tvPacientPhoneNo;
    private LinearLayout medicineListContainer;
    private Button confirmButton, cancelButton;
    private List<String> medicineList = new ArrayList<>();
    private String name, cnp, phoneNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        name = getArguments() != null ? getArguments().getString("name") : "";
        cnp = getArguments() != null ? getArguments().getString("cnp") : "";
        phoneNo = getArguments() != null ? getArguments().getString("phoneNo"): "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_automatic_order, container, false);

        tvPacientName = view.findViewById(R.id.pacient_name_order);
        tvPacientName.setText(name);
        tvPacientCnp= view.findViewById(R.id.pacient_cnp_order);
        tvPacientCnp.setText(cnp);
        tvPacientPhoneNo = view.findViewById(R.id.pacient_phone_order);
        tvPacientPhoneNo.setText(phoneNo);

        medicineListContainer = view.findViewById(R.id.PrescriptionLayout);
        confirmButton = view.findViewById(R.id.confirm_order_button);
        cancelButton = view.findViewById(R.id.cancel_order_button);

        confirmButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Order confirmed!", Toast.LENGTH_SHORT).show();
        });

        cancelButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_AutomaticOrderFragment_to_HomeFragment);
        });

        return view;
    }
}