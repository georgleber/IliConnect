package com.android.iliConnect.dataproviders;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.models.Authentification;

import android.app.ProgressDialog;

public class DataDownloadThread extends Thread {

	@Override
	public void run() {

		while (true) {
			RemoteDataProvider rProvider = new RemoteDataProvider();
			rProvider.execute(MainActivity.localDataProvider.settings.auth.url_src);
			try {
				sleep(MainActivity.localDataProvider.settings.interval * 60 * 1000);
			} catch (InterruptedException e) {

			}
		}
	}

}
