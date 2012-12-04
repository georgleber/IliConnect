package com.android.iliConnect;

import java.util.ArrayList;
import java.util.List;

import com.android.iliConnect.dataproviders.DataDownloadThread;
import com.android.iliConnect.dataproviders.LocalDataProvider;
import com.android.iliConnect.dataproviders.RemoteDataProvider;
import com.android.iliConnect.models.Authentification;
import com.example.ilias.R;


import android.widget.AdapterView.OnItemClickListener;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

	public static Context context;
	public static Activity activity;
	public static ProgressDialog progressDialog;
	public static RemoteDataProvider remoteDataProvider;
	public static LocalDataProvider localDataProvider;

	
    @SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi", "NewApi" })
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

		if (context == null)
			context = getApplicationContext();

		//TODO: Login überspringen, falls Autologin, Auth vorhanden 
		localDataProvider = LocalDataProvider.getInstance();
				
		//remoteDataProvider = new RemoteDataProvider();
		progressDialog = new ProgressDialog(activity);
		progressDialog.setMessage("Lade Daten");
		progressDialog.setIndeterminate(false);
		progressDialog.setMax(100);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        Button testalert = (Button) findViewById(R.id.testmessagebutton);
        testalert.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showAlertMessage();
				
				
			}
		});
        
        Button login = (Button) findViewById(R.id.button1);
        login.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {				
				
				EditText etUserID = (EditText) findViewById(R.id.editText1);
				EditText etPassword = (EditText) findViewById(R.id.editText2);

				DataDownloadThread watchThread = new DataDownloadThread();
				localDataProvider.auth = new Authentification(true, etUserID.getText().toString(), etPassword.getText().toString());
				localDataProvider.readConfig();
				
				Intent i = new Intent(MainActivity.this, MainTabView.class);				  
				startActivity(i);
				
				}
        });
        
        Intent intent = new Intent(this, MainTabView.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Build notification        
        Notification noti = new Notification.Builder(this)
            .setContentTitle("Kritisch")
            .setContentText("Termin SWE Praktikum läuft am 23.11.2012 ab").setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        
        notificationManager.notify(0, noti);
	        
        
        
}
    private void showAlertMessage(){
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
    	alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
    	alertDialog.setTitle("QR Code nicht erkannt!");
    	alertDialog.setMessage("Der eingescannte QR-Code ist ungültig.");
    	alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Anmeldung", Toast.LENGTH_LONG).show();
			}
		});
//    	alertDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
//			
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "abbruch", Toast.LENGTH_LONG).show();
//			}
//		});
    	AlertDialog alertDialog1= alertDialog.create();
    	alertDialog1.show();
    }  

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    getMenuInflater().inflate(R.menu.activity_main, menu);
    //    return true;
   // }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
