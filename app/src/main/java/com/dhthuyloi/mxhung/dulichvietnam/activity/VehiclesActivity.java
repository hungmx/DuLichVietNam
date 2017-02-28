package com.dhthuyloi.mxhung.dulichvietnam.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dhthuyloi.mxhung.dulichvietnam.R;
import com.dhthuyloi.mxhung.dulichvietnam.adapter.VehiclesAdapter;
import com.dhthuyloi.mxhung.dulichvietnam.database.DBManager;
import com.dhthuyloi.mxhung.dulichvietnam.model.Vehicle;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MXHung on 9/13/2016.
 */
public class VehiclesActivity extends AppCompatActivity {
    @Bind(R.id.lvVehicles)
    ListView lvVehicles;
    VehiclesAdapter adapter;
    DBManager db;
    ArrayList<Vehicle> listVehicle;
    String imString = "";
    Bitmap bitmap;
    static int i = 1;
    int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id = getIntent().getIntExtra("id_diadanh", 0);
        db = new DBManager(this);
        listVehicle = db.getVedicleID(id);
        if (listVehicle.size() == 0) {
            Toast.makeText(getApplicationContext(), "Chưa có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            setAdapterListView(listVehicle);
        }

        lvVehicles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                Intent iDetail = new Intent(VehiclesActivity.this, DetailVehicleActivity.class);
                iDetail.putExtra("id", listVehicle.get(position).getId());
//				iDetail.putExtra("image", listVehicle.get(position).getImage());
//				iDetail.putExtra("detail", listVehicle.get(position).getDetail());
//				iDetail.putExtra("name", listVehicle.get(position).getName());
                startActivity(iDetail);
            }
        });


    }

    public void setAdapterListView(ArrayList<Vehicle> list) {
        adapter = new VehiclesAdapter(getApplicationContext(), list);
        lvVehicles.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

}
