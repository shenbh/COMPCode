package com.newland.comp.adapter;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.newland.comp.bean.process.AuditData;
import com.newland.comp2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProAuditAdapter extends BaseAdapter {
	Context context;
	List<AuditData> detailDataList = new ArrayList<AuditData>();
	LayoutInflater mInflater;

	public ProAuditAdapter(Context context, List<AuditData> detailDataList) {
		this.context = context;
		this.detailDataList = detailDataList;
		this.mInflater = LayoutInflater.from(context);

	}

	public int getCount() {
		return detailDataList.size();
	}

	public Object getItem(int arg0) {
		return arg0;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View arg1, ViewGroup arg2) {
		final ViewHolder holder;
		if (arg1 == null) {
			arg1 = mInflater.inflate(R.layout.prtodo_datail_list_iterm, null);
			holder = new ViewHolder();
			holder.image2 = (ImageView) arg1.findViewById(R.id.pr_detail_listiterm_image3);
			holder.comment_txt = (TextView) arg1.findViewById(R.id.pr_detail_listiterm_txt5);
			holder.operatorName_txt = (TextView) arg1.findViewById(R.id.pr_detail_listiterm_txt2);
			holder.endDate_txt = (TextView) arg1.findViewById(R.id.pr_detail_listiterm_txt6);
			holder.title = (TextView) arg1.findViewById(R.id.pr_detail_listiterm_txt1);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		holder.title.setText(detailDataList.get(position).getName());
		holder.comment_txt.setText(URLDecoder.decode(detailDataList.get(position).getComment()));
		holder.operatorName_txt.setText(detailDataList.get(position).getOperatorName());
		holder.endDate_txt.setText(detailDataList.get(position).getEndDate());
//		if ((detailDataList.size() - 1) == position) {
//			holder.image2.setVisibility(View.GONE);
//			System.out.println("detailDataList.size() - 1) == position------------------------"+position);
//		}
		return arg1;
	}

	class ViewHolder {
		public ImageView image1, image2;
		public TextView comment_txt, operatorName_txt, endDate_txt, title;
	}

}
