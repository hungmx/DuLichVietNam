package com.dhthuyloi.mxhung.dulichvietnam.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.dhthuyloi.mxhung.dulichvietnam.R;

/**
 * Created by MXHung on 9/4/2016.
 */
public class ItemPagerAdapter extends PagerAdapter {
	private Context context;
	private String[] resource;
	private LayoutInflater inflater;
	private int size;
	public ItemPagerAdapter (Context context, String[] resource){
		this.context = context;
		this.resource = resource;
	}
	@Override
	public int getCount() {
		return resource.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_pager,container,false);
		ImageView imageView = (ImageView) view.findViewById(R.id.imItem);
//		imageView.setImageResource(resource[position]);
		Glide.with(context)
				.load(resource[position])
				.error(R.drawable.im_thumbnail)
				.skipMemoryCache(true)
				.into(imageView);
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((LinearLayout)object);
	}
}
