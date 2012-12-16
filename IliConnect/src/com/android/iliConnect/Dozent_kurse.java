package com.android.iliConnect;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
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

public class Dozent_kurse extends FragmentActivity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.dozent_layout);        
      Intent intent = getIntent();
        ((TextView)(findViewById(R.id.dozent_name))).setText(intent.getStringExtra("selected"));
           List<String> valueList = new ArrayList<String>();		
		valueList.add("TIWS");
		valueList.add("Compiler Bau");		
			
	  ListAdapter fileList = new ArrayAdapter<String>( getApplicationContext(), R.layout.black_list_item, valueList);	 
	  final ListView lv = (ListView)findViewById(R.id.dozent_list);
	  lv.setAdapter(fileList);
		lv.setOnItemClickListener(new OnItemClickListener(){
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{			
			 //Todo
		}
		});		
		
	  
        
        
}
    
    
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    private void showAlertMessage(){
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
    	alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
    	alertDialog.setTitle("Kursanmeldung");
    	alertDialog.setMessage("Der eingescannte QR-Code ist ungültig.");
    	alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Anmeldung", Toast.LENGTH_LONG).show();
			}
		});
//    	alertDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
//			
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "abbruch", Toast.LENGTH_LONG).show();
//			}
//		});
    	AlertDialog alertDialog1= alertDialog.create();
    	alertDialog1.show();
    }  

}
