package com.android.iliConnect.dataproviders;

import com.android.iliConnect.MainActivity;

public  class DataDownloadThread extends Thread {
	
	@Override
	public void run() {
		
		while (true) {
			MainActivity.instance.remoteDataProvider.execute(MainActivity.instance.localDataProvider.auth.url_src + "RemoteData.xml?user=root&pass=homer");
			try {
				sleep(MainActivity.instance.localDataProvider.settings.interval * 60 * 1000);
			} catch (InterruptedException e) {
				
			}
		}
	}
	
	

}
