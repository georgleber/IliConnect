package com.android.iliConnect;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.iliConnect.dataproviders.DataDownloadThread;
import com.android.iliConnect.dataproviders.LocalDataProvider;
import com.android.iliConnect.dataproviders.RemoteDataProvider;

public class MainActivity extends Activity {

	public Context context;
	public static MainActivity instance;
	public static Activity currentActivity;
	public RemoteDataProvider remoteDataProvider;
	public ProgressDialog progressDialog;
	public LocalDataProvider localDataProvider;
	
	
	private final DataDownloadThread watchThread = new DataDownloadThread();
	private EditText etUrl;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		currentActivity = this;

		instance = this;

		if (context == null)
			context = getApplicationContext();

		// TODO: Login überspringen, falls Autologin, Auth vorhanden
		localDataProvider = LocalDataProvider.getInstance();
		localDataProvider.init(R.xml.config);
		localDataProvider.localdata.load();
		//localDataProvider.auth.load();
		//localDataProvider.settings.load();

		localDataProvider.updateLocalData();
		
		
		remoteDataProvider = new RemoteDataProvider();
		etUrl = (EditText) findViewById(R.id.urlText);
		etUrl.setText(localDataProvider.auth.url_src);

		Button login = (Button) findViewById(R.id.button1);

		login.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				EditText etUserID = (EditText) findViewById(R.id.editText1);
				EditText etPassword = (EditText) findViewById(R.id.editText2);

				try {

					if (!localDataProvider.auth.autologin)
						localDataProvider.auth.setLogin(true, etUserID.getText().toString(), etPassword.getText().toString(), etUrl.getText().toString());

					if (!watchThread.isAlive())
						watchThread.start();

					synchronized (remoteDataProvider.synchronizeObject) {
						if (!localDataProvider.isAvaiable)
							remoteDataProvider.synchronizeObject.wait();
						Intent i = new Intent(MainActivity.this, MainTabView.class);
						startActivity(i);
					}

				}

				catch (Exception ex) {
					Toast t = Toast.makeText(context, "Login fehlgeschlagen", Toast.LENGTH_LONG);
					t.show();
					return;
				}

			}
		});


	}
	
	public static void showNotification(String title, String text){
		Intent intent = new Intent(MainActivity.instance, MainTabView.class);
		PendingIntent pIntent = PendingIntent.getActivity(MainActivity.instance, 0, intent, 0);

		// Build notification
		Notification noti = new Notification.Builder(MainActivity.instance).setContentTitle(title).setContentText(text).setSmallIcon(android.R.drawable.ic_dialog_alert).setContentIntent(pIntent).build();
		NotificationManager notificationManager = (NotificationManager) MainActivity.instance.getSystemService(NOTIFICATION_SERVICE);
		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, noti);
	}
	
	
	private void showAlertMessage() {
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
		// alertDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
		//
		// public void onClick(DialogInterface dialog, int which) {
		// // TODO Auto-generated method stub
		// Toast.makeText(getApplicationContext(), "abbruch", Toast.LENGTH_LONG).show();
		// }
		// });
		AlertDialog alertDialog1 = alertDialog.create();
		alertDialog1.show();
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.activity_main, menu);
	// return true;
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

	public MainActivity getInstance() {
		return instance;
	}
	public void showBrowserContent(String url){
		
		Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(openUrlIntent);
		
	}
}
