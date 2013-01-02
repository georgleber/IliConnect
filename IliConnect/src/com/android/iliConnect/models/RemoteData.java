package com.android.iliConnect.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;

@Root (name="Iliconnect") 
public class RemoteData extends PersistableObject {
	@Element
	public Current Current; 


	@Override
	public void load() {
			try {
				super.deserialize(MainActivity.instance.localDataProvider.remoteDataFileName);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {

		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.instance, "Fehler beim laden der Authentifizierungsparameter.", Toast.LENGTH_LONG);
			t.show();
		}

	}
	public String getSyncUrl(){
		//return MainActivity.instance.localDataProvider.auth.url_src+MainActivity.instance.localDataProvider.auth.api_src+"?user="+MainActivity.instance.localDataProvider.auth.user_id+"&pass="+MainActivity.instance.localDataProvider.auth.password+"&action=sync";
//		return MainActivity.instance.localDataProvider.auth.url_src+MainActivity.instance.localDataProvider.auth.api_src+"?user=gbonney&pass=gbonney&action=sync";
		return MainActivity.instance.localDataProvider.auth.url_src+"RemoteData.xml";
	}
	
}
