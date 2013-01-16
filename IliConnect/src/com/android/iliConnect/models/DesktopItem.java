package com.android.iliConnect.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.simpleframework.xml.Element;

@Element
public class DesktopItem extends Item {

	//@ElementList(required = false, name="Items")
	public ArrayList<Item> Item;

	@Element(required = false)
	public Date timestamp;

	@Override
	public ArrayList<Item> getItems() {
		return Item;
	}

	public String getDate() {

		if (timestamp != null) {
			SimpleDateFormat sFormat = new SimpleDateFormat("dd.MM kk:mm");
			return sFormat.format(timestamp);
		} else
			return "";

	}

	public DesktopItem() {

	}

}
