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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;




import android.os.Environment;

public class FileManager {
	
	final static String directory = Environment.getExternalStorageDirectory().getAbsolutePath()+"/bumerang/";
	
	public static String getDirectory() {
		return directory;
	}

	private static FileManager instance;
	
	
	private FileManager()
	{
		 if(!(new File(directory)).exists())
    	 {
    		 new File(directory).mkdirs();
    	 }
		 
		 
	}
	
	public static FileManager getInstance()
	{
		if(instance == null)
		{
			instance = new FileManager();
			return instance;
		}
		else
			return instance;
	}
	
	public String isExisting(String date, int count)
	{
		String path = directory+date+"/"+date+"_"+(count+1)+".mp3";
		 if((new File(path)).exists())
    	 {
    		return path;
    	 }
		 return null;
	}
	
	 static public boolean deleteDirectory(File path) {
		    if( path.exists() ) {
		      File[] files = path.listFiles();
		      for(int i=0; i<files.length; i++) {
		         if(files[i].isDirectory()) {
		           deleteDirectory(files[i]);
		         }
		         else {
		           files[i].delete();
		         }
		      }
		    }
		    return( path.delete() );
		  }
	
	public void deleteFolder(Date date)
	{
		String path = directory+new SimpleDateFormat("yyyyMMdd").format(date);
		deleteDirectory(new File(path));
	}
	
	public void deleteFile(String path)
	{
		new File(path).delete();
	}
	
	public static ArrayList<String> getAllFilePath(String parent)
	{
		ArrayList<String> file_paths = new ArrayList<String>();
		if(parent == null) parent = directory;
		File[] children = new File(parent).listFiles();
		for(File child : children)
		{
			if(child.isDirectory())
				{
					file_paths.addAll(getAllFilePath(child.getAbsolutePath()));
				}
			else
			{
				if(child.getAbsolutePath().endsWith(".mp3"))
					file_paths.add(child.getAbsolutePath());
				
			}
		}
		return file_paths;
	}



}
