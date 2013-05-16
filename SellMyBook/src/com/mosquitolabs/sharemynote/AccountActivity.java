package com.mosquitolabs.sharemynote;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.parse.Parse;
import com.parse.ParseUser;

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
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Drawable background = getResources().getDrawable(R.drawable.untitled2);
		getSupportActionBar().setBackgroundDrawable(background);
		setTitle(ParseUser.getCurrentUser().getUsername());
		
		profilePicture = (ImageView) findViewById(R.id.profilePicture);

		
		
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.profile_picture);
		profilePicture.setImageBitmap(getRoundedCornerBitmap(bmp, 10));
	
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
	
	
	
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	
	

}
