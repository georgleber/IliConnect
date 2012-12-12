package com.android.iliConnect;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class Menu_setting extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
       // getActionBar().setDisplayHomeAsUpEnabled(true);
       
        
        //Intervall
      Spinner intervall = (Spinner) findViewById(R.id.intervall_drop);
     // Create an ArrayAdapter using the string array and a default spinner layout
     ArrayAdapter<CharSequence> adapterintervall = ArrayAdapter.createFromResource(this,
             R.array.array_intervall, android.R.layout.simple_spinner_item);
     // Specify the layout to use when the list of choices appears
     adapterintervall.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    
     // Apply the adapter to the spinner
    intervall.setAdapter(adapterintervall);
     intervall.setSelection(1); //standard selection
     
   //anzahl der Termine
     Spinner anzahlT = (Spinner) findViewById(R.id.Anzahl_drop);
    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapteranzahlT = ArrayAdapter.createFromResource(this,
            R.array.array_termine, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
    adapteranzahlT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
   
    // Apply the adapter to the spinner
    anzahlT.setAdapter(adapteranzahlT);
    anzahlT.setSelection(2); //standard selection
    
  //Warnung
    Spinner warnung = (Spinner) findViewById(R.id.Warning_drop);
   // Create an ArrayAdapter using the string array and a default spinner layout
   ArrayAdapter<CharSequence> adapterwarnung = ArrayAdapter.createFromResource(this,
           R.array.array_warnung, android.R.layout.simple_spinner_item);
   // Specify the layout to use when the list of choices appears
   adapterwarnung.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  
   // Apply the adapter to the spinner
   warnung.setAdapter(adapterwarnung);
   warnung.setSelection(0); //standard selection
   
   //Kritisch
   Spinner kritisch = (Spinner) findViewById(R.id.Critical_drop);
  // Create an ArrayAdapter using the string array and a default spinner layout
  ArrayAdapter<CharSequence> adapterkritisch = ArrayAdapter.createFromResource(this,
          R.array.array_Kritisch, android.R.layout.simple_spinner_item);
  // Specify the layout to use when the list of choices appears
  adapterkritisch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
  // Apply the adapter to the spinner
  kritisch.setAdapter(adapterkritisch);
  kritisch.setSelection(0); //standard selection
	        
        
        
}
}
