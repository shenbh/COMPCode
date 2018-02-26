package com.newland.comp.adapter.my;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newland.comp.bean.my.CheckIn;
import com.newland.comp.bean.my.PersonList;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 人员列表
 * 
 * @author H81
 *
 */
public class PersonListAdapter extends BaseAdapter {

	private Context context;
	private List<PersonList> list = new ArrayList<PersonList>();
	private LayoutInflater mInflater;

	public PersonListAdapter(Context context, List<PersonList> list) {
		super();
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.person_list_row, null);
			holder = new ViewHolder();
			holder.left_tv = (TextView) convertView.findViewById(R.id.id);
			holder.right_tv = (TextView) convertView.findViewById(R.id.name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PersonList bean = list.get(position);
		holder.left_tv.setText(StringUtil.noNull(bean.misid));
		holder.right_tv.setText(StringUtil.noNull(bean.username));

		return convertView;
	}

	class ViewHolder {
		public TextView left_tv;
		public TextView right_tv;
		// public TextView time;//时间
	}

}
