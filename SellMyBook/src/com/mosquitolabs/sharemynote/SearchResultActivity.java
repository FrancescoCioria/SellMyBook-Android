package com.mosquitolabs.sharemynote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class SearchResultActivity extends SherlockActivity {

	private ListView listView;
	
	private TextView topBar;

	private BookCollection bookCollection = BookCollection.getInstance();

	private MyCustomAdapterSearch adapterSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setTheme(com.actionbarsherlock.R.style.Sherlock___Theme_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		listView = (ListView) findViewById(R.id.listView);
		topBar = (TextView) findViewById(R.id.results);

		adapterSearch = new MyCustomAdapterSearch(this);

		listView.setAdapter(adapterSearch);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				Intent localIntent = new Intent(SearchResultActivity.this,
						BookDetailsActivity.class);
				startActivity(localIntent);
				
			}
			
			
		});
		BookData b1 = new BookData();
		BookData b2 = new BookData();
		BookData b3 = new BookData();
		BookData b4 = new BookData();
		b2.ID = "2";
		b3.ID = "3";
		b4.ID = "4";
		bookCollection.addBookToList(bookCollection.getSearchResult(), b1);
		bookCollection.addBookToList(bookCollection.getSearchResult(), b2);
		bookCollection.addBookToList(bookCollection.getSearchResult(), b3);
		bookCollection.addBookToList(bookCollection.getSearchResult(), b4);

		adapterSearch.notifyDataSetChanged();

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
