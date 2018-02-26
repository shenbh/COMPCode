package com.newland.comp.adapter.hr;



import java.util.List;
import java.util.Random;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.indicator.IndicatorAssData2;
import com.newland.comp.bean.my.IntegralSeckillData;
import com.newland.comp.bean.my.IntegralSeckillExp;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.Options;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 积分商城之积分秒杀Adapter
 * 
 * @author H81
 *
 *         2015年5月12日 下午2:13:34
 * @version 1.0.0
 */
public class SecondKillAdapter extends BaseAdapter {
	LayoutInflater inflater;

	List<IntegralSeckillData> list;
	IntegralSeckillData integralSeckillData;
	Context context;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options = Options.getListOptions();

	private SecondKillPopWindow popWin; // 秒杀泡泡弹窗
	IntegralSeckillExp integralSeckillExp;  //活动信息 
	IntegralSeckillData bean; //奖品列表
	
	boolean isSeckill;//是否可以秒杀
	public SecondKillAdapter(Context context, List<IntegralSeckillData> group_list, IntegralSeckillExp integralSeckillExp, boolean isSeckill) {
		this.context = context;
		list = group_list;
		if(context!=null){
			inflater = LayoutInflater.from(context);
		}
		this.integralSeckillExp = integralSeckillExp;
		this.isSeckill=isSeckill;
		System.out.println("Adapter isSeckill=========="+isSeckill);
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

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fragment_secondkill_item, null);
			viewHolder = new ViewHolder();

