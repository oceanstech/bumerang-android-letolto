package com.bumerang;

import com.viewpagerindicator.TitlePageIndicator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class MusorPagerActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		super.onCreate( savedInstanceState );
	    setContentView( R.layout.musrotab_layout );
	 
	    ViewPagerAdapter adapter = new ViewPagerAdapter( this );
	    ViewPager pager =
	        (ViewPager)findViewById( R.id.viewpager );
	    TitlePageIndicator indicator =
	        (TitlePageIndicator)findViewById( R.id.indicator );
	    pager.setAdapter( adapter );
	    indicator.setViewPager( pager );
	}

}
