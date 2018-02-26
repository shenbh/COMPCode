package com.newland.comp.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.newland.comp.activity.BaseActivity;
import com.newland.comp.fragment.BaseFragment;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

/**
 * 日期选择控件
 * 
 * @author
 */
public class DatePickDialog implements OnDateChangedListener, OnTimeChangedListener {
	// 选择样式
	private int showStyle = 2;
	// 选择样式
	public static final int YEAR = 0;
	public static final int YEAR_MONTH = 1;
	public static final int YEAR_MONTH_DAY = 2;
	public static final int YEAR_MONTH_DAY_HOUR =3 ;
	private DatePicker datePicker;
	private AlertDialog ad;
	private String date;
	private String initDate;
	private Activity activity;
	
	public static String selectTimeStr;

	/**
	 * 日期时间弹出选择框构造函数
	 * 
	 * @param activity
	 *            ：调用的父activity
	 * @param initDate
	 *            初始日期时间值，作为弹出窗口的标题和日期时间初始值
	 */

	public DatePickDialog(Activity activity, int showStyle) {
		this.activity = activity;
		this.showStyle = showStyle;
	}

	public void init(DatePicker datePicker) {
		Calendar calendar = Calendar.getInstance();
		if (!(null == initDate || "".equals(initDate))) {
			calendar = this.getCalendarByInintData(initDate);
		} else {
			if (showStyle == DatePickDialog.YEAR) {
				initDate = calendar.get(Calendar.YEAR) + "年";
			}
			if (showStyle == DatePickDialog.YEAR_MONTH) {
				initDate = calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月";
			}
			if (showStyle == DatePickDialog.YEAR_MONTH_DAY) {
				initDate = calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日 ";
			}
			if (showStyle == DatePickDialog.YEAR_MONTH_DAY_HOUR)
			{
				initDate = calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日" + calendar.get(Calendar.HOUR_OF_DAY)+"时";
			}
		}

		datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
	}

	/**
	 * 弹出日期时间选择框方法
	 * 
	 * @param inputDate
	 *            :为需要设置的日期时间文本编辑框
	 * @return
	 */
	public AlertDialog datePicKDialog(final EditText inputDate) {
		// LinearLayout dateLayout = (LinearLayout) activity
		// .getLayoutInflater().inflate(R.layout.common_date, null);
		RelativeLayout dateLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.custom_datepickerdialog, null);
		datePicker = (DatePicker) dateLayout.findViewById(R.id.datepicker);
		init(datePicker);

