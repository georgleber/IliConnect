package com.android.iliConnect.dataproviders;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.android.iliConnect.MainActivity;

public class DataDownloadThread {

	public TimerTask doAsynchronousTask;
	private Timer timer = new Timer();
	private static boolean startNotificationThread = false;

	public void startTimer() {
		final Handler handler = new Handler();

		doAsynchronousTask = new TimerTask() {

			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						try {
							if (MainActivity.instance.localDataProvider.settings.sync) {
								MainActivity.instance.remoteDataProvider = new RemoteDataProvider();

								ConnectivityManager cM = (ConnectivityManager) MainActivity.instance.getSystemService(Context.CONNECTIVITY_SERVICE);
								NetworkInfo nInfo = cM.getActiveNetworkInfo();
								// WIFI-only sync ?
								if (!MainActivity.instance.localDataProvider.settings.sync_wlanonly || nInfo.getType() == ConnectivityManager.TYPE_WIFI) {
									MainActivity.instance.remoteDataProvider.execute(MainActivity.instance.localDataProvider.remoteData.getSyncUrl() + "?action=sync");
								}

								// Notification-Thread erst beim zweiten Sync-Lauf starten
								if (startNotificationThread == true) {
									if (MainActivity.instance.notificationThread.doAsynchronousTask == null) {
										MainActivity.instance.notificationThread.startTimer();
									}
								} else {
									startNotificationThread = true;
								}

							}
						} catch (Exception e) {

						}
					}
				});
			}
		};
		start();
	}

	public void start() {
		timer.schedule(doAsynchronousTask, 0, MainActivity.instance.localDataProvider.settings.interval * 60 * 1000);
	}

}
