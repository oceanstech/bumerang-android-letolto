package com.bumerang;


import com.bumerang.dialogs.StatDialog;
import com.bumerang.model.FileManager;
import com.bumerang.util.DOWNLOADSContentProvider;
import com.bumerang.util.ExpandableDownloadsListaAdapter;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;

public class Files extends Activity {

	private ExpandableDownloadsListaAdapter adapter;
	private ExpandableListView lv1;
	private FileManager fm = FileManager.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		
		this.setContentView(R.layout.files_layout);
		
		adapter = new ExpandableDownloadsListaAdapter(this);
		lv1 = (ExpandableListView)this.findViewById(R.id.fileslist);
		 lv1.setAdapter(adapter);
			lv1.setGroupIndicator(null);
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
