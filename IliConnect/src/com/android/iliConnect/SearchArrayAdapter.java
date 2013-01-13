package com.android.iliConnect;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.iliConnect.Exceptions.CoursePasswordException;
import com.android.iliConnect.Exceptions.JoinCourseException;
import com.android.iliConnect.Exceptions.NetworkException;
import com.android.iliConnect.dataproviders.LocalCourseProvider;
import com.android.iliConnect.models.Item;

public class SearchArrayAdapter extends DesktopArrayAdapter {
	
	public SearchArrayAdapter(Context context, int textViewResourceId, List<Item> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.items = items;
	}
	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		LayoutInflater vi = (LayoutInflater) MainActivity.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		v = fillListRecursive(null, items.get(position), vi);
		
		v.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				v.setBackgroundColor(45739);
				Item item = items.get(position);
				joinCourse(item.ref_id);		
			}
			
		});

		/*
		v.findViewById(R.id.imageButton1).setVisibility(View.VISIBLE);
		v.findViewById(R.id.imageButton1).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MainActivity.instance.showBrowserContent(MainActivity.instance.localDataProvider.auth.url_src+"webdav.php?ref_id="+items.get(position).ref_id);
			}
		});
		*/
		//replaceView(v, (LinearLayout)convertView,items.get(position).getType());
		return v;
	}
	
	
	private void joinCourse(String ref_id) {
		LocalCourseProvider courseProv = new LocalCourseProvider();
		try {
			try {
				courseProv.joinCourse(ref_id, null);
			} catch (NetworkException e) {
				// TODO Fehlermeldung anzeigen!!
				e.printStackTrace();
			}
		} catch (JoinCourseException e) {
			e.printStackTrace();
		} catch (CoursePasswordException e) {
			// TODO Passwortabfrage einbauen
			e.printStackTrace();
		}
		
	}

}
