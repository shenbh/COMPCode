package com.newland.comp.activity.my;

import java.io.File;
import java.io.IOException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.LoginActivity;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.Update;
import com.newland.comp.common.AppContext;
import com.newland.comp.service.LoopSysMsgService;
import com.newland.comp.utils.FileHelper;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.ExitDialog;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp.view.SwitchButton;
import com.newland.comp2.R;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * 系统设置界面
 * 
 * @author Administrator
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SystemSettingActivity extends BaseActivity implements OnClickListener {

	LinearLayout clearCacheLayout, passwordLayout, mobileinfoLayout, versionLayout;// 屏蔽意见反馈aboutLayout，通知与提醒noticeLayout
	Button outBtn;
	private String strImei;// 客户端的IMEI
	// private String struserid;// 当前登录的用户id
	private int sysVersionCode;// 当前操作系统版本号
	private String sysVersionName;// 当前操作系统版本名
	private String clientVersionCode;// 客户端系统版本号
	private TextView center_title;
	// private SwitchButton switch_button;
	private TextView current_version;
	LoadingDialog dialog;
	ProgressDialog mypDialog;
	// private UserInfo userInfo;
	HttpHandler<File> handler;
	FinalHttp fh = new FinalHttp();
	String TAG = "SystemSettingActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_syssetting);
		initView();
	}

	private void initView() {
		dialog = new LoadingDialog(this);
		// userInfo=(UserInfo) getIntent().getSerializableExtra("userinfo");
		// noticeLayout = (LinearLayout) findViewById(R.id.syssetting_notice);//
		// 通知与提醒
		clearCacheLayout = (LinearLayout) findViewById(R.id.syssetting_clear_cache);// 清除缓存
		passwordLayout = (LinearLayout) findViewById(R.id.syssetting_psw);// 修改密码
		mobileinfoLayout = (LinearLayout) findViewById(R.id.syssetting_mobileinfo);// 查看本机信息
		// aboutLayout = (LinearLayout)
		// findViewById(R.id.syssetting_feedback);// 意见反馈
		versionLayout = (LinearLayout) findViewById(R.id.syssetting_version);// 版本更新
		outBtn = (Button) findViewById(R.id.syssetting_out);// 退出
		center_title = (TextView) findViewById(R.id.head_center_title);
		// switch_button = (SwitchButton) findViewById(R.id.switch_button);
		current_version = (TextView) findViewById(R.id.current_version);// 显示当前版本号
		center_title.setText("系统设置");
		// noticeLayout.setOnClickListener(this);
		clearCacheLayout.setOnClickListener(this);
		passwordLayout.setOnClickListener(this);
		mobileinfoLayout.setOnClickListener(this);
		// aboutLayout.setOnClickListener(this);
		versionLayout.setOnClickListener(this);
		outBtn.setOnClickListener(this);

		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		left_btn.setVisibility(View.VISIBLE);
		left_btn.setOnClickListener(this);
		right_btn.setVisibility(View.GONE);
		left_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				SystemSettingActivity.this.finish();

			}
		});
		// switch_button.setChecked(!SharedPreferencesUtils.getConfigBoolean(getApplicationContext(),
		// SharedPreferencesUtils.Name, "isClosedPush"));
		// switch_button.setOnCheckedChangeListener(new
		// OnCheckedChangeListener() {
		//
		//
		// public void onCheckedChanged(CompoundButton arg0, boolean ischeched)
		// {
		// if (ischeched) {
		// Toast.makeText(SystemSettingActivity.this, "开启推送", 1000).show();
		// SharedPreferencesUtils.setConfigBoolean(getApplicationContext(),
		// SharedPreferencesUtils.Name, "isClosedPush", false);
		// if (!isServiceRunning(getApplication(),
		// "com.newland.comp.service.LoopSysMsgService")) // 开启服务
		// {
		// Intent intent = new Intent(getApplication(),
		// LoopSysMsgService.class);
		// startService(intent);
		// }
		// } else {
		// Toast.makeText(SystemSettingActivity.this, "关闭推送", 1000).show();
		// SharedPreferencesUtils.setConfigBoolean(getApplicationContext(),
		// SharedPreferencesUtils.Name, "isClosedPush", true);
		// if (isServiceRunning(getApplication(),
		// "com.newland.comp.service.LoopSysMsgService")) // 关闭服务
		// {
		// Intent intent = new Intent(getApplication(),
		// LoopSysMsgService.class);
		// stopService(intent);
		// }
		// }
		//
		// }
		// });
		PackageManager packageManager = getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			sysVersionName = packageInfo.versionName;// 当前操作系统的版本名
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String current_versionStr = current_version.getText() + (sysVersionName + "");
		current_version.setText(current_versionStr);

	}

	/**
	 * 清除缓存
	 */
	private void clearCache() {
		// new Thread(new Runnable() {
		// public void run() {
		FileHelper.deleteDirectory(StorageUtils.getOwnCacheDirectory(getApplicationContext(), "newland/COMP/Cache2").getAbsolutePath());
		// }
		// });
		Toast.makeText(SystemSettingActivity.this, "已清空缓存", 1000).show();
	}

	public void onClick(View arg0) {
		switch (arg0.getId()) {
		// case R.id.syssetting_notice:// 通知与提醒
		//
		// break;
		case R.id.syssetting_clear_cache:// 清除缓存
			// 二次确认是否清除缓存
			AlertDialog build = new AlertDialog.Builder(this).setTitle("提示").setMessage("是否清除缓存？").setPositiveButton("清除", new android.app.AlertDialog.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					clearCache();
				}
			}).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {

				}
			}).show();
			break;
		case R.id.syssetting_psw:// 修改密码
			Intent intentpwd = new Intent(SystemSettingActivity.this, ChangePwdActivity.class);
			startActivity(intentpwd);

			break;
		case R.id.syssetting_mobileinfo:// 查看本机信息
			mobileinfo();

			break;

		// case R.id.syssetting_feedback:// 意见反馈
		// Intent intentfeedback = new Intent(SystemSettingActivity.this,
		// FeedbackActivity.class);
		// startActivity(intentfeedback);
		//
		// break;
		case R.id.syssetting_version:// 版本更新

			getCheckVersionService();
			break;
		case R.id.syssetting_out:// 退出
			syssettingoutDialog();
			break;
		case R.id.head_left_btn:// 标题栏左侧“返回”按钮
			SystemSettingActivity.this.finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 退出当前账号对话框
	 */
	private void syssettingoutDialog() {
		new AlertDialog.Builder(this).setTitle("提示").setMessage("是否退出当前账号？").setPositiveButton("是", new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				AppContext.getInstance().tempMenuActivity.finish();
				// 清空密码
				SharedPreferencesUtils.setConfigStr(SystemSettingActivity.this, CASH_NAME, "userpwd", "");
				Intent intent = new Intent(SystemSettingActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}).setNegativeButton("否", new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		}).show();
	}

	/**
	 * 查看本机信息&当前登录用户id
	 */
	private void mobileinfo() {
		/**
		 * 获取IMEI
		 */
		strImei = BaseActivity.getSystemImei(this);// 当前设备的IMEI
		PackageManager packageManager = getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			sysVersionName = packageInfo.versionName;// 当前操作系统的版本名
			sysVersionCode = packageInfo.versionCode;// 当前操作系统的版本号
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		clientVersionCode = android.os.Build.VERSION.RELEASE + "";// 当前设备的版本号
		intentData(clientVersionCode, sysVersionCode, sysVersionName);

	}

	/**
	 * 把值传递给DeviceInfoActivity
	 * 
	 * @param sysVersionName2当前系统版本名
	 * @param sysVersionCode2当前系统版本号
	 * @param clientVersionCode2客户端版本号
	 */
	private void intentData(String sysVersionName2, int sysVersionCode2, String clientVersionCode2) {
		Intent intent = new Intent(SystemSettingActivity.this, DeviceInfoActivity.class);
		intent.putExtra("versionname", sysVersionName2);
		intent.putExtra("versioncode", sysVersionCode2 + "");
		intent.putExtra("clientversioncode", clientVersionCode2 + "");
		startActivity(intent);
	}

	/**
	 * 版本更新
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private void getCheckVersionService() {
		PackageManager packageManager = getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		final String strVersionCode = packageInfo.versionCode + "";// 当前操作系统的版本号
		String userid = SharedPreferencesUtils.getConfigStr(getApplication(), CASH_NAME, "userid");
		dialog.setTvMessage("检查更新...");
		if (!isFinishing()) {
			dialog.show(true);
		}
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("type", "01");
		params.put("method", "update_install");
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
					strMsg = replaceErroStr(strMsg);
				strMsg = "连接超时";
				Toast.makeText(getApplicationContext(), strMsg, 1000).show();
				dialog.dismiss();
			}

			@Override
			public void onSuccess(Object t) {
				System.out.println(TAG + "  " + t.toString());
				dialog.dismiss();
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) // 正常返回数据
					{
						// 服务器传回来的用户信息
						JsonInfo jsonInfo = jsonInfov2.getResultDataToClass(JsonInfo.class);
						Log.i("tag", jsonInfo.getData() + "     jsonInfo.getData()==============");

						Update update = jsonInfo.getData_exp(Update.class);
						if (update != null) {
							String reVersionId = update.getVsersion_id();// json解析返回的版本号
							String updateUrl = update.getInstall_url();// json解析返回的下载地址
							System.out.println("reVersionId  ====  " + reVersionId + "updateUrl  ===  " + updateUrl);
							//

							if (strVersionCode.equals(reVersionId)) {
								Toast.makeText(getApplicationContext(), "当前已是最新版本", 1000).show();
							} else {
								if (null == updateUrl) {
									return;
								}
								showDownLoadDialog(updateUrl);
							}
						}
					}
				} else {// 显示失败信息
					Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1000).show();
					System.out.println("版本检查失败，请检查网络================" + jsonInfov2.getResultDesc());
				}
			}

		});
	}

	/**
	 * 版本更新提示对话框
	 */
	private void showDownLoadDialog(final String updateUrl) {
		new AlertDialog.Builder(this).setTitle("确认").setMessage("有最新版本,是否更新？").setPositiveButton("立即去更新", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				doDownLoadByBrowser(updateUrl);

				// doDownLoadWork(updateUrl);
			}
		}).setNegativeButton("稍后再说", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		}).show();
	}

	/**
	 * 利用浏览器更新版本
	 */
	private void doDownLoadByBrowser(String updateUrl) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri downLoadUrl = Uri.parse(updateUrl);
		intent.setData(downLoadUrl);
		startActivity(intent);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	private void doDownLoadWork(String strurl) {

		mypDialog = new ProgressDialog(this);
		// 实例化
		mypDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// 设置进度条风格，风格为长形，有刻度的
		// mypDialog.setTitle("正在下载");
		// 设置ProgressDialog 标题
		mypDialog.setMessage("正在下载");
		// 设置ProgressDialog 提示信息
		// mypDialog.setIcon(R.drawable.ic_launcher);
		// 设置ProgressDialog 标题图标
		mypDialog.setProgress(59);
		mypDialog.setButton("取消下载", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {
				handler.stop();
				mypDialog.dismiss();

			}

		});
		// 设置ProgressDialog 进度条进度
		// 设置ProgressDialog 的一个Button
		mypDialog.setIndeterminate(false);
		// 设置ProgressDialog 的进度条是否不明确
		mypDialog.setCancelable(false);
		// 设置ProgressDialog 是否可以按退回按键取消
		mypDialog.show();
		// 让ProgressDialog显示

		// 调用download方法开始下载
		File file = new File(AppContext.CASH_DIR_APK);
		if (!file.exists()) {
			try {
				FileHelper.createDir(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String fileName = strurl.substring(strurl.lastIndexOf('/') + 1);
		handler = fh.download(strurl, new AjaxParams(), AppContext.CASH_DIR_APK + fileName, false, new AjaxCallBack() {

			@Override
			public void onLoading(long count, long current) {
				System.out.println("下载进度：" + current + "/" + count);
				mypDialog.setMax((int) count);
				mypDialog.setProgress((int) current);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = replaceErroStr(strMsg);
				// System.out.println("下载进度："+errorNo+"/"+strMsg);
				if (errorNo == 0) {
					Toast.makeText(getApplicationContext(), "用户取消下载", 1500).show();
					handler.stop();
				} else {
					Toast.makeText(getApplicationContext(), "错误提示：" + errorNo + "/" + strMsg, 1500).show();
				}

				mypDialog.dismiss();

			}

			@Override
			public void onSuccess(Object t) {
				if (t instanceof File) {
					File f = (File) t;
					Toast.makeText(getApplicationContext(), "下载完成", 1000).show();
					mypDialog.dismiss();
					installApk(((File) t).getAbsoluteFile().toString());
				}
			}
		});

	}

	public void installApk(String fileName) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
		SystemSettingActivity.this.startActivity(intent);
	}

}
