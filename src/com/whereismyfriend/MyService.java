package com.whereismyfriend;


import com.commonsware.cwac.locpoll.LocationPoller;
import com.commonsware.cwac.locpoll.LocationPollerParameter;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

public class MyService extends IntentService
{
	
	private static final int PERIOD=30000; 	// 30 seconnnnns papaaa
	private PendingIntent pi=null;
	private AlarmManager mgr=null;
	
	
  public MyService()
  {
    super(MyService.class.getSimpleName());
  }
  

  @Override
  protected void onHandleIntent(Intent intent)
  {	  
	mgr=(AlarmManager)getSystemService(ALARM_SERVICE);
		
		Intent i=new Intent(this, LocationPoller.class);
		
		Bundle bundle = new Bundle();
		LocationPollerParameter parameter = new LocationPollerParameter(bundle);
		parameter.setIntentToBroadcastOnCompletion(new Intent(this, LocationReceiver.class));
	
		 final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
		 if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ))
			 parameter.setProviders(new String[]{LocationManager.GPS_PROVIDER});
		 else
			 parameter.setProviders(new String[]{LocationManager.NETWORK_PROVIDER});
		
		// try GPS and fall back to NETWORK_PROVIDER
		//parameter.setProviders(new String[] {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER});
		parameter.setTimeout(25000);
		i.putExtras(bundle);
		
		pi=PendingIntent.getBroadcast(this, 0, i, 0);
		mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
											SystemClock.elapsedRealtime(),
											PERIOD,
											pi);
		
  }

 

 
}
