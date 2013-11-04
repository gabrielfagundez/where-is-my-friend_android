package com.whereismyfriend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;





import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;




public class Solicitudes extends Activity implements AdapterView.OnItemClickListener{
	
	
	public static Activity activ;
	private static Context context;
	public static String idSol;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.solicitudes);
		
		context = getApplicationContext();

		activ = this;

		ListView list = (ListView) findViewById(R.id.list);
		list.setOnItemClickListener(this);
		new consumidorPost().execute();

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
		new AlertDialog.Builder(this)
        .setMessage(getResources().getString(R.string.confirm_logout))
        .setCancelable(true)
        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
        		//RUTINA AL APRETAR EL BOTON DE logout
            	ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
        		pbar.setVisibility(pbar.VISIBLE);
        		Comunicador com = new Comunicador();
        		new consumidorPostLogout().execute();
	            	
            }
        })
        .setNegativeButton(getResources().getString(R.string.no), null)
        .show();

		
	}
	
	
	
	//METODOS LLAMADOS PARA HACER EL LOGOUT
    private class consumidorPostLogout extends AsyncTask<String, Void, String>{
		protected String doInBackground(String...s) {
			// TODO Auto-generated method stub
			Comunicador com= new Comunicador();
			String res = com.postLogout(context.getSharedPreferences("prefs",Context.MODE_PRIVATE).getString("user_mail", "1"));
			return res;
		}
		
		 @Override
			protected void onPostExecute(String result){
		        super.onPostExecute(result);
		       // setProgressBarIndeterminateVisibility(false);
		        ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
		        pbar.setVisibility(pbar.INVISIBLE);
		        
		        int codigo_res = Integer.parseInt(result);
				if (codigo_res==200){
					//Logout exitoso
					SharedPreferences pref = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
					pref.edit().putBoolean("log_in", false).commit();
					pref.edit().putString("user_name", "").commit();
					pref.edit().putString("user_id", "").commit();
					pref.edit().putString("user_mail", "").commit();
					
					Intent intent_name = new Intent();
					intent_name.setClass(getApplicationContext(), MainActivity.class);
					startActivity(intent_name);
					activ.finish();
				}
				else if (codigo_res==404) {
					//USUARIO NO ENCONTRADO
					
			    	Toast.makeText(getApplicationContext(), R.string.user_not_found , Toast.LENGTH_LONG).show();
				}
				else{
					//OTRO TIPO DE ERROR
			    	Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
					
				}
			}
		
	}
    
	
	 private class consumidorPost extends AsyncTask<String[], Void, String[]>{
			protected String[] doInBackground(String[]... arg0) {
				// TODO Auto-generated method stub
				Comunicador com= new Comunicador();
				String[] res= com.getSolicitudes(getSharedPreferences("prefs",Context.MODE_PRIVATE).getString("user_id", ""));
				return res;
			}
			
			 @Override
				protected void onPostExecute(String[] result){
			        super.onPostExecute(result);
			       // setProgressBarIndeterminateVisibility(false);
			        ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
			        pbar.setVisibility(pbar.INVISIBLE);
			        ListView list = (ListView) findViewById(R.id.list);
			        
			        int codigo_res = Integer.parseInt(result[0]);
					if (codigo_res==200){
						Comunicador com= new Comunicador();
						
						try {
							
							JSONTokener jsonT = new JSONTokener(result[1]);
							JSONArray jsonA = new JSONArray(jsonT);
							
							ListItem[] data= new ListItem[jsonA.length()];
							
							for (int i = 0; i < jsonA.length(); i++) {
									//TextView name = new TextView(Solicitudes.context);
									//name.setTextSize(30);
								
								 	
								 	JSONObject jsonO = jsonA.getJSONObject(i);
									//name.setText(jsonO.get("Name").toString());
									
									ListItem item = new ListItem(R.drawable.contacto,jsonO.get("SolicitudFromNombre").toString() );
									item.setIdSol(jsonO.get("SolicitudId").toString());
									item.setName(jsonO.get("SolicitudFromNombre").toString());
									data[i] = item;
									
									//Amigo a = new Amigo(jsonO.get("Name").toString(), jsonO.get("Mail").toString(), jsonO.get("Id").toString());
									//ManejadorAmigos ma = ManejadorAmigos.getInstance();
									//ma.agregarAmigo(a);
									
							}
							
							ListAdapter adapter = new ListAdapter(Solicitudes.this, R.layout.list_item, data);
							list.setAdapter(adapter);
							list.setLongClickable(true);
							
							
						} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
						}
							
						
						
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
	
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
		// TODO Auto-generated method stub
		ListItem item =  (ListItem) l.getItemAtPosition(position);
		ManejadorAmigos ma = ManejadorAmigos.getInstance();
		this.idSol = item.getIdSol();
		
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(getString(R.string.solicitud_titulo));
		alertDialog.setMessage(getString(R.string.solicitud) + " " + item.getName() );
		alertDialog.setButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			// here you can add functions
					
				new consumidorPostAceptarSolicitud().execute();
			}
		});
		alertDialog.setButton2(getString(R.string.rechazar), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			// here you can add functions
					
				new consumidorPostRechazarSolicitud().execute();

			}
		});
		alertDialog.setIcon(R.drawable.contacto);
		alertDialog.show();	
	}
	
	 private class consumidorPostAceptarSolicitud extends AsyncTask<String[], Void, String[]>{
			protected String[] doInBackground(String[]... arg0) {
				// TODO Auto-generated method stub
				Comunicador com= new Comunicador();
				String[] res= com.aceptarSolicitud((getSharedPreferences("prefs",Context.MODE_PRIVATE).getString("user_id", "")), Solicitudes.idSol);
				return res; 
			}
			
			 @Override
				protected void onPostExecute(String[] result){
			        super.onPostExecute(result);
			        ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
			        pbar.setVisibility(pbar.INVISIBLE);
			        ListView list = (ListView) findViewById(R.id.list);
			        
			        int codigo_res = Integer.parseInt(result[0]);
					if (codigo_res==200){
						
						Toast.makeText(getApplicationContext(),"Acepto solicitud correctamente"+result[1], Toast.LENGTH_LONG).show();

							
						
						
					}
					else if (codigo_res==404) {
						Toast.makeText(getApplicationContext(),"Error404", Toast.LENGTH_LONG).show();
					}
					else if (codigo_res==401){
						Toast.makeText(getApplicationContext(),"Error401", Toast.LENGTH_LONG).show();
					}
					else{
						//OTRO TIPO DE ERROR
				    	Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
						
					}
				}
			
		}
	
	 
	 private class consumidorPostRechazarSolicitud extends AsyncTask<String[], Void, String[]>{
			protected String[] doInBackground(String[]... arg0) {
				// TODO Auto-generated method stub
				Comunicador com= new Comunicador();
				String[] res= com.rechazarSolicitud((getSharedPreferences("prefs",Context.MODE_PRIVATE).getString("user_id", "")), Solicitudes.idSol);
				return res; 
			}
			
			 @Override
				protected void onPostExecute(String[] result){
			        super.onPostExecute(result);
			        ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
			        pbar.setVisibility(pbar.INVISIBLE);
			        ListView list = (ListView) findViewById(R.id.list);
			        
			        int codigo_res = Integer.parseInt(result[0]);
					if (codigo_res==200){
						
						Toast.makeText(getApplicationContext(),"Rechazo solicitud correctamente"+result[1], Toast.LENGTH_LONG).show();

							
						
						
					}
					else if (codigo_res==404) {
						Toast.makeText(getApplicationContext(),"Error404", Toast.LENGTH_LONG).show();
					}
					else if (codigo_res==401){
						Toast.makeText(getApplicationContext(),"Error401", Toast.LENGTH_LONG).show();
					}
					else{
						//OTRO TIPO DE ERROR
				    	Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
						
					}
				}
			
		}
	 
	 
}