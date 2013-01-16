package com.android.iliConnect;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.iliConnect.models.Item;

public class DesktopItemAdapter extends ArrayAdapter<Item> {
	public DesktopItemAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}
	
	private List<Item> items = new ArrayList<Item>();

	public DesktopItemAdapter(Context context, int textViewResourceId, ArrayList<Item> schreibtischItems) {
		super(context, textViewResourceId, schreibtischItems);

		this.items = schreibtischItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DesktopViews desktopViews = new DesktopViews();

		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) MainActivity.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = (LinearLayout) vi.inflate(R.layout.item, null);

			desktopViews = new DesktopViews();
			desktopViews.title = (TextView) v.findViewById(R.id.itemTitle);
			desktopViews.description = (TextView) v.findViewById(R.id.itemDescription);
			
			desktopViews.type = (TextView) v.findViewById(R.id.itemType);
			desktopViews.date = (TextView) v.findViewById(R.id.itemDate);
			desktopViews.owner = (TextView) v.findViewById(R.id.itemOwner);

			v.setTag(desktopViews);
		} else {
			desktopViews = (DesktopViews) v.getTag();
			desktopViews.type.setVisibility(View.GONE);
			desktopViews.date.setVisibility(View.GONE);		
			desktopViews.owner.setVisibility(View.GONE);
		}

		Item item = items.get(position);
		if (item != null) {

			desktopViews.title.setText(item.getTitle());
			desktopViews.title.setLines(1);
			
			if (item.changed == true) {
				desktopViews.description.setText("'Inhalt geändert'");
				desktopViews.description.setTypeface(null, Typeface.BOLD);
			} 
			else {
				desktopViews.description.setVisibility(View.INVISIBLE);
			}
			
			//desktopViews.description.setText(item.getDescription());

			// restlichen Felder ausblenden
			desktopViews.type.setVisibility(View.GONE);
			desktopViews.date.setVisibility(View.GONE);
			desktopViews.owner.setVisibility(View.GONE);
			
			
		}

		return v;
	}

	private class DesktopViews {
		TextView title;
		TextView description;
		TextView type;
		TextView date;
		TextView owner;
	}
}
