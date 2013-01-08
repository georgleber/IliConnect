package com.android.iliConnect.test;


import com.android.iliConnect.Suche;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

public class SucheTest extends
		ActivityInstrumentationTestCase2<Suche> {

	private Suche mActivity;
	private EditText placeholder;
	
	public SucheTest() {
		super("com.android.iliConnect",Suche.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		//Touch mode abschalten um tasteneingaben simulieren zu koennen
		setActivityInitialTouchMode(false);
		//Referenzen auf UI Elemente holen und Activity starten
		mActivity = getActivity();
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
