package com.bumerang.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
    private HashMap<Integer,Boolean> selected;
    

	private FileManager filemanager;
	
    public ExpandableDownloadsListaAdapter(Context context) 
    {
        this.context = context;
        data  = new Letoltesek(context);
        filemanager = FileManager.getInstance();   
        albumok = data.getAlbums();
        files = data.getFiles(albumok);
        selected = new HashMap<Integer,Boolean>();
       
     
    }

	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getChildView(final int groupPosition,  int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		  ViewHolderChildren holder = null;
	 if (convertView == null) {
		 
         LayoutInflater infalInflater = (LayoutInflater) context
                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         convertView = infalInflater.inflate(R.layout.files_elem_child, null);
         holder = new ViewHolderChildren();
         holder.title= (TextView) convertView.findViewById(R.id.musor_cime);
         holder.checkdel= (CheckBox) convertView.findViewById(R.id.checkBox1);
        // holder.title2 = (TextView) convertView.findViewById(R.id.size_text);
         convertView.setTag(holder);
     }
	
	 else
	 {
		 holder = (ViewHolderChildren) convertView.getTag();
	 }
	 if(selected.get(groupPosition)!=null && selected.get(groupPosition)) holder.checkdel.setChecked(true);
		holder.title.setText(files.get(groupPosition).get(childPosition)[0]);
		//holder.title2.setText(String.valueOf(Float.valueOf(albumok.get(groupPosition)[1])/(1024*1024))+" MB");
	 
	 return convertView;
	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		ViewHolderParent holder = null;
		if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.files_elem_closed, null);
            holder = new ViewHolderParent();
            holder.title = (TextView) convertView.findViewById(R.id.title_text);
            holder.title2 = (TextView) convertView.findViewById(R.id.size_text);
            holder.checkdel = (CheckBox)convertView.findViewById(R.id.checkBox1);
            convertView.setTag(holder);
        }
		else
		{
			holder = (ViewHolderParent) convertView.getTag();
		}
				
		holder.title.setText(albumok.get(groupPosition)[0]);
		if(selected.get(groupPosition)!=null && selected.get(groupPosition)) holder.checkdel.setChecked(true);
		holder.title2.setText(String.valueOf(Math.round((Float.valueOf(albumok.get(groupPosition)[1])/(1024*1024))*100)/100)+" MB");
		holder.checkdel.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				data.deletable(groupPosition);
				selected.put(groupPosition, true);
			
				
			}});
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
		
		return (String[]) files.get(groupPosition).get(childPosition);
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

	 
	static class ViewHolderChildren {

		public CheckBox checkdel;
		public TextView title2;
		public TextView title;
        
    }
	 
	 static class ViewHolderParent {

		public CheckBox checkdel;
		public TextView title2;
		public TextView title;

        
    }
	
	 
}
