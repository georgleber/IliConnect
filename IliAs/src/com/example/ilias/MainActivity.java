package com.example.ilias;

import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
				Intent i = new Intent(MainActivity.this, MainTabView.class);				  
				startActivity(i);
				
				}
        });
        
        Intent intent = new Intent(this, MainTabView.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Build notification
        // Actions are just fake
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
    	alertDialog.setIcon(android.R.drawable.ic_dialog_info);
    	alertDialog.setTitle("Kursanmeldung!");
    	alertDialog.setMessage("Wollen Sie sich zu dem Kurs: SWE anmelden?");
    	alertDialog.setPositiveButton("Anmelden", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Anmeldung", Toast.LENGTH_LONG).show();
			}
		});
    	alertDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "abbruch", Toast.LENGTH_LONG).show();
			}
		});
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
