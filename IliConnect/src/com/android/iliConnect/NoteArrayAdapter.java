package com.android.iliConnect;

import java.util.List;

import com.android.iliConnect.models.Item;
import com.android.iliConnect.models.Notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NoteArrayAdapter extends ArrayAdapter<Notification> {

	private class NoteViews {
		TextView title;
		TextView description;
		TextView date;
	}

	public NoteArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	private List<Notification> items;

	public NoteArrayAdapter(Context context, int textViewResourceId, List<Notification> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		NoteViews noteViews = new NoteViews();
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) MainActivity.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.noteitem, null);
			noteViews = new NoteViews();
			noteViews.title = (TextView) v.findViewById(R.id.title);
			noteViews.description = (TextView) v.findViewById(R.id.description);
			noteViews.date = (TextView) v.findViewById(R.id.date);

			v.setTag(noteViews);
		} else
			noteViews = (NoteViews) v.getTag();

		Notification item = items.get(position);

		noteViews.title.setText(item.getTitle());
		noteViews.description.setText(item.getDescription());
		noteViews.date.setText(item.getDate());

		return v;
	}

}