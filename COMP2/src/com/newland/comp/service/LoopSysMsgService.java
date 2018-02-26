package com.newland.comp.service;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.my.MyMessageActivity;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.my.SysMsg;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 循环获取系统消息服务
 * 
 * @author H81
 * 
 */
public class LoopSysMsgService extends Service {

	private List<SysMsg> list;
	private Handler mHandler;
	String userid;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("LoopSysMsgService开启");
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					if (StringUtil.isNotEmpty(userid)) {
						refrushSysMsg();// 刷新系统消息
					}
					break;

				case 2:
					if (StringUtil.isNotEmpty(userid)) {
						refrushSysMsgNum();// 刷新系统消息数
						reflushProNum();//刷新待办流程是否有未读消息
					}
					break;
				}
			}
		};
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					getData();
				}

			}
		}).start();
		;

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	private void getData() {
		try {
			userid = SharedPreferencesUtils.getConfigStr(this, BaseActivity.CASH_NAME, "userid");
			mHandler.sendEmptyMessage(2); // 刷新系统消息数
			Thread.sleep(1000 * 20);
			mHandler.sendEmptyMessage(1);
			Thread.sleep(1000 * 60 * 5); // 每隔5分钟请求一次网络

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 刷新系统消息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void refrushSysMsg() {

		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "sys_msg");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("page_index", "1");
		params.put("page_row", "10");

		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = new BaseActivity().replaceErroStr(strMsg);
				System.out.println("服务请求网络失败" + strMsg);
			}

			@Override
			public void onSuccess(Object t) {
				System.out.println(t.toString());
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}

				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) // 正常返回数据
					{
						JsonInfo jsonInfo = jsonInfov2.getResultDataToClass(JsonInfo.class);
						list = jsonInfo.getDataList(SysMsg.class);
						if (list.size() > 0) {
//							boolean isPush = false; // 是否要推送新消息
							List<SysMsg> noRead = new ArrayList<SysMsg>(); // 筛选出未读的消息
							for (SysMsg sysMsg : list) {
								if ("0".equals(sysMsg.statue)) { // 未读
									noRead.add(sysMsg);
								}
							}
							if (noRead.size() > 0) { // 说明有未读消息
//								FinalDb db = FinalDb.create(LoopSysMsgService.this);
//								for (SysMsg sysMsg : noRead) {
//									SysMsg bean = db.findById(sysMsg.msgid, SysMsg.class);
//									if (bean == null) // 说明没有推送过这条消息
//									{
//										isPush = true;
//										db.save(sysMsg); // 保存已通知过的
//									}
//								}
//
//								if (isPush) {// 需要推送，就告诉用户有新消息
//									// 获得通知管理器
//									NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//									// 构建一个通知对象(需要传递的参数有三个,分别是图标,标题和 时间)
//									Notification notification = new Notification(R.drawable.ic_launcher, "通知", System.currentTimeMillis());
//									Intent intent = new Intent(LoopSysMsgService.this, MyMessageActivity.class);
//									PendingIntent pendingIntent = PendingIntent.getActivity(LoopSysMsgService.this, 0, intent, 0);
//									notification.setLatestEventInfo(getApplicationContext(), "通知", "您有新系统消息", pendingIntent);
//									notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动消失
//									notification.defaults = Notification.DEFAULT_SOUND;// 声音默认
//									manager.notify(1, notification);// 发动通知,id由自己指定，每一个Notification对应的唯一标志
//									// 其实这里的id没有必要设置,只是为了下面要用到它才进行了设置
//									// 其实这里的id没有必要设置,只是为了下面要用到它才进行了设置
//									// 1、由于每个Notification的ID是唯一的，所以我们可以删除某些通知：
//									// manager.cancel(id, notification);
//									// 2、同理，通过重复发送相同ID的Notification，我们还可以更新某些通知：
//									// manager.notify(id, new_notification);
//									// 3由于Notification的包装内容为Intent，我们就可以方便地为通知被点击的触发的事件传值：现在回到下面的位置来
//									// PendingIntent pendingIntent =
//									// PendingIntent.getActivity(MainActivity.this,0,new
//									// Intent(MainActivity.this,MainActivity.class),0);
//								}

							}
						}
					} else {// 显示登陆失败信息
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1500).show();
					}
				}

			}
		});
	}

	
	/**
	 * 获取未读消息数
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void refrushSysMsgNum() {
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "sys_msg_unred");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = new BaseActivity().replaceErroStr(strMsg);
				System.out.println("服务请求网络失败" + strMsg);
			}

			@Override
			public void onSuccess(Object t) {
				System.out.println(t.toString());
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfov2 != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) // 正常返回数据
					{
						JsonInfo jsonInfo = jsonInfov2.getResultDataToClass(JsonInfo.class);
						JSONObject jsonObject = jsonInfo.getData_expToJsonObject();
						// 在缓存中保存未读消息数
						SharedPreferencesUtils.setConfigStr(getApplicationContext(), SharedPreferencesUtils.Name, "unred_num", StringUtil.noNull(jsonObject.getString("unred_num"), "0"));
					} else {// 显示登陆失败信息
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1500).show();
					}
				}

			}
		});
	}

	/**
	 * 刷新待办流程是否有未处理消息
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void reflushProNum() {
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "getOperatorTasksCount");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				System.out.println("getOperatorTasksCount:"+t.toString());
				JsonInfoV3 jsonInfov3 = null;
				try {
					jsonInfov3 = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfov3 != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfov3.getResultCode())) // 正常返回数据
					{
						JSONObject json3= jsonInfov3.getResultDataToJsonObject();
						// 在缓存中保存未读消息数
						SharedPreferencesUtils.setConfigStr(getApplicationContext(), SharedPreferencesUtils.Name, "pro_unred_num", StringUtil.noNull(json3.getString("count"), "0"));
					} else {// 显示登陆失败信息
						Toast.makeText(getApplicationContext(), jsonInfov3.getResultDesc(), 1500).show();
					}
				}

			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = new BaseActivity().replaceErroStr(strMsg);
				System.out.println("服务请求网络失败" + strMsg);
			}

		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("LoopSysMsgService销毁");
	}
}
