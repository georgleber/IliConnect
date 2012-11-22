package com.example.ilias;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
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

public class MainActivityAnzeige extends FragmentActivity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.auswahlseminar);

       Intent intent = getIntent();

       ((TextView)(findViewById(R.id.auswahl))).setText(intent.getStringExtra("selected")+ " gewählt!");
       
       List<String> valueList = new ArrayList<String>();		
		valueList.add("Aufgabe 1.pdf");
		valueList.add("Aufgabe 2.pdf");
		valueList.add("Aufgabe 3.pdf");
			
	  ListAdapter fileList = new ArrayAdapter<String>( getApplicationContext(), R.layout.black_list_item, valueList);	 
	  final ListView lv = (ListView)findViewById(R.id.list_files);
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

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    getMenuInflater().inflate(R.menu.activity_main, menu);
    //    return true;
   // }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
