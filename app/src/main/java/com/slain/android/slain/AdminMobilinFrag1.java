package com.slain.android.slain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminMobilinFrag1 extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private List<Mobil> listMobil;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mobilin1, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewM1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listMobil = new ArrayList<>();

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        refreshLayout.setOnRefreshListener(this);

        loadRecyclerViewItem();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
            void refreshItems() {
                // Load complete
                refreshLayout.setRefreshing(true);
                Intent i = new Intent(getActivity(), AdminMobilin.class);
                getActivity().startActivity(i);
                getActivity().overridePendingTransition(0, 0);
                getActivity().finish();
                onItemsLoadComplete();
            }

            void onItemsLoadComplete() {
                // Stop refresh animation
                refreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    private void loadRecyclerViewItem() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GET_MOBIL_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                listMobil.add(new Mobil(
                                        product.getInt("id"),
                                        product.getString("nama_mobil"),
                                        product.getString("keterangan"),
                                        product.getString("start"),
                                        product.getString("end"),
                                        product.getInt("status"),
                                        product.getInt("id_user"),
                                        product.getString("nama"),
                                        product.getString("fungsi"),
                                        product.getString("email")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            AdminMobilinAdapter adapter = new AdminMobilinAdapter(getContext(), listMobil);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public void onRefresh() {
        refreshLayout.setRefreshing(true);
    }
}
