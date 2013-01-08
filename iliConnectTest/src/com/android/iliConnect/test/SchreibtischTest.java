package com.android.iliConnect.test;


import com.android.iliConnect.Schreibtisch;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

public class SchreibtischTest extends
		ActivityInstrumentationTestCase2<Schreibtisch> {

	private Schreibtisch mActivity;
	private EditText placeholder;
	
	public SchreibtischTest() {
		super("com.android.iliConnect",Schreibtisch.class);
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
