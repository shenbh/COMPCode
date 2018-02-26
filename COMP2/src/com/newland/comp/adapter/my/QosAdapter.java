package com.newland.comp.adapter.my;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.bean.indicator.IndicatorOperationItem;
import com.newland.comp.bean.my.CheckIn;
import com.newland.comp.bean.my.PayrollQos;
import com.newland.comp.utils.AnimatorUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 服务质量适配器
 * 
 * @author H81
 *
 */
public class QosAdapter extends BaseAdapter {

	public int FWZL = 1; //服务质量
	public int YSKH = 2; //服务原始考核
	public int JSSM = 3; //计算说明
	public int currentShow = 1; //当前显示哪个分类
	private Context context;
	private List<PayrollQos> list = new ArrayList<PayrollQos>();
	private LayoutInflater mInflater;
	private PopupWindow popupWindow;
	private TextView arert_info;
	private View mView; //popwin 显示在的view下面
	

	public QosAdapter(Context context,List<PayrollQos> list, View mView) {
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
			convertView = mInflater.inflate(R.layout.fwzl_cell,
					null);
			holder = new ViewHolder();
			holder.left_tv = (TextView) convertView.findViewById(R.id.left_tv);
			holder.right_tv = (TextView) convertView.findViewById(R.id.right_tv);
			holder.bottom_tv = (TextView) convertView.findViewById(R.id.bottom_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PayrollQos bean = list.get(position);
		holder.left_tv.setText(StringUtil.noNull(bean.getExamine_value()));
		if(currentShow == FWZL) //服务质量
		{
			holder.right_tv.setText(StringUtil.noNull(bean.getExamine_num()));
			holder.bottom_tv.setVisibility(View.GONE);
		}
		else if(currentShow == YSKH) //原始考核项
		{
			holder.bottom_tv.setVisibility(View.GONE);
			holder.right_tv.setText(StringUtil.noNull(bean.getOrigin_examine()));
		}
		else if(currentShow == JSSM) //计算说明
		{
			holder.right_tv.setText("");
			holder.bottom_tv.setVisibility(View.VISIBLE);
			holder.bottom_tv.setText(StringUtil.noNull(bean.getCal_explain()));
		}
//       holder.left_tv.setText(StringUtil.noNull(checkIn.getClocking_exp()));
//       holder.right_tv.setText(StringUtil.noNull(checkIn.getClocking_result()));
		if(Integer.parseInt(StringUtil.noNull(bean.getExamine_level())) ==1) //说明有子节点
		{
			holder.right_tv.setTextColor(context.getResources().getColor(R.color.app_green));
			holder.left_tv.setTextColor(context.getResources().getColor(R.color.app_green));
		}
		else {
			holder.right_tv.setTextColor(context.getResources().getColor(R.color.black));
			holder.left_tv.setTextColor(context.getResources().getColor(R.color.black));
		}
//		int sdk = android.os.Build.VERSION.SDK_INT;
//		if(sdk <13) //不支持pop动画
//		{
//			holder.left_tv.setOnClickListener(new OnClickListener() {
//				
//				
//				public void onClick(View view) {
//					Toast.makeText(context, holder.left_tv.getText().toString(), 3000).show();
//					
//				}
//			});
//		}
//		else {
//			holder.left_tv.setOnClickListener(new OnClickListener() {
//				
//				
//				public void onClick(View view) {
//					showPop(mView,holder.left_tv.getText().toString());
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
		//popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		//popupWindow.setAnimationStyle(R.style.mPopAnimation);
		arert_info=(TextView) popView.findViewById(R.id.arert_info);
		popView.requestFocus(); 
		System.err.println("----------initPopWindow--------------");
	     
	}

	public void showPop(View parent,String s) {
		float height=context.getResources().getDimension(R.dimen.dp_25);//通知显示的textview默认高度
		if(popupWindow.isShowing()){
			popupWindow.dismiss();
		}
		
		arert_info.setText(s);
		arert_info.invalidate();	
		if(arert_info.getLineCount()>1){
			height+=(arert_info.getLineCount()-1)*arert_info.getLineHeight();
		}
		
		popupWindow.showAsDropDown(parent);
		//popupWindow.showAsDropDown(parent, x+(AndroidUtils.getScreenWidth()/3-getResources().getDimensionPixelSize(R.dimen.dip_120))/2, 0);
		//获取popwindow焦点
		//popupWindow.setFocusable(true);
		//设置popwindow如果点击外面区域，便关闭。
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		AnimatorUtils.setY(popupWindow.getContentView(), height+context.getResources().getDimension(R.dimen.dp_4), 3000);  
		
	}
	class ViewHolder {
		public TextView left_tv; 
		public TextView right_tv; 
		public TextView bottom_tv;  //底部计算说明
	}

	
}
