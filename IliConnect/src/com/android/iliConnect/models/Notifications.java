package com.android.iliConnect.models;

import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;

public class Notifications extends PersistableObject{
	public ArrayList<Notification> Notifications = new ArrayList<Notification>();
	 
	public Notifications(){
		super.arrayModel = true;
	}
	
	@Override
	public void load() {
		try {
			super.deserialize(MainActivity.localDataProvider.remoteDataFileName);

		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.context, "Fehler bein laden der Authentifizierungsparameter.", Toast.LENGTH_LONG);
			t.show();
		}

	}
	
}
