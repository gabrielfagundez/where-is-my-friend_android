	package com.whereismyfriend;





import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static Activity fa;//Esto permite matar la activity desde afuera
	String user_name;
	String user_id;
	String user_mail;
	String deviceid="";
	Button button1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fa = this;
		Intent intent = getIntent();
		deviceid= intent.getStringExtra("deviceid");
		if (deviceid==null)
			deviceid="";
		button1= (Button)findViewById(R.id.button1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//Escondo los iconos del men� de logout y settings
		//menu.findItem(R.id.action_logout).setVisible(false);
		menu.findItem(R.id.action_settings).setVisible(false);
		return true;
	}

	
	public void login(View view) {
		//RUTINA AL APRETAR EL BOTON DE LOGIN
		//Obtengo usuario y pass
		button1.setClickable(false);
		EditText mail = (EditText) findViewById(R.id.editText1);
	    String mail_str = mail.getText().toString();
		EditText password = (EditText) findViewById(R.id.editText2);
	    String password_str = password.getText().toString();
	    ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
	    
	    //Si alg�n campo est� vacio, evito la llamada al server
	    if ((mail_str.compareTo("")==0) || (password_str.compareTo("")==0)){
	    	Toast.makeText(getApplicationContext(), R.string.blank_fields , Toast.LENGTH_LONG).show();
	    }
	    else{
	    	//Hago la llamada al server
	    	String idioma= Locale.getDefault().getLanguage();
	    	if (idioma.compareTo("es")==0)
	    		idioma= "esp";
	    	else
	    		idioma="eng";
	    	
	    	String [] parametros = {mail_str,password_str, deviceid, idioma};
	    	pbar.setVisibility(view.VISIBLE);
	    	//setProgressBarIndeterminateVisibility(true);
	    	
	    	new consumidorPost().execute(parametros);
	    }
		
	}
	
	//METODOS LLAMADOS PARA HACER EL LOGIN
    private class consumidorPost extends AsyncTask<String[], Void, String[]>{
		protected String[] doInBackground(String[]... arg0) {
			// TODO Auto-generated method stub
			Comunicador com= new Comunicador();
			String[] res= com.postLogin(arg0[0][0], arg0[0][1], arg0[0][2], arg0[0][3]);
			return res;
		}
		
		 @Override
			protected void onPostExecute(String[] result){
		        super.onPostExecute(result);
		       // setProgressBarIndeterminateVisibility(false);
		        ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
		        pbar.setVisibility(pbar.INVISIBLE);
		        button1.setClickable(true);
		        
		        int codigo_res = Integer.parseInt(result[0]);
				if (codigo_res==200){
					
					
					
					//Login exitoso
					//Actualizamos las variables globales
					user_id=result[1];
					user_name=result[2];
					user_mail=result[3];
					//Guardamos el user como logueado
					SharedPreferences pref = getSharedPreferences("prefs",Context.MODE_PRIVATE);
					pref.edit().putBoolean("log_in", true).commit();
					pref.edit().putString("user_name", user_name).commit();
					pref.edit().putString("user_id", user_id).commit();
					pref.edit().putString("user_mail", user_mail).commit();
					//Paso a la siguiente activity
					Intent intent_name = new Intent();
					intent_name.setClass(getApplicationContext(),Amigos.class);
					intent_name.putExtra("name", user_name);
					intent_name.putExtra("id", user_id);
					startActivity(intent_name);
					finish();
				}
				else if (codigo_res==404) {
					//USUARIO NO ENCONTRADO
					//Borro los campos y pongo el foco en el primero
					EditText mail = (EditText) findViewById(R.id.editText1);
					EditText password = (EditText) findViewById(R.id.editText2);
					mail.setText("");
					password.setText("");
					mail.requestFocus();
			    	Toast.makeText(getApplicationContext(), R.string.user_not_found , Toast.LENGTH_LONG).show();
				}
				else if (codigo_res==401){
					//PASSWORD INCORRECTO
					//Borro el campo de password y pongo foco en el
					EditText password = (EditText) findViewById(R.id.editText2);
					password.setText("");
					password.requestFocus();
			    	Toast.makeText(getApplicationContext(), R.string.invalid_password, Toast.LENGTH_LONG).show();
				}
				else{
					//OTRO TIPO DE ERROR
					//Borro los campos y pongo el foco en el primero
					EditText mail = (EditText) findViewById(R.id.editText1);
					EditText password = (EditText) findViewById(R.id.editText2);
					password.setText("");
					mail.requestFocus();
			    	Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
					
				}
			}
		
	}
	
    
   
    
    
	@Override
	protected void onResume() {
		super.onResume();
		
		
		//Veo si ya est� logueado
		SharedPreferences pref = getSharedPreferences("prefs",Context.MODE_PRIVATE);
		boolean logueado = pref.getBoolean("log_in", false);
		if (logueado){
			//Redirijo a la activity con el Mapa
			Intent intent_name = new Intent();
			intent_name.setClass(getApplicationContext(), Amigos.class);
			intent_name.putExtra("name", pref.getString("user_name", ""));
			intent_name.putExtra("id", pref.getString("user_id", ""));
			startActivity(intent_name);
			this.finish();
		}
		else{
			//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
			setContentView(R.layout.activity_main);
			
			EditText nom = (EditText) findViewById(R.id.editText1);
			EditText pass = (EditText) findViewById(R.id.editText2);
			Button but_login = (Button) findViewById(R.id.button1);
			ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
			
			//but_login.setOnClickListener((OnClickListener) this);
			pbar.setVisibility(View.INVISIBLE);
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
