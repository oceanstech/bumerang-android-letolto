package com.bumerang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

public class Main extends Activity {
	
	
	final Handler handler = new Handler() {
		   private Notification notifyDetails;
		private RemoteViews contentView;
		

		public void handleMessage(Message msg) {
			 if(msg.what==0){
				 
				 Intent notificationIntent = new Intent(android.content.Intent.ACTION_VIEW);
			      
				  Uri u = Uri.parse("http://www.4shared.com/file/0Guy4Lwu/Bumerang.html");
				  notificationIntent.setData(u);
			        				 
					  PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
					 notifyDetails = new Notification(R.drawable.noitification,"Elérhető frissítés!",System.currentTimeMillis());
					
					 notifyDetails.contentIntent = contentIntent;
					// notifyDetails.setLatestEventInfo(getApplicationContext(), msg.getData().getString("title"), msg.getData().getString("text"), contentIntent);
					 contentView = new RemoteViews(context.getPackageName(), R.layout.error_notification);
					 contentView.setTextViewText(R.id.textView1, "Új verzió érhető el!");
					 contentView.setTextViewText(R.id.error_message, "Jelenlegi verzió: "+this_version+" Új verzió: "+html);
					 notifyDetails.contentView = contentView;
					 
					 mManager.notify(2, notifyDetails);
							 
				}
			 else if(msg.what==1)
			 {
				 hiba.show();
			 }
	}
	};
	public String html;
	private Double this_version;
	private Context context;
	private NotificationManager mManager;
	private Toast hiba;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.context = this.getApplicationContext();
		mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		hiba = Toast.makeText(this, "Frissítések ellenőrzése nem sikerült!", 5000);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 getWindow().setFormat(PixelFormat.RGBA_8888);
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		this.setContentView(R.layout.main);
		
		PackageInfo packageInfo;
		try {
			packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			this_version = Double.valueOf(packageInfo.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CheckUpdate dt = new CheckUpdate(context);
		  dt.start();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.info_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    if(item.getItemId() == R.id.item1)
	    {	
	    	
	    Info idi = new Info(this);
	    idi.show();
			
	    	return true;
	    }
	    
	    return true;
	}
	public void GoToCalendar(View v)
	{
		Intent intent = new Intent().setClass(this, BumerangCalendar.class);
		startActivity(intent);
	}
	
	public void online(View v)
	{
		ArrayList<Button> buttons = new ArrayList<Button>();
		 LayoutInflater infalInflater = (LayoutInflater) this
	     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		Button button = (Button) infalInflater.inflate(R.layout.selector_button, null);
		
				
		button.setText("96kbps");
		button.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				 Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
			      
				  Uri u = Uri.parse("http://neofmstream2.gtk.hu:8080");
				  intent.setDataAndType(u, "audio/mpeg");
			        
			        
			       v.getContext().startActivity(intent);
				
			}
			
		});
		
		buttons.add(button);
		button = (Button) infalInflater.inflate(R.layout.selector_button, null);
		button.setText("128kbps");

		
		
		button.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				 Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
			      
				  Uri u = Uri.parse("http://www.xhosting.hu/NeoFM/128_kbs_mp3.m3u");
			        intent.setData(u);
			        
			       v.getContext().startActivity(intent);
				
			}
			
		});
		
		
		buttons.add(button);
		
		SelectorDialog d = new SelectorDialog(v.getContext(),"Minoség",buttons);
		
		d.show();
		
	
	}
	
	public void ustream(View v)
	{
		
		ArrayList<Button> buttons = new ArrayList<Button>();
		 LayoutInflater infalInflater = (LayoutInflater) this
	     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		Button button = (Button) infalInflater.inflate(R.layout.selector_button, null);
		
				
		button.setText("Teljes stúdió");
		button.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
			      
				  Uri u = Uri.parse("http://www.ustream.tv/mobile/view/channel/6358375");
			        intent.setData(u);
			        
			      
			        
			        
			       v.getContext().startActivity(intent);
				
			}
			
		});
		
		buttons.add(button);
		button = (Button) infalInflater.inflate(R.layout.selector_button, null);
		button.setText("Műsorvezetők");

		
		
		button.setOnClickListener(new OnClickListener(){
		
			public void onClick(View v) {
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
			      
				  Uri u = Uri.parse("http://www.ustream.tv/mobile/view/channel/8618450");
			        intent.setData(u);
			        
			      
			        
			        
			       v.getContext().startActivity(intent);
				
				
			}
			
		});
		
		
		buttons.add(button);
		
		button = (Button) infalInflater.inflate(R.layout.selector_button, null);
		button.setText("Segítség a játékokhoz");

		
		
		button.setOnClickListener(new OnClickListener(){
		
			public void onClick(View v) {
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
			      
				  Uri u = Uri.parse("http://www.ustream.tv/mobile/view/channel/6926046");
			        intent.setData(u);
			        
			      
			        
			        
			       v.getContext().startActivity(intent);
				
				
			}
			
		});
		
		
		buttons.add(button);
		
		SelectorDialog d = new SelectorDialog(v.getContext(),"Csatornák",buttons);
		
		d.show();
		
		  
	}
	

	
	class CheckUpdate extends Thread {

			private Context context;

			public CheckUpdate(Context c) {
		       
		         this.context = c;
		         
		     }
			
			 public void run() 
			  {	 
				 
				 HttpClient client = new DefaultHttpClient();
			        HttpGet request = new HttpGet("http://vikcuccok.neobase.hu/sites/vikcuccok.neobase.hu/files/bumerang/ver.txt");
			        HttpResponse response = null;
			        
					
						try {
							
							response = client.execute(request);
								       
			        InputStream in = null;
					
						in = response.getEntity().getContent();
					
			        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			        StringBuilder str = new StringBuilder();
			        String line = null;
			        
						while((line = reader.readLine()) != null)
						{
						    str.append(line);
						}
						in.close();
					 
						 html = str.toString();
						} catch (ClientProtocolException e) {
							handler.sendEmptyMessage(1);
						} catch (IOException e) {
							handler.sendEmptyMessage(1);
						}
						
						if(html!=null)
						{
							try {
								PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
								if(Double.valueOf(html)>Double.valueOf(packageInfo.versionName))
								{
									 handler.sendEmptyMessage(0);
								}
								
							} catch (NameNotFoundException e) {
								handler.sendEmptyMessage(1);
							}
						}
					
			       
				 
			
			  }
		}

}
