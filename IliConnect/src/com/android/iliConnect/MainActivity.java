package com.android.iliConnect;

import java.io.File;
import java.util.Date;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.iliConnect.dataproviders.DataDownloadThread;
import com.android.iliConnect.dataproviders.LocalDataProvider;
import com.android.iliConnect.dataproviders.RemoteDataProvider;
import com.android.iliConnect.models.Authentification;

public class MainActivity extends Activity {

	public static MainActivity instance;
	public static Activity currentActivity;
	public RemoteDataProvider remoteDataProvider;
	public ProgressDialog progressDialog;
	public static Object syncObject = new Object();
	public LocalDataProvider localDataProvider;

	public DataDownloadThread watchThread = new DataDownloadThread();
	private EditText etUrl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		currentActivity = this;

		instance = this;

		localDataProvider = LocalDataProvider.getInstance();
		localDataProvider.init(R.xml.config);
		
		localDataProvider.localdata.load();
		localDataProvider.auth = localDataProvider.localdata.Static.auth;
		localDataProvider.settings = localDataProvider.localdata.Static.settings;

		//localDataProvider.updateLocalData();

		remoteDataProvider = new RemoteDataProvider();
		
		

		View login = findViewById(R.id.button1);
		
		EditText etUserID = (EditText) findViewById(R.id.editText1);
		EditText etPassword = (EditText) findViewById(R.id.editText2);
		etUrl = (EditText) findViewById(R.id.urlText);

		if(!localDataProvider.auth.user_id.equals("")) 
			etUserID.setText(localDataProvider.auth.user_id);
		if(!localDataProvider.auth.user_id.equals("")) 
			etPassword.setText(localDataProvider.auth.password);
		if(!localDataProvider.auth.url_src.equals("")) 
			etUrl.setText(localDataProvider.auth.url_src);
		

		if(localDataProvider.auth.autologin)
			login();
		
		login.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				EditText etUserID = (EditText) findViewById(R.id.editText1);
				localDataProvider.auth.user_id = etUserID.getText().toString();
				EditText etPassword = (EditText) findViewById(R.id.editText2);
				localDataProvider.auth.password = etPassword.getText().toString();
				
				// Prüfen, ob / an Url-Ende vorhaden und ggf. hinzufügen
				String url = etUrl.getText().toString();
				if(!url.endsWith("/")) {
					url = url + "/";		
				}
				localDataProvider.auth.url_src = url;
				localDataProvider.auth.setLogin(true, etUserID.getText().toString(), etPassword.getText().toString(), url);
				localDataProvider.localdata.save();
				login();

			}
		});

		showNotification("Titel", "Text");
	}

	public static void showNotification(String title, String text) {
		Intent intent = new Intent(MainActivity.instance, MainTabView.class);
		PendingIntent pIntent = PendingIntent.getActivity(MainActivity.instance, 0, intent, 0);

		Builder noti = new android.support.v4.app.NotificationCompat.Builder(instance);
		noti.setContentTitle(title).setContentText(text).setSmallIcon(android.R.drawable.ic_dialog_alert).setContentIntent(pIntent);

		NotificationManager notificationManager = (NotificationManager) MainActivity.instance.getSystemService(NOTIFICATION_SERVICE);

		notificationManager.notify(0, noti.build());

	}

	public MainActivity getInstance() {
		return instance;
	}

	public void showBrowserContent(String url) {

		Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(openUrlIntent);

	}

	public void sync(Context context) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("Sync");
		progressDialog.setMessage("Bitte warten...");

		remoteDataProvider = new RemoteDataProvider(progressDialog);
		remoteDataProvider.execute(MainActivity.instance.localDataProvider.remoteData.getSyncUrl() + "?action=sync");
	}

	public void showToast(final String msg) {
				Toast t = Toast.makeText(MainActivity.instance, msg, Toast.LENGTH_LONG);
				t.show();		
		
	}

	public void logout() {
		if (MainTabView.instance != null)
			MainTabView.instance.finish();

		localDataProvider.remoteData.delete();

		remoteDataProvider.cancel(true);
		LocalDataProvider.isAvaiable = false;

		if (watchThread.doAsynchronousTask != null) {
			watchThread.doAsynchronousTask.cancel();
			watchThread.doAsynchronousTask = null;
		}

	}

	public void login() {
		final File remoteDataFile = new File(MainActivity.instance.getFilesDir() + "/" + localDataProvider.remoteDataFileName);

		if (remoteDataFile.exists())
			remoteDataFile.delete();

		MainActivity.instance.sync(MainActivity.instance);

		new Thread(new Runnable() {

			public void run() {
				synchronized (syncObject) {
					Date start = new Date();
					long timeout = 5000;

					while (!remoteDataFile.exists() || !LocalDataProvider.isAvaiable) {
						try {
							syncObject.wait(2000);
						} catch (InterruptedException e) {
						}
						if (new Date().getTime() - start.getTime() > timeout)
							break;
					}
					if (remoteDataFile.exists()) {
						Intent i = new Intent(MainActivity.this, MainTabView.class);
						startActivity(i);

						MainActivity.instance.runOnUiThread(new Runnable() {

							public void run() {
								if (watchThread.doAsynchronousTask == null)
									watchThread.startTimer();
							}
						});

					}
				}
			}
		}).start();
	}
	
	@Override
	protected void onRestart() {
		EditText etUserID = (EditText) findViewById(R.id.editText1);
		EditText etPassword = (EditText) findViewById(R.id.editText2);
		EditText etUrl = (EditText) findViewById(R.id.urlText);
		
		// Bei Ausloggen Textfelder zurücksetzen
		etUserID.setText("");
		etPassword.setText("");
		etUrl.setText(localDataProvider.auth.url_src);

		// Instance von MainTabView überschreiben, da sonst IllegalStateExeption bei Update der View auftitt.
		MainTabView.instance = null;
		super.onRestart();
	}

}