			viewHolder.mImg_prize = (ImageView) convertView.findViewById(R.id.img_prize);
			viewHolder.mTv_title = (TextView) convertView.findViewById(R.id.tv_title);
			viewHolder.mTv_needintegral = (TextView) convertView.findViewById(R.id.tv_needintegral);
			viewHolder.mBtn_secondkill = (Button) convertView.findViewById(R.id.btn_secondkill);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		 bean = list.get(position);
		imageLoader.displayImage(HttpUtils.URL_ROOT + StringUtil.noNull(bean.prize_img, ""), viewHolder.mImg_prize, options);
		viewHolder.mTv_title.setText(StringUtil.noNull(bean.prize_name));
		viewHolder.mTv_needintegral.setText("所需积分：" + StringUtil.noNull(bean.integral_num));
		viewHolder.mBtn_secondkill.setOnClickListener(new OnClickListener() {

			
			public void onClick(View v) {
				if (isSeckill) {
					popWindow(position);
				}else {
					//TODO
					System.out.println("不可以秒杀===============================================================");
					Toast.makeText(context, "活动未开始", Toast.LENGTH_SHORT).show();
				}
				// postData(position);
			}
		});
		return convertView;
	}

	class ViewHolder {
		private ImageView mImg_prize;// 图标
		private TextView mTv_title;// 标题
		private TextView mTv_needintegral;// 所需积分
		private Button mBtn_secondkill;// 秒杀按钮
	}

	private void popWindow(final int position) {
		// alpha.setVisibility(View.VISIBLE);
		// 设置透明度
		WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
		lp.alpha = 0.3f;
		((Activity) context).getWindow().setAttributes(lp);
		popWin = new SecondKillPopWindow(context, null, integralSeckillData, position);
		popWin.setOnDismissListener(new OnDismissListener() {

			
			public void onDismiss() {
				// alpha.setVisibility(View.GONE);
				WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
				lp.alpha = 1f;
				((Activity) context).getWindow().setAttributes(lp);
				System.out.println("position--------------------------------------" + position);
			}

		});

		// 显示窗口
		popWin.showAtLocation(((Activity) context).findViewById(R.id.layout), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	/**
	 * 积分秒杀提交
	 * 
	 */
	public void postData(int position) {
		AjaxParams params = new AjaxParams();
		String userid = SharedPreferencesUtils.getConfigStr(context, BaseActivity.CASH_NAME, "userid");
		params.put("userid", userid);
		params.put("method", "integral_seckill_submit");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("prize_id", StringUtil.noNull(list.get(position).prize_id));
		
		params.put("seckillBoutId", integralSeckillExp.seckillBoutId);
		params.put("seckillId", integralSeckillExp.seckillId);
		params.put("activityId", integralSeckillExp.activityId);
		params.put("prize_name", bean.prize_name);
		params.put("integral_num", bean.integral_num);
		
		System.out.println(HttpUtils.URL+"?" + params.toString());
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=new BaseActivity().replaceErroStr(strMsg);

				super.onFailure(t, errorNo, strMsg);
				Log.v("newland", strMsg + t.getMessage());
			}

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				Log.v("newland", t.toString());
				JsonInfoV3 jsonInfo = null;
				try {
					jsonInfo = JSON.parseObject(t.toString(),JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(context, "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfo != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
						Toast.makeText(context, StringUtil.noNull(jsonInfo.getResultDesc(), "秒杀成功！！！"), 1000).show();// 显示登录成功提示
					} else {
						Toast.makeText(context, StringUtil.noNull(jsonInfo.getResultDesc(), "秒杀失败！！！"), 1000).show();// 显示登录失败提示
					}
				}
			}
		});
	}

	/**
	 * 积分秒杀popwindow
	 * 
	 * @author H81
	 *
	 *         2015年5月13日 上午10:10:55
	 * @version 1.0.0
	 */
	class SecondKillPopWindow extends PopupWindow {

		private Button close;// 关闭
		private View mMenuView;
		List<IndicatorAssData2> listCom;

		private TextView mTv_verificationcode;// text显示验证码
		private EditText mEt_verificationcode;// 输入验证码
		private Button mBtn_firm;// 确认
		private Context context;
		IntegralSeckillData bean;
		boolean flag = false;// 接收验证码匹配结果

		public SecondKillPopWindow(final Context context2, OnClickListener itemsOnClick, IntegralSeckillData integralSeckillData, final int position) {
			super(context2);
			LayoutInflater inflater = (LayoutInflater) context2.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.bean = integralSeckillData;
			this.context =context2;
			mMenuView = inflater.inflate(R.layout.fragment_secondkill_popwin, null);

			bindViews();
			updateVerificationcode();// 一进来就更新验证码
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
					if (StringUtil.isEmpty(mEt_verificationcode.getText().toString())) {//验证码为空
						Toast.makeText(context, "请输入验证码", 1000).show();
					} else {
						flag = matchingVerificationcode();
						if (flag) {
							dismiss();
							postData(position);
//							Toast.makeText(context, "验证码正确", 1000).show();
						} else {
							updateVerificationcode();
//							Toast.makeText(context, "验证码错误", 1000).show();
						}
					}
				}
			});
			// 验证码
			mTv_verificationcode.setOnClickListener(new OnClickListener() {

				
				public void onClick(View v) {
					updateVerificationcode();
				}
			});
			// 设置SelectPicPopupWindow的View
			this.setContentView(mMenuView);
			// 设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(BaseActivity.SCREEN_WIDTH*2/3);
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

		/**
		 * 更新验证码
		 */
		private void updateVerificationcode() {
			Random r = new Random();
			int randomNum[] = { 1, 2, 3, 4 };
			String str = "";
			for (int i = 0; i < 4; i++) {
				randomNum[i] = r.nextInt(10);
				str += randomNum[i] + "";
			}
			mTv_verificationcode.setText(str);
			mEt_verificationcode.setText("");// 验证码输入框清空
		}

		/**
		 * 匹配验证码
		 */
		private boolean matchingVerificationcode() {
			// 匹配验证码
			if (mEt_verificationcode.getText().toString().equals(mTv_verificationcode.getText().toString())) {
				// Toast.makeText(getApplicationContext(), "验证码正确", 1000).show();
				return true;
			} else {
				Toast.makeText(context, "验证码错误", Toast.LENGTH_LONG).show();
				// updateVerificationcode();
				return false;

			}
		}

		private void bindViews() {
			mTv_verificationcode = (TextView) mMenuView.findViewById(R.id.tv_verificationcode);
			mEt_verificationcode = (EditText) mMenuView.findViewById(R.id.et_verificationcode);
			mBtn_firm = (Button) mMenuView.findViewById(R.id.btn_firm);
			close = (Button) mMenuView.findViewById(R.id.close);
			// content.setText(StringUtil.noNull(bean.guid));
		}
	}
}
