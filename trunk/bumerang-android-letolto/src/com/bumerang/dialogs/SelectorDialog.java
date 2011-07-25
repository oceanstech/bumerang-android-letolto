package com.bumerang.dialogs;


import java.util.ArrayList;

import com.bumerang.R;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SelectorDialog extends Dialog {

	
	private  LinearLayout l;


	public SelectorDialog(Context context,String title,ArrayList<Button> buttons) {
		super(context,R.style.CustomDialogTheme);
		
		
		 requestWindowFeature(Window.FEATURE_NO_TITLE);  
		 this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  
		 
		 
		 setContentView(R.layout.selectorbox);
		  TextView tw = (TextView)this.findViewById(R.id.textView1);
		  tw.setText(title);
		  
		  l = (LinearLayout)this.findViewById(R.id.videoscroll_container);
		  l.removeAllViews();
		  for(Button button:buttons)
		  {
			  l.addView(button);
		  }
		  
		 
	}

	
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.dismiss();
	}




	



	
	 
	 



	
	
}
