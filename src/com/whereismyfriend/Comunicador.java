package com.whereismyfriend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

public class Comunicador {

	
	
	
	public String[] postLogin(String user, String pass) {
		
		String res_id="";
		String res_name="";
		String res_codigo="";
		String res_mail="";
		

	    try {
	    	Properties prop = new Properties();
			prop.load(getClass().getResourceAsStream("server.properties"));
			String server = prop.getProperty("loginwhere");
		    // Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(server);
			
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
	        nameValuePairs.add(new BasicNameValuePair("Mail", user));
	        nameValuePairs.add(new BasicNameValuePair("Password", pass));
	        nameValuePairs.add(new BasicNameValuePair("Platform", "android"));
	        nameValuePairs.add(new BasicNameValuePair("DeviceId", "121"));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        //Obtengo el codigo de la respuesta http
	        int response_code = response.getStatusLine().getStatusCode();
	        //Obtengo el nombre de usuario
	        if (response_code==200){
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		        String json = reader.readLine();
		        JSONTokener tokener = new JSONTokener(json);
		        try {
					JSONObject finalResult = new JSONObject(tokener);
			        res_name =finalResult.get("Name").toString();
			        res_id =finalResult.get("Id").toString();
			        res_mail =finalResult.get("Mail").toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        }
	        
	        res_codigo=Integer.toString(response_code);
	        String[] result= {res_codigo, res_id, res_name, res_mail};
	        return result;

	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	String[] result={"-1"};
	    	return result;
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	String[] result={"-1"};
	    	return result;
	    }
	} 
	
	public String[] getFriends(String id) {

		String[] result = {"-1","-1"};
		 //crear un ArrayList bidimensional de enteros vac�o
        //Realmente se crea un ArrayList de ArrayLists de strings
       // ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
		
		try {
			Properties prop = new Properties();
			prop.load(getClass().getResourceAsStream("server.properties"));
			String server = prop.getProperty("getallfriends");
			  // Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet(server + id);
		
		HttpResponse response = httpclient.execute(httpGet);
		
		 //Obtengo el codigo de la respuesta http
        int response_code = response.getStatusLine().getStatusCode();
        result[0] = Integer.toString(response_code);
        if (response_code==200){
        	
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
	        String json = reader.readLine();
	        result[1]=json;
		
        }
		
		return result;
		
		} catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	String[] result2={"-1"};
	    	return result2;
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	String[] result2={"-1"};
	    	return result2;
	    }
		
	}

