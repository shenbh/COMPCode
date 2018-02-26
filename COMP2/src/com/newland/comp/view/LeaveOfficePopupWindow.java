package com.newland.comp.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.newland.comp.bean.hr.LeaveOffice;
import com.newland.comp.bean.indicator.IndicatorAssData2;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**离职指南泡泡弹窗
 * @author H81
 *
 */
public class LeaveOfficePopupWindow extends PopupWindow {


	private Button close;
	private View mMenuView;
	List<IndicatorAssData2> listCom;
	
    private TextView content;
    private Context context;
    LeaveOffice bean ;

    
	public LeaveOfficePopupWindow(final Activity context,OnClickListener itemsOnClick, LeaveOffice bean ) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.bean = bean;
		this.context = context;
		mMenuView = inflater.inflate(R.layout.leave_office_popwin, null);
		close = (Button) mMenuView.findViewById(R.id.close);
		//取消按钮
		close.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				context.findViewById(R.id.alpha).setVisibility(View.GONE);
				//销毁弹出框
				dismiss();
			}
		});
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		//实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0xb0000000);
		ColorDrawable dw = new ColorDrawable(0x90000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		this.setFocusable(true);               
		this.setTouchable(true);
		this.setOutsideTouchable(false);  
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.top_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						context.findViewById(R.id.alpha).setVisibility(View.GONE);
						dismiss();
					}
				}				
				return true;
			}
		});
		
		
		bindViews();
	}
	
	



	private void bindViews() {

        content = (TextView) mMenuView.findViewById(R.id.content);
        content.setText(Html.fromHtml(StringUtil.noNull(bean.guid)) );
    }
	

}
