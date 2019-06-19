package com.jinke.calligraphy.touchmode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.select.Elements;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import android.widget.Toast;

import com.jinke.calligraphy.activity.CalligraphyIndex;
import com.jinke.calligraphy.app.branch.CalliPoint;
import com.jinke.calligraphy.app.branch.Calligraph;
import com.jinke.calligraphy.app.branch.ColorPickerDialog;
import com.jinke.calligraphy.app.branch.MyView;
import com.jinke.calligraphy.app.branch.R;
import com.jinke.calligraphy.app.branch.Start;
import com.jinke.calligraphy.template.WolfTemplateUtil;
import com.jinke.single.BitmapCount;

public class SideDownMode implements TouchMode{

	private static final String TAG = "SideDownMode";
	private List<FlipItem> right_First_ItemList;
	private List<FlipItem> left_First_ItemList;
	private List<FlipItem> down_First_ItemList;
	private FlipItem temp_item;
	private FlipItem focusItem;
	private float[] tempxxxx = new float[10];
	private boolean fliping = false;
	private String drawText;
	private boolean flipper;
	private Rect[] rects;
	private Rect[] left_rects;
	private Rect[] down_rects;
	private boolean backup = false;
	
	//MediaVedio
	private MediaPlayer player;
	private int currentVol,maxVol;
	private AudioManager am;
	private boolean passed = false;
	private int musicTime = 1;
	private MusicPlayer musicPlayer;
	//
	
	MyView view;
	public static int whichComment=7;
	private Canvas mCanvas;
	public static int ifComment=0;
//	private int mWidth = 600;
	private int mWidth = Start.SCREEN_WIDTH;
//	private int mHeight = 1024;
	private int mHeight = Start.SCREEN_HEIGHT;
	private int mCornerX = 0; // ��ק���Ӧ��ҳ��
	private int mCornerY = 0;
	private Path mPath0;
	public static int flag1 = 0;
//	Bitmap mCurPageBitmap = null; // ��ǰҳ
//	Bitmap mNextPageBitmap = null;

	PointF mTouch = new PointF(); // ��ק��
	PointF mBezierStart1 = new PointF(); // �����������ʼ��
	PointF mBezierControl1 = new PointF(); // ��������߿��Ƶ�
	PointF mBeziervertex1 = new PointF(); // ��������߶���
	PointF mBezierEnd1 = new PointF(); // ��������߽����

	PointF mBezierStart2 = new PointF(); // ��һ�����������
	PointF mBezierControl2 = new PointF();
	PointF mBeziervertex2 = new PointF();
	PointF mBezierEnd2 = new PointF();

	float mMiddleX;
	float mMiddleY;
	float mDegrees;
	float mTouchToCornerDis;
	ColorMatrixColorFilter mColorMatrixFilter;
	Matrix mMatrix;
	float[] mMatrixArray = { 0, 0, 0, 0, 0, 0, 0, 0, 1.0f };

	boolean mIsRTandLB; // �Ƿ�������������
//	int mIsRTandLB;
	float mMaxLength = (float) Math.hypot(mWidth, mHeight);
	int[] mBackShadowColors;
	int[] mFrontShadowColors;
	GradientDrawable mBackShadowDrawableLR;
	GradientDrawable mBackShadowDrawableRL;
	GradientDrawable mFolderShadowDrawableLR;
	GradientDrawable mFolderShadowDrawableRL;

	GradientDrawable mFrontShadowDrawableHBT;
	GradientDrawable mFrontShadowDrawableHTB;
	GradientDrawable mFrontShadowDrawableVLR;
	GradientDrawable mFrontShadowDrawableVRL;
	
	

	Paint mPaint;
	public boolean ondrawflag = false;
	private int time = 5;
	Scroller mScroller;
	int flag = 0;
	private boolean right = true;
	private boolean left = false;
	private boolean down = false;
	private boolean over = false;
	private boolean dispear = false;
	//static final float width_test = (float) 599.999;
	//ly
	static final float width_test = (float) 859.999;

//	static final float width_test = (float) (Start.SCREEN_WIDTH - 0.001);
	
	private boolean fenye = false;

	public static boolean splitFlag = false;
	
	//构造
	public SideDownMode(MyView v) {
		this.view = v;
		doCreate();
	}
	
	
	//结合MyView里的点击event坐标数据处理，可以进入doTouch。
	//右下角：event.setLocation(event.getRawX()-300, event.getRawY());
	//(改完)
	@Override
	public void touch_action_down(MotionEvent event) {
		// TODO Auto-generated method stub
		
		float x = event.getX();
		float y = event.getY();
		Log.e("lytest", "X:"+x+"Y:"+y);
		Log.i("touchdown", "in touch_action_down:x = " + x + " y = " + y);
		Log.i("touchdown", "right over dispear fenye splitFlag : " + right + " "+ over + " "+ dispear + " "+ fenye + " "+ splitFlag);
		
		if(checkRight(event.getX(), event.getY()) || over == true) {   //if(splitFlag == true ||over == true)
			/*
			if((x < 100 && y > 533 && y < 600)){
//				splitFlag = true;
				dispear = false;
			}
			if((x > 500 && y > 533 && y < 600)){
//				splitFlag = true;
				dispear = false;
			}
			*/
			Log.i("touchdown", "goto doTouch");
			doTouch(event);
		}
	}

	//null
	@Override
	public void touch_action_pointer_down(MotionEvent event) {
		// TODO Auto-generated method stub
		
	}

	//null
	@Override
	public void touch_action_pointer_up(MotionEvent event) {
		// TODO Auto-generated method stub
		
	}

	//不需改， over之后继续split
	@Override
	public void touch_move(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.i("0801", "sideDownMode touch_mode");
		if(splitFlag || over == true)
			doTouch(event);
	}

	//不需改，如果fenye，doTouch
	@Override
	public void touch_up(MotionEvent event) {
		// TODO Auto-generated method stub
		if(fenye)
			doTouch(event);
	}

