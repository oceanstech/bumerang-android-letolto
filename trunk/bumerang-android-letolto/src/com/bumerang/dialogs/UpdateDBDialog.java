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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import com.bumerang.R;
import com.bumerang.model.FileManager;
import com.bumerang.model.Letoltesek;
import com.bumerang.util.db.DOWNLOADSContentProvider;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class UpdateDBDialog extends Dialog  {

	private Context context;
	private Locale Magyar = new Locale("hu","HU");
	 final Handler handler;

	public UpdateDBDialog(Context context) {
		super(context,android.R.style.Theme_Dialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		 this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  
		 this.setContentView(R.layout.upd_dialog);
		 final TextView upd_stat =(TextView) this.findViewById(R.id.upd_stat);
		 new Letoltesek(context);
		 handler = new Handler() {
			 public void handleMessage(Message msg) {
				 if(msg.what == 0)
				 { 
					 
					 finish();
					
				 }
				 else
				 {
					
					 upd_stat.setText( msg.getData().getString("ratio"));
				 }
			 }
		};
		
		new Thread(){

			@Override
			public void run() {
				 StartProcessing();
				
			}}.start();
		
	}
	
	public void finish()
	{
		this.dismiss();
	}

	private void StartProcessing() {
  		ArrayList<String> files_in_the_db = Letoltesek.filesQuery();
		ArrayList<String> files_in_the_flashdrive = FileManager.getInstance().getAllFilePath(null);
		Uri u = Uri.parse("content://com.bumerang.util.downloadscontentprovider/downloads");
		int i=0;
		for(String filepath : files_in_the_flashdrive)
		{
			i++;
			Bundle b = new Bundle();
		    b.putString("ratio", String.valueOf(i)+"/"+String.valueOf(files_in_the_flashdrive.size()));
		  
			 Message message = new Message();
			 message.setData(b);
			 message.what=1;
			handler.sendMessage(message);
			if(!files_in_the_db.contains(filepath))
			{
				try {
					File f = new File(filepath);
					AudioFile af = AudioFileIO.read(f);
					Tag tag = af.getTag();
					if(tag!= null && !tag.isEmpty())
					{
					
					ContentValues initialValues = new ContentValues();
			        initialValues.put(DOWNLOADSContentProvider.TITLE,tag.getFirst(FieldKey.TITLE));
			        Date album_date = new SimpleDateFormat("yyyy.MM.dd",Magyar).parse(tag.getFirst(FieldKey.ALBUM));
			        initialValues.put(DOWNLOADSContentProvider.DATE, album_date.getTime());
			        initialValues.put(DOWNLOADSContentProvider.POSITION, tag.getFirst(FieldKey.TRACK));
			        initialValues.put(DOWNLOADSContentProvider.SIZE, f.length());
			        initialValues.put(DOWNLOADSContentProvider.FILENAME, filepath);
			        initialValues.put(DOWNLOADSContentProvider.DL_ID,tag.getFirst(FieldKey.ALBUM).replace(".", "").concat(tag.getFirst(FieldKey.TRACK)));
					context.getContentResolver().insert(u, initialValues);
					
					}
					else 
						new File(filepath).delete();
				} catch (CannotReadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TagException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ReadOnlyFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidAudioFrameException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (KeyNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		handler.sendEmptyMessage(0);
		
		
	}

}
