package dataproviders;

import com.example.ilias.MainActivity;

import android.app.ProgressDialog;
import models.Authentification;

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
