package com.jinke.smartpen;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.R;
import android.R.bool;
import android.R.integer;
import android.content.Intent;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GesturePoint;
import android.gesture.GestureStroke;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.readAndSave.SmartPenPage;
import com.example.readAndSave.SmartPenUnitils;
import com.google.common.collect.ArrayListMultimap;
import com.jinke.calligraphy.app.branch.Calligraph;
import com.jinke.calligraphy.app.branch.Start;
import com.tqltech.tqlpencomm.Dot;
import com.tqltech.tqlpencomm.PenCommAgent;
import com.tqltech.tqlpencomm.util.BLEFileUtil;

public class ToolFun {

	private String TAG = "ToolFuns";
	// private boolean bIsReply = false;
	private ImageView gImageView;
	// private RelativeLayout gLayout;
	public DrawView[] bDrawl = new DrawView[2]; // add 2016-06-15 for draw

	private final static boolean isSaveLog = false; // 是否保存绘制数据到日志
	private final static String LOGPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/TQL/"; // 绘制数据保存目录
	private volatile long penUpTime;
	private volatile boolean firstpen=true;
	protected volatile long curTime;
	private volatile boolean firstPenChi=true;
	
	private boolean         ismSmartPenStrokeBufferNeedClear=true;
	private ArrayList<GesturePoint> SmartPenStrokeBuffer = new ArrayList<GesturePoint>();
	private       SmartPenGesture currentSmartPenGesture=null;
	public  GestureLibrary gestureLibrary=GestureLibraries.fromFile("/sdcard/zgmgesture");
	public boolean isDealPenPoint=true;
	public String soundPathString="";
    public  DealSmartPenGesture dealSmartPenGesture=new DealSmartPenGesture();
	// private BluetoothLEService mService = null; //蓝牙服务

	// private static final int REQUEST_SELECT_DEVICE = 1; //蓝牙扫描
	// private static final int REQUEST_ENABLE_BT = 2; //开启蓝牙
	// private static final int REQUEST_LOCATION_CODE = 100; //请求位置权限
	// private static final int GET_FILEPATH_SUCCESS_CODE = 1000;//获取txt文档路径成功

	// private int penType = 1; //笔类型（0：TQL-101 1：TQL-111 2：TQL-112 3: TQL-101A）

	public double XDIST_PERUNIT = Constants.XDIST_PERUNIT; // 码点宽
	public double YDIST_PERUNIT = Constants.YDIST_PERUNIT; // 码点高
	public double A5_WIDTH = Constants.A5_WIDTH; // 本子宽
	public double A5_HEIGHT = Constants.A5_HEIGHT; // 本子高
	public double A4_WIDTH = Constants.A4_WIDTH; // 本子宽
	public double A4_HEIGHT = Constants.A4_HEIGHT; // 本子高

	public int BG_REAL_WIDTH = Constants.BG_REAL_WIDTH; // 资源背景图宽
	public int BG_REAL_HEIGHT = Constants.BG_REAL_HEIGHT; // 资源背景图高

	public int BG_WIDTH; // 显示背景图宽
	public int BG_HEIGHT; // 显示背景图高
	public int A5_X_OFFSET; // 笔迹X轴偏移量
	public int A5_Y_OFFSET; // 笔迹Y轴偏移量
	public int gcontentLeft; // 内容显示区域left坐标
	public int gcontentTop; // 内容显示区域top坐标

	public static float mWidth; // 屏幕宽
	public static float mHeight; // 屏幕高

	private float mov_x; // 声明起点坐标
	private float mov_y; // 声明起点坐标
	 int gCurPageID = -1; // 当前PageID
	int gCurBookID = -1; // 当前BookID
	private float gScale = 1; // 笔迹缩放比例
	private int gColor = 6; // 笔迹颜色
	private int gWidth = 6; // 笔迹粗细
	private int gSpeed = 30; // 笔迹回放速度
	private float gOffsetX = 0; // 笔迹x偏移
	private float gOffsetY = 0; // 笔迹y偏移
	public float screenwidth = 30; // 屏幕宽度，单位像素
	public float screenhight = 30; // 屏幕高度，单位像素
	public ArrayListMultimap<Integer, Dots> dot_number = ArrayListMultimap
			.create(); // Book=100笔迹数据
	public ArrayListMultimap<Integer, Dots> dot_number1 = ArrayListMultimap
			.create(); // Book=0笔迹数据
	public ArrayListMultimap<Integer, Dots> dot_number2 = ArrayListMultimap
			.create(); // Book=1笔迹数据
	public ArrayListMultimap<Integer, Dots> dot_number4 = ArrayListMultimap
			.create(); // 笔迹回放数据
//	public ArrayListMultimap<Integer, Dot> pigaihuanDotsContainer = ArrayListMultimap
//	.create();
	// private Intent serverIntent = null;
	// private Intent LogIntent = null;
	// private PenCommAgent bleManager;
	// / private String penAddress;

	public static float g_x0, g_x1, g_x2, g_x3;
	public static float g_y0, g_y1, g_y2, g_y3;
	public static float g_p0, g_p1, g_p2, g_p3;
	public static float g_vx01, g_vy01, g_n_x0, g_n_y0;
	public static float g_vx21, g_vy21;
	public static float g_norm;
	public static float g_n_x2, g_n_y2;
    public int gPIndex = -1;
    private boolean gbSetNormal = false;
    private boolean gbCover = false;
	private float pointX;
	private float pointY;
	private int pointZ;
	public boolean bIsReply = false;//用于批改环回放笔迹用
	private boolean bIsOfficeLine = false;
	// private RoundProgressBar bar;
	private BluetoothLEService mService = null; // 蓝牙服务

	private RelativeLayout dialog;
	private Button confirmBtn;
	private TextView textView;

	private float gpointX;
	private float gpointY;
	private float x;
	private float y;//用于回放笔迹
	private String gStrHH = "";
	private boolean bLogStart = false;
	public SmartPenPage smartPenPage=null;
	public int mN;

	//wsk 2019.4.14
	private ArrayListMultimap<Integer, points> point_number = ArrayListMultimap.create();
	private long soundTime = System.currentTimeMillis();//wsk 2019.1.26
	public ArrayList<Integer> tihao = new ArrayList<Integer>();//wsk 2019.1.26
    public MediaPlayer mediaPlayer = new MediaPlayer();
    public boolean IsArrangeGomework;
    public ArrayList<Integer> group = new ArrayList<Integer>();
    public int currentGroup;
    
