package com.jinke.calligraphy.app.branch;

//
//import android.content.Context;
//import android.graphics.Matrix;
//import android.graphics.PointF;
//import android.graphics.drawable.Drawable;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.ScaleGestureDetector;
//import android.view.View;
//import android.widget.ImageView;
// 
//public class DragImageView extends ImageView {
// 
//    Matrix matrix;
// 
//    // We can be in one of these 3 states
//    static final int NONE = 0;
//    static final int DRAG = 1;
//    static final int ZOOM = 2;
//    int mode = NONE;
// 
//    // Remember some things for zooming
//    PointF last = new PointF();
//    PointF start = new PointF();
//    float minScale = 0.01f;
//    float maxScale = 10f;
//    float[] m;
// 
// 
//    int viewWidth, viewHeight;
//    static final int CLICK = 3;
//    float saveScale = 1f;
//    protected float origWidth, origHeight;
//    int oldMeasuredWidth, oldMeasuredHeight;
// 
// 
//    ScaleGestureDetector mScaleDetector;
// 
//    Context context;
// 
//    public DragImageView(Context context) {
//        super(context);
//        sharedConstructing(context);
//    }
// 
//    public DragImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        sharedConstructing(context);
//    }
//     
//    private void sharedConstructing(Context context) {
//        super.setClickable(true);
//        this.context = context;
//        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
//        matrix = new Matrix();
//        m = new float[9];
//        setImageMatrix(matrix);
//        setScaleType(ScaleType.MATRIX);
// 
//        setOnTouchListener(new OnTouchListener() {
// 
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                mScaleDetector.onTouchEvent(event);
//                PointF curr = new PointF(event.getX(), event.getY());
// 
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        last.set(curr);
//                        start.set(last);
//                        mode = DRAG;
//                        break;
//                         
//                    case MotionEvent.ACTION_MOVE:
//                        if (mode == DRAG) {
//                            float deltaX = curr.x - last.x;
//                            float deltaY = curr.y - last.y;
//                            float fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale);
//                            float fixTransY = getFixDragTrans(deltaY, viewHeight, origHeight * saveScale);
//                            matrix.postTranslate(deltaX, fixTransY);
//                            fixTrans();
//                            last.set(curr.x, curr.y);
//                        }
//                        break;
// 
//                    case MotionEvent.ACTION_UP:
//                        mode = NONE;
//                        int xDiff = (int) Math.abs(curr.x - start.x);
//                        int yDiff = (int) Math.abs(curr.y - start.y);
//                        if (xDiff < CLICK && yDiff < CLICK)
//                            performClick();
//                        break;
// 
//                    case MotionEvent.ACTION_POINTER_UP:
//                        mode = NONE;
//                        break;
//                }
//                 
//                setImageMatrix(matrix);
//                invalidate();
//                return true; // indicate event was handled
//            }
// 
//        });
//    }
// 
//    public void setMaxZoom(float x) {
//        maxScale = x;
//    }
// 
//    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//        @Override
//        public boolean onScaleBegin(ScaleGestureDetector detector) {
//            mode = ZOOM;
//            return true;
//        }
// 
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//            float mScaleFactor = detector.getScaleFactor();
//            float origScale = saveScale;
//            saveScale *= mScaleFactor;
//            if (saveScale > maxScale) {
//                saveScale = maxScale;
//                mScaleFactor = maxScale / origScale;
//            } else if (saveScale < minScale) {
//                saveScale = minScale;
//                mScaleFactor = minScale / origScale;
//            }
// 
//            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight)
//                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2, viewHeight / 2);
//            else
//                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
// 
//            fixTrans();
//            return true;
//        }
//    }
// 
//    void fixTrans() {
//        matrix.getValues(m);
//        float transX = m[Matrix.MTRANS_X];
//        float transY = m[Matrix.MTRANS_Y];
//         
//        float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
//        float fixTransY = getFixTrans(transY, viewHeight, origHeight * saveScale);
// 
//        if (fixTransX != 0 || fixTransY != 0)
//            matrix.postTranslate(fixTransX, fixTransY);
//    }
// 
//    float getFixTrans(float trans, float viewSize, float contentSize) {
//        float minTrans, maxTrans;
// 
//        if (contentSize <= viewSize) {
//            minTrans = 0;
//            maxTrans = viewSize - contentSize;
//        } else {
//            minTrans = viewSize - contentSize;
//            maxTrans = 0;
//        }
// 
//        if (trans < minTrans)
//            return -trans + minTrans;
//        if (trans > maxTrans)
//            return -trans + maxTrans;
//        return 0;
//    }
//     
//    float getFixDragTrans(float delta, float viewSize, float contentSize) {
//        if (contentSize <= viewSize) {
//            return 0;
//        }
//        return delta;
//    }
// 
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
//        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
//         
//        //
//        // Rescales image on rotation
//        //
//        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight
//                || viewWidth == 0 || viewHeight == 0)
//            return;
//        oldMeasuredHeight = viewHeight;
//        oldMeasuredWidth = viewWidth;
// 
//        if (saveScale == 1) {
//            //Fit to screen.
//            float scale;
// 
//            Drawable drawable = getDrawable();
//            if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)
//                return;
//            int bmWidth = drawable.getIntrinsicWidth();
//            int bmHeight = drawable.getIntrinsicHeight();
//             
//            Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);
// 
//            float scaleX = (float) viewWidth / (float) bmWidth;
//            float scaleY = (float) viewHeight / (float) bmHeight;
//            scale = Math.min(scaleX, scaleY);
//            matrix.setScale(scale, scale);
// 
//            // Center the image
//            float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);
//            float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);
//            redundantYSpace /= (float) 2;
//            redundantXSpace /= (float) 2;
// 
//            matrix.postTranslate(redundantXSpace, redundantYSpace);
// 
//            origWidth = viewWidth - 2 * redundantXSpace;
//            origHeight = viewHeight - 2 * redundantYSpace;
//            setImageMatrix(matrix);
//        }
//        fixTrans();
//    }
//}

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class DragImageView extends ImageView {

	private PointF startPoint = new PointF();
	public Matrix matrix = new Matrix();
	public Matrix currentMaritx = new Matrix();

	private int mode = 0;// 用于标记模式
	private static final int DRAG = 1;// 拖动
	private static final int ZOOM = 2;// 放大
	private float startDis = 0;
	private float endDis = 0;
	private PointF midPoint;// 中心点

	/**
	 * 默认构造函数
	 * 
	 * @param context
	 */
	public DragImageView(Context context) {
		super(context);
	}

	/**
	 * 该构造方法在静态引入XML文件中是必须的
	 * 
	 * @param context
	 * @param paramAttributeSet
	 */
	public DragImageView(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
	}

	public boolean onTouchEvent(MotionEvent event) {
		// Log.i("dragImageView", "touch ImageView" + (event.getAction() &
		// MotionEvent.ACTION_MASK));
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
		setImageMatrix(matrix);
		invalidate();
		return true;
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
//	@Override
//	public boolean  dispatchTouchEvent(MotionEvent event) {
//		return true;
//	}
	
}