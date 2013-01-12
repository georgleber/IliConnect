package com.android.iliConnect.dataproviders;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.os.Handler;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.handler.AndroidNotificationBuilder;
import com.android.iliConnect.models.Notification;
import com.android.iliConnect.models.Notifications;
import com.android.iliConnect.models.modification.AppData;
import com.android.iliConnect.models.modification.NotificationData;
import com.android.iliConnect.models.modification.NotificationItem;

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
								if (!isNotificationMarked(notification.getRef_id())) {
									Date currentDate = new Date(System.currentTimeMillis());
									Date notiDate = new Date(notification.date);

									String title = "IliConnect - Bevorstehender Termin";
									long daysBetween = TimeUnit.MILLISECONDS.toDays(notiDate.getTime() - currentDate.getTime());
									if (daysBetween <= critical) {
										String notificationText = "Termin " + notification.getTitle() + " läuft am " + notification.getDate() + " ab";

										AndroidNotificationBuilder notiBuilder = new AndroidNotificationBuilder(title, notificationText, AndroidNotificationBuilder.STATUS_CRITICAL);
										notiBuilder.showNotification();
									} else if (daysBetween <= warning) {
										String notificationText = "Termin " + notification.getTitle() + " läuft am " + notification.getDate() + " ab";
										AndroidNotificationBuilder notiBuilder = new AndroidNotificationBuilder(title, notificationText, AndroidNotificationBuilder.STATUS_WARNING);
										notiBuilder.showNotification();
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

	private boolean isNotificationMarked(String ref_id) {
		AppData appData = MainActivity.instance.localDataProvider.appData;
		if (appData.getNotificationData() != null) {
			NotificationItem item = null;
			for (NotificationItem notiItem : appData.getNotificationData().NotificationItems) {
				if (ref_id.equals(notiItem.getRef_id())) {
					item = notiItem;
					break;
				}
			}

			if (item != null) {
				return item.isMarkedRead();
			}
		}

		return false;
	}
}
