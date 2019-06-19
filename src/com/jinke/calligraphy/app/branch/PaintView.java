package com.jinke.calligraphy.app.branch;

import java.security.acl.LastOwnerException;

import com.jinke.calligraphy.activity.MainTab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class PaintView extends View {
	private Paint mPaint;
	private Path mPath;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	public int mode = 0;

	private int screenWidth, screenHeight;
	private float currentX, currentY;

	public PaintView(Context context, int screenWidth, int screenHeight) {
		super(context);
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true); // 去除锯齿
		mPaint.setStrokeWidth(5);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.RED);

		mPath = new Path();

		mBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
				Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		// mCanvas.drawColor(Color.WHITE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, 0, 0, null);
		canvas.drawPath(mPath, mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

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
		Log.i("paint", "mode  " + mode);
		if (mode == 1)
			return true;
		else
			return true;
	}
	

	// @Override
	// public boolean dispatchTouchEvent(MotionEvent event) {
	// return super.onTouchEvent(event);
	// }

	public Bitmap getPaintBitmap() {
		return resizeImage(mBitmap, 320, 480);
	}

	public Path getPath() {
		return mPath;
	}

	// 缩放
	public static Bitmap resizeImage(Bitmap bitmap, int width, int height) {
		int originWidth = bitmap.getWidth();
		int originHeight = bitmap.getHeight();

		float scaleWidth = ((float) width) / originWidth;
		float scaleHeight = ((float) height) / originHeight;

		Matrix matrix = new Matrix();
//		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, originWidth,
				originHeight, matrix, true);
		return resizedBitmap;
	}

	// 清除画板
	public void clear() {
		if (mCanvas != null) {
			mPath.reset();
			mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
			invalidate();
		}
	}
}