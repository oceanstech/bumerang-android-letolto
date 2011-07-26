package com.bumerang;

import java.io.File;
import java.util.Stack;

import com.bumerang.dialogs.StatDialog;
import com.bumerang.model.FileManager;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Files extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		
		this.setContentView(R.layout.files_layout);
		
		
		
		 ImageView free_space = (ImageView) this.findViewById(R.id.imageView1);
		 free_space.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				StatDialog d = new StatDialog(v.getContext());
				d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
				d.show();
				
			}});
		
		
	}
	
	
	
}
