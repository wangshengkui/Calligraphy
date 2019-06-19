package com.jinke.smartpen;

import com.google.common.collect.ArrayListMultimap;
import com.jinke.calligraphy.app.branch.Start;
import com.tqltech.tqlpencomm.Dot;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

public class DrawfromPigaihuanContainer extends AsyncTask<Void, Integer, Long> {

	ArrayListMultimap<Integer, Dot> almm;
	DrawView dwView;
	Start start;
	
	
	public DrawfromPigaihuanContainer(ArrayListMultimap<Integer, Dot>pigaihuanContainer,DrawView drawView,Start activity) {
		// TODO Auto-generated constructor stub
		this.almm = pigaihuanContainer;
		this.dwView = drawView;
		this.start = activity;
//		if(pigaihuanContainer.equals("pigaihuanDotsContainer"))dwView.paint.setColor(Color.BLACK);
//		else dwView.paint.setColor(Color.RED);
	}
	@Override
	protected Long doInBackground(Void... arg0) {

//		// TODO Auto-generated method stub
//		if(almm.equals("pigaihuanDotsContainer")) {
//			dwView.paint.setColor(Color.BLACK);
//			start.drawfromContainer(almm, dwView);}
//		else start.drawfromContainer(almm, dwView);
			
		start.drawfromContainer(almm, dwView);
		
//		 dwView.paint.setColor(Color.BLACK);
		
		
		
		return null;
	}
	protected void onPreExecute() {
		start.dealingSomeThing=true;	
	}
	@Override
	protected void onPostExecute(Long result) {
		// TODO Auto-generated method stub
		//super.onPostExecute(result);
//		if(almm.equals("pigaihuanDotsContainer"))dwView.paint.setColor(Color.RED);
//		else dwView.paint.setColor(Color.BLACK);
		dwView.paint.setColor(Color.RED);
		start.dealingSomeThing=false;
//		start.toolFun.bIsReply=false;
		if(isCancelled())
			return;
	}


}
