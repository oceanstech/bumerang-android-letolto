package com.bumerang;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.widget.RemoteViews;


public class Downloader extends Thread {
	private Day day;
	private Musor musor;
	private Context context;
	private Notification notifyDetails;
	private RemoteViews contentView;
	private NotificationManager mManager;
	private long length;
	Locale Magyar = new Locale("hu","HU");
	private static final int FINISHED_SUCCESFULLY = 1;
	private static final int JAUDIOTAGGER_PROBLEM = 2;
	private static final int FILE_OR_CONNECTION_PROBLEM = 3;
	private static final int FULL_SDCARD = 4; 
	
	final Handler handler = new Handler() {
		   public void handleMessage(Message msg) {
			 if(msg.what == FINISHED_SUCCESFULLY)
			 { 
				 
				 mManager.cancel(1);
			 }
			 else if(msg.what==JAUDIOTAGGER_PROBLEM) 
			 {
				 Intent notificationIntent = new Intent();
				  PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
				 notifyDetails = new Notification(R.drawable.noitification,"Fájl írási hiba....",System.currentTimeMillis());
				
				 notifyDetails.contentIntent = contentIntent;
				// notifyDetails.setLatestEventInfo(getApplicationContext(), msg.getData().getString("title"), msg.getData().getString("text"), contentIntent);
				 contentView = new RemoteViews(context.getPackageName(), R.layout.error_notification);
				 contentView.setTextViewText(R.id.error_message, "Tag-ek írása nem sikerült...");
				 notifyDetails.contentView = contentView;
				 
				 mManager.notify(2, notifyDetails);
			 }
			 else if(msg.what==FILE_OR_CONNECTION_PROBLEM)
			 {
				 
				 NetworkInfo info = (NetworkInfo) ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

				    if (info == null || !info.isConnected()) {
				    	  
						 Intent notificationIntent = new Intent();
						  PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
						 notifyDetails = new Notification(R.drawable.noitification,"Letöltés megszakadt...",System.currentTimeMillis());
						
						 notifyDetails.contentIntent = contentIntent;
						// notifyDetails.setLatestEventInfo(getApplicationContext(), msg.getData().getString("title"), msg.getData().getString("text"), contentIntent);
						 contentView = new RemoteViews(context.getPackageName(), R.layout.error_notification);
						 contentView.setTextViewText(R.id.error_message, "Kapcsolódási probléma...");
						 notifyDetails.contentView = contentView;
						 
						 mManager.notify(2, notifyDetails);
				    }
				    else
				    {

						 Intent notificationIntent = new Intent();
						  PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
						 notifyDetails = new Notification(R.drawable.noitification,"Letöltés megszakadt...",System.currentTimeMillis());
						
						 notifyDetails.contentIntent = contentIntent;
						// notifyDetails.setLatestEventInfo(getApplicationContext(), msg.getData().getString("title"), msg.getData().getString("text"), contentIntent);
						 contentView = new RemoteViews(context.getPackageName(), R.layout.error_notification);
						 contentView.setTextViewText(R.id.error_message, "A tárhely megtelt");
						 notifyDetails.contentView = contentView;
						 
						 mManager.notify(2, notifyDetails);
				    }
				    
				    
			 }
			 
			 else if(msg.what==FULL_SDCARD)
			 {
				 
						 Intent notificationIntent = new Intent();
						  PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
						 notifyDetails = new Notification(R.drawable.noitification,"Letöltés megszakadt...",System.currentTimeMillis());
						
						 notifyDetails.contentIntent = contentIntent;
						// notifyDetails.setLatestEventInfo(getApplicationContext(), msg.getData().getString("title"), msg.getData().getString("text"), contentIntent);
						 contentView = new RemoteViews(context.getPackageName(), R.layout.error_notification);
						 contentView.setTextViewText(R.id.error_message, "A tárhely megtelt");
						 notifyDetails.contentView = contentView;
						 
						 mManager.notify(2, notifyDetails);
				    
			 }
			
			 else
			 {

				 if(msg.getData().getInt("percent")!=0)
				 {
					 contentView.setTextViewText(R.id.percent, msg.getData().getInt("percent")+"%");
					 contentView.setProgressBar(R.id.progressBar1, 100, msg.getData().getInt("percent"), false);
					 mManager.notify(1, notifyDetails);
				 }
				 else
				 {
					 
				  
				 Intent notificationIntent = new Intent();
				  PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
				 notifyDetails = new Notification(R.drawable.noitification,msg.getData().getString("title"),System.currentTimeMillis());
				
				 notifyDetails.contentIntent = contentIntent;
				// notifyDetails.setLatestEventInfo(getApplicationContext(), msg.getData().getString("title"), msg.getData().getString("text"), contentIntent);
				  
				 contentView = new RemoteViews(context.getPackageName(), R.layout.notification);
				 contentView.setTextViewText(R.id.title, msg.getData().getString("title"));
				 contentView.setTextViewText(R.id.percent, "0%");
				 contentView.setProgressBar(R.id.progressBar1, 100, 0, false);
				 contentView.setTextViewText(R.id.text, msg.getData().getString("text"));
				 notifyDetails.contentView = contentView;
				 
				 mManager.notify(1, notifyDetails);
				 }
				
			
				 
			 }
			 
			 }
		   };
	
