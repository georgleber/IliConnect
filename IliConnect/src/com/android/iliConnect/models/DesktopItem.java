package com.android.iliConnect.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
@Element
public class DesktopItem extends Item{
	
	
	@ElementList (required = false)
	public ArrayList<Item> Items; 


	@Element (required = false)
	public Date timestamp = new Date();
	
	@Override
	public ArrayList<Item> getItems() {
		return Items;
	}

	public String getDate() {

		SimpleDateFormat sFormat = new SimpleDateFormat("dd.MM hh:mm");
		return sFormat.format(timestamp); 

	}
	
	public DesktopItem() {

	}

}
