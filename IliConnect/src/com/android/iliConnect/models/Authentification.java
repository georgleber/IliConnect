package com.android.iliConnect.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;

@Root
public class Authentification extends PersistableObject {
	@Element
	public boolean autologin = true;
	@Element
	public String user_id = "";
	@Element
	public String password = "";
	@Element
	public String url_src = "http://www.recruitment-specialist.de/xml/";

	public Authentification() {

	}

	@Override
	public void load() {
		try {
			super.deserialize(MainActivity.instance.localDataProvider.localDataFilename);

		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.instance, "Fehler beim laden der Authentifizierungsparameter.", Toast.LENGTH_LONG);
			t.show();
		}

	}

	public void setLogin(boolean autologin, String user_id, String password, String url_src) {
		this.autologin = autologin;
		this.user_id = user_id;
		this.password = password;
		this.url_src = url_src;
	}

}
