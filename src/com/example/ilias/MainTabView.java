package com.example.ilias;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

import com.example.ilias.R;
import com.example.ilias.PagerAdapter;

/*
 * The <code>TabsViewPagerFragmentActivity</code> class implements the Fragment activity that maintains a TabHost using a ViewPager. 
 */
public class MainTabView extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

	private TabHost mTabHost;
	private ViewPager mViewPager;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, MainTabView.TabInfo>();
	private PagerAdapter mPagerAdapter;
	/*
	 *	 
	 * Maintains extrinsic info of a tab's construct
	 */
	private class TabInfo {
		 private String tag;
         private Class<?> clss;
         private Bundle args;
         private Fragment fragment;
         TabInfo(String tag, Class<?> clazz, Bundle args) {
        	 this.tag = tag;
        	 this.clss = clazz;
        	 this.args = args;
         }

	}
	/*
	 * A simple factory that returns dummy views to the Tabhost	
	 */
	class TabFactory implements TabContentFactory {

		private final Context mContext;

	    /*
	     * @param context
	     */
	    public TabFactory(Context context) {
	        mContext = context;
	    }

	    /*
	     * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
	     */
	    public View createTabContent(String tag) {
	        View v = new View(mContext);
	        v.setMinimumWidth(0);
	        v.setMinimumHeight(0);
	        return v;
	    }

	}
	/*
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Inflate the layout
		setContentView(R.layout.tabviewcontainer);
		// Initialise the TabHost
		this.initialiseTabHost(savedInstanceState);
		if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("Schreibtisch")); //set the tab as per the saved state
        }
		// Intialise ViewPager
		this.intialiseViewPager();
	}

	/*
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }

    /*
     * Initialise ViewPager
     */
    private void intialiseViewPager() {

		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, QR.class.getName()));
		fragments.add(Fragment.instantiate(this, Schreibtisch.class.getName()));
		fragments.add(Fragment.instantiate(this, Magazin.class.getName()));
		fragments.add(Fragment.instantiate(this, Termine.class.getName()));
		fragments.add(Fragment.instantiate(this, Suche.class.getName()));
		
		this.mPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
		//
		this.mViewPager = (ViewPager)super.findViewById(R.id.viewpager);
		this.mViewPager.setAdapter(this.mPagerAdapter);
		this.mViewPager.setCurrentItem(1); //set default site "Schreibtisch"
		this.mViewPager.setOnPageChangeListener(this);
    }

	/*
	 * Initialise the Tab Host
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;
        MainTabView.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator("QR", getResources().getDrawable(R.drawable.qr_code_default)), ( tabInfo = new TabInfo("Tab1", QR.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainTabView.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator("Schreibtisch", getResources().getDrawable(R.drawable.icon_crs)), ( tabInfo = new TabInfo("Tab2", Schreibtisch.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainTabView.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator("Magazin", getResources().getDrawable(R.drawable.icon_root_b)), ( tabInfo = new TabInfo("Tab3", Magazin.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainTabView.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab4").setIndicator("Termine"), ( tabInfo = new TabInfo("Tab4", Termine.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainTabView.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab5").setIndicator("Suche", getResources().getDrawable(R.drawable.icon_seas_s)), ( tabInfo = new TabInfo("Tab5", Suche.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);  
        mTabHost.setCurrentTab(1);//set default tab "Schreibtisch"
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++){
            mTabHost.getTabWidget().getChildAt(i).setPadding(10,10,10,10);
        }
        mTabHost.setOnTabChangedListener(this);
	}

	/*
	 * Add Tab content to the Tabhost	
	 */
	private static void AddTab(MainTabView activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
	}

	/*
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	public void onTabChanged(String tag) {
		//TabInfo newTab = this.mapTabInfo.get(tag);
		int pos = this.mTabHost.getCurrentTab();
		this.mViewPager.setCurrentItem(pos);
    }

	/* 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
	 */
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub

	}

	/* 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
	 */
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		this.mTabHost.setCurrentTab(position);
	}

	/* 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
	 */
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}
}