	public String postLogout(String user){
	
		

	    try {
			Properties prop = new Properties();
			prop.load(getClass().getResourceAsStream("server.properties"));
			String server = prop.getProperty("logoutwhere");
			// Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(server);
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("Mail", user));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        //Obtengo el codigo de la respuesta http
	        int response_code = response.getStatusLine().getStatusCode();
	        //Obtengo el nombre de usuario
	        return Integer.toString(response_code);
	        

	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	return "1";
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	return "1";
	    }
		
	}
	
	public String[] sendSolicitud(String userid, String friendid){
		
		String[] result = {"-1","-1"};
		
		// Create a new HttpClient and Post Header
	    
	    try {
	    	Properties prop = new Properties();
			prop.load(getClass().getResourceAsStream("server.properties"));
			String server = prop.getProperty("solicitudsend");
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(server);
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("IdFrom", userid));
	        nameValuePairs.add(new BasicNameValuePair("IdTo", friendid));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	      
	        //Obtengo el codigo de la respuesta http
	        int response_code = response.getStatusLine().getStatusCode();
	        result[0] = Integer.toString(response_code);
	        
	        if (response_code==200){
	        	
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		        String json = reader.readLine();
		        JSONTokener tokener = new JSONTokener(json);
		        try {
					JSONObject finalResult = new JSONObject(tokener);
					result[1]= finalResult.get("IdTo").toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  
			
	        }
			
			return result;
			
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	String[] result2={"-1"};
	    	return result2;
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	String[] result2={"-1"};
	    	return result2;
	    }
	}
	
	
	public String[] GetLastFriendsLocationById(String userid){
	
		
	    String[] result = {"-1","-1"};
		 
		
	    // Create a new HttpClient and Post Header
		try {
			Properties prop = new Properties();
			prop.load(getClass().getResourceAsStream("server.properties"));
			String server = prop.getProperty("lastfriendlocation");
			  HttpClient httpclient = new DefaultHttpClient();
			    HttpGet httpGet = new HttpGet(server + userid);
		
			HttpResponse response = httpclient.execute(httpGet);
			
			 //Obtengo el codigo de la respuesta http
	       int response_code = response.getStatusLine().getStatusCode();
	       result[0] = Integer.toString(response_code);
	       if (response_code==200){
	       	
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		        String json = reader.readLine();
		        result[1]=json;
		
	       }
		
		return result;
		
		} catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	String[] result2={"-1"};
	    	return result2;
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	String[] result2={"-1"};
	    	return result2;
	    }
	}
	
	
	public String[] getSolicitudes(String id) {

		String[] result = {"-1","-1"};
		 //crear un ArrayList bidimensional de enteros vacío
        //Realmente se crea un ArrayList de ArrayLists de strings
       // ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
		
	   
		try {			
			Properties prop = new Properties();
			prop.load(getClass().getResourceAsStream("server.properties"));
			String server = prop.getProperty("getallsolicitudes");
			 // Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet(server + id);
		
		HttpResponse response = httpclient.execute(httpGet);
		
		 //Obtengo el codigo de la respuesta http
        int response_code = response.getStatusLine().getStatusCode();
        result[0] = Integer.toString(response_code);
        if (response_code==200){
        	
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
	        String json = reader.readLine();
	        result[1]=json;
		
        }
		
		return result;
		
		} catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	String[] result2={"-1"};
	    	return result2;
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	String[] result2={"-1"};
	    	return result2;
	    }
		
	}
	
public String[] aceptarSolicitud(String userid, String friendid){
		
		String[] result = {"-1","-1"};
		
		
	    try {
	    	Properties prop = new Properties();
			prop.load(getClass().getResourceAsStream("server.properties"));
			String server = prop.getProperty("acceptsolicitudes");
			// Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(server);
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("IdUser", userid));
	        nameValuePairs.add(new BasicNameValuePair("IdSolicitud", friendid));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	      
	        //Obtengo el codigo de la respuesta http
	        int response_code = response.getStatusLine().getStatusCode();
	        result[0] = Integer.toString(response_code);
	        
	        if (response_code==200){
	        	
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		        String json = reader.readLine();
		        JSONTokener tokener = new JSONTokener(json);
		        try {
					JSONObject finalResult = new JSONObject(tokener);
					// procesar la respuesta
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  
			
	        }
			
			return result;
			
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	String[] result2={"-1"};
	    	return result2;
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	String[] result2={"-1"};
	    	return result2;
	    }
	}
	
	public String[] rechazarSolicitud(String userid, String friendid){
		
		String[] result = {"-1","-1"};
		
	    try {
	    	Properties prop = new Properties();
			prop.load(getClass().getResourceAsStream("server.properties"));
			String server = prop.getProperty("rejectsolicitudes");
			// Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(server);
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("IdUser", userid));
	        nameValuePairs.add(new BasicNameValuePair("IdSolicitud", friendid));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	      
	        //Obtengo el codigo de la respuesta http
	        int response_code = response.getStatusLine().getStatusCode();
	        result[0] = Integer.toString(response_code);
	        
	        if (response_code==200){
	        	
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		        String json = reader.readLine();
		        JSONTokener tokener = new JSONTokener(json);
		        try {
					JSONObject finalResult = new JSONObject(tokener);
					// procesar la respuesta
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  
			
	        }
			
			return result;
			
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	String[] result2={"-1"};
	    	return result2;
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	String[] result2={"-1"};
	    	return result2;
	    }
	}
	
	
	
	
	
	
	

}