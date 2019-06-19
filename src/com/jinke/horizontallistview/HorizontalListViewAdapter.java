package com.jinke.horizontallistview;

import java.io.File;
import java.util.List;
import com.jinke.calligraphy.app.branch.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HorizontalListViewAdapter extends BaseAdapter {
//	private int[] mIconIDs;
	// private String[] mTitles;
	private Context mContext;
	private LayoutInflater mInflater;

	Bitmap iconBitmap;

	private int selectIndex = -1;
	private String[] mPathArray;

//	public HorizontalListViewAdapter(Context context, int[] imagesList) {
//		this.mContext = context;
//		this.mIconIDs = imagesList;
//		// this.mTitles = titles;
//		mInflater = (LayoutInflater) mContext
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// LayoutInflater.from(mContext);
//	}

	public HorizontalListViewAdapter(Context context, String[] strings) {
		// TODO Auto-generated constructor stub
		
		this.mContext=context;
		this.mPathArray=strings;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// LayoutInflater.from(mContext);	
		
		Log.v("zgm", "适配器创建成功1"+this.mPathArray.length);
		Log.v("zgm", "适配器创建成功1");
		
		
		
		
		}

	@Override
	public int getCount() {
//		return mIconIDs.length;
		Log.v("zgm","你好，你好"+ mPathArray.length);
		return mPathArray.length;
		
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater
					.inflate(R.layout.horizontal_list_item, null);
			holder.mImage = (ImageView) convertView
					.findViewById(R.id.img_list_item);
			Log.i("historyinitstatus","第1次");
			// holder.mTitle=(TextView)convertView.findViewById(R.id.text_list_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			Log.i("historyinitstatus","第2次");
		}
		if (position == selectIndex) {
			convertView.setSelected(true);
			Log.i("click","test   "+position);
		} else {
			convertView.setSelected(false);
		}

		// holder.mTitle.setText(mTitles[position]);
//		iconBitmap = getPropThumnail(mIconIDs[position]);
		iconBitmap=getThumBitmap(mPathArray[position]);
		holder.mImage.setImageBitmap(iconBitmap);
		return convertView;
	}

	private static class ViewHolder {
		// private TextView mTitle ;
		private ImageView mImage;
	}

/*	private Bitmap getPropThumnail(int id) {
		Log.i("click","id"+id);
		
		
		Drawable d = mContext.getResources().getDrawable(id);
		Bitmap b = BitmapUtil.drawableToBitmap(d);
		
		
		
		
		
		// Bitmap bb = BitmapUtil.getRoundedCornerBitmap(b, 100);
		int w = mContext.getResources().getDimensionPixelOffset(
				R.dimen.thumnail_default_width);
		int h = mContext.getResources().getDimensionPixelSize(
				R.dimen.thumnail_default_height);

		Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);

		return thumBitmap;
	}
*/	
	/**
	 * 
	 * zgm 20170522
	 * 
	*/
	private Bitmap getThumBitmap(String path) {
//		Log.i("click","id"+id);
		
		
//		Drawable d = mContext.getResources().getDrawable(id);
//		Bitmap b = BitmapUtil.drawableToBitmap(d);
//		File file=new File(path);
		Bitmap b = BitmapFactory.decodeFile(path);
		
		

		 Bitmap bb = BitmapUtil.getRoundedCornerBitmap(b, 100);
		int w = mContext.getResources().getDimensionPixelOffset(
				R.dimen.thumnail_default_width);
		int h = mContext.getResources().getDimensionPixelSize(
				R.dimen.thumnail_default_height);

		Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(bb, w, h);
//		thumBitmap.recycle();
		return thumBitmap;
	}
	
	
	
	

	public void setSelectIndex(int i) {
		selectIndex = i;
	}
}