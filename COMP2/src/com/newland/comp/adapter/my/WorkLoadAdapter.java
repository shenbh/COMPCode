package com.newland.comp.adapter.my;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.alibaba.fastjson.JSON;
import com.newland.comp.bean.indicator.IndicatorOperationItem;
import com.newland.comp.bean.my.CheckIn;
import com.newland.comp.bean.my.PayrollQos;
import com.newland.comp.bean.my.PayrollWorkload;
import com.newland.comp.utils.AnimatorUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 工作量适配器
 * 
 * @author H81
 *
 */
public class WorkLoadAdapter extends BaseAdapter {

	public int WORK = 1; // 工作折合量
	public int ORIGIN_WORK = 2; // 原始工作量
	public int FINAL_WORK = 3; // 个人实际量
	public int MAX_VALUE = 4; // 允许最大值
	public int currentShow = 1; // 当前显示哪个分类
	private Context context;
	private List<PayrollWorkload> list = new ArrayList<PayrollWorkload>();
	private LayoutInflater mInflater;
	private PopupWindow popupWindow;
	private TextView arert_info;
	private View mView; // popwin 显示在的view下面

	public WorkLoadAdapter(Context context, List<PayrollWorkload> list, View mView) {
		super();
		this.context = context;
		this.list = list;
		this.mView = mView;
		this.mInflater = LayoutInflater.from(context);
		initPopWindow();
	}

	
	public int getCount() {
		return list.size();
	}

	
	public Object getItem(int position) {
		return position;
	}

	
	public long getItemId(int position) {
		return 0;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.fwzl_cell, null);
			holder = new ViewHolder();
			holder.left_tv = (TextView) convertView.findViewById(R.id.left_tv);
			holder.right_tv = (TextView) convertView.findViewById(R.id.right_tv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PayrollWorkload bean = list.get(position);
		holder.left_tv.setText(StringUtil.noNull(bean.getSkill_value()));
		if (currentShow == WORK) // 工作折合量
		{
			holder.right_tv.setText(StringUtil.noNull(bean.getWorkload()));

		} else if (currentShow == ORIGIN_WORK) // 原始工作量
		{
			holder.right_tv.setText(StringUtil.noNull(bean.getOrigin_work()));
		} else if (currentShow == FINAL_WORK) // 个人实际值
		{
			holder.right_tv.setText(StringUtil.noNull(bean.getPerson_work()));
		} else if (currentShow == MAX_VALUE) // 允许最大值
		{
			holder.right_tv.setText(StringUtil.noNull(bean.getMax_value()));
		}

		if (Integer.parseInt(StringUtil.noNull(bean.getSkill_level())) == 1) // 说明有子节点
		{
			holder.right_tv.setTextColor(context.getResources().getColor(R.color.app_green));
			holder.left_tv.setTextColor(context.getResources().getColor(R.color.app_green));
		} else {
			holder.right_tv.setTextColor(context.getResources().getColor(R.color.black));
			holder.left_tv.setTextColor(context.getResources().getColor(R.color.black));
		}

		int sdk = android.os.Build.VERSION.SDK_INT;
//		if (sdk < 13) // 不支持pop动画
//		{
//			holder.left_tv.setOnClickListener(new OnClickListener() {
//
//				
//				public void onClick(View view) {
//					Toast.makeText(context, holder.left_tv.getText().toString(), 3000).show();
//
//				}
//			});
//		} else {
//			holder.left_tv.setOnClickListener(new OnClickListener() {
//
//				
//				public void onClick(View view) {
//					showPop(mView, holder.left_tv.getText().toString());
//				}
//			});
//		}

		return convertView;
	}

	/**
	 * 初始化popWindow
	 * */
	private void initPopWindow() {
		LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View popView = inflater.inflate(R.layout.maps_pop, null);
		popupWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		// popupWindow.setAnimationStyle(R.style.mPopAnimation);
		arert_info = (TextView) popView.findViewById(R.id.arert_info);
		popView.requestFocus();
		System.err.println("----------initPopWindow--------------");

	}

	public void showPop(View parent, String s) {
		float height = context.getResources().getDimension(R.dimen.dp_25);// 通知显示的textview默认高度
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		}

		arert_info.setText(s);
		arert_info.invalidate();
		if (arert_info.getLineCount() > 1) {
			height += (arert_info.getLineCount() - 1) * arert_info.getLineHeight();
		}

		popupWindow.showAsDropDown(parent);
		// popupWindow.showAsDropDown(parent,
		// x+(AndroidUtils.getScreenWidth()/3-getResources().getDimensionPixelSize(R.dimen.dip_120))/2,
		// 0);
		// 获取popwindow焦点
		// popupWindow.setFocusable(true);
		// 设置popwindow如果点击外面区域，便关闭。
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		AnimatorUtils.setY(popupWindow.getContentView(), height + context.getResources().getDimension(R.dimen.dp_4), 3000);

	}

	class ViewHolder {
		public TextView left_tv;
		public TextView right_tv;
		// public TextView time;//时间
	}

}
