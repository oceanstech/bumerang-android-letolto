package com.bumerang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Day {
	
	static final String url = "http://bumerang.hu/?menu=2";
	private String html = "";
	private String date;
	
	private ArrayList<String> mp3list;
	private ArrayList<String> im;
	private ArrayList<String> l;
	private ArrayList<String> t;
	private ArrayList<Musor> musorok;
	
	static final  String BaseUrl = "http://bumerang.hu/?ezaz=2_";
	static final  String REGEXP_MUSOR = "<p align=\"justify\">(.+?)</p>";
	static final String REGEXP_MP3 = "http://s.bumerang.hu(.+?).mp3";
	static final String REGEXP_IMAGE = "<img src=\"hirkep/kicsi/(.+?)\" width=\"102\" height=\"252\" align=\"left\" />";
	
	public Day(String date) throws ClientProtocolException, IOException
	{
		this.date = date;
		musorok = new ArrayList<Musor>();
		MusorHtmlQuery(BaseUrl+date);
		parse();
	}
	

	private void MusorHtmlQuery(String url) throws ClientProtocolException, IOException
	{
		 HttpClient client = new DefaultHttpClient();
	        HttpGet request = new HttpGet(url);
	        HttpResponse response = null;
	        
			
				response = client.execute(request);
			

	       
	        InputStream in = null;
			
				in = response.getEntity().getContent();
			
			
		      Charset charset = Charset.forName("Cp1250");
		        
			
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in,charset));
	        StringBuilder str = new StringBuilder();
	        String line = null;
	       
				while((line = reader.readLine()) != null)
				{
				    str.append(line);
				}
			
				in.close();
			
	        html = str.toString();
	        
	}
	
	
	private void parse()
	{
		
		
		l = new ArrayList<String>();
		t = new ArrayList<String>();
		im = new ArrayList<String>();
		
		
		mp3list = new ArrayList<String>();
		String musor;
		String tartalom;
        Pattern p = Pattern.compile(REGEXP_MUSOR);
        Matcher m = p.matcher(html);
        
        while(m.find())
        {
        	musor = NCRDecoder.decode(m.toMatchResult().group());
        	musor = musor.trim();
        	musor = musor.replaceAll("( )+", " ");
        	musor = musor.replace("\t", "");
        	musor = musor.replace("<p align=\"justify\">", "");
        	musor = musor.replace("<font color=\"#fca308\">", "");
        	musor = musor.replace("</font></p>", "");
        	l.add(musor);
        	m.find();
        	
        	tartalom = m.toMatchResult().group();
        	tartalom = tartalom.trim();
        	tartalom = tartalom.replace("<p align=\"justify\">", "");
        	tartalom = tartalom.replace("<br>", "\n");
        	tartalom = tartalom.replace("<a>", "");
        	tartalom = tartalom.replace("</a>", "");
        	tartalom = tartalom.replace("</p>", "");
        	
             t.add(tartalom);
        }  
        
    	p = Pattern.compile(REGEXP_IMAGE);
    	m = p.matcher(html);
        
    	String Imagelink;
    	
        while(m.find())
        {
        	Imagelink = m.toMatchResult().group();
        	Imagelink = Imagelink.replace("<img src=\"", "");
        	Imagelink = Imagelink.replace("\" width=\"102\" height=\"252\" align=\"left\" />", "");
        	Imagelink = "http://www.bumerang.hu/"+Imagelink;
        	im.add(Imagelink);
        	
        }
        p = Pattern.compile(REGEXP_MP3);
    	m = p.matcher(html);
        
    	String mp3link;
    	
        while(m.find())
        {
        	mp3link = m.toMatchResult().group();
        	
        	mp3list.add(mp3link);      
        }
        
        if(mp3list.size() == 2*t.size())
        {
        	ArrayList<String> temp = new ArrayList<String>();
        	for(int i =0;i<mp3list.size();i+=2)
        	{
        		temp.add(mp3list.get(i));
        	}
        	mp3list=temp;
        }
        
        for(int i=0;i<l.size();i++)
		{
        Musor Temp = new Musor(l.get(i));
        Temp.setID(i+1);
        Temp.setFrom(l.size());
		Temp.setTartalom(t.get(i));
		Temp.setImageUrl(im.get(i));
		if((mp3list.size()-1)>=i){
		Temp.addMP3Link(mp3list.get(i));
		}
		Temp.addDate(date);
		musorok.add(Temp);
		}
        
	}


	public ArrayList<Musor> getMusorok() {
		
		return musorok;
	}


	public int size() {
		// TODO Auto-generated method stub
		return musorok.size();
	}


	public ArrayList<String> getMP3Links() {
		// TODO Auto-generated method stub
		return this.mp3list;
	}


	public String[] getListElements() {
		// TODO Auto-generated method stub
		String[] temp = new String[l.size()];
		l.toArray(temp);
		return temp;
	}
	
	public String getDirectory()
	{
		return FileManager.getInstance().directory+"/"+this.date;
	}

}
