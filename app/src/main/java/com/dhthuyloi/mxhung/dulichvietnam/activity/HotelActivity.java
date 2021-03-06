package com.dhthuyloi.mxhung.dulichvietnam.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhthuyloi.mxhung.dulichvietnam.R;
import com.dhthuyloi.mxhung.dulichvietnam.adapter.HotelAdapter;
import com.dhthuyloi.mxhung.dulichvietnam.database.DBManager;
import com.dhthuyloi.mxhung.dulichvietnam.model.Hotel;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MXHung on 9/13/2016.
 */
public class HotelActivity extends AppCompatActivity {
    @Bind(R.id.lvPlace)
    ListView lvPlace;
    ArrayList<Hotel> list;
    HotelAdapter adapter;
    DBManager db;
    int id_diadanh;

    //search
    @Bind(R.id.etSearch) EditText etSearch;
    @Bind(R.id.tvEmpty) TextView tvEmpty;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textChange();
        id_diadanh = getIntent().getIntExtra("id_diadanh", 0);
        db = new DBManager(this);
        list = db.getHotelID(id_diadanh);
        if (list.size() == 0) {
            Toast.makeText(getApplicationContext(), "Chưa có dữ liệu", Toast.LENGTH_LONG).show();
        } else {
            setAdapterListView(list);
        }
        lvPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent iDetail = new Intent(HotelActivity.this, DetailHotelActivity.class);
                iDetail.putExtra("id", list.get(position).getId());
                startActivity(iDetail);
            }
        });
    }

//	public void loadListHotel() {
//		list = new ArrayList<>();
//		Hotel hotel;
//
//		String[] doAn = {"Khách sạn Hương Rừng", "Khách sạn Hạ Long ", "Khách sạn Mi Mi", "Khách sạn Thế Giới Xanh", "Khách sạn Sao Mai", "Khách sạn Suối Bạc", "Khách sạn Hanvet"};
//		for (int i = 0; i < doAn.length; i++) {
//			hotel = new Hotel(1, doAn[i], R.drawable.im_hotel, "21.451423,105.646162", "0921963862", "250.000 - 350.000", 1);
//			list.add(hotel);
//		}
//
//		if (list.size() == 0) {
//
//		} else {
//
//		}
//
//		setAdapterListView(list);
//		adapter.notifyDataSetChanged();
//	}

    public void setAdapterListView(ArrayList<Hotel> list) {
        adapter = new HotelAdapter(getApplicationContext(), list);
        lvPlace.setAdapter(adapter);
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

    //----------------------------------------------------------searchh
    //--
    @OnClick(R.id.ibtSpeech)
    public void speech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_somthing));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            ;
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if ((result.get(0).equals("xóa"))) {
                        etSearch.setText("");
                    } else {
                        etSearch.setText(result.get(0));
                    }
                }
                break;
            }
        }
    }

    @OnClick(R.id.btClean)
    public void cleanText() {
        etSearch.setText("");
    }

    public void textChange() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etSearch.setSelection(etSearch.getText().length());
                list = db.searchHotel(String.valueOf(s), id_diadanh);
                if (list.size() == 0) {
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                }
                setAdapterListView(list);
                //adapter.notifyDataSetChanged();
            }
        });
    }
}
