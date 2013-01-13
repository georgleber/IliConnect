package com.android.iliConnect;


import com.android.iliConnect.message.QROnClickListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public class MessageBuilder {	
	
	 public static void course_alreadysignedin(Activity activity) {		
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setIcon(R.drawable.error);
			alertDialog.setTitle("Bereits im Kurs angemeldet");
			alertDialog.setMessage("Ein erneuter Beitritt zum Kurs ist nicht möglich");
			alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					
					return;
				}
			});
			
			AlertDialog alertDialog1 = alertDialog.create();
			alertDialog1.show();			
		}
	 public static void course_singnout(Activity activity,String course_name, String refID) {		
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setIcon(R.drawable.error);
			alertDialog.setTitle("Abmeldung!");
			alertDialog.setMessage("Wollen Sie sich wirklich vom Kurs " +"\"" + course_name + "\""  + " abmelden?");
			alertDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					//TODO: Kurs abmelden mit REFID
					return;
				}
			});
			alertDialog.setNegativeButton("Nein", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					
					return;
				}
			});
			AlertDialog alertDialog1 = alertDialog.create();
			alertDialog1.show();			
		}
	 public static void login_false(Activity activity) {		
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setIcon(R.drawable.error);
			alertDialog.setTitle("Logindaten falsch");
			alertDialog.setMessage("Ihr Benutzername oder Kennwort ist falsch!");
			alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					
					return;
				}
			});
			
			AlertDialog alertDialog1 = alertDialog.create();
			alertDialog1.show();			
		}
	 public static void no_folder(Activity activity) {		
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setIcon(R.drawable.error);
			alertDialog.setTitle("Ordner nicht mehr verfügbar");
			alertDialog.setMessage("Der aktuelle Ordner ist nicht mehr verfügbar.");
			alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					
					return;
				}
			});
			
			AlertDialog alertDialog1 = alertDialog.create();
			alertDialog1.show();			
		}
	 public static void QR_error(Activity activity) {		
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setIcon(R.drawable.error);
			alertDialog.setTitle("QR Code nicht erkannt!");
			alertDialog.setMessage("Der eingescannte QR-Code ist ungültig");
			alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					
					return;
				}
			});
			
			AlertDialog alertDialog1 = alertDialog.create();
			alertDialog1.show();			
		}
	 public static void connection_failed(Activity activity) {		
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setIcon(R.drawable.error);
			alertDialog.setTitle("Verbindung fehlgeschlagen");
			alertDialog.setMessage("Es konnte keine Verbindung zum ILIAS-Server hergestellt werden. Bitte überprüfen Sie Ihre Internetverbindung und die Serveradresse und versuchen Sie es erneut!");
			alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					
					return;
				}
			});
			
			AlertDialog alertDialog1 = alertDialog.create();
			alertDialog1.show();			
		}
	 public static void course_login(Activity activity,String course_name, String refID) {		
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setIcon(R.drawable.warn);
			alertDialog.setTitle("Kursanmeldung!");
			alertDialog.setMessage("Wollen Sie sich zu dem Kurs: " + "\"" + course_name + "\"" + " anmelden?");
			alertDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					//TODO: Anmeldung mit Join Course(refID);
					return;
				}
			});
			alertDialog.setNegativeButton("Nein", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					
					return;
				}
			});
			
			AlertDialog alertDialog1 = alertDialog.create();
			alertDialog1.show();			
		}
	 public static void course_password(Activity activity, final String refID, final QROnClickListener listener) {		
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setIcon(R.drawable.warn);
			alertDialog.setTitle("Passwort");
			alertDialog.setMessage("Bitte geben Sie ein Passwort für diesen Kurs ein!");
			final EditText input = new EditText(activity);
			alertDialog.setView(input);
			alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					//TODO: Anmeldung mit Join Course mit Password(refID);
					listener.onClickCoursePassword(refID,input.getText().toString());
					return;
				}
			});
			alertDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					
					return;
				}
			});
			
			AlertDialog alertDialog1 = alertDialog.create();
			alertDialog1.show();			
		}
	 public static void course_register(Activity activity, final String refID) {		
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setIcon(R.drawable.warn);
			alertDialog.setTitle("Anmeldung");
			alertDialog.setMessage("Sie wurden Erfolgreich angemeldet!");
			final EditText input = new EditText(activity);
			alertDialog.setView(input);
			alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {					
					
					return;
				}
			});
			
			
			AlertDialog alertDialog1 = alertDialog.create();
			alertDialog1.show();			
		}
	 
}
