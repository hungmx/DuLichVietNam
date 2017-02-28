package com.dhthuyloi.mxhung.dulichvietnam.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dhthuyloi.mxhung.dulichvietnam.R;
import com.dhthuyloi.mxhung.dulichvietnam.database.DBManager;
import com.dhthuyloi.mxhung.dulichvietnam.model.Vehicle;

import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MXHung on 10/11/2016.
 */
public class DetailVehicleActivity extends AppCompatActivity {
	@Bind(R.id.imVehicel) ImageView imVehicel;
	@Bind(R.id.tvVehicles) TextView tvVehicles;
	private String name;
	private String image;
	private String detail;
	private  int id;
	private Vehicle vehicle;
	private DBManager db;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_vehicles);
		ButterKnife.bind(this);
		db = new DBManager(this);


		id = getIntent().getIntExtra("id", 1);
		vehicle = db.getVedicleDetail(id);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(vehicle.getName());

		tvVehicles.setText(Html.fromHtml(vehicle.getDetail()));
//		new DownloadImageTask().execute(vehicle.getImage());
//		PicassoClient.downloadImage(getApplicationContext(), anh[0], imVehicel);
		Glide.with(getApplicationContext())
				.load(vehicle.getImage())
				.error(R.drawable.im_thumbnail)
				.skipMemoryCache(true)
				.into(imVehicel);

	}

//	public void getData(){
//		Intent intent = getIntent();
//		Bundle bundle = intent.getExtras();
//
//		name = bundle.getString("name");
//		image = bundle.getString("image");
//		detail = bundle.getString("detail");
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home){
			finish();
		}
		return true;
	}
	//ham download image
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap bitmap = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				bitmap = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap result) {
			imVehicel.setImageBitmap(result);
		}
	}
}
