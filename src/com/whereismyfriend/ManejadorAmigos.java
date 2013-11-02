package com.whereismyfriend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.SharedPreferences;
import android.os.AsyncTask;






public class ManejadorAmigos {

	
	private Map amigos;
	private ArrayList<Amigo> amigosVisibles;
	private  SharedPreferences sharedPrefs;
	
	//SINGLETON
    private ManejadorAmigos() {
      amigos = new HashMap();
      
    }

    public static ManejadorAmigos getInstance() {
        return ManejadorAmigosHolder.INSTANCE;
    }

    private static class ManejadorAmigosHolder {
        private static final ManejadorAmigos INSTANCE = new ManejadorAmigos();

    }
    // FIN
    
    public void agregarAmigo(Amigo a){
    	amigos.put(a.getId().toUpperCase(), a);
    }
    
    public Amigo getAmigo(String id){
    	return (Amigo) amigos.get(id.toUpperCase());
    }
    
    

	public ArrayList<Amigo> getAmigosVisibles() {
		return amigosVisibles;
	}

	public void setAmigosVisibles(ArrayList<Amigo> amigosVisibles) {
		this.amigosVisibles = amigosVisibles;
	}

	
	public SharedPreferences getSharedPrefs() {
		return sharedPrefs;
	}

	public void setSharedPrefs(SharedPreferences sharedPrefs) {
		this.sharedPrefs = sharedPrefs;
	}
	
	
	

	public void actualizarPosiciones(){
    	new consumidorPost().execute();
    }
    
    private class consumidorPost extends AsyncTask<String[], Void, String[]>{
		protected String[] doInBackground(String[]... arg0) {
			// TODO Auto-generated method stub
			Comunicador com= new Comunicador();
			String[] res= com.GetLastFriendsLocationById(sharedPrefs.getString("user_id", ""));
			return res;
		}
		
		 @Override
			protected void onPostExecute(String[] result){
		        super.onPostExecute(result);
		       // setProgressBarIndeterminateVisibility(false);
		        
		        
		        int codigo_res = Integer.parseInt(result[0]);
				if (codigo_res==200){
					Comunicador com= new Comunicador();
				
					ArrayList<Amigo> amigos = new ArrayList<Amigo>();
					try {
						
						JSONTokener jsonT = new JSONTokener(result[1]);
					 	JSONArray jsonA = new JSONArray(jsonT);
					 	
						for (int i = 0; i < jsonA.length(); i++) {
						
							
						 	JSONObject jsonO = jsonA.getJSONObject(i);
						 	Amigo amigo = new Amigo(jsonO.get("Name").toString(),jsonO.get("Mail").toString(),jsonO.get("Id").toString());
							amigo.setLat(Double.parseDouble(jsonO.get("Latitude").toString()));
							amigo.setLon(Double.parseDouble(jsonO.get("Longitude").toString()));
						 	amigos.add(amigo);
						}
						
					} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
					}
						
					
					
					ManejadorAmigos.getInstance().setAmigosVisibles(amigos);
					
				}
				
				}
			}
		





}
    
    
    
    
    
	

