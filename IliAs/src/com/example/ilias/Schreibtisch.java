package com.example.ilias;

import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ilias.R;


public class Schreibtisch extends ListFragment  {
	
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

//		List<String> valueList = new ArrayList<String>();	
//		for (int i = 0; i < 10; i++)
//		{
//		valueList.add("Seminar "+i);
//		}		
//     	ListAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_black,R.id.list_content, valueList);
//     	View view = inflater.inflate(R.layout.schreibtisch_layout,container);
//	    final ListView lv = (ListView) view.findViewById(R.id.seminarlist);		
//		lv.setAdapter(adapter);
//		lv.setOnItemClickListener(new OnItemClickListener(){
//		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
//		{						
//
//	         Intent i = new Intent();	
//	         i.setClass(getActivity(), MainActivityAnzeige.class);
//	         i.putExtra("selected", lv.getAdapter().getItem(arg2).toString());		
//	         startActivity(i);
//		}
//		
//		});
		
		
		
		return (LinearLayout)inflater.inflate(R.layout.schreibtisch_layout, container, false);
	}
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
		List<String> valueList = new ArrayList<String>();		
		valueList.add("Software Engineering");
		valueList.add("Bildverarbeitung");
		valueList.add("Wissenschaftliches Arbeiten");
			
	  ListAdapter myListAdapter = new ArrayAdapter<String>( getActivity(), android.R.layout.simple_list_item_1, valueList);
	  setListAdapter(myListAdapter);
	  
	 
	 }
	 @Override
	 public void onListItemClick(ListView l, View v, int position, long id) {
	  // TODO Auto-generated method stub
	  Intent i = new Intent();	
      i.setClass(getActivity(), MainActivityAnzeige.class);
      i.putExtra("selected", getListView().getItemAtPosition(position).toString());		
      startActivity(i);
	 }
	  
}
	
	

