package com.bumerang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Months {

	String url = "http://bumerang.hu/?menu=2";
	static final String REGEXP = "<td width=\"25\" height=\"25\" background=\"kepek/vanbejegyzes\\.jpg\"><div align=\"right\"><span class=\"style130\"><div align=\"center\"><a href=\".{16}";
	String html = "";
	ArrayList<ArrayList<Calendar>> Month;
	int actual =-1;
	Locale Magyar = new Locale("hu","HU");
	private SimpleDateFormat URLDateFormat;
	
	public Months() throws ClientProtocolException, IOException
	{
		URLDateFormat = new SimpleDateFormat("yyyyMMdd",Magyar);
		Month = new ArrayList<ArrayList<Calendar>>();
        GetHtmlQuery(url);
        parseCalendar();
	}
	
	private void parseCalendar()
	{
		
		
		ArrayList<Calendar> l = new ArrayList<Calendar>();
        Pattern p = Pattern.compile(REGEXP);
        Matcher m = p.matcher(html);
        while(m.find())
        {
        	String temp = m.toMatchResult().group();
        	
        	Calendar t = Calendar.getInstance();
        	try {
				t.setTime(URLDateFormat.parse(temp.substring(temp.length()-8, temp.length())));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	l.add(t);
        	
        } 
        
      Month.add(l);   	
	}
	
	private void GetHtmlQuery(String url) throws ClientProtocolException, IOException
	{
		 HttpClient client = new DefaultHttpClient();
	        HttpGet request = new HttpGet(url);
	        HttpResponse response = null;
	        
			
				response = client.execute(request);
			

	       
	        InputStream in = null;
			
				in = response.getEntity().getContent();
			
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        StringBuilder str = new StringBuilder();
	        String line = null;
	        
				while((line = reader.readLine()) != null)
				{
				    str.append(line);
				}
				in.close();
			 
	        html = str.toString();
	}
	
	public ArrayList<Calendar> getPrev() throws ClientProtocolException, IOException
	{
		
		actual++;
		if(Month.size()>=actual+1) return Month.get(actual);
						
		if(Month.size()>actual-1)
		{
		Calendar prev_first = (Calendar) Month.get(actual-1).get(0).clone();
		prev_first.add(Calendar.MONTH, -1);
		prev_first.set(Calendar.DAY_OF_MONTH, 1);
		
		String Date =  URLDateFormat.format(prev_first.getTime());
		
		url="http://www.bumerang.hu/?ezaz=2_"+Date;
		 GetHtmlQuery(url);
	        parseCalendar();
	        		
			return Month.get(actual);
		}		
		
		return Month.get(actual-=2);
	}

	public ArrayList<Calendar> getNext() {
		if(actual>0)
		{
			actual--;
			return Month.get(actual);
		}
		else
		{
			if(Month.size()>0) return Month.get(0);
			return null;
		}
		
	}

	public void setDay(String dateUrl) {
		// TODO Auto-generated method stub
		
	}
	
		

}

	

