package com.android.iliConnect;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class Einstellungen extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings_layout);
		
		CheckBoxPreference cPs = (CheckBoxPreference) findPreference("checkboxPrefSync");
		cPs.setChecked(MainActivity.instance.localDataProvider.settings.sync);
		cPs.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				MainActivity.instance.localDataProvider.settings.sync = (Boolean)newValue;
				return true;
			}
		}); 
		
		
		CheckBoxPreference cPsWlan = (CheckBoxPreference) findPreference("checkboxPrefWlanSync");
		cPsWlan.setChecked(MainActivity.instance.localDataProvider.settings.sync_wlanonly);
		cPsWlan.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				MainActivity.instance.localDataProvider.settings.sync_wlanonly = (Boolean)newValue;
				return true;
			}
		}); 
		
		
		ListPreference lP = (ListPreference) findPreference("listPrefInterv");
		lP.setDefaultValue(MainActivity.instance.localDataProvider.settings.interval);
		lP.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				MainActivity.instance.localDataProvider.settings.interval = Integer.parseInt((String) newValue);
				return true;
			}
		});
		
		ListPreference lPNot = (ListPreference) findPreference("listPrefNumNoti");
		lPNot.setDefaultValue(MainActivity.instance.localDataProvider.settings.num_notifications);
		lPNot.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				MainActivity.instance.localDataProvider.settings.num_notifications = Integer.parseInt((String) newValue);
				return true;
			}
		});
		
		ListPreference lPWarn = (ListPreference) findPreference("listPrefNumNoti");
		lPWarn.setDefaultValue(MainActivity.instance.localDataProvider.settings.level_warning);
		lPWarn.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				MainActivity.instance.localDataProvider.settings.level_warning = Integer.parseInt((String) newValue);
				return true;
			}
		});
		
		
	}
	@Override
	public void onBackPressed() {
		MainActivity.instance.localDataProvider.localdata.save();
		MainActivity.instance.watchThread.startTimer();
		super.onBackPressed();
	}
}