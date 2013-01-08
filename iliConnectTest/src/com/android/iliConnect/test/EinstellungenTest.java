package com.android.iliConnect.test;

import com.android.iliConnect.Einstellungen;
import com.android.iliConnect.R;

import android.app.Instrumentation;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class EinstellungenTest extends
		ActivityInstrumentationTestCase2<Einstellungen> {

	private Einstellungen mActivity;
	private CheckBoxPreference sync;
	private CheckBoxPreference wlan;
	private ListPreference list_intervall;
	private ListPreference list_numNoti;
	private ListPreference list_warning;
	private ListPreference list_critical;
	private CheckBox syncBox;
	
	public EinstellungenTest() {
		super("com.android.iliConnect",Einstellungen.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		//Touch mode abschalten um tasteneingaben simulieren zu koennen
		setActivityInitialTouchMode(false);
		//Referenzen auf UI Elemente holen und Activity starten
		mActivity = getActivity();
		sync = (CheckBoxPreference) mActivity.findPreference("checkboxPrefSync");
		wlan = (CheckBoxPreference) mActivity.findPreference("checkboxPrefWlanSync");
		list_intervall = (ListPreference) mActivity.findPreference("listPrefInterv");
		list_numNoti = (ListPreference) mActivity.findPreference("listPrefNumNoti");
		list_warning = (ListPreference) mActivity.findPreference("listPrefWarning");
		list_critical = (ListPreference) mActivity.findPreference("listPrefCritical");
		syncBox = (CheckBox) mActivity.findViewById(sync.getLayoutResource());
		
	}
	
	
	
	public void testSettingsActivityUI() {
				mActivity.runOnUiThread(
						new Runnable(){
							public void run(){
								syncBox.requestFocus();
							}
						}
						);
				this.sendKeys(KeyEvent.KEYCODE_ENTER);
				this.sendKeys(KeyEvent.KEYCODE_ENTER);
		//wclnrthur
	}
	
	public void testStateDestroy() {
		
		//activity stoppen und neu starten
		mActivity.finish();
		mActivity = this.getActivity();
		
	}
	
	@UiThreadTest
	public void testStatePause() {
		Instrumentation mInstr = this.getInstrumentation();
		mInstr.callActivityOnPause(mActivity);
		//feldinhalt loeschen, um sicherzustellen dass Inhalt wiederhergestellt anstatt
		//nur beibehalten wird
		mInstr.callActivityOnResume(mActivity);
		}
}
