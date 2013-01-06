package com.android.iliConnect;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.iliConnect.models.Notification;

public class TerminItemAdapter extends ArrayAdapter<Notification> {
	private List<Notification> notifications;

	public TerminItemAdapter(Context context, int textViewResourceId, List<Notification> notifications) {
		super(context, textViewResourceId, notifications);
		this.notifications = notifications;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) MainActivity.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.termin_list_item, null);
		}

		Notification notification = notifications.get(position);
		if (notification != null) {
			TextView date = (TextView) v.findViewById(R.id.terminDatum);
			TextView name = (TextView) v.findViewById(R.id.terminName);
			CheckBox marker = (CheckBox) v.findViewById(R.id.terminCheckbox);

			if (date != null) {
				date.setText(notification.getDate());
			}
			
			if (name != null) {
				name.setText(notification.getTitle());
			}

			if (marker != null) {
				marker.setChecked(notification.isMarked());
			}
		}
		return v;
	}
}
