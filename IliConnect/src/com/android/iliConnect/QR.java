package com.android.iliConnect;

import com.android.iliConnect.Exceptions.CoursePasswordException;
import com.android.iliConnect.Exceptions.JoinCourseException;
import com.android.iliConnect.Exceptions.NetworkException;
import com.android.iliConnect.dataproviders.LocalCourseProvider;
import com.android.iliConnect.message.IliOnClickListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.iliConnect.MessageBuilder;

public class QR extends Fragment implements Redrawable, IliOnClickListener {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}
		LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.qr_layout, container, false);

		Button mButton = (Button) mLinearLayout.findViewById(R.id.scannow);
		mButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				int REQUEST_SCAN = 0; // Request code for Intent result
				String packageString = "com.android.iliConnect";

				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				intent.setPackage(packageString);
				// Add any optional extras to pass
				// e.g.
				// intent.putExtra("SCAN_FORMATS", "EAN13");
				// intent.putExtra("SCAN_FORMATS", "EAN8");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				// Launch
				startActivityForResult(intent, REQUEST_SCAN);

				// joinCourse("49", null);
				// leaveCourse("49");
			}
		});

		return mLinearLayout;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode != 0) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				if (contents.matches("[0-9]+")) {
					MessageBuilder.course_login(MainTabView.instance, "", contents, this);
				} else {
					MessageBuilder.QR_error(MainTabView.instance);
				}

				// Handle successful scan

				// Zunaechst Messagebox anzeigen, ob wirklich beitreten will

				// Bei Best��tigung joinCourse() aufrufen
			} else if (resultCode == 0) {
				// Handle cancel
			}
		}
	}

	private void joinCourse(String ref_id, String crs_pw) {
		LocalCourseProvider local = new LocalCourseProvider();
		try {
			String result = null;
			result = local.joinCourse(ref_id, crs_pw);

			// Falls Passwort für Anmeldung benötigt wird, Abfrage einblenden
			if (result != null && result.contains("PASSWORD_NEEDED")) {
				MessageBuilder.course_password(MainTabView.instance, ref_id, this);
				// Passwortabfrage einblenden

			}
			// local.leaveCourse("49");
		} catch (JoinCourseException e) {
			MessageBuilder.course_alreadysignedin(MainTabView.instance);

		} catch (CoursePasswordException e) {
			System.out.println(e.getMessage());

		} catch (NetworkException e) {
			MessageBuilder.exception_message(MainTabView.instance, e.getMessage());
		}

	}

	// private void leaveCourse(String ref_id) {
	// LocalCourseProvider local = new LocalCourseProvider();
	// try {
	// String result = null;
	// try {
	// result = this.local.leaveCourse(ref_id);
	// } catch (NetworkException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// if(result != null && result.contains("LEFT")) {
	// this.showAlert("Sie wurde erfolgreich abgemeldet");
	// }
	// } catch (JoinCourseException e) {
	// this.showAlert(e.getMessage());
	// e.printStackTrace();
	// }
	// }

	public void refreshViews() {
		// TODO Auto-generated method stub

	}

	public void onClickCoursePassword(String refID, String password) {
		// TODO Auto-generated method stub
		this.joinCourse(refID, password);

	}

	public void onClickLeftCourse(String refID, String courseName) {
		// TODO: Kurs abmelden mit REFID
	}

	public void onClickJoinCourse(String refID, String courseName) {
		// TODO: Anmeldung mit Join Course(refID);

		this.joinCourse(refID, null);

	}

	public void onClickMessageBox() {
		// TODO Auto-generated method stub

	}

}
