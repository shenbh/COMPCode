package com.newland.comp.adapter.my.miniknowledge;

import com.newland.comp2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**微知识列表适配器
 * @author Administrator
 *
 */
public class MiniKnowledgeListAdapter extends BaseAdapter {

	LayoutInflater layoutInflater;

	public MiniKnowledgeListAdapter(Context context) {
		layoutInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return 0;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null ) {
			convertView = layoutInflater.inflate(R.layout.miniknewledgelist_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
			viewHolder.mTime = (TextView) convertView.findViewById(R.id.time);
			viewHolder.mContent = (TextView) convertView.findViewById(R.id.content);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		return null;
	}

	class ViewHolder {
		private TextView mTitle;
		private TextView mTime;
		private TextView mContent;
	}
}
