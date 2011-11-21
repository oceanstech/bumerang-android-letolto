/*******************************************************************************
 * Copyright 2011 Ait-Said Sofian
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.bumerang;

import com.bumerang.dialogs.StatDialog;
import com.bumerang.dialogs.UpdateDBDialog;
import com.bumerang.util.ExpandableDownloadsListaAdapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

public class Files extends Activity {

	

	private ExpandableDownloadsListaAdapter adapter;
	private ExpandableListView lv1;
	
	

	
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
