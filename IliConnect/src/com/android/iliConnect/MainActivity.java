package com.android.iliConnect;

import java.util.Date;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

public class MainActivity extends Activity {

	public static MainActivity instance;
	public static Activity currentActivity;
	public RemoteDataProvider remoteDataProvider;

	public LocalDataProvider localDataProvider;

	public DataDownloadThread watchThread = new DataDownloadThread();
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
		localDataProvider.auth = localDataProvider.localdata.Static.auth;
		localDataProvider.settings = localDataProvider.localdata.Static.settings;

		localDataProvider.updateLocalData();

		remoteDataProvider = new RemoteDataProvider();
		etUrl = (EditText) findViewById(R.id.urlText);
		etUrl.setText(localDataProvider.auth.url_src);

		View login = findViewById(R.id.button1);

		login.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				EditText etUserID = (EditText) findViewById(R.id.editText1);
				localDataProvider.auth.user_id = etUserID.getText().toString();
				EditText etPassword = (EditText) findViewById(R.id.editText2);
				localDataProvider.auth.password = etPassword.getText().toString();

				try {

					if (!localDataProvider.auth.autologin)
						localDataProvider.auth.setLogin(true, etUserID.getText().toString(), etPassword.getText().toString(), etUrl.getText().toString());

					if (watchThread.doAsynchronousTask == null)
						watchThread.start();

					int timeout = 5000;
					Date start = new Date();
					synchronized (MainActivity.instance.localDataProvider.syncObject) {
						try {
							while (!localDataProvider.isAvaiable) {
								if (new Date().getTime() - start.getTime() > timeout)
									throw new InterruptedException();
								MainActivity.instance.localDataProvider.syncObject.wait(1000);

							}
							Intent i = new Intent(MainActivity.this, MainTabView.class);
							startActivity(i);
						} catch (InterruptedException e) {
							Toast t = Toast.makeText(instance, "Login fehlgeschlagen", Toast.LENGTH_LONG);
							t.show();

						}
					}

				}

				catch (Exception ex) {
					Toast t = Toast.makeText(instance, "Login fehlgeschlagen", Toast.LENGTH_LONG);
					t.show();

					return;
				}

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
		// Hide the notification after its selected
		// noti.flags |= android.app.Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, noti.build());

	}

	public MainActivity getInstance() {
		return instance;
	}

	public void showBrowserContent(String url) {

		Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(openUrlIntent);

	}
}