    //wsk 2019.5.30
    public Map<String,ArrayList<SimplePoint>> gesture=new HashMap<String, ArrayList<SimplePoint>>();
	public Map<String,ArrayList<SimplePoint>> firgesture=new HashMap<String, ArrayList<SimplePoint>>();
//	public Map<String,ArrayList<SimplePoint>> allgesture=new HashMap<String, ArrayList<SimplePoint>>();
	int  getureStrokeCout=0;
	int allgetureStrokeCout=0;
	private ArrayList<SimplePoint> points=null;
	public boolean bihua = true;
	/*
	 * 将原始点数据进行处理（主要是坐标变换）并保存
	 */
	/*
	 * public ToolFun(int screenwidth,int screenhight) { // TODO Auto-generated
	 * constructor stub if (screenwidth!=0&&screenhight!=0) { Log.e("ToolFun",
	 * "ToolFun类构造函数参数不能为零，他们是屏幕显示尺寸"); this.screenwidth=screenwidth;
	 * this.screenhight=screenhight; } }
	 */
	public void setScreensize(float mWidth2, float mHeight2) {
		this.screenwidth = mWidth2;
		this.screenhight = mHeight2;
	}

	public void ProcessEachDot(Dot dot ,DrawView mDrawView) {
		
		
//		dot.x-=2210;
//		dot.y-=75082;
/*		float ax = (float) (A5_WIDTH / XDIST_PERUNIT); // A5纸张宽度方向包含多少个编码单元		
		float ay = (float) (A5_HEIGHT / YDIST_PERUNIT);*/
		
//		Log.i(TAG, "111 ProcessEachDot=" + dot.toString());
		
/*        if (dot.BookID!=gCurBookID) {
        	Log.e("zgm", "dot.BookID:"+dot.BookID+" "+"gCurBookID"+gCurBookID);
//        	mDrawView.canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//清除画布
            gCurBookID=dot.BookID;
            
             * 这里应该写换背景或者作业所有者的逻辑，或者直接发送信息给主VIEW
             
        }  */           
 		
/*        if (dot.BookID==100) {
        	ax = (float) (A5_WIDTH / XDIST_PERUNIT);
        	ay = (float) (A5_HEIGHT / YDIST_PERUNIT);
        }		
		
        if (dot.BookID==0) {
            ax = (float) (A4_WIDTH / XDIST_PERUNIT); 
            ay = (float) (A4_HEIGHT / YDIST_PERUNIT);
         }*/
/*        float  ax = (float) (A4_WIDTH / XDIST_PERUNIT); 
        float  ay = (float) (A4_HEIGHT / YDIST_PERUNIT);*/
		int counter = 0;
		pointZ = dot.force;
		counter = dot.Counter;
/*		Log.i("zgm", "BookID:  " + dot.BookID);
		Log.i("zgm", "Counter: " + dot.Counter);
		Log.i("zgm", "Counter: " + dot.force);*/
		if (pointZ < 0) {
			// Log.i(TAG, "Counter=" + counter + ", Pressure=" + pointZ +
			// "  Cut!!!!!");
			return;
		}

		// 将坐标的小数部分和整数部分和成一个实数
		int tmpx = dot.x;
		pointX = dot.fx;
		pointX /= 100.0;
		pointX += tmpx;

//		Log.i("zgm", "dot.x=" + dot.x + " " + "dot.fx=" + dot.fx);

		int tmpy = dot.y;
				
		if(Start.c.pigaihuanLayout.getVisibility()== View.VISIBLE)tmpy-=Start.pghCenterYOffset-15;
		
		
		pointY = dot.fy;
		pointY /= 100.0;
		pointY += tmpy;
//		Log.i("zgm", "dot.y=" + dot.y + " " + "dot.fy=" + dot.fy);

		gpointX = pointX;
		gpointY = pointY;
		pointX*=11.67;
		pointY*=11.0;//0426
/*		Log.e("zgm", " BG_WIDTH=" + BG_WIDTH);
		Log.e("zgm", " BG_HEIGHT=" + BG_HEIGHT);*/

/*		pointX *= (BG_WIDTH);

//		Log.e("zgm", "ax=" + ax);
		pointX /= ax;

		pointY *= (BG_HEIGHT);

//		Log.e("zgm", "ay=" + ay);
		// ay = (float) 168.0;
		pointY /= ay;*/
/*		Log.e("zgm", "A5_X_OFFSET=" + A5_X_OFFSET);
		Log.e("zgm", "A5_Y_OFFSET=" + A5_Y_OFFSET);*/

/*		pointX += A5_X_OFFSET;
		pointY += A5_Y_OFFSET;*/

/*		Log.e("zgm", "A5_X_OFFSET=" + A5_X_OFFSET);
		Log.e("zgm", "A5_Y_OFFSET=" + A5_Y_OFFSET);*/
		// pointX /= 100;
		// pointY /= 1000;
		/*
		 * Log.i("zgm", "A5_X_OFFSET=" + A5_X_OFFSET + "tempx=" + tmpx + " " +
		 * "tempy=" + tmpy + " " + "pointX=" + pointX + " " + "pointY=" + pointY
		 * + " ");
		 */

/*		if (isSaveLog) {
			saveOutDotLog(dot.BookID, dot.PageID, pointX, pointY, dot.force, 1,
					gWidth, gColor, dot.Counter, dot.angle);
		}*/

		if (pointZ > 0) {
			if (dot.type == Dot.DotType.PEN_DOWN) {
				{//代码块处理多笔情况
				penUpTime=System.currentTimeMillis();
				
				//wsk 2019.5.30
				bufferInit();
				points.add(new SimplePoint((float)(dot.x+dot.fx/100.0), (float)(dot.y+dot.fy/100.0),System.currentTimeMillis(),
						dot.force,dot.OwnerID,dot.PageID,dot.SectionID,dot.BookID,
						dot.color,dot.angle,dot.Counter));
				
//				将点坐标放进gestureBuffer中
//				showSound(R.raw.cricket);
				if (ismSmartPenStrokeBufferNeedClear) {
					SmartPenStrokeBuffer=new ArrayList<GesturePoint>();
//					Log.e("zgm","1223:mSmartPenStrokeBufferisempty="+SmartPenStrokeBuffer.isEmpty());
					
/*			    	 if (currentSmartPenGesture!=null) {
		    		 currentSmartPenGesture.SmartPenGestureClearAllStroke();
		    		 currentSmartPenGesture.SmartPenGestureClearmBoundingBox();
					} */ 
//					Log.e("zgm","1223:mCurrentGesture.SmartPenGestureClearmBoundingBox()="+mCurrentGesture.getBoundingBox().height());
//					Log.e("zgm", "1210:mCurrentGesture.getStrokesCount()::"+mCurrentGesture.getStrokesCount());
				}
				SmartPenStrokeBuffer.add(new GesturePoint((float)(dot.x+dot.fx/100.0),(float)(dot.y+dot.fy/100.0),System.currentTimeMillis()));
//				mSmartPenStrokeBuffer.add(new MGesturePoint(pointX,pointY,System.currentTimeMillis()));
//				将点坐标放进gestureBuffer中 完				
				
				}
/*				
				if(dot.type ==Dot.DotType.PEN_DOWN){
					Log.i("cahe","yyyyyyy"+pointY);
					if(pointY>2000){
						Calligraph.pigaihuanLayout.setVisibility(View.VISIBLE);
						Calligraph.pigaiResultImageView.setVisibility(View.GONE);
						Calligraph.pigaihuanPingyuText.setVisibility(View.GONE);
					}
				}*/
				// Log.i(TAG, "PEN_DOWN");
				gPIndex = 0;
				int PageID, BookID;
				PageID = dot.PageID;
				BookID = dot.BookID;
				if (PageID < 0 || BookID < 0) {
					// 谨防笔连接不切页的情况
					return;
				}

				// Log.i(TAG, "PageID=" + PageID + ",gCurPageID=" + gCurPageID +
				// ",BookID=" + BookID + ",gCurBookID=" + gCurBookID);
				if (PageID != gCurPageID || BookID != gCurBookID) {
					gbSetNormal = false;
					// SetBackgroundImage(BookID, PageID);
					// gImageView.setVisibility(View.VISIBLE);
					bIsOfficeLine = true;
					gCurPageID = PageID;
					gCurBookID = BookID;
//					drawInit(mDrawView);
//					DrawExistingStroke(gCurBookID, gCurPageID,mDrawView);
				}

//				SetPenColor(gColor);
				drawSubFountainPen2(mDrawView, gScale, gOffsetX, gOffsetY,
						gWidth, (float)(pointX), (float)(pointY*1.12), pointZ, 0);
				// drawSubFountainPen3(mDrawView, gScale, gOffsetX, gOffsetY,
				// gWidth, pointX, pointY, pointZ);

				// 保存屏幕坐标，原始坐标会使比例缩小
				saveData(gCurBookID, gCurPageID, pointX, pointY, pointZ, 0,
						gWidth, gColor, dot.Counter, dot.angle);
				//wsk 2019.4.14
				savePointData(gpointX,gpointY,dot.BookID);
				mov_x = pointX;
				mov_y = pointY;
				return;
			}

			if (dot.type == Dot.DotType.PEN_MOVE) {
				penUpTime=System.currentTimeMillis();
				
				//wsk 2019.5.30
				points.add(new SimplePoint((float)(dot.x+dot.fx/100.0), (float)(dot.y+dot.fy/100.0),System.currentTimeMillis(),
						dot.force,dot.OwnerID,dot.PageID,dot.SectionID,dot.BookID,
						dot.color,dot.angle,dot.Counter));
				
				SmartPenStrokeBuffer.add(new GesturePoint((float)(dot.x+dot.fx/100.0),(float)(dot.y+dot.fy/100.0),System.currentTimeMillis()));
				// Log.i(TAG, "PEN_MOVE");
				// gPIndex = 0;
				// Pen Move
				gPIndex += 1;
				mN += 1;
				mov_x = pointX;
				mov_y = pointY;

//				SetPenColor(gColor);
				/*
				 * zgm 20190508解决意外(同一张纸上)切页问题 始
				 */
//				if (dealSmartPenGesture.activity.ispendown) {//强制转化为第一笔
//					drawSubFountainPen2(mDrawView, gScale, gOffsetX, gOffsetY,
//							gWidth, (float)(pointX), (float)(pointY*1.12), pointZ, 0);
//					dealSmartPenGesture.activity.ispendown=false;
//				}else {
					drawSubFountainPen2(mDrawView, gScale, gOffsetX, gOffsetY,
							gWidth, (float)(pointX), (float)(pointY*1.12), pointZ, 1);					
//				}
				/*
				 * zgm 20190508解决意外(同一张纸上)切页问题 完
				 */

				// drawSubFountainPen3(mDrawView, gScale, gOffsetX, gOffsetY,
				// gWidth, pointX, pointY, pointZ);
				mDrawView.invalidate();
				// 保存屏幕坐标，原始坐标会使比例缩小
				saveData(gCurBookID, gCurPageID, pointX, pointY, pointZ, 1,
						gWidth, gColor, dot.Counter, dot.angle);
				//wsk 2019.4.14
				savePointData(gpointX,gpointY,dot.BookID);
			}
		} else if (dot.type == Dot.DotType.PEN_UP) {
			SmartPenStrokeBuffer.add(new GesturePoint((float)(dot.x+dot.fx/100.0),(float)(dot.y+dot.fy/100.0),System.currentTimeMillis()));
//			mSmartPenStrokeBuffer.add(new MGesturePoint(pointX,pointY,System.currentTimeMillis()));
			//wsk 2019.5.30
			if(bihua == true)
			{
				bihua = false;
				firgesture.put(getureStrokeCout+"", points);
				allgetureStrokeCout++;
//				allgesture.put(allgetureStrokeCout+"", points);
			}
			else
			{
				getureStrokeCout++;
				allgetureStrokeCout++;
	            gesture.put(getureStrokeCout+"", points);
//	            allgesture.put(allgetureStrokeCout+"", points);
			}
			
			//wsk 2019.4.14 0419
			//判断是否是布置作业，如果是否，继续判别手势
			savePointData(gpointX,gpointY,dot.BookID);
			//wsk 20190426
			//ArrangeHomework增加参数：pageID
			if(Calligraph.sceneSituation==0)IsArrangeGomework = ArrangeHomework(point_number,dot.BookID,dot.PageID);//布置作业函数
			transferToGesture(SmartPenStrokeBuffer);
			/*			if(Calligraph.sceneSituation==1)
			{
				transferToGesture(SmartPenStrokeBuffer);
			}*/
			point_number.clear();
			
			//transferToGesture(SmartPenStrokeBuffer);
			// Log.i(TAG, "PEN_UP");
			// Pen Up
			if (dot.x == 0 || dot.y == 0) {
				pointX = mov_x;
				pointY = mov_y;
			}

			gPIndex += 1;
			drawSubFountainPen2(mDrawView, gScale, gOffsetX, gOffsetY, gWidth,
					(float)(pointX), (float)(pointY*1.12), pointZ, 2);
			// drawSubFountainPen3(mDrawView, gScale, gOffsetX, gOffsetY,
			// gWidth, pointX, pointY, pointZ);
			// 保存屏幕坐标，原始坐标会使比例缩小
			saveData(gCurBookID, gCurPageID, pointX, pointY, pointZ, 2, gWidth,
					gColor, dot.Counter, dot.angle);
			//wsk 2019.4.14
			//savePointData(gpointX,gpointY,dot.BookID);
			mDrawView.invalidate();

			pointX = 0;
			pointY = 0;
			mN = 0;
			gPIndex = -1;
			penUpTime=System.currentTimeMillis();
			if(firstpen==true) {
				firstpen=false;
			new Thread(new Runnable()
			{

				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					firstpen=false;
					curTime=System.currentTimeMillis();
	//Log.e("di","1129:hah计时开始："+System.currentTimeMillis());				
					while(curTime-penUpTime<1000) {
						curTime=System.currentTimeMillis();
					}
					{
						firstpen=true;
						firstPenChi=true;
						
						//wsk 2019.4.18
						//判断是否是布置作业，如果是否，继续判别手势
						if(Calligraph.sceneSituation == 1)
						{
//							dealSmartPenGesture.dealWithGesture(currentSmartPenGesture);
							GestureInfor gestureInfor=dealSmartPenGesture.recogniseGesture(currentSmartPenGesture);
							if (gestureInfor==null) {
								return;
								
							}
							Log.e("zgm","0610:"+ gestureInfor.getGestureName());
							dealSmartPenGesture.gestureResponse(gestureInfor);
						}
						else if (Calligraph.sceneSituation == 2) 
						{
							GestureInfor gestureInfor=null;
							try {
//								dealSmartPenGesture.recognize(currentSmartPenGesture);
								 gestureInfor=dealSmartPenGesture.svmRecognize(currentSmartPenGesture);
										dealSmartPenGesture.gestureResponse(gestureInfor);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(gestureInfor.getGestureName().equals("书写")) {
								gestureInfor=dealSmartPenGesture.recogniseGesture(currentSmartPenGesture);
								if (gestureInfor==null) {
									return;
									}
								Log.e("zgm","0614:"+ gestureInfor.getGestureName());
								dealSmartPenGesture.gestureResponse(gestureInfor);
							}
							
							
							gesture.clear();
							firgesture.clear();
							bihua = true;
							
						}
						//dealSmartPenGesture.dealWithGesture(currentSmartPenGesture);
						Log.e("di", "1129:计时完成："+System.currentTimeMillis());
					}
					}

				
			}).start();
			}			
		}
	}

