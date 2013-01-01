package com.android.iliConnect.models;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;
@Element
public class Desktop extends PersistableObject {
	@ElementList
	public ArrayList<DesktopItem> DesktopItem;
	
	public Desktop(){
		super.arrayModel = true;
	}
	
	@Override
	public void load() {
		try {
			super.deserialize(MainActivity.instance.localDataProvider.remoteDataFileName);

		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.instance.context, "Fehler bein laden der Authentifizierungsparameter.", Toast.LENGTH_LONG);
			t.show();
		}

	}
}
