package com.android.iliConnect;

import java.util.ArrayList;
import java.util.List;

import com.android.iliConnect.models.Desktop;
import com.android.iliConnect.models.Item;
import com.android.iliConnect.models.Item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemArrayAdapter extends ArrayAdapter<Item> {

	private class DesktopViews {
		TextView title;
		TextView description;
		TextView date;
		LinearLayout items;
	}

	public ItemArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	private List<Item> items;

		
		public ItemArrayAdapter(Context context, int textViewResourceId, List<Item> items) {
			super(context, textViewResourceId, items);
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		LayoutInflater vi = (LayoutInflater) MainActivity.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		v = fillListRecursive(null, items.get(position), vi);
		
		return v;
	}

	public LinearLayout fillListRecursive(LinearLayout v, Item item, LayoutInflater vi) {
		DesktopViews desktopViews = new DesktopViews();
		if (v == null) {
			v = (LinearLayout) vi.inflate(R.layout.item, null);
			desktopViews = new DesktopViews();
			desktopViews.title = (TextView) v.findViewById(R.id.itemTitle);
			desktopViews.description = (TextView) v.findViewById(R.id.itemDescription);
			desktopViews.date = (TextView) v.findViewById(R.id.itemDate);
			
			v.setTag(desktopViews);
		} else
			desktopViews = (DesktopViews) v.getTag();

		desktopViews.title.setText(item.getTitle());
		desktopViews.description.setText(item.getDescription());

		if (item.getItems() != null) {
			for (Item childItem : item.getItems())
				v = fillListRecursive(desktopViews.items, childItem, vi);
				
		}
		return v;

	}

}