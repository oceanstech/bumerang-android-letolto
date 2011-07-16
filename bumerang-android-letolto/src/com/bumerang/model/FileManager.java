package com.bumerang.model;

import java.io.File;

import android.os.Environment;

public class FileManager {
	
	final static String directory = Environment.getExternalStorageDirectory().getAbsolutePath()+"/bumerang/";
	
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

}
