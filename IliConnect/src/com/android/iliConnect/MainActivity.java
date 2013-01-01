package com.android.iliConnect;



import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.iliConnect.dataproviders.DataDownloadThread;
import com.android.iliConnect.dataproviders.LocalDataProvider;
import com.android.iliConnect.dataproviders.RemoteDataProvider;
import com.android.iliConnect.models.Notification;

public class MainActivity extends Activity {

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

		View login =  findViewById(R.id.button1);
		

		login.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				EditText etUserID = (EditText) findViewById(R.id.editText1);
				EditText etPassword = (EditText) findViewById(R.id.editText2);

				DataDownloadThread watchThread = new DataDownloadThread();
				
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
					Toast t = Toast.makeText(instance, "Login fehlgeschlagen", Toast.LENGTH_LONG);
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
/*<<<<<<< HEAD
		Notification noti = new Notification.Builder(MainActivity.instance).setContentTitle(title).setContentText(text).setSmallIcon(android.R.drawable.ic_dialog_alert).setContentIntent(pIntent).build();
		NotificationManager notificationManager = (NotificationManager) MainActivity.instance.getSystemService(NOTIFICATION_SERVICE);
=======
		//Notification noti = new Notification.Builder(this).setContentTitle("Kritisch").setContentText("Termin SWE Praktikum läuft am 23.11.2012 ab").setSmallIcon(android.R.drawable.ic_dialog_alert).setContentIntent(pIntent).build();
		//NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
>>>>>>> branch 'master' of https://github.com/Develman/IliConnect.git
		// Hide the notification after its selected
		//noti.flags |= Notification.FLAG_AUTO_CANCEL;

<<<<<<< HEAD
		notificationManager.notify(0, noti);
=======
		//notificationManager.notify(0, noti);

>>>>>>> branch 'master' of https://github.com/Develman/IliConnect.git*/
	}
	
	



	public MainActivity getInstance() {
		return instance;
	}
	public void showBrowserContent(String url){
		
		Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(openUrlIntent);
		
	}
}
