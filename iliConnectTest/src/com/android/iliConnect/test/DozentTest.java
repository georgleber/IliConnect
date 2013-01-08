package com.android.iliConnect.test;

import com.android.iliConnect.Dozent_kurse;
import com.android.iliConnect.R;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class DozentTest extends
		ActivityInstrumentationTestCase2<Dozent_kurse> {

	private Dozent_kurse mActivity;
	private TextView dozentname;
	private ListView kursliste;
	
	public DozentTest() {
		super("com.android.iliConnect",Dozent_kurse.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		//Touch mode abschalten um tasteneingaben simulieren zu koennen
		setActivityInitialTouchMode(false);
		//Referenzen auf UI Elemente holen und Activity starten
		mActivity = getActivity();
		dozentname = (TextView) mActivity.findViewById(com.android.iliConnect.R.id.dozent_name);
		kursliste = (ListView) mActivity.findViewById(com.android.iliConnect.R.id.dozent_list);
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
