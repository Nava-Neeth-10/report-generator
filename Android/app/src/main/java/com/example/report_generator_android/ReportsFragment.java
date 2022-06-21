package com.example.report_generator_android;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ReportsFragment extends Fragment {

    public ReportsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        Spinner years=view.findViewById(R.id.year_select);
        Spinner products=view.findViewById(R.id.product_select);
        Button reset=view.findViewById(R.id.Reset);
        Spinner quarter=view.findViewById(R.id.quarter_select);
        Spinner file_type=view.findViewById(R.id.file_type);
        Button download =view.findViewById(R.id.download);
        ArrayList<String> all_products=new ArrayList<>();
        all_products.add("Select Product");
        ArrayAdapter<String> product_select_adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,all_products);
        product_select_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        products.setAdapter(product_select_adapter);
        ArrayList<String> all_years=new ArrayList<>();
        all_years.add("Select Year");
        ArrayAdapter<String> year_select_adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,all_years);
        year_select_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        years.setAdapter(year_select_adapter);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products.setSelection(0);
                years.setSelection(0);
                quarter.setSelection(0);
                file_type.setSelection(0);
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/products", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] data=response.split(",");
                for(int x=0;x<data.length;x++)
                    all_products.add(data[x]);
                product_select_adapter.notifyDataSetChanged();
            }
        }, error ->{Toast.makeText(getActivity(), "Server Connection Failed!!!", Toast.LENGTH_LONG).show();});
        queue.add(stringRequest);
        products.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!products.getSelectedItem().toString().equals("Select Product")) {
                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/years?product=" + products.getSelectedItem().toString(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            all_years.clear();
                            all_years.add("Select Year");
                            String[] data = response.split(",");
                            for (int x = 0; x < data.length; x++)
                                all_years.add(data[x]);
                            year_select_adapter.notifyDataSetChanged();
                        }
                    }, error -> {Toast.makeText(getActivity(), "Server Connection Failed!!!", Toast.LENGTH_LONG).show();});
                    queue.add(stringRequest2);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        Button generate=view.findViewById(R.id.generate_report);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(file_type.getSelectedItem().toString().equals("Select File Type") || products.getSelectedItem().toString().equals("Select Product") ||quarter.getSelectedItem().toString().equals("Select Quarter")||years.getSelectedItem().toString().equals("Select Year"))
                    Toast.makeText(getActivity(), "Please select all the fields", Toast.LENGTH_LONG).show();
                else{
                    StringRequest stringRequest=new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/generatereport?product=" + products.getSelectedItem().toString() + "&year=" + years.getSelectedItem().toString() + "&quarter=" + quarter.getSelectedItem().toString(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            download.setVisibility(View.VISIBLE);
                            download.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                                        File file = new File(folder, "data.csv");
                                        FileOutputStream f = new FileOutputStream(file);
                                        f.write(response.getBytes());
                                        f.close();
                                        Toast.makeText(getActivity(), "Download Completed", Toast.LENGTH_LONG).show();
                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    },error->{Toast.makeText(getActivity(), "Error occured", Toast.LENGTH_LONG).show();});
                    queue.add(stringRequest);
                }
            }
        });
        return view;
    }
}