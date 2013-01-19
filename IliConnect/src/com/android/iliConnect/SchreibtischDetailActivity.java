package com.android.iliConnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.iliConnect.Exceptions.JoinCourseException;
import com.android.iliConnect.Exceptions.NetworkException;
import com.android.iliConnect.dataproviders.LocalCourseProvider;
import com.android.iliConnect.message.IliOnClickListener;
import com.android.iliConnect.models.Item;

public class SchreibtischDetailActivity extends FragmentActivity implements IliOnClickListener {

	private Object listener = this;
	private String selectedCourse = "";
	private String selectedCourseName = "";
	private String itemType = "";
	public static SchreibtischDetailActivity instance;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schreibtisch_detail_layout);
		Intent intent = getIntent();

		MainActivity.currentActivity = this;
		instance = this;

		final String kurs = intent.getStringExtra("CourseName");

		((TextView) (findViewById(R.id.courseName1))).setText(kurs);
		

		Item item = null;
		final int position = intent.getIntExtra("position", -1);
		if (position > -1) {
			item = MainActivity.instance.localDataProvider.desktopItems.DesktopItem.get(position);
		}
		
		if(intent.getSerializableExtra("Item") != null) {
			item = (Item) intent.getSerializableExtra("Item");
		}
		
		selectedCourse = item.ref_id;
		selectedCourseName = item.title;
		itemType = item.type;
		
		
		final ListView lv = (ListView) findViewById(R.id.desktop_content);
		
		if(item.Item!=null && item.getItems().size() > 0) {
			ListAdapter adapter = new DesktopDetailArrayAdapter(getApplicationContext(), R.id.desktop_content, item.getItems());
			lv.setAdapter(adapter);
			((TextView) (findViewById(R.id.courseDesc1))).setVisibility(View.GONE);
		}
		else {
			// falls Kurs keine Inhalte hat, Meldung anzeigen.
			((TextView) (findViewById(R.id.courseDesc1))).setText("Dem Kurs wurden noch keine Inhalte hinzugefügt.");
			lv.setVisibility(View.GONE);
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		if(itemType.equalsIgnoreCase("CRS")) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.kurs_menu, menu);
		}
		
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// respond to menu item selection
		switch (item.getItemId()) {
		case R.id.abmeldung:
			MessageBuilder.course_singnout(this, selectedCourse, selectedCourseName, (IliOnClickListener) listener);
			return true;
		}

		return true;
	}

	public void onClickCoursePassword(String refID, String password) {
		// TODO Auto-generated method stub

	}

	public void onClickJoinCourse(String refID, String courseName) {
		// TODO Auto-generated method stub

	}

	public void onClickLeftCourse(String refID, String courseName) {
		// TODO Auto-generated method stub
		LocalCourseProvider courseProv = new LocalCourseProvider();
		try {
			try {
				courseProv.leaveCourse(selectedCourse);
			} catch (NetworkException e) {
				MessageBuilder.exception_message(MainTabView.instance, e.getMessage());
			}
			// neuer Sync und View update wird noch leaveCourse gemacht

		} catch (JoinCourseException e) {
			MessageBuilder.exception_message(MainTabView.instance, e.getMessage());
			e.printStackTrace();
		}
		this.finish();
	}

	public void onClickMessageBox() {
		// TODO Auto-generated method stub

	}
	@Override
	protected void onResume() {
		super.onResume();
		MainActivity.currentActivity = this;
		
	}

}