	//不需改，不消失的情况下，绘制菜单界面
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		//Log.i(TAG, "draw");
		view.baseImpl.draw(canvas, new Matrix());
		if(!dispear)
			doDraw(canvas);
	}
	
	//目录初始化（改完）
	private void initFirstRectList(){
		right_First_ItemList = new ArrayList<FlipItem>();
		left_First_ItemList = new ArrayList<FlipItem>();//不能在doCreat里初始化，原因未知
		down_First_ItemList = new ArrayList<FlipItem>();
		
		String[] names = new String[]{
				"批语智能匹配","最近批语","<能力·情感>批语","<心理·情感>批语","本页批语","缺陷发掘","引导","收藏","分享",""
		};
		String[] left_names = new String[]{
//				"","","硬笔","","颜色","","毛笔","","目录",""
//				"","","硬笔","","颜色","","毛笔","","",""
				"开始","颜色","参数设置","细硬笔","细硬笔","粗硬笔","细毛笔","细毛笔","粗毛笔",""
		};
		String[] down_names = new String[]{"一元一次方程","二次函数","旋转","4","5","6","7","8","9",""			
		};
		
		//右：一级目录
		for(int i=0;i<rects.length;i++){
			temp_item = new FlipItem();
			temp_item.setName(names[i]); 
			temp_item.setTextRect(rects[i]);

			temp_item.setType(i);
			temp_item.setEnd(false);
			//设定所有都没有下一级目录
//			if(i> 6 || i == 0 || i == 2 )
//				temp_item.setDeapper(false);//保存，分享，设置,没有下一级目录
//			else
//				temp_item.setDeapper(true);
			
			temp_item.setDeapper(false);
			right_First_ItemList.add(temp_item);
			//Log.i("rightRecText", temp_item.getName());
		}
		//左：一级目录
		for(int i=0;i<left_rects.length;i++){
			temp_item = new FlipItem();
			temp_item.setId(i);
			temp_item.setName(left_names[i]);
			temp_item.setTextRect(left_rects[i]);
			temp_item.setType(i);
			temp_item.setEnd(false);
			temp_item.setDeapper(false);//没有下一级目录
		
			left_First_ItemList.add(temp_item);
		}
		//下：一级目录
		for(int i=0;i<down_rects.length;i++){
			temp_item = new FlipItem();
			temp_item.setId(i);
			temp_item.setName(down_names[i]);
			temp_item.setTextRect(down_rects[i]);
			temp_item.setType(i);
			temp_item.setEnd(false);
			temp_item.setDeapper(false);//没有下一级目录
		
			down_First_ItemList.add(temp_item);
		}
		//右：二级目录
		List<FlipItem> right_Second_ItemList = new ArrayList<FlipItem>();
		String[] second_names = new String[]{
				"开始","云文档模板","待定","书法","格子纸记事本","书笺型记事本","主题型记事本","商务记事本","更多","保存"
		};		
		for(int i=0;i<rects.length;i++){
			temp_item = new FlipItem();
			if(i< 2 || i == rects.length-1){
				temp_item.setInside(true);
				temp_item.setDeapper(true);
			}
			temp_item.setName(second_names[i]);
			
/*			
			//ly
			//这个应该是旧的无用代码
			switch (i) {
			case 2:
				try {
					temp_item.setSmallBitmap(BitmapFactory.decodeResource(Start.context.getResources(), R.drawable.note_2_1));
					BitmapCount.getInstance().createBitmap("SideDownMode initFirstRectList note_2_1");

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					Log.e("AndroidRuntime", "SideDownMode initFirstRectList() OOM!!!"); 
				}
				break;
			case 3:
				try{
					temp_item.setSmallBitmap(BitmapFactory.decodeResource(Start.context.getResources(), R.drawable.note_2_2));
					BitmapCount.getInstance().createBitmap("SideDownMode initFirstRectList note_2_2");
				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					Log.e("AndroidRuntime", "SideDownMode initFirstRectList() OOM!!!"); 
				}
				break;
			case 4:
				try{
					temp_item.setSmallBitmap(BitmapFactory.decodeResource(Start.context.getResources(), R.drawable.note_2_3));
					BitmapCount.getInstance().createBitmap("SideDownMode initFirstRectList note_2_3");
				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					Log.e("AndroidRuntime", "SideDownMode initFirstRectList() OOM!!!"); 
				}
				break;
			case 5:
				try{
					temp_item.setSmallBitmap(BitmapFactory.decodeResource(Start.context.getResources(), R.drawable.note_2_4));
					BitmapCount.getInstance().createBitmap("SideDownMode initFirstRectList note_2_4");
				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					Log.e("AndroidRuntime", "SideDownMode initFirstRectList() OOM!!!"); 
				}
				break;
			case 6:
				try{
					temp_item.setSmallBitmap(BitmapFactory.decodeResource(Start.context.getResources(), R.drawable.note_2_5));
					BitmapCount.getInstance().createBitmap("SideDownMode initFirstRectList note_2_5");
				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					Log.e("AndroidRuntime", "SideDownMode initFirstRectList() OOM!!!"); 
				}
				break;
			case 7:
				try{
					temp_item.setSmallBitmap(BitmapFactory.decodeResource(Start.context.getResources(), R.drawable.note_2_6));
					BitmapCount.getInstance().createBitmap("SideDownMode initFirstRectList note_2_6");
				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					Log.e("AndroidRuntime", "SideDownMode initFirstRectList() OOM!!!"); 
				}
				break;
			default:
				break;
			}
 */
			temp_item.setTextRect(rects[i]);
			temp_item.setType(i);
			temp_item.setEnd(false);
			right_Second_ItemList.add(temp_item);
		}
		right_First_ItemList.get(6).setRight_Second_ItemList(right_Second_ItemList);
		Log.e("right","second"+ (right_First_ItemList.get(6).getRight_Second_ItemList()));
	}
	
	//初始化（改完）
	public void doCreate(){
		
		//初始化AudioManager
		am = (AudioManager)Start.instance.getSystemService(Context.AUDIO_SERVICE);
		maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		//MediaPlayer初始化
		Uri uri = Uri.parse("file:///android_asset/pagesound.mp3");
//		player = MediaPlayer.create(Start.context, R.raw.pagesound);
		player = new MediaPlayer();
		try {
			player.setDataSource(Start.context, uri);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player.setLooping(false);
		
		musicPlayer = new MusicPlayer(player);
		
		mCanvas = new Canvas();
		mPath0 = new Path();
		over = false;
		mPaint = new Paint();
		mPaint.setColor(Color.BLUE);
//		right_First_ItemList = new ArrayList<FlipItem>();
//		left_First_ItemList = new ArrayList<FlipItem>();//在这里初始化是不可以滴
		int dst = (int)(mWidth - width_test);
		Log.i("touchdown", "in doCreate: dst = " + dst);
		rects = new Rect[]{
//				new Rect(10 + dst,533,60 + dst,600),
//				new Rect(67 + dst,475, 200 + dst,600),
//				new Rect(125 + dst,418, 225 + dst,475),
//				new Rect(182 + dst,360, 282 + dst,418),
//				new Rect(240 + dst,303, 340 + dst,360),
//				new Rect(297 + dst,245, 397 + dst,303),
//				new Rect(355 + dst,188, 455 + dst,245),
//				new Rect(412 + dst,130, 512 + dst,188),
//				new Rect(470 + dst,73, 570 + dst,130),
//				new Rect(527 + dst,0, 600 + dst,73)
//				new Rect(0 + dst,533,90 + dst,600), //开始
				new Rect(50 + dst,700,180 + dst,800), //开始
				new Rect(97 + dst,475, 200 + dst,700), //空白
				new Rect(180 + dst,600, 282 + dst,680), //目录
				new Rect(282 + dst,530, 370 + dst,600), //个性字库
				new Rect(370 + dst,420, 455 + dst,495), //云国画
				new Rect(450 + dst,330, 550 + dst,420), //云书法
				new Rect(535 + dst,250, 650 + dst,335), //云文档
				new Rect(630 + dst,150, 722 + dst,230), //保存
				new Rect(700 + dst,70, 800 + dst,160), //分享
				new Rect(527 + dst,0, 600 + dst,73) //null
		};
				
		left_rects = new Rect[]{
//				new Rect(),
//				new Rect(435 ,460 ,535 ,560 ),
//				new Rect(375 ,418, 475 ,475),
//				new Rect(300 ,350,400 ,450),
//				new Rect(240 ,300, 340 ,360),
//				new Rect(190 ,250,290 ,350),
//				new Rect(145 ,194, 245 ,244),
//				new Rect(80 ,150,180 ,200),
//				new Rect(20 ,106,190 ,128),
//				new Rect()
				new Rect(),
				new Rect(690 ,720 ,800 ,800 ),
				new Rect(590 ,620, 700 ,700),
				new Rect(500 ,530,620 ,620),
				new Rect(430 ,450, 530 ,530),
				new Rect(340 ,350, 440 ,440),
				new Rect(250 ,250, 350 ,350),
				new Rect(150 ,160, 250 ,260),
				new Rect(60 ,60, 160 ,160),
//				new Rect(800 ,850, 1200 ,1000),
				new Rect()
		};

		Log.i("touchdown", "screen height = " + mHeight);
		
		down_rects = new Rect[]{
				new Rect(10 + dst,2120, 160 + dst,2220), 
				new Rect(110 + dst,2225, 250 + dst,2320), 
				new Rect(200 + dst,2325, 350 + dst,2425),
				
				new Rect(230 + dst,1520, 340 + dst,1620), 
				new Rect(270 + dst,1620, 380 + dst,1720), 
				new Rect(350 + dst,1720, 450 + dst,1820), 
				new Rect(410 + dst,1820, 510 + dst,1920), 
				new Rect(490 + dst,1920, 600 + dst,2020), 
				new Rect(560 + dst,2020, 670 + dst,2120), 
				new Rect(620 + dst,2120, 720 + dst,2220) 
		};
		
		initFirstRectList();
		//暂时设置云模板二级目录
		
		
//		mPaint.setStyle(Paint.Style.FILL);

		ColorMatrix cm = new ColorMatrix();
		float array[] = { 0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0,
				0.55f, 0, 80.0f, 0, 0, 0, 0.2f, 0 };
		cm.set(array);
		mColorMatrixFilter = new ColorMatrixColorFilter(cm);
		mMatrix = new Matrix();
		mScroller = new Scroller(view.getContext());

		mTouch.x = 0.01f; // ����x,yΪ0,�����ڵ����ʱ��������
		mTouch.y = 0.01f;
	}
	
	//音乐（不需改）
	private void music(){
		
		if(musicTime == 1 && passed == true && !player.isPlaying()){
			if(focusItem != null && ("维尼系列记事本".equals(focusItem.getName()) || "五线谱记事本".equals(focusItem.getName())
					|| "格子纸记事本".equals(focusItem.getName()))){
				
			}else{
				player.start();
				
			}
			musicTime = 0;
			passed = false;
		}
		
		
	}
	
	public boolean rightFlipTouch(MotionEvent event){
		
		if(focusItem!= null && !focusItem.isDeapper()){
			//没有下一级目录，处理点击事件
			return true;
		}
		//改到draw里，用数组排序实现
		fliping = true;
		mTouch.x = event.getX();
		mTouch.y = event.getY();
		
		float x = width_test - event.getX();          //width_test = 860
		float y = event.getY();
		
		if(y >width_test){
			return true;	
		}
		Log.v("media", "x:"+mTouch.x+" y:"+mTouch.y);
		if(right){
			if(focusItem != null)
			flipper = true;
			if(x > y) {
				mTouch.x = width_test -x;
				mTouch.y = x;
			}
			else{
				mTouch.x = width_test-y;
				mTouch.y = y;
			}
		}else{
			if(mTouch.x > mTouch.y) {
				
				mTouch.x = mTouch.y-0.01f;
			}
			else{
				mTouch.y = mTouch.x-0.01f;
			}
		}
		
		view.invalidate();
		return true;
	}
	
	private void whichRect(float x,float y,boolean right, boolean left, boolean down){
		musicTime = 1;
		if(right){
			for(int i=0;i<right_First_ItemList.size();i++){
				Log.e("right", "set focusItem:"+ right_First_ItemList.get(i).getName() 
						+ "second:"+(right_First_ItemList.get(i).getRight_Second_ItemList() == null));
				if(right_First_ItemList.get(i).getTextRect().contains((int)x, (int)y)){
					Log.i("touchdown", "whichRect right: x =" + x + " y = " + y);
//					Toast.makeText(view.getContext(), right_First_ItemList.get(i).getName()+"点击了", Toast.LENGTH_SHORT).show();
					right_First_ItemList.get(i).setTouch(true);
					focusItem = right_First_ItemList.get(i);
					Log.e("right", "set focusItem:"+ focusItem.getName() 
							+ "second:"+(focusItem.getRight_Second_ItemList() == null));
				}
			}
		}else if(left){
			for(int i=0;i<left_First_ItemList.size();i++){
				Log.e("touched","-----------------------x:"+x+" y:"+y);
				Log.e("touched","-------------rect:"+left_First_ItemList.get(i).getTextRect().toShortString());
				if(left_First_ItemList.get(i).getTextRect().contains((int)x, (int)y)){
					Log.i("touchdown", "whichRect left: x =" + x + " y = " + y);
//					Toast.makeText(view.getContext(), left_First_ItemList.get(i).getName()+"点击了", Toast.LENGTH_SHORT).show();
			        left_First_ItemList.get(i).setTouch(true);
					focusItem = left_First_ItemList.get(i);
					Log.e("INIT", "SET!!!!!!!!! :" + left_First_ItemList.get(i).getTextRect().toShortString());
				}
			}
		} else if(down) {
			for(int i=0;i<down_First_ItemList.size();i++){
				Log.e("touched","-----------------------x:"+x+" y:"+y);
				Log.e("touched","-------------rect:"+left_First_ItemList.get(i).getTextRect().toShortString());
				if(down_First_ItemList.get(i).getTextRect().contains((int)x, (int)y)){
					Log.i("touchdown", "whichRect down: x =" + x + " y = " + y);
//					Toast.makeText(view.getContext(), down_First_ItemList.get(i).getName()+"点击了", Toast.LENGTH_SHORT).show();
			        down_First_ItemList.get(i).setTouch(true);
					focusItem = down_First_ItemList.get(i);
					Log.e("INIT", "SET!!!!!!!!! :" + down_First_ItemList.get(i).getTextRect().toShortString());
				}
			}
		
		}
	}
	//Action up-move-down 改完
	public boolean doTouch(MotionEvent event){
		float x = event.getX();
		float y = event.getY();
		Log.e("down", "dotouch ");
		Log.e("mytest","X:"+x+"y:"+y);
		Log.i("touchdown","doTouch X:"+x+"y:"+y);
		
		//action_move（改完）*******************************************************
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			Log.e("bug", "mWebView ACTION_MOVE"+index);
			Log.e("down", "move ");
			Log.i("touchdown", "doTouch ACTION_MOVE");
			Log.i("touchdown", "right left down over dispear fenye splitFlag : " + right + " " + left + " " + down + " "+ over + " "+ dispear + " "+ fenye + " "+ splitFlag);
			//ly
			//if(y>600){
			if(y>900 && y < 2200){
				return true;
			}
			Log.i("touchdown", "begin to run");
			 
			//右侧逻辑
			if(right){
//				musicPlayer.play(x);
				
				Log.e("backup", ""+backup);
				if(!backup && event.getX() < 550){
					
					passed = true;
					music();
				}else if(backup && event.getX() > 50){
					passed = true;
					music();
				}
			}else if(left){
				//左侧逻辑,与右侧相反
				if(backup && event.getX() < 300 ){
					
					passed = true;
					music();
				}else if(!backup && event.getX() > 300){
					passed = true;
					music();
				}
			} else if(down) {
				if(!backup && event.getX() < 550) {
					passed = true;
					music();
				}else if(backup && event.getX() > 50) {
					passed = true;
					music();
				}
			}
			
			if(over && right){
				return rightFlipTouch(event);
			}if(over && left){
				return true;
			}if(over && down) { //分页后再点击over不再分页
				return true;
			}
			else{
				Log.e("down", "move dotouchEvent");
				return doTouchEvent(event);
			}
		}
		//action_move（改完）*******************************************************
		
		//action_down*******************************************************
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Log.e("INIT", "mWebView ACTION_DOWN over:"+over);
			Log.i("touchdown", "ACTION_DOWN");
			Log.i("touchdown", "x = " +x +" y = " +y);
			//over判断
			if(over){
				if(!down) {
					//if(y>600){
					if(y>800){
						Log.e("bug", "mWebView ACTION_DOWN y>800（600） dispearfenye()");
						dispearfenye();
						return true;
					}
					if(!right){
						//左侧按下
						//ly
						//if((x > 500 && y > 533 && y < 600)){
						if((x > 900 && y > 933 && y < 0)){
							fenye = true;
							over = false;
							right = false;
							left = true;
							down = false;
							backup = true;//shouhui
							musicTime = 1;
							Log.e("down", " left 收回");
						}else{//判断是否是收回区域
							Log.i("touchdown", "action_down left : x = " + x + " y = " + y);
							whichRect(x,y,false,true,down);//left
						}
						return true;
					}else{
						float dst = mWidth - width_test;
						Log.e("down", "x:" + x + " y:" + y + " 100+dst:" + (100+dst));
						if((x < 100 + dst && y > 533 && y < 600)){
							fenye = true;
							over = false;
							backup = true;//shouhui
							right = true;
							left = false;
							down = false;
							musicTime = 1;
							Log.e("down", "收回");
		//					Log.e("bug", "mWebView ACTION_DOWN 收回");
						}else{
							Log.e("down", "收回 which");
							Log.i("touchdown", "action_down right : x = " + x + " y = " + y);
							whichRect(x,y,true,false,down);//right
						}
						Log.e("down", "return true");
						return true;
					}
				}	
				
				if(down) {
					if(y<800){
						Log.e("bug", "mWebView ACTION_DOWN y>800（600） dispearfenye()");
						dispearfenye();
						return true;
					}
					float dst = mWidth - width_test;
					if((x < 100 + dst && y < 533 && y > 500)) {
						fenye = true;
						over = false;
						backup = true;
						right = false;
						left = false;
						down = true;
						musicTime = 1;
						Log.e("down", "down收回");
					} else {
						Log.e("down", "收回");
						whichRect(x,y,left,right,down);
					}
					Log.e("down", "return true");
					return true;
				}
			}//over判断
			else{
				Log.e("down", "over false");
				if(x>500 && y<200){
					fenye = true;
					over = false;
					right = true;
					left = false;
					down = false;
					dispear = false;
					musicTime = 1;
					initFirstRectList();
					Log.e("INIT", "!!!!!!!!!!!!!!!!!");
					Log.v("bug", "apear x>400 y<200!!!!!!!!!!!!!!!!!set fenye ="+fenye +" set over=false");
				}
				if(x<100 && y<200){
					fenye = true;
					over = false;
					right = false;
					left = true;
					down = false;
					dispear = false;
					musicTime = 1;
					Log.v("bug", "apear x>400 y<200!!!!!!!!!!!!!!!!!set fenye ="+fenye);
				}
				if(x>500 && y > 2200) {
					Log.i("touchdown","press down");
					fenye = true;
					over = false;
					right = false;
					left = false;
					down = true;
					dispear = false;
					musicTime = 1;		
				}
				if(fenye == false){
//					return super.onTouchEvent(event);
					return false;
				}
				
				return doTouchEvent(event);
			}
			
		}
		//action_down(改完)*******************************************************
		
		//action_up(改完)*******************************************************
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.e("touch", "mWebView ACTION_UP mTouchToCornerDis:"+mTouchToCornerDis);
			
			if(over){
				
				if(left){
					if(focusItem != null){
						focusItem.setTouch(false);//设置未选中
						String focusItemName;
						//"开始","颜色","","细硬笔（红）","细硬笔（黑）","粗硬笔（黑）","细毛笔（红）","细毛笔（黑）","粗毛笔（黑）",""
						if(focusItem.getId() == 2) { //参数设置
							dispearfenye();
					        Start.parameterDialog.create();
						}
//						if("细硬笔".equals(focusItem.getName())){
						else if(focusItem.getId() == 3){
							view.changePenState(MyView.STATUS_PEN_HARD);
							view.hardImpl.paint.setStrokeWidth((3));
							view.hardImpl.paint.setColor(Color.RED);
							focusItemName = focusItem.getName()+"（红）";
							dispearfenye();
							Toast.makeText(Start.context, focusItemName, Toast.LENGTH_SHORT).show();
//						}else if("细硬笔".equals(focusItem.getName())){
						}else if(focusItem.getId() == 4){
							view.changePenState(MyView.STATUS_PEN_HARD);
							view.hardImpl.paint.setStrokeWidth((3));
							view.hardImpl.paint.setColor(Color.BLACK);
							focusItemName = focusItem.getName()+"（黑）";
							dispearfenye();
							Toast.makeText(Start.context, focusItemName, Toast.LENGTH_SHORT).show();
//						}else if("粗硬笔".equals(focusItem.getName())){
						}else if(focusItem.getId() == 5){
							view.changePenState(MyView.STATUS_PEN_HARD);
							view.hardImpl.paint.setStrokeWidth((7));
							view.hardImpl.paint.setColor(Color.BLACK);
							focusItemName = focusItem.getName();
							dispearfenye();
							Toast.makeText(Start.context, focusItemName, Toast.LENGTH_SHORT).show();
//						}else if("细毛笔".equals(focusItem.getName())){
						}else if(focusItem.getId() == 6){
							view.changePenState(MyView.STATUS_PEN_CALLI);
							CalliPoint.SIZE_MIN = (2);
							CalliPoint.SIZE_MAX = (6);
							view.calliImpl.mPaint.setStrokeWidth((2));
							view.calliImpl.mPaint.setColor(Color.RED);
							focusItemName = focusItem.getName()+"（红）";
							dispearfenye();
							view.calliImpl.mPathPaint.set(view.calliImpl.mPaint);
							Toast.makeText(Start.context, focusItemName, Toast.LENGTH_SHORT).show();
//						}else if("细毛笔".equals(focusItem.getName())){
						}else if(focusItem.getId() == 7){
							view.changePenState(MyView.STATUS_PEN_CALLI);
							CalliPoint.SIZE_MIN = (2);
							CalliPoint.SIZE_MAX = (40);
							view.calliImpl.mPaint.setStrokeWidth((5));
							view.calliImpl.mPaint.setColor(Color.BLACK);
							focusItemName = focusItem.getName()+"（黑）";
							dispearfenye();
							view.calliImpl.mPathPaint.set(view.calliImpl.mPaint);
							Toast.makeText(Start.context, focusItemName, Toast.LENGTH_SHORT).show();
//						}else if("粗毛笔".equals(focusItem.getName())){
						}else if(focusItem.getId() == 8){
							view.changePenState(MyView.STATUS_PEN_CALLI);
							
//							CalliPoint.SIZE_MIN = (5);
//							CalliPoint.SIZE_MAX = (30);
							
							CalliPoint.SIZE_MIN = (10);
							CalliPoint.SIZE_MAX = (100);
							//view.calliImpl.mPaint.setStrokeWidth((5));
							view.calliImpl.mPaint.setStrokeWidth((12));
							view.calliImpl.mPaint.setColor(Color.BLACK);
							focusItemName = focusItem.getName();
							dispearfenye();
							view.calliImpl.mPathPaint.set(view.calliImpl.mPaint);
							Toast.makeText(Start.context, focusItemName, Toast.LENGTH_SHORT).show();
						}
						
//						else if("颜色".equals(focusItem.getName())){
						else if(focusItem.getId() == 1){
							//颜色
							new ColorPickerDialog(view.getContext(), view, view.baseImpl.bPaint.getColor()).show();
							dispearfenye();
						}
						focusItem = null;
						
//						if("毛笔".equals(focusItem.getName())){
//							//毛笔
//							view.changePenState(MyView.STATUS_PEN_CALLI);
//							dispearfenye();
//						}else if("硬笔".equals(focusItem.getName())){
//							//硬笔
//							view.changePenState(MyView.STATUS_PEN_HARD);
//							dispearfenye();
//						}else if("颜色".equals(focusItem.getName())){
//							//颜色
//							new ColorPickerDialog(view.getContext(), view, view.baseImpl.bPaint.getColor()).show();
//							dispearfenye();
//						}

					}
//					leftTouchUp(x,y);
				}else if(right){
					Log.e("touch", "mWebView a over:================="+over);
					Log.v("touch", "x:"+x+" y:"+y+" over:"+over);
					Log.i("rightRecText", focusItem.getName());
					/*
					 * 调试二级目录，暂时住掉点击事件
					 * */
	//				if(right)
	//					rightTouchUp(x,y);
	//				else{
	//					leftTouchUp(x,y);
	//				}
					
	//				return true;
//					Log.e("right", "CHANGE!!!!!!!!!"+" focusItem:"+(focusItem != null)
//							+" focusItem.getRight_Second_ItemList()!= null:"+(focusItem.getRight_Second_ItemList()!= null)+
//							"focusItem:"+focusItem.getName());
					if(focusItem != null)
					if(focusItem.isDeapper()){
						//存在下一级目录，松手后，设置进入下一层目录
						if(focusItem != null && focusItem.getRight_Second_ItemList()!= null){
							Log.e("right", "deapper");
							right_First_ItemList = focusItem.getRight_Second_ItemList();
							Log.e("INIT", "CHANGE!!!!!!!!!");
						}
						focusItem.setTouch(false);//设置没有选中
						flipper = false;
						fliping = false;
					}else{
//						Toast.makeText(view.getContext(), focusItem.getName()+"点击了", Toast.LENGTH_SHORT).show();
						if("保存".equals(focusItem.getName())){
							//保存代码
//							view.freeBitmap.drawFreeBitmapSync();
							//Start.c.view.cursorBitmap.updateHandwriteState();
//							view.saveDatebaseBak();
							view.saveDatebase();
							Toast.makeText(view.getContext(), "保存点击了",Toast.LENGTH_SHORT);
							Log.i("0801", "done");
							//view.saveDatebase();
//							new UploadCommand(Start.context, null).execute();
							dispearfenye();		  
						//	Start.c.view.cursorBitmap.updateHandwriteState();
						}else if("本页批语".equals(focusItem.getName())){
							//2016.4.29本来这里是分享
//							view.share();
//				            dispearfenye();
//				            Start.c.view.cursorBitmap.updateHandwriteState();
					//		Toast.makeText(view.getContext(), "本页批语", Toast.LENGTH_SHORT).show();
							AlertDialog.Builder  builder = new AlertDialog.Builder(view.getContext());
							builder.setTitle("本页批语");
							String[] str = new String[7];
							System.arraycopy(Calligraph.str1, 0,str , 0, 6);
							  builder.setSingleChoiceItems(  str, 0,  new DialogInterface.OnClickListener() 
							   {
								   public void onClick(DialogInterface dialog, int which) { 
								//	   dialog.dismiss();
									
									   
									   whichComment=which;
									   Calligraph.tounaoText.setVisibility(View.VISIBLE);
									   
									   
								   }
							   });

							builder.setPositiveButton("提交评价", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									ifComment = 1;
									 	Toast.makeText(view.getContext(), "评价已提交。", Toast.LENGTH_SHORT).show();
									arg0.cancel();							
								}
							});					
							builder.create().show();								
						}else if("书笺型记事本".equals(focusItem.getName())){
							view.doChangeBackground(WolfTemplateUtil.BRIEFNOTE, true);
							dispearfenye();
						}else if("主题型记事本".equals(focusItem.getName())){
							view.doChangeBackground(WolfTemplateUtil.NOTEBOOK, true);
							dispearfenye();
						}else if("商务记事本!!".equals(focusItem.getName())){
							view.doChangeBackground(WolfTemplateUtil.BUSINESS, true);
							dispearfenye();
						}else if("格子纸记事本".equals(focusItem.getName())){
							view.doChangeBackground(WolfTemplateUtil.CHECKER, true);
							dispearfenye();
						}else if("维尼系列记事本".equals(focusItem.getName())){
							view.doChangeBackground(WolfTemplateUtil.WINI, true);
							dispearfenye();
						}else if("目录".equals(focusItem.getName())){
							Toast.makeText(view.getContext(), "目录点击了!!!!!!!", Toast.LENGTH_SHORT).show();
							
							Intent i = new Intent();
							i.setClass(Start.context, CalligraphyIndex.class);
							Start.context.startActivity(i);
							
//							view.openDirectory();
							dispearfenye();
						}else if("缺陷发掘".equals(focusItem.getName())){
							Toast.makeText(view.getContext(), "本页批语", Toast.LENGTH_SHORT).show();
							dispearfenye();
						}else if("<能力·情感>批语".equals(focusItem.getName())) {
//						Toast.makeText(view.getContext(), "点击了!!!!!!!", Toast.LENGTH_SHORT).show();
						dispearfenye();
						
//						AlertDialog.Builder  builder = new AlertDialog.Builder(view.getContext());
//						builder.setTitle("试卷评价");
//						builder.setMessage("逻辑清晰，答案标准");
//						builder.setNegativeButton("重新评价", new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int which) {
//								//Toast.makeText(view.getContext(), "评价提交中...", Toast.LENGTH_SHORT).show();
//								dialog.cancel();
//							}
//						});
						AlertDialog.Builder  builder = new AlertDialog.Builder(view.getContext());
						builder.setTitle("<能力·情感>批语");
						
//						String[] str2 = {"对角线概念要清晰，再碰到类似题目可以多画图","计算要认真点，题目准确率会更高","要热爱学习，再复习下函数的概念",
//								"审题过程要细致，下次肯定能做的更好","可以试试带入法，思维要开阔","计算结果要化简，学习要认真起来"};
//						String[] str1 = {"审题要认真","掌握清楚知识点","学习态度要端正","做题要仔细"};
						String[] str = new String[7];
						
						if(Calligraph.gestures.getY()>1030){
							Log.i("commentPos",""+Calligraph.gestures.getY());
							System.arraycopy(Calligraph.commentString, 0,str , 0, 6);
						//	flag1 = 1;
						}
						else {
								System.arraycopy(Calligraph.commentString, 0,str , 0, 6);
						//		flag1 = 0;
								
							}
						
						   builder.setSingleChoiceItems(  str, 0,  new DialogInterface.OnClickListener() 
						   {
							   public void onClick(DialogInterface dialog, int which) { 
							//	   dialog.dismiss();
								   whichComment=which;
								   if (which ==0){
									 
									 Calligraph.pingyuText.setVisibility(View.VISIBLE);
//									    CountDownTimer timer1 = new CountDownTimer(10000, 1000) {  
//											  
//											 @Override  	       
//											 public void onTick(long millisUntilFinished) {    
//											        }  
//											  
//											        @Override  
//											        public void onFinish() {  
//											        	 Calligraph.pingyuText.setEnabled(true);  
//											        	 Calligraph.pingyuText.setVisibility(View.INVISIBLE);							        	
//											        }  
//											    }.start();  		   
								   }
								   
								   else if(which ==1)  {
									
									   Calligraph.pingyuText1.setVisibility(View.VISIBLE);
								   }
								   
										else 	Toast.makeText(view.getContext(), "此条评语已保存",Toast.LENGTH_SHORT).show();
								   
				//				   Calligraph.pingyuText.setVisibility(View.GONE);
				//				   Calligraph.tounaoText.setVisibility(View.VISIBLE);					   
              }
						   });

						builder.setPositiveButton("提交评价", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								final Elements elementComment =Calligraph.doc.getElementsByTag("comment");
								if(Calligraph.currentItem!=0){
								elementComment.get(Calligraph.currentItem-1).text(Calligraph.commentString[whichComment]);
       
								 	Toast.makeText(view.getContext(), "评价已提交。", Toast.LENGTH_SHORT).show();
								 	arg0.cancel();
								 	}
								else arg0.cancel();
								
								
							}
						});
						builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								Toast.makeText(view.getContext(), "已取消", Toast.LENGTH_SHORT).show();
								
							}
						});
						
						builder.create().show();
							
						}else if("最近批语".equals(focusItem.getName())) {
							Toast.makeText(view.getContext(), "最近评语", Toast.LENGTH_SHORT).show();
							dispearfenye();
						}else if("批语智能匹配".equals(focusItem.getName())) {
							Toast.makeText(view.getContext(), "批语智能匹配", Toast.LENGTH_SHORT).show();
							dispearfenye();
						}
						
						if(focusItem != null)
							focusItem.setTouch(false);//设置未选中
					}
					if(focusItem != null)
						focusItem.setTouch(false);
					Log.e("right", "set touch false");
				}//end if right
				else if(down){
					Log.i("touchdown","action_up down");
					if(focusItem != null){
						focusItem.setTouch(false);//设置未选中
						String focusItemName = focusItem.getName();
						if("一元一次方程".equals(focusItem.getName())){
//							Toast.makeText(Start.context, focusItemName, Toast.LENGTH_SHORT).show();
							dispearfenye();
							AlertDialog.Builder  builder = new AlertDialog.Builder(view.getContext());
							builder.setTitle("一元一次方程");
							String[] str = {"0944号 王洪亮","0945号 陈程","0946号 李亚芳"};
							   builder.setSingleChoiceItems(  str, 0,  new DialogInterface.OnClickListener() 
							   {
								   public void onClick(DialogInterface dialog, int which) { 
								//	   dialog.dismiss();
								   }
							   });

							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
//									Toast.makeText(view.getContext(), "提取试卷中...", Toast.LENGTH_SHORT).show();
									arg0.cancel();
									
									view.DemoChangeBg();
									Calligraph.nameText.setText("学科：数学 章节：第二十一章 一元一次方程 姓名 ：0944号 王洪亮");
									Calligraph.pageNum = 3;
								}
							});
							
							builder.create().show();
							
						} else if("二次函数".equals(focusItem.getName())){
//							Toast.makeText(Start.context, focusItemName, Toast.LENGTH_SHORT).show();
							dispearfenye();
							AlertDialog.Builder  builder = new AlertDialog.Builder(view.getContext());
							builder.setTitle("二次函数");
							String[] str = {"0944号 王洪亮","0945号 陈程","0946号 李亚芳"};
							   builder.setSingleChoiceItems(  str, 0,  new DialogInterface.OnClickListener() 
							   {
								   public void onClick(DialogInterface dialog, int which) { 
								//	   dialog.dismiss();
								   }
							   });

							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									Toast.makeText(view.getContext(), "提取试卷中...", Toast.LENGTH_SHORT).show();
									arg0.cancel();	
								}
							});
							
							builder.create().show();
							
						} else if("旋转".equals(focusItem.getName())){
//							Toast.makeText(Start.context, focusItemName, Toast.LENGTH_SHORT).show();
							dispearfenye();
							
							AlertDialog.Builder  builder = new AlertDialog.Builder(view.getContext());
							builder.setTitle("旋转");
							String[] str = {"0944号 王洪亮","0945号 陈程","0946号 李亚芳"};
							   builder.setSingleChoiceItems(  str, 0,  new DialogInterface.OnClickListener() 
							   {
								   public void onClick(DialogInterface dialog, int which) { 
								//	   dialog.dismiss();
									   
								   }
							   });

							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									Toast.makeText(view.getContext(), "提取试卷中...", Toast.LENGTH_SHORT).show();
									arg0.cancel();	
								}
							});
							
							builder.create().show();
						}
						focusItem = null;
					}
				if(focusItem != null)
					focusItem.setTouch(false);
				Log.e("right", "set touch false");
				}
			}else{
				return doTouchEvent(event);
			}
		}
		//action_up(改完)*******************************************************
		return doTouchEvent(event);
	}

	//doTouchEvent 改完
	public boolean doTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		//Action_move 在左和下里面没有加入动画
		//action_move, 左和下不加入动画，不需改*********************************
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			Log.i("touchdown","doTouchEvent right left down over dispear fenye splitFlag : " + right + " " + left + " " + down + " "+ over + " "+ dispear + " "+ fenye + " "+ splitFlag);
			if(over && !right){
				
			}else{
				mTouch.x = event.getX();
				mTouch.y = event.getY();
				
//				float x = width_test - event.getX();
				float x = mWidth - event.getX();
				float y = event.getY();
				

				//ly
				//if(y >width_test){
				if(y> 900){
					return true;	
				}
				Log.v("bug", "x:"+mTouch.x+" y:"+mTouch.y);
				if(right){
					if(x > y) {
//						mTouch.x = width_test -x;
						mTouch.x = mWidth - 0.001f -x;
						mTouch.y = x;
					}
					else{
//						mTouch.x = width_test-y;
						mTouch.x = mWidth - 0.001f -y;
						mTouch.y = y;
					}
				}else{
					if(mTouch.x > mTouch.y) {
						
						mTouch.x = mTouch.y-0.01f;
					}
					else{
						mTouch.y = mTouch.x-0.01f;
					}
				}
				view.invalidate();
			}
		}
		//action_move, 左和下不加入动画，不需改*********************************
		
		//action_down, 不需要改*********************************
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Log.e("bug", "doTouchEvent ACTION_DOWN calcXY postInvalidate");
			Log.e("down", "收回 dotouchevent down");
			Log.i("touchdown", "in doTouchEvent in action_down");
			
			time = 5;
			mTouch.x = event.getX();
			mTouch.y = event.getY();
			calcCornerXY(mTouch.x, mTouch.y);
			view.invalidate();
		}
		//action_down, 不需要改*********************************
		
		//action_up,将up点设置为2200+1*********************************
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if(fenye){
				Log.i("touchdown", "action_up y = " +mTouch.y);
				Log.i("touchdown","action_up right left down over dispear fenye splitFlag : " + right + " " + left + " " + down + " "+ over + " "+ dispear + " "+ fenye + " "+ splitFlag);
				Log.e("touch", "ACTION_UP x:"+mTouch.x+" y:"+mTouch.y);
				
//			cannot backup	if(mTouch.y< 300 && focusItem != null && focusItem.getType() == 0){
				if(mTouch.y< 300 && backup){
					//没有下级目录，即点开始回收，使消失
					Log.i("touchdown", "!!!!!!!!!!!!dispear");
					Log.e("touch", "!!!!!!!!!!!!dispear ");
					dispearfenye();
				}else if(mTouch.y< 300 && focusItem != null){
					//有下级目录，回收，返回上层目录
					if(focusItem.getType() != 0)
						initFirstRectList();
					if(right){
						mTouch.x = 10;
						mTouch.y = width_test-10;
						}else{
							mTouch.x = 590;
							mTouch.y = width_test-10;
						}
						over = true;
				}else{
					if(right){
						float dst = mWidth - width_test;
						mTouch.x = 10 + dst - 0.001f;
						mTouch.y = width_test-10;
						Log.i("touchdown", "!!!!!change touch y = " + mTouch.y);
					}else if(left){
						//ly
						mTouch.x = 875 ;
//						mTouch.y = width_test-10;
						mTouch.y = 875-1;
					}else if(down) {
						float dst = mWidth - width_test;
						mTouch.x = 10 + dst - 0.001f;
						mTouch.y = 2201;
					}
					over = true;
				}
			} 
