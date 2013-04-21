package com.mosquitolabs.sharemynote;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.XmlResourceParser;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

public class BaseActivity extends SlidingActivity {

	private static final String TAG = "BaseActivity";
	private SlidingMenu slidingMenu;
	private ArrayList<SideNavigationItem> menuItems;
	private ListView list;

	private static final String LOG_TAG = SideNavigationAdapter.class
			.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setBehindContentView(R.layout.menu_frame);

		SlidingMenu slidingMenu = getSlidingMenu();

		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setShadowWidth(10);
		slidingMenu.setShadowDrawable(R.drawable.side_navigation_shadow_right);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		slidingMenu.setBehindOffset(width / 7);
		slidingMenu.setFadeDegree(0.75f);
		slidingMenu.setMenu(R.layout.side_navigation_left);

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
		SideNavigationAdapter adapter = new SideNavigationAdapter(0,
				BaseActivity.this);
		list.setAdapter(adapter);

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
		menuItems = new ArrayList<SideNavigationItem>();
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
						SideNavigationItem item = new SideNavigationItem();
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

	public ArrayList<SideNavigationItem> getMenuItems() {
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
	
	
	public ListView getSlidingMenuList(){
		return list;
	}

}
