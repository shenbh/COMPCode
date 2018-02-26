package com.newland.comp.view;

import com.newland.comp2.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * ********************************************************** 内容摘要 �?p> 进度�? 作�?
 * ：zcy 创建时间 �?014-7-15 上午10:15:49 当前版本号：v1.0 历史记录 : 日期 : 2014-7-15 上午10:15:49
 * 修改人： 描述 :
 *********************************************************** 
 */
public class LoadingDialog {
	private Dialog mDialog;
	private Context mContext;
	private TextView tvMessage;

	public LoadingDialog(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		mDialog = new Dialog(mContext, R.style.dialogstyle);
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null);
		tvMessage = (TextView) view.findViewById(R.id.tv_content);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
	}

	/**
	 * 
	 * @param isCancelable
	 *            是否可以点击取消
	 */
	public void show(boolean isCancelable) {
		mDialog.setCancelable(isCancelable);
		mDialog.show();
	}

	public void dismiss() {
		mDialog.dismiss();
	}

	public void isVisible() {
		tvMessage.setVisibility(View.VISIBLE);
	}

	public boolean isShowing() {
		return mDialog.isShowing();
	}

	public void setTvMessage(String message) {
		this.tvMessage.setText(message);
		this.tvMessage.setVisibility(View.VISIBLE);
	}

}