		ad = new AlertDialog.Builder(activity).setTitle(initDate).setView(dateLayout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				inputDate.setText(date);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				inputDate.setText("");
			}
		}).show();

		onDateChanged(null, 0, 0, 0);
		return ad;
	}

	/**
	 * 弹出日期时间选择框方法
	 * 
	 * @param inputDate
	 *            :为需要设置的日期时间文本编辑框
	 * @return
	 */
	public AlertDialog datePicKDialog(final BaseActivity baseActivity, final EditText inputDate) {
		// LinearLayout dateLayout = (LinearLayout) activity
		// .getLayoutInflater().inflate(R.layout.common_date, null);
		RelativeLayout dateLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.custom_datepickerdialog, null);
		datePicker = (DatePicker) dateLayout.findViewById(R.id.datepicker);
		init(datePicker);

		ad = new AlertDialog.Builder(activity).setTitle(initDate).setView(dateLayout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if (BaseActivity.CURRENTACTIVITY.equals("HrLeaveOfficeActivity")) {// 拟离职时间选择范围设置
					String day_time=StringUtil.DAY_TIME;
					SimpleDateFormat format=new SimpleDateFormat(day_time);
					try {
						if ((format.parse(date)).before(format.parse(StringUtil.getNowTime(day_time)))) {
							inputDate.setText(StringUtil.getNowTime(day_time));
							Toast.makeText(baseActivity, "拟离职时间不能小于当天日期", 1000).show();
						} else {
							inputDate.setText(date);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					inputDate.setText(date);
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				inputDate.setText("");
			}
		}).show();

		onDateChanged(null, 0, 0, 0);
		return ad;
	}
	public AlertDialog datePicKDialog(final TextView inputDate, final BaseFragment currentFragment) {
		// LinearLayout dateLayout = (LinearLayout) activity
		// .getLayoutInflater().inflate(R.layout.common_date, null);
		RelativeLayout dateLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.custom_datepickerdialog, null);
		datePicker = (DatePicker) dateLayout.findViewById(R.id.datepicker);
		init(datePicker);
		
		ad = new AlertDialog.Builder(activity).setTitle(initDate).setView(dateLayout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				inputDate.setText(date);
				currentFragment.refresh();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// inputDate.setText("");
			}
		}).show();
		
		onDateChanged(null, 0, 0, 0);
		return ad;
	}

	public AlertDialog datePicKDialog(final TextView inputDate) {
		// LinearLayout dateLayout = (LinearLayout) activity
		// .getLayoutInflater().inflate(R.layout.common_date, null);
		RelativeLayout dateLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.custom_datepickerdialog, null);
		datePicker = (DatePicker) dateLayout.findViewById(R.id.datepicker);
		init(datePicker);

		ad = new AlertDialog.Builder(activity).setTitle(initDate).setView(dateLayout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				inputDate.setText(date);
				selectTimeStr=date;
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			
			}
		}).show();

		onDateChanged(null, 0, 0, 0);
		return ad;
	}

	public AlertDialog datePicKDialog(final TextView inputDate, final BaseActivity activity) {
		// LinearLayout dateLayout = (LinearLayout) activity
		// .getLayoutInflater().inflate(R.layout.common_date, null);
		RelativeLayout dateLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.custom_datepickerdialog, null);
		datePicker = (DatePicker) dateLayout.findViewById(R.id.datepicker);
		init(datePicker);

		if (BaseActivity.CURRENTACTIVITY.equals("HrVacateApplyActivity")) {
			ad = new AlertDialog.Builder(activity).setTitle(initDate).setView(dateLayout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					inputDate.setText(date);
				}
			}).setNegativeButton("清空", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				 inputDate.setText("");
				}
			}).show();
		}
		if (BaseActivity.CURRENTACTIVITY.equals("MyPerformanceActivity")) {
			ad = new AlertDialog.Builder(activity).setTitle(initDate).setView(dateLayout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					inputDate.setText(date);
					activity.reflush();
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
//				 inputDate.setText("");
				}
			}).show();
		}
		

		onDateChanged(null, 0, 0, 0);
		return ad;
	}

	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		onDateChanged(null, 0, 0, 0);
	}

	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		// 获得日历实例
		Calendar calendar = Calendar.getInstance();

		calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

		SimpleDateFormat sdf = null;

		if (showStyle == DatePickDialog.YEAR) {
			sdf = new SimpleDateFormat("yyyy");
		}
		if (showStyle == DatePickDialog.YEAR_MONTH) {
			sdf = new SimpleDateFormat("yyyy-MM");
		}
		if (showStyle == DatePickDialog.YEAR_MONTH_DAY) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
		if(showStyle == DatePickDialog.YEAR_MONTH_DAY_HOUR)
		{
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss");
		}

		int SDKVersion = BaseActivity.getSDKVersionNumber();// 获取系统版本
		DatePicker dp = findDatePicker((ViewGroup) ad.getWindow().getDecorView());
		if (dp != null) {
			if (SDKVersion < 11) {
				((ViewGroup) dp.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
			} else if (SDKVersion > 14) {
				if (showStyle == DatePickDialog.YEAR) {
					initDate = calendar.get(Calendar.YEAR) + "年";
					((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
					((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
				}
				if (showStyle == DatePickDialog.YEAR_MONTH) {
					initDate = calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月";
					((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
				}
			}
		}

		date = sdf.format(calendar.getTime());
		ad.setTitle(date);
	}

	/**
	 * 从当前Dialog中查找DatePicker子控件
	 * 
	 * @param group
	 * @return
	 */
	private DatePicker findDatePicker(ViewGroup group) {
		if (group != null) {
			for (int i = 0, j = group.getChildCount(); i < j; i++) {
				View child = group.getChildAt(i);
				if (child instanceof DatePicker) {
					return (DatePicker) child;
				} else if (child instanceof ViewGroup) {
					DatePicker result = findDatePicker((ViewGroup) child);
					if (result != null)
						return result;
				}
			}
		}
		return null;
	}

	/**
	 * 实现将初始日期时间2012年07月02日拆分成年 月 日,并赋值给calendar
	 * 
	 * @param initDate
	 *            初始日期时间值 字符串型
	 * @return Calendar
	 */

	private Calendar getCalendarByInintData(String initDate) {
		Calendar calendar = Calendar.getInstance();

		// 将初始日期时间2012年07月02日 拆分成年 月 日
		String date = spliteString(initDate, "日", "index", "front"); // 日期
		String time = spliteString(initDate, "日", "index", "back"); // 时间

		String yearStr = spliteString(date, "年", "index", "front"); // 年份
		String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

		String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
		String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

		int currentYear = Integer.valueOf(yearStr.trim()).intValue();
		int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
		int currentDay = Integer.valueOf(dayStr.trim()).intValue();

		calendar.set(currentYear, currentMonth, currentDay);
		return calendar;
	}

	/**
	 * 截取子串
	 * 
	 * @param srcStr
	 *            源串
	 * @param pattern
	 *            匹配模式
	 * @param indexOrLast
	 * @param frontOrBack
	 * @return
	 */
	public static String spliteString(String srcStr, String pattern, String indexOrLast, String frontOrBack) {
		String result = "";
		int loc = -1;
		if (indexOrLast.equalsIgnoreCase("index")) {
			loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
		} else {
			loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
		}
		if (frontOrBack.equalsIgnoreCase("front")) {
			if (loc != -1)
				result = srcStr.substring(0, loc); // 截取子串
		} else {
			if (loc != -1)
				result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
		}
		return result;
	}

}
