package com.example.thothv2.thothcontacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver{
	public static final String ANNIVERSARY_CHECK = "android.intent.action.ANNIVERSARY_CHECK";
	private static final String TAG = "MY_RECEIVER";
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equals(ANNIVERSARY_CHECK)){
			Log.d(TAG, "Custom Notification!");
		}
		
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
			Log.d(TAG, "Boot Notification!");
		}
		
		if(intent.getAction().equals(Intent.ACTION_REBOOT)){
			Log.d(TAG, "Reboot Notification!");
		}
		
		if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
			Log.d(TAG, "Idle Notification!");
		}
	}

}
