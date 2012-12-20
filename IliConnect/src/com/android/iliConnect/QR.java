package com.android.iliConnect;

import com.android.iliConnect.dataproviders.CoursePasswordException;
import com.android.iliConnect.dataproviders.DataDownloadThread;
import com.android.iliConnect.dataproviders.JoinCourseException;
import com.android.iliConnect.dataproviders.LocalDataProvider;
import com.android.iliConnect.dataproviders.LocalCourseProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class QR extends Fragment {
	

	public static LocalCourseProvider local = new LocalCourseProvider();


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
		  LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.qr_layout,
                  container, false);
		  
		  Button mButton = (Button) mLinearLayout.findViewById(R.id.scannow);
		    mButton.setOnClickListener(new OnClickListener() {
		        public void onClick(View v) {
		        	
		        	int REQUEST_SCAN = 0; //Request code for Intent result
		        	String packageString = "com.android.iliConnect";

		        	Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		        	intent.setPackage(packageString);
		        	//Add any optional extras to pass
		        	//e.g. 
		        	//intent.putExtra("SCAN_FORMATS", "EAN13");
		        	//intent.putExtra("SCAN_FORMATS", "EAN8");
		        	intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		        	//Launch
		        	startActivityForResult(intent, REQUEST_SCAN);
		        	
		        	joinCourse("49", "4711");

		        	
		        }
		    });

		    return mLinearLayout;
		
		
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 0) {
	        if (resultCode != 0) {
	            String contents = intent.getStringExtra("SCAN_RESULT");
	            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	            
	            
	            
	            Toast.makeText(getActivity(), contents, Toast.LENGTH_LONG).show();	            
	            // Handle successful scan
	            
	            // Zunaechst Messagebox anzeigen, ob wirklich beitreten will 
	            
	            // Bei Bestätigung joinCourse() aufrufen
	        } else if (resultCode == 0) {
	            // Handle cancel
	        }
	    }
	}
	
	private void joinCourse(String ref_id, String crs_pw) {
    	try {
			String result = QR.local.joinCourse("49", "4711");
			
			if(result != null && result.contains("JOINED")) {
				this.showAlert("Sie wurde erfolgreich angemeldet");
			}
			// Falls Passwort für Anmeldung benoetigt wird, Abfrage einblenden
			if(result != null && result.contains("PASSWORD_NEEDED")) {
				// Passwortabfrage einblenden
				this.showAlert("Bitte geben Sie das Passwort des Kurses ein");
			}
			//local.leaveCourse("49");
		} catch (JoinCourseException e) {
			this.showAlert(e.getMessage());
			System.out.println(e.getMessage());

		} catch (CoursePasswordException e) {
			System.out.println(e.getMessage());
			this.showAlert(e.getMessage());
		}		
	}
	
	// Fuer Ausgabetest etwas zum Anzeigen!!
	private void showAlert(String text) {
		 AlertDialog ad = new AlertDialog.Builder(getActivity())
         .create();
		 ad.setCancelable(false);
		 ad.setTitle("Test");
		 ad.setMessage(text);
		 ad.setButton(getActivity().toString(), new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int which) {
				 dialog.dismiss();
			 }
		 });
		 ad.show();
	}
			
}
