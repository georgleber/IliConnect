package com.android.iliConnect;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.iliConnect.Exceptions.JoinCourseException;
import com.android.iliConnect.Exceptions.NetworkException;
import com.android.iliConnect.dataproviders.LocalCourseProvider;
import com.android.iliConnect.models.DesktopItem;
import com.android.iliConnect.models.Item;

public class SchreibtischDetailActivity extends FragmentActivity {

	private String selectedCourse = "";
	public static SchreibtischDetailActivity instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schreibtisch_detail_layout);
		Intent intent = getIntent();

		MainActivity.currentActivity = this;
		instance = this;

		final String Kurs = intent.getStringExtra("CourseName");
		((TextView) (findViewById(R.id.courseName1))).setText(Kurs);

		Item item = null;
		final int position = intent.getIntExtra("position", -1);
		if (position > -1) {
			item = MainActivity.instance.localDataProvider.desktopItems.DesktopItem.get(position);
		}

		selectedCourse = item.ref_id;
		ListAdapter adapter = new DesktopDetailArrayAdapter(getApplicationContext(), R.id.desktop_content, item.getItems());
		final ListView lv = (ListView) findViewById(R.id.desktop_content);
		lv.setAdapter(adapter);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.kurs_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// respond to menu item selection
		switch (item.getItemId()) {
		case R.id.abmeldung:
			LocalCourseProvider courseProv = new LocalCourseProvider();
			try {
				try {
					courseProv.leaveCourse(selectedCourse);
				} catch (NetworkException e) {
					// TODO Fehlermeldung anzeigen
					e.printStackTrace();
				}
				// neuer Sync und View update wird noch leaveCourse gemacht

			} catch (JoinCourseException e) {
				// TODO Fehlermeldung einbauen
				e.printStackTrace();
			}
			
			Intent i = new Intent(SchreibtischDetailActivity.this, MainTabView.class);
			startActivity(i);
			return true;
		}
		
		return true;
	}

	protected LinearLayout fillListRecursive(LinearLayout v, final Item item, LayoutInflater vi) {
		DesktopViews desktopViews = new DesktopViews();
		if (v == null) {
			v = (LinearLayout) vi.inflate(R.layout.item, null);
			desktopViews = new DesktopViews();
			desktopViews.title = (TextView) v.findViewById(R.id.itemTitle);
			desktopViews.description = (TextView) v.findViewById(R.id.itemDescription);
			desktopViews.date = (TextView) v.findViewById(R.id.itemDate);
			desktopViews.type = (TextView) v.findViewById(R.id.itemType);
			desktopViews.owner = (TextView) v.findViewById(R.id.itemOwner);

		} else
			desktopViews = (DesktopViews) v.getTag();

		desktopViews.title.setText(item.getTitle());
		desktopViews.description.setText(item.getDescription());
		desktopViews.type.setText(item.getType());
		desktopViews.owner.setVisibility(View.GONE);

		if (item.getType().equalsIgnoreCase("CRS") || item.getType().equalsIgnoreCase("FOLD"))
			desktopViews.type.setVisibility(View.GONE);

		if (item.getClass().equals(DesktopItem.class) && !((DesktopItem) item).getDate().equals(""))
			desktopViews.date.setText(((DesktopItem) item).getDate());
		else
			desktopViews.date.setVisibility(View.GONE);

		v.setTag(item.getRef_id());

		if (item.getItems() != null) {
			for (final Item childItem : item.getItems()) {

				View v1 = fillListRecursive(desktopViews.items, childItem, vi);
				LinearLayout layout = (LinearLayout) v1;

				v1 = layout;
				v1.setTag(childItem.getRef_id());
				v.addView(v1);
			}

		}
		
		final Item parentItem = item;
		v.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String s = v.getTag().toString();

				if (parentItem.getType().equalsIgnoreCase("CRS")) {
					// ref_id für ggf. Abmeldung merken
					toggleVisibility(parentItem, v);
				} else if (parentItem.getType().equalsIgnoreCase("FOLD")) {
					toggleVisibility(parentItem, v);
				} else if (parentItem.getType().equalsIgnoreCase("FILE")) {
					MainActivity.instance.localDataProvider.openFileOrDownload(s);
				} else if (parentItem.getType().equalsIgnoreCase("TST")) {
					String url_src = MainActivity.instance.localDataProvider.auth.url_src;

					// Test im Browser starten
					// String testUrl = url_src + "ilias.php?baseClass=ilObjTestGUI&ref_id=" + s + "&cmd=infoScreen";

					// Ilias im Browser starten
					String iliasUrl = url_src + "login.php";
					MainActivity.instance.showBrowserContent(iliasUrl);

				} else if (parentItem.getType().equalsIgnoreCase("UNSIGN")) {
					LocalCourseProvider courseProv = new LocalCourseProvider();
					try {
						try {
							courseProv.leaveCourse(selectedCourse);
						} catch (NetworkException e) {
							// TODO Fehlermeldung anzeigen
							e.printStackTrace();
						}
						// neuer Sync und View update wird noch leaveCourse gemacht

					} catch (JoinCourseException e) {
						// TODO Fehlermeldung einbauen
						e.printStackTrace();
					}
				} else {
					MainActivity.instance.showBrowserContent(MainActivity.instance.localDataProvider.auth.url_src + "webdav.php?ref_id=" + s);
				}

			}
		});

		toggleVisibility(item, v);

		return v;

	}

	private void toggleVisibility(Item childItem, View v) {
		View vSub;
		ArrayList<Item> childItems = new ArrayList<Item>();
		if (childItem.Item != null)
			childItems = childItem.Item;

		for (Item item : childItems)
			if ((vSub = v.findViewWithTag(item.getRef_id())).getVisibility() == View.VISIBLE)
				vSub.setVisibility(View.GONE);
			else
				vSub.setVisibility(View.VISIBLE);
	}

	private class DesktopViews {
		TextView title;
		TextView description;
		TextView date;
		TextView type;
		TextView owner;

		LinearLayout items;
	}
}
