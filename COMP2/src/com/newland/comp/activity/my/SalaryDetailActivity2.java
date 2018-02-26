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
public class SalaryDetailActivity2 extends Activity {
	LayoutInflater layoutInflater;
	PayBill parentPayBillYF;// 应发
	PayBill parentPayBillSF;// 实发
	List<CustomPayBill> childPayBills;
	TextView valueTextView; // 应发值
	TextView titleTextView;
	TextView tv_sf_value; // 实发工资
	TextView tv_sf_title;

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
					SalaryDetailActivity2.this.finish();
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

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.salary_detail);
		Bundle bundle = getIntent().getExtras();
		parentPayBillYF = (PayBill) bundle.getSerializable("yFparent");
		parentPayBillSF = (PayBill) bundle.getSerializable("sfParent");
		childPayBills = (List<CustomPayBill>) bundle.getParcelableArrayList("childs").get(0);
//		for (int i = 0, size = childPayBills.size(); i < size; i++) {
//			int childSize = childPayBills.get(i).getChildPayBills().size();
//			if (childSize != 0) {
//				for (int j = 0; j < childSize; j++) {
//					PayBill payBill = childPayBills.get(i).getChildPayBills().get(j);
//					if (payBill.getPay_level().equals("2") && payBill.getPay_num().equals("0") 
//							|| payBill.getPay_level().equals("2") && payBill.getPay_num().equals("0.0")
//							|| payBill.getPay_level().equals("2") && payBill.getPay_num().equals("0.00")) {
//						childPayBills.get(i).getChildPayBills().remove(j);
//						childSize -= 1;
//						j--;
//					}
//				}
//			}
//		}
		layoutInflater = LayoutInflater.from(this);

		valueTextView = (TextView) findViewById(R.id.tv_value);
		titleTextView = (TextView) findViewById(R.id.tv_title);
		tv_sf_value = (TextView) findViewById(R.id.tv_sf_value);
		tv_sf_title = (TextView) findViewById(R.id.tv_sf_title);

		valueTextView.setText(parentPayBillYF.getPay_num());
		titleTextView.setText("当月" + parentPayBillYF.getPay_value());
		tv_sf_value.setText(parentPayBillSF.getPay_num());
		tv_sf_title.setText("当月" + parentPayBillSF.getPay_value());
		initTitle();

		ExpandableListAdapter adapter = new CustomExpendListViewAdapter(childPayBills);
		ExpandableListView expandListView = (ExpandableListView) findViewById(R.id.list);
		expandListView.setGroupIndicator(null);
		expandListView.setAdapter(adapter);
	}

	public class CustomExpendListViewAdapter extends BaseExpandableListAdapter {
		List<CustomPayBill> childPayBills;
		public CustomExpendListViewAdapter(List<CustomPayBill> childPayBills) {
			this.childPayBills = childPayBills;
		}

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
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.salary_detail_cell2, null);
				viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.tv_name);
				viewHolder.valueTextView = (TextView) convertView.findViewById(R.id.tv_value);
				viewHolder.ll_salarydetailcell2 = (LinearLayout) convertView.findViewById(R.id.ll_salarydetailcell2);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			PayBill payBill = (PayBill) getChild(groupPosition, childPosition);
			viewHolder.nameTextView.setText(payBill.getPay_value());
			viewHolder.valueTextView.setText(payBill.getPay_num());
			return convertView;
		}

		class ViewHolder {
			TextView nameTextView;
			TextView valueTextView;
			LinearLayout ll_salarydetailcell2;
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
			CustomPayBill customPayBill = (CustomPayBill) getGroup(groupPosition);
			ViewHolderGroup viewHolderGroup = null;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.salary_detail_cell, null);
				viewHolderGroup = new ViewHolderGroup();
				viewHolderGroup.imageView = (ImageView) convertView.findViewById(R.id.iv_arrow);
				viewHolderGroup.nameTextView = (TextView) convertView.findViewById(R.id.tv_name);
				viewHolderGroup.valueTextView = (TextView) convertView.findViewById(R.id.tv_value);
				convertView.setTag(viewHolderGroup);
			} else {
				viewHolderGroup = (ViewHolderGroup) convertView.getTag();
			}
			viewHolderGroup.nameTextView.setText(customPayBill.getPayBill().getPay_value());
			viewHolderGroup.valueTextView.setText(customPayBill.getPayBill().getPay_num());
			if (customPayBill.getChildPayBills() == null || customPayBill.getChildPayBills().size() == 0) {
				viewHolderGroup.imageView.setVisibility(View.INVISIBLE);
			} else {
				viewHolderGroup.imageView.setVisibility(View.VISIBLE);
				if (isExpanded) {
					viewHolderGroup.imageView.setBackgroundResource(R.drawable.ic_btn_down);
				} else {
					viewHolderGroup.imageView.setBackgroundResource(R.drawable.jt1);
				}
			}
			return convertView;
		}

		class ViewHolderGroup {
			ImageView imageView;
			TextView nameTextView;
			TextView valueTextView;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

		public boolean hasStableIds() {
			return false;
		}
	}
}