//			this.postInvalidate();
			view.invalidate();
		}
		//action_up（改完）*********************************
		
		if(fenye){
			return true;
		}else{
//			return super.onTouchEvent(event);
			return false;
		}
	}
/*
//	public boolean checkRight(float x, float y){
//		if (x>500 && y<200 || x<100 && y<200) 
//			splitFlag = true;
//		else 
//			splitFlag = false;
//		
//		
//		if(over){
//			if((x < 100 && y > 533 && y < 600)){
//				splitFlag = true;
//				dispear = false;
//			}
//			if((x > 500 && y > 533 && y < 600)){
//				splitFlag = true;
//				dispear = false;
//			}
//		}
//		return splitFlag;
//	}
 */
	

	float tempx;
	float tempy;
	//改完
	public void doDraw(Canvas canvas){
		 tempx = mTouch.x;
		 tempy = mTouch.y;
//		 Log.i("touchdown", "doDraw x = " + tempx + " y = " + tempy);
		 Log.i("flipper", "flipper"+flipper);
		if(!flipper){
			//不是二次展开
			for (int i = 0; i<10; i++){
	//			Log.e("for", "for:"+i);
				calcPoints();
				if( i != 9)
					drawCurrentPageArea(canvas, mPath0,i);
				else
					drawCurrentPageArea(canvas, mPath0,9);
				
				if(right){
					//ly
					mTouch.x += (875.0/10);
					mTouch.y -= (875.0/10);
				}else if(left){
					//mTouch.x -= (575.0/10);
					//mTouch.y -= (575.0/10);	
					//ly
					mTouch.x -= (875.0/10);
					mTouch.y -= (875.0/10);
				} else if(down) {
					mTouch.x += (875.0/10);
					mTouch.y += (875.0/10);
				}
				
				//if (mTouch.x>=600 && mTouch.x<0)
				if(mTouch.x >=900 && mTouch.x<0)
					break;
				
				//if (mTouch.y<0 && mTouch.y>=600)
//				if (mTouch.y<0 && mTouch.y>=900)
//					break;
			}
		}else{
			//左和下没有flipper
			Log.e("flipper", "flipper"+flipper);
			float xx = mTouch.x;
			float yy = mTouch.y;
			
			mTouch.x = 10;
			mTouch.y = width_test-10;
			calcPoints();
			drawCurrentPageArea(canvas, mPath0,0);
			
			mTouch.x = xx;
			mTouch.y = yy;
			if(right){
				//展开二级目录
				//根据触摸点，计算所要画的10个点
				for(int i=0;i<10;i++){
					tempxxxx[i] = xx;
					xx += (575.0/10);
					if(xx > 600)
						xx -= 630;
				}
				//从小到大排序，否则绘制时会覆盖
				Arrays.sort(tempxxxx);
				
				drawText = "";
			}
			for (int i = 0; i<10; i++){
							
				//			Log.e("for", "for:"+i);
							mTouch.x = tempxxxx[i];
							mTouch.y = width_test - tempxxxx[i];
							calcPoints();
							if( i != 9)
								drawCurrentPageArea(canvas, mPath0,i);
							else
								drawCurrentPageArea(canvas, mPath0,9);
			}
			
		}
		mTouch.x = tempx;
		mTouch.y = tempy;
		
	}
	
	//doTouchEvent中直接使用calcCornerXY(mTouch.x, mTouch.y);
    //然而其他地方没有用到mIsRTandLB，不需要改,但是用到了mCornerX，和CornerY
	//左：mCornerX，和CornerY为0 0；
	//右：mCornerX，和CornerY为1600 0；
	//下：mCornerX，和CornerY为1600 2560；
	public void calcCornerXY(float x, float y) {
		if (x <= mWidth / 2)
			mCornerX = 0;
		else
			mCornerX = mWidth;
		if (y <= mHeight / 2)
			mCornerY = 0;
		else
			mCornerY = mHeight;
		
		if ((mCornerX == 0 && mCornerY == mHeight)
				|| (mCornerX == mWidth && mCornerY == 0))
			mIsRTandLB = true;
		else
			mIsRTandLB = false;
		
		Log.e("touch", "mIsRTandLB"+mIsRTandLB);
		Log.i("touchdown", "mIsRTandLB" + mIsRTandLB);   //左false，右true
	}
	
	
	//无需改
	private void dispearfenye(){
		fenye = false;
		over = false;
		ondrawflag = false;
		dispear = true;
		backup = false;
		Log.v("touch", "dispear!!!!!!!!!!!!!!!!!!!!!!!set fenye ="+fenye);
		view.setTouchMode(view.getHandWriteMode());
		
		//ly
		//涂鸦态换笔之后要刷屏
		if(view.drawStatus == MyView.STATUS_DRAW_FREE)
		{
			view.freeBitmap.drawFreeBitmapSync();
		}
		//end
		
		view.invalidate();
		focusItem = null;
	}
	
	//无需改
	/**
	 * Author : hmg25 Version: 1.0 Description : ���ֱ��P1P2��ֱ��P3P4�Ľ������
	 */
	public PointF getCross(PointF P1, PointF P2, PointF P3, PointF P4) {
		PointF CrossP = new PointF();
		// ��Ԫ����ͨʽ�� y=ax+b
		float a1 = (P2.y - P1.y) / (P2.x - P1.x);
		float b1 = ((P1.x * P2.y) - (P2.x * P1.y)) / (P1.x - P2.x);
	
		float a2 = (P4.y - P3.y) / (P4.x - P3.x);
		float b2 = ((P3.x * P4.y) - (P4.x * P3.y)) / (P3.x - P4.x);
		CrossP.x = (b2 - b1) / (a1 - a2);
		CrossP.y = a1 * CrossP.x + b1;
		return CrossP;
	}
	
	//不需要修改
	private void calcPoints() {
//		Log.i("touchdown", "!!!!!!!!calcPoints mCornerX:"+mCornerX+" mCornerY:"+mCornerY);
		mMiddleX = (mTouch.x + mCornerX) / 2;
		mMiddleY = (mTouch.y + mCornerY) / 2;
		 if(mCornerX - mMiddleX==0)
	        {
	                return;
	        }
		 
		 mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
			* (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
		mBezierControl1.y = mCornerY;
		mBezierControl2.x = mCornerX;
	
	
	    if(mCornerY - mMiddleY==0)
	    	return;
	
		mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
				* (mCornerX - mMiddleX) / (mCornerY - mMiddleY);
	
		mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x)
				/ 2;
		mBezierStart1.y = mCornerY;
	
		if (mTouch.x > 0 && mTouch.x < mWidth) {
			if (mBezierStart1.x < 0 || mBezierStart1.x > mWidth) {
			}
		}
		mBezierStart2.x = mCornerX;
		mBezierStart2.y = mBezierControl2.y - (mCornerY - mBezierControl2.y)
				/ 2;
	
		mTouchToCornerDis = (float) Math.hypot((mTouch.x - mCornerX),
				(mTouch.y - mCornerY));
	
		mBezierEnd1 = getCross(mTouch, mBezierControl1, mBezierStart1,
				mBezierStart2);
		mBezierEnd2 = getCross(mTouch, mBezierControl2, mBezierStart1,
				mBezierStart2);
	
		mBeziervertex1.x = (mBezierStart1.x + 2 * mBezierControl1.x + mBezierEnd1.x) / 4;
		mBeziervertex1.y = (2 * mBezierControl1.y + mBezierStart1.y + mBezierEnd1.y) / 4;
		mBeziervertex2.x = (mBezierStart2.x + 2 * mBezierControl2.x + mBezierEnd2.x) / 4;
		mBeziervertex2.y = (2 * mBezierControl2.y + mBezierStart2.y + mBezierEnd2.y) / 4;
	}
	
	//改完
	private void drawCurrentPageArea(Canvas canvas, Path path,int time) {
		
		mPath0.reset();
		mPath0.moveTo(mBezierStart1.x, mBezierStart1.y);
		mPath0.quadTo(mBezierControl1.x, mBezierControl1.y, mBezierEnd1.x,
				mBezierEnd1.y);
		mPath0.lineTo(mTouch.x, mTouch.y);
		mPath0.lineTo(mBezierEnd2.x, mBezierEnd2.y);
		mPath0.quadTo(mBezierControl2.x, mBezierControl2.y, mBezierStart2.x,
				mBezierStart2.y);
		mPath0.lineTo(mCornerX, mCornerY);
		mPath0.close();
		
		//实心
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.WHITE);
		
		canvas.drawPath(mPath0, mPaint);
		