	//wsk 2019.5.30
	void bufferInit(){
		points=new ArrayList<SimplePoint>();
//		gesture.clear();
	}	
	private void transferToGesture(
			ArrayList<GesturePoint> mSmartPenStrokeBuffer2) {
		// TODO Auto-generated method stub
/*		if (mCurrentGesture==null) {
			mCurrentGesture= new MGesture(new MGestureStroke(mSmartPenStrokeBuffer2));
			return;
		}else {
			if (true) {
				mCurrentGesture.clearMGestureStroke();
				mCurrentGesture.addMGestureStroke(new MGestureStroke(mSmartPenStrokeBuffer2));
				
			}
		}*/
		
		if (currentSmartPenGesture==null) {
			currentSmartPenGesture= new SmartPenGesture();
			currentSmartPenGesture.addStroke(new GestureStroke(mSmartPenStrokeBuffer2));
			firstPenChi=false;
		return;
	}else {
		if (firstPenChi) {
			firstPenChi=false;
			currentSmartPenGesture.SmartPenGestureClearAllStroke();
//			Log.e("zgm", "0108:"+currentSmartPenGesture.getBoundingBox());
			currentSmartPenGesture.SmartPenGestureClearmBoundingBox();
		}
//		Log.e("zgm", "01181:"+currentSmartPenGesture.getStrokesCount());
		currentSmartPenGesture.addStroke(new GestureStroke(mSmartPenStrokeBuffer2));
//		Log.e("zgm", "01182:"+currentSmartPenGesture.getStrokesCount());
	 }	
	}	
	public void SetPenColor(int ColorIndex,DrawView mDrawView) {
		switch (ColorIndex) {
		case 0:
			mDrawView.paint.setColor(Color.GRAY);
			return;
		case 1:
			mDrawView.paint.setColor(Color.RED);
			return;
		case 2:
			mDrawView.paint.setColor(Color.rgb(192, 192, 0));
			return;
		case 3:
			mDrawView.paint.setColor(Color.rgb(0, 128, 0));
			return;
		case 4:
			mDrawView.paint.setColor(Color.rgb(0, 0, 192));
			return;
		case 5:
			mDrawView.paint.setColor(Color.BLUE);
			return;
		case 6:
			mDrawView.paint.setColor(Color.BLACK);
			return;
		case 7:
			mDrawView.paint.setColor(Color.MAGENTA);
			return;
		case 8:
			mDrawView.paint.setColor(Color.CYAN);
			return;
		}
		return;
	}

