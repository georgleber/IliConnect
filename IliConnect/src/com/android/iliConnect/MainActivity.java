package com.android.iliConnect;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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

					try {

						File remoteDataFile = new File(MainActivity.instance.getFilesDir() + "/" + localDataProvider.remoteDataFileName);

						if (!remoteDataFile.exists())
							MainActivity.instance.sync(MainActivity.instance);

						Date start = new Date();
						long timeout = 4000;

						synchronized (syncObject) {
							while (!remoteDataFile.exists()) {
								syncObject.wait(100);
								if (new Date().getTime() - start.getTime() > timeout)
									throw new InterruptedException();

							}
						}

						// while (!remoteDataFile.exists()) {
						// if (new Date().getTime() - start.getTime() > timeout)
						// throw new InterruptedException();
						// Thread.sleep(100);
						// }

						if (watchThread.doAsynchronousTask == null)
							watchThread.startTimer();

						Intent i = new Intent(MainActivity.this, MainTabView.class);
						startActivity(i);
					} catch (Exception e) {
						Toast t = Toast.makeText(instance, "Login fehlgeschlagen", Toast.LENGTH_LONG);
						t.show();

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

	public void sync(Context context) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("Sync");
		progressDialog.setMessage("Bitte warten...");

		RemoteDataProvider rP = new RemoteDataProvider(progressDialog);
		rP.execute(MainActivity.instance.localDataProvider.remoteData.getSyncUrl() + "?action=sync");
	}

	public void showToast(final String msg) {
		Toast t = Toast.makeText(MainActivity.instance, msg, Toast.LENGTH_LONG);
		t.show();

	}

}
