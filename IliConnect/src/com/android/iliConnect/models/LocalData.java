package com.android.iliConnect.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;

@Root(name="Iliconnect")
public class LocalData extends PersistableObject {
	@Element
	public Static Static; 


	@Override
	public void load() {
		try {
			super.deserialize(MainActivity.instance.localDataProvider.localDataFilename);

		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.instance, "Fehler beim laden der Authentifizierungsparameter.", Toast.LENGTH_LONG);
			t.show();
		}

	}
	
	public void save() {

		try {
			 super.serialize(MainActivity.instance.localDataProvider.localDataFilename);
			
		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.instance, "Fehler beim speichern der Einstellungen, nutze Standardkonfiguration.", Toast.LENGTH_LONG);
			t.show();
		}

	}
}
