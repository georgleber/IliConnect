package com.android.iliConnect;

import java.util.List;

import com.android.iliConnect.models.Item;

public class ListViewItem {
	
	private String title = "";
	private String description = "";
	private String date = "";
	private List<Item> items;
	
	public ListViewItem(String title, String description, String date, List<Item> items) {
		this.title = title;
		this.description = description;
		this.date = date;
		this.items = items;
	} 

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getDate() {
		return date;
	}

	public List<Item> getItems() {
		return items;
	}
	
}
