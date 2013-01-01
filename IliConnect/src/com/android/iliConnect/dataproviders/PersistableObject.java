package com.android.iliConnect.dataproviders;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.models.ClassAlias;

public abstract class PersistableObject {
	
	protected Class currentClass = Object.class;
	
	public boolean arrayModel = false;
	
	public PersistableObject() {
		this.currentClass = this.getClass();
	}

	protected void deserialize(String fileName) throws Exception {
		Serialization ser = new Serialization();
		/*BufferedReader reader = new BufferedReader(new InputStreamReader(MainActivity.instance.openFileInput(fileName)));
		String xmlContent = "";
		String line = null;
		while ((line = reader.readLine()) != null)
			xmlContent += line + "\n";
		reader.close();*/
		
		PersistableObject object = (PersistableObject) ser.deserialize(this.currentClass,fileName);

		for (Field field : this.currentClass.getFields()) {
			field.set(this, field.get(object));
		}

	}
	
	
	public abstract void load();

}
