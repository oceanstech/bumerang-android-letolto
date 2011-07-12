package com.bumerang;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class ImageServer {
	
	private static ImageServer im;
	
	private ImageServer()
	{
		if(!(new File(Environment.getExternalStorageDirectory()+"/bumerang/cache/")).exists())
   	 {
   		 new File(Environment.getExternalStorageDirectory()+"/bumerang/cache/").mkdirs();
   	 }
	}
	
	public static ImageServer getInstance()
	{
		
			  if(im== null) {
			         im = new ImageServer();
			      }
			      return im;
	}
	
	public Bitmap getImage(String url)
	{
		String [] url_split = url.split("/");
		String filename = url_split[url_split.length-1];
	/*	Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/bumerang/cache/"+filename);
		if(bitmap!=null) 
		return bitmap;*/
			
		
			 Bitmap bm = null; 
		        
		            URL aURL;
				try {	
						aURL = new URL(url);
					
		            URLConnection conn = aURL.openConnection(); 
		            conn.connect(); 
		            InputStream is = conn.getInputStream(); 
		            BufferedInputStream bis = new BufferedInputStream(is); 
		            bm = BitmapFactory.decodeStream(bis); 
		            bis.close(); 
		            is.close(); 
		    /*   
		       File f = new File(Environment.getExternalStorageDirectory()+"/bumerang/cache/"+filename);
		       FileOutputStream outStream;
			
			   outStream = new FileOutputStream(f);
			
		       bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
		       
				outStream.flush();
			 
		      
				outStream.close();*/
		
		        
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
			
					return bm; 
		
	}

}
