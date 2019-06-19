package com.jinke.calligraphy.app.branch;

import android.app.Service;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.os.IBinder;
import android.util.FloatMath;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;

public class pingyuService extends Service{
	//定义浮动窗口布局
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
	WindowManager mWindowManager;
	
	ImageView mFloatView;
//	Button mFloatView;
	
	float startPoint_x;
	float startPoint_y;
	

	private PointF startPoint = new PointF();
	private Matrix matrix = new Matrix();
	private Matrix currentMatrix = new Matrix();
	
	private int mode = 0; //标记模式
	private static final int DRAG = 1; //拖动
	private static final int ZOOM = 2; //放大缩小
	private float startDis = 0;
	private PointF midPoint;
	
	private static final String TAG = "pingyuService";

	
	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "oncreat");
		createFloatView();		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}


	private void createFloatView()
	{
		wmParams = new WindowManager.LayoutParams();
		//获取的是WindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
		Log.i(TAG, "mWindowManager--->" + mWindowManager);
		//设置window type
//		wmParams.type = LayoutParams.TYPE_PHONE;
		wmParams.type = LayoutParams.TYPE_TOAST;
		//设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888; 
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;      
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;       
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据  
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		 /*// 设置悬浮窗口长宽数据
        wmParams.width = 200;
        wmParams.height = 80;*/
   
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        mFloatView = (ImageView)mFloatLayout.findViewById(R.id.imageView1);
//        mFloatView = (Button)mFloatLayout.findViewById(R.id.button1);
        
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
//        Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight()/2);
        //设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				Log.i(TAG, "event" + (event.getAction() & MotionEvent.ACTION_MASK));
				Log.i(TAG, "DOWN" + MotionEvent.ACTION_DOWN + " Move"+MotionEvent.ACTION_MOVE+ " ZOOM " + MotionEvent.ACTION_POINTER_DOWN);
				
				switch(event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					mode = DRAG;
					currentMatrix.set(mFloatView.getImageMatrix());
					startPoint.set(event.getX(), event.getY());
					startPoint_x = event.getX();
					startPoint_y = event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					if(mode == DRAG) {
						Log.i(TAG, "DRAG");
						wmParams.x = (int)(event.getRawX() - mFloatView.getMeasuredWidth()/2);
						wmParams.y = (int)(event.getRawY() - mFloatView.getMeasuredHeight()/2 - 25);
						mWindowManager.updateViewLayout(mFloatLayout, wmParams);
					} else if(mode == ZOOM) {
						Log.i(TAG, "ZOOM");
						float endDis = distance(event);
						if(endDis > 10f) {
							float scale = endDis / startDis;
							matrix.set(currentMatrix);
							matrix.postScale(scale, scale, midPoint.x, midPoint.y);
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					mode = 0;
					break;
				case MotionEvent.ACTION_POINTER_UP:
					mode = 0;
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					mode = ZOOM;
					startDis = distance(event);
					
					if(startDis > 10f) {
						midPoint = mid(event);
						currentMatrix.set(mFloatView.getImageMatrix());
					}
					Log.i(TAG, "change mode to zoom");
					break;
					
	
				}
				mFloatView.setImageMatrix(matrix);
//				// TODO Auto-generated method stub
//				wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth()/2;
////				Log.i(TAG, "RawX" + event.getRawX());
////				Log.i(TAG, "X" + event.getX());
//				//减25为状态栏的高度
//	            wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight()/2 - 25;
////	            Log.i(TAG, "RawY" + event.getRawY());
////	            Log.i(TAG, "Y" + event.getY());
//	             //刷新
//	            mWindowManager.updateViewLayout(mFloatLayout, wmParams);
				return false;
			}
		});
        
        mFloatView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(pingyuService.this, "onClick", Toast.LENGTH_SHORT).show(); 
			}
		});

	}
	
	@Override
	public void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mFloatLayout != null)
		{
			//移除悬浮窗口
			mWindowManager.removeView(mFloatLayout);
		}
	}
	
	private static float distance(MotionEvent event){
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getX(0);
		return FloatMath.sqrt(dx*dx + dy*dy);
	}
	
	private static PointF mid(MotionEvent event) {
		float midx = event.getX(1) - event.getX(0);
		float midy = event.getY(1) - event.getY(0);
		return new PointF(midx/2, midy/2);
	}
	




}
