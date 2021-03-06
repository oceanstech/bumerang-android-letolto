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
package com.bumerang;

import java.io.IOException;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.telephony.TelephonyManager;

public class StreamMusicService extends Service {
	
	private MediaPlayer player;
	private OnInfoListener onInfo;
	private OnBufferingUpdateListener onBuffer;
	private OnPreparedListener onPrepared;
	private OnErrorListener onError;
	private Notification notifyDetails;
	private RemoteViews contentView;
	private NotificationManager mManager;
	private BroadcastReceiver   HeadPhoneButtonReciver;
	private int actual_station=0;
	private int broadcastcall = 0;
	
	private String[] channels = {	"http://neofmstream.gtk.hu:8080",
									"http://neofmstream2.gtk.hu:8080",
									"http://neofmstream3.gtk.hu:8080",
									"http://www.sztarnet.hu:9214"};
	
	public class  MusicServiceBinder extends Binder
	{
		public StreamMusicService getservice()
		{
			return StreamMusicService.this;
		}
	}
	
	private final IBinder mBinder = new MusicServiceBinder();
	
	@Override
	public void onStart(Intent intent, int startId) {
		
		super.onStart(intent, startId);
		
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initialize_handlers();
		player = new MediaPlayer();
		player.setOnInfoListener(onInfo);
		player.setOnBufferingUpdateListener(onBuffer);
		player.setOnPreparedListener(onPrepared);
		player.setOnErrorListener(onError);
		
		mManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		
		 notifyDetails = new Notification(R.drawable.noitification,"Neo FM - Online",System.currentTimeMillis());
		
		notifyDetails.flags |= Notification.FLAG_NO_CLEAR;
		// notifyDetails.setLatestEventInfo(getApplicationContext(), msg.getData().getString("title"), msg.getData().getString("text"), contentIntent);
		 contentView = new RemoteViews(this.getPackageName(), R.layout.playernotification_controller);
		 Intent startMplayer = new Intent(this,Main.class);
		 startMplayer.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		 contentView.setTextViewText(R.id.Title, "NeoFM - WebR�di�");
		 contentView.setTextViewText(R.id.Status, "Bufferel�s");
		 
		PendingIntent pendingIntent = PendingIntent.getActivity(this,
				    0 , startMplayer  , 0);
		 notifyDetails.contentIntent = pendingIntent;
		 
		 notifyDetails.contentView = contentView;
		 
		 this.HeadPhoneButtonReciver = new BroadcastReceiver(){

				@Override
				public void onReceive(Context arg0, Intent arg1) {
					 TelephonyManager tm = (TelephonyManager) arg0.getSystemService(Context.TELEPHONY_SERVICE);
		                if (tm.getCallState() == TelephonyManager.CALL_STATE_IDLE) {
					if(broadcastcall==0)
					{
					if(player.isPlaying()) inner_stop();
					else play();
					broadcastcall++;
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
					else
					{
						broadcastcall=0;
					}
					
					
					abortBroadcast();
		                }
		                else
		                {
		                	if(player.isPlaying()) inner_stop();
		                }
		                
				}};
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try
		{this.unregisterReciver();
		
		}
		catch(Exception e)
		{e.printStackTrace();};
		if(player!=null&& player.isPlaying()) player.stop();
		mManager.cancel(50);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	private void initialize_handlers() {
		onInfo = new OnInfoListener(){

			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				return false;
			}};
			
		onBuffer = new OnBufferingUpdateListener(){

			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				if(!player.isPlaying())
				{contentView.setTextViewText(R.id.Status, "Bufferel�s");
				mManager.notify(50, notifyDetails);}
				
			}};
			
		onPrepared = new MediaPlayer.OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				
				contentView.setTextViewText(R.id.Status, "Online");
				mManager.notify(50, notifyDetails);
				mp.start();
				registerReciver();
				
			}
		};
		
		onError = new MediaPlayer.OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				
				contentView.setTextViewText(R.id.Status, "Lej�tsz�si hiba!");
				mManager.notify(50, notifyDetails);
				if(actual_station<channels.length-1){
					actual_station++;
					play();
				}
				else actual_station = 0;
				return false;
			}
		};
		
	}


	public boolean isplaying()
	{
		return player.isPlaying();
	}

	public void inner_stop()
	{
		if(player.isPlaying()) player.stop();
		mManager.cancel(50);
		player.reset();
		
		
	}
	
	public void stop()
	{
		if(player.isPlaying()) player.stop();
		mManager.cancel(50);
		player.reset();
		this.unregisterReciver();
		
	}
	
	public void play() {
		try {
			player.reset();
			player.setDataSource(channels[actual_station]);
			mManager.notify(50, notifyDetails);
			player.prepareAsync();
			
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void registerReciver()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_BUTTON);
		
		filter.setPriority(2000000);
		
		this.registerReceiver(HeadPhoneButtonReciver, filter);
		
	}
	
	private void unregisterReciver()
	{
		
	this.unregisterReceiver(HeadPhoneButtonReciver);
	}

	public void dismiss() {
		
		mManager.cancel(50);
		
	}

	public void emiss() {
		if(player!=null && player.isPlaying())
		{
			mManager.notify(50, notifyDetails);
		}		
		else
		{
			this.stopSelf();
		}
		
	}
	
}
