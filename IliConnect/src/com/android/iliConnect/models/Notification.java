package com.android.iliConnect.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.simpleframework.xml.Element;

import com.android.iliConnect.dataproviders.PersistableObject;

@Element
public class Notification extends PersistableObject {

	public Notification() {

	}

	@Element
	public String title;
	@Element(required = false)
	public String description;
	@Element(required = false)
	public Date date;
	@Element(required = false)
	public String ref_id;
	@Element(required = false)
	public boolean marked;

	public String getDate() {

		if (date != null) {
			SimpleDateFormat sFormat = new SimpleDateFormat("dd.MM hh:mm");
			return sFormat.format(date);
		} else
			return "";

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

	public boolean isMarked() {
		return marked;
	}
}