	public void drawInit(DrawView dv) {

		dv.initDraw();
		dv.setVcolor(Color.WHITE);
		dv.setVwidth(1);

		SetPenColor(gColor,dv);
		dv.paint.setStrokeCap(Paint.Cap.ROUND);
		dv.paint.setStyle(Paint.Style.FILL);
		dv.paint.setAntiAlias(true);
		dv.invalidate();

	}

	/*
	 * private void SetBackgroundImage(int BookID, int PageID) { if
	 * (!gbSetNormal) { LayoutParams para; para = gImageView.getLayoutParams();
	 * para.width = BG_WIDTH; para.height = BG_HEIGHT;
	 * gImageView.setLayoutParams(para); gbSetNormal = true;
	 * 
	 * //Log.i(TAG, "testOffset BG_WIDTH = " + BG_WIDTH + ", BG_HEIGHT =" +
	 * BG_HEIGHT + ", gcontentLeft = " + gcontentLeft + ", gcontentTop = " +
	 * gcontentTop); //Log.i(TAG, "testOffset A5_X_OFFSET = " + A5_X_OFFSET +
	 * ", A5_Y_OFFSET = " + A5_Y_OFFSET); //Log.i(TAG, "testOffset mWidth = " +
	 * mWidth + ", mHeight = " + mHeight); //Log.i(TAG, "testOffset getTop = " +
	 * gImageView.getTop() + ", getLeft = " + gImageView.getLeft());
	 * //Log.i(TAG, "testOffset getWidth = " + gImageView.getWidth() +
	 * ", getHeight = " + gImageView.getHeight()); //Log.i(TAG,
	 * "testOffset getMeasuredWidth = " + gImageView.getMeasuredWidth() +
	 * ", getMeasuredHeight = " + gImageView.getMeasuredHeight()); }
	 * 
	 * gbCover = true; bDrawl[0].canvas.drawColor(Color.TRANSPARENT,
	 * PorterDuff.Mode.CLEAR); if (BookID == 168) { if
	 * (getResources().getIdentifier("p" + PageID, "drawable", getPackageName())
	 * == 0) { return; }
	 * gImageView.setImageResource(getResources().getIdentifier("p" + PageID,
	 * "drawable", getPackageName())); } else if (BookID == 100) { if
	 * (getResources().getIdentifier("p" + PageID, "drawable", getPackageName())
	 * == 0) { return; }
	 * gImageView.setImageResource(getResources().getIdentifier("p" + PageID,
	 * "drawable", getPackageName())); } else if (BookID == 0) { if
	 * (getResources().getIdentifier("blank" + PageID, "drawable",
	 * getPackageName()) == 0) { return; }
	 * gImageView.setImageResource(getResources().getIdentifier("blank" +
	 * PageID, "drawable", getPackageName())); } else if (BookID == 1) { if
	 * (getResources().getIdentifier("zhen" + PageID, "drawable",
	 * getPackageName()) == 0) { return; }
	 * gImageView.setImageResource(getResources().getIdentifier("zhen" + PageID,
	 * "drawable", getPackageName())); } }
	 */

