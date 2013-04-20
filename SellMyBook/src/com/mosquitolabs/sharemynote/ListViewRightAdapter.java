package com.mosquitolabs.sharemynote;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListViewRightAdapter extends BaseAdapter {
	private LayoutInflater inflater;

	private final static int LOGGED_OUT = 0;
	private final static int LOGGED_IN = 1;
	private MainActivity parentActivity;

	public ListViewRightAdapter(MainActivity context) {
		parentActivity = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return parentActivity.getBookList().size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.book_list_item_prova, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView
					.findViewById(R.id.book_list_item_title);
			holder.author = (TextView) convertView
					.findViewById(R.id.book_list_item_author);
			holder.desc = (TextView) convertView
					.findViewById(R.id.book_list_item_desc);
			holder.year = (TextView) convertView
					.findViewById(R.id.book_list_item_year);
			holder.filter = (RelativeLayout) convertView
					.findViewById(R.id.book_list_item_filter);
			holder.finalShadow = (RelativeLayout) convertView
					.findViewById(R.id.book_list_item_shadow);
			holder.filterShadow = (RelativeLayout) convertView
					.findViewById(R.id.book_list_item_shadowFilter);
			holder.finalMargin = (RelativeLayout) convertView
					.findViewById(R.id.book_list_item_finalMargin);

			holder.icon = (ImageView) convertView
					.findViewById(R.id.book_list_item_icon);
			holder.mainRelative = (RelativeLayout) convertView
					.findViewById(R.id.book_list_item_mainField);

			// holder.rate = (RatingBar) convertView
			// .findViewById(R.id.book_list_item_ratingBar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		BookData book = parentActivity.getBookList().get(position);
		holder.title.setText(book.title);

		// holder.text.setTextColor(Color.BLACK);
		// holder.icon.setVisibility(View.GONE);
		holder.author.setText(book.author);
		if (book.desc.length() > 0) {
			// holder.desc.setText(book.desc);
		}
		String year = book.year;
		if (year.length() > 4) {
			year = year.substring(0, 4);
		}
		holder.year.setText("(edizione " + year + ")");
		// +book.publisher+", "
		// holder.rate.setRating(book.state);
		if (position == 0) {
			holder.filter.setVisibility(View.VISIBLE);
			holder.filterShadow.setVisibility(View.VISIBLE);
		} else {
			holder.filter.setVisibility(View.GONE);
			holder.filterShadow.setVisibility(View.GONE);
		}

		// holder.bottomLine.setVisibility(View.GONE);
		holder.finalShadow.setVisibility(View.VISIBLE);

		if (position == parentActivity.getBookList().size() - 1) {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			int dp = 10;
			double px = 0;
			switch (parentActivity.getResources().getDisplayMetrics().densityDpi) {
			case DisplayMetrics.DENSITY_LOW:
				px = dp * 0.75;
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				px = dp;
				break;
			case DisplayMetrics.DENSITY_HIGH:
				px = dp * 1.5;
				break;
			case DisplayMetrics.DENSITY_XHIGH:
				px = dp * 2;
				break;

			default:
				break;
			}
			params.setMargins(0, 0, 0, (int) px);
			holder.finalMargin.setVisibility(View.VISIBLE);

			// holder.finalShadow.setLayoutParams(params);
		} else {
			holder.finalMargin.setVisibility(View.GONE);
		}

		// holder.shadowLeft.setVisibility(View.VISIBLE);
		// holder.shadowRight.setVisibility(View.VISIBLE);

		holder.mainRelative.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// toast("relative");

			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView title;
		TextView author;
		TextView desc;
		TextView year;

		RelativeLayout filter;
		RelativeLayout finalShadow;
		RelativeLayout filterShadow;
		RelativeLayout mainRelative;
		RelativeLayout finalMargin;
		// RatingBar rate;
		ImageView icon;
	}

}
