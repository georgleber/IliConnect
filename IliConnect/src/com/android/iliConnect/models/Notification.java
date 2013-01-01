package com.android.iliConnect.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.android.iliConnect.dataproviders.PersistableObject;

@Element
public class Notification extends PersistableObject {
	
	public Notification() {
	
	}
	@Element
	public String title = "";
	@Element
	public String description = "";
	public Date date = new Date();
	@Element
	public String ref_id = "";

	public String getDate() {

		SimpleDateFormat sFormat = new SimpleDateFormat("dd.MM hh:mm");
		return sFormat.format(date); 

	}

	@Override
	public void load() {
		
		
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getRef_id() {
		return ref_id;
	}

}
