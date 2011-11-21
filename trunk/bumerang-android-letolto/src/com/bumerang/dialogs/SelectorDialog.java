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
