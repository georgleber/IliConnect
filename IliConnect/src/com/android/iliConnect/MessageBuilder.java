package com.android.iliConnect;


import com.android.iliConnect.message.IliOnClickListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
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
	 public static void course_singnout(Activity activity, final String refID,final String course_name, final IliOnClickListener listener) {		
		 String message = "Wollen Sie sich vom Kurs abmelden?";
		   AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
		   alertDialog.setIcon(R.drawable.warn);
		   alertDialog.setTitle("Kursabmeldung!");
		   if(!course_name.equals("") || course_name != null) {
		    message = "Wollen Sie sich vom Kurs: " + "\"" + course_name + "\"" + " abmelden?";
		   }
			alertDialog.setMessage(message);
			alertDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					
					listener.onClickLeftCourse(refID, course_name);
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
	 public static void course_login(Activity activity,final String course_name, final String refID, final IliOnClickListener listener) {		
		 String message = "Wollen Sie sich am Kurs anmelden?";
		   AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
		   alertDialog.setIcon(R.drawable.warn);
		   alertDialog.setTitle("Kursanmeldung!");
		   if(!course_name.equals("") || course_name != null) {
		    message = "Wollen Sie sich am Kurs: " + "\"" + course_name + "\"" + " anmelden?";
		   }
			alertDialog.setMessage(message);
			alertDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					listener.onClickJoinCourse(refID, course_name);
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
	 public static void course_login_acc(Activity activity,final String course_name, final String refID, final IliOnClickListener listener) {	
		 	String message ="Wollen Sie sich am Kurs anmelden?";
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setIcon(R.drawable.warn);
			alertDialog.setTitle("Kursanmeldung!");
			if(!course_name.equals("") || course_name != null) {
				message = "Wollen Sie sich am Kurs: " + "\"" + course_name + "\"" + " anmelden?";
			}
			alertDialog.setMessage(message);
		
			alertDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					listener.onClickJoinCourse(refID, course_name);
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
	 public static void course_password(Activity activity, final String refID, final IliOnClickListener listener) {		
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setIcon(R.drawable.warn);
			alertDialog.setTitle("Passwort");
			alertDialog.setMessage("Bitte geben Sie ein Passwort für diesen Kurs ein!");
			final EditText input = new EditText(activity);
			input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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
			alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {					
					
					return;
				}
			});
			
			
			AlertDialog alertDialog1 = alertDialog.create();
			alertDialog1.show();			
		}
	 public static void course_notexist(Activity activity, final String refID) {		
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setIcon(R.drawable.warn);
			alertDialog.setTitle("Kurs nicht vorhanden");
			alertDialog.setMessage("Der gesuchte Kurs ist nicht vorhanden!");			
			alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {					
					
					return;
				}
			});
			
			
			AlertDialog alertDialog1 = alertDialog.create();
			alertDialog1.show();			
		}
	 public static void course_passwordfalse(Activity activity, final String refID) {		
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setIcon(R.drawable.error);
			alertDialog.setTitle("Passwort Falsch!");
			alertDialog.setMessage("Das eingegebene Passwort ist falsch! Bitte scannen Sie den QRCode erneut.");	
//			final EditText input = new EditText(activity);
//			input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//			alertDialog.setView(input);
			alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {					
//					listener.onClickCoursePassword(refID,input.getText().toString());
					return;
				}
			});
			
			
			AlertDialog alertDialog1 = alertDialog.create();
			alertDialog1.show();			
		}
	 public static void course_permissondenied(Activity activity, final String refID) {		
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setIcon(R.drawable.error);
			alertDialog.setTitle("Zugriff verweigert!");
			alertDialog.setMessage("Die Anmeldung ist nicht Möglich!");			
			alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {					
					
					return;
				}
			});
			
			
			AlertDialog alertDialog1 = alertDialog.create();
			alertDialog1.show();			
		}
	
	 
	 public static void download_error(Activity activity, String fileName) {		
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
			alertDialog.setIcon(R.drawable.warn);
			alertDialog.setTitle("Download fehlgeschlagen");
			alertDialog.setMessage("Die Datei " + fileName + " konnte nicht heruntergeladen werden");			
			alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {					
					
					return;
				}
			});
			
			
			AlertDialog alertDialog1 = alertDialog.create();
			alertDialog1.show();			
		}
	 
	public static void exception_message(Activity activity, String errMsg) {
		if(activity == null) {
			activity = MainActivity.instance;
		}
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
		alertDialog.setIcon(R.drawable.error);
		alertDialog.setTitle("Fehler");
		alertDialog.setMessage(errMsg);
		alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});

		AlertDialog alertDialog1 = alertDialog.create();
		alertDialog1.show();
	}
	 
}
