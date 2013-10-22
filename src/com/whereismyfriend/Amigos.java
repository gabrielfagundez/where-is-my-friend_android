package com.whereismyfriend;





import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class Amigos extends Activity {

	 private static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.amigos);
		
		ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
		pbar.setVisibility(View.VISIBLE);
		
		Comunicador com = new Comunicador();
		SharedPreferences pref = getSharedPreferences("prefs",Context.MODE_PRIVATE);
		TableLayout tabla =  (TableLayout) findViewById(R.id.table);
		
		Amigos.context = getApplicationContext();
		new consumidorPost().execute();
	
	}
	
	
	//METODOS LLAMADOS PARA HACER EL LOGIN
    private class consumidorPost extends AsyncTask<String[], Void, String[]>{
		protected String[] doInBackground(String[]... arg0) {
			// TODO Auto-generated method stub
			Comunicador com= new Comunicador();
			String[] res= com.getFriends(getSharedPreferences("prefs",Context.MODE_PRIVATE).getString("user_id", ""));
			return res;
		}
		
		 @Override
			protected void onPostExecute(String[] result){
		        super.onPostExecute(result);
		       // setProgressBarIndeterminateVisibility(false);
		        ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
		        pbar.setVisibility(pbar.INVISIBLE);
		        ListView list = (ListView) findViewById(R.id.listView1);
		        
		        int codigo_res = Integer.parseInt(result[0]);
				if (codigo_res==200){
					Comunicador com= new Comunicador();
					//TableLayout tabla =  (TableLayout) findViewById(R.id.table);
					//String[] amigos = com.getFriends(getSharedPreferences("prefs",Context.MODE_PRIVATE).getString("user_id","1"));
					
					
					ListItem[] data= new ListItem[result.length+1];
					
					for (int i = 0; i < result.length+1; i++) {
						//TableRow row= new TableRow(Amigos.context);
						TextView name = new TextView(Amigos.context);
						name.setTextSize(30);
						
						JSONTokener jsonT = new JSONTokener(result[1]);
						
						 try {
							 	JSONArray jsonA = new JSONArray(jsonT);
							 	JSONObject jsonO = jsonA.getJSONObject(i);
								name.setText(jsonO.get("Name").toString());
								//row.addView(name);
								//tabla.addView(row,i);
								
								ListItem item = new ListItem(R.drawable.contacto,jsonO.get("Name").toString() );
								data[i] = item;
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
					}
					
					ListAdapter adapter = new ListAdapter(Amigos.this, R.layout.list_item, data);
					list.setAdapter(adapter);
				}
				else if (codigo_res==404) {
					Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
				}
				else if (codigo_res==401){
					Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
				}
				else{
					//OTRO TIPO DE ERROR
			    	Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
					
				}
			}
		
	}
	
	public void mapa(View view) {
		//RUTINA AL APRETAR EL BOTON DE mapa
		Intent intent_name = new Intent();
		intent_name.setClass(getApplicationContext(), Mapa.class);
		startActivity(intent_name);
		this.finish();
	}
	
	public void requests(View view) {
		//RUTINA AL APRETAR EL BOTON DE requests
		Intent intent_name = new Intent();
		intent_name.setClass(getApplicationContext(), Solicitudes.class);
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
