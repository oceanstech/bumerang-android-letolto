package com.bumerang.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.bumerang.util.db.DOWNLOADSContentProvider;

public class Letoltesek {

	private ArrayList<String[]> albumok;
	private ArrayList<ArrayList<String[]>> musorok = new ArrayList<ArrayList<String[]>>();
	private static Context context;
	private ArrayList<String> deletable = new ArrayList<String>();
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
		c.close();
		return albumok;
	}

	private ArrayList<ArrayList<String[]>> albumFilesQuery(ArrayList<String[]> albumok2)
	{

		String[] projection = {"TITLE","FILENAME","SIZE"};

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
						String[] album_fields = new String[2];
						album_fields[0]=(c.getString(c.getColumnIndex(
								DOWNLOADSContentProvider.TITLE)));
						album_fields[1]=(c.getString(c.getColumnIndex(DOWNLOADSContentProvider.FILENAME)));
						album_files.add(album_fields);

					} while (c.moveToNext());
				}
				c.close();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			musorok.add(album_files);
		}
		
		return musorok;

	}
	
	public static ArrayList<String> filesQuery()
	{
		String[] projection = {"FILENAME"};
		Uri allTitles = Uri.parse("content://com.bumerang.util.downloadscontentprovider/downloads");
		ArrayList<String> filepaths = new ArrayList<String>();
		Cursor c = context.getContentResolver().query(allTitles, projection, null,null, null);
		
		if (c.moveToFirst()) {
			do{
				
				filepaths.add((c.getString(c.getColumnIndex(
						DOWNLOADSContentProvider.FILENAME))));
				
				

			} while (c.moveToNext());
		}
		c.close();
		return filepaths;
	
	
	}

	private void DeleteFolder(Date date)
	{

		Uri allTitles = Uri.parse("content://com.bumerang.util.downloadscontentprovider/downloads");

		String[] sel_args = {String.valueOf(date.getTime())};
		context.getContentResolver().delete(allTitles, "date=?",sel_args);
	}
	
	private void DeleteFile(String path)
	{
		Uri allTitles = Uri.parse("content://com.bumerang.util.downloadscontentprovider/downloads");

		String[] sel_args = {path};
		context.getContentResolver().delete(allTitles, "filename=?",sel_args);
	}

	public void deleteAlbum(int id)
	{
		Date date_folder;
		try {
			date_folder = new SimpleDateFormat("yyyy MMMM d.",Magyar).parse(albumok.get(id)[0]);
			FileManager.getInstance().deleteFolder(date_folder);
			DeleteFolder(date_folder);
			albumok.remove(id);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void deleteFile(int album_id,int file_id)
	{
		if(musorok.get(album_id).size()<=1)
		{
			deleteAlbum(album_id);
			musorok.get(album_id).remove(file_id);
		}
		else
		{
			String path = musorok.get(album_id).get(file_id)[1];
			FileManager.getInstance().deleteFile(musorok.get(album_id).get(file_id)[1]);
			DeleteFile(path);
			musorok.get(album_id).remove(file_id);
		}

	}

}
