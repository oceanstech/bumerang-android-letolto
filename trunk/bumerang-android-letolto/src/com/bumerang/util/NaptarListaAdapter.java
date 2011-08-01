package com.bumerang.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.bumerang.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class NaptarListaAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<Calendar> calendar;

    
	Locale Magyar = new Locale("hu","HU");
	private SimpleDateFormat month_day_format;
	private SimpleDateFormat weekday;
	
    public NaptarListaAdapter(Context context,ArrayList<Calendar> calendar) 
    {
        this.context = context;
        this.calendar = calendar;
        
     month_day_format = new SimpleDateFormat("MMMM d.",Magyar);
     weekday = new SimpleDateFormat("EEEE",Magyar);
    }

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}


	public int getCount() {
		// TODO Auto-generated method stub
		return calendar.size();
	}


	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}


	public View getView(int position, View convertView, ViewGroup arg2) {
		
		ViewHolder holder = null;
		if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.calendar_elem, null);
            holder = new ViewHolder();
            holder.day_of_month = (TextView)convertView.findViewById(R.id.nap);
            holder.day_of_week = (TextView)convertView.findViewById(R.id.hetnapja);
            convertView.setTag(holder);
        }
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.day_of_month.setText(month_day_format.format(calendar.get(position).getTime()));
		holder.day_of_week.setText(weekday.format(calendar.get(position).getTime()));
		
		return convertView;
	}

	 static class ViewHolder
	 {

		public TextView day_of_week;
		public TextView day_of_month;
		 
	 }
	
	
	 
}
