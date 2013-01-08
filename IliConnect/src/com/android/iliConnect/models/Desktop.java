package com.android.iliConnect.models;

import java.util.ArrayList;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;
@Element
public class Desktop extends PersistableObject {
	@ElementList( name="Items", required = true)
	public ArrayList<Item> DesktopItem;
	
	@Override
	public void load() {
		try {
			super.deserialize(MainActivity.instance.localDataProvider.remoteDataFileName);

		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.instance, "Fehler bein laden der Schreibtischelemente.", Toast.LENGTH_LONG);
			t.show();
		}

	}
}