	public Downloader(Context c,Day day)
	{
		this.day = day;
		this.context = c;
		mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	public Downloader(Context c, Musor musor)
	{
		this.musor = musor;
		this.context = c;
		mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	

	public void run() {
		if(musor==null && day!=null) 
			{
			DayDownload();
			 
			}
		else 
			{
			MusorDownload(this.musor);
			}
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse
	    		("file://"
	    		+ Environment.getExternalStorageDirectory())));

	}
	
	@SuppressWarnings("static-access")
	private void MusorDownload(Musor m)
	{
		
		
		FileOutputStream f;
   	 
		Bundle b = new Bundle();
	    b.putString("title", (m.getId())+"/"+m.getFrom()+". letöltése...");
	    b.putString("text",m.getTime()+" "+m.getTitle());
	    Message message = new Message();
	    message.setData(b);
		handler.sendMessage(message);
		URL u;
		try {
			u = new URL(m.getMP3());
		
		
		
    HttpURLConnection c;

		c = (HttpURLConnection) u.openConnection();
	
 
		c.setRequestMethod("GET");

    c.setDoOutput(true);
   
		c.connect();
	
    
    length = c.getContentLength();
    
    if(!(new File(m.getDirectory()).exists()))
	 {
		 new File(m.getDirectory()).mkdirs();
	 }
    
    
    
    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
    long available = stat.getAvailableBlocks();
    long blocksize = stat.getBlockSize();
    long available_bytes=available*blocksize;
    if(available_bytes<length)
    {
    	IOException baj = new IOException("SDCARD_FULL");
    	throw baj;
    	
    }
   
   
    	
    File mp3 = new File(m.getDirectory(),m.getDate()+"_"+(m.getId())+".mp3");
		
			f = new FileOutputStream(mp3);
		
		
	    InputStream in;
	
			in = c.getInputStream();
		

	    byte[] buffer = new byte[1024];
	    int len1 = 0;
	   int step = 1;
	    int progressbytes=0;
	   
	 
			while ( (len1 = in.read(buffer)) > 0 ) {
				
			     f.write(buffer,0, len1);
			     progressbytes+=len1;
			     float perc = ((float)progressbytes/(float)length);
			     if(perc >= step*0.1)
			     {
			    	 step++;
			    	 Bundle progress = new Bundle();
					    progress.putInt("percent",(int) (perc*100));
					   
					    Message pm = new Message();
					    pm.setData(progress);
						handler.sendMessage(pm);
						
			     }
			   
}
		
	    
			f.close();
		
	   /*
		String DayDirectory = Environment.getExternalStorageDirectory()+"/bumerang/"+DateUrl;
		
		if(!(new File(DayDirectory).exists()))
    	 {
    		 new File(DayDirectory).mkdirs();
    	 }
		String url = "http://bumerang.hu/letolt.php?ev="+DateUrl.substring(0, 4)+"&ho="+Integer.valueOf(DateUrl.substring(4, 6))+"&nap="+Integer.valueOf(DateUrl.substring(6, 8))+"&sorszam="+(i+1);
		
		 File mp3 = new File(DayDirectory,DateUrl+"_"+(i+1)+".mp3");
		new DefaultHttpClient().execute(new HttpGet(url))
        .getEntity().writeTo(
                new FileOutputStream(mp3));*/
			
			TagOptionSingleton.getInstance().setAndroid(true);
	    AudioFile af;
		
			af = AudioFileIO.read(mp3);
		
	    Tag tag = af.getTagOrCreateAndSetDefault();
	
	    
	    Artwork a = new Artwork();
	    
	    
	    AssetFileDescriptor file = context.getAssets().openFd("bumcov.jpg");
	    
	    InputStream is= file.createInputStream();
	   
            // We guarantee that the available method returns the total
            // size of the asset...  of course, this does mean that a single
            // asset can't be more than 2 gigs.
            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] image_buffer = new byte[size];
            is.read(image_buffer);
            is.close();
          
         //   a.setImageUrl("http://www.vivatv.hu/gsp/images/bumerang_logo_supyo_293x195.jpg");
            String mimetype = ImageFormats.getMimeTypeForBinarySignature(image_buffer);
     	   a.setMimeType(mimetype);
	   a.setBinaryData(image_buffer);
	  
	  
	    
	   
	    tag.addField(a);
	    
	    
	    TagOptionSingleton.getInstance().setAndroid(true);
	    
			tag.setField(FieldKey.ARTIST,"Bumeráng");
		
	    tag.setField(FieldKey.TITLE, m.getTime()+" "+m.getTitle());
	    tag.setField(FieldKey.URL_OFFICIAL_ARTIST_SITE, "http://www.bumerang.hu");
	    tag.setField(FieldKey.ALBUM,new SimpleDateFormat("yyyyMMdd",Magyar).parse(m.getDate()).toLocaleString().substring(0,11));
	    tag.setField(FieldKey.GENRE, "57");
	    tag.setField(FieldKey.YEAR, m.getDate().substring(0, 4));
	    tag.setField(FieldKey.TRACK, String.valueOf(m.getId()));
	    tag.setField(FieldKey.TRACK_TOTAL,String.valueOf(m.getFrom()));
	    				   
	    af.commit();
	    } catch (KeyNotFoundException e) {
			delete(m);
			handler.sendEmptyMessage(this.JAUDIOTAGGER_PROBLEM);
			
		} catch (FieldDataInvalidException e) {
			delete(m);
			handler.sendEmptyMessage(this.JAUDIOTAGGER_PROBLEM);
		} catch (CannotWriteException e) {
			delete(m);
			handler.sendEmptyMessage(this.JAUDIOTAGGER_PROBLEM);
		} catch (ParseException e) {
			delete(m);
			handler.sendEmptyMessage(this.JAUDIOTAGGER_PROBLEM);
		} catch (IOException e) {
			delete(m);
			if(e.getMessage().equals("SDCARD_FULL"))
			handler.sendEmptyMessage(this.FULL_SDCARD);
			else
			handler.sendEmptyMessage(this.FILE_OR_CONNECTION_PROBLEM);
		} catch (CannotReadException e) {
			delete(m);
			handler.sendEmptyMessage(this.JAUDIOTAGGER_PROBLEM);
		} catch (TagException e) {
			delete(m);
			handler.sendEmptyMessage(this.JAUDIOTAGGER_PROBLEM);
		} catch (ReadOnlyFileException e) {
			delete(m);
			handler.sendEmptyMessage(this.JAUDIOTAGGER_PROBLEM);
		} catch (InvalidAudioFrameException e) {
			delete(m);
			handler.sendEmptyMessage(this.JAUDIOTAGGER_PROBLEM);
		}
		finally
		{
	   
	    handler.sendEmptyMessage(this.FINISHED_SUCCESFULLY); 
		}
		}
		
	
	
	private void delete(Musor m) {
		new File(m.getDirectory(),m.getDate()+"_"+(m.getId())+".mp3").delete();
		
	}

	private void DayDownload()
	{
		for(Musor m:day.getMusorok())
		{
			 if(!(new File(m.getDirectory(),m.getDate()+"_"+m.getId()+".mp3").exists()))
			 {
			MusorDownload(m);
			 }
		}
		
	}
	
	public synchronized void appendLog(String text)
	{       
	   File logFile = new File("sdcard/log.txt");
	   if (!logFile.exists())
	   {
	      try
	      {
	         logFile.createNewFile();
	      } 
	      catch (IOException e)
	      {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	   }
	   try
	   {
	      //BufferedWriter for performance, true to set append to file flag
	      BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
	      buf.append(text);
	      buf.newLine();
	      buf.close();
	   }
	   catch (IOException e)
	   {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	   }
	}

}
