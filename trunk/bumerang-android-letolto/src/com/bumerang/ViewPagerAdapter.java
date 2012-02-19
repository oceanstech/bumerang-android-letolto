package com.bumerang;

import java.util.ArrayList;

import com.bumerang.dialogs.VideoGalleryDialog;
import com.bumerang.model.Downloader;
import com.bumerang.model.FileManager;
import com.bumerang.model.Musor;
import com.bumerang.util.DownloadThreadQueue;
import com.viewpagerindicator.TitleProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPagerAdapter extends PagerAdapter
implements TitleProvider
{

private FileManager filemanager;
private final Context context;
private ArrayList<Musor> Musorok;


public ViewPagerAdapter( Context context )
{
    this.context = context;
    filemanager = FileManager.getInstance();
    
}


public void addMusorok(ArrayList<Musor> Musorok)
{
	this.Musorok = Musorok;
	
}


@Override
public String getTitle( int position )
{
    return Musorok.get(position).getTime();
}

@Override
public int getCount()
{
    return Musorok.size();
}

@Override
public Object instantiateItem( View pager, final int position )
{
	
        LayoutInflater infalInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableLayout v = (TableLayout) infalInflater.inflate(R.layout.musor_elem_opened, null);
        ImageView Image = (ImageView) v.findViewById(R.id.musor_image);
        TextView TitleText= (TextView) v.findViewById(R.id.TitleText);
        
        Button download= (Button) v.findViewById(R.id.download);
        Button play = (Button) v.findViewById(R.id.play);
        Button show = (Button) v.findViewById(R.id.videos);
   	 	TextView Description = (TextView) v.findViewById(R.id.description_text);
   	 	Description.setMovementMethod(new ScrollingMovementMethod());
       
   	 	TitleText.setText(Musorok.get(position).getTitle());
	 Image.setImageBitmap(Musorok.get(position).getImage());
	 Description.setText(Musorok.get(position).getTartalom());
	 final Uri u;
		
	 String url = filemanager.isExisting(Musorok.get(position).getDate(), position);
	 
	 
	if(url == null && Musorok.get(position).getMP3()!=null)	    
	{
		u = Uri.parse(Musorok.get(position).getMP3());
		
		
		download.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
			Downloader dt = new Downloader(context,Musorok.get(position));
			DownloadThreadQueue.getInstance().execute(dt);
			Toast.makeText(v.getContext(), "Egy elem hozzáadva a letöltési listához.", 50000).show();
		}});
	    download.setEnabled(true);
	}
	   	
	    else
	    {
	    	u = Uri.parse("file://"+url);
	    	download.setEnabled(false);
	    }
	 
	if(Musorok.get(position).getMP3()!=null)	    
	{
	
	play.setOnClickListener(new View.OnClickListener() {
		   
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
	
	play.setEnabled(true);
	}
	 if(Musorok.get(position).hasVideos())
    {
		 show.setOnClickListener(new View.OnClickListener() {
			   
			   public void onClick(View v) {
			    VideoGalleryDialog customizeDialog = new VideoGalleryDialog(context,Musorok.get(position));
			    customizeDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
			    customizeDialog.show();
			   }
			  });
		 
		 

		show.setEnabled(true);
	}
	 
	
	 ( (ViewPager) pager ).addView( v, 0 );
    return v;
}

@Override
public void destroyItem( View pager, int position, Object view )
{
    ((ViewPager)pager).removeView( (TableLayout)view );
}

@Override
public boolean isViewFromObject( View view, Object object )
{
    return view.equals( object );
}

@Override
public void finishUpdate( View view ) {}

@Override
public void restoreState( Parcelable p, ClassLoader c ) {}

@Override
public Parcelable saveState() {
    return null;
}

@Override
public void startUpdate( View view ) {}
}