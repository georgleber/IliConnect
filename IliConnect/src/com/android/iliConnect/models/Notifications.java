package com.android.iliConnect.models;

import java.util.ArrayList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;

@Element
public class Notifications extends PersistableObject{

	@ElementList (name="Notifications")
	public ArrayList<Notification> Notifications;
	 
	public Notifications(){
		super.arrayModel = true;
	}
	
	@Override
	public void load() {
		try {
			super.deserialize(MainActivity.instance.localDataProvider.remoteDataFileName);

		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.instance, "Fehler bein laden der Authentifizierungsparameter.", Toast.LENGTH_LONG);
			t.show();
		}

	}
	
}
