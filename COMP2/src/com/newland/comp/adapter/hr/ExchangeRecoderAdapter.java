package com.newland.comp.adapter.hr;

import java.util.List;
import java.util.Random;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.hr.ExchangeRecord;
import com.newland.comp.bean.indicator.IndicatorAssData2;
import com.newland.comp.bean.my.FilingDetailData;
import com.newland.comp.bean.my.IntegralSeckillData;
import com.newland.comp.fragment.BaseFragment;
import com.newland.comp.fragment.ExchangeRecoderFragment;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 积分商城之兑换记录Adapter
 * 
 * @author H81
 * 
 *         2015年5月12日 上午9:42:08
 * @version 1.0.0
 */
public class ExchangeRecoderAdapter extends BaseAdapter {
	LayoutInflater inflater;
	List<ExchangeRecord> list;
	Context context;
	BaseFragment exchangeRecoderFragment;
	private EvaluatePopWindow popWin; // 评价泡泡弹窗

	public ExchangeRecoderAdapter(FragmentActivity myAttendanceLeaveActivity, List<ExchangeRecord> group_list, BaseFragment exchangeRecoderFragment) {
		this.context = myAttendanceLeaveActivity;
		list = group_list;
		inflater = LayoutInflater.from(myAttendanceLeaveActivity);
		this.exchangeRecoderFragment = exchangeRecoderFragment;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.exchangerecoder_item, null);
			viewHolder = new ViewHolder();

			viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
			viewHolder.mQuantity = (TextView) convertView.findViewById(R.id.quantity);
			viewHolder.mTotal = (TextView) convertView.findViewById(R.id.total);
			viewHolder.mTime = (TextView) convertView.findViewById(R.id.time);
			viewHolder.mType = (TextView) convertView.findViewById(R.id.type);
			viewHolder.cx = (TextView) convertView.findViewById(R.id.cx);
			viewHolder.pj = (TextView) convertView.findViewById(R.id.pj);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final ExchangeRecord bean = list.get(position);
		viewHolder.mTitle.setText(StringUtil.noNull(list.get(position).prize_name));
		viewHolder.mQuantity.setText(StringUtil.noNull(list.get(position).exchange_num));
		viewHolder.mTotal.setText(StringUtil.noNull(list.get(position).all_integral));
		viewHolder.mTime.setText(StringUtil.noNull(list.get(position).exchange_time));
		viewHolder.mType.setText(StringUtil.noNull(list.get(position).type));

		// 撤销
		viewHolder.cx.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				final LoadingDialog dialog = new LoadingDialog(context);
				dialog.show(false);

				AjaxParams params = new AjaxParams();
				String userid = SharedPreferencesUtils.getConfigStr(context, BaseActivity.CASH_NAME, "userid");
				params.put("userid", userid);
				params.put("method", "exchange_cancel");
				params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
				params.put("consumeId", bean.consumeId);// 兑换记录id

