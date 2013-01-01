package com.android.iliConnect.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.android.iliConnect.dataproviders.PersistableObject;
@Root
public class Static extends PersistableObject{
	
@Element(name = "Authentification")	
public Authentification auth;

@Override
public void load() {
	// TODO Auto-generated method stub
	
}

	
}
