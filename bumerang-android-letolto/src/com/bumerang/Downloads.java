package com.bumerang;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;

import com.bumerang.model.Day;
import com.bumerang.model.Downloader;
import com.bumerang.model.Musor;
import com.bumerang.util.DownloadThreadQueue;
import com.bumerang.util.ExpandableMusorListaAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class Downloads extends Activity {

	String DateUrl;
	private ExpandableMusorListaAdapter adapter;
	static final String BaseUrl = "http://bumerang.hu/?ezaz=2_";
	static final String REGEXP = "<p align=\"justify\">(.+?)</p>";
	static final String REGEXP_MP3 = "http://s.bumerang.hu(.+?).mp3";
	static final String REGEXP_IMAGE = "<img src=\"hirkep/kicsi/(.+?)\" width=\"102\" height=\"252\" align=\"left\" />";
	private Locale Magyar = new Locale("hu","HU");
	
	
	String[] listelements;
	
	ProgressDialog dialog;
	 

	
	private String DayDirectory;
	private Day day;
	

	final Handler handler = new Handler() {
		   public void handleMessage(Message msg) {
			   if(msg.what==0)
				 {
				   adapter.addMusorok(day.getMusorok());
				   lv1.setAdapter(adapter);
					lv1.setGroupIndicator(null);
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
	private ExpandableListView lv1;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		this.setContentView(R.layout.musorlayout);
		 hiba = Toast.makeText(this, "Kapcsolati hiba!", 5000);
		
		DateUrl = this.getIntent().getStringExtra("Date");
		
		
		
		
		  DayDirectory = Environment.getExternalStorageDirectory()+"/bumerang/"+DateUrl;
		
		adapter = new ExpandableMusorListaAdapter(this, new ArrayList<Musor>());
		
		
		
		TextView header = (TextView) findViewById(R.id.downloads_header);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy MMMM d.",Magyar);
	    	 SimpleDateFormat URLDateFormat = new SimpleDateFormat("yyyyMMdd",Magyar);
	    	 
	    	 try {
			String date = format.format(URLDateFormat.parse(DateUrl));
			
			header.setText(date);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		 lv1 = (ExpandableListView)this.findViewById(R.id.listView2);
		//lv1.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , listelements));
		
		
		progress = new Dialog(this);
        progress.setTitle("Lekérés folyamatban...");
		
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.downloads_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    if(item.getItemId() == R.id.item1)
	    {	
	    	
	    	Downloader dl = new Downloader(this.getApplicationContext(), day);
			 dl.start();
			
	    	return true;
	    }
	    
	    return true;
	}
	
	/*
	public void DownThemAll(View view) {
				 
			 DownloadThread dt = new DownloadThread(day);
			 dt.start();
		}*/
	
	
	
	public void downThemAll(View v)
	{
		Downloader dl = new Downloader(this.getApplicationContext(), day);
		DownloadThreadQueue.getInstance().execute(dl);
		
		Toast.makeText(v.getContext(), day.size() + " elem hozzáadva a letöltési listához.", 50000).show();
		
	}

	
}


