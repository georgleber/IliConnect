package com.android.iliConnect.models.modification;

import org.simpleframework.xml.Element;

import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;

@Element
public class NotificationMessage extends PersistableObject {
	@Element
	private String ref_id;

	@Override
	public void load() {
		try {
			super.deserialize(MainActivity.instance.localDataProvider.appDataFileName);

		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.instance, "Fehler bein laden der Applikations-Daten.", Toast.LENGTH_LONG);
			t.show();
		}
	}

	public String getRef_id() {
		return ref_id;
	}

	public void setRef_id(String ref_id) {
		this.ref_id = ref_id;
	}
}
