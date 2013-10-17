package com.whereismyfriend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

public class Solicitudes extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.solicitudes);
		
		
	}
	
	
	public void mapa(View view) {
		//RUTINA AL APRETAR EL BOTON DE mapa
		Intent intent_name = new Intent();
		intent_name.setClass(getApplicationContext(), Mapa.class);
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
	
	public void logout(View view) {
		//RUTINA AL APRETAR EL BOTON DE requests
		Comunicador com = new Comunicador();
		com.logout(view);
		Intent intent_name = new Intent();
		intent_name.setClass(getApplicationContext(), MainActivity.class);
		startActivity(intent_name);
		this.finish();
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
	
	
	
	
	
	
}
