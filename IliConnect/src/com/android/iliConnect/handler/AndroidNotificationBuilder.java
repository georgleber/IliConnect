package com.android.iliConnect.handler;

import static android.content.Context.NOTIFICATION_SERVICE;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.MainTabView;
import com.android.iliConnect.R;

public class AndroidNotificationBuilder {
	public static final int STATUS_NORMAL = 0;
	public static final int STATUS_WARNING = 1;
	public static final int STATUS_CRITICAL = 2;

	private String title;
	private String text;
	private int status;

	public AndroidNotificationBuilder(String title, String text) {
		this(title, text, STATUS_NORMAL);
	}

	public AndroidNotificationBuilder(String title, String text, int status) {
		this.title = title;
		this.text = text;

		this.status = status;
	}

	public void showNotification() {
		Intent intent = new Intent(MainActivity.instance, MainTabView.class);
		PendingIntent pIntent = PendingIntent.getActivity(MainActivity.instance, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(MainActivity.instance);

		int icon = STATUS_NORMAL;
		switch (status) {
		case STATUS_NORMAL:
			icon = android.R.drawable.ic_dialog_alert;
			break;

		case STATUS_WARNING:
			icon = R.drawable.warn;
			break;

		case STATUS_CRITICAL:
			icon = R.drawable.error;
			break;
		}

		android.app.Notification notification = notificationBuilder.setContentTitle(title).setContentText(text).setSmallIcon(icon).setContentIntent(pIntent).setAutoCancel(true).build();

		NotificationManager notificationManager = (NotificationManager) MainActivity.instance.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);
	}

	public static void cancelNotification() {
		NotificationManager notificationManager = (NotificationManager) MainActivity.instance.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
	}
}
