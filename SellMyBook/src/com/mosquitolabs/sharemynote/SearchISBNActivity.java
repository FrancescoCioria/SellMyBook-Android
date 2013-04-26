package com.mosquitolabs.sharemynote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class SearchISBNActivity extends SherlockActivity {

	private Button buttonISBN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setTheme(com.actionbarsherlock.R.style.Sherlock___Theme_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_isbn);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		buttonISBN = (Button) findViewById(R.id.buttonISBNGo);
		buttonISBN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent localIntent = new Intent(SearchISBNActivity.this,
						BookDetailsActivity.class);
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
