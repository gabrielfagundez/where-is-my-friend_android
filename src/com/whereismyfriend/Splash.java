package com.whereismyfriend;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		ImageView imgview = (ImageView) findViewById(R.id.imageView1);
		imgview.setScaleType(ScaleType.FIT_XY);
		final Splash s= this;
		Thread logoTimer = new Thread(){
			@Override
			public void run() {
				try {
                    sleep(3000);
                    Intent i = new Intent(s,MainActivity.class);
                    startActivity(i);
				}catch (InterruptedException e) {
					e.printStackTrace();
				}finally{
					 finish();
				}
			}
		};
		
		logoTimer.start();
	
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
