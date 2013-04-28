package com.mosquitolabs.sharemynote;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MyCustomAdapterSell extends BaseAdapter {
	private LayoutInflater mInflater;
	private BookCollection bookCollection = BookCollection.getInstance();
	private int currentList;

	private static final int ALL = 0;
	private static final int CURRENT = 1;
	private static final int SELLING = 2;
	private static final int SOLD = 3;

	private static final int EDIT = 0;
	private static final int USER = 0;
	private static final int FAILED = 1;
	private static final int REMOVE = 1;
	private static final int SELL_ANOTHER = 1;

	private ArrayAdapter<String> dataAdapterCurrent;
	private ArrayAdapter<String> dataAdapterSelling;
	private ArrayAdapter<String> dataAdapterSold;

	private int size;
	private MainActivity context;

	private boolean firstCurrent = true;
	private boolean firstSelling = true;
	private boolean firstSold = true;

	private static String[] SPINNER_CURRENT = { "Profilo compratore",
			"Transazione fallita" };
	private static String[] SPINNER_SELLING = { "Modifica", "Rimuovi" };
	private static String[] SPINNER_SOLD = { "Profilo compratore",
			"Vendine un altro" };

	public MyCustomAdapterSell(MainActivity paramContext, int currentList) {
		this.mInflater = LayoutInflater.from(paramContext);
		this.currentList = currentList;
		context = paramContext;
		dataAdapterSelling = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, SPINNER_SELLING);
		dataAdapterCurrent = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, SPINNER_CURRENT);
		dataAdapterSold = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, SPINNER_SOLD);
		dataAdapterSelling
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataAdapterCurrent
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataAdapterSold
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	}

	public int getCount() {
		switch (currentList) {

		case ALL:
			size = bookCollection.getSellAll().size();
			break;

		/*
		 * case CURRENT: size = bookCollection.getSellCurrent().size(); break;
		 * 
		 * case SELLING: size = bookCollection.getSellSelling().size(); break;
		 * 
		 * case SOLD: size = bookCollection.getSellSold().size(); break;
		 */

		default:
			size = bookCollection.getSellSizeForSellingState(currentList);
			break;

		}
		return (size);

	}

	public Object getItem(int paramInt) {
		return Integer.valueOf(paramInt);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public View getView(final int paramInt, View paramView,
			ViewGroup paramViewGroup) {

		ViewHolder localViewHolder;

		if (paramView == null) {

			paramView = mInflater.inflate(R.layout.book_list_item_sell, null);
			localViewHolder = new ViewHolder();

			localViewHolder.icon = (ImageView) paramView
					.findViewById(R.id.book_list_item_icon);
			localViewHolder.topMargin = (RelativeLayout) paramView
					.findViewById(R.id.book_list_item_TopMargin);
			localViewHolder.topTitleAll = (TextView) paramView
					.findViewById(R.id.book_list_item_TopTitleAll);
			localViewHolder.bottomMargin = (RelativeLayout) paramView
					.findViewById(R.id.book_list_item_BottomMargin);

			localViewHolder.sold = (ImageView) paramView
					.findViewById(R.id.book_list_item_corner_sold);
			localViewHolder.buttonSetAsSold = (Button) paramView
					.findViewById(R.id.buttonSetAsSold);

			localViewHolder.overflow = (Spinner) paramView
					.findViewById(R.id.spinnerOverflow);

			paramView.setTag(localViewHolder);

		}

		localViewHolder = (ViewHolder) paramView.getTag();

		BookData temp = new BookData();
		switch (currentList) {
		case ALL:

			temp = bookCollection.getNextBookForSellingState(CURRENT, paramInt);
			if (temp == null) {
				temp = bookCollection.getNextBookForSellingState(
						SELLING,
						paramInt
								- bookCollection
										.getSellSizeForSellingState(CURRENT));
			}
			if (temp == null) {
				temp = bookCollection
						.getNextBookForSellingState(
								SOLD,
								paramInt
										- (bookCollection
												.getSellSizeForSellingState(CURRENT) + bookCollection
												.getSellSizeForSellingState(SELLING)));
			}
			break;
		/*
		 * case CURRENT: temp = bookCollection.getSellCurrent().get(paramInt);
		 * break;
		 * 
		 * case SELLING: temp = bookCollection.getSellSelling().get(paramInt);
		 * break;
		 * 
		 * case SOLD: temp = bookCollection.getSellSold().get(paramInt); break;
		 */

		default:
			temp = bookCollection.getNextBookForSellingState(currentList,
					paramInt);
			break;

		}

		final BookData book = temp;

		// CODICE COMUNE //
		Bitmap b = null;
		if (paramInt % 2 == 0) {
			b = getRoundedCornerBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.book_exampleold), 13);
		} else {
			b = getRoundedCornerBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.book_exampleold2), 13);
		}
		localViewHolder.icon.setImageBitmap(b);

		localViewHolder.sold.setVisibility(View.GONE);
		localViewHolder.buttonSetAsSold.setVisibility(View.GONE);
		localViewHolder.topTitleAll.setVisibility(View.GONE);

		localViewHolder.buttonSetAsSold
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						context.setBookAsSold(book);
					}

				});

		if (currentList == ALL) {
			if ((paramInt == 0)
					|| (paramInt == bookCollection
							.getSellSizeForSellingState(CURRENT))
					|| (paramInt == (bookCollection
							.getSellSizeForSellingState(CURRENT) + bookCollection
							.getSellSizeForSellingState(SELLING)))) {

				localViewHolder.topTitleAll.setVisibility(View.VISIBLE);

			}
		}

		switch (book.sellingState) {

		case SELLING:

			localViewHolder.overflow.setAdapter(dataAdapterSelling);
			localViewHolder.overflow
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						public void onItemSelected(AdapterView<?> arg0, View v,
								int position, long id) {
							switch (position) {
							case EDIT:

								break;

							case REMOVE:
								context.removeBookFromSell(book);
								break;
							}
						}

						public void onNothingSelected(AdapterView<?> arg0) {

						}

					});

			localViewHolder.topTitleAll.setText("Libri in vendita");

			break;

		case CURRENT:

			localViewHolder.overflow.setAdapter(dataAdapterCurrent);
			localViewHolder.overflow
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						public void onItemSelected(AdapterView<?> arg0, View v,
								int position, long id) {
							switch (position) {
							case USER:

								break;

							case FAILED:
								context.setBookAsSelling(book);
								break;
							}
						}

						public void onNothingSelected(AdapterView<?> arg0) {

						}

					});

			localViewHolder.buttonSetAsSold.setVisibility(View.VISIBLE);
			localViewHolder.topTitleAll.setText("Transazioni in corso");

			break;
		case SOLD:

			localViewHolder.overflow.setAdapter(dataAdapterSold);
			localViewHolder.overflow
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						public void onItemSelected(AdapterView<?> arg0, View v,
								int position, long id) {
							switch (position) {
							case USER:

								break;

							case SELL_ANOTHER:

								break;
							}
						}

						public void onNothingSelected(AdapterView<?> arg0) {

						}

					});

			localViewHolder.sold.setVisibility(View.VISIBLE);
			localViewHolder.topTitleAll.setText("Libri venduti");

			break;
		}
		if (paramInt == 0) {
			localViewHolder.topMargin.setVisibility(View.VISIBLE);
		} else {
			localViewHolder.topMargin.setVisibility(View.GONE);
		}

		if (paramInt == size - 1) {
			localViewHolder.bottomMargin.setVisibility(View.VISIBLE);
		} else {
			localViewHolder.bottomMargin.setVisibility(View.GONE);
		}

		return paramView;

	}

	static class ViewHolder {
		ImageView icon;
		TextView topTitleAll;
		TextView title;
		TextView author;
		RelativeLayout topMargin;
		RelativeLayout bottomMargin;
		Spinner overflow;
		ImageView sold;
		Button buttonSetAsSold;

	}

	private void t(int position) {

		Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT)
				.show();

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
