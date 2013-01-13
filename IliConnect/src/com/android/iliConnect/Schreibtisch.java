package com.android.iliConnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.iliConnect.models.Item;

public class Schreibtisch extends ListFragment implements Redrawable {

	private DesktopItemAdapter desktopAdapter;

	public void refreshViews() {
		getListView().invalidateViews();
		desktopAdapter = new DesktopItemAdapter(MainActivity.currentActivity, R.layout.item, MainActivity.instance.localDataProvider.desktopItems.DesktopItem);
		setListAdapter(desktopAdapter);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.schreibtisch_layout, container, false);

		desktopAdapter = new DesktopItemAdapter(MainActivity.currentActivity, R.layout.item, MainActivity.instance.localDataProvider.desktopItems.DesktopItem);
		setListAdapter(desktopAdapter);

		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}

		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Item item = MainActivity.instance.localDataProvider.desktopItems.DesktopItem.get(position);

		// doStuff
		Intent intentMain = new Intent(MainTabView.instance, SchreibtischDetailActivity.class);
		intentMain.putExtra("CourseName", item.title);
		intentMain.putExtra("position", position);
		MainActivity.currentActivity.startActivity(intentMain);

		/*
		ItemArrayAdapter desktopAdapter = new ItemArrayAdapter(MainActivity.currentActivity, R.layout.item, MainActivity.instance.localDataProvider.desktopItems.DesktopItem.get(position).Item);

		((TextView) MainActivity.currentActivity.findViewById(R.id.textView1)).setText(MainActivity.instance.localDataProvider.desktopItems.DesktopItem.get(position).title);
		setListAdapter(desktopAdapter);
		super.onListItemClick(l, v, position, id);
		 */
	}

}