//		mPaint.setColor(Color.RED);
		mPaint.setStrokeWidth(3);
		
		
		
		if(right){
			//右侧
			mPaint.setColor(Color.BLUE);
			if(flipper &&fliping){
				//正在滑动，不显示字
				drawText = "";
				if(focusItem != null)
					canvas.drawText(focusItem.getName(),tempx+10,tempy-10,mPaint);
//				drawText = focusItem.getName()+" "+ focusItem.getType()+"time:"+time;
			}else{
				if(right_First_ItemList != null){
					drawText = right_First_ItemList.get(time).getName();
//					Log.e("right", "size"+right_First_ItemList.get(time).getTextSize());
//					Log.e("right", "touch"+right_First_ItemList.get(time).isTouch());
					mPaint.setTextSize(right_First_ItemList.get(time).getTextSize());
				}else{
					drawText = "not at all";
				}
			}
			
			//ly
			//测试点击区域用
//			canvas.drawRect(right_First_ItemList.get(time).getTextRect(), mPaint);
			
			canvas.drawText(drawText,mTouch.x+10,mTouch.y-10,mPaint);
//			Log.e("small", "null:"+(right_First_ItemList.get(time).getSmallBitmap() == null));
			if(right_First_ItemList.get(time).getSmallBitmap() != null)
			canvas.drawBitmap(right_First_ItemList.get(time).getSmallBitmap(),
					new Rect(10, 10, 80,80 ),
					new Rect((int)mTouch.x-56, (int)mTouch.y-66, (int)mTouch.x, (int)mTouch.y),
					mPaint);
			

			
		} else if(left){//左侧
			mPaint.setColor(Color.BLACK);
			drawText = left_First_ItemList.get(time).getName();
//			if("硬笔".equals(drawText) && MyView.penStatus == MyView.STATUS_PEN_HARD){
//				left_First_ItemList.get(time).setTouch(true);
//			}else if("毛笔".equals(drawText) && MyView.penStatus == MyView.STATUS_PEN_CALLI){
//				left_First_ItemList.get(time).setTouch(true);
//			}else{
//				left_First_ItemList.get(time).setTouch(false);
//			}
			if("开始".equals(drawText) && MyView.penStatus == MyView.STATUS_PEN_HARD){
				left_First_ItemList.get(time).setTouch(false);
			}else if("".equals(drawText) && MyView.penStatus == MyView.STATUS_PEN_HARD){
				left_First_ItemList.get(time).setTouch(false);
			}else{
				left_First_ItemList.get(time).setTouch(false);
			}
			mPaint.setTextSize(left_First_ItemList.get(time).getTextSize());

			
			canvas.drawText(drawText,mTouch.x-100,mTouch.y-10,mPaint);
			
			//ly
			//测试点击区域用
//			canvas.drawRect(left_First_ItemList.get(time).getTextRect(), mPaint);
			
//			if("粗毛笔".equals(drawText)){
			if(time == 8){
				mPaint.setColor(Color.BLACK);
				canvas.drawCircle(mTouch.x-117, mTouch.y-15, 15, mPaint);
				
			}
//			if("细毛笔".equals(drawText)){
			if(time == 7){
				mPaint.setColor(Color.BLACK);
				canvas.drawCircle(mTouch.x-117, mTouch.y-15, 10, mPaint);
			}
//			if("细毛笔".equals(drawText)){
			if(time == 6){
				mPaint.setColor(Color.RED);
				canvas.drawCircle(mTouch.x-117, mTouch.y-15, 10, mPaint);
			}
//			if("粗硬笔".equals(drawText)){
			if(time == 5){
				mPaint.setColor(Color.BLACK);
				canvas.drawCircle(mTouch.x-117, mTouch.y-15, 15, mPaint);
			}
//			if("细硬笔".equals(drawText)){
			if(time == 4){
				mPaint.setColor(Color.BLACK);
				canvas.drawCircle(mTouch.x-117, mTouch.y-15, 10, mPaint);
			}
//			if("细硬笔".equals(drawText)){
			if(time == 3){
				mPaint.setColor(Color.RED);
				canvas.drawCircle(mTouch.x-117, mTouch.y-15, 10, mPaint);
			}
//			Log.e("drawText", "x:"+ (mTouch.x-100) + " y:"+(mTouch.y-10));
		}else if(down) {
			Log.i("draw","down draw");
			//下侧
			mPaint.setColor(Color.BLUE);
			Log.i("draw", "flipper fliping" + flipper + " " + fliping);
			if(flipper &&fliping){
				//正在滑动，不显示字
				drawText = "";
				if(focusItem != null)
					canvas.drawText(focusItem.getName(),tempx+10,tempy-10,mPaint);
//				drawText = focusItem.getName()+" "+ focusItem.getType()+"time:"+time;
			}else{
				if(down_First_ItemList != null){
					drawText = down_First_ItemList.get(time).getName();
					Log.i("draw", "drawText = " + drawText);
//					Log.e("right", "size"+right_First_ItemList.get(time).getTextSize());
//					Log.e("right", "touch"+right_First_ItemList.get(time).isTouch());
					mPaint.setTextSize(down_First_ItemList.get(time).getTextSize());
				}else{
					drawText = "not at all";
				}
			}
			
			//ly
			//测试点击区域用
//			canvas.drawRect(down_First_ItemList.get(time).getTextRect(), mPaint);
			
			canvas.drawText(drawText,mTouch.x+10,mTouch.y-10,mPaint);
//			Log.e("small", "null:"+(right_First_ItemList.get(time).getSmallBitmap() == null));
			if(down_First_ItemList.get(time).getSmallBitmap() != null)
			canvas.drawBitmap(down_First_ItemList.get(time).getSmallBitmap(),
					new Rect(10, 10, 80,80 ),
					new Rect((int)mTouch.x-56, (int)mTouch.y-66, (int)mTouch.x, (int)mTouch.y),
					mPaint);
		}
		
		//空心
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.GRAY);
		mPaint.setStrokeWidth(2);
		canvas.drawPath(mPath0, mPaint);
		
		canvas.save();
