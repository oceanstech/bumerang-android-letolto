package com.bumerang.dialogs;

import java.util.HashMap;

import com.bumerang.R;
import com.bumerang.model.Musor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class VideoGalleryDialog extends Dialog {

	private Musor musor;
	private  LinearLayout l;


	public VideoGalleryDialog(Context context,Musor m) {
		super(context,R.style.CustomDialogTheme);
		
		this.musor=m;
		 requestWindowFeature(Window.FEATURE_NO_TITLE);  
		 this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  
		 
		 
		 setContentView(R.layout.videobox);
		  TextView tw = new TextView(this.getContext());
		  tw.setText("Kérlek várj");
		  
		  l = (LinearLayout)this.findViewById(R.id.videoscroll_container);
		  l.removeAllViews();
		  l.addView(tw);
		  
		  DownloadVideoImages dt = new DownloadVideoImages(musor);
		  dt.start();
	}

	
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.dismiss();
	}




	final Handler handler = new Handler() {
		   public void handleMessage(Message msg) {
			 if(msg.what==0){
				 l.removeAllViews();
				 HashMap<String,Bitmap> image_map = musor.getVideoPreviews();
				 for(final String link : musor.getVideos())
				 {
					 ImageButton ib = new ImageButton(l.getContext());
					 if(image_map.get(link)==null)
					 ib.setImageResource(R.drawable.novideo);
					 else
					 ib.setImageBitmap(image_map.get(link));
					 ib.setBackgroundResource(R.drawable.child_bg);
					
					 ib.setOnClickListener(new View.OnClickListener(){
						 
							public void onClick(View v) {
								
										 v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));   
										
													
								 			
							}

				     });
					 
					 l.addView(ib);
			 }
				}
	}
	};
	

	class DownloadVideoImages extends Thread {

		private Musor musor;


		public DownloadVideoImages(Musor m) {
	       
	         this.musor = m;
	         
	     }
		
		 public void run() 
		  {	 
		 musor.getVideoPreviews();
		 handler.sendEmptyMessage(0);
		  }
	}


	
	 
	 



	
	
}
