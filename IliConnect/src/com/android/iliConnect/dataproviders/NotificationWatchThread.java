package com.android.iliConnect.dataproviders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import com.android.iliConnect.message.NotificationClickListener;
import com.android.iliConnect.models.Notification;
import com.android.iliConnect.models.Notifications;

public class NotificationWatchThread implements NotificationClickListener {
	public TimerTask doAsynchronousTask;
	private Timer timer = new Timer();
	public static NotificationWatchThread instance;
	public static int visibleMsgCount = 0;

	public void startTimer() {
		final Handler handler = new Handler();
		instance = this;

		doAsynchronousTask = new TimerTask() {

			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						try {
							showNotificationPopups();
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

	public void onMessageClose() {
		visibleMsgCount--;
	}

	private void showNotificationPopups() {
		// LocalDataProvider localDataProvider = MainActivity.instance.localDataProvider;
		Notifications notifications = MainActivity.instance.localDataProvider.notifications;// localDataProvider.notifications;

		int warning = MainActivity.instance.localDataProvider.settings.level_warning;
		int critical = MainActivity.instance.localDataProvider.settings.level_critical;

		ModificationHandler handler = new ModificationHandler();

		ArrayList<Notification> nextNotifications = notifications.Notifications;
		if (nextNotifications != null) {
			Collections.sort(nextNotifications, new NotificationComparator());

			int criticalNotificationCount = 0;
			int warningNotificationCount = 0;

			int count = 0;
			boolean stopCheck = false;
			for (Notification notification : nextNotifications) {
				if (!stopCheck) {
					if (!handler.isNotificationMarked(notification.getRef_id()) && !handler.isNotificationShown(notification.getRef_id())) {
						stopCheck = true;
						Date currentDate = new Date(System.currentTimeMillis());

						// FIXME: Workaround conversion PHP Timestamp to Java Timestamp
						Long date = Long.valueOf(notification.date) * 1000;
						Date notiDate = new Date(date);

						String title = "IliConnect: " + notification.getTitle();
						long daysBetween = TimeUnit.MILLISECONDS.toDays(notiDate.getTime() - currentDate.getTime());
						if (daysBetween <= critical) {
							criticalNotificationCount++;

							if (visibleMsgCount == count) {
								String messageText = "Termin " + notification.getTitle() + " endet " + notification.getDate() + " Uhr";
								MessageBuilder.critical_message(MainTabView.instance, messageText, this);

								handler.setNotificationShown(notification.getRef_id());
								visibleMsgCount++;
								count++;
							}

							stopCheck = false;
						} else if (daysBetween <= warning) {
							warningNotificationCount++;

							if (visibleMsgCount == count) {
								String messageText = "Termin " + notification.getTitle() + " endet " + notification.getDate() + " Uhr";
								MessageBuilder.warning_message(MainTabView.instance, messageText, this);

								handler.setNotificationShown(notification.getRef_id());
								visibleMsgCount++;
								count++;
							}

							stopCheck = false;
						}
					}
				} else {
					break;
				}
			}

			if (criticalNotificationCount > 0) {
				AndroidNotificationBuilder notiBuilder = new AndroidNotificationBuilder(criticalNotificationCount, AndroidNotificationBuilder.STATUS_CRITICAL);
				notiBuilder.showNotification();
			}

			if (warningNotificationCount > 0) {
				AndroidNotificationBuilder notiBuilder = new AndroidNotificationBuilder(warningNotificationCount, AndroidNotificationBuilder.STATUS_WARNING);
				notiBuilder.showNotification();
			}
		}
	}
}
