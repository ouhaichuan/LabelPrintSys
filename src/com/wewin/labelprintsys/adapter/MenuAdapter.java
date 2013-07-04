package com.wewin.labelprintsys.adapter;

import java.util.ArrayList;
import java.util.List;
import com.wewin.labelprintsys.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 自定义ListAdapter
 * 
 * @author HCOU
 * @date 2013-6-24
 */
public class MenuAdapter extends BaseAdapter {

	private List<String> list = new ArrayList<String>();
	private Context context = null;

	/**
	 * 自定义构造方法
	 * 
	 * @param activity
	 * @param handler
	 * @param list
	 */
	public MenuAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
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
		MenuViewHolder holder = null;
		if (convertView == null) {
			holder = new MenuViewHolder();
			// 下拉项布局
			convertView = LayoutInflater.from(context).inflate(
					R.layout.adapter_item, null);
			holder.txt_des = (TextView) convertView.findViewById(R.id.txt_des);
			holder.head_imge = (ImageView) convertView
					.findViewById(R.id.head_imge);

			convertView.setTag(holder);
		} else {
			holder = (MenuViewHolder) convertView.getTag();
		}

		String des = list.get(position);
		holder.txt_des.setText(des);
		holder.head_imge.setBackgroundResource(R.drawable.head_image);
		return convertView;
	}
}

class MenuViewHolder {
	ImageView head_imge;
	TextView txt_des;
}
