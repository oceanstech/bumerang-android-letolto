package com.bumerang;

import java.util.ArrayList;

import com.bumerang.dialogs.StatDialog;
import com.bumerang.dialogs.UpdateDBDialog;
import com.bumerang.model.FileManager;
import com.bumerang.util.ExpandableDownloadsListaAdapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
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
		UpdateDBDialog udi = new UpdateDBDialog(this);
		udi.setOnDismissListener(new OnDismissListener(){

			@Override
			public void onDismiss(DialogInterface dialog) {
				 initialize();
				
			}});
		 udi.show();
 ImageButton free_space = (ImageButton) this.findViewById(R.id.imageButton1);
		 
		 
		 free_space.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				StatDialog d = new StatDialog(v.getContext());
				d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
				d.show();
				
			}});
		
	}
	
	private void initialize()
	{
		adapter = new ExpandableDownloadsListaAdapter(this);
		lv1 = (ExpandableListView)this.findViewById(R.id.fileslist);
		 lv1.setAdapter(adapter);
			lv1.setGroupIndicator(null);
		
		
	}
	
	
	
}
