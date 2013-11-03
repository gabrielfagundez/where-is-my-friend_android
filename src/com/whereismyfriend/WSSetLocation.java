package com.whereismyfriend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class WSSetLocation {

	public String[] llamarServer(String mail, String latitude, String longitude) {
		
	    // Create a new HttpClient and Post Header

	    try {
	    	Properties prop = new Properties();
			prop.load(getClass().getResourceAsStream("server.properties"));
			String server = prop.getProperty("setlocation");
	    	// Build the JSON object to pass parameters
	    	JSONObject jsonObj = new JSONObject();
	    	jsonObj.put("Mail", mail);
	    	jsonObj.put("Latitude", latitude);
	    	jsonObj.put("Longitude", longitude);
	    	// Create the POST object and add the parameters
	    	HttpPost httpPost = new HttpPost(server);
	    	StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
	    	entity.setContentType("application/json");
	    	httpPost.setEntity(entity);
	    	
	    	//Timeout
	    	HttpParams httpParameters = new BasicHttpParams();
	    	// Set the timeout in milliseconds until a connection is established.
	    	int timeoutConnection = 10000;
	    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
	    	// Set the default socket timeout (SO_TIMEOUT) 
	    	// in milliseconds which is the timeout for waiting for data.
	    	int timeoutSocket = 10000;
	    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

	    	DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
	    	BasicHttpResponse response = (BasicHttpResponse)  httpClient.execute(httpPost);
	        //Obtengo el código de la respuesta http
	        int response_code = response.getStatusLine().getStatusCode();
	        String  [] result={Integer.toString(response_code)};
	        return result;
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	        String  [] result={Integer.toString(-1)};
	        return result;
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        String  [] result={Integer.toString(-1)};
	        return result;
	    } catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
	        String  [] result={Integer.toString(-1)};
	        return result;
		}
	} 
}
