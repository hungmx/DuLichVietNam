package com.dhthuyloi.mxhung.dulichvietnam.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dhthuyloi.mxhung.dulichvietnam.R;
import com.dhthuyloi.mxhung.dulichvietnam.database.DBManager;
import com.dhthuyloi.mxhung.dulichvietnam.fragment.DiaDanhFragment;
import com.dhthuyloi.mxhung.dulichvietnam.fragment.ExpFragment;
import com.dhthuyloi.mxhung.dulichvietnam.fragment.FavoriteFragment;
import com.dhthuyloi.mxhung.dulichvietnam.fragment.SOSFragment;
import com.dhthuyloi.mxhung.dulichvietnam.fragment.ToolsFragment;
import com.dhthuyloi.mxhung.dulichvietnam.model.DiaDanh;
import com.dhthuyloi.mxhung.dulichvietnam.model.Hotel;
import com.dhthuyloi.mxhung.dulichvietnam.model.Place;
import com.dhthuyloi.mxhung.dulichvietnam.model.Restaurant;
import com.dhthuyloi.mxhung.dulichvietnam.model.Vehicle;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
	@Bind(R.id.toolbar)
	Toolbar toolbar;
	@Bind(R.id.nav_drawer)
	DrawerLayout drawerLayout;
	@Bind(R.id.nav_view)
	NavigationView navigationView;
	@Bind(R.id.nav_frame)
	FrameLayout contentFrame;
	DBManager db;

	//test
	String imString = "";
	Bitmap bitmap;
	static int i = 1;
	static int idVehicel = 1;
	static int idPlace = 1;
	static int idHotel = 1;
	static int idDiadanh = 1;
	static int idRestaurant = 1;

	private static final String PLACE = "p";
	private static final String RESTAURANT = "r";
	private static final String VEHICLE = "v";
	private static final String HOTEL = "h";
	String TAG;
	String chuoi = "";
	ArrayList<DiaDanh> listD;
	boolean doubleBack = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		setUpToolbar();
		setUpNavDrawer();
		startFragment(new DiaDanhFragment());

		//Database
		db = new DBManager(this);
		db.openDataBase();


