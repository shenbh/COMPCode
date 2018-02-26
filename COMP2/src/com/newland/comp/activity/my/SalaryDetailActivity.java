package com.newland.comp.activity.my;

import java.util.List;

import com.newland.comp.bean.CustomPayBill;
import com.newland.comp.bean.my.PayBill;
import com.newland.comp.common.AppContext;
import com.newland.comp2.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 工资明细
 * 
 * 
 */
public class SalaryDetailActivity extends Activity {
	LayoutInflater layoutInflater;
	PayBill parentPayBill;
	List<CustomPayBill> childPayBills;
	TextView valueTextView;
	TextView titleTextView;

	/**
	 * 设置工资明细标题栏
	 */
	private void initTitle() {
		TextView center_title = (TextView) findViewById(R.id.head_center_title);
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		left_btn.setVisibility(View.VISIBLE);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		TextView right_tv = (TextView) findViewById(R.id.head_right_tv);
		if (center_title != null) {
			center_title.setText("工资明细");
		}
		if (left_btn != null) {
			left_btn.setOnClickListener(new OnClickListener() {

				
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					SalaryDetailActivity.this.finish();
				}
			});
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {
			right_tv.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);

		Bundle bundle = getIntent().getExtras();
		parentPayBill = (PayBill) bundle.getSerializable("parent");
		childPayBills = (List<CustomPayBill>) bundle.getParcelableArrayList("childs").get(0);
		layoutInflater = LayoutInflater.from(this);

		setContentView(R.layout.salary_detail);
		valueTextView = (TextView) findViewById(R.id.tv_value);
		titleTextView = (TextView) findViewById(R.id.tv_title);

		valueTextView.setText(parentPayBill.getPay_num());
		titleTextView.setText("当月" + parentPayBill.getPay_value());
		initTitle();

		ExpandableListAdapter adapter = new CustomExpendListViewAdapter();
		ExpandableListView expandListView = (ExpandableListView) findViewById(R.id.list);
		expandListView.setGroupIndicator(null);
		expandListView.setAdapter(adapter);
	}

	public class CustomExpendListViewAdapter extends BaseExpandableListAdapter {
		// 获取指定组位置、指定子列表项处的子列表项数据
		
		public Object getChild(int groupPosition, int childPosition) {
			return childPayBills.get(groupPosition).getChildPayBills().get(childPosition);
		}

		
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		
		public int getChildrenCount(int groupPosition) {
			return childPayBills.get(groupPosition).getChildPayBills().size();
		}

		// 该方法决定每个子选项的外观
		
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			convertView = layoutInflater.inflate(R.layout.salary_detail_cell2, null);
			PayBill payBill = (PayBill) getChild(groupPosition, childPosition);
			TextView nameTextView = (TextView) convertView.findViewById(R.id.tv_name);
			TextView valueTextView = (TextView) convertView.findViewById(R.id.tv_value);

			nameTextView.setText(payBill.getPay_value());
			valueTextView.setText(payBill.getPay_num());
			return convertView;
		}

		// 获取指定组位置处的组数据
		
		public Object getGroup(int groupPosition) {
			return childPayBills.get(groupPosition);
		}

		
		public int getGroupCount() {
			return childPayBills.size();
		}

		
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		// 该方法决定每个组选项的外观
		
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			convertView = layoutInflater.inflate(R.layout.salary_detail_cell, null);
			CustomPayBill customPayBill = (CustomPayBill) getGroup(groupPosition);
			ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_arrow);
			TextView nameTextView = (TextView) convertView.findViewById(R.id.tv_name);
			TextView valueTextView = (TextView) convertView.findViewById(R.id.tv_value);

			nameTextView.setText(customPayBill.getPayBill().getPay_value());
			valueTextView.setText(customPayBill.getPayBill().getPay_num());
			if (customPayBill.getChildPayBills() == null || customPayBill.getChildPayBills().size() == 0) {
				imageView.setVisibility(View.INVISIBLE);
			} else {
				imageView.setVisibility(View.VISIBLE);
				if (isExpanded) {
					imageView.setBackgroundResource(R.drawable.jt1);
				} else {
					imageView.setBackgroundResource(R.drawable.ic_btn_down);
				}
			}

			return convertView;
		}

		
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

		
		public boolean hasStableIds() {
			return false;
		}
	}
}
