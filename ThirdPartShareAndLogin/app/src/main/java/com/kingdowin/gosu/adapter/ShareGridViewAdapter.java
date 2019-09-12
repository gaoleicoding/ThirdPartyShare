package com.kingdowin.gosu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingdowin.gosu.R;
import com.kingdowin.gosu.object.ShareAppInfo;

public class ShareGridViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<ShareAppInfo> list;

	public ShareGridViewAdapter(List<ShareAppInfo> list, Context context) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void changeList(List<ShareAppInfo> list) {
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.share_list_items, null);

			holder.share_app_icon = (ImageView) convertView
					.findViewById(R.id.share_app_icon);
			holder.share_app_text = (TextView) convertView
					.findViewById(R.id.share_app_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (list.size() > 0) {
			holder.share_app_icon.setImageDrawable(list.get(position)
					.getAppIcon());
			holder.share_app_text.setText(list.get(position).getAppName());

		}
		return convertView;

	}

	class ViewHolder {
		ImageView share_app_icon;
		TextView share_app_text;

	}
}