package com.android.iliConnect;

import com.android.iliConnect.models.Settings;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;


public class Einstellungen extends PreferenceActivity {
	
	private static boolean syncIntervChanged = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		Settings settings = MainActivity.instance.localDataProvider.settings;
		
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
		
		/*
		CheckBoxPreference cPsLogin = (CheckBoxPreference) findPreference("checkboxPrefAutologin");
		cPsLogin.setChecked(MainActivity.instance.localDataProvider.auth.autologin);
		cPsLogin.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				MainActivity.instance.localDataProvider.auth.autologin = (Boolean)newValue;
				return true;
			}
		}); 
		*/
		
		ListPreference lP = (ListPreference) findPreference("listPrefInterv");
		//lP.setDefaultValue(MainActivity.instance.localDataProvider.settings.interval);
		int index = 0;
		switch(settings.interval) {
		case 5:
			index = 0;
			break;
		case 15:
			index = 1;
			break;
		case 30:
			index = 2;
			break;
		case 60:
			index = 3;
			break;
		case 120:
			index = 4;
			break;
		case 1440:
			index = 5;
			break;
		}
		lP.setValueIndex(index);		

		lP.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				MainActivity.instance.localDataProvider.settings.interval = Integer.parseInt((String) newValue);
				syncIntervChanged = true;
				return true;
			}
		});
		
		ListPreference lPNot = (ListPreference) findPreference("listPrefNumNoti");
		lPNot.setDefaultValue(MainActivity.instance.localDataProvider.settings.num_notifications);		
		lPNot.setValueIndex(settings.num_notifications-1);
		lPNot.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				MainActivity.instance.localDataProvider.settings.num_notifications = Integer.parseInt((String) newValue);
				return true;
			}
		});
		
		ListPreference lPWarn = (ListPreference) findPreference("listPrefWarning");
		lPWarn.setDefaultValue(MainActivity.instance.localDataProvider.settings.level_warning);
		lPWarn.setValueIndex(settings.level_warning-3);
		lPWarn.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				MainActivity.instance.localDataProvider.settings.level_warning = Integer.parseInt((String) newValue);
				return true;
			}
		});
		
		ListPreference lPCrit = (ListPreference) findPreference("listPrefCritical");
		//lPCrit.setDefaultValue(MainActivity.instance.localDataProvider.settings.level_critical);
		lPCrit.setValueIndex(settings.level_critical-1);
		lPCrit.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				MainActivity.instance.localDataProvider.settings.level_critical = Integer.parseInt((String) newValue);
				return true;
			}
		});
		
	}
	@Override
	public void onBackPressed() {
		MainActivity.instance.localDataProvider.localdata.save();
		
		if(syncIntervChanged)
			MainActivity.instance.watchThread.startTimer();
		
		MainTabView.getInstance().update();
		super.onBackPressed();
	}
}
