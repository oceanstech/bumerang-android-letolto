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
package com.bumerang.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bumerang.util.NCRDecoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Musor{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String time;
	private String title_text;
	private String imgurl;
	private Bitmap image;
	private String tartalom;
	private ImageServer is;
	private ArrayList<String> videos;
	private HashMap<String,Bitmap> video_images;
	private String MP3;
	private String date;
	private int from;

	public Musor(String title)
	{
		this.time=title.substring(0,6);
		this.title_text = title.substring(6);
		videos = new ArrayList<String>();
		is = ImageServer.getInstance();
		
	}

	public void setID(int id)
	{
		this.id=id;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setTartalom(String tartalom) {
		
		

   	 Pattern p2 = Pattern.compile("http://www.youtube.com/embed/(.+?){11}");
    	Matcher m2 = p2.matcher(tartalom);
    	
    	Pattern p3 = Pattern.compile("http://www.youtube.com/v/(.+?){11}");
    	Matcher m3 = p3.matcher(tartalom);
        
    	String ytlink;
    	
        while(m2.find())
        {
        	ytlink = m2.toMatchResult().group();
        	ytlink = ytlink.replace("embed/", "watch?v=");
        	
        	videos.add(ytlink);
        	
        	
        }
        while(m3.find())
        {
        	ytlink = m3.toMatchResult().group();
        	
        	ytlink = ytlink.replace("v/", "watch?v=");
        	
        	videos.add(ytlink);
        	
        	if(videos.size()>1 && videos.get(videos.size()-2).compareTo(ytlink)==0)
        	{
        		videos.remove(videos.size()-1);
        	}
        	
        	
        }
        
       
        this.tartalom = collapseNewlines(tartalom);
		
	}

	public void setImageUrl(String imgurl) {
		
	this.imgurl=imgurl;
	
	image = is.getImage(imgurl);
		
	}
	
	public Bitmap getImage()
	{
		if(image==null)
		{
			image= is.getImage(imgurl);
			return image;
		}
		else
		{
			return image;
		}
		
	}
	
	public boolean hasVideos()
	{
		if(videos.size()>0) return true;
		return false;
	}
	
	public ArrayList<String> getVideos()
	{
		if(videos!= null)return videos;
		return null;
	}

	public String getTartalom() {	
		
		return NCRDecoder.decode(tartalom.replaceAll("\\<.*?\\>", ""));
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}


	public static String collapseNewlines(String argStr)
	  {
	      char last = argStr.charAt(0);
	      StringBuffer argBuf = new StringBuffer();

	      for (int cIdx = 0 ; cIdx < argStr.length(); cIdx++)
	      {
	          char ch = argStr.charAt(cIdx);
	          if (ch != '\n' || last != '\n')
	          {
	              argBuf.append(ch);
	              
	              last = ch;
	          }
	          
	          else if(cIdx+1 < argStr.length() && argStr.charAt(cIdx+1)!='\n') argBuf.append('\n');
	      }

	      return argBuf.toString();
	  }
	
	private Bitmap getImageBitmap(String url) { 
        Bitmap bm = null; 
        try { 
            URL aURL = new URL(url); 
            URLConnection conn = aURL.openConnection(); 
            conn.connect(); 
            InputStream is = conn.getInputStream(); 
            BufferedInputStream bis = new BufferedInputStream(is); 
            bm = BitmapFactory.decodeStream(bis); 
            bis.close(); 
            is.close(); 
       } catch (IOException e) { 
           Log.e("TAG", "Error getting bitmap", e); 
       } 
       return bm; 
    }

	public String getTime() {
		// TODO Auto-generated method stub
		return time;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return title_text;
	}

	public HashMap<String,Bitmap> getVideoPreviews() {
		if(video_images==null)
		{
			video_images = new HashMap<String,Bitmap>();
		
			for(String link : videos)
			{
				Bitmap kep  = getImageBitmap("http://img.youtube.com/vi/"+link.split("=")[1]+"/1.jpg");
				video_images.put(link, kep);
				
			}
			
			return video_images;
		}
		else
			return video_images;
	}

	public void addMP3Link(String link) {
		MP3=link;
		
	}
	
	public String getMP3()
	{
		return MP3;
	}

	public void addDate(String date) {
		this.date = date;
		
	}
	
	public String getDate()
	{
		return date;
	}
	
	public String getDirectory()
	{
		return FileManager.getInstance().directory+this.getDate();
	}

	public void setFrom(int size) {
		this.from = size;
		
	}
	
	public int getFrom()
	{
		return from;
	}

}
