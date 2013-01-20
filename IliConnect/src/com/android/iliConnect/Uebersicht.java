package com.android.iliConnect;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.iliConnect.handler.NotificationHandler;
import com.android.iliConnect.models.Notification;

public class Uebersicht extends Fragment implements Redrawable {
	
	ListView lv1;
	ListView lv2;
	
	
	private NotificationHandler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.uebersicht_layout, container, false);
		
		List<Notification> items = handler.loadNotifications(true);
		lv1 = (ListView) v.findViewById(R.id.listNotifications);
		lv1.setSelector( R.drawable.list_selector);
		if(items.size() == 0) {
			Notification empty = new Notification();
			empty.title = "Kein Termin vorhanden!";
			items.add(empty);
		}
		NoteArrayAdapter noteAdapter = new NoteArrayAdapter(MainActivity.currentActivity, R.layout.noteitem, items);
		lv1.setAdapter(noteAdapter);
		
		lv2 =(ListView) v.findViewById(R.id.listViewNewFiles);
		lv2.setSelector( R.drawable.list_selector);
		DesktopDetailArrayAdapter fileAdapter = new DesktopDetailArrayAdapter(MainActivity.instance, R.layout.item, MainActivity.instance.localDataProvider.remoteData.Current.Desktop.ChangedFiles);
		lv2.setAdapter(fileAdapter);
	
		
		
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

		handler = new NotificationHandler();
	}

	public void refreshViews() {
		getView().invalidate();
		
		// Notifications in Abhängigkeit der in den Einstellungen angegeben Anzahl auslesen
		List<Notification> items = handler.loadNotifications(true);
		if(items.size() == 0) {
			Notification empty = new Notification();
			empty.title = "Kein Termin vorhanden!";
			items.add(empty);
		}
		NoteArrayAdapter noteAdapter = new NoteArrayAdapter(MainActivity.currentActivity, R.layout.noteitem, items);
		DesktopDetailArrayAdapter fileAdapter = new DesktopDetailArrayAdapter(MainActivity.instance, R.layout.item, MainActivity.instance.localDataProvider.remoteData.Current.Desktop.ChangedFiles);
		lv1.setAdapter(noteAdapter);
		lv1.setSelector( R.drawable.list_selector);
		lv2.setSelector( R.drawable.list_selector);
		lv2.setAdapter(fileAdapter);
		
	}
}
