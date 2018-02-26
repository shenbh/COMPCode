package com.newland.comp.adapter.more;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.more.ProbleDetailAttachmentData;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.Options;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 员工心声问题详情附件展示
 * 
 * @author Administrator
 *
 */
public class MoreStaffAspDetailAccessoryAdapter extends BaseAdapter {
	private final int MAX_INDEX=9;
	LayoutInflater inflater;
	List<ProbleDetailAttachmentData> imgList;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options = Options.getListOptions();
	public MoreStaffAspDetailAccessoryAdapter(BaseActivity context,List<ProbleDetailAttachmentData> mlistData3) {
		inflater = LayoutInflater.from(context);
		this.imgList=mlistData3;
	}

	
	public int getCount() {
		return imgList.size();
	}

	
	public Object getItem(int position) {
		return imgList.get(position);
	}

	
	public long getItemId(int position) {
		return position;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.more_staffasp_detail_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mIv_accessory=(ImageView) convertView.findViewById(R.id.more_staff_detail_img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		imageLoader.displayImage(HttpUtils.URL_PIC_ROOT+StringUtil.noNull(imgList.get(position).attachment_img, ""), viewHolder.mIv_accessory, options);
		return null;
	}

	class ViewHolder {
		ImageView mIv_accessory;
	}
}
