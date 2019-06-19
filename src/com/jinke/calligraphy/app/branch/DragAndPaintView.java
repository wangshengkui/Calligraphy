package com.jinke.calligraphy.app.branch;

import java.io.File;
import java.util.ArrayList;

import javax.security.auth.PrivateCredentialPermission;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class DragAndPaintView extends ImageView {
	private PointF startPoint = new PointF();
	public Matrix matrix = new Matrix();
	public Matrix currentMaritx = new Matrix();
	public static Bitmap bgBitmap;

	private int mode = 0;// 用于标记模式
	private static final int DRAG = 1;// 拖动
	private static final int ZOOM = 2;// 放大
	private float startDis = 0;
	private float endDis = 0;
	private PointF midPoint;// 中心点

	private Paint mPaint;
	public static Path mPath;
	public static Bitmap mBitmap;
	public static Canvas mCanvas;
	private float currentX, currentY;
	public Canvas drawCanvas;

	private int count = 1;
	private File pigaihuantituwenjianjia;
	
	
//	private int  piGaiHuanPiGaiTiMu=0;
//	int  indexl=0;
//	
//	
//	void dragAndpaintView(int piGaiHuanPiGaiTiMu){
//		
//		this.piGaiHuanPiGaiTiMu=piGaiHuanPiGaiTiMu;
//		
//		}
//		
//	
	
	// 传入一个文件名获取他的图片，返回的是图片路径数组

	private String[] getFile(File files) {
		Log.v("zgm", "这是测试");
		Log.v("zgm", ""+files);
		String[] returnStrings = null;
		ArrayList<String> dirAllStrArr = new ArrayList<String>();
		try {
			// 如果手机插入了SD卡，而且应用程序具有访问SD卡的权限
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 获取SD卡的目录
				File sdCardDir = Environment.getExternalStorageDirectory();
				File newFile = new File(sdCardDir + File.separator + "mynewimg"
						+ File.separator +files);
				Log.v("zgm", "这是测试2" + newFile);
				File filesArry[] = newFile.listFiles();

				Log.v("zgm", "这是测试" + filesArry.length);
				Log.v("zgm", "布尔值：" + filesArry[0].isDirectory());
				for (File file:filesArry) {
					if (file.isDirectory()) {

						// String fileNameString=file.getName();
						getFile(file);// 递归调用可能有问题
					} else {
						Log.v("zgm", "我执行了不是路径的情况");
						String fileName = file.getName();
						Log.v("zgm", "路径中文件的名字:" + fileName);
						if (fileName.endsWith(".png")
								|| fileName.endsWith(".jpeg")
								|| fileName.endsWith(".jpg")
								|| fileName.endsWith(".bmp")
								|| fileName.endsWith(".gif")) {

							// ArrayList<String> dirAllStrArr = new
							// ArrayList<String>();

							dirAllStrArr.add(newFile.getPath() + File.separator
									+ file.getName());
							Log.v("zgm", "最终的路径:" + dirAllStrArr.get(0));

							// returnStrings = (String[])
							// dirAllStrArr.toArray();

						}
					}
				}
				Log.v("zgm", "dirAllStrArr:" + dirAllStrArr.size());
				returnStrings = (String[]) dirAllStrArr
						.toArray(new String[dirAllStrArr.size()]);
				Log.v("zgm", "最终返回的数组长度:" + returnStrings.length);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return returnStrings;

	}
	
//	private String[] piGaiHuanPiGaiTiMu=getFile((File) pigaihuantituwenjanjia);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//public  void setBackGroundImage(int piGaiHuanPiGaiTiMu){
		public  void setBackGroundImage(String bgName){
		
		String  tiTuName = bgName.substring(0,bgName.length())+"_9.jpg";//作业第9题题目

		
		try {
			// 如果手机插入了SD卡，而且应用程序具有访问SD卡的权限
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 获取SD卡的目录
				File sdCardDir = Environment.getExternalStorageDirectory();
//				File newFile = new File(sdCardDir + File.separator + "mynewimg"
//						+ File.separator + tiTuName);
				bgBitmap = BitmapFactory.decodeFile(sdCardDir + File.separator + "mynewimg"
						+ File.separator + "slice"+File.separator+tiTuName);
				Log.i("pbgimagetest",sdCardDir + File.separator + "mynewimg"
						+ File.separator + "slice"+File.separator+tiTuName);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		
	}
		
		//用点阵笔设置批改环的背景图
		public void setPigaihuanBgImage(String fileName) {
		try {
			// 如果手机插入了SD卡，而且应用程序具有访问SD卡的权限
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 获取SD卡的目录
				File sdCardDir = Environment.getExternalStorageDirectory();
//				File newFile = new File(sdCardDir + File.separator + "mynewimg"
//						+ File.separator + tiTuName);
				bgBitmap=null;
				System.gc();
				bgBitmap = BitmapFactory.decodeFile(sdCardDir + File.separator + "mynewimg"
						+ File.separator + "slice2"+File.separator+fileName+".jpg");

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		
		
		
		} 
		
		
		
		
		
		
		
		
		
	

	/**
	 * 默认构造函数
	 * 
	 * @param context
	 */
//	public DragAndPaintView(Context context) {
//		super(context);
	public DragAndPaintView(Context context) {
		super(context);
//		this.piGaiHuanPiGaiTiMu=piGaiHuanPiGaiTiMu;
		Resources res = getResources();
//		bgBitmap = BitmapFactory.decodeResource(res, R.drawable.ppp);
//		setBackGroundImage();
		invalidate();
		init();
	}

	/**
	 * 该构造方法在静态引入XML文件中是必须的
	 * 
	 * @param context
	 * @param paramAttributeSet
	 */
	public DragAndPaintView(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		canvas.drawBitmap(bgBitmap, matrix, null);
		canvas.drawPath(mPath, mPaint);
		// this.drawCanvas=canvas;
		Calligraph.pigaihuanSaveCanvas.drawPath(mPath, mPaint);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		count = event.getPointerCount();
		if (count == 2) {
			// Log.i("touchTest" ," drag ");
			dragImage(event);
		} else {
			// Log.i("touchTest", "draw");
			drawImage(event);
		}
		return true;
	}

	public boolean dragImage(MotionEvent event) {
		Log.i("touchTest", "in drag");
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			Log.i("dragImageView", "down");
			// mode = DRAG;
			currentMaritx.set(this.getImageMatrix());// 记录ImageView当期的移动位置
			startPoint.set(event.getX(), event.getY());// 开始点
			break;

		case MotionEvent.ACTION_MOVE:// 移动事件
			if (event.getPointerCount() == 2) {
				endDis = distance(event);
				if (Math.abs(endDis - startDis) < 15f) {
					mode = DRAG;

				} else
					mode = ZOOM;
			}

			else
				mode = 0;

			Log.i("dragImageView", "move");
			if (mode == DRAG) {// 图片拖动事件
				Log.i("dragImageView", "drag");
				float dx = event.getX(0) - startPoint.x;// x轴移动距离
				float dy = event.getY(0) - startPoint.y;
				matrix.set(currentMaritx);// 在当前的位置基础上移动
				matrix.postTranslate(dx, dy);

			} else if (mode == ZOOM) {// 图片放大事件
				Log.i("dragImageView", "zoom");
				// float endDis = distance(event);//结束距离
				if (endDis > 10f) {
					float scale = endDis / startDis;// 放大倍数
					// Log.v("scale=", String.valueOf(scale));
					matrix.set(currentMaritx);
					matrix.postScale(scale, scale, midPoint.x, midPoint.y);
				}

			}

			break;

		case MotionEvent.ACTION_UP:
			Log.i("dragImageView", "up");
			mode = 0;
			break;
		// 有手指离开屏幕，但屏幕还有触点(手指)
		case MotionEvent.ACTION_POINTER_UP:
			Log.i("dragImageView", "pointer up");
			mode = 0;
			break;
		// 当屏幕上已经有触点（手指）,再有一个手指压下屏幕
		case MotionEvent.ACTION_POINTER_DOWN:
			Log.i("dragImageView", "pointer down");
			// mode = ZOOM;
			startDis = distance(event);

			if (startDis > 10f) {// 避免手指上有两个茧
				midPoint = mid(event);
				currentMaritx.set(this.getImageMatrix());// 记录当前的缩放倍数
			}
			// starDis和endDis之间的差要小于一定的值设为拖动
			// 不要单指操作

			break;

		}
		// setImageMatrix(matrix);
		invalidate();
		return true;

	}

	public void drawImage(MotionEvent event) {
		Log.i("touchTest", "in draw");
		float x = event.getX();
		float y = event.getY();
		int count = event.getPointerCount();
		Log.i("paintTest", "count " + count);
		// Log.i("paint", "" + event.getPointerCount());
		if (count == 2) {
			// this.setEnabled(false);
			mode = 0;
			Log.i("tag", "1   " + event.getPointerCount());

		} else {
			mode = 1;
			Log.i("paint", "2   " + event.getPointerCount());
			switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				Log.i("tag", "downpg");
				currentX = x;
				currentY = y;
				mPath.moveTo(currentX, currentY);
				break;
			case MotionEvent.ACTION_MOVE:

				Log.i("tag", "movepg");
				currentX = x;
				currentY = y;
				mPath.quadTo(currentX, currentY, x, y); // 画线
				mCanvas.drawPath(mPath, mPaint);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				mode = 0;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				mode = 0;
				break;

			}
			invalidate();
		}
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true); // 去除锯齿
		mPaint.setStrokeWidth(5);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.RED);

		mPath = new Path();

		mBitmap = Bitmap.createBitmap(1200, 1000, Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		// mCanvas.drawColor(Color.WHITE);
		Log.i("slidemode", "   " + MyView.SlideMode);

	}

	/**
	 * 两点之间的距离
	 * 
	 * @param event
	 * @return
	 */
	private static float distance(MotionEvent event) {
		// 两根线的距离
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		return FloatMath.sqrt(dx * dx + dy * dy);
	}

	/**
	 * 计算两点之间中心点的距离
	 * 
	 * @param event
	 * @return
	 */
	private static PointF mid(MotionEvent event) {
		float midx = event.getX(1) + event.getX(0);
		float midy = event.getY(1) - event.getY(0);

		return new PointF(midx / 2, midy / 2);
	}
	// @Override
	// public boolean dispatchTouchEvent(MotionEvent event) {
	// return true;
	// }

}
