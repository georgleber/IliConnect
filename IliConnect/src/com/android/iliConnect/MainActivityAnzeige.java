package com.android.iliConnect;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

public class MainActivityAnzeige extends FragmentActivity implements Updatetable {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.auswahlseminar);        
       Intent intent = getIntent();

       ((TextView)(findViewById(R.id.auswahl))).setText(intent.getStringExtra("selected")+ " gewählt!");
       

       final String Kurs=intent.getStringExtra("selected");
       List<String> valueList = new ArrayList<String>();
       	valueList.add("Testordner");
		valueList.add("Aufgabe 1.pdf");
		valueList.add("Aufgabe 2.pdf");
		valueList.add("Aufgabe 3.pdf");
			
	  ListAdapter fileList = new ArrayAdapter<String>( getApplicationContext(), R.layout.black_list_item, valueList);	 
	  final ListView lv = (ListView)findViewById(R.id.list_content);
	  lv.setAdapter(fileList);
		lv.setOnItemClickListener(new OnItemClickListener(){
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{			
			  Toast.makeText(
			getApplicationContext(), 
		    "Download von " + ((TextView) arg1).getText(), 
		    Toast.LENGTH_LONG).show();
		}
		});
		
		
        
        
}
    private void showSignoutMessage(String Kurs){
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
    	alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
    	alertDialog.setTitle("Abmeldung!");
    	alertDialog.setMessage("Wollen Sie sich wirklich vom Kurs \""+ Kurs +"\" abmelden");
    	alertDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Anmeldung", Toast.LENGTH_LONG).show();
			}
		});
    	alertDialog.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "abbruch", Toast.LENGTH_LONG).show();
			}
		});
    	AlertDialog alertDialog1= alertDialog.create();
    	alertDialog1.show();
    }  

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    getMenuInflater().inflate(R.menu.activity_main, menu);
    //    return true;
   // }

    
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
	public void updateViews() {
		
		
	}
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.kurs_menu, menu);
	    return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    //respond to menu item selection
		  Intent intent = getIntent();
		  ((TextView)(findViewById(R.id.auswahl))).setText(intent.getStringExtra("selected")+ " gewählt!");
	      final String Kurs=intent.getStringExtra("selected");
		
		switch (item.getItemId()) {
	    case R.id.abmeldung:
	    	showSignoutMessage(Kurs);;
	    return true;	    	    
	    default:
	    return super.onOptionsItemSelected(item);
	}
	}

}