//		canvas.clipPath(path, Region.Op.XOR);
//		canvas.restore();
		//android 4.0报错
	}

	//未用，不改
	private void rightTouchUp(float x,float y){
		
		/*
		 * new Rect(left, top, right, bottom)
		 * new Rect(67,475, 200,600);
		 * new Rect(125,418, 225,475);
		 * new Rect(182,360, 282,418);
		 * new Rect(240,303, 340,360);
		 * new Rect(297,245, 397,303);
		 * new Rect(355,188, 455,245);
		 * new Rect(412,130, 512,188);
		 * new Rect(470,73, 570,130);
		 * new Rect(527,0, 600,73);
		String size = "";
		if((x < 300 && y > 533 && y < 600) || (x > 300 && y > 533 && y < 600)){	
		
		}
		else if ( x>67 && x <200 && y<600 && y>475 ){
		
			Toast.makeText(view.getContext(), "左下第2个"+size, Toast.LENGTH_SHORT).show();
		}else if ( x>125 && x <225 && y<475 && y>418 ){
		new Rect(125,418, 225,475);
			Toast.makeText(view.getContext(), "左下第3个"+size, Toast.LENGTH_SHORT).show();
			
//			切换涂鸦态代码
//			Toast.makeText(view.getContext(), "涂鸦", Toast.LENGTH_SHORT).show();
//			//changed by mouse @11-15
//			if(MyView.drawStatus == MyView.STATUS_DRAW_CURSOR)
//				view.changeStateAndSync(MyView.STATUS_DRAW_FREE);
//			dispearfenye();
			
		}else if ( x>182 && x <282 && y<418 && y>360 ){
		new Rect(182,360, 282,418);
			Toast.makeText(view.getContext(), "左下第4个-个性字库"+size, Toast.LENGTH_SHORT).show();
		}else if ( x>240 && x <340 && y<360 && y>303 ){
		new Rect(240,303, 340,360);
			Toast.makeText(view.getContext(), "左下第5个-云国画"+size, Toast.LENGTH_SHORT).show();
			
			Intent mIntent = new Intent();
			mIntent.setClass(view.getContext(), PicActivity.class);
			view.getContext().startActivity(mIntent);
			
//			切换光标态代码
//			Toast.makeText(view.getContext(), "光标", Toast.LENGTH_SHORT).show();
//			//changed by mouse @11-15
//			if(MyView.drawStatus == MyView.STATUS_DRAW_FREE)
//				view.changeStateAndSync(MyView.STATUS_DRAW_CURSOR);
//			dispearfenye();
			
		}else if ( x>297 && x <397 && y<303 && y>245 ){
		new Rect(297,245, 397,303);
			Toast.makeText(view.getContext(), "左下第6个-云书法"+size, Toast.LENGTH_SHORT).show();
		}else if ( x>355 && x <455 && y<245 && y>188 ){
		new Rect(355,188, 455,245);
			Toast.makeText(view.getContext(), "左下第7个-云文档模板"+size, Toast.LENGTH_SHORT).show();
			
			//更换模板代码
			if(flag == 0)
				flag = 1;
			else 
				flag = 0;
			view.changeBackground(flag);
			Toast.makeText(view.getContext(), "35分钟", Toast.LENGTH_SHORT).show();
			dispearfenye();
			
		}else if ( x>412 && x <512 && y<188 && y>130 ){
		new Rect(412,130, 512,188);
			Toast.makeText(view.getContext(), "左下第8个-保存"+size, Toast.LENGTH_SHORT).show();
			//保存代码
			view.save();
			view.saveDatebase();
			dispearfenye();
			
			
		} else if ( x>470 && x <570 && y<130 && y>73 ) {
		new Rect(470,73, 570,130);
			Toast.makeText(view.getContext(), "左下第9个-分享"+size, Toast.LENGTH_SHORT).show();
			
//			UploadToServer u = new UploadToServer();
//			try {
//				u.uploadByHttpURLConnection(CDBPersistent.bitmapDecode(BitmapFactory.decodeResource(view.getResources(), R.drawable.icon)));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		     
			view.share();
            dispearfenye();

		} else if ( x>527 && x <600 && y<73 && y>0 ) {
		new Rect(527,0, 600,73);
			
			Toast.makeText(view.getContext(), "右上角第一个开始分页", Toast.LENGTH_SHORT).show();
		}
		*/
		for(int i=0;i<right_First_ItemList.size();i++){
			if(right_First_ItemList.get(i).getTextRect().contains((int)x, (int)y)){
				Toast.makeText(view.getContext(), right_First_ItemList.get(i).getName(), Toast.LENGTH_SHORT).show();
				right_First_ItemList.get(i).setTouch(false);
			}
			
		}
		
	}
	
	//未用，不改
	private void leftTouchUp(float x,float y){
		
		if ( x>145 && x <245 && y<244 && y>194 ) {
			Toast.makeText(view.getContext(), "毛笔", Toast.LENGTH_SHORT).show();
			//毛笔
			view.changePenState(MyView.STATUS_PEN_CALLI);
			dispearfenye();
		}else if ( x>200 && x <300 && y<360 && y>300 ){
			Toast.makeText(view.getContext(), "颜色", Toast.LENGTH_SHORT).show();
			//颜色
			new ColorPickerDialog(view.getContext(), view, view.baseImpl.bPaint.getColor()).show();
			dispearfenye();
		}else if ( x>375 && x <475 && y<475 && y>418 ){
			Toast.makeText(view.getContext(), "硬笔", Toast.LENGTH_SHORT).show();
			//硬笔
			view.changePenState(MyView.STATUS_PEN_HARD);
			dispearfenye();
		}
		
		
		
		
	}
	
	//判断点击位置（改完）
	public boolean checkRight(float x, float y){
		float dst = mWidth - width_test;
		Log.i("touchdown", "checkRight x = "+x+ " y= " + y);
		Log.i("touchdown", "in checkRight right over dispear fenye splitFlag : " + right + " "+ over + " "+ dispear + " "+ fenye + " "+ splitFlag);
		if (x>500 && y<100 || x<100+dst && y<100 || x>500 && y>2400) 
			splitFlag = true;
		else 
			splitFlag = false;
		//拉出菜单之后，再在菜单外区域点击，显示书页。
		if(over) {
			Log.i("touchdown", "checkRight over");
			if((x < 100+dst && y > 533 && y < 600)){
				splitFlag = true;
				dispear = false;
			}
			if((x > 500 && y > 533 && y < 600)){
				splitFlag = true;
				dispear = false;
			}
			if((x >500 && y < 533 && y > 0)) {
				splitFlag = true;
				dispear = false;
			}
		}
		return splitFlag;
		
	}

	//null
	@Override
	public void printInfo() {
		// TODO Auto-generated method stub
		Log.i("touchmode", "This is " + TAG);
		
	}
	//null
	@Override
	public Matrix getMatrix() {
		// TODO Auto-generated method stub
		return null;
	}
	//null
	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}
}
