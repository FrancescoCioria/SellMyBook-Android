package com.mosquitolabs.sharemynote;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;

import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

public class BaseActivity extends SlidingActivity {

	private static final String TAG = "BaseActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		
		
		

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

}
