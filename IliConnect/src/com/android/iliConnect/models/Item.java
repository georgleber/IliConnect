package com.android.iliConnect.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import com.android.iliConnect.dataproviders.PersistableObject;

@Element
public class Item extends PersistableObject{
	
	public Item(){
		super.arrayModel = true;
	}
	@Element (required = false)	
	public String title = "";
	@Element (required = false)
	public String description = "";
	//@Element (required = false)
	//public Date timestamp;
	@Element (required = false)
	public String type = "";
	@Element (required = false)
	public String ref_id = "";
	@ElementList (required = false, name="SubItems")
	public ArrayList<Item> Item;
	
	//public Date timestamp = new Date();
	@Override
	public void load() {
		
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getType() {
		
	 return type;
		
	}

	public String getRef_id() {
		return ref_id;
	}


	public ArrayList<Item> getItems() {
		return Item;
	}


	
}
