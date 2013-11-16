package com.whereismyfriend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa extends android.support.v4.app.FragmentActivity implements LocationListener {
	
    GoogleMap googleMap;
	private static Context context;
	public static Activity activ;
	public static int isfront=0;
	ImageButton button1;
	ImageButton button2;
	ImageButton button3;
	
	final int MARKER_UPDATE_INTERVAL = 10000; /* milliseconds */
    Handler handler = new Handler();

    Runnable updateMarker = new Runnable() {
        @Override
        public void run() {
            googleMap.clear();
            
            ManejadorAmigos manejador = ManejadorAmigos.getInstance();

			
			try {
				manejador.actualizarPosiciones();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            ArrayList<Amigo> amigos = manejador.getAmigosVisibles();
            
            if (amigos != null){
            	Iterator<Amigo> it = amigos.iterator();
                while(it.hasNext())
                {
                    Amigo am = it.next();
                    if (am.getLat()!=-1){
	                    MarkerOptions mr = new MarkerOptions();
	                    mr.position(new LatLng(am.getLat(),am.getLon()));
	                    mr.title(manejador.getAmigoByMail(am.getMail()).getName());
	                    googleMap.addMarker(mr);
                    }
                    else{                  
    			    	Toast.makeText(getApplicationContext(),R.string.no_marker_1+manejador.getAmigoByMail(am.getMail()).getName()+ R.string.no_marker_2, Toast.LENGTH_LONG).show();
                    }
                }
            }
            
            //marker = map.addMarker(new MarkerOptions().position(location));

            handler.postDelayed(this, MARKER_UPDATE_INTERVAL);
        }
    };
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(1);
		if (Solicitudes.activ!=null)
			Solicitudes.activ.finish();
		if (Mapa.activ!=null)
			Mapa.activ.finish();
		if (Amigos.activ!=null)
			Amigos.activ.finish();
		isfront=1;
		setContentView(R.layout.mapa);
		
		//Pido los botones para bloquearlos
		button1= (ImageButton) findViewById(R.id.ImageButton4);
		button2 = (ImageButton) findViewById(R.id.ImageButton5);
		button3= (ImageButton) findViewById(R.id.imageButton1);
		button1.setClickable(true);
		button2.setClickable(true);
		button3.setClickable(true);
		
		Mapa.context = getApplicationContext();
		activ = this;
		// Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play Services are available

            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();

            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);

            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);

            if(location!=null){
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 5000, 0, this);		
        
            ManejadorAmigos manejador = ManejadorAmigos.getInstance();

			
			try {
				manejador.actualizarPosiciones();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            ArrayList<Amigo> amigos = manejador.getAmigosVisibles();
            
            if (amigos != null){
            	Iterator<Amigo> it = amigos.iterator();
                while(it.hasNext())
                {
                    Amigo am = it.next();
                    if (am.getLat()!=-1){
	                    MarkerOptions mr = new MarkerOptions();
	                    mr.position(new LatLng(am.getLat(),am.getLon()));
	                    mr.title(manejador.getAmigoByMail(am.getMail()).getName());
	                    googleMap.addMarker(mr);
                    }
                    else{
    			    	Toast.makeText(getApplicationContext(),R.string.no_marker_1+manejador.getAmigoByMail(am.getMail()).getName()+ R.string.no_marker_2, Toast.LENGTH_LONG).show();
                    }
                }
            }
            
            
            
      /*      MarkerOptions a = new MarkerOptions();
            a.position(new LatLng(-34,-56));
            a.title("prueba");
            googleMap.addMarker(a);
            //m.setPosition(new LatLng(50,5));*/
        
        }
            
        handler.postDelayed(updateMarker, MARKER_UPDATE_INTERVAL);
        ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
		pbar.setVisibility(View.INVISIBLE);
        
	}
	
	@Override
    protected void onDestroy() {
        handler.removeCallbacks(updateMarker);

        super.onDestroy();
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

	
	@Override
    public void onLocationChanged(Location location) {


        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
       // googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        

    }
	
	@Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void amigos (View view){
		Intent intent_name = new Intent();
		intent_name.setClass(getApplicationContext(), Amigos.class);
		startActivity(intent_name);
	}
	
	public void requests (View view){
		Intent intent_name = new Intent();
		intent_name.setClass(getApplicationContext(), Solicitudes.class);
		startActivity(intent_name);
	}
	
	public void logout(View view) {
		new AlertDialog.Builder(this)
        .setMessage(getResources().getString(R.string.confirm_logout))
        .setCancelable(true)
        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
        		//RUTINA AL APRETAR EL BOTON DE logout
                ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar1);
        		pbar.setVisibility(View.VISIBLE);
        		button1.setClickable(false);
        		button2.setClickable(false);
        		button3.setClickable(false);
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
				button1.setClickable(true);
				button2.setClickable(true);
				button3.setClickable(true);
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
	
	
	

}
