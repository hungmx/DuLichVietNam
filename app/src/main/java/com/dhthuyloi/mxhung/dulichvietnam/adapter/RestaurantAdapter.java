package com.dhthuyloi.mxhung.dulichvietnam.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dhthuyloi.mxhung.dulichvietnam.R;
import com.dhthuyloi.mxhung.dulichvietnam.model.Restaurant;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MXHung on 9/9/2016.
 */
public class RestaurantAdapter extends BaseAdapter{
	private ArrayList<Restaurant> list;
	Context context;
	Restaurant restaurant;

	public RestaurantAdapter(Context context, ArrayList<Restaurant> list){
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		return list != null ? list.size() : null;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RestaurantHolder holder;
		if (convertView == null){
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant,parent,false);
			holder = new RestaurantHolder(convertView);
			convertView.setTag(holder);
		}else {
			holder = (RestaurantHolder) convertView.getTag();
		}
		holder.bind((Restaurant) getItem(position));
		restaurant = list.get(position);
		return convertView;
	}

		public class RestaurantHolder{
		@Bind(R.id.imgRestaurant) ImageView imgRestaurant;
		@Bind(R.id.tvNameR) TextView tvNameR;
		@Bind(R.id.tvPhoneR) TextView tvPhoneR;
		public RestaurantHolder(View view){
			ButterKnife.bind(this,view);
		}

		public void bind(Restaurant restaurant){
			tvNameR.setText(restaurant.getName());
			if (restaurant.getPhone().equals("0")){
				tvPhoneR.setVisibility(View.GONE);
			}else {
				tvPhoneR.setText("SĐT: " + restaurant.getPhone());
			}
//			imgRestaurant.setImageResource(restaurant.getImage());
			boolean a = list.get(0).getImage().startsWith("/9j/");
			if (a) {
				imgRestaurant.setImageBitmap(base64toBmp(restaurant.getImage()));
			}else {
				String[] image = (restaurant.getImage()).split(";");
//			new DownloadImageTask(holder.imgPlace).execute(list.get(position).getImage());
//				PicassoClient.downloadImage(context, image[0], imgRestaurant);
				Glide.with(context)
						.load(image[0])
						.error(R.drawable.im_thumbnail)
						.skipMemoryCache(true)
						.into(imgRestaurant);
			}
		}
	}
//	public class HotelHolder{
//		@Bind(R.id.imgRestaurant) ImageView imgRestaurant;
//		@Bind(R.id.tvNameR) TextView tvNameR;
//		@Bind(R.id.tvPhoneR) TextView tvPhoneR;
//		public HotelHolder(View view){
//			ButterKnife.bind(this,view);
//		}
//
//		public void bind(Hotel hotel){
//			tvNameR.setText(hotel.getName());
//			tvPhoneR.setText(hotel.getPrice() + " VNĐ");
////			imgRestaurant.setImageResource(hotel.getImage());
//			boolean a = list.get(0).getImage().startsWith("/9j/");
//			if (a) {
//				imgRestaurant.setImageBitmap(base64toBmp(hotel.getImage()));
//			}else {
//				String[] image = (hotel.getImage()).split(";");
////			new DownloadImageTask(holder.imgPlace).execute(list.get(position).getImage());
//				PicassoClient.downloadImage(context, image[0], imgRestaurant);
//
//			}
//		}
//	}

	public Bitmap base64toBmp(String encodedImage){
		byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
		return decodedByte;
	}

}
