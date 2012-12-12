package com.android.iliConnect.dataproviders;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.models.Authentification;
import com.android.iliConnect.models.ClassAlias;
import com.android.iliConnect.models.Desktop;
import com.android.iliConnect.models.Notifications;
import com.android.iliConnect.models.Settings;

public abstract class PersistableObject {
	
	protected Class currentClass = Object.class;
	public static List<ClassAlias> classAliases = new ArrayList<ClassAlias>();
	public boolean arrayModel = false;
	
	public PersistableObject() {
		this.currentClass = this.getClass();
	}

	protected void deserialize(String fileName) throws Exception {
		Serialization ser = new Serialization();

		BufferedReader reader = new BufferedReader(new InputStreamReader(MainActivity.context.openFileInput(fileName)));
		String xmlContent = "";
		String line = null;
		while ((line = reader.readLine()) != null)
			xmlContent += line + "\n";
		reader.close();

		PersistableObject object = (PersistableObject) ser.deserialize(xmlContent, LocalDataProvider.classAliases, this.currentClass.getName());

		for (Field field : this.currentClass.getFields()) {
			field.set(this, field.get(object));
		}

	}
	
	
	public abstract void load();

}
