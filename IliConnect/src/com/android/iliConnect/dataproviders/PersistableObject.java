package com.android.iliConnect.dataproviders;

import java.io.File;
import java.lang.reflect.Field;

import com.android.iliConnect.MainActivity;

public abstract class PersistableObject {

	protected void deserialize(String fileName) throws Exception {
		Serialization ser = new Serialization();

		PersistableObject object = (PersistableObject) ser.deserialize(this.getClass(), fileName);

		for (Field field : this.getClass().getFields()) {
			field.set(this, field.get(object));
		}

	}

	protected void serialize(String fileName) throws Exception {
		Serialization ser = new Serialization();
		ser.serialize(this, fileName);

	}

	protected void delete(String name) {
		File file = new File(MainActivity.instance.getFilesDir()+"/"+name);
		if(file.exists())
			file.delete();
	}
	
	public abstract void load();

	
}
