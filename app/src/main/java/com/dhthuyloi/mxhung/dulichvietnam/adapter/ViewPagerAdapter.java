package com.dhthuyloi.mxhung.dulichvietnam.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by MXHung on 8/31/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
	private final ArrayList<Fragment> listFragment = new ArrayList<>();
	private final ArrayList<String> listTitle = new ArrayList<>();
	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return listFragment.get(position);
	}

	@Override
	public int getCount() {
		return listFragment.size();
	}

	public void addFragment(Fragment fragment, String title){
		listFragment.add(fragment);
		listTitle.add(title);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return listTitle.get(position);
	}
}
