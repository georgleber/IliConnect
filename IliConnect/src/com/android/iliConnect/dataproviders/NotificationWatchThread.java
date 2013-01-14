package com.android.iliConnect.dataproviders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.os.Handler;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.MainTabView;
import com.android.iliConnect.MessageBuilder;
import com.android.iliConnect.handler.AndroidNotificationBuilder;
import com.android.iliConnect.handler.ModificationHandler;
import com.android.iliConnect.handler.NotificationComparator;
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

							ModificationHandler handler = new ModificationHandler();
							
							ArrayList<Notification> nextNotifications = notifications.Notifications;
							Collections.sort(nextNotifications, new NotificationComparator(false));
							
							for (Notification notification : nextNotifications) {
								if (!handler.isNotificationMarked(notification.getRef_id())) {
									Date currentDate = new Date(System.currentTimeMillis());

									// FIXME: Workaround conversion PHP Timestamp to Java Timestamp
									Long date = Long.valueOf(notification.date) * 1000;
									Date notiDate = new Date(date);

									String title = "IliConnect: " + notification.getTitle();
									long daysBetween = TimeUnit.MILLISECONDS.toDays(notiDate.getTime() - currentDate.getTime());
									if (daysBetween <= critical) {
										String notificationText = "Frist endet " + notification.getDate() + "Uhr";

										AndroidNotificationBuilder notiBuilder = new AndroidNotificationBuilder(title, notificationText, AndroidNotificationBuilder.STATUS_CRITICAL);
										notiBuilder.showNotification(notiDate.getDate());

										MessageBuilder.critical_message(MainTabView.instance, notificationText);
									} else if (daysBetween <= warning) {
										String notificationText = "Frist endet " + notification.getDate() + "Uhr";
										AndroidNotificationBuilder notiBuilder = new AndroidNotificationBuilder(title, notificationText, AndroidNotificationBuilder.STATUS_WARNING);
										notiBuilder.showNotification(notiDate.getDate());

										MessageBuilder.warning_message(MainTabView.instance, notificationText);
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
}
