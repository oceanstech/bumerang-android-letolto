package com.bumerang.model;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

import com.bumerang.model.FileManager.FileComparator;

import android.os.Environment;

public class FileManager {
	
	final static String directory = Environment.getExternalStorageDirectory().getAbsolutePath()+"/bumerang/";
	
	public static String getDirectory() {
		return directory;
	}

	private static FileManager instance;
	private static DirComparator dirCompare;
	private static FileComparator fileCompare;
	
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
	
	public TreeSet<TreeMap<Integer, File>> getFileStructure()
	{	
		AudioFile af;
		File dir = new File(getDirectory());
		
		File[] directories = dir.listFiles();
		if(dirCompare==null) dirCompare = new DirComparator();
		if(fileCompare==null) fileCompare = new FileComparator();
		
		TreeSet<TreeMap<Integer, File>> mp3_dir = new TreeSet<TreeMap<Integer,File>>(dirCompare);
		
		for(File adir:directories)
		{
			if(adir.getName().length()==8)
			{
				TreeMap<Integer, File> mp3s = new TreeMap<Integer,File>(fileCompare); 
				File[] Files = adir.listFiles();
				
				for(File mp3_file:Files)
				{
					String[] a = mp3_file.getName().split("\\.");
					if(mp3_file.getName().split("\\.")[1].compareTo("mp3")==0)
					{
						
						
						try {
							af = AudioFileIO.read(mp3_file);
							if(af.getTag().hasField(FieldKey.TRACK.toString()))
							mp3s.put(Integer.valueOf(af.getTag().getFirst(FieldKey.TRACK)),mp3_file);
						} catch (CannotReadException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TagException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ReadOnlyFileException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidAudioFrameException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
				}
				mp3_dir.add( mp3s);
				
			}
		
		}
				
		return mp3_dir;
	}
	
	class DirComparator implements Comparator<TreeMap<Integer,File>> {

		@Override
		public int compare(TreeMap<Integer, File> a,
				TreeMap<Integer, File> b) {
			if(a!=null && b!=null)
			{
			if(a.firstKey()<b.firstKey()) return -1;
			else if(a.firstKey()>b.firstKey()) return 1;
			}
			return 0;
		}

		
		
	}
	
class FileComparator implements Comparator<Integer> {

	@Override
	public int compare(Integer a, Integer b) {
		if(a!=null && b!=null)
		{
		if(a.intValue()<b.intValue()) return -1;
		else if(a.intValue()>b.intValue()) return 1;
		}
		
		return 0;
	}

	
	
	}



}
