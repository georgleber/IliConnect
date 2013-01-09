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
			for (Notification notification : loadedNotifications) {
				if (notification.date != null && cnt <= numNotifications) {
					if (isFutureNotification(notification)) {
						notifications.add(notification);
					}

					cnt++;
				}
			}
		}

		return notifications;
	}

	private boolean isFutureNotification(Notification notification) {
		Calendar notiDate = new GregorianCalendar();
		// notiDate.set(notification.date.getYear(), notification.date.getMonth(), notification.date.getDay());

		Date todayDate = new Date();
		Calendar today = new GregorianCalendar();
		today.set(todayDate.getYear(), todayDate.getMonth(), todayDate.getDay());

		if (notiDate.after(today)) {
			return true;
		}

		return false;
	}

	private class NotificationComparator implements Comparator<Notification> {
		public int compare(Notification first, Notification second) {
			if (first.date == null && second.date == null) {
				return 0;
			}

			if (first.date == null) {
				return 1;
			}

			if (second.date == null) {
				return -1;
			}

			return first.date.compareTo(second.date);
		}
	}
}
