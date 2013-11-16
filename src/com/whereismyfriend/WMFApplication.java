package com.whereismyfriend;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

import android.app.Application;
import android.util.Log;

public class WMFApplication extends Application {
   
	public static int app_is_visible=0;

    @Override
    public void onCreate() {
        super.onCreate();
        
        Log.d("TestApplication", "onCreate()");

        // output debug to LogCat, with tag LittleFluffyLocationLibrary
        LocationLibrary.showDebugOutput(true);

        try {
            // in most cases the following initialising code using defaults is probably sufficient:
            //
            // LocationLibrary.initialiseLibrary(getBaseContext(), "com.your.package.name");
            //
            // however for the purposes of the test app, we will request unrealistically frequent location broadcasts
            // every 1 minute, and force a location update if there hasn't been one for 2 minutes.
        	//Lo cambie a 15 y 30 seg
            LocationLibrary.initialiseLibrary(getBaseContext(), 15 * 1000, 30 * 1000, "com.whereismyfriend");
            LocationLibrary.startAlarmAndListener(getBaseContext());
        }
        catch (UnsupportedOperationException ex) {
            Log.d("TestApplication", "UnsupportedOperationException thrown - the device doesn't have any location providers");
        }
    }
}
