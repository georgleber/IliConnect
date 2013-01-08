package com.android.iliConnect.dataproviders;

import static android.content.Context.NOTIFICATION_SERVICE;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat.Builder;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.MainTabView;
import com.android.iliConnect.R;
import com.android.iliConnect.models.Notification;
import com.android.iliConnect.models.Notifications;

public class NotificationWatchThread {
	public TimerTask doAsynchronousTask;
	private Timer timer = new Timer();

	public void startTimer() {
		final Handler handler = new Handler();

		doAsynchronousTask = new TimerTask() {

			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						try {
							LocalDataProvider localDataProvider = MainActivity.instance.localDataProvider;
							Notifications notifications = localDataProvider.notifications;

							int warning = MainActivity.instance.localDataProvider.settings.level_warning;
							int critical = MainActivity.instance.localDataProvider.settings.level_critical;

							for (Notification notification : notifications.Notifications) {
								if (!notification.isMarked()) {
									Date currentDate = new Date(System.currentTimeMillis());
									Date notiDate = notification.date;

									long daysBetween = TimeUnit.MILLISECONDS.toDays(notiDate.getTime() - currentDate.getTime());
									if (daysBetween <= critical) {
										String notificationText = "Termin " + notification.getTitle() + " läuft am " + notification.getDate() + " ab";
										createNotification(false, notificationText);
									} else if (daysBetween <= warning) {
										String notificationText = "Termin " + notification.getTitle() + " läuft am " + notification.getDate() + " ab";
										createNotification(true, notificationText);
									}
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

	private void createNotification(boolean warning, String notificationText) {
		Intent intent = new Intent(MainActivity.instance, MainTabView.class);
		PendingIntent pIntent = PendingIntent.getActivity(MainActivity.instance, 0, intent, 0);

		Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(MainActivity.instance);
		if (warning) {
			notificationBuilder.setContentTitle("Warnung");
		} else {
			notificationBuilder.setContentTitle("Kritisch");
		}
		
		android.app.Notification notification = null;
		if (warning) {
			notification = notificationBuilder.setContentText(notificationText).setSmallIcon(R.drawable.warn).setContentIntent(pIntent).build();
		} else {
			notification = notificationBuilder.setContentText(notificationText).setSmallIcon(R.drawable.error).setContentIntent(pIntent).build();
		}

		NotificationManager notificationManager = (NotificationManager) MainActivity.instance.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);
	}
}
