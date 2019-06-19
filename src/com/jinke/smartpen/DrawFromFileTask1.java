package com.jinke.smartpen;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;



import com.example.readAndSave.SmartPenPage;
import com.jinke.calligraphy.app.branch.Start;

import android.R.string;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

public class DrawFromFileTask1 extends AsyncTask<Void, Integer, Long> {
	private ProgressDialog mDialog;
	private File mFile;
	private DrawView drawView;
	private Context mContext;
	private String fileName;
	private SmartPenPage smartPenPage=null;
	Start activity;
	/**
	 * 
	 * @param filename 从该文件中读取数据(笔迹点序列)
	 * @param drawView 要绘图的画布
	 * 
	 *  
	 */
	//要恢复教师的批改笔迹
	public DrawFromFileTask1(String fileName,DrawView drawView,Start activity ) {
		// TODO Auto-generated constructor stub
		super();
		this.drawView=drawView;
		this.activity=activity;
//		this.mFile=new File("/sdcard/-1/"+filenames);
		this.fileName=fileName;
/*		if(context!=null){
			mDialog = new ProgressDialog(context);
			mContext = context;
		}
		else{
			mDialog = null;
		}*/
		
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		activity.dealingSomeThing=true;
/*		if(mDialog!=null){
			mDialog.setTitle("Downloading...");
			mDialog.setMessage(mFile.getName());
			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					cancel(true);
				}
			});
			mDialog.show();
		}*/
	}
	@Override
	protected Long doInBackground(Void... params) {
		// TODO Auto-generated method stub
		if (fileName==null) {
			return null;
		}
//		android.util.Log.e("zgm", "0417：fileName："+fileName);
		drawView.paint.setColor(Color.RED);
/*		if (fileName.contains("001")) {
			drawView.paint.setColor(Color.RED);
			
//			smartPenPage=activity.getfromFile("/sdcard/xyz/"+filename,filename);
//			activity.drawsmartpenpoints(smartPenPage);
		}else {
			drawView.paint.setColor(Color.BLACK);
		}*/
	smartPenPage=activity.getfromFile("/sdcard/-1/"+fileName);
		Log.i("name","===="+"/sdcard/-1/"+fileName);
		activity.drawsmartpenpointsfromteacher(smartPenPage, drawView);
		
//		drawView.paint.setColor(Color.BLACK);

		return null;

	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		//super.onProgressUpdate(values);
		if(mDialog==null)
			return;
		if(values.length>1){
			int contentLength = values[1];
			if(contentLength==-1){
				mDialog.setIndeterminate(true);
			}
			else{
				mDialog.setMax(contentLength);
			}
		}
		else{
			mDialog.setProgress(values[0].intValue());
		}
	}
	@Override
	protected void onPostExecute(Long result) {
		// TODO Auto-generated method stub
		//super.onPostExecute(result);
		drawView.paint.setColor(Color.RED);
		activity.dealingSomeThing=false;
		/*
		if(mDialog!=null&&mDialog.isShowing()){
			mDialog.dismiss();
		}*/
		if (isCancelled()) {
			activity.dealingSomeThing = false;
			if (activity.alertDialog != null && activity.alertDialog.isShowing()) {
				activity.dealingSomeThing = false;
				activity.alertDialog.dismiss();
			}
			return;
		}
//		((MainActivity)mContext).showUnzipDialog();
		if (activity.alertDialog != null && activity.alertDialog.isShowing()) {
			activity.dealingSomeThing = false;
			activity.alertDialog.dismiss();
		}
		
		
		
	}
}
