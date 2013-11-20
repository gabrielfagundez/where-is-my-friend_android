/***
	Copyright (c) 2010 CommonsWare, LLC
	
	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */

package com.whereismyfriend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.commonsware.cwac.locpoll.LocationPollerResult;

public class LocationReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
	  
      
      Bundle b=intent.getExtras();
      
      LocationPollerResult locationResult = new LocationPollerResult(b);
      
      Location loc=locationResult.getLocation();

      if (loc==null) {
	      //NO PUDE OBTENER LA POSICION
    	  Log.i("RESULTADO", "Error no pude obtener posicion");
      }
      else {
			//OBTENER EL MAIL DE ALGUN SHARED PREFERENCES
			  SharedPreferences pref = context.getApplicationContext().getSharedPreferences("prefs",Context.MODE_PRIVATE);
			  String mail=pref.getString("user_mail","");
			  String name=pref.getString("user_name", "");
			  if (mail.compareTo("")!=0){
				  String lat= Double.toString(loc.getLatitude());
	       		  String lon= Double.toString(loc.getLongitude());
				  Log.i("RESULTADO", lat+" "+lon);
				  //Hago la llamada al server
		          String [] parametros = {mail,name,lat,lon};
		          new consumidorPost().execute(parametros);
				  }
			  }
    }

  

//METODOS LLAMADOS PARA SETEAR LA POSICION EN EL SERVER
private class consumidorPost extends AsyncTask<String[], Void, String[]>{
          protected String[] doInBackground(String[]... arg0) {
                  // TODO Auto-generated method stub
                  WSSetLocation wssetlocation= new WSSetLocation();
                  String[] res= wssetlocation.llamarServer(arg0[0][0], arg0[0][1],arg0[0][2],arg0[0][3]);
                  return res;
          }
          
          @Override
          protected void onPostExecute(String[] result){
		      super.onPostExecute(result);
		      int codigo_res = Integer.parseInt(result[0]);
		      Log.i("ESTO", Integer.toString(codigo_res));
          }
}










}


