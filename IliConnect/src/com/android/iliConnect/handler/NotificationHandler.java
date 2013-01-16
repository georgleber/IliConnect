package com.android.iliConnect.handler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.models.Notification;

public class NotificationHandler {
	public List<Notification> loadNotifications(boolean checkSettings) {
		List<Notification> loadedNotifications = MainActivity.instance.localDataProvider.notifications.Notifications;
		List<Notification> notifications = new ArrayList<Notification>();

		if (loadedNotifications != null) {
			Collections.sort(loadedNotifications, new NotificationComparator());

			// Eingestellte Anzahl der Termine
			int numNotifications = loadedNotifications.size();
			if (checkSettings) {
				numNotifications = MainActivity.instance.localDataProvider.settings.num_notifications;
			}

			int cnt = 0;
			ModificationHandler handler = new ModificationHandler();
			for (Notification notification : loadedNotifications) {
				if (notification.date != null && cnt < numNotifications) {
					if (isFutureNotification(notification)) {
						if (checkSettings) {
							if (!handler.isNotificationMarked(notification.getRef_id())) {
								notifications.add(notification);
							} else {
								cnt--;
							}
						} else {
							notifications.add(notification);
						}
					}

					cnt++;
				}
			}
		}

		return notifications;
	}

	private boolean isFutureNotification(Notification notification) {
		// FIXME: Workaround conversion PHP Timestamp to Java Timestamp
		Long date = Long.valueOf(notification.date) * 1000;
		Date notiDate = new Date(date);
		
		Calendar notiCal = new GregorianCalendar();
		notiCal.set(notiDate.getYear(), notiDate.getMonth(), notiDate.getDay());

		Date todayDate = new Date();
		Calendar today = new GregorianCalendar();
		today.set(todayDate.getYear(), todayDate.getMonth(), todayDate.getDay());

		if (notiCal.after(today)) {
			return true;
		}

		return true;
	}
}
