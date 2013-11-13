package com.whereismyfriend;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class DialogPush extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog_push);
		Intent intent = getIntent();
		String alert= intent.getStringExtra("alert");
		String badge= intent.getStringExtra("badge");
		String mensaje;
		if (Integer.parseInt(badge)>1)
        	mensaje=getResources().getString(R.string.push_no_leidas_1)+ " "+ badge + " "+ getResources().getString(R.string.push_no_leidas_2);
		else
			mensaje=alert;
		TextView text = (TextView) findViewById(R.id.TextView01);
		text.setText(mensaje);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dialog_push, menu);
		return true;
	}

	public void ok (View view){
		if (Amigos.activ!=null)
			Amigos.activ.finish();
		if (Mapa.activ!=null)
			Mapa.activ.finish();
		if (Solicitudes.activ!=null)
			Solicitudes.activ.finish();
		
		Intent intent_name = new Intent();
		intent_name.setClass(getApplicationContext(),Solicitudes.class);
		startActivity(intent_name);
		finish();
	}
	
	public void cancel (View view){
		finish();
	}
}
