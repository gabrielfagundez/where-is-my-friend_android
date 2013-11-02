package com.whereismyfriend;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class LocationBroadcastReceiver extends BroadcastReceiver {
	String latitude;
	String longitude;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LocationBroadcastReceiver", "onReceive: received location update");
        
        final LocationInfo locationInfo = (LocationInfo) intent.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
        
    	//OBTENER EL MAIL DE ALGUN SHARED PREFERENCES
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("prefs",Context.MODE_PRIVATE);
        String mail=pref.getString("user_mail","");
        if (mail.compareTo("")!=0){
	    	latitude=Float.toString(locationInfo.lastLat);
	    	longitude=Float.toString(locationInfo.lastLong);
	    	//Hago la llamada al server
	    	String [] parametros = {mail,latitude,longitude};
	    	new consumidorPost().execute(parametros);
        }
    }
    
  //METODOS LLAMADOS PARA SETEAR LA POSICION EN EL SERVER
    private class consumidorPost extends AsyncTask<String[], Void, String[]>{
		protected String[] doInBackground(String[]... arg0) {
			// TODO Auto-generated method stub
			WSSetLocation wssetlocation= new WSSetLocation();
			String[] res= wssetlocation.llamarServer(arg0[0][0], arg0[0][1],arg0[0][2]);
			return res;
		}
		
		@Override
		protected void onPostExecute(String[] result){
            super.onPostExecute(result);
            int codigo_res = Integer.parseInt(result[0]);
			//Si quiero hacer algo con el resultado es acá
		}
    }
}