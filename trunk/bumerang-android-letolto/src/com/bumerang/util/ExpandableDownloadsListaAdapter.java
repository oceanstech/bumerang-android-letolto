package com.bumerang.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.bumerang.R;
import com.bumerang.dialogs.VideoGalleryDialog;
import com.bumerang.model.Downloader;
import com.bumerang.model.FileManager;
import com.bumerang.model.Letoltesek;
import com.bumerang.model.Musor;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableDownloadsListaAdapter extends BaseExpandableListAdapter{

	private Context context;

	private Locale Magyar = new Locale("hu","HU");
    private ArrayList<String[]> albumok;
    private ArrayList<ArrayList<String[]>> files;
    private Letoltesek data;
    

	private FileManager filemanager;
	
    public ExpandableDownloadsListaAdapter(Context context) 
    {
        this.context = context;
        data  = new Letoltesek(context);
        filemanager = FileManager.getInstance();   
        albumok = data.getAlbums();
        files = data.getFiles(albumok);
       
     
    }

	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getChildView(final int groupPosition,  int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
				
	 if (convertView == null) {
         LayoutInflater infalInflater = (LayoutInflater) context
                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         convertView = infalInflater.inflate(R.layout.musor_elem_closed, null);
     }
	 
	 LinearLayout ll = (LinearLayout)convertView.findViewById(R.id.linearLayout2);
	 ll.removeAllViews();
	
	 TextView title = (TextView) convertView.findViewById(R.id.title_text);
		TextView title2 = (TextView) convertView.findViewById(R.id.time_text);
		title.setText(files.get(groupPosition).get(childPosition)[0]);
		//title2.setText(String.valueOf(Float.valueOf(albumok.get(groupPosition)[1])/(1024*1024))+" MB");
	 
	 return convertView;
	}

	
	
	
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		
		if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.musor_elem_closed, null);
        }
		
		LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.linearLayout2);
		ll.removeAllViews();
		
		TextView title = (TextView) convertView.findViewById(R.id.title_text);
		TextView title2 = (TextView) convertView.findViewById(R.id.time_text);
		title.setText(albumok.get(groupPosition)[0]);
		title2.setText(String.valueOf(Math.round((Float.valueOf(albumok.get(groupPosition)[1])/(1024*1024))*100)/100)+" MB");
                
        
        return convertView;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}






	@Override
	public Object getChild(int groupPosition, int childPosition) {
		
		return files.get(groupPosition).get(childPosition);
	}






	@Override
	public int getChildrenCount(int groupPosition) {
		
		return files.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		
		return albumok.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		
		return albumok.size();
	}

	 
	
	
	 
}
