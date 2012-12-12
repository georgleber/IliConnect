package com.android.iliConnect.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Notification {

	public String title;
	public String description;
	public Date date;
	public String ref_id;
	
	public String getDate(){
		SimpleDateFormat sFormat = new SimpleDateFormat("dd.MM hh:mm");
		return sFormat.format(date);
	}
	
}
