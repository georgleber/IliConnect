package com.android.iliConnect.test;


import com.android.iliConnect.MainActivity;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;
	private EditText userId;
	private EditText password;
	private EditText url;
	private Button button_login;
	
	public MainActivityTest() {
		super("com.android.iliConnect",MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		//Touch mode abschalten um tasteneingaben simulieren zu koennen
		setActivityInitialTouchMode(false);
		//Referenzen auf UI Elemente holen und Activity starten
		mActivity = getActivity();
		userId = (EditText) mActivity.findViewById(com.android.iliConnect.R.id.editText1);
		password = (EditText) mActivity.findViewById(com.android.iliConnect.R.id.editText2);
		url = (EditText) mActivity.findViewById(com.android.iliConnect.R.id.urlText);
		button_login = (Button) mActivity.findViewById(com.android.iliConnect.R.id.button1);
		
	}
	
	private void enterUserId(){
		//fokus auf erstes Textfeld legen
		mActivity.runOnUiThread(
				new Runnable(){
					public void run(){
						userId.requestFocus();
					}
				}
				);
		this.sendKeys(KeyEvent.KEYCODE_F);
		this.sendKeys(KeyEvent.KEYCODE_R);
		this.sendKeys(KeyEvent.KEYCODE_I);
		this.sendKeys(KeyEvent.KEYCODE_I);
		this.sendKeys(KeyEvent.KEYCODE_S);
	}
	
	public void testMainActivityUI() {
		enterUserId();
		//pruefen ob mit Enter zum richtigen feld gesprungen wird
		this.sendKeys(KeyEvent.KEYCODE_ENTER);
		this.sendKeys(KeyEvent.KEYCODE_1);
		this.sendKeys(KeyEvent.KEYCODE_2);
		this.sendKeys(KeyEvent.KEYCODE_3);
		this.sendKeys(KeyEvent.KEYCODE_4);
		this.sendKeys(KeyEvent.KEYCODE_5);
		this.sendKeys(KeyEvent.KEYCODE_6);
		
		assertEquals(userId.getText().toString(),"friis");
		assertEquals(password.getText().toString(),"123456");
	}
	
	public void testLogin(){
		
	}
	
	public void testStateDestroy() {
		enterUserId();
		
		//activity stoppen und neu starten
		mActivity.finish();
		mActivity = this.getActivity();
		
		assertEquals(userId.getText().toString(),"swe");
	}
	
	@UiThreadTest
	public void testStatePause() {
		Instrumentation mInstr = this.getInstrumentation();
		userId.setText("swe");
		mInstr.callActivityOnPause(mActivity);
		//feldinhalt loeschen, um sicherzustellen dass Inhalt wiederhergestellt anstatt
		//nur beibehalten wird
		userId.setText("");
		mInstr.callActivityOnResume(mActivity);
		assertEquals("swe",userId.getText().toString());
	}
}
