package com.wewin.labelprintsys.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.wewin.labelprintsys.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * 自定义ListAdapter
 * 
 * @author HCOU
 * @date 2013-6-24
 */
public class ListAdapter extends BaseAdapter {

	private List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private Context context = null;
	private static HashMap<Integer, Boolean> isSelected;

	/**
	 * 自定义构造方法
	 * 
	 * @param activity
	 * @param handler
	 * @param list
	 */
	public ListAdapter(Context context, List<HashMap<String, String>> list) {
		this.context = context;
		this.list = list;
		isSelected = new HashMap<Integer, Boolean>();
		initData();
	}

	private void initData() {
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ListViewHolder holder = null;
		if (convertView == null) {
			holder = new ListViewHolder();
			// 下拉项布局
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item, null);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.item_chk);
			holder.checkBox.setClickable(false);

			holder.txt_labelid = (TextView) convertView
					.findViewById(R.id.txt_labelid);
			holder.txt_name = (TextView) convertView
					.findViewById(R.id.txt_name);
			holder.txt_code = (TextView) convertView
					.findViewById(R.id.txt_code);

			convertView.setTag(holder);
		} else {
			holder = (ListViewHolder) convertView.getTag();
		}
		HashMap<String, String> map = list.get(position);
		holder.checkBox.setChecked(getIsSelected().get(position));
		holder.txt_labelid.setText(map.get("id"));
		holder.txt_name.setText(map.get("name"));
		holder.txt_code.setText(map.get("code"));

		return convertView;
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		ListAdapter.isSelected = isSelected;
	}
}

class ListViewHolder {
	TextView txt_labelid;
	CheckBox checkBox;
	TextView txt_name;
	TextView txt_code;
}
