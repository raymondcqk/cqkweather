package com.raymondqck.www.rayweather.receiver;

//import com.coolweather.app.service.AutoUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.raymondqck.www.rayweather.service.AutoUpdateService;

public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, AutoUpdateService.class);
		context.startService(i);
	}

}