				System.out.println(HttpUtils.URL + "?" + params.toString());
				FinalHttp fh = new FinalHttp();
				fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
				fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg) {
						if (StringUtil.isNotEmpty(strMsg))
							strMsg = new BaseActivity().replaceErroStr(strMsg);
						super.onFailure(t, errorNo, strMsg);
						Log.v("newland", strMsg + t.getMessage());
						dialog.dismiss();
					}

					@Override
					public void onLoading(long count, long current) {
					}

					@Override
					public void onSuccess(Object t) {
						dialog.dismiss();
						Log.v("newland", t.toString());

						JsonInfoV3 jsonInfo = null;
						try {
							jsonInfo = JSON.parseObject(t.toString(), JsonInfoV3.class);
						} catch (Exception e) {
							Toast.makeText(context, "更新接口数据返回异常，请检查接口格式", 1000).show();
						}
						if (jsonInfo != null) {
							if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
								Toast.makeText(context, "撤销成功", 1500).show();// 显示登录失败提示
								exchangeRecoderFragment.refresh();
							} else {
								Toast.makeText(context, jsonInfo.getResultDesc(), 1500).show();// 显示登录失败提示
							}
						}
					}
				});
			}
		});

		// 评论
		viewHolder.pj.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				popWindow(bean);
			}
		});

		return convertView;
	}

	class ViewHolder {
		private TextView mTitle;// 标题
		private TextView mQuantity;// 兑换数量
		private TextView mTotal;// 总积分
		private TextView mTime;// 兑换时间
		private TextView mType;// 兑换方式
		public TextView cx;// 撤销
		public TextView pj;// 评论
	}

	private void popWindow(ExchangeRecord bean) {
		// alpha.setVisibility(View.VISIBLE);
		// 设置透明度
		WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
		lp.alpha = 0.3f;
		((Activity) context).getWindow().setAttributes(lp);
		popWin = new EvaluatePopWindow(context, null, bean);
		popWin.setOnDismissListener(new OnDismissListener() {

			public void onDismiss() {
				// alpha.setVisibility(View.GONE);
				WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
				lp.alpha = 1.0f;
				((Activity) context).getWindow().setAttributes(lp);
			}

		});

		// 显示窗口
		popWin.showAtLocation(((Activity) context).findViewById(R.id.layout), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	/**
	 * 评价popwindow
	 * 
	 * @author H81
	 * 
	 *         2015年5月13日 上午10:10:55
	 * @version 1.0.0
	 */
	class EvaluatePopWindow extends PopupWindow {

		private Button close;// 关闭
		private View mMenuView;
		List<IndicatorAssData2> listCom;

		private RatingBar ratingBar;// 评分
		private EditText ev_content;// 评价内容
		private Button mBtn_firm;// 确认
		private Context context;
		ExchangeRecord bean;
		boolean flag = false;// 接收验证码匹配结果

		public EvaluatePopWindow(final Context context2, OnClickListener itemsOnClick, final ExchangeRecord bean) {
			super(context2);
			LayoutInflater inflater = (LayoutInflater) context2.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.bean = bean;
			this.context = context2;

			mMenuView = inflater.inflate(R.layout.fragment_exchange_popwin, null);
			bindViews();
			// 取消按钮
			close.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// 销毁弹出框
					dismiss();
				}
			});
			// 确认按钮
			mBtn_firm.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (StringUtil.isEmpty(ev_content.getText().toString())) {// 验证码为空
						Toast.makeText(context, "请输入内容", 1000).show();
						return;
					}

					final LoadingDialog dialog = new LoadingDialog(context);
					dialog.show(false);

					AjaxParams params = new AjaxParams();
					String userid = SharedPreferencesUtils.getConfigStr(context, BaseActivity.CASH_NAME, "userid");
					params.put("userid", userid);
					params.put("method", "exchange_pj");
					params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
					params.put("consumeId", bean.consumeId);// 兑换记录id
					params.put("content", ev_content.getText().toString());// 评价内容
					params.put("start", ((int) ratingBar.getRating()) + "");// 评价星级

					System.out.println(HttpUtils.URL + "?" + params.toString());
					FinalHttp fh = new FinalHttp();
					fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
					fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							if (StringUtil.isNotEmpty(strMsg))
								strMsg = new BaseActivity().replaceErroStr(strMsg);
							super.onFailure(t, errorNo, strMsg);
							Log.v("newland", strMsg + t.getMessage());
							dialog.dismiss();
						}

						@Override
						public void onLoading(long count, long current) {
						}

						@Override
						public void onSuccess(Object t) {
							dialog.dismiss();
							Log.v("newland", t.toString());

							JsonInfoV3 jsonInfo = null;
							try {
								jsonInfo = JSON.parseObject(t.toString(), JsonInfoV3.class);
							} catch (Exception e) {
								Toast.makeText(context, "更新接口数据返回异常，请检查接口格式", 1000).show();
							}
							if (jsonInfo != null) {
								if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
									Toast.makeText(context, "评价成功", 1500).show();// 显示登录失败提示
									dismiss();
								} else {
									Toast.makeText(context, jsonInfo.getResultDesc(), 1500).show();// 显示登录失败提示
								}
							}
						}
					});

				}
			});
			// 设置SelectPicPopupWindow的View
			this.setContentView(mMenuView);
			// 设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(BaseActivity.SCREEN_WIDTH);
			// 设置SelectPicPopupWindow弹出窗体的高
			this.setHeight(LayoutParams.WRAP_CONTENT);
			// 设置SelectPicPopupWindow弹出窗体可点击
			this.setFocusable(true);
			// 设置SelectPicPopupWindow弹出窗体动画效果
			this.setAnimationStyle(R.style.AnimBottom);
			// 实例化一个ColorDrawable颜色为半透明
			// ColorDrawable dw = new ColorDrawable(0xb0000000);
			ColorDrawable dw = new ColorDrawable(0x90000000);
			// 设置SelectPicPopupWindow弹出窗体的背景
			this.setBackgroundDrawable(dw);
			this.setFocusable(true);
			this.setTouchable(true);
			this.setOutsideTouchable(false);
			// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
			mMenuView.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {

					int height = mMenuView.findViewById(R.id.top_layout).getTop();
					int y = (int) event.getY();
					if (event.getAction() == MotionEvent.ACTION_UP) {
						if (y < height) {
							((Activity) context2).findViewById(R.id.alpha).setVisibility(View.GONE);
							dismiss();
						}
					}
					return true;
				}
			});

		}

		private void bindViews() {
			ratingBar = (RatingBar) mMenuView.findViewById(R.id.ratingBar);
			ev_content = (EditText) mMenuView.findViewById(R.id.ev_content);
			mBtn_firm = (Button) mMenuView.findViewById(R.id.btn_firm);
			close = (Button) mMenuView.findViewById(R.id.close);
			// content.setText(StringUtil.noNull(bean.guid));
		}
	}
}