//		bo thuoc tinh default black icon
		navigationView.setItemIconTintList(null);
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem item) {
				item.setCheckable(true);
				switch (item.getItemId()) {
					case R.id.nav_diadanh:
						startFragment(new DiaDanhFragment());
						drawerLayout.closeDrawers();
						return true;
					case R.id.nav_favorite:
						startFragment(new FavoriteFragment());
						drawerLayout.closeDrawers();
						return true;
					case R.id.nav_experience:
						startFragment(new ExpFragment());
						drawerLayout.closeDrawers();
						return true;
					case R.id.nav_sos:
						drawerLayout.closeDrawers();
						Bundle bundle = new Bundle();
						bundle.putInt("TAG", 1);
						SOSFragment fragment = new SOSFragment();
						fragment.setArguments(bundle);
						startFragment(fragment);
						return true;
					case R.id.nav_tools:
						drawerLayout.closeDrawers();
						startFragment(new ToolsFragment());
						return true;
					case R.id.nav_update:
						drawerLayout.closeDrawers();
						showAlertDialog();
						return true;
					case R.id.nav_information:
						drawerLayout.closeDrawers();
						showDialogInfo();
						return true;
					case R.id.nav_send_mail:
						Intent emailIntent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", "hungmx94@gmail.com", null));
						emailIntent.putExtra("android.intent.extra.SUBJECT", "Phản hồi");
						startActivity(Intent.createChooser(emailIntent, "Phản hồi"));
						break;

				}
				return false;
			}
		});
	}

	private void setUpToolbar() {
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}
	}

	private void setUpNavDrawer() {
		if (toolbar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			toolbar.setNavigationIcon(R.drawable.ic_drawer);
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					drawerLayout.openDrawer(GravityCompat.START);
				}
			});

		}
	}

	public void startFragment(Fragment fragment) {
		String fragmentName = fragment.getClass().getName();
		String fragmentTag = fragmentName;

		FragmentManager manager = getSupportFragmentManager();
		boolean fragmentPopp = manager.popBackStackImmediate(fragmentName, 0);

		if (!fragmentPopp && manager.findFragmentByTag(fragmentTag) == null) {
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.nav_frame, fragment, fragmentTag);
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.commit();
		}
	}

	public void showAlertDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
				.setIcon(R.drawable.ic_warning)
				.setTitle("Update")
				.setMessage("Bạn đang dùng phiên bản mới nhất")
				.show();
	}

	public void showDialogInfo() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Thông tin")
				.setMessage(Html.fromHtml("<p>&bull; <strong>Sổ tay du lịch</strong> (<span style=\"text-decoration: underline;\"><span style=\"color: #3366ff; text-decoration: underline;\">https://play.google.com/store/apps/details?id=com.sotaydulich</span></span>)</p>\n" +
						"<p>&bull; Version 1.0</p>\n" +
						"<p><span style=\"background-color: #cfcccc;\">Ứng dụng Sổ tay du lịch được ph&aacute;t triển bởi sinh vi&ecirc;n Mai Xu&acirc;n Hưng - Lớp 54TH2 &ndash; Trường Đại Học Thủy Lợi, với sự tận t&igrave;nh hướng dẫn của Thạc sĩ Trương Xu&acirc;n Nam. </span></p>\n" +
						"<p><span style=\"background-color: #cfcccc;\">Ứng dụng gi&uacute;p cho người d&ugrave;ng c&oacute; thể t&igrave;m hiểu địa điểm cần đi một c&aacute;ch đầy đủ v&agrave; kh&aacute;i qu&aacute;t nhất. Với c&aacute;ch b&agrave;y tr&iacute; khoa học, phối m&agrave;u ph&ugrave; hợp, nhiều chức năng cần thiết, dễ d&agrave;ng thao t&aacute;c. </span></p>\n" +
						"<p><span style=\"background-color: #cfcccc;\">Ứng dụng Sổ tay du lịch ch&iacute;nh l&agrave; một người bạn đồng h&agrave;nh của bạn.</span></p>"));
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})

				.show();
	}

	//ham download image
	private class LoadImageR extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
				imString = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
				publishProgress(imString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return imString;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			String image;
			Restaurant restaurant = new Restaurant();
			restaurant.setId(idRestaurant++);
			image = values[0];
			restaurant.setImage(image);
			db.editRestaurant(restaurant);

		}

		protected void onPostExecute(String imString) {
			Log.d("imString", imString);
		}

	}

	//ham download image
	private class LoadImageP extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
				imString = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
				publishProgress(imString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return imString;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			String image;
			Place place = new Place();
			place.setId(idPlace++);
			image = values[0];
			place.setImage(image);
			db.editPlace(place);

		}

		protected void onPostExecute(String imString) {
			Log.d("imString", imString);
		}

	}

	//ham download image
	private class LoadImageV extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
				imString = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
				publishProgress(imString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return imString;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			Log.d("TAG-image", values[0]);
			String image;
			Vehicle vehicle = new Vehicle();
			vehicle.setId(idVehicel++);
			image = values[0];
			vehicle.setImage(image);
			db.editVehicle(vehicle);

		}

		protected void onPostExecute(String imString) {
			Log.d("imString", imString);
		}

	}

	//ham download image
	private class LoadImageH extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
				imString = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
				publishProgress(imString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return imString;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			Log.d("TAG-image", values[0]);
			String image;
			Hotel hotel = new Hotel();
			hotel.setId(idHotel++);
			image = values[0];
			hotel.setImage(image);
			db.editHotel(hotel);

		}

		protected void onPostExecute(String imString) {
			Log.d("imString", imString);
		}

	}

	//ham download image
	private class LoadImageD extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
				imString = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
				publishProgress(imString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return imString;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			Log.d("TAG-image", values[0]);
			String image;
			DiaDanh diaDanh = new DiaDanh();
			diaDanh.setIdDiaDanh(idDiadanh++);
			image = values[0];
			diaDanh.setImage_int(image);
			db.editDiadanh(diaDanh);

		}

		protected void onPostExecute(String imString) {
			Log.d("imString", imString);
		}

	}

	public String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
		ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		image.compress(compressFormat, quality, byteArrayOS);
		return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
	}

	@Override
	public void onBackPressed() {
		if (doubleBack) {
			super.onBackPressed();
			finish();
			//thoat app ra man hinh chinh'.
			moveTaskToBack(true);
		}
		this.doubleBack = true;
		Toast.makeText(getApplicationContext(), "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT).show();
		//thời gian chờ là 2s
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				doubleBack = false;
			}
		}, 2000);

	}
}
