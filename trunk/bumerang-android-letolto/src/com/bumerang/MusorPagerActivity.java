package com.bumerang;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;

import com.bumerang.model.Day;
import com.bumerang.model.Downloader;
import com.bumerang.util.DownloadThreadQueue;
import com.viewpagerindicator.TitlePageIndicator;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MusorPagerActivity extends Activity{
	String DateUrl;

	static final String BaseUrl = "http://bumerang.hu/?ezaz=2_";
	static final String REGEXP = "<p align=\"justify\">(.+?)</p>";
	static final String REGEXP_MP3 = "http://s.bumerang.hu(.+?).mp3";
	static final String REGEXP_IMAGE = "<img src=\"hirkep/kicsi/(.+?)\" width=\"102\" height=\"252\" align=\"left\" />";
	private Locale Magyar = new Locale("hu","HU");
	private ViewPagerAdapter adapter;
	private ViewPager pager;
	private  TitlePageIndicator indicator;

	String[] listelements;

	ProgressDialog dialog;



	private String DayDirectory;
	private Day day;


	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==0)
			{
			     adapter.addMusorok(day.getMusorok());
				 pager.setAdapter( adapter );
				 indicator.setViewPager( pager );
				
				progress.dismiss();
			}
			else if(msg.what==1)
			{
				progress.dismiss();
				hiba.show();

			}

		}
	};
	private Dialog progress;
	private Toast hiba;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		setContentView( R.layout.musrotab_layout );
		hiba = Toast.makeText(this, "Kapcsolati hiba!", 5000);

		DateUrl = this.getIntent().getStringExtra("Date");
		DayDirectory = Environment.getExternalStorageDirectory()+"/bumerang/"+DateUrl;

		TextView header = (TextView) findViewById(R.id.download_tab_header);

		SimpleDateFormat format = new SimpleDateFormat("yyyy MMMM d.",Magyar);
		SimpleDateFormat URLDateFormat = new SimpleDateFormat("yyyyMMdd",Magyar);

		try {
			String date = format.format(URLDateFormat.parse(DateUrl));

			header.setText(date);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		 

		progress = new Dialog(this);
		progress.setTitle("Lekérés folyamatban...");
		adapter = new ViewPagerAdapter( this );
	    pager =
	        (ViewPager)findViewById( R.id.viewpager );
	    indicator =
	        (TitlePageIndicator)findViewById( R.id.indicator );
		Thread t = new Thread(new Runnable(){

			public void run() {
				try {

					day = new Day(DateUrl);

					handler.sendEmptyMessage(0);
				} catch (ClientProtocolException e) {
					handler.sendEmptyMessage(1);
				} catch (IOException e) {
					handler.sendEmptyMessage(1);
				}

			}});
		progress.show();

		t.start();
	   
	 
	    
	   
	}
	
	public void downThemAll(View v)
	{
		Downloader dl = new Downloader(this.getApplicationContext(), day);
		DownloadThreadQueue.getInstance().execute(dl);
		Toast.makeText(v.getContext(), day.size() + " elem hozzáadva a letöltési listához.", 50000).show();		
	}

}
