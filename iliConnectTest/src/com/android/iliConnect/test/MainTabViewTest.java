package com.android.iliConnect.test;


import com.android.iliConnect.MainTabView;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

public class MainTabViewTest extends
		ActivityInstrumentationTestCase2<MainTabView> {

	private MainTabView mActivity;
	private EditText placeholder;
	
	public MainTabViewTest() {
		super("com.android.iliConnect",MainTabView.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		//Touch mode abschalten um tasteneingaben simulieren zu koennen
		setActivityInitialTouchMode(false);
		//Referenzen auf UI Elemente holen und Activity starten
		mActivity = getActivity();
		//Viewvariablen belegen
		placeholder = (EditText) mActivity.findViewById(com.android.iliConnect.R.id.editText1);
		
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
