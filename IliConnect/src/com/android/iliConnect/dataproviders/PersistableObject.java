package com.android.iliConnect.dataproviders;

import java.lang.reflect.Field;

public abstract class PersistableObject {
	
	
	protected void deserialize(String fileName) throws Exception {
		Serialization ser = new Serialization();
		
		PersistableObject object = (PersistableObject) ser.deserialize(this.getClass(),fileName);

		for (Field field : this.getClass().getFields()) {
			field.set(this, field.get(object));
		}

	}
	
	protected void serialize(String fileName) throws Exception {
		Serialization ser = new Serialization();
		ser.serialize(this, fileName);

	}
	
	
	public abstract void load();



}
