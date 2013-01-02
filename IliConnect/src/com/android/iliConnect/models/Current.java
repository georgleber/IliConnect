package com.android.iliConnect.models;

import java.util.ArrayList;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
@Element
public class Current {
	
@ElementList
public ArrayList<Notification> Notifications;
@Element
public Desktop Desktop;

	
}
