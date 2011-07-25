package com.bumerang.dialogs;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.bumerang.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Info extends Dialog {

		LinearLayout l;
	   private Context context;
	   LayoutInflater infalInflater;
	   private String html;
	final Handler handler = new Handler() {
		

		public void handleMessage(Message msg) {
			 if(msg.what==4){
				
				 l.removeAllViews();
				 TextView tw = new TextView(context);
					tw.setText("Verzió:"+this_version);
					l.addView(tw);
				
				  
				  Button button = (Button) infalInflater.inflate(R.layout.selector_button, null);
					
					
					button.setText("Logout információs oldal");
					button.setOnClickListener(new View.OnClickListener(){

						public void onClick(View v) {
							Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
						      
							  Uri u = Uri.parse("http://logout.hu/bejegyzes/sofian/bumerang_letolto_android/hsz_1-50.html");
						        intent.setData(u);
						        				        
						       v.getContext().startActivity(intent);
							
						}

						
						
					});
				 
					  l.addView(button);
					  
					  
					  button = (Button) infalInflater.inflate(R.layout.selector_button, null);
						
						
						button.setText("Lekérdezés folyamatban...");
						
				
						  l.addView(button);
				
				}
			 
			 else if(msg.what==2){
					
				 l.removeAllViews();
				  
				
				 TextView tw = new TextView(context);
					tw.setText("Verzió:"+this_version);
					l.addView(tw);
				  Button button = (Button) infalInflater.inflate(R.layout.selector_button, null);
					
					
					button.setText("Logout információs oldal");
					button.setOnClickListener(new View.OnClickListener(){

						public void onClick(View v) {
							Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
						      
							  Uri u = Uri.parse("http://logout.hu/bejegyzes/sofian/bumerang_letolto_android/hsz_1-50.html");
						        intent.setData(u);
						        				        
						       v.getContext().startActivity(intent);
							
						}

						
						
					});
				 
					  l.addView(button);
					  
					  
					  button = (Button) infalInflater.inflate(R.layout.selector_button, null);
						
						
						button.setText("Frissítés: "+html+" verzióra");
						
						button.setOnClickListener(new View.OnClickListener(){
						public void onClick(View v) {
							Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
						      
							  Uri u = Uri.parse("http://www.4shared.com/file/0Guy4Lwu/Bumerang.html");
						        intent.setData(u);
						        				        
						       v.getContext().startActivity(intent);
							
						}
						});
						
						  l.addView(button);
				
				}
			 
			 else if(msg.what==0){
					
				 l.removeAllViews();
				  
				
				 TextView tw = new TextView(context);
					tw.setText("Verzió:"+this_version);
					l.addView(tw);
				  Button button = (Button) infalInflater.inflate(R.layout.selector_button, null);
					
					
					button.setText("Logout információs oldal");
					button.setOnClickListener(new View.OnClickListener(){

						public void onClick(View v) {
							Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
						      
							  Uri u = Uri.parse("http://logout.hu/bejegyzes/sofian/bumerang_letolto_android/hsz_1-50.html");
						        intent.setData(u);
						        				        
						       v.getContext().startActivity(intent);
							
						}

						
						
					});
				 
					  l.addView(button);
					  
					  
					  button = (Button) infalInflater.inflate(R.layout.selector_button, null);
						
						
						button.setText("Nincs elérhetõ frissítés!");
						
						
						  l.addView(button);
							 
				}
			 
			 else if(msg.what==1){
					
				 l.removeAllViews();
				  
				
				 TextView tw = new TextView(context);
					tw.setText("Verzió:"+this_version);
					l.addView(tw);
				  Button button = (Button) infalInflater.inflate(R.layout.selector_button, null);
					
					
					button.setText("Logout információs oldal");
					button.setOnClickListener(new View.OnClickListener(){

						public void onClick(View v) {
							Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
						      
							  Uri u = Uri.parse("http://logout.hu/bejegyzes/sofian/bumerang_letolto_android/hsz_1-50.html");
						        intent.setData(u);
						        				        
						       v.getContext().startActivity(intent);
							
						}

						
						
					});
				 
					  l.addView(button);
					  
					  
					  button = (Button) infalInflater.inflate(R.layout.selector_button, null);
						
						
						button.setText("Kapcsolódási hiba...");
						
						
						  l.addView(button);
				
				
				 
					 
					 
			 
				}
	}
	};
	private Double this_version;
	
	public Info(final Context context) {
		super(context,R.style.CustomDialogTheme);
		
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			this_version = Double.valueOf(packageInfo.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		this.context = context;
		 requestWindowFeature(Window.FEATURE_NO_TITLE);  
		 this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  
		this.setContentView(R.layout.selectorbox);
		
		TextView title = (TextView)this.findViewById(R.id.textView1);
		
		title.setText("Információk");
		
		l = (LinearLayout)this.findViewById(R.id.videoscroll_container);
		  l.removeAllViews();
		  
		  infalInflater = (LayoutInflater) context
		     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		  
		  Button button = (Button) infalInflater.inflate(R.layout.selector_button, null);
			
			TextView tw = new TextView(context);
			tw.setText("Verzió:"+this_version);
			l.addView(tw);
			button.setText("Logout információs oldal");
			button.setOnClickListener(new View.OnClickListener(){

				public void onClick(View v) {
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
				      
					  Uri u = Uri.parse("http://logout.hu/bejegyzes/sofian/bumerang_letolto_android/hsz_1-50.html");
				        intent.setData(u);
				        				        
				       v.getContext().startActivity(intent);
					
				}

				
				
			});
		 
			  l.addView(button);
			  
			  
			  button = (Button) infalInflater.inflate(R.layout.selector_button, null);
				
				
				button.setText("Frissítés keresése");
				button.setOnClickListener(new View.OnClickListener(){

					public void onClick(View v) {
					
						  CheckUpdate dt = new CheckUpdate(context);
						  dt.start();
						
					}

					
					
				});
		
				  l.addView(button);
	}

	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		this.dismiss();
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
						 handler.sendEmptyMessage(4);
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
								 handler.sendEmptyMessage(2);
							}
							else 
							handler.sendEmptyMessage(0);
						} catch (NameNotFoundException e) {
							handler.sendEmptyMessage(1);
						}
					}
				
		       
			 
		
		  }
	}


}
