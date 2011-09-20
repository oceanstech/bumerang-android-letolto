package com.bumerang.model;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;



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
