package com.android.iliConnect;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.iliConnect.R;
import com.android.iliConnect.models.Item;
import com.android.iliConnect.models.Notification;



public class UebersichtArrayAdapter extends ArrayAdapter<Item> {
	
	private class UebersichtView {
		TextView title;
		TextView description;
		TextView date;
		TextView type;
		TextView owner;
	}
	
	public UebersichtArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
	}
	
	private List<Item> items;

	public UebersichtArrayAdapter(Context context, int textViewResourceId, List<Item> items) {
		super(context, textViewResourceId, items);
		this.items = items;	
	}

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;

		UebersichtView view = new UebersichtView();
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) MainActivity.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item, null);
			view = new UebersichtView();

			view.title = (TextView) v.findViewById(R.id.itemTitle);
			view.description = (TextView) v.findViewById(R.id.itemDescription);		
			view.type = (TextView) v.findViewById(R.id.itemType);
			
			
			v.setTag(view);
		} else
			view = (UebersichtView) v.getTag();

		Item item = items.get(position);
		
		view.title.setText(item.getTitle());
		view.description.setText(item.getDescription());
		if (view.description.equals("")) {
			view.description.setVisibility(View.INVISIBLE);
		}

		if(item.getType().equals("")) {
			view.type.setVisibility(View.INVISIBLE);
		}
		view.type.setText("Datei");
		
		
		v.setClickable(false);
		v.findViewById(R.id.itemOwner).setVisibility(View.GONE);
		v.findViewById(R.id.itemDate).setVisibility(View.GONE);
		
		return v;
	}

}
