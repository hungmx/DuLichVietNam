package com.dhthuyloi.mxhung.dulichvietnam.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhthuyloi.mxhung.dulichvietnam.R;
import com.dhthuyloi.mxhung.dulichvietnam.adapter.ItemPagerAdapter;
import com.dhthuyloi.mxhung.dulichvietnam.database.DBManager;
import com.dhthuyloi.mxhung.dulichvietnam.google.MapActivity;
import com.dhthuyloi.mxhung.dulichvietnam.model.DiaDanh;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MXHung on 9/4/2016.
 */
public class DiaDanhActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    protected View view;
    @Bind(R.id.tvGioiThieu) TextView tvGioiThieu;
    @Bind(R.id.tvDiemDi) TextView tvDiemDi;
    @Bind(R.id.tvAnUong) TextView tvAnUong;
    @Bind(R.id.tvNhaNghi) TextView tvNhaNghi;
    @Bind(R.id.tvPhuongTien) TextView tvPhuongTien;
    @Bind(R.id.tvLichTrinh) TextView tvLichTrinh;
    @Bind(R.id.chkFavorite) CheckBox chkFavorite;
    @Bind(R.id.tvMap) TextView tvMap;
    @Bind(R.id.tvWeather) TextView tvWeather;
    @Bind(R.id.pager_introduction) ViewPager intro_image;
    @Bind(R.id.viewCountDots) LinearLayout viewCountDots;
    private int dotsCount;
    private ImageView[] dots;
    private ItemPagerAdapter adapter;
    private String[] imageResource;
    int count = 0;
    DBManager db;
    String image;
    Bitmap bitmap, bitmap1, bitmap2, bitmap3, bitmap4, bitmap5;
    ArrayList<Bitmap> listBmp;
    static String[] words;
    int id_diadanh;
    static int favorite;
    DiaDanh diaDanh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diadanh);
        ButterKnife.bind(this);


        id_diadanh = getIntent().getIntExtra("id_diadanh", 1);
        db = new DBManager(this);
        diaDanh = db.getDiaDanhDetail(id_diadanh);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(diaDanh.getNameDiaDanh());

//		downImage();
        Log.d("TAGLIST", String.valueOf(listBmp));
//		if (listBmp.size() == 0) {
//			Toast.makeText(getApplicationContext(), "rooxng", Toast.LENGTH_SHORT).show();
//		}else {
        String image = diaDanh.getImDiaDanh();
        String[] array_image = image.split(";");
//        imageResource = new int[]{R.drawable.im_thitbo, R.drawable.im_thitbo, R.drawable.im_thitbo, R.drawable.im_thitbo, R.drawable.im_thitbo};
            imageResource = new String[]{array_image[0],array_image[1],array_image[2],array_image[3],array_image[4]};
//		}
//		if (listBmp.size() == words.length) {
        Log.d("TAGimage", String.valueOf(imageResource));
        adapter = new ItemPagerAdapter(DiaDanhActivity.this, imageResource);
        intro_image.setAdapter(adapter);
        intro_image.setCurrentItem(0);
        intro_image.setOnPageChangeListener(this);
        setUiPage();

        setOnClick();
        //time for auto
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count <= 5) {
                            intro_image.setCurrentItem(count);
                            count++;
                        } else {
                            count = 0;
                            intro_image.setCurrentItem(count);
                        }
                    }
                });
            }
        }, 500, 3000);

        Log.d("--favorite", String.valueOf(diaDanh.getFavotite()));
        if (diaDanh.getFavotite() == 1) {
            chkFavorite.setChecked(true);
        }

        chkFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkFavorite.isChecked()) {
//                    favorite = 1;
                    DiaDanh diaDanh = new DiaDanh();
                    diaDanh.setIdDiaDanh(id_diadanh);
                    diaDanh.setFavotite(1);
                    db.editFavorite(diaDanh);
                    Log.d("--isChecked", String.valueOf(1));
                } else {
//                    favorite = 0;
                    DiaDanh diaDanh = new DiaDanh();
                    diaDanh.setIdDiaDanh(id_diadanh);
                    diaDanh.setFavotite(0);
                    db.editFavorite(diaDanh);
                    Log.d("--UNChecked", String.valueOf(0));
                }
            }
        });



    }

//	}

//    public void getData() {
//        id_diadanh = getIntent().getIntExtra("id_diadanh", 1);
//        favorite = getIntent().getIntExtra("favorite", 0);
//        regions = getIntent().getIntExtra("regions", 0);
//        image = getIntent().getStringExtra("image");
//        String latlng = getIntent().getStringExtra("latlng");
//        String name = getIntent().getStringExtra("name");
//        Log.d("---", "id " + String.valueOf(id_diadanh) + " latlng " + latlng + " name " + name);
//    }

    public void downImage() {
        listBmp = new ArrayList<>();
////		String[] imPosition = image.split(";");
////		for (int i = 0; i < imPosition.length; i++){
////		new LoadImage().execute(imPosition[i]);
////		Log.d("--", String.valueOf(listBmp));
////		}
//
//		words = image.split(";");
//		for (int w = 0; w < words.length; w++) {
//			Log.d("image", words[w]);
//			new LoadImage().execute(words[w]);
//		}

        String[] words = diaDanh.getImDiaDanh().split(";");//chia chuoi dua tren string phan cac nhau boi khoang trang (\\s)

        //su dung vong lap foreach de in cac phan tu trong mang chuoi
        for (int w = 0; w < words.length; w++) {
            Log.d("words", words[w]);
            Log.d("bip", String.valueOf(decodeBase64(words[w])));
            listBmp.add(decodeBase64(words[w]));
        }
        Log.d("TAGLIST", String.valueOf(listBmp));

    }

    public void setOnClick() {
        tvGioiThieu.setOnClickListener(this);
        tvDiemDi.setOnClickListener(this);
        tvAnUong.setOnClickListener(this);
        tvPhuongTien.setOnClickListener(this);
        tvNhaNghi.setOnClickListener(this);
        tvLichTrinh.setOnClickListener(this);
        tvMap.setOnClickListener(this);
        tvWeather.setOnClickListener(this);
    }

    private void setUiPage() {
        dotsCount = adapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            viewCountDots.addView(dots[i], params);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        id_diadanh = getIntent().getIntExtra("id_diadanh", 0);
        String latlng = getIntent().getStringExtra("latlng");
        String name = getIntent().getStringExtra("name");
        String city = getIntent().getStringExtra("city");
        switch (v.getId()) {
            case R.id.tvGioiThieu:
                Intent iIntro = new Intent(DiaDanhActivity.this, IntroduceActivity.class);
                iIntro.putExtra("id_diadanh", id_diadanh);
                startActivity(iIntro);
                break;
            case R.id.tvDiemDi:
                Intent iPlace = new Intent(DiaDanhActivity.this, PlaceActivity.class);
                iPlace.putExtra("id_diadanh", id_diadanh);
                startActivity(iPlace);
                break;
            case R.id.tvAnUong:
                Intent iRestaurant = new Intent(DiaDanhActivity.this, RestaurantActivity.class);
                iRestaurant.putExtra("id_diadanh", id_diadanh);
                startActivity(iRestaurant);
                break;
            case R.id.tvPhuongTien:
                Intent iVehicles = new Intent(DiaDanhActivity.this, VehiclesActivity.class);
                iVehicles.putExtra("id_diadanh", id_diadanh);
                startActivity(iVehicles);
                break;
            case R.id.tvNhaNghi:
                Intent iHotel = new Intent(DiaDanhActivity.this, HotelActivity.class);
                iHotel.putExtra("id_diadanh", id_diadanh);
                startActivity(iHotel);
                break;
            case R.id.tvLichTrinh:
                Intent iTour = new Intent(DiaDanhActivity.this, TourActivity.class);
                iTour.putExtra("id_diadanh", id_diadanh);
                iTour.putExtra("image", diaDanh.getImage_int());
                iTour.putExtra("name", diaDanh.getNameDiaDanh());
                startActivity(iTour);
                break;
            case R.id.tvMap:
//				id = getIntent().getIntExtra("id", 0);
                Log.d("+++", "id " + String.valueOf(id_diadanh) + " latlng " + latlng + " name " + name);
                Intent iMap = new Intent(DiaDanhActivity.this, MapActivity.class);
                iMap.putExtra("name", diaDanh.getNameDiaDanh());
                iMap.putExtra("latlng", diaDanh.getLatlng());
                iMap.putExtra("zoom", 10);
                startActivity(iMap);
                break;
            case R.id.tvWeather:
                Intent iWeather = new Intent(DiaDanhActivity.this, WeatherActivity.class);
                iWeather.putExtra("city", diaDanh.getCity());
                startActivity(iWeather);
                break;

        }
    }

    private class LoadImage extends AsyncTask<String, Bitmap, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
                publishProgress(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Bitmap... values) {
            super.onProgressUpdate(values);
            listBmp.add(values[0]);
//			Log.d("bytes2", String.valueOf(bitmap));
            Log.d("list", String.valueOf(listBmp));
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//			Log.d("TAG-list", String.valueOf(listBmp));
            if (listBmp.size() == words.length) {
                Log.d("TAG-listXONG", String.valueOf(listBmp));
//				imageResource = new Bitmap[]{listBmp.get(0), listBmp.get(1), listBmp.get(2), listBmp.get(3), listBmp.get(4)};
//				imageResource = listBmp;

            }

        }
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
