package com.mosquitolabs.sharemynote;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class BookDetailsActivity extends SherlockActivity {

	private ListView listView;

	private TextView topBar;

	private String book_ID;

	private BookCollection bookCollection = BookCollection.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setTheme(com.actionbarsherlock.R.style.Sherlock___Theme_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		book_ID = getIntent().getStringExtra("book_ID");

		listView = (ListView) findViewById(R.id.listView);
		final Button buttonWanted = (Button) findViewById(R.id.buttonFavourite);
		final Button buttonSell = (Button) findViewById(R.id.buttonSell);

		TextView usedby = (TextView) findViewById(R.id.usedby);
		usedby.setText(Html.fromHtml("Usato al " + "<b>"
				+ "Politecnico di Milano" + "</b>" + " dal prof. " + "<b>"
				+ "Andrea Sianesi" + "</b>"));

		buttonWanted.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bookCollection.addBookToListWanted(bookCollection
						.getBookFromSearchByID(book_ID));
				buttonWanted.setText("Rimuovi dai libri osservati");

				// bookCollection.getBookFromSearchByID(book_ID).title

				toast("Fondamenti di fisica"
						+ " è stato correttamente aggiunto ai libri che stai osservando.");
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

	private void toast(final String msg) {
		BookDetailsActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				Toast toast = Toast.makeText(BookDetailsActivity.this, msg,
						Toast.LENGTH_LONG);
				toast.show();
			}

		});
	}

}
