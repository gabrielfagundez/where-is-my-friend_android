package com.whereismyfriend;



import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView mensajeError = (TextView) findViewById(R.id.textView1);
		EditText nom = (EditText) findViewById(R.id.editText1);
		EditText pass = (EditText) findViewById(R.id.editText2);
		Button but_login = (Button) findViewById(R.id.button1);
		ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
		
		but_login.setOnClickListener((OnClickListener) this);
		mensajeError.setVisibility(View.INVISIBLE);
		pbar.setVisibility(View.INVISIBLE);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	public void onClick(View v){
		
		switch (v.getId()){
		case R.id.button1: 
			// boton login
			// if me loguie bien
			Intent i = new Intent(this, Mapa.class);
			startActivity(i);
			
			break;
		}
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
