package com.bumerang.util;

import com.bumerang.MusorFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MusorszamAdapter extends FragmentPagerAdapter {
	private int NUM_ITEMS;

	public MusorszamAdapter(FragmentManager fm, int NUM_ITEMS) {
		super(fm);
		this.NUM_ITEMS = NUM_ITEMS;
	}

	@Override
	public Fragment getItem(int arg0) {
		return new MusorFragment();
	}

	@Override
	public int getCount() {
		
		return NUM_ITEMS;
	}

}
