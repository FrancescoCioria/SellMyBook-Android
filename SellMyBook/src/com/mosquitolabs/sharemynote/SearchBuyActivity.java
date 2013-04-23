package com.mosquitolabs.sharemynote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class SearchBuyActivity extends SherlockActivity {

	private Button buttonISBN;
	private Button buttonRandom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setTheme(com.actionbarsherlock.R.style.Sherlock___Theme_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_buy);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		buttonISBN = (Button) findViewById(R.id.buttonISBNSearch);
		buttonRandom = (Button) findViewById(R.id.buttonRandomGo);
		buttonISBN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent localIntent = new Intent(SearchBuyActivity.this,
						SearchISBNActivity.class);
				startActivity(localIntent);
			}
		});

		buttonRandom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent localIntent = new Intent(SearchBuyActivity.this,
						SearchResultActivity.class);
				startActivity(localIntent);

			}
		});

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
