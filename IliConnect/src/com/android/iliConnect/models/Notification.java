package com.android.iliConnect.models;

import java.text.SimpleDateFormat;

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
	public Long date;
	@Element(required = false)
	public String ref_id;

	public String getDate() {
		if (date != null) {
			// FIXME: Workaround conversion PHP Timestamp to Java Timestamp
			Long reDate = Long.valueOf(date) * 1000;

			SimpleDateFormat sFormat = new SimpleDateFormat("dd.MM hh:mm");
			return sFormat.format(reDate);
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
}
