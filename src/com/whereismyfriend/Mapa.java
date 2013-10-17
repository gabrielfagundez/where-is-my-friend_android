package com.whereismyfriend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.internal.v;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa extends Activity {
	
	MapView mapView;

	private final LatLng FING = new LatLng(-34.918364, -56.166798);
	private GoogleMap map;
	
	 @Override 
	 protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_main);
	      
	      if (MainActivity.fa !=null)
				MainActivity.fa.finish();
	      
	      mapView = (MapView) findViewById(R.id.mapView);
	      mapView.onCreate(savedInstanceState);
	      
	   // Gets to GoogleMap from the MapView and does initialization stuff
			map = mapView.getMap();
			map.getUiSettings().setMyLocationButtonEnabled(false);
			map.setMyLocationEnabled(true);
	 
			// Needs to call MapsInitializer before doing any CameraUpdateFactory calls
			try {
				MapsInitializer.initialize(this);
			} catch (GooglePlayServicesNotAvailableException e) {
				e.printStackTrace();
			}
			
			// Updates the location and zoom of the MapView
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(FING, 10);
			map.animateCamera(cameraUpdate);

	   }
	 

	   
	   @Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	       // Inflate the menu items for use in the action bar
	       MenuInflater inflater = getMenuInflater();
	       inflater.inflate(R.menu.main, menu);
	       return super.onCreateOptionsMenu(menu);
	   }
		
	   
	   public void requests(View view) {
			//RUTINA AL APRETAR EL BOTON DE mapa
			Intent intent_name = new Intent();
			intent_name.setClass(getApplicationContext(), Solicitudes.class);
			startActivity(intent_name);
			this.finish();
		}
		
		public void amigos(View view) {
			//RUTINA AL APRETAR EL BOTON DE requests
			Intent intent_name = new Intent();
			intent_name.setClass(getApplicationContext(), Amigos.class);
			startActivity(intent_name);
			this.finish();
		}
	   
	   
	   
	   @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		    switch (keyCode) {
		    case KeyEvent.KEYCODE_VOLUME_UP:
		        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
		                AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
		        return true;
		    case KeyEvent.KEYCODE_VOLUME_DOWN:
		        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
		                AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
		        return true;
		    default:
		    	return super.onKeyDown(keyCode, event);
		    }
		}
		
	   
	 //Manejo de los botones de la Action Bar
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		    switch (item.getItemId()) {
		    	//Al apretar el boton de logout
		        case R.id.action_logout:
		        	//Actualizo las preferencias
		        	SharedPreferences pref = getSharedPreferences("prefs",Context.MODE_PRIVATE);
					pref.edit().putBoolean("log_in", false).commit();
					pref.edit().putString("user_name", "").commit();
					pref.edit().putString("user_id", "").commit();
		            // go to previous screen when app icon in action bar is clicked
		            Intent intent = new Intent(this, MainActivity.class);
		            startActivity(intent);
		            finish();
		            return true;
		         //Al apretar el boton de settings
		    }
		    return super.onOptionsItemSelected(item);
		}
		
		
		public void logout(View view) {
			//RUTINA AL APRETAR EL BOTON DE requests
			Comunicador com = new Comunicador();
			com.logout(view);
			Intent intent_name = new Intent();
			intent_name.setClass(getApplicationContext(), MainActivity.class);
			startActivity(intent_name);
			finish();
		}
		
	
}
