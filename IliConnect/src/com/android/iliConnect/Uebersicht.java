package com.android.iliConnect;

import java.util.List;

import com.android.iliConnect.models.Notification;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Uebersicht extends ListFragment implements Redrawable{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
		View v = inflater.inflate(R.layout.uebersicht_layout, container, false);
		
		// Notifications in Abhängigkeit der in den Einstellungen angegeben Anzahl auslesen
		List<Notification> items = this.getNotificationsToShow();
		NoteArrayAdapter noteAdapter = new NoteArrayAdapter(MainActivity.currentActivity, R.layout.noteitem, items);
	
		setListAdapter(noteAdapter);

		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	
	public void refreshViews() {
		getListView().invalidateViews();
		// Notifications in Abhängigkeit der in den Einstellungen angegeben Anzahl auslesen
		List<Notification> items = this.getNotificationsToShow();
		NoteArrayAdapter noteAdapter = new NoteArrayAdapter(MainActivity.currentActivity, R.layout.noteitem, items);
		setListAdapter(noteAdapter);	
	}
	
	
	
	private List<Notification> getNotificationsToShow() {
		// Eingestellte Anzahl der Termine 
		int numNotifications = MainActivity.instance.localDataProvider.settings.num_notifications;
		// Insgesamt vorhandene Termine
		int totalNumOfNotifications = MainActivity.instance.localDataProvider.notifications.Notifications.size();
		
		List<Notification> allNotis = MainActivity.instance.localDataProvider.notifications.Notifications;

		if(numNotifications < totalNumOfNotifications) {
			return allNotis.subList(0, numNotifications);
		}
		return allNotis;
	}
}
