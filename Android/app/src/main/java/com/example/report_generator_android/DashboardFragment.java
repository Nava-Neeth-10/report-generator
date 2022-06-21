package com.example.report_generator_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    public DashboardFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dashboard, container, false);
        Spinner product_select=view.findViewById(R.id.product_select);
        ArrayList<String> year=new ArrayList<>();
        ArrayList<String> q1=new ArrayList<>();
        ArrayList<String> q2=new ArrayList<>();
        ArrayList<String> q3=new ArrayList<>();
        ArrayList<String> q4=new ArrayList<>();
        ArrayList<String> products=new ArrayList<>();
        products.add("All Products");
        ArrayAdapter<String> product_select_adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,products);
        product_select_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        product_select.setAdapter(product_select_adapter);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/products", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] data=response.split(",");
                for(int x=0;x<data.length;x++)
                    products.add(data[x]);
                product_select_adapter.notifyDataSetChanged();
            }
        }, error ->{Toast.makeText(getActivity(), "Server Connection Failed!!!", Toast.LENGTH_LONG).show();});
        queue.add(stringRequest);
        RecyclerView product_revenue=view.findViewById(R.id.product_revenue);
        product_revenue.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.Adapter product_revenue_adapter=new Product_RevenueAdapter(year,q1,q2,q3,q4);
        product_revenue.setAdapter(product_revenue_adapter);
        product_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/dashboard?product="+product_select.getSelectedItem().toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        year.clear();
                        q1.clear();
                        q2.clear();
                        q3.clear();
                        q4.clear();
                        String[] data=response.split("\n");
                        for(int x=0;x<data.length;x++)
                        {
                            String[] data2=data[x].split(",");
                            year.add("Revenue for "+product_select.getSelectedItem().toString()+" in "+data2[0]);
                            q1.add(data2[1]);
                            q2.add(data2[2]);
                            q3.add(data2[3]);
                            q4.add(data2[4]);
                        }
                        product_revenue_adapter.notifyDataSetChanged();
                    }
                }, error ->{Toast.makeText(getActivity(), "Server Connection Failed!!!", Toast.LENGTH_LONG).show();});
                queue.add(stringRequest);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        return view;
    }
}