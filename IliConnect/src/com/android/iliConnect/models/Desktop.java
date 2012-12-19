package com.android.iliConnect.models;

import java.util.List;

import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;

public class Desktop extends PersistableObject {
	public List<Item> Item;
	
	public Desktop(){
		super.arrayModel = true;
	}
	
	@Override
	public void load() {
		try {
			super.deserialize(MainActivity.localDataProvider.remoteDataFileName);

		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.context, "Fehler beim Laden der Authentifizierungsparameter.", Toast.LENGTH_LONG);
			t.show();
		}

	}
}
