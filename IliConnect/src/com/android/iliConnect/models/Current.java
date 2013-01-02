package com.android.iliConnect.models;

import java.util.ArrayList;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

@Element
public class Current {

	@ElementList(required = false)
	public ArrayList<Notification> Notifications;
	
	@Element(required = false)
	public Desktop Desktop;

}
