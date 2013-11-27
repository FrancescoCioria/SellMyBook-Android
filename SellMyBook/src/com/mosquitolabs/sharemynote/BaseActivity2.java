package com.mosquitolabs.sharemynote;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class BaseActivity2 extends SherlockFragmentActivity {
	final String[] data = { "one", "two", "three" };
	final String[] fragments = { "com.mosquitolabs.sharemynote.FragmentOne",
			"com.mosquitolabs.sharemynote.FragmentTwo",
			"com.mosquitolabs.sharemynote.FragmentThree" };
	ActionBarDrawerToggle mDrawerToggle;
	DrawerLayout drawer;
	ListView navList;
	int pos;
	boolean fragmentChanged = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_drawer);
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		navList = (ListView) findViewById(R.id.drawer);

		getSupportActionBar().show();
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, data);

		navList.setAdapter(adapter);
		navList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if(pos!=position){
					pos = position;
					fragmentChanged=true;
				}
				drawer.closeDrawer(navList);
			}
		});
		FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
		tx.replace(R.id.main,
				Fragment.instantiate(BaseActivity2.this, fragments[0]));
		tx.commit();
		
		initializeToggle();

	}

	private void initializeToggle() {
		mDrawerToggle = new ActionBarDrawerToggle(this, drawer,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				// TODO Auto-generated method stub
				getSupportActionBar().setTitle("SellMyBook");
				if(fragmentChanged){
				FragmentTransaction tx = getSupportFragmentManager()
						.beginTransaction();
				tx.replace(R.id.main, Fragment.instantiate(
						BaseActivity2.this, fragments[pos]));
				tx.commit();
				fragmentChanged=false;
				}
				
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				// Set the title on the action when drawer open
				
				getSupportActionBar().setTitle("Hello");
				fragmentChanged=false;
				super.onDrawerOpened(drawerView);
			}
		};
		mDrawerToggle.syncState();
		drawer.setDrawerListener(mDrawerToggle);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {

			if (drawer.isDrawerOpen(navList)) {
				drawer.closeDrawer(navList);
			} else {
				drawer.openDrawer(navList);
			}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}
