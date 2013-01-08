package com.android.iliConnect.models;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;

@Root (name="IliConnect") 
public class Results extends PersistableObject {
	
	@Path("Current")
	@ElementList (required = false, name="Results")
	public ArrayList<Item> Item; 


	@Override
	public void load() {
			try {
				super.deserialize(MainActivity.instance.localDataProvider.searchDataFileName);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {

		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.instance, "Fehler beim laden der Authentifizierungsparameter.", Toast.LENGTH_LONG);
			t.show();
		}

	}

	
}
