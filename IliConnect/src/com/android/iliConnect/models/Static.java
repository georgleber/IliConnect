package com.android.iliConnect.models;

import org.simpleframework.xml.Element;
import com.android.iliConnect.dataproviders.PersistableObject;

@Element
public class Static extends PersistableObject{
	
@Element(name = "Authentification")	
public Authentification auth;
@Element(name = "Settings")	
public Settings settings;


@Override
public void load() {
	// TODO Auto-generated method stub
	
}

	
}
