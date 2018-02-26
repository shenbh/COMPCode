package com.newland.comp.common;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.WindowManager;

import com.newland.comp.fragment.BaseFragment;
import com.newland.comp2.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * 一些公用的方法和捕获程序全局异常
 * 
 * @author lsd
 *
 */
public class AppContext extends Application {
	private static AppContext appContext;
	private Context context;
	private FragmentTransaction transaction;
	private List<Activity> mList = new LinkedList<Activity>(); // activity所以队列
	private static AppContext instance;
	public Activity tempMenuActivity; // 菜单主界面
	public static String CASH_DIR_APK = Environment.getExternalStorageDirectory() + "/newland/COMP/apk/";// 缓存spk地址

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
		instance = this;
		initImageLoader(getApplicationContext());
	}

	/**
	 * 获取全局变量
	 * 
	 * @return
	 */
	public static AppContext getInstance() {

		return instance;
	}

	public static AppContext getAppContext() {
		if (null == appContext) {
			appContext = new AppContext();
		}
		return appContext;
	}

	public Context getContext() {
		if (null == context) {
			context = appContext.context;
		}
		return context;
	}

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	public void finishActivity(Activity activity) {
		mList.remove(activity);
	}

	/** 初始化ImageLoader */
	public static void initImageLoader(Context context) {

		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "newland/COMP/Cache2");// 获取到缓存的目录地址
		Log.d("cacheDir", cacheDir.getPath());
		// 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		// .memoryCacheExtraOptions(480, 800) // max width, max
		// height，即保存的每个缓存文件的最大长宽
		// .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null) //
		// Can slow ImageLoader, use it carefully (Better don't use
		// it)设置缓存的详细信息，最好不要设置这个
				.threadPoolSize(3)// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				// .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 *
				// 1024)) // You can pass your own memory cache
				// implementation你可以通过自己的内存缓存实现
				// .memoryCacheSize(2 * 1024 * 1024)
				// /.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())// 将保存的时候的URI名称用MD5
																		// 加密
				// .discCacheFileNameGenerator(new
				// HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .discCacheFileCount(100) //缓存的File数量
				.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
				// .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				// .imageDownloader(new BaseImageDownloader(context, 5 * 1000,
				// 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);// 全局初始化此配置
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取屏幕宽度
	 */
	public int getScreenWidth() {
		WindowManager windowManager = (WindowManager) appContext.getContext().getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int width = windowManager.getDefaultDisplay().getWidth();
		return width;
	}

	/**
	 * 改变当前的Fragment
	 * */
	public void replaceFragment(FragmentManager fragmentmanager, int containerViewId, BaseFragment fragment) {
		transaction = fragmentmanager.beginTransaction();
		// 设置切换Fragment时的动画效果
		transaction.setCustomAnimations(R.anim.move_x_in, R.anim.move_x_out);
		transaction.replace(containerViewId, fragment);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		// transaction.addToBackStack(null);
		transaction.commit();
		fragmentmanager.executePendingTransactions();
	}
}
