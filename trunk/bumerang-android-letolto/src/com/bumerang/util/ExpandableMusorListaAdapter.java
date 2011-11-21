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

import java.util.ArrayList;

import com.bumerang.R;
import com.bumerang.dialogs.VideoGalleryDialog;
import com.bumerang.model.Downloader;
import com.bumerang.model.FileManager;
import com.bumerang.model.Musor;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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

public class ExpandableMusorListaAdapter extends BaseExpandableListAdapter{

	private Context context;

    private ArrayList<Musor> musorok;
    

	private FileManager filemanager;
	
    public ExpandableMusorListaAdapter(Context context,ArrayList<Musor> groups) 
    {
        this.context = context;
        this.musorok = groups;
        filemanager = FileManager.getInstance();
       
              
    }

    
    
    public void addMusor(Musor temp)
    {
    	musorok.add(temp);
    	
    	 	
    }
    
    public void addMusorok(ArrayList<Musor> musorok)
    {
    	for(Musor m : musorok)
    	{
    		addMusor(m);
    	}
    }
    
	public Object getChild(int groupPosition, int childPosition) {
		
		return  musorok.get(groupPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getChildView(final int groupPosition,  int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		 final Musor data = (Musor)this.getChild(groupPosition, childPosition);
	
		ViewHolderChildren holder = null;
		
	 if (convertView == null) {
         LayoutInflater infalInflater = (LayoutInflater) context
                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         convertView = infalInflater.inflate(R.layout.musor_elem_opened, null);
         
         holder = new ViewHolderChildren();
         holder.ll =  (LinearLayout)convertView.findViewById(R.id.linearLayout3);
         holder.download= (Button) infalInflater.inflate(R.layout.download_button, null);
         holder.play = (Button) infalInflater.inflate(R.layout.play_button, null);
         holder.show = (Button) infalInflater.inflate(R.layout.video_button, null);
         holder.image = (ImageView) convertView.findViewById(R.id.musor_image);
    	 holder.tv = (TextView) convertView.findViewById(R.id.description_text);
         convertView.setTag(holder);
     }
	 else
	 {
		 holder = (ViewHolderChildren)convertView.getTag();
	 }
	
	 holder.ll.removeAllViews();
	 holder.image.setImageBitmap(data.getImage());
	 holder.tv.setText(data.getTartalom());
	 final Uri u;
		
	 String url = filemanager.isExisting(data.getDate(), groupPosition);
	 
	 
	if(url == null && data.getMP3()!=null)	    
	{
		u = Uri.parse(data.getMP3());
		
		
		holder.download.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
			Downloader dt = new Downloader(context,data);
			DownloadThreadQueue.getInstance().execute(dt);
			Toast.makeText(v.getContext(), "Egy elem hozzáadva a letöltési listához.", 50000).show();
		}});
	    holder.ll.addView(holder.download);
	}
	   	
	    else
	    {
	    	u = Uri.parse("file://"+url);
	    }
	 
	if(data.getMP3()!=null)	    
	{
	
	holder.play.setOnClickListener(new View.OnClickListener() {
		   
		public void onClick(View v) {
  
			
		        Intent intent = new Intent(Intent.ACTION_VIEW);
		       intent.setDataAndType(u,"audio/mp3"); 
		      
		      
		       try { 
		                  v.getContext().startActivity(intent); 
		           } catch (ActivityNotFoundException e) { 
		                  e.printStackTrace(); 
		           } 
		   }
		  });
	
	holder.ll.addView(holder.play);
	}
	 if(data.hasVideos())
     {
		 holder.show.setOnClickListener(new View.OnClickListener() {
			   
			   public void onClick(View v) {
			    VideoGalleryDialog customizeDialog = new VideoGalleryDialog(context,data);
			    customizeDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
			    customizeDialog.show();
			   }
			  });
		 
		 

		 holder.ll.addView(holder.show);
	}
	 
     return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		
		return 1;
	}

	public Object getGroup(int groupPosition) {
		
		return musorok.get(groupPosition);
	}

	public int getGroupCount() {
		
		return musorok.size();
	}

	public long getGroupId(int groupPosition) {
		
		return 0;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolderParent holder=null;
		Musor data = (Musor) this.getGroup(groupPosition);
		if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.musor_elem_closed, null);
            holder=new ViewHolderParent();
            
            holder.ll =  (LinearLayout) convertView.findViewById(R.id.linearLayout2);
            holder.tv  = (TextView) convertView.findViewById(R.id.title_text);
            holder.tv2 = (TextView) convertView.findViewById(R.id.time_text);
            holder.iw = new ImageView(context);
            holder.iw.setImageResource(R.drawable.video);
            holder.saved = new ImageView(context);
            holder.saved.setImageResource(R.drawable.saved);
            convertView.setTag(holder);
        }
		else
		{
			holder = (ViewHolderParent)convertView.getTag();
		}
		
		
		holder.ll.removeAllViews();
        
		holder.tv.setText(data.getTitle());
        
		holder.tv2.setText(data.getTime());
        
        if(data.hasVideos())
        {
        	 holder.ll.addView(holder.iw);
        }
       
        if(filemanager.isExisting(data.getDate(), groupPosition)!=null)
        { 	       	 
       	 holder.ll.addView(holder.saved);
        }
                
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

	 
	 static class ViewHolderParent {
         public ImageView saved;
         public ImageView iw;
         public TextView tv2;
         public TextView tv;
         LinearLayout ll;
         ImageView icon;
     }
	 
	 static class ViewHolderChildren {

		public TextView tv;
		public ImageView image;
		public Button show;
		public Button play;
		public Button download;
		public LinearLayout ll;
         
     }
	
	 
}
