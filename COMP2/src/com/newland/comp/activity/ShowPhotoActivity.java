package com.newland.comp.activity;

import com.newland.comp.utils.Options;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ShowPhotoActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_photo_entry);
		Intent intent = getIntent();
		String url = intent.getStringExtra("strurl");

		ImageView img = (ImageView) findViewById(R.id.large_image);

		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = Options.getListOptions();
		imageLoader.displayImage(StringUtil.noNull(url, ""), img, options);// 这个是加载网络图片的，可以是自己的图片设置方法
		img.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}

}
