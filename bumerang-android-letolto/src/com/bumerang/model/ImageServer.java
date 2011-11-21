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
package com.bumerang.model;

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
