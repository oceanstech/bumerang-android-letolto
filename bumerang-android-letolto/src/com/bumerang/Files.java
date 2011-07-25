package com.bumerang;

import java.io.File;
import java.util.Stack;

import com.bumerang.model.FileManager;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
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
		
		FileSysStat();
		
		
		
		
	}
	
	private void FileSysStat()
	{
		 StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
		 FileManager fm = FileManager.getInstance();
		
		 long available_space = stat.getAvailableBlocks();
		 long bumerang_space = dirSize(new File(fm.getDirectory()))/stat.getBlockSize();
		 long left_space=stat.getBlockCount();
		 
		 TextView free_space = (TextView) this.findViewById(R.id.free_space);
		 
		 long bum_mb =  ((bumerang_space*stat.getBlockSize())/(1024*1024));
		 long other_mb =  (((left_space-bumerang_space-available_space)*stat.getBlockSize())/(1024*1024));
		 long free_mb =  ((available_space*stat.getBlockSize())/(1024*1024));
		
		 free_space.setText(free_space.getText()+" "+bum_mb+"/"+other_mb+"/"+free_mb+" MB");
		 
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
