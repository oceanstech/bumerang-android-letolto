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
	
		
		
	 if (convertView == null) {
         LayoutInflater infalInflater = (LayoutInflater) context
                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         convertView = infalInflater.inflate(R.layout.musor_elem_opened, null);
     }
	 
	 LinearLayout ll = (LinearLayout)convertView.findViewById(R.id.linearLayout3);
	 ll.removeAllViews();
	
	 LayoutInflater infalInflater = (LayoutInflater) context
     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
	 final Uri u;
		
	 String url = filemanager.isExisting(data.getDate(), groupPosition);
	 
	 
	 
	if(url == null && data.getMP3()!=null)	    
	{
		u = Uri.parse(data.getMP3());
		Button download= (Button) infalInflater.inflate(R.layout.download_button, null);
		
		download.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
			Downloader dt = new Downloader(context,data);
			DownloadThreadQueue.getInstance().execute(dt);
			Toast.makeText(v.getContext(), "Egy elem hozzáadva a letöltési listához.", 50000).show();
		}});
	    ll.addView(download);
	}
	   	
	    else
	    {
	    	u = Uri.parse("file://"+url);
	    }
	 
	if(data.getMP3()!=null)	    
	{
	
	Button play = (Button) infalInflater.inflate(R.layout.play_button, null);
	
	play.setOnClickListener(new View.OnClickListener() {
		   
		   public void onClick(View v) {
  
			   
		        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
		      
		        intent.setDataAndType(u,"audio/mp3"); 
		      
		        try { 
		                  v.getContext().startActivity(intent); 
		           } catch (ActivityNotFoundException e) { 
		                  e.printStackTrace(); 
		           } 
		   }
		  });
	
	ll.addView(play);
	}
	 if(data.hasVideos())
     {
		 
		  infalInflater = (LayoutInflater) context
         .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Button show = (Button) infalInflater.inflate(R.layout.video_button, null);
		
		

		 show.setOnClickListener(new View.OnClickListener() {
			   
			   public void onClick(View v) {
			    VideoGalleryDialog customizeDialog = new VideoGalleryDialog(context,data);
			    customizeDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
			    customizeDialog.show();
			   }
			  });
		 
		 

		 ll.addView(show);
		 
    
     }
	 
	 ImageView image = (ImageView) convertView.findViewById(R.id.musor_image);
	 image.setImageBitmap(data.getImage());
	 
	 TextView tv = (TextView) convertView.findViewById(R.id.description_text);
     tv.setText(data.getTartalom());
    

    
     return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		
		
		
		return musorok.get(groupPosition);
	}

	public int getGroupCount() {
		// TODO Auto-generated method stub
		return musorok.size();
	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		Musor data = (Musor) this.getGroup(groupPosition);
		if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.musor_elem_closed, null);
        }
		
		LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.linearLayout2);
		ll.removeAllViews();
        TextView tv = (TextView) convertView.findViewById(R.id.title_text);
        tv.setText(data.getTitle());
        TextView tv2 = (TextView) convertView.findViewById(R.id.time_text);
        tv2.setText(data.getTime());
        
        if(data.hasVideos())
        {
        	 
        	 ImageView iw = new ImageView(context);
        	 iw.setImageResource(R.drawable.video);
        	 ll.addView(iw);
        }
       
        if(filemanager.isExisting(data.getDate(), groupPosition)!=null)
        {
        	
       	 ImageView iw = new ImageView(context);
       	 iw.setImageResource(R.drawable.saved);
       	 ll.addView(iw);
       	
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

	 
	
	
	 
}
