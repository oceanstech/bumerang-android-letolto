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
package com.bumerang.dialogs;

import java.io.File;
import java.util.Stack;

import com.bumerang.R;
import com.bumerang.model.FileManager;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatDialog extends Dialog {

	public StatDialog(Context context) {
		super(context,android.R.style.Theme_Dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		 this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  
		this.setContentView(R.layout.stat_dialog);
		// TODO Auto-generated constructor stub
		
		FileSysStat();
		
		
	}
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		this.dismiss();
	}
	
	private void FileSysStat()
	{
		 StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
		 FileManager fm = FileManager.getInstance();
		
		 long available_space = stat.getAvailableBlocks();
		 long bumerang_space = dirSize(new File(fm.getDirectory()))/stat.getBlockSize();
		 long left_space=stat.getBlockCount();
		 
		 TextView free_space = (TextView) this.findViewById(R.id.free_space);
		 TextView bum_space = (TextView) this.findViewById(R.id.bum_space);
		 TextView other_space = (TextView) this.findViewById(R.id.other_space);
		 
		 long bum_mb =  ((bumerang_space*stat.getBlockSize())/(1024*1024));
		 long other_mb =  (((left_space-bumerang_space-available_space)*stat.getBlockSize())/(1024*1024));
		 long free_mb =  ((available_space*stat.getBlockSize())/(1024*1024));
		
		 free_space.setText(Long.toString(free_mb));
		 bum_space.setText(Long.toString(bum_mb)+"/");
		 other_space.setText(Long.toString(other_mb)+"/");
		 
		 ImageView bum_bar = (ImageView) this.findViewById(R.id.bumerang_bar);
		 ImageView other_bar = (ImageView) this.findViewById(R.id.other_bar);
		 ImageView free_bar = (ImageView) this.findViewById(R.id.free_bar);
		 
		 float all = bum_mb+other_mb+free_mb;
		 
		 LinearLayout.LayoutParams param = (android.widget.LinearLayout.LayoutParams) bum_bar.getLayoutParams();
		 
		 param.weight=bum_mb/all;
		
		 bum_bar.setLayoutParams(param);
		 param = (android.widget.LinearLayout.LayoutParams) other_bar.getLayoutParams();
		 
		 param.weight=other_mb/all;
		 other_bar.setLayoutParams(param);
		 param = (android.widget.LinearLayout.LayoutParams) free_bar.getLayoutParams();
		 
		 param.weight=free_mb/all;
		 free_bar.setLayoutParams(param);
		 

	}
	
	private static long dirSize(File dir) {
	    long result = 0;

	    Stack<File> dirlist= new Stack<File>();
	    dirlist.clear();

	    dirlist.push(dir);

	    while(!dirlist.isEmpty())
	    {
	        File dirCurrent = dirlist.pop();

	        File[] fileList = dirCurrent.listFiles();
	        for (int i = 0; i < fileList.length; i++) {

	            if(fileList[i].isDirectory())
	                dirlist.push(fileList[i]);
	            else
	                result += fileList[i].length();
	        }
	    }

	    return result;
	}

}
