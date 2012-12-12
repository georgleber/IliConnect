package com.android.iliConnect.models;

import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;

public class Authentification extends PersistableObject {
	public boolean autologin;
	public String user_id;
	public String password;
	public String url_src = "http://www.recruitment-specialist.de/xml/RemoteData.xml";

	public Authentification() {

	}

	@Override
	public void load() {
		try {
			super.deserialize(MainActivity.localDataProvider.localDataFilename);

		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.context, "Fehler beim laden der Authentifizierungsparameter.", Toast.LENGTH_LONG);
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