	/*
	 * 根据传入的原始点的部分属性重新打包成Dots，并存放在bookID对应的dot_number
	 * dot_number类型为ArrayListMultimap<Integer, Dots>
	 */
	private void saveData(Integer bookID, Integer pageID, float pointX,
			float pointY, int force, int ntype, int penWidth, int color,
			int counter, int angle) {
		Log.i(TAG, "======savaData pageID======" + pageID + "========sdfsdf"
				+ angle);
		Dots dot = new Dots(bookID, pageID, pointX, pointY, force, ntype,
				penWidth, color, counter, angle);

		try {
			if (bookID == 100) {
				dot_number.put(pageID, dot);
			} else if (bookID == 0) {
				dot_number1.put(pageID, dot);
			} else if (bookID == 1) {
				dot_number2.put(pageID, dot);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void ProcessDots(final Dot dot,DrawView mDrawView) {
		// //Log.i(TAG, "=======222draw dot=======" + dot.toString());
		/*
		 * // 回放模式，不接受点 if (bIsReply) { return; }
		 */
		putPointIntoPage(dot);
		ProcessEachDot(dot,mDrawView);

	}

	private void saveOutDotLog(Integer bookID, Integer pageID, float pointX,
			float pointY, int force, int ntype, int penWidth, int color,
			int counter, int angle) {
		// Log.i(TAG, "======savaData pageID======" + pageID + "========sdfsdf"
		// + angle);
		Dots dot = new Dots(bookID, pageID, pointX, pointY, force, ntype,
				penWidth, color, counter, angle);

		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		String str1 = formatter1.format(curDate);
		String hh = str.substring(0, 2);

		if (!gStrHH.equals(hh)) {
			// Log.i(TAG, "sssssss " + gStrHH + " " + hh);
			gStrHH = hh;
			bLogStart = true;
		}

		String txt = str + "BookID: " + bookID + " PageID: " + pageID
				+ " Counter: " + counter + "  pointX: " + gpointX
				+ "  pointY: " + gpointY + "  force: " + force + "  angle: "
				+ angle;
		String fileName = str1 + gStrHH + ".log";
		if (isSaveLog) {
			if (bLogStart) {
				BLEFileUtil
						.writeTxtToFile(
								"-------------------------TQL SmartPen LOG--------------------------",
								LOGPATH, fileName);
				bLogStart = false;
			}

			BLEFileUtil.writeTxtToFile(txt, LOGPATH, fileName);
		}
	}
	
	//wsk 2019.4.14
		//布置作业函数  
		public boolean ArrangeHomework(ArrayListMultimap<Integer, points> point_number12, int bookID,int pageID) 
		    {
			// TODO Auto-generated method stub
			    //String path = Environment.getExternalStorageDirectory().getPath();
			    ArrayList<Integer> itemnumber = new ArrayList<Integer>();
		    	itemnumber = SendHomework.ReceiveSendHomenworkGestureDots(point_number12, bookID,pageID);
		    	if(itemnumber == null)
		    	{
		    		return false;
		    	}
		    	
		    	else if(itemnumber.get(0) == -1)
		    	{
		    		switch(gCurPageID%20)
		    		{
		    		case 1:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup01);
		    		break;
		    		case 2:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup02);
		    		break;
		    		case 3:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup03);
		    		break;
		    		case 4:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup04);
		    		break;
		    		case 6:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup06);
		    		break;
		    		case 7:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup07);
		    		break;
		    		case 9:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup09);
		    		break;
		    		case 10:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup010);
		    		break;
		    		case 11:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup011);
		    		break;
		    		case 12:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup012);
		    		break;
		    		case 14:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup014);
		    		break;
		    		case 15:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup015);
		    		break;
		    		case 16:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup016);
		    		break;
		    		case 17:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup017);
		    		break;
		    		case 18:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup018);
		    		break;
		    		case 19:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup019);
		    		break;
		    		default:
		    		break;
		    		}
		    		soundTime = System.currentTimeMillis();
					curTime=System.currentTimeMillis();			
					while(curTime-soundTime<7000) 
					{
						curTime=System.currentTimeMillis();
					}
					showSound(com.jinke.calligraphy.app.branch.R.raw.quanbutimu);
		    		tihao.clear();
		    		return true;
		    	}
		    	
		    	else if(itemnumber.get(0) == -2)
		    	{
		    		switch(gCurPageID%20)
		    		{
		    		case 2:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup02);
		    		break;
		    		case 5:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup05);
		    		break;
		    		case 4:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup04);
		    		break;
		    		case 7:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup07);
		    		break;
		    		case 8:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup08);
		    		break;
		    		case 10:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup010);
		    		break;
		    		case 12:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup012);
		    		break;
		    		case 13:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup013);
		    		break;
		    		case 15:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup015);
		    		break;
		    		case 17:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup017);
		    		break;
		    		case 19:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup019);
		    		break;
		    		default:
		    		break;
		    		}
		    		soundTime = System.currentTimeMillis();
					curTime=System.currentTimeMillis();			
					while(curTime-soundTime<7000) 
					{
						curTime=System.currentTimeMillis();
					}
					showSound(com.jinke.calligraphy.app.branch.R.raw.quanbutimu);
		    		tihao.clear();
		    		return true;
		    	}
		    	
		    	else if(itemnumber.get(1) == 0)
		    	{
		    		tihao.add(itemnumber.get(0));
		    		group.add(itemnumber.get(2));
		    		Calligraph.my_toast("第"+itemnumber.get(0)+"题");
		    		return true;
	
		    	}
		    	
		    	else if(itemnumber.get(1) == 1)
		    	{
		    		group.add(itemnumber.get(2));
		    		tihao.add(itemnumber.get(0));
		    		//播放所留作业题号的声音，间隔两秒
		    		showSound(com.jinke.calligraphy.app.branch.R.raw.success);
		    		soundTime = System.currentTimeMillis();
					curTime=System.currentTimeMillis();			
					while(curTime-soundTime<2000) 
					{
						curTime=System.currentTimeMillis();
					}
					
//					showInftTextView.setText("组："+group.get(0));
					if(group.get(0) == -1)
					{
						switch(gCurPageID%20)
			    		{
			    		case 1:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup01);
			    		break;
			    		case 2:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup02);
			    		break;
			    		case 3:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup03);
			    		break;
			    		case 4:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup04);
			    		break;
			    		case 6:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup06);
			    		break;
			    		case 7:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup07);
			    		break;
			    		case 9:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup09);
			    		break;
			    		case 10:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup010);
			    		break;
			    		case 11:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup011);
			    		break;
			    		case 12:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup012);
			    		break;
			    		case 14:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup014);
			    		break;
			    		case 15:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup015);
			    		break;
			    		case 16:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup016);
			    		break;
			    		case 17:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup017);
			    		break;
			    		case 18:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup018);
			    		break;
			    		case 19:showSound(com.jinke.calligraphy.app.branch.R.raw.agroup019);
			    		break;
			    		default:
			    		break;
			    		}
						soundTime = System.currentTimeMillis();
		    			curTime=System.currentTimeMillis();			
		    			while(curTime-soundTime<5000) 
		    			{
		    				curTime=System.currentTimeMillis();
		    			}
					}
					
					else
					{
						switch(gCurPageID%20)
			    		{
			    		case 2:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup02);
			    		break;
			    		case 5:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup05);
			    		break;
			    		case 4:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup04);
			    		break;
			    		case 7:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup07);
			    		break;
			    		case 8:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup08);
			    		break;
			    		case 10:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup010);
			    		break;
			    		case 12:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup012);
			    		break;
			    		case 13:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup013);
			    		break;
			    		case 15:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup015);
			    		break;
			    		case 17:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup017);
			    		break;
			    		case 19:showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup019);
			    		break;
			    		default:
			    		break;
			    		}
			    		soundTime = System.currentTimeMillis();
						curTime=System.currentTimeMillis();			
						while(curTime-soundTime<5000) 
						{
							curTime=System.currentTimeMillis();
						}
					}
					
					currentGroup = group.get(0);
		    		for(int i = 0;i<tihao.size();i++)
		    		{
		
		    			if(currentGroup != group.get(i))
		    			{
		    				soundTime = System.currentTimeMillis();
		        			curTime=System.currentTimeMillis();			
		        			while(curTime-soundTime<1500) 
		        			{
		        				curTime=System.currentTimeMillis();
		        			}
		    				if(group.get(i) == -1)
		    				{
		    					showSound(com.jinke.calligraphy.app.branch.R.raw.agroup);
		    				}
		    				
		    				else
		    				{
		    					showSound(com.jinke.calligraphy.app.branch.R.raw.bgroup);
		    				}
		    				currentGroup = group.get(i);
		    			}
						soundTime = System.currentTimeMillis();
		    			curTime=System.currentTimeMillis();			
		    			while(curTime-soundTime<1500) 
		    			{
		    				curTime=System.currentTimeMillis();
		    			}
		    			
						switch(tihao.get(i))
						{
						case 1:showSound(com.jinke.calligraphy.app.branch.R.raw.one);
						       break;
						case 2:showSound(com.jinke.calligraphy.app.branch.R.raw.two);
					       break;
						case 3:showSound(com.jinke.calligraphy.app.branch.R.raw.three);
					       break;
						case 4:showSound(com.jinke.calligraphy.app.branch.R.raw.four);
					       break;
						case 5:showSound(com.jinke.calligraphy.app.branch.R.raw.five);
					       break;
						case 6:showSound(com.jinke.calligraphy.app.branch.R.raw.six);
					       break;
						case 7:showSound(com.jinke.calligraphy.app.branch.R.raw.seven);
					       break;
						case 8:showSound(com.jinke.calligraphy.app.branch.R.raw.eight);
					       break;
						case 9:showSound(com.jinke.calligraphy.app.branch.R.raw.nine);
					       break;
						case 10:showSound(com.jinke.calligraphy.app.branch.R.raw.ten);
					       break;
						default:break;
						}
		    		}
		    		tihao.clear();
		    		group.clear();
		    		//等待五秒，播放学生收到作业的反馈音频
		    		soundTime = System.currentTimeMillis();
		    		curTime=System.currentTimeMillis();			
		    		while(curTime-soundTime<3000) 
		    		{
		    			
		    			curTime=System.currentTimeMillis();
		    		}
		    		showSound(com.jinke.calligraphy.app.branch.R.raw.feedback);
		    	}
		    	return true;
			}
		//播放音频
	    public void showSound(int raw) {
			if (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer = MediaPlayer.create(Start.context, raw);
			mediaPlayer.setVolume(1.0f, 1.0f);
			mediaPlayer.start();
		}
		//wsk 2019.4.14
				//播放音频
				public void showSound(String path) 
				{
					//MediaPlayer mediaPlayer = new MediaPlayer();
					if (mediaPlayer!=null&&mediaPlayer.isPlaying()) 
					{
						//mediaPlayer.stop();
						return;
					}
						try {
							mediaPlayer.reset();
							mediaPlayer.setDataSource(path);
							mediaPlayer.prepareAsync();;
							mediaPlayer.setVolume(1.0f, 1.0f);
							mediaPlayer.start();
							
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
				}
			
	    //wsk 2019.4.14
		//保存点，用于布置作业
		private void savePointData(float pointX,float pointY,int number)
		{
//			pointX = (float) (pointX/138.14*1519)+20;
//			pointY = (float) (pointY/194.296*2151)+100;
			points point = new points(pointX, pointY, number);
		    try 
			{
		    	point_number.put(number, point);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		 }
	public void DrawExistingStroke(int BookID, int PageID,DrawView mDrawView) {
		Log.e("zgm", "执行函数DrawExistingStroke");
		if (BookID == 100) {
			dot_number4 = dot_number;
		} else if (BookID == 0) {
			dot_number4 = dot_number1;
		} else if (BookID == 1) {
			dot_number4 = dot_number2;
		}

		if (dot_number4.isEmpty()) {
			return;
		}

		Set<Integer> keys = dot_number4.keySet();
		for (int key : keys) {
			// Log.i(TAG, "=========pageID=======" + PageID + "=====Key=====" +
			// key);
			if (key == PageID) {
				List<Dots> dots = dot_number4.get(key);
				for (Dots dot : dots) {
					// Log.i(TAG, "=========pageID=======" + dot.pointX + "===="
					// + dot.pointY + "===" + dot.ntype);

					drawSubFountainPen1(mDrawView, gScale, gOffsetX, gOffsetY,
							dot.penWidth, dot.pointX, dot.pointY, dot.force,
							dot.ntype, dot.ncolor);
				}
			}
		}

		mDrawView.postInvalidate();
		gPIndex = -1;
	}

	public void drawSubFountainPen1(DrawView DV, float scale, float offsetX,
			float offsetY, int penWidth, float x, float y, int force,
			int ntype, int color) {
		Log.e("zgm", "执行函数drawSubFountainPen1");

		if (ntype == 0) {
			g_x0 = x;
			g_y0 = y;
			g_x1 = x;
			g_y1 = y;
			// Log.i(TAG, "--------draw pen down-------");
		}

		if (ntype == 2) {
			g_x1 = x;
			g_y1 = y;
			Log.i("TEST", "--------draw pen up--------");
			// return;
		} else {
			g_x1 = x;
			g_y1 = y;
			// Log.i(TAG, "--------draw pen move-------");
		}

		DV.paint.setStrokeWidth(penWidth);
		DV.paint.setColor(color);
		DV.canvas.drawLine(g_x0, g_y0, g_x1, g_y1, DV.paint);
		g_x0 = g_x1;
		g_y0 = g_y1;

		return;
	}

	public void drawSubFountainPen2(DrawView DV, float scale, float offsetX,
			float offsetY, int penWidth, float x, float y, int force, int ntype) {

		// x = x % 1600;
		// y =y%955000;
		// y = y % 2500;
		Log.i("zgm", "执行函数drawSubFountainPen2");
		if (ntype == 0) {
			g_x0 = x;
			g_y0 = y;
			g_x1 = x;
			g_y1 = y;
			// Log.i(TAG, "--------draw pen down-------");
		}
		if (ntype == 2) {
			g_x1 = x;
			g_y1 = y;
			Log.i("TEST", "--------draw pen up--------");
		} else {
			g_x1 = x;
			g_y1 = y;

			// Log.i(TAG, "--------draw pen move-------");
		}
//		float[] currentPoint = PointTransplantIntoScreen(x, y);// currentPoint[0]存放x的值，currentPoint[1]存放y的值
//		float[] LastPoint = PointTransplantIntoScreen(g_x0, g_y0);

//		g_x0 = g_x1;// 将现在的数据保存
//		g_y0 = g_y1;// 将现在的数据保存

		/*
		 * 下面的if判断是为了处理上一个点的横坐标和当前的点的横坐标是否在同一个显示区域内([0~screenwidth]),即两个点是否跨界 开始
		 */
/*
		if ((x - g_x0) * (currentPoint[0] - LastPoint[0]) < 0) {

			if (x > g_x0) {
				currentPoint[0] = screenwidth;
				g_x0 = screenwidth;
			} else {
				currentPoint[0] = 0;
				g_x0 = 0;
			}
		}

		if (Math.abs(LastPoint[0] - currentPoint[0]) > screenwidth / 2) {
			LastPoint[0] = currentPoint[0];
			g_x0 = g_x1;
		}
*/
		/*
		 * 上面的if判断是为了处理上一个点的横坐标和当前的点的横坐标是否在同一个显示区域内([0~screenwidth]),即两个点是否跨界 完
		 */
/*
		if ((y - g_y0) * (currentPoint[1] - LastPoint[1]) < 0) {

			if (y > g_y0) {
				currentPoint[1] = screenhight;
				g_y0 = screenhight;
			} else {
				currentPoint[1] = 0;
				g_y0 = 0;
			}
		}
*/
		/*
		 * 下面的if判断是为了处理上一个点的横坐标和当前的点的横坐标是否在同一个显示区域内([0~screenwidth]),即两个点是否跨界 开始
		 */
/*
		if (Math.abs(LastPoint[1] - currentPoint[1]) > screenhight / 2) {
			LastPoint[1] = currentPoint[1];
			g_y0 = g_y1;
		}
*/
		/*
		 * 上面的if判断是为了处理上一个点的横坐标和当前的点的横坐标是否在同一个显示区域内([0~screenwidth]),即两个点是否跨界 完
		 */
		Log.i("zgm", "ntype_______" + ntype);
		Log.i("zgm", "g_x0:" + g_x0 + " " + "g_y0:" + g_y0 + " " + "g_x1:"
				+ g_x1 + " " + "g_y1:" + g_y1);
		DV.paint.setStrokeWidth(penWidth);
//		DV.paint.setColor(Color.RED);
//		if (g_x0==0||g_y0==0) {
//			g_x0=g_x1;
//			g_y0=g_y1;
//		}
		DV.canvas.drawLine(g_x0, g_y0, g_x1, g_y1, DV.paint);
//        DV.canvas.drawLine((int)g_x0, (int)g_y0, (int)g_x1,(int) g_y1, DV.paint);
//		DV.canvas.drawLine(currentPoint[0], currentPoint[1], LastPoint[0],LastPoint[1], DV.paint);
		// DV.canvas.drawLine(g_x0, g_y0, 1000, 1000, DV.paint);
		DV.invalidate();
//		if(com.jinke.calligraphy.app.branch.Calligraph.pigaihuanLayout.getVisibility()==View.VISIBLE) {
//			
//			g_x0 = g_x1;
//			g_y0 = g_y0;
//		}
//		else {
			  g_x0 = g_x1;
		      g_y0 = g_y1;
//		}
       

		return;
	}

	private float[] PointTransplantIntoScreen(float point_x, float point_y) {
		// System.out.println(new BigDecimal(15.2%5).floatValue());

		if ((point_x % screenwidth) < 4 && point_x >= 2) {
			// Log.e("zgm", "aab:"+(point_x
			// %screenwidth)+" "+"point_x:"+point_x+" "+"screenwidth:"+screenwidth);
			point_x = screenwidth;

		} else {
			point_x = point_x % screenwidth;
		}
		if (point_y % screenhight < 4 & point_y >= 1) {
			point_y = screenhight;
		} else {

			point_y = point_y % screenwidth;
		}

		float[] mpoint = new float[2];
		mpoint[0] = point_x;
		mpoint[1] = point_y;
		return mpoint;
	}
	public void putPointIntoPage(Dot dot){
		if (smartPenPage==null) {
			smartPenPage=new SmartPenPage(dot.PageID, dot.BookID);
		}
		if (dot.PageID!=smartPenPage.pageNumber||dot.BookID!=smartPenPage.bookeId){
			String fileName =smartPenPage.bookeId+"-"+smartPenPage.pageNumber+"-1.page";
			SmartPenUnitils.save(smartPenPage,fileName);
			smartPenPage=new SmartPenPage(dot.PageID, dot.BookID);
		}
		 
	
			smartPenPage.addStrokePoint(dot);
			Log.i("name",""+smartPenPage.pageNumber);
	
	}
	
	
	//加入批改环回放笔迹的功能

	
	public void RunReplay(final ArrayListMultimap<Integer, Dot>container,final DrawView dv) {
		dealSmartPenGesture.activity.dealingSomeThing=true;
		if (gCurPageID < 0) {
            bIsReply = false;
            return;
        }

        drawInit(dv);
        bDrawl[1].canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ReplayCurrentPage(container,dv);
                Message msg =new Message();
                msg.what = 430;//学生笔迹回放完成之后显示教师批改笔迹
                dealSmartPenGesture.activity.transHandler.sendMessage(msg);
                dealSmartPenGesture.activity.dealingSomeThing=false;
                //批改完成取消dialog
                if (!dealSmartPenGesture.activity.dealingSomeThing&&dealSmartPenGesture.activity.alertDialog!=null&&dealSmartPenGesture.activity.alertDialog.isShowing()) {
                	dealSmartPenGesture.activity.dealingSomeThing=false;
					// TODO Auto-generated method stub
                	dealSmartPenGesture.activity.alertDialog.dismiss();
                }
     
                
            }
        }).start();
        
    }

    public void ReplayCurrentPage(ArrayListMultimap<Integer, Dot>container,DrawView dv) {
    	int type = 0;
    	int previousDotTime = 0;
    	int sleepTime = 0;

        if (container.isEmpty()) {
            bIsReply = false;
            return;
        }

        Set<Integer> keys = container.keySet();
        for (int key : keys) {
            //Log.i(TAG, "=========pageID=======" + PageID + "=====Key=====" + key);
            bIsReply = true;
            if (key == Start.gCurPageID) {
            	for(Dot dot:container.get(gCurPageID)) 
            	{
            		
            		{
//            			sleepTime=(int) (dot.timelong-previousDotTime);
//            			previousDotTime = (int) dot.timelong;
            			if(Calligraph.pigaihuanLayout.getVisibility()==View.VISIBLE) {
    						x = (float) (dot.x+dot.fx/100.0);
    						x *= 11.65;
    						y = (float) (15+dot.y-Start.pghCenterYOffset+dot.fy/100.0);//改掉128
    						y *= 11.0;
    					}
    					else{
    						x = (float) (dot.x+dot.fx/100.0);
    						x *= 11.65;
    						y = (float) (dot.y+dot.fy/100.0);
    						y *= 11.0;
    					}
						switch(dot.type) {
						case  PEN_DOWN:type=0;break;
						case  PEN_MOVE:type=1;break;
						case  PEN_UP:type=2;break;
						}
					}
            		//wsk 2019.6.18
            		if(x<=0 || y<=0)continue;
                    //Log.i(TAG, "=========pageID1111=======" + dot.pointX + "====" + dot.pointY + "===" + dot.ntype);
            		drawSubFountainPen1(dv, 1, 0, 0, 6, x, (float)y, dot.force, type,Color.BLACK);
                    //drawSubFountainPen3(bDrawl[0], gScale, gOffsetX, gOffsetY, dot.penWidth, dot.pointX, dot.pointY, dot.force);

                    bDrawl[1].postInvalidate();
                    SystemClock.sleep(10);
                }
            	bIsReply=false;
            }
   
	
	
	
        }
    }
	
	
	
}
