package com.bumerang;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import org.apache.http.client.ClientProtocolException;

import com.bumerang.model.FileManager;
import com.bumerang.model.Months;
import com.bumerang.util.NaptarListaAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class BumerangCalendar extends Activity {
	
	Months months;
	private ListView lv1;
	ArrayList<Calendar> l;
	SimpleDateFormat format;
	SimpleDateFormat URLDateFormat;
	Intent intent;
	FileManager storage;
	Dialog progress;
	Toast hiba;
	Animation animation_out;
	Animation animation;
	Locale Magyar = new Locale("hu","HU");
	Thread t;
	
	final Handler handler = new Handler() {
		   public void handleMessage(Message msg) {
			 if(msg.what==0)
			 {
				 try {
					l = months.getPrev();
					progress.dismiss();
			        display(l);
			        
			        
			        animation_out = AnimationUtils.loadAnimation( BumerangCalendar.this, R.anim.slide__right_right );
					
			        lv1.startAnimation( animation_out );
				} catch (ClientProtocolException e) {
					
					handler.sendEmptyMessage(1);
				} catch (IOException e) {
					
					handler.sendEmptyMessage(1);
				}
				
			 }
			 else if(msg.what==1)
			 {
				 progress.dismiss();
				hiba.show();
			        display(l);
			 }
			 else if(msg.what==2)
			 {
				 
				 progress.dismiss();
				 display(l);
				 animation_out = AnimationUtils.loadAnimation( BumerangCalendar.this, R.anim.slide__right_right );
					
			        lv1.startAnimation( animation_out );
			 }
			 
			 }
		   
		   
	};
	private NaptarListaAdapter adapter;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
    	 requestWindowFeature(Window.FEATURE_NO_TITLE);
    	 getWindow().setFormat(PixelFormat.RGBA_8888);
    	 getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        	hiba =  Toast.makeText(this, "Kapcsolati hiba!", 5000);
    	 format =
         new SimpleDateFormat("yyyy MMMM d.",Magyar);
    	 URLDateFormat = new SimpleDateFormat("yyyyMMdd",Magyar);
    	 intent = new Intent().setClass(this, Downloads.class);
    	
    	 storage = FileManager.getInstance();
    	 
        this.setContentView(R.layout.naptarlayout);
        
        progress = new Dialog(this);
        progress.setTitle("Lekérés folyamatban...");
        
        
        adapter = new NaptarListaAdapter(this, new ArrayList<Calendar>());
        lv1 = (ListView)findViewById(R.id.calendarlist);
        lv1.setAdapter(adapter);
		

        OnItemClickListener myClickListener = new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			
				intent.putExtra("Date",URLDateFormat.format(l.get(arg2).getTime()));
				
				
				arg1.getContext().startActivity(intent);
				
			}
			};
		
		lv1.setOnItemClickListener(myClickListener);
		
		t = new Thread(new Runnable(){
		
		public void run() {
			try {
			
				months = new Months();
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
    
    public void Back(View view) {
    	animation = AnimationUtils.loadAnimation( BumerangCalendar.this, R.anim.slide_right );
		lv1.startAnimation( animation );
		t = new Thread(new Runnable(){
			
			public void run() {
				try {
					if(months!=null)
					l = months.getPrev();
					else
					{
						months = new Months();
						l = months.getPrev();
					}
					handler.sendEmptyMessage(2);
				} catch (ClientProtocolException e) {
					handler.sendEmptyMessage(1);
				} catch (IOException e) {
					handler.sendEmptyMessage(1);
				}
				
			}});
		 	progress = new Dialog(this);
	        progress.setTitle("Lekérés folyamatban...");
			progress.show();
			t.start();
    	
       
    }
    
    public void Forward(View view) {
    	animation = AnimationUtils.loadAnimation( BumerangCalendar.this, R.anim.slide_left );
    	
    	animation.setAnimationListener(new AnimationListener(){

			public void onAnimationEnd(Animation animation) {
				animation = AnimationUtils.loadAnimation( BumerangCalendar.this, R.anim.slide_left_left);
				lv1.startAnimation(animation);
				
			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
    		
    	});
    	
		lv1.startAnimation( animation );
    	if(months != null)
    	 l = months.getNext();
        display(l);
    }
    public void display(ArrayList<Calendar> l)
    {
    	if(l!=null && l.size()!=0)
    	{
    		SimpleDateFormat month_format = new SimpleDateFormat("yyyy MMMM",Magyar);
    		TextView nh = (TextView)this.findViewById(R.id.naptar_header);
            nh.setText(month_format.format(l.get(0).getTime()));
            
    	adapter = new NaptarListaAdapter(this, l);
		
    	
    	lv1.setAdapter(adapter);
    	}
    	
    }
}