package com.android.iliConnect.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.android.iliConnect.dataproviders.PersistableObject;
@Element
public class Current {
	
@ElementList
public ArrayList<Notification> Notifications;
@ElementList
public ArrayList<DesktopItem> Desktop;

	
}
