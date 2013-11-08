package com.whereismyfriend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;






public class ManejadorAmigos {

	
	private Map amigos;
	private Map amigosPorMail;
	private ArrayList<Amigo> amigosVisibles;
	private  SharedPreferences sharedPrefs;
	
	//SINGLETON
    private ManejadorAmigos() {
      amigos = new HashMap();
      amigosPorMail = new HashMap();
      
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
    	amigosPorMail.put(a.getMail().toUpperCase(), a);
    }
    
    public Amigo getAmigo(String id){
    	return (Amigo) amigos.get(id.toUpperCase());
    }
    
    public Amigo getAmigoByMail(String mail){
    	return (Amigo) amigosPorMail.get(mail.toUpperCase());

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
	
	
	

	public void actualizarPosiciones() throws InterruptedException, ExecutionException{
    	consumidorPost cp = new consumidorPost();
    	String [] s = cp.execute().get();
    }
    
    private class consumidorPost extends AsyncTask<String[], Void, String[]>{
		protected String[] doInBackground(String[]... arg0) {
			// TODO Auto-generated method stub
			Comunicador com= new Comunicador();
			String[] res= com.GetLastFriendsLocationById(sharedPrefs.getString("user_id", ""));
			
			int codigo_res = Integer.parseInt(res[0]);
			
			
			if (codigo_res==200){
			
				ArrayList<Amigo> amigos = new ArrayList<Amigo>();
				try {
					
					JSONTokener jsonT = new JSONTokener(res[1]);
				 	JSONArray jsonA = new JSONArray(jsonT);
				 	
					for (int i = 0; i < jsonA.length(); i++) {
						JSONObject jsonO = jsonA.getJSONObject(i);
					 	Amigo amigo = new Amigo("NAME",jsonO.get("Mail").toString(),"ID");
						amigo.setLat(Double.parseDouble(jsonO.get("Latitude").toString()));
						amigo.setLon(Double.parseDouble(jsonO.get("Longitude").toString()));
					 	amigos.add(amigo);
					 	System.out.println(jsonO.get("Mail").toString());
					}
					
				} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
				}
					
				
				
				ManejadorAmigos.getInstance().setAmigosVisibles(amigos);
				
			}
			
			return res;
		}
		
		 @Override
			protected void onPostExecute(String[] result){
		        super.onPostExecute(result);
		       // setProgressBarIndeterminateVisibility(false);
		        
							
				
		        
		        
			}
		
    }




}
    
    
    
    
    
	

