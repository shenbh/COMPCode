package com.newland.comp.activity;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.Update;
import com.newland.comp.bean.UserInfo;
import com.newland.comp.common.AppContext;
import com.newland.comp.service.LoopSysMsgService;
import com.newland.comp.utils.DESUtil;
import com.newland.comp.utils.FileHelper;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 
 * @author Administrator 登陆界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	TextView tv_verificationcode;// 验证码
	CheckBox cb_rememberpsw;// 是否记住密码
	Button btn_user_selected;
	EditText edt_verificationcode;
	LoadingDialog dialog;
	EditText edt_userid;
	EditText edt_userpwd;
	ProgressDialog mypDialog;
	// private UserInfo userInfo;
	HttpHandler<File> handler;
	String userid, userpwd;
	FinalHttp fh = new FinalHttp();
	private View yzm_layout;// 验证码所在布局
	private int loginNum = 0;// 登录次数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		initView();

		initData();
		updateVerificationcode();
		checkPushService();
	}

	private void initView() {
		dialog = new LoadingDialog(this);
		getCheckVersionService();
		edt_userid = (EditText) findViewById(R.id.edt_userid);
		edt_userpwd = (EditText) findViewById(R.id.edt_userpwd);
		cb_rememberpsw = (CheckBox) findViewById(R.id.cb_rememberpsw); // 记住密码
		TextView tv_forgetpsw = (TextView) findViewById(R.id.tv_forgetpsw); // 忘记密码
		edt_verificationcode = (EditText) findViewById(R.id.edt_verificationcode);
		tv_verificationcode = (TextView) findViewById(R.id.tv_verificationcode); // 验证码

		tv_verificationcode.setOnClickListener(this);
		btn_user_selected = (Button) findViewById(R.id.btn_user_selected);// 登录
		btn_user_selected.setOnClickListener(this);
		tv_forgetpsw.setOnClickListener(this);
		yzm_layout = this.findViewById(R.id.yzm_layout);// 验证码所在layout

	}

	/**
	 * 如果有记住密码 就默认自动填写
	 */
	private void initData() {
		edt_userid.setText(SharedPreferencesUtils.getConfigStr(getApplication(), CASH_NAME, "userid"));
		String userpwd = null;
		String desUserpwd = null;
		try {
			userpwd = SharedPreferencesUtils.getConfigStr(getApplication(), CASH_NAME, "userpwd");
			System.out.println("======================  Userpwd    " + userpwd);
			if (userpwd != null) {
				desUserpwd = DESUtil.getInstance().decrypt(userpwd);
				System.out.println("======================  desUserpwd    " + desUserpwd);
				edt_userpwd.setText(desUserpwd);
			} else {
				edt_userpwd.setText("");

			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}

		cb_rememberpsw.setChecked(SharedPreferencesUtils.getConfigBoolean(this, CASH_NAME, "isRemberPwd"));
	}

	public void onClick(View arg0) {

		userid = edt_userid.getText().toString();
		userpwd = edt_userpwd.getText().toString();
		// 登录
		if (arg0.getId() == R.id.btn_user_selected) {

			if (StringUtil.isEmpty(userid) || StringUtil.isEmpty(userpwd)) {
				Toast.makeText(getApplication(), "账号或密码为空！", 1000).show();
				return;
			}
			if (loginNum >= 3) {// 登录次数大于3次，出现验证码
				yzm_layout.setVisibility(View.VISIBLE);
				if (matchingVerificationcode()) // 匹配验证码
				{
					getLoginService();
				}
			} else {
				getLoginService();
			}
		}

		// 验证码
		if (arg0.getId() == R.id.tv_verificationcode) {
			updateVerificationcode();
		}

		// 忘记密码
		if (arg0.getId() == R.id.tv_forgetpsw) {
			forgetPwd();
		}
	}

	/**
	 * 请求网络登录
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private void getLoginService() {

		dialog.setTvMessage("正在加载...");
		if (!isFinishing()) {
			dialog.show(true);
		}
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("pwd", userpwd);
		params.put("imei", "testnum");
		params.put("method", "login");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			// fh.post("http://10.1.1.186:8990/csoa/sessionAuth/userSessionAuth.do",
			// params, new AjaxCallBack() {
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
				System.out.println(t.toString());
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
						if (cb_rememberpsw.isChecked()) {// 记住密码
							rememberUserInformation(userid, userpwd, true);
						} else {
							rememberUserInformation(userid, "", false);
						}
						// 兼容新接口
						JsonInfo jsonInfo = jsonInfov2.getResultDataToClass(JsonInfo.class);
						List<UserInfo> list = jsonInfo.getDataList(UserInfo.class);
						if (list != null && list.size() > 0) {
							UserInfo userInfo = list.get(0);
							if (userInfo != null) {
								// 保存用户中文名
								SharedPreferencesUtils.setConfigStr(LoginActivity.this, CASH_NAME, "username", StringUtil.noNull(userInfo.getUsername()));
								Intent intent = new Intent(LoginActivity.this, MenuTabActivity.class);
								intent.putExtra("userinfo", userInfo);

								startActivity(intent);
								overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);
								// overridePendingTransition(R.anim.in_translate_top,
								// R.anim.out_translate_top);
								finish();
							}
						} else {
							Toast.makeText(getApplicationContext(), "接口返回无用户信息", 1000).show();
						}
					} else {// 显示登陆失败信息
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1000).show();
						loginNum++;// 登录次数+1
						if (loginNum >= 3) {
							updateVerificationcode();
						}
					}
				}
			}
		});
	}

	/**
	 * 保存工号，密码,是否记住密码
	 */
	private void rememberUserInformation(String userid, String userpwd, boolean isCheck) {
		String enUserpwd = null;
		try {
			enUserpwd = DESUtil.getInstance().encrypt(userpwd);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SharedPreferencesUtils.setConfigStr(this, CASH_NAME, "userid", userid);
		SharedPreferencesUtils.setConfigStr(this, CASH_NAME, "userpwd", enUserpwd);
		SharedPreferencesUtils.setConfigBoolean(this, CASH_NAME, "isRemberPwd", isCheck);
		System.out.println("SharedPreferencesUtils.setConfigStr   enUserpwd  " + enUserpwd);
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
		tv_verificationcode.setText(str);
		edt_verificationcode.setText("");// 验证码输入框清空
	}

	/**
	 * 匹配验证码
	 */
	private boolean matchingVerificationcode() {
		// 匹配验证码
		if (edt_verificationcode.getText().toString().equals(tv_verificationcode.getText().toString())) {
			return true;
		} else {
			Toast.makeText(LoginActivity.this, "验证码错误", 1000).show();
			updateVerificationcode();
			return false;

		}
	}

	/**
	 * 忘记密码
	 */
	private void forgetPwd() {
		// Intent intent=new Intent(this, PwdDiscoveryActivity.class);
		// startActivity(intent);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("友情提示");

		builder.setMessage("如需找回密码，请登录综合运营管理平台电脑版使用重置密码功能，或联系系统管理员。");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.setCancelable(false);
		builder.create();
		builder.show();
	}

	/**
	 * 检查服务是否运行
	 */
	private void checkPushService() {
		if (!SharedPreferencesUtils.getConfigBoolean(this, SharedPreferencesUtils.Name, "isClosedPush"))// 用户没有关闭推送的话
		{
			if (!isServiceRunning(this, "com.newland.comp.service.LoopSysMsgService")) {
				Intent intent = new Intent(this, LoopSysMsgService.class);
				startService(intent);
			}

		}
	}

	/**
	 * 版本更新
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getCheckVersionService() {
		if (HttpUtils.UPDATE_FLAG) {
			return;
		} else {
			HttpUtils.UPDATE_FLAG = true;
		}
		PackageManager packageManager = getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		final String strVersionCode = packageInfo.versionCode + "";// 当前操作系统的版本号

		// String userid = SharedPreferencesUtils.getConfigStr(getApplication(),
		// CASH_NAME, "userid");
		dialog.setTvMessage("检查更新...");
		if (!isFinishing()) {
			dialog.show(true);
		}
		AjaxParams params = new AjaxParams();
		params.put("userid", "");
		params.put("type", "01");
		params.put("method", "update_install");
		System.out.println(HttpUtils.URL + "?" + HttpUtils.URL + "?" + params.toString());
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
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("确认").setMessage("有最新版本,是否更新？").setPositiveButton("立即去更新", new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				doDownLoadByBrowser(updateUrl);
				
				// doDownLoadWork(updateUrl);
			}
		}).setNegativeButton("稍后再说", new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		}).show();
		dialog.setCanceledOnTouchOutside(false);
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
		LoginActivity.this.startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AppContext.getInstance().exit();
			return true;
		}
		// 拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}