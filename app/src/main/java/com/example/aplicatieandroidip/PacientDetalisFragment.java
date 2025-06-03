package com.example.aplicatieandroidip;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class PacientDetalisFragment extends Fragment {

    private String fullName, cnp, email, county, city, address, phone, jobName, jobPlace, gender, bloodGroup, rh, bedId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cnp = getArguments().getString("cnp");
        Log.d("PacientDetails", "Fetching details for CNP: " + cnp);

        fetchPacientDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pacient_detalis, container, false);
    }

    private void fetchPacientDetails() {
        new Thread(() -> {
            try {
                SharedPreferences prefs = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                String token = prefs.getString("access_token", null);

                URL url = new URL("http://132.220.27.51/angajati/medic");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setRequestProperty("Accept", "application/json");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) response.append(line);

                JSONArray pacients = new JSONArray(response.toString());

                for (int i = 0; i < pacients.length(); i++) {
                    JSONObject obj = pacients.getJSONObject(i);
                    if(cnp.equals(obj.optString("cnp", ""))) {
                        fullName = obj.optString("nume", "") + " " + obj.optString("prenume", "");
                        address = obj.optString("strada", "") + " nr. "
                                + obj.optInt("nr_strada", 0) + " sc. "
                                + obj.optString("scara", "") + " ap. "
                                + obj.optInt("apartament", -1);
                        phone = obj.optString("telefon", "");
                        email = obj.optString("email", "");
                        county = obj.optString("judet", "");
                        city = obj.optString("localitate", "");
                        jobName = obj.optString("profesie", "");
                        jobPlace = obj.optString("loc_de_munca", "");
                        gender = obj.optString("sex", "");
                        bloodGroup = obj.optString("grupa_sange", "");
                        rh = obj.optString("rh", "");
                        bedId = obj.optString("id_pat", "");
                    }
                }
                requireActivity().runOnUiThread(() -> {
                    ((TextView) requireView().findViewById(R.id.pacient_name_details)).setText(fullName);
                    ((TextView) requireView().findViewById(R.id.pacient_cnp_details)).setText(cnp);
                    ((TextView) requireView().findViewById(R.id.pacient_mail_details)).setText(email);
                    ((TextView) requireView().findViewById(R.id.pacient_phone_details)).setText(phone);
                    ((TextView) requireView().findViewById(R.id.pacient_county_details)).setText(county);
                    ((TextView) requireView().findViewById(R.id.pacient_city_details)).setText(city);
                    ((TextView) requireView().findViewById(R.id.pacient_address_details)).setText(address);
                    ((TextView) requireView().findViewById(R.id.pacient_jobName_details)).setText(jobName);
                    ((TextView) requireView().findViewById(R.id.pacient_jobPlace_details)).setText(jobPlace);
                    ((TextView) requireView().findViewById(R.id.pacient_gender_details)).setText(gender);
                    ((TextView) requireView().findViewById(R.id.pacient_blood_details)).setText(bloodGroup);
                    ((TextView) requireView().findViewById(R.id.pacient_rh_details)).setText(rh);
                    ((TextView) requireView().findViewById(R.id.pacient_bed_details)).setText(bedId);
                });

            } catch (Exception e) {
                Log.e("PacientDetails", "Error fetching pacient", e);
            }
        }).start();
    }

}