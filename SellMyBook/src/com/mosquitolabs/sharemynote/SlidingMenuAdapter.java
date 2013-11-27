package com.mosquitolabs.sharemynote;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlidingMenuAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private BaseActivity2 parentActivity;
	private ArrayList<SlidingMenuItem> menuItems;

	private final static int LOGGED_OUT = 0;
	private final static int LOGGED_IN = 1;

	public SlidingMenuAdapter(int menu, BaseActivity2 context) {
		parentActivity = context;

		
		//CAMBIATO
		//menuItems = parentActivity.getMenuItems();
		inflater = LayoutInflater.from(parentActivity);
	}

	@Override
	public int getCount() {
		return menuItems.size();
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
			convertView = inflater.inflate(R.layout.side_navigation_item, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView
					.findViewById(R.id.side_navigation_item_text);
			holder.icon = (ImageView) convertView
					.findViewById(R.id.side_navigation_item_icon);
			holder.layout = (LinearLayout) convertView
					.findViewById(R.id.layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		SlidingMenuItem item = menuItems.get(position);

		holder.text.setText(menuItems.get(position).getText());

		if (item.getIcon() != SlidingMenuItem.DEFAULT_ICON_VALUE) {
			holder.icon.setVisibility(View.VISIBLE);
			holder.icon.setImageResource(menuItems.get(position).getIcon());
		} else {
			holder.icon.setVisibility(View.GONE);
		}

		
		
		//CAMBIATO
		//if (position == parentActivity.getSellectedTab()) {
		if(true){
			holder.layout.setBackgroundColor(parentActivity.getResources()
					.getColor(R.color.sliding_menu_item_selected));
		} else {
			holder.layout
					.setBackgroundResource(R.drawable.sliding_menu_item_selector);
		}

		return convertView;
	}

	class ViewHolder {
		TextView text;
		ImageView icon;
		LinearLayout layout;
	}

}
