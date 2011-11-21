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
package com.bumerang.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import com.bumerang.R;
import com.bumerang.model.FileManager;
import com.bumerang.model.Letoltesek;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

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

	public View getChildView(final int groupPosition,  final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		  ViewHolderChildren holder = null;
	 if (convertView == null) {
		 
         LayoutInflater infalInflater = (LayoutInflater) context
                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         convertView = infalInflater.inflate(R.layout.files_elem_child, null);
         holder = new ViewHolderChildren();
         holder.title= (TextView) convertView.findViewById(R.id.musor_cime);
         holder.delbutton= (Button) convertView.findViewById(R.id.child_trash);
         holder.playbutton= (Button) convertView.findViewById(R.id.play_button);
           
        // holder.title2 = (TextView) convertView.findViewById(R.id.size_text);
         
         convertView.setTag(holder);
     }
	
	 else
	 {
		 holder = (ViewHolderChildren) convertView.getTag();
		
	 }
	 
	 holder.delbutton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
					data.deleteFile(groupPosition, childPosition);
					NotifyDataChanged();
				
			}});
	 holder.playbutton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
								
				Uri u = Uri.parse("file://"+files.get(groupPosition).get(childPosition)[1]);
		        Intent intent = new Intent(Intent.ACTION_VIEW);
		       intent.setDataAndType(u,"audio/mp3"); 
		       
		       try { 
	                  v.getContext().startActivity(intent); 
	           } catch (ActivityNotFoundException e) { 
	                  e.printStackTrace(); 
	           } 
					
				
			}});
	 
	
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
            holder.delbutton = (Button)convertView.findViewById(R.id.parentbutton);
           
          
            convertView.setTag(holder);
        }
		else
		{
			holder = (ViewHolderParent) convertView.getTag();
			 
		}
		holder.delbutton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
					data.deleteAlbum(groupPosition);
					NotifyDataChanged();
				
			}});
				
		holder.title.setText(albumok.get(groupPosition)[0]);
		
		holder.title2.setText(String.valueOf(Math.round((Float.valueOf(albumok.get(groupPosition)[1])/(1024*1024))*100)/100)+" MB");
		
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

	private void NotifyDataChanged()
	{
		this.notifyDataSetChanged();
	}
	 
	static class ViewHolderChildren {

		public Button playbutton;
		public Button delbutton;
		public TextView title2;
		public TextView title;
        
    }
	 
	 static class ViewHolderParent {

		public Button delbutton;
		public TextView title2;
		public TextView title;

        
    }


	
	 
}
