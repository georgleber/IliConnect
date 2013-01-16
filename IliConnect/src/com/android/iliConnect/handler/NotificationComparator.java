package com.android.iliConnect.handler;

import java.util.Comparator;

import com.android.iliConnect.models.Notification;

public class NotificationComparator implements Comparator<Notification> {
	private boolean ascending = true;

	public NotificationComparator() {
		this(true);
	}

	public NotificationComparator(boolean ascending) {
		this.ascending = ascending;
	}

	public int compare(Notification first, Notification second) {
		if (ascending) {
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
		} else {
			if (first.date == null && second.date == null) {
				return 0;
			}

			if (first.date == null) {
				return -1;
			}

			if (second.date == null) {
				return 1;
			}

			return second.date.compareTo(first.date);
		}
	}
}