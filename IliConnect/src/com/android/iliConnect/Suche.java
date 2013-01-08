package com.android.iliConnect;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.android.iliConnect.dataproviders.RemoteDataProvider;
import com.android.iliConnect.models.Item;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;

public class Suche extends ListFragment implements Redrawable {

	private ArrayList<Item> curses = new ArrayList<Item>();
	private EditText etSearch;
	private ListAdapter fileList;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.suche_layout, container, false);
		
		if(MainActivity.instance.localDataProvider.results.Item!=null)
			curses = MainActivity.instance.localDataProvider.results.Item;
		
		fileList = new DesktopArrayAdapter(getActivity(), R.layout.item, curses);
		setListAdapter(fileList);
		
		
		if (container == null) {
			return null;
		}

		etSearch = (EditText) mLinearLayout.findViewById(R.id.editText1);
		etSearch.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void afterTextChanged(Editable s) {
				updateResults(s.toString());
				
			}
		});

		
		return mLinearLayout;

	}

	private void updateResults(String s) {
		 
		
		
		 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		 nameValuePairs.add(new BasicNameValuePair("searchfor", s));
		  
		 ((ProgressBar)MainTabView.instance.findViewById(R.id.progressBar1)).setVisibility(View.VISIBLE);;
		 
		 RemoteDataProvider rP = new RemoteDataProvider(nameValuePairs);
		 rP.execute(new String[]{MainActivity.instance.localDataProvider.remoteData.getSyncUrl()+"?action=search",MainActivity.instance.localDataProvider.searchDataFileName});
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public void refreshViews() {

		curses = MainActivity.instance.localDataProvider.results.Item;
		
		fileList = new DesktopArrayAdapter(getActivity(), R.layout.item, curses);
		getListView().invalidateViews();
		setListAdapter(fileList);
		
		((ProgressBar)MainTabView.instance.findViewById(R.id.progressBar1)).setVisibility(View.INVISIBLE);
		

	}
}