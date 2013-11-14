package com.whereismyfriend;





import java.util.concurrent.ExecutionException;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Amigos extends Activity implements AdapterView.OnItemClickListener {

	public static Activity activ;
	private static Context context;
	private String IdTo; 
	public static int isfront;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        
		super.onCreate(savedInstanceState);
		if (Solicitudes.activ!=null)
			Solicitudes.activ.finish();
		if (Mapa.activ!=null)
			Mapa.activ.finish();
		if (Amigos.activ!=null)
			Amigos.activ.finish();
		isfront=1;
		setContentView(R.layout.amigos);
		
		ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
		pbar.setVisibility(View.VISIBLE);
		
		Comunicador com = new Comunicador();
		SharedPreferences pref = getSharedPreferences("prefs",Context.MODE_PRIVATE);
		TableLayout tabla =  (TableLayout) findViewById(R.id.table);
		
		ManejadorAmigos ma = ManejadorAmigos.getInstance();
		ma.setSharedPrefs(pref);
		
		Amigos.context = getApplicationContext();
		new consumidorPost().execute();
	
		activ = this;
		
        ListView list = (ListView) findViewById(R.id.list);
		list.setOnItemClickListener(this);
	}
	
	
	@Override
	public void onResume (){
        isfront=1;
        super.onResume();
	}
	
	@Override
	public void onStart (){
        isfront=1;
        super.onStart();
	}
	
	@Override
	public void onStop (){
        isfront=0;
        super.onStop();
	}
	@Override
	public void onPause (){
        isfront=0;
        super.onPause();
	}
	
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
		        ListView list = (ListView) findViewById(R.id.list);
		        list.setVisibility(View.VISIBLE);
		        
		        TextView text = (TextView) findViewById(R.id.textView2);
		        text.setVisibility(View.INVISIBLE);

		        
		        int codigo_res = Integer.parseInt(result[0]);
				if (codigo_res==200){
					Comunicador com= new Comunicador();
					//TableLayout tabla =  (TableLayout) findViewById(R.id.table);
					//String[] amigos = com.getFriends(getSharedPreferences("prefs",Context.MODE_PRIVATE).getString("user_id","1"));
					
					try {
						
						JSONTokener jsonT = new JSONTokener(result[1]);
						JSONArray jsonA = new JSONArray(jsonT);
						
						ListItem[] data= new ListItem[jsonA.length()];
						
						if (jsonA.length() == 0){
							text.setText(getString(R.string.no_amigos));
							text.setVisibility(View.VISIBLE);
							list.setVisibility(View.INVISIBLE);
							list.setVisibility(View.GONE);
						}
						
						for (int i = 0; i < jsonA.length(); i++) {
								//TableRow row= new TableRow(Amigos.context);
								TextView name = new TextView(Amigos.context);
								name.setTextSize(30);
							
							 	
							 	JSONObject jsonO = jsonA.getJSONObject(i);
								name.setText(jsonO.get("Name").toString());
								//row.addView(name);
								//tabla.addView(row,i);
								
								ListItem item = new ListItem(R.drawable.contacto,jsonO.get("Name").toString() );
								item.setId(jsonO.get("Id").toString());
								item.setName(jsonO.get("Name").toString());
								data[i] = item;
								
								Amigo a = new Amigo(jsonO.get("Name").toString(), jsonO.get("Mail").toString(), jsonO.get("Id").toString());
								ManejadorAmigos ma = ManejadorAmigos.getInstance();
								ma.agregarAmigo(a);
								
						}
						
						ListAdapter adapter = new ListAdapter(Amigos.this, R.layout.list_item, data);
						list.setAdapter(adapter);
						list.setLongClickable(true);
						
						
					} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}
						
					
					
				}
				else if (codigo_res==404) {
					Toast.makeText(getApplicationContext(),R.string.user_not_found, Toast.LENGTH_LONG).show();
				}
				else if (codigo_res==401){
					Toast.makeText(getApplicationContext(),R.string.invalid_password, Toast.LENGTH_LONG).show();
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
		
	}
	
	public void requests(View view) {
		//RUTINA AL APRETAR EL BOTON DE requests
		Intent intent_name = new Intent();
		intent_name.setClass(getApplicationContext(), Solicitudes.class);
		startActivity(intent_name);
		
	}
	
	public void actualizar(View view) {
		//RUTINA AL APRETAR EL BOTON DE actualizar
		ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
		pbar.setVisibility(View.VISIBLE);
		new consumidorPost().execute();
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
					intent_name.setClass(getApplicationContext(), DemoActivity.class);
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
    
   
    
   
	
    private class consumidorPostSolicitud extends AsyncTask<String[], Void, String[]>{
		protected String[] doInBackground(String[]... arg0) {
			// TODO Auto-generated method stub
			Comunicador com= new Comunicador();
			String[] res= com.sendSolicitud((getSharedPreferences("prefs",Context.MODE_PRIVATE).getString("user_id", "")), IdTo);
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
					
					Toast.makeText(getApplicationContext(),R.string.mando_solicitud_correctamente, Toast.LENGTH_LONG).show();

					new consumidorPost().execute();

					
					
				}
				else if (codigo_res==404) {
					Toast.makeText(getApplicationContext(),R.string.user_not_found, Toast.LENGTH_LONG).show();
				}
				else if (codigo_res==401){
					Toast.makeText(getApplicationContext(),R.string.invalid_password, Toast.LENGTH_LONG).show();
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
	    this.IdTo = item.getId();
	    
	    ManejadorAmigos manejador = ManejadorAmigos.getInstance();

        ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
        pbar.setVisibility(pbar.VISIBLE);
		try {
			manejador.actualizarPosiciones();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    boolean mande = manejador.yaEstaEnVisibles(IdTo);
	    pbar.setVisibility(pbar.INVISIBLE);
	    if (mande){
	    	Toast.makeText(getApplicationContext(), R.string.solicitud_ya_enviada, Toast.LENGTH_LONG).show();
	    }else{
		    final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(getString(R.string.solicitud_titulo));
			alertDialog.setMessage(getString(R.string.enviar_solicitud) + " " + item.getName() );
			alertDialog.setButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				// here you can add functions
					ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
			        pbar.setVisibility(pbar.VISIBLE);	
				    new consumidorPostSolicitud().execute();
				}
			});
			alertDialog.setButton2(getString(R.string.no), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				// here you can add functions
						
				}
			});
			alertDialog.setIcon(R.drawable.contacto);
			alertDialog.show();	
	    }
	}
	
	
	
	 /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
      ListItem item =  (ListItem) getListAdapter().getItem(position);
      this.IdTo = item.id;
      ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
      pbar.setVisibility(l.VISIBLE);
      new consumidorPostSolicitud().execute();
    }*/
	
	
}
