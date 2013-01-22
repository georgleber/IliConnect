package com.android.iliConnect.handler;

import static android.content.Context.NOTIFICATION_SERVICE;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.MainTabView;
import com.android.iliConnect.R;

public class AndroidNotificationBuilder {
	public static final int STATUS_WARNING = 0;
	public static final int STATUS_CRITICAL = 1;

	private int notificationCount;
	private int status;

	public AndroidNotificationBuilder(int notificationCount, int status) {
		this.notificationCount = notificationCount;
		this.status = status;
	}

	public void showNotification() {
		Intent intent = new Intent(MainActivity.instance, MainTabView.class);
		PendingIntent pIntent = PendingIntent.getActivity(MainActivity.instance, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(MainActivity.instance);

		String text = "";
		int icon = STATUS_WARNING;
		switch (status) {
		case STATUS_WARNING:
			text = notificationCount == 1 ? "Warnung: 1 Termin steht bevor." : "Warnung: " + notificationCount + " Termine stehen bevor.";
			icon = R.drawable.warn;
			break;

		case STATUS_CRITICAL:
			text = notificationCount == 1 ? "Kritisch: 1 Termin steht bevor." : "Kritisch: " + notificationCount + " Termine stehen bevor.";
			icon = R.drawable.error;
			break;
		}

		android.app.Notification notification = notificationBuilder.setContentTitle("IliConnect - bevorstehende Termine").setContentText(text).setSmallIcon(icon).setContentIntent(pIntent).build();
		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		NotificationManager notificationManager = (NotificationManager) MainActivity.instance.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(status, notification);
	}

	public static void cancelNotification() {
		NotificationManager notificationManager = (NotificationManager) MainActivity.instance.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(STATUS_WARNING);
		notificationManager.cancel(STATUS_CRITICAL);
	}
}
