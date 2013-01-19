package com.android.iliConnect.models.modification;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;

@Element
public class NotificationData extends PersistableObject {
	@ElementList(required = false)
	public List<NotificationItem> NotificationItems;
	
	@ElementList(required = false)
	public List<NotificationMessage> NotificationMessages;

	@Override
	public void load() {
		try {
			super.deserialize(MainActivity.instance.localDataProvider.appDataFileName);

		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.instance, "Fehler bein laden der Applikations-Daten.", Toast.LENGTH_LONG);
			t.show();
		}
	}
}
