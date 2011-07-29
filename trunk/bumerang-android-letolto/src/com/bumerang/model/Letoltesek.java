package com.bumerang.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.bumerang.util.DOWNLOADSContentProvider;

public class Letoltesek {
	
	private ArrayList<String[]> albumok;
	private ArrayList<ArrayList<String[]>> musorok = new ArrayList<ArrayList<String[]>>();
	private Context context;
	private Locale Magyar = new Locale("hu","HU");
	
	public Letoltesek(Context c)
	{
		this.context = c;
	}
	
	public ArrayList<String[]> getAlbums()
	{
		albumok = albumsQuery();
		return albumok;
	}
	
	public ArrayList<ArrayList<String[]>> getFiles(ArrayList<String[]> albumok2)
	{
		return this.albumFilesQuery(albumok2);
	}
	
	private ArrayList<String[]> albumsQuery()
	{
		 albumok = new ArrayList<String[]>();
		
		 Uri allTitles = Uri.parse(
	        "content://com.bumerang.util.downloadscontentprovider/downloads/albums");
	       
	     Cursor c = context.getContentResolver().query(allTitles, null, null, null,null);
	     
	     if (c.moveToFirst()) {
	         do{
	        	 String[] album_fields = new String[2];
	        	 album_fields[0]=(new SimpleDateFormat("yyyy MMMM d.",Magyar).format(new Date(Long.valueOf(c.getString(c.getColumnIndex(
				               DOWNLOADSContentProvider.DATE))))));
	        	 album_fields[1]=(c.getString(c.getColumnIndex(
			               DOWNLOADSContentProvider.SUM_SIZE)));
	        	 albumok.add(album_fields);

	         } while (c.moveToNext());
	         
	     }
	     return albumok;
	}
	
	private ArrayList<ArrayList<String[]>> albumFilesQuery(ArrayList<String[]> albumok2)
	{
		ArrayList<ArrayList<String[]>> Files = new ArrayList<ArrayList<String[]>>();
		String[] projection = {"TITLE","SIZE"};
		
		Uri allTitles = Uri.parse("content://com.bumerang.util.downloadscontentprovider/downloads");
		Date album_date;
		for(String[] album :albumok2)
		{
			ArrayList<String[]> album_files = new ArrayList<String[]>();
		try {
			
			album_date = new SimpleDateFormat("yyyy MMMM d.",Magyar).parse(album[0]);
			String[] sel_args = {String.valueOf(album_date.getTime())};
			Cursor c = context.getContentResolver().query(allTitles, projection, "date=?",sel_args,"position ASC");
			
			if (c.moveToFirst()) {
		         do{
		        	 String[] album_fields = new String[1];
		        	 album_fields[0]=(c.getString(c.getColumnIndex(
					               DOWNLOADSContentProvider.TITLE)));
		        	// album_fields[1]=(c.getString(c.getColumnIndex(DOWNLOADSContentProvider.SUM_SIZE)));
		        	 album_files.add(album_fields);

		         } while (c.moveToNext());
		     }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Files.add(album_files);
		}
		return Files;
		
	}
}
