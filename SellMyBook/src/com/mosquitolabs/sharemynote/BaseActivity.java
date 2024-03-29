package com.mosquitolabs.sharemynote;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

public class BaseActivity extends SlidingActivity {

	private static final String TAG = "BaseActivity";
	private SlidingMenu slidingMenu;
	private ArrayList<SlidingMenuItem> menuItems;
	private ListView list;
	private RelativeLayout account;
	
	private int selectedTab=0;
	
	private TextView name;
	private TextView email;
	private TextView university;
	
	private SlidingMenuAdapter adapter;

	private static final String LOG_TAG = SlidingMenuAdapter.class
			.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setTheme(com.actionbarsherlock.R.style.Sherlock___Theme_DarkActionBar);

		super.onCreate(savedInstanceState);
		

		setBehindContentView(R.layout.menu_frame);

		SlidingMenu slidingMenu = getSlidingMenu();

		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		slidingMenu.setShadowWidth(10);
		slidingMenu.setShadowDrawable(R.drawable.side_navigation_shadow_right);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		slidingMenu.setBehindOffset(width / 5);
		slidingMenu.setFadeDegree(0.75f);
		slidingMenu.setMenu(R.layout.sliding_menu);

		slidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
			@Override
			public void onOpen() {
				Log.d(TAG, "onOpen");
			}
		});
		slidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
			@Override
			public void onOpened() {
				Log.d(TAG, "onOpened");
			}
		});
		slidingMenu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
			@Override
			public void onClose() {
				Log.d(TAG, "onClose");
			}
		});
		slidingMenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
			@Override
			public void onClosed() {
				Log.d(TAG, "onClosed");
			}
		});

		parseXml(R.menu.menu_sidebar);

		list = (ListView) slidingMenu.getMenu().findViewById(
				R.id.side_navigation_listview);
		adapter = null;
				//new SlidingMenuAdapter(0,BaseActivity.this);
		list.setAdapter(adapter);
		
		account = (RelativeLayout) slidingMenu.getMenu().findViewById(R.id.accountLayout);
		account.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent localIntent = new Intent(BaseActivity.this,
						AccountActivity.class);
				startActivity(localIntent);
			}
		});
		
		name = (TextView) slidingMenu.getMenu().findViewById(R.id.name);
		email = (TextView) slidingMenu.getMenu().findViewById(R.id.email);
		university = (TextView) slidingMenu.getMenu().findViewById(R.id.university);
		

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	private void parseXml(int menu) {
		menuItems = new ArrayList<SlidingMenuItem>();
		try {
			XmlResourceParser xrp = getResources().getXml(menu);
			xrp.next();
			int eventType = xrp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					String elemName = xrp.getName();
					if (elemName.equals("item")) {
						String textId = xrp.getAttributeValue(
								"http://schemas.android.com/apk/res/android",
								"title");
						String iconId = xrp.getAttributeValue(
								"http://schemas.android.com/apk/res/android",
								"icon");
						String resId = xrp.getAttributeValue(
								"http://schemas.android.com/apk/res/android",
								"id");
						SlidingMenuItem item = new SlidingMenuItem();
						item.setId(Integer.valueOf(resId.replace("@", "")));
						item.setText(resourceIdToString(textId));
						if (iconId != null) {
							try {
								item.setIcon(Integer.valueOf(iconId.replace(
										"@", "")));
							} catch (NumberFormatException e) {
								Log.d(LOG_TAG, "Item " + item.getId()
										+ " not have icon");
							}
						}

						menuItems.add(item);

					}
				}
				eventType = xrp.next();
			}
		} catch (Exception e) {
			Log.w(LOG_TAG, e);
		}
	}

	public ArrayList<SlidingMenuItem> getMenuItems() {
		return menuItems;
	}

	private String resourceIdToString(String resId) {
		if (!resId.contains("@")) {
			return resId;
		} else {
			String id = resId.replace("@", "");
			return getResources().getString(Integer.valueOf(id));
		}
	}

	public ListView getSlidingMenuList() {
		return list;
	}
	
	
	public void setName(String s){
		name.setText(s);
	}
	public void setEmail(String s){
		email.setText(s);
	}
	public void setUniversity(String s){
		university.setText(s);
	}
	
	public void setSelectedTab(int x){
		selectedTab=x;
	}
	
	public int getSellectedTab(){
		return selectedTab;
	}
	
	public void refreshSlidingMenuAdapter(){
		adapter.notifyDataSetChanged();
	}
	

}
