package com.mosquitolabs.sharemynote;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class AccountActivity extends SherlockActivity {

	private Button buttonSave;
	private Button buttonFacebook;
	private Button buttonEmail;
	private Button buttonPhone;

	private TextView textViewName;
	private TextView textViewStat;

	private ImageView profilePicture;

	private Spinner spinnerUniversity;
	private Spinner spinnerCampus;
	private Spinner spinnerCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setTheme(com.actionbarsherlock.R.style.Sherlock___Theme_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
		//getSupportActionBar().setTitle("Account");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

}
