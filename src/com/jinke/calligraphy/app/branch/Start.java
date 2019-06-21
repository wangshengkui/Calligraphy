package com.jinke.calligraphy.app.branch;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.example.readAndSave.SmartPenPage;
import com.example.readAndSave.SmartPenUnitils;
import com.google.common.base.CaseFormat;
import com.google.common.collect.ArrayListMultimap;
import com.itextpdf.text.xml.simpleparser.NewLineHandler;
import com.jinke.calligraphy.activity.DownloadProgressActivity;
import com.jinke.calligraphy.activity.MainTab;
import com.jinke.calligraphy.activity.Properyt;
import com.jinke.calligraphy.app.branch.CursorDrawBitmap.Timer;
import com.jinke.calligraphy.backup.CalligraphyBackupUtil;
import com.jinke.calligraphy.command.BackupCommand;
import com.jinke.calligraphy.command.EditStatus;
import com.jinke.calligraphy.command.UploadCommand;
import com.jinke.calligraphy.database.CDBPersistent;
import com.jinke.calligraphy.database.CalligraphyDB;
import com.jinke.calligraphy.date.DateSlider;
import com.jinke.calligraphy.date.DateTimeSlider;
import com.jinke.calligraphy.date.GoogleCalendarUtil;
import com.jinke.calligraphy.date.TimeLabeler;
import com.jinke.calligraphy.fliplayout.FlipHorizontalLayout;
import com.jinke.calligraphy.template.WolfTemplateUtil;
import com.jinke.downloadanddecompression.DownLoaderTask;
import com.jinke.downloadanddecompression.ZipExtractorTask;
import com.jinke.kanbox.DownloadAllFileThread;
import com.jinke.kanbox.DownloadEntity;
import com.jinke.mywidget.FileListDialog;
import com.jinke.newly.Config;
import com.jinke.rloginservice.IReadingsLoginService;
import com.jinke.rloginservice.UserInfo;
import com.jinke.single.BitmapCount;
import com.jinke.single.BitmapUtils;
import com.jinke.single.ScaleSave;
import com.jinke.smartpen.BluetoothLEService;
import com.jinke.smartpen.DrawFromFileTask;
import com.jinke.smartpen.DrawFromFileTask1;
import com.jinke.smartpen.DrawView;
import com.jinke.smartpen.DrawfromPigaihuanContainer;
import com.jinke.smartpen.ToolFun;
import com.jinke.smartpen.UpLoadTask;
import com.tqltech.tqlpencomm.Dot;
import com.tqltech.tqlpencomm.Dot.DotType;
import com.tqltech.tqlpencomm.PenCommAgent;

import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.AvoidXfermode.Mode;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.Settings;
import android.renderscript.RenderScript.Priority;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import hallelujah.cal.CalligraphyVectorUtil;

public class Start extends Activity implements OnGestureListener,
		OnTouchListener {
	// 01-05 23:36:26.701: ERROR/AndroidRuntime(9178): at
	// com.jinke.calligraphy.database.CDBPersistent.getCharListByPageAndID(CDBPersistent.java:638)

	public static final String CLIENT_ID = "45fd6312c0c847d62017e483f05f5f50"; // 申请的api应用的client_id
	public static final String CLIENT_SECRET = "adf5b6555197ee52d8dbbd7ec1cb3fb9"; // 申请的api应用的client_secret
	public static final String REDIRECT_URI = "https://www.kanbox.com"; // 重定向url，可自行修改

	public static Context context;
	public static Activity instance;
	public static SharedPreferences LoginInfo;
	public static EditStatus status;
	public static Bitmap OOM_BITMAP;
	public static Bitmap EMPTY_BITMAP;
	// public static Bitmap BUTTOM_LINE_BITMAP;
	public static Bitmap RED_ARROW_BITMAP;
	public static Bitmap BLACK_ARROW_BITMAP;
	public static Bitmap backgroundBitmap;

	// public static Uri saveBitmapUri;
	// public static OutputStream imageFileOS;
	//
	public static String storagePath = "";
	static ProgressBar bar;
	public static TextView barText;
    public int reupLoadCounter=0;
	public static final String WIFI = "wifi";
	public static final String ADHOC = "adhoc";
	public static String netStatus = WIFI;
	// 定义广播Action
	private static final String BC_ACTION = "com.jinke.calligraphy.action.BC_ACTION";

	public static List<Uri> picList = new ArrayList<Uri>();// 存放URl
	public static List<Cursor> picCursor = new ArrayList<Cursor>();
	public static int picListIndex;
	public static int picCursorIndex;

	public static List<String> picName = new ArrayList<String>();
	public static int picNameIndex;
	MediaPlayer mediaPlayer = null;
	public static Calligraph c;
	public static int totlePageNum = 0;
	public static int PAGENUM = 1;
	public static String date = "";
	private static AlertDialog.Builder builder;
	public AlertDialog alertDialog;
	private UpLoadTask upLoadTask;
	static final int DATETIMESELECTOR_ID = 5;

	public static final int AddPictureRequest = 1;
	public static final int AddCameraRequest = 2;
	public static final int AddVideoRequest = 3;
	public static final int AddAudioRequest = 4;
	public static final String TempImgFilePath = "/sdcard/calligraphy/img.jpg";

	public static final String TempImgUpPath = "/sdcard/calligraphy/up.jpg";
	public static float timePerItem = 5;//统计单题答题时间
	// gesture detect
	GestureDetector mGestureDetector;

	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	private static float density;

	public static int limit_num = 1;
	public static int autoSaveTime = 2;
	private boolean paused = false;
	public static ParametersDialog parameterDialog;
	public static boolean netUploadLogin = false;
	public static boolean netDownloadLogin = false;

	public static double auto_upload_time = 10.3;

	public static String inputIp = "123.206.16.114";// 用户输入的Ip地址

	/**
	 * 变量for smartpen zgm
	 * 
	 */
	private BluetoothLEService mService = null; // 蓝牙服务
	private Intent serverIntent = null;

	private static final int REQUEST_SELECT_DEVICE = 20181101; // 蓝牙扫描
	private static final int REQUEST_ENABLE_BT = 20181102; // 开启蓝牙
	private static final int REQUEST_LOCATION_CODE = 20181103; // 请求位置权限
	private static final int GET_FILEPATH_SUCCESS_CODE = 20181104;// 获取txt文档路径成功

	private PenCommAgent bleManager;
	public static float mWidth; // 屏幕宽
	public static float mHeight; // 屏幕高

	public static int gCurPageID = -1; // 当前PageID
	
	public static int gCurPageIDx = -1; //模20用来加载模版作业，减100模8加载试卷
	public static int gCurBookID = -1; // 当前BookID
	private String gCurName = null;
	public  static String currentPageName = "NONE-0001-0-0-0.page";
	private int type=0;//dot的坐标，用在两个画布同步上
	private float x = 0;//dot的坐标，用在两个画布同步上
	private float y = 0;//dot的坐标，用在两个画布同步上
//	public int totalQuestion;
	public ArrayListMultimap<Integer, Dot> pigaihuanDotsContainer = ArrayListMultimap.create();//批改环中展示的学生写的数据
	public ArrayListMultimap<Integer,Dot>pigaihuanPigaiContainer = ArrayListMultimap.create();//批改环中教师的笔迹
	public ArrayListMultimap<Integer,Dot>studentDotsContainer = ArrayListMultimap.create();//下载的整份学生作业笔迹
	public ArrayListMultimap<Integer,Dot>teacherDotsContainer = ArrayListMultimap.create();//教师书写的实时笔迹
/*
 * zgm 20190508解决意外(同一张纸上)切页问题
 */
	//  public ArrayList<Dot> tepContainer=new ArrayList<Dot>();
/*	public int differentBooidOrPageidCounter=0;
	public Dot lastDot=null;*/
//	public boolean ispendown=true;
	/*
	 * zgm 20190508解决意外(同一张纸上)切页问题 完
	 */	
	public static int pghCenterYOffset; 
	public String commentString[][] = new String[15][5];//批改环文字
	int uploadTAG=0;//发送录音的题目
	String md5 = "";
	public  volatile boolean dealingSomeThing=false;
	public ToolFun toolFun = new ToolFun();
	

	File dotfile = new File("sdcard/-1/dot.txt");
	BufferedWriter	out ;
	BufferedWriter	outtemp ;
	private int penType = 1; // 笔类型（0：TQL-101 1：TQL-111 2：TQL-112 3: TQL-101A）
	public Typeface fontFace;
	private ServiceConnection mServiceConnection = new ServiceConnection() { 
		public void onServiceConnected(final ComponentName className,
				IBinder rawBinder) {
			mService = ((BluetoothLEService.LocalBinder) rawBinder)
					.getService();
			Log.d("mService:", "onServiceConnected mService= " + mService);
			if (!mService.initialize()) {
				finish();
			}

			mService.setOnDataReceiveListener(new BluetoothLEService.OnDataReceiveListener() {
				

				@Override
				public void onDataReceive(final Dot dot) {
//					if(dot.y>=128) {
//						pigaihuanPigaiContainer.put(dot.PageID,dot);
//					}
//			      if(Start.c.pigaihuanLayout.getVisibility()==View.VISIBLE) {
//			    	  dot.y+=128;
//			    	  pigaihuanDotsContainer.put(dot.PageID,dot);
//			      }
					
				//0506	
					
			if (dealingSomeThing) {//正在处理信息不接收点数据
				if (alertDialog==null||!alertDialog.isShowing()) {
					Log.e("0505", "加载文件中");
					showAlertDialog();
				}
//				Log.e("0505", "加载文件中");
				return;
			}
			if (!dealingSomeThing&&alertDialog!=null&&alertDialog.isShowing()) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dealingSomeThing=false;
						// TODO Auto-generated method stub
						alertDialog.dismiss();
						Log.e("0505", "消除dialog");
					}
				});
			}

					if(c.pigaihuanLayout.getVisibility()==View.VISIBLE)pigaihuanPigaiContainer.put(dot.PageID,dot);//批改环显示的时候将数据保存在pigaicontainer中0426

					if (dot.type==DotType.PEN_DOWN&&(dot.PageID != gCurPageID || dot.BookID != gCurBookID)) {
						/*
						 * zgm 20190508解决意外(同一张纸上)切页问题 始
						 */							
/*						if(lastDot!=null)
						{
							if(dot.PageID!=lastDot.PageID||dot.BookID!=lastDot.BookID) {
								differentBooidOrPageidCounter=0;
								lastDot=dot;	
								return;
							}else {
							if(differentBooidOrPageidCounter<5) {
							lastDot=dot;	
							differentBooidOrPageidCounter++;
							return;}
							else {
								differentBooidOrPageidCounter=0;
//								ispendown=true;//下面进行换页操作
							}
							}
							
						}
						if(lastDot==null) {
							lastDot=dot;
							dot.type=DotType.PEN_DOWN;
//						ispendown=true;
						}*/
						/*
						 * zgm 20190508解决意外(同一张纸上)切页问题 始
						 */	
						studentDotsContainer.clear();
						gCurBookID = dot.BookID;
						switch(gCurBookID) {
						case 0: 
							gCurPageIDx = dot.PageID%20;
							break;
						case 1: 
							Calligraph.sceneSituation = 2;
							gCurPageIDx = dot.PageID%8+100;
							Log.i("pageid","gCurPageIDx="+gCurPageIDx);
							break;
						default:break;}
						
						gCurPageID = dot.PageID;
						if(!teacherDotsContainer.isEmpty())teacherDotsContainer.clear();
						
						 toolFun.bDrawl[0].canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);//清除画布
						 toolFun.bDrawl[1].canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);//清除画布
						 currentPageName = getCurrentPageName("/sdcard/xyz/",gCurBookID, gCurPageID);
//						Log.i("getfile", "当前纸的名字是" + currentPageName);
					
						 //0425cahe切换底图
						
						try {
							setSmartpenPageBackground(gCurBookID,gCurPageID);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
						
						
						doDrawFromFile(currentPageName , toolFun.bDrawl[0]);
					
//						c.view.freeBitmap.addBgPic(bitmap);
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								toolFun.SetPenColor(6, toolFun.bDrawl[0]);
								//0425切换透明统计条
								c.transParentStatisticLayout.removeAllViews();
								final File file = new File("/sdcard/pagebackground/" + gCurPageIDx+".xml");
								for(int i=0;i<c.totalQuestion;i++)c.pos[i]=0;//0427强行清空c.pos[i]
								Document doc = null;
								try {
									doc = Jsoup.parse(file, "UTF-8");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									return;
								}
								if(doc==null)return;
								Elements element = doc.getElementsByTag("ystart");//解析xml模板
								c.totalQuestion = element.size();
								
								for(int i=0;i<c.totalQuestion;i++) {
								c.pos[i] = Integer.valueOf(element.get(i).text().toString());
								c.pos[i] *= 11.0;
								c.statisticTextView[i] = new BorderTextView(context);
								LayoutParams stvLp = new LayoutParams(900, 100);
								stvLp.setMargins(700,
										(int)((Integer.valueOf(element.get(i).text().toString()))*13.5), 1500,
										2800 - (int)(Integer.valueOf(element.get(i).text().toString())*11.0));
								c.statisticTextView[i].setGravity(Gravity.CENTER_VERTICAL);
								c.statisticTextView[i].setBackgroundResource(R.drawable.st);
								c.transParentStatisticLayout.addView(c.statisticTextView[i], stvLp);
								c.statisticTextView[i].setVisibility(View.GONE);
//								c.statisticTextView[i].setText(i+"-----");
								
								//增加盛放评语的textview
								
								c.commentsTv[i] =new TextView(context);
								LayoutParams ctvLp = new LayoutParams(900, 100);
								ctvLp.setMargins(500,
										(int)((Integer.valueOf(element.get(i).text().toString()))*13.5)+200, 1500,
										2800 - (int)(Integer.valueOf(element.get(i).text().toString())*11.0));
								c.commentsTv[i].setGravity(Gravity.CENTER_VERTICAL);
								c.transParentStatisticLayout.addView(c.commentsTv[i], ctvLp);
								
								
								c.commentsTv[i].setTypeface(fontFace);
								
								
								
								}
		
							}
						});
						
					}
//					lastDot=dot;
					
					//将所有教师笔迹加入0426
					teacherDotsContainer.put(dot.PageID,dot);
					
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
/*							Log.i("zgm0414", "Dot信息,BookID" + dot.BookID);
							Log.i("zgm0414", "Dot信息,PageID" + dot.PageID);
							Log.i("zgm0414", "Dot信息,ab_x" + dot.ab_x);
							//获取当前纸张名称cahe
							gCurBookID = dot.BookID;
							gCurPageID = dot.PageID;
							currentPageName = getCurrentPageName(gCurBookID, gCurPageID);
							Log.i("zgm0414", "当前纸的名字是"+currentPageName);*/
							if (c.pigaihuanLayout.getVisibility() == View.VISIBLE) {
						
								
					
//									dot.y = dot.y-128;
								toolFun.bDrawl[1].paint.setColor(Color.RED);
								toolFun.ProcessDots(dot, toolFun.bDrawl[1]);
//								dot.y = dot.y+128;
//								toolFun.ProcessDots(dot, toolFun.bDrawl[0]);
								
									
							
								
//								Log.i("zgm0414", "Dot信息,dot.x=" + dot.x);
//								Log.i("zgm0414", "Dot信息,dot.y=" + dot.y);
//								toolFun.SetPenColor(1, toolFun.bDrawl[1]);
								
								
//								x = (float) (dot.x+dot.fx/100.0);
//								x *= 11.65;
//								y = (float) (dot.y+dot.fy/100.0);
//								y *= 11.75;
//								{
//									
//									switch(dot.type) {
//									case  PEN_DOWN:type=0;break;
//									case  PEN_MOVE:type=1;break;
//									case  PEN_UP:type=2;break;
//									}
//									
//								}
//								Log.i("gx","x="+x+"-------y="+y+"------type="+type);
//								toolFun.drawSubFountainPen2(toolFun.bDrawl[0], 1, 0,
//										0, 6, x, y, dot.force, type);
//								dot.y = dot.y+128;
//								toolFun.ProcessDots(dot, toolFun.bDrawl[0]);
							} else {
//								toolFun.SetPenColor(1, toolFun.bDrawl[0]);
								toolFun.ProcessDots(dot, toolFun.bDrawl[0]);
							}
							// bleManager.setPenBeepMode(true);
							// bleManager.setPenBeepMode(false);
						}
					});
				}

				@Override
				public void onOfflineDataReceive(final Dot dot) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							toolFun.ProcessDots(dot, toolFun.bDrawl[0]);
						}
					});
				}

				@Override
				public void onFinishedOfflineDown(boolean success) {
					// Log.i(TAG, "---------onFinishedOfflineDown--------" +
					// success);
					/*
					 * layout.setVisibility(View.GONE); bar.setProgress(0);
					 */
				}

				@Override
				public void onOfflineDataNum(final int num) {
					// Log.i(TAG, "---------onOfflineDataNum1--------" + num);
					/*
					 * runOnUiThread(new Runnable() {
					 * 
					 * @Override public void run() { runOnUiThread(new
					 * Runnable() {
					 * 
					 * @Override public void run() {}
					 * 
					 * }); }
					 * 
					 * });
					 * 
					 * } });
					 */
				}

				@Override
				public void onReceiveOIDSize(int OIDSize) {
/*					Log.i("TEST1", "-----read OIDSize=====" + OIDSize);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							gCurPageID = -1;
						}
					});*/
				}

				@Override
				public void onReceiveOfflineProgress(final int i) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							/*
							 * if (startOffline) {
							 * 
							 * layout.setVisibility(View.VISIBLE);
							 * text.setText("开始缓存离线数据"); bar.setProgress(i);
							 * Log.e(TAG, "onReceiveOfflineProgress----" + i);
							 * if (i == 100) { layout.setVisibility(View.GONE);
							 * bar.setProgress(0); } } else {
							 * layout.setVisibility(View.GONE);
							 * bar.setProgress(0); }
							 */
						}

					});
				}

				@Override
				public void onDownloadOfflineProgress(final int i) {

				}

				@Override
				public void onReceivePenLED(final byte color) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Log.e(TAG, "receive led is " + color);
							switch (color) {
							case 1: // blue
								break;
							case 2: // green
								break;
							case 3: // cyan
								break;
							case 4: // red
								break;
							case 5: // magenta
								break;
							case 6: // yellow
								break;
							case 7: // white
								break;
							default:
								break;
							}
						}
					});
				}

				@Override
				public void onOfflineDataNumCmdResult(boolean success) {
					// Log.i(TAG, "onOfflineDataNumCmdResult---------->" +
					// success);
				}

				@Override
				public void onDownOfflineDataCmdResult(boolean success) {
					// Log.i(TAG, "onDownOfflineDataCmdResult---------->" +
					// success);
				}

				@Override
				public void onWriteCmdResult(int code) {
					// Log.i(TAG, "onWriteCmdResult---------->" + code);
				}

				@Override
				public void onReceivePenType(int type) {
					// Log.i(TAG, "onReceivePenType type---------->" + type);
					penType = type;
				}
			});
		}

		public void onServiceDisconnected(ComponentName classname) {
			showSound(R.raw.smartpemdisconnect);
			mService = null;
		}
	};
 	// private com.jinke.smartpen.DrawView[] bDrawl = new
	// com.jinke.smartpen.DrawView[2]; //add 2016-06-15 for draw

	private final static boolean isSaveLog = false; // 是否保存绘制数据到日志
	private final static String LOGPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/TQL/"; // 绘制数据保存目录

	/**
	 * zgm 变量for smartpen完
	 */
	/**
	 * zgm 变量for 非低功耗蓝牙
	 */
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 3000;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 3001;

	private com.jinke.smartpen.BluetoothChatService mChatService = null;
	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	private String mConnectedDeviceName = null;
	// private ArrayAdapter<String> mConversationArrayAdapter;
	private StringBuffer mOutStringBuffer;
	private BluetoothAdapter tableBluetoothAdapter;
public java.util.Timer timerInStartActivity=new java.util.Timer();
private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// final AlertDialog builder = new
			// AlertDialog.Builder(MainActivity.this).create();
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case com.jinke.smartpen.BluetoothChatService.STATE_CONNECTED:
					// setStatus(getString(R.string.title_connected_to,
					// mConnectedDeviceName));
					// mConversationArrayAdapter.clear();
					break;
				case com.jinke.smartpen.BluetoothChatService.STATE_CONNECTING:
					// setStatus(R.string.title_connecting);
					break;
				case com.jinke.smartpen.BluetoothChatService.STATE_LISTEN:
				case com.jinke.smartpen.BluetoothChatService.STATE_NONE:
					// setStatus(R.string.title_not_connected);
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
				// mConversationArrayAdapter.add("Me:  " + writeMessage);
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				@SuppressWarnings("unchecked")
				ArrayListMultimap<Long, Dot> drawsmartpenpoints = (ArrayListMultimap<Long, Dot>) com.jinke.smartpen.ObjAndByte
						.ByteToObject(readBuf);
				// drawsmartpenpoints(drawsmartpenpoints);
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				/*
				 * showSound(R.raw.in); showVibrator();// 震动
				 */Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	/**
	 * zgm 变量for 非低功耗蓝牙 结束
	 */
public static  ArrayList<Integer> tag = new ArrayList<Integer>();
	// 判题数据库
	public static SQLiteDatabase db;
	UploadMD5 upMd5=null;
	public static final int PAGE_CHANGE = -1;
	public static final int PAGE_DELETE = -2;
	public static Handler pageChangeHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == PAGE_CHANGE) {
				// caoheng 2015.11.24不让乱保存
				// c.view.saveDatebase();
				PAGENUM = msg.arg1;
				if (c.view.drawStatus != MyView.STATUS_DRAW_CURSOR) {
					Log.e("CalligraphyIndex", "pagenum:" + PAGENUM);
					Calligraph.mDrawStatusChangeBtn
							.setBackgroundResource(R.drawable.status_tuyasel);
					c.view.changeDrawState(MyView.STATUS_DRAW_CURSOR);
				}
			} else if (msg.what == PAGE_DELETE) {
				int deletePage = msg.arg1;
				Log.e("CalligraphyIndex", "delete:" + deletePage);
				CDBPersistent db = new CDBPersistent(context);
				db.open();
				db.deletePage(deletePage);
				db.close();

				if (deletePage <= getPageNum()) {
					Start.delPageNum();
				}
				resetTotalPagenum();

			}
			ImageLimit.instance().resetImageCount();
			WordLimit.getInstance().resetWordCount();
			bar.setVisibility(View.VISIBLE);
			barText.setText("正在载入");
			barText.setVisibility(View.VISIBLE);
			(new TT()).start();

		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.e("okkeydown", "onkeydown KEY_BACK"
				+ (keyCode == KeyEvent.KEYCODE_BACK));
		if (keyCode == 126 || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
			// 126 3.0新增的KEYCODE_MEDIA_PLAY按键。

			// Start.saveHandler.sendEmptyMessage(0);

			Start.status.modified("tuya");
			if (c.view.drawStatus == MyView.STATUS_DRAW_FREE) {
				c.view.changeStateAndSync(MyView.STATUS_DRAW_CURSOR);
				c.view.cursorBitmap.initDate(WolfTemplateUtil
						.getCurrentTemplate());
			} else {
				c.view.changeStateAndSync(MyView.STATUS_DRAW_FREE);
			}
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_1) {
			Log.e("KeyCode", "1 black");
			c.view.colorChanged(Color.BLACK);
		}
		if (keyCode == KeyEvent.KEYCODE_2) {
			Log.e("KeyCode", "2 red");
			c.view.colorChanged(Color.RED);
		}
		if (keyCode == KeyEvent.KEYCODE_0) {
			Log.e("KeyCode", "3 blue");
			c.view.colorChanged(Color.BLUE);
		}
		if (keyCode == KeyEvent.KEYCODE_3) {
			Log.e("KeyCode", "4 green");
			c.view.colorChanged(Color.GREEN);
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.e("KeyCode", "keycode back");
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public static Handler fileListHandler = new Handler() {
		FileListDialog fdialog;

		public void handleMessage(android.os.Message msg) {

			DownloadProgressActivity.barTextHandler
					.sendEmptyMessage(DownloadProgressActivity.KANBOX_START_FILELIST);

			ArrayList<String> list = (ArrayList<String>) msg.obj;
			Log.v("listlocal", list + "");
			// fdialog = new FileListDialog(context , list);

			Intent i = new Intent();
			Bundle b = new Bundle();
			b.putStringArrayList("dirList", list);
			i.putExtras(b);
			i.setClass(Start.context, MainTab.class);
			Start.context.startActivity(i);

		};
	};

	/*
	 * public static Handler saveHandler = new Handler() { public void
	 * handleMessage(android.os.Message msg) { // Toast.makeText(context,
	 * "自动保存", Toast.LENGTH_LONG).show(); // c.view.saveDatebase(); // new
	 * UploadCommand(context, null,false).execute();
	 * 
	 * 
	 * // EditableCalligraphy editable = null; // boolean needSaved = false; //
	 * for(int i=0;i<c.view.cursorBitmap.listEditableCalligraphy.size();i++){ //
	 * editable = c.view.cursorBitmap.listEditableCalligraphy.get(i); // for(int
	 * j=0;j<editable.charList.size();j++){ //
	 * if(editable.charList.get(j).getSaved()){ // needSaved = true; // } // }
	 * // } // Toast.makeText(context, "自动保存", Toast.LENGTH_LONG).show(); if
	 * (Start.status.isNeedSave()) { Toast.makeText(context, "自动保存",
	 * Toast.LENGTH_LONG).show(); // Start.status.resetStatus();
	 * 
	 * (new AutoSaveThread()).start(); }
	 * 
	 * }; };
	 */
	public static Handler kanboxUploadHandler = new Handler() {
		// 03-05 17:19:24.275: ERROR/AndroidRuntime(10544):
		// Caused by: java.lang.RuntimeException:
		// Can't create handler inside thread that has not called
		// Looper.prepare()

		public void handleMessage(android.os.Message msg) {
			if (msg.what == 2) {
				Log.e("autoupload", "auto upload !!!!!!!!!!");
				removeMessages(2);
				Log.e("autoupload", "auto upload delay "
						+ (long) (Start.auto_upload_time * 60 * 4)
						+ "!!!!!!!!!!");
				sendEmptyMessageDelayed(2,
						(long) (Start.auto_upload_time * 60 * 4));
				return;
			}

			if (msg.what == 1) {
				// 自动上传，检测有没有网络

				if (!checkNetworkInfo()) {
					Toast.makeText(Start.context, "当前没有网络，不进行上传",
							Toast.LENGTH_LONG).show();
					return;
				}

				Intent uploadintent = new Intent();
				uploadintent.putExtra("type",
						DownloadProgressActivity.AUTO_UPLOAD);
				uploadintent.setClass(Start.context,
						DownloadProgressActivity.class);
				Start.context.startActivity(uploadintent);
				Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>startActivity");
				// 等初始化控件完成之后，再开始上传，否则会报异常。
				return;
			}
			if (!checkNetworkInfo())
				dialogNet();

			redownload = 0;
			// caoheng 2015.11.24不让乱保存
			// c.view.saveDatebase();
			// barText.setVisibility(View.VISIBLE);
			// barText.setText("保存当前页，开始上传");
			// if(msg.what == 0)
			new UploadCommand(Start.context, new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					if (msg.what != -1)
						Toast.makeText(Start.context, "已经更新到服务器",
								Toast.LENGTH_LONG).show();
					else
						Toast.makeText(Start.context, "更新出现异常，请重试",
								Toast.LENGTH_LONG).show();

				}
			}, true).execute();
			// else
			// new BackupCommand(Start.context, backupHandler).execute();
			// Toast.makeText(Start.context, "开始上传", Toast.LENGTH_SHORT).show();
		};
	};
	public static Handler kanboxDownloadHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			checkNetworkInfo();
			redownload = 0;

			if (msg.what == -1) {
				// 下载具体文件

				// barText.setVisibility(View.VISIBLE);
				// barText.setText("开始从酷盘服务器下载");
				String dirName = (String) msg.obj;
				Log.e("Start",
						"---------------------------------------startDownload"
								+ dirName);
				new BackupCommand(Start.context, backupHandler,
						DownloadAllFileThread.OP_DOWNLOAD, dirName).execute();
			} else {
				// 获取文件列表
				// barText.setVisibility(View.VISIBLE);
				// barText.setText("开始从酷盘服务器下载");
				new BackupCommand(Start.context, backupHandler,
						DownloadAllFileThread.OP_GETLIST, "").execute();
			}

			// barText.setVisibility(View.VISIBLE);
			// barText.setText("开始从酷盘服务器下载");
			// new BackupCommand(Start.context, backupHandler).execute();
			// Toast.makeText(Start.context, "开始下载", Toast.LENGTH_SHORT).show();
		};
	};
	public  Handler transHandler = new Handler() { 
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 10:
				
				c.setTrans(msg.getData().getInt("situ"), msg.getData()
						.getFloat("yy"));				
				break;
			case 20:
				Toast.makeText(context, "请留言", Toast.LENGTH_SHORT).show();
				uploadTAG = tag.get(0);
				break;
			case 30:
				Toast.makeText(context, "播放", Toast.LENGTH_SHORT).show();
				break;
			case 40:
				c.recordTime.setVisibility(View.GONE);
				Toast.makeText(context, "录音结束", Toast.LENGTH_SHORT).show();
//				doUpLoadTask("/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3");
				//0507
				
//				reupLoadCounter=0;
//				  upMd5= new UploadMD5("/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3");
				 
				  //				upMd5.md5 = upMd5.md5sum("/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3");
				
//				new Thread(new Runnable() {
//					
//					@Override
//					public void run() {	
//						// TODO Auto-generated method stub
//						 upMd5.uploadFile("http://118.24.109.3/Public/smartpen/uploadmd5ver.php", "/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3",  upMd5.md5);
//						Log.i("md5","reuploadm="+upMd5.mresult);
//					}
//				}).start();
//				Log.i("md5","reupload5="+upMd5.md5);
/*				if (timerInStartActivity==null) {
					timerInStartActivity=new java.util.Timer();
				}	*/				
		new Thread(new Runnable() {
			@Override
			public void run() {
//				boolean isUploadDone=false;
				reupLoadCounter=0;				
				while(reupLoadCounter<4) {//检测次数
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Log.i("md5","count="+reupLoadCounter);
//							showSound(R.raw.reupload);
							upMd5= new UploadMD5("/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3");
							md5 = upMd5.md5sum("/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3");
							upMd5.uploadFile("http://118.24.109.3/Public/smartpen/uploadmd5ver.php", "/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3", md5);
						}
					}).start();	
			    	try {//等待上传3秒后判断
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					Log.i("md5","result="+upMd5.mresult);
					Log.i("md5","md5="+md5);
					md5 = upMd5.md5sum("/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3");
			    	if (upMd5.mresult.equals(md5)) {
			    		showSound(R.raw.upload_success_comment);	
						showToast("教师评语最小微课提交成功");		
//						deleteRecordFile("/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3");
//						isUploadDone=true;
						break;
					}else {
//						upLoadTask.timer.cancel();
						showSound(R.raw.reupload);
						if(upMd5==null||upMd5.con==null)
							return;
						upMd5.con.disconnect();
						try {
							if(upMd5.fis!=null)
							upMd5.fis.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						upMd5=null;
						System.gc();
//						isUploadDone=false;
						reupLoadCounter++;
/*						if (reupLoadCounter<4) {

						}else*/ 

					}
			    	
					
				}
			if(reupLoadCounter>3){
					showToast("最小微课录制失败，请重新录制");
					//删除存储的录音文件，重新录制0507
					deleteRecordFile("/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3");
					showSound(R.raw.upload_fail);
					reupLoadCounter=0;
					dealingSomeThing=false;
//					isUploadDone=true;
//				   this.cancel();
				}

			}}).start();		
				
				
				
/*				TimerTask task = new TimerTask() {  
				    @Override  
				    public void run() { 
						
//				    	if (upLoadTask.mresult.equals("The file "+srcPath.substring(srcPath.lastIndexOf("/")+1)+" has been uploaded")) {
//				    		showToast("云端存储文件成功");
		
						
					
				    	if (UploadMD5.mresult.equals(UploadMD5.md5)) {
				    		showSound(R.raw.upload_success_comment);
							showToast("教师评语最小微课提交成功");
				    		if (timerInStartActivity!=null) {
								timerInStartActivity.cancel();
								timerInStartActivity.purge();
								timerInStartActivity=null;	
								this.cancel();
							}

						}else {
//							upLoadTask.timer.cancel();
//							UploadMD5.con.disconnect();
							reupLoadCounter++;
							if (reupLoadCounter<4) {
								showSound(R.raw.reupload);
								Log.i("md5","count="+reupLoadCounter);
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										UploadMD5.uploadFile("http://118.24.109.3/Public/smartpen/uploadmd5ver.php", "/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3", UploadMD5.md5);
									}
								}).start();
								
								
							}else {

								showToast("最小微课录制失败，请重新录制");
								//删除存储的录音文件，重新录制0507
								deleteRecordFile("/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3");
								showSound(R.raw.upload_fail);
								reupLoadCounter=0;
								dealingSomeThing=false;
								timerInStartActivity.cancel();
								timerInStartActivity.purge();
								timerInStartActivity=null;
							   this.cancel();
							}
						}
				    }  
				};

				timerInStartActivity.schedule(task, 3000, 3000);*/
				dealingSomeThing=false;
				
				
				
				
				
				
				
				
				
				
				
				
				Log.i("tag0","final tag0="+uploadTAG);
/*				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Looper.prepare();
						boolean status2 = UpLoad.uploadFile("http://118.24.109.3/Public/smartpen/upload.php",
								"/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3");
						Log.i("tag0","1st upload tag0="+uploadTAG);
						
//						if (status2) {
//							showSound(R.raw.in);
//							Log.e("zgm","录音文件：001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+tag.get(0)+".mp3");
//							Log.e("zgm","录音文件2："+currentPageName);
//							 showToast("录音:001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+tag.get(0)+".mp3发送成功");
////							Toast.makeText(context, "录音:001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+tag.get(0)+".mp3发送成功", Toast.LENGTH_SHORT).show();
//						}else {
//							//录音失败的处理，0430，cahe
//							//删除掉录音，并要求重新录制
//							int i = 3;
//							while(!status2&&i>0) {
//								status2 = UpLoad.uploadFile("http://118.24.109.3/Public/smartpen/upload.php",
//										"/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+tag.get(0)+".mp3");
//								showToast("重传中，请稍后");
//								i--;
//							}
//							if(!status2) {deleteRecordFile("/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+tag.get(0)+".mp3");
//							showSound(R.raw.upload_fail);
//							
//							
//							 showToast("录音失败，请冲洗你录音");}
////							Toast.makeText(context, "录音发送失败", Toast.LENGTH_SHORT).show();	
//						}
						//重写上传逻辑
						int i=3;
						
						while(!status2&&i>0){
							Log.i("tag0",i+" upload tag0="+uploadTAG);
							status2 = UpLoad.uploadFile("http://118.24.109.3/Public/smartpen/upload.php",
									"/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3");
							showToast("重传失败");
							i--;
						}
						if(status2) {
							showSound(R.raw.in);
							
							showToast("录音:001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3发送成功");
							
						}
						else {
							Log.i("tag0","delete upload tag0="+uploadTAG);
							deleteRecordFile("/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3");
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							showSound(R.raw.upload_fail);
							showToast("录音文件错误，请重新录音");
						}
						
						
						
						
						
					}
				}
			
						).start();*/
				
				break;
			case 401://录音进行中
				//0507cahe暂时注释掉
//				if (c.recordTime.getVisibility()==View.GONE) {
//					c.recordTime.setVisibility(View.VISIBLE);
//					Toast.makeText(context, "录音进行中....", Toast.LENGTH_LONG).show();
//				}
//				int a=(Integer) msg.obj;
//				
//				c.recordTime.setText(a+"");
				break;
			case 402://文件上传成功
				if (timerInStartActivity!=null) {
					timerInStartActivity.cancel();
					timerInStartActivity.purge();
					timerInStartActivity=null;						
				}
				break;
			case 50://
				toolFun.bDrawl[0].setVisibility(View.GONE);
				c.setTrans(msg.getData().getInt("situ"), msg.getData()
						.getFloat("yy"));				
				//更换批改环评语
//				String commentString[][] = new String[15][5];
				Log.i("0425","pageID="+gCurPageID+".xml");
				commentString = PghComments.getPghString(gCurPageIDx+"-"+tag.get(0)+".xml");
//				Log.i("0425","commentString"+commentString[0][0]);
				if(commentString==null)return;
				System.arraycopy(commentString, 0,
						Calligraph.subContentString, 0,
						Calligraph.subContentString.length);
				Log.i("0425","subContentString"+commentString[0][0]);
				
				//重设二级按钮文字
				for(int i=0;i<5;i++) {
					Calligraph.subMenuBtnLK_array[i].setText(Calligraph.subContentString[0][i]);
					Calligraph.subMenuBtnLB_array[i].setText(Calligraph.subContentString[1][i]);
					Calligraph.subMenuBtnLC_array[i].setText(Calligraph.subContentString[2][i]);
					Calligraph.subMenuBtnLM_array[i].setText(Calligraph.subContentString[3][i]);
					Calligraph.subMenuBtnLT_array[i].setText(Calligraph.subContentString[4][i]);
					Calligraph.subMenuBtnBE_array[i].setText(Calligraph.subContentString[5][i]);
					Calligraph.subMenuBtnBI_array[i].setText(Calligraph.subContentString[6][i]);
					Calligraph.subMenuBtnBEx_array[i].setText(Calligraph.subContentString[7][i]);
					Calligraph.subMenuBtnBD_array[i].setText(Calligraph.subContentString[8][i]);
					Calligraph.subMenuBtnBM_array[i].setText(Calligraph.subContentString[9][i]);
					Calligraph.subMenuBtnRW_array[i].setText(Calligraph.subContentString[10][i]);
					Calligraph.subMenuBtnRE_array[i].setText(Calligraph.subContentString[11][i]);
					Calligraph.subMenuBtnRT_array[i].setText(Calligraph.subContentString[12][i]);
					Calligraph.subMenuBtnRU_array[i].setText(Calligraph.subContentString[13][i]);
					Calligraph.subMenuBtnRA_array[i].setText(Calligraph.subContentString[14][i]);
					
				}
				
				//加入批改环底图0425
				c.pBgImage.setPigaihuanBgImage(gCurPageIDx+"-"+tag.get(0));
				c.pBgImage.postInvalidate();
/*				c.setTrans(msg.getData().getInt("situ"), msg.getData()
						.getFloat("yy"))*/;
//				

				
				
				
//				try {
//					out = new BufferedWriter(new OutputStreamWriter(
//					         new FileOutputStream(dotfile)));
//					outtemp = new BufferedWriter(new OutputStreamWriter(
//					         new FileOutputStream("sdcard/-1/previous.txt")));
//				} catch (FileNotFoundException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//				}
				//显示批改环，先将student和teacher的container加入dots和pigai两个container  cahe0426
				for(Dot dot:studentDotsContainer.get(gCurPageID)) {
					if(dot.y>=tag.get(2)&&dot.y<=tag.get(3)) {
						pigaihuanDotsContainer.put(gCurPageID, dot);
//						try {
//							outtemp.write(dot.x+" "+dot.y+" "+dot.type+"\n");
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						  
					}
					
				}
				
				//
//				try {
//					outtemp.close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				
	
				try {
					 
					float d = 0;
				     
					
					if(pigaihuanDotsContainer.get(gCurPageID).get(0)!=null)
					{
						pigaihuanDotsContainer.get(gCurPageID).get(0).type=DotType.PEN_DOWN;
						
						for(int i =1;i<pigaihuanDotsContainer.get(gCurPageID).size();i++) 
					     {
					      d=getDistance(pigaihuanDotsContainer.get(gCurPageID).get(i-1).x,pigaihuanDotsContainer.get(gCurPageID).get(i).x,
					        pigaihuanDotsContainer.get(gCurPageID).get(i-1).y,pigaihuanDotsContainer.get(gCurPageID).get(i).y);
					      if(d>3) {
					       pigaihuanDotsContainer.get(gCurPageID).get(i).type=DotType.PEN_DOWN;
					       
					      }
					     
					     }
						
					
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
				
				
				
//				for(Dot dot:pigaihuanDotsContainer.get(gCurPageID)) {
//					
//						try {
//							out.write(dot.x+" "+dot.y+" "+dot.type+"\n");
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						
//						  
//					}
					
//				}
//				
//				
//				try {
//					out.close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				
				

//				Log.i("replay","replay0"+pigaihuanDotsContainer.get(gCurPageID).get(0).type);
//				Log.i("replay","replay1"+pigaihuanDotsContainer.get(gCurPageID).get(1).type);
				
//				for(int i=0;i<9;i++) {
//					Log.i("replay","replayX"+pigaihuanDotsContainer.get(gCurPageID).get(i).x);
//					Log.i("replay","replayY"+pigaihuanDotsContainer.get(gCurPageID).get(i).y);
//				}
				
	
				//统计答题时间
				if(!pigaihuanDotsContainer.isEmpty()) {
				timePerItem = pigaihuanDotsContainer.get(gCurPageID).get(pigaihuanDotsContainer.get(gCurPageID).size()-1).timelong-pigaihuanDotsContainer.get(gCurPageID).get(0).timelong;
				int minutetemp=(int) (timePerItem /1000/60);	
			    int secondtemp=(int) (timePerItem/1000)%60;
				c.totalTimeTv.setText("此题耗时"+minutetemp+"'"+secondtemp+"\""+"当前排名2名");
				}
				for(Dot dot:teacherDotsContainer.get(gCurPageID)) {
					if(dot.y>=tag.get(2)&&dot.y<=tag.get(3)) {
						pigaihuanPigaiContainer.put(gCurPageID, dot);
					}
				}
				
				//wsk 2019.6.21
				//解决批改环内教师笔迹飞
				try {
					float d = 0;
					if(pigaihuanPigaiContainer.get(gCurPageID).get(0)!=null)
					{
						pigaihuanPigaiContainer.get(gCurPageID).get(0).type=DotType.PEN_DOWN;
						
						for(int i =1;i<pigaihuanPigaiContainer.get(gCurPageID).size();i++) 
					     {
					      d=getDistance(pigaihuanPigaiContainer.get(gCurPageID).get(i-1).x,pigaihuanPigaiContainer.get(gCurPageID).get(i).x,
					    		  pigaihuanPigaiContainer.get(gCurPageID).get(i-1).y,pigaihuanPigaiContainer.get(gCurPageID).get(i).y);
					      if(d>3) {
					    	  pigaihuanPigaiContainer.get(gCurPageID).get(i).type=DotType.PEN_DOWN;
					       
					      }
					     
					     }
						
					
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
				
				
				
				c.setTrans(msg.getData().getInt("situ"), msg.getData()
						.getFloat("yy"));
	
				doDrawFromContainer(pigaihuanDotsContainer,toolFun.bDrawl[1]);//先加载学生笔迹
				doDrawFromContainer(pigaihuanPigaiContainer,toolFun.bDrawl[1]);//加载教师笔迹
				
				
				System.gc();
				
				
				break;
			case 51://批改环消失
				toolFun.bDrawl[0].setVisibility(View.VISIBLE);
				c.setTrans(msg.getData().getInt("situ"), msg.getData()
						.getFloat("yy"));
//				doDrawFromContainer(pigaihuanPigaiContainer,toolFun.bDrawl[0]);//只加载教师笔迹
				doDrawFromContainer(teacherDotsContainer,toolFun.bDrawl[0]);//所有教师笔迹都重新加载一遍0426
				
				//清空批改环的两个container的数据
				toolFun.bDrawl[1].canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);
				pigaihuanDotsContainer.clear();
				pigaihuanPigaiContainer.clear();
				
				//显示点选的批语
				c.commentsTv[tag.get(4)].setTextColor(Color.RED);
				c.commentsTv[tag.get(4)].setTextSize(20);
				c.commentsTv[tag.get(4)].setText(c.comments);
				c.commentsTv[tag.get(4)].setVisibility(View.VISIBLE);
				break;
			case 52://在批改环中回放笔迹
				dealingSomeThing=true;
				toolFun.bIsReply =true;
				toolFun.bDrawl[1].canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
				toolFun.bDrawl[1].postInvalidate();
				synchronized(toolFun.bDrawl[1]){
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						toolFun.RunReplay(pigaihuanDotsContainer,toolFun.bDrawl[1]);
						toolFun.bDrawl[1].paint.setColor(Color.RED);
						
					}
					
				});
				}
//				doDrawFromContainer(pigaihuanPigaiContainer,toolFun.bDrawl[1]);//加载教师笔迹
				Log.i("toolFun",""+toolFun. bIsReply);
				toolFun.bDrawl[1].invalidate();
				
				
				break;
			case 430:
				drawfromContainer(pigaihuanPigaiContainer,toolFun.bDrawl[1]);
				break;
			case 0326:
				c.sumText.setVisibility(View.VISIBLE);
				c.sumText.setTextSize(23);
				c.sumText.setTextColor(Color.RED);
				c.sumText.setText("总分:"+c.sum);
				c.sum = 100;
				break;
			case 431:
				String fileNameT = gCurBookID+"-"+gCurPageID+"-1.page";
				Log.i("name","fileNameT="+fileNameT);				
				doDrawFromFile1(fileNameT,toolFun.bDrawl[0]);
				break;
			default:
				break;
			}
		}
	};
	
	public Handler download4Penhandler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==99) {
				showDownLoadDialog();
			}
		};
	};
	
	

	public static Handler saveToDatebaseHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			// c.view.saveDatebase();
			EditableCalligraphy editable = null;
			boolean needSaved = false;
			for (int i = 0; i < c.view.cursorBitmap.listEditableCalligraphy
					.size(); i++) {
				editable = c.view.cursorBitmap.listEditableCalligraphy.get(i);
				for (int j = 0; j < editable.charList.size(); j++) {
					if (editable.charList.get(j).getSaved()) {
						needSaved = true;
					}
				}
			}
			if (Start.status.isNeedSave()) {
				Toast.makeText(context, "自动保存", Toast.LENGTH_LONG).show();
				Start.status.resetStatus();
				(new AutoSaveThread()).start();
			}
			sendEmptyMessageDelayed(1, autoSaveTime * 700000);
		};
	};

	public static final int KANBOX_CHECK_AUTHOR = 1;
	public static final int KANBOX_CHECK_TOKEN = 2;

	public static final int KANBOX_START_FILELIST = 19;
	public static final int KANBOX_START_DOWNLOAD = 3;
	public static final int KANBOX_START_UPLOAD = 4;
	public static final int KANBOX_START_UPLOAD_PAGE = 5;

	public static final int KANBOX_GET_FILELIST = 6;

	public static final int KANBOX_END_DBDOWNLOAD = 7;
	public static final int KANBOX_END_MKDIR = 8;
	public static final int KANBOX_END_UPLOAD_PAGE = 9;
	public static final int KANBOX_END_UPLOAD = 10;

	public static final int KANBOX_FINISH_UPLOAD = 11;
	public static final int KANBOX_FINISH_REFRESHTOKEN = 12;
	public static final int KANBOX_FINISH_DOWNLOAD = 13;
	public static final int START_FINISH = 14;

	public static final int KANBOX_ERROR = 15;
	public static final int KANBOX_ERROR_DOWNLOAD = 16;
	public static final int KANBOX_ERROR_UPLOAD = 17;
	public static final int KANBOX_ERROR_REFRESHTOKEN = 18;

	private static List<DownloadEntity> downloadList = new ArrayList<DownloadEntity>();
	private static int MAX_REDOWNLOAD = 3;
	private static int MAX_LINES = 30;
	private static int redownload = 0;
	private static int lines = 0;

	public static boolean addDownloadEnty(DownloadEntity newEnty) {
		// for(DownloadEntity enty : downloadList){
		// if(enty.getPath().equals(newEnty.getPath()))
		// return false;
		// }
		downloadList.add(newEnty);
		return true;
	}

	public static void clearDownloadList() {
		downloadList.clear();
	}

	public static List<DownloadEntity> getDownloadList() {
		synchronized (downloadList) {
			return downloadList;
		}

	}

	public static Handler barTextHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			// Toast.makeText(context, "自动保存", Toast.LENGTH_LONG).show();
			// c.view.saveDatebase();
			switch (msg.what) {
			case KANBOX_GET_FILELIST:
				lines++;
				// barText.setText(barText.getText() + "\n" +
				// "获取文件列表成功，开始下载具体文件");
				break;
			case KANBOX_START_DOWNLOAD:
			case KANBOX_END_DBDOWNLOAD:
			case KANBOX_START_UPLOAD:
			case KANBOX_END_UPLOAD_PAGE:
			case KANBOX_END_UPLOAD:
			case KANBOX_END_MKDIR:
			case KANBOX_START_UPLOAD_PAGE:
			case KANBOX_ERROR:
				lines++;
				if (lines > MAX_LINES) {
					// barText.setText((String)msg.obj);
					lines = 0;
				} else
					// barText.setText(barText.getText() + "\n" +
					// (String)msg.obj);
					break;
			case START_FINISH:

				// CDBPersistent db = new CDBPersistent(context);
				// db.open();
				// int template_byPage =
				// db.getTemplateByPage(Start.getPageNum());
				//
				// c.view.doChangeBackground(WolfTemplateUtil
				// .getTypeByID(template_byPage));
				// db.close();
				//
				// for (int i = 0; i <
				// CursorDrawBitmap.listEditableCalligraphy.size(); i++) {
				// CursorDrawBitmap.listEditableCalligraphy.get(i)
				// .initDatabaseCharList();
				// }
				for (int i = 0; i < CursorDrawBitmap.listEditableCalligraphy
						.size(); i++) {
					CursorDrawBitmap.listEditableCalligraphy.get(i)
							.initFlipBlock();
				}
				c.view.cursorBitmap.updateHandwriteState();
				Log.v("flipper",
						"                      START_FINISH updateHandwriteState!! ");
				c.view.freeBitmap.resetFreeBitmapList();
				bar.setVisibility(View.GONE);
				barText.setVisibility(View.GONE);
				break;
			case KANBOX_FINISH_DOWNLOAD:
				lines = 0;
				Toast.makeText(context, "恢复全部完成", Toast.LENGTH_LONG).show();
				barText.setVisibility(View.INVISIBLE);
				CalligraphyDB.getInstance(Start.context).resetDB();
				Log.e("download",
						"clear data after download -----------------------------------");
				c.view.cursorBitmap.clearDataBitmap();

				Log.e("create", "------initParsedWordList Start Handler");
				CalligraphyVectorUtil.initParsedWordList(Start.getPageNum());

				for (int i = 0; i < CursorDrawBitmap.listEditableCalligraphy
						.size(); i++) {
					CursorDrawBitmap.listEditableCalligraphy.get(i)
							.initDatabaseCharList();
				}
				c.view.cursorBitmap.updateHandwriteState();
				break;
			case KANBOX_FINISH_UPLOAD:
				lines = 0;
				Toast.makeText(context, "上传全部完成", Toast.LENGTH_LONG).show();
				// barText.setVisibility(View.INVISIBLE);
				break;
			case KANBOX_ERROR_DOWNLOAD:
				lines++;
				// barText.setText(barText.getText() + "\n" + (String)msg.obj);

				for (DownloadEntity enty : downloadList) {
					// barText.setText(barText.getText() + "\n" + enty.getPath()
					// +"需要重新传输");
				}

				if (redownload < MAX_REDOWNLOAD) {
					redownload++;
					lines++;
					// barText.setText(barText.getText() + "\n" + "重新启动失败的任务");

					new BackupCommand(Start.context, backupHandler).execute();
				} else {
					lines++;
					// barText.setText(barText.getText() + "\n" +
					// "失败"+MAX_REDOWNLOAD+"次，请稍后重试恢复功能");
				}

				break;
			case KANBOX_ERROR_UPLOAD:
				// if(barText.getVisibility() == View.INVISIBLE)
				// barText.setVisibility(View.VISIBLE);

				lines++;
				// barText.setText(barText.getText() + "\n" + (String)msg.obj);

				for (DownloadEntity enty : downloadList) {
					// barText.setText(barText.getText() + "\n" + enty.getPath()
					// +"需要重新上传");
				}

				if (redownload < MAX_REDOWNLOAD) {
					redownload++;
					lines++;
					// barText.setText(barText.getText() + "\n" + "重新启动失败的任务");

					new UploadCommand(Start.context, new Handler() {
						@Override
						public void handleMessage(Message msg) {
							// TODO Auto-generated method stub
							if (msg.what != -1)
								Toast.makeText(Start.context, "已经更新到服务器",
										Toast.LENGTH_LONG).show();
							else
								Toast.makeText(Start.context, "更新出现异常，请重试",
										Toast.LENGTH_LONG).show();

						}
					}, true).execute();
				} else {
					lines++;
					// barText.setText(barText.getText() + "\n" +
					// "失败"+MAX_REDOWNLOAD+"次，请稍后重试酷盘功能");
				}

				break;
			case 0327:
				Toast.makeText(context, "提交完成", Toast.LENGTH_LONG).show();
				
				// barText.setVisibility(View.INVISIBLE);
				break;
			default:
				break;
			}

		};
	};
	

	public static Matrix m;

	public void dismissButtom() {
		if (android.os.Build.VERSION.SDK_INT >= 11)
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork() // or
																		// .detectAll()
																		// for
																		// all
																		// detectable
																		// problems
				.penaltyLog().build());
		// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		// .detectLeakedSqlLiteObjects()
		// .detectLeakedClosableObjects()
		// .penaltyLog()
		// .penaltyDeath()
		// .build());

		/**
		 * zgm for smartpen
		 */

		fontFace = Typeface.createFromAsset(getResources().getAssets(),"fonts/sxzt.ttf");
	
		
		Intent gattServiceIntent = new Intent(this, BluetoothLEService.class);
		boolean bBind = bindService(gattServiceIntent, mServiceConnection,
				BIND_AUTO_CREATE);

		toolFun.bDrawl[0] = new DrawView(this);
		toolFun.bDrawl[1] = new DrawView(this);
		toolFun.bDrawl[0].setVcolor(Color.YELLOW);
//		toolFun.bDrawl[1] = new DrawView(this);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mWidth = dm.widthPixels;
		mHeight = dm.heightPixels;

		float density = dm.density; // 屏幕密度（0.75 / 1.0 / 1.5）
		int densityDpi = dm.densityDpi; // 屏幕密度dpi（120 / 160 / 240）
		Log.e(TAG, "density=======>" + density + ",densityDpi=======>"
				+ densityDpi);
		// 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
		int screenWidth = (int) (mWidth / density); // 屏幕宽度(dp)
		int screenHeight = (int) (mHeight / density);// 屏幕高度(dp)
		Log.e(TAG, "width=======>" + screenWidth);
		Log.e(TAG, "height=======>" + screenHeight);

		Log.e(TAG, "-----screen pixel-----width:" + mWidth + ",height:"
				+ mHeight);
		toolFun.setScreensize(mWidth, mHeight);
		toolFun.dealSmartPenGesture.setDealSmartPenGesture(this);
		/*
		 * zgm for smart pen20181108
		 */
		float ratio = 0.95f;
		// float ratio =1;
		ratio = (ratio * mWidth) / toolFun.BG_REAL_WIDTH;
		toolFun.BG_WIDTH = (int) (toolFun.BG_REAL_WIDTH * ratio);
		toolFun.BG_HEIGHT = (int) (toolFun.BG_REAL_HEIGHT * ratio);

		toolFun.gcontentLeft = getWindow().findViewById(
				Window.ID_ANDROID_CONTENT).getLeft();
		toolFun.gcontentTop = getWindow().findViewById(
				Window.ID_ANDROID_CONTENT).getTop();

		// toolFun.A5_X_OFFSET = (int) (mWidth - toolFun.gcontentLeft -
		// toolFun.BG_WIDTH) / 2;
		// toolFun.A5_Y_OFFSET = (int) (mHeight - toolFun.gcontentTop -
		// toolFun.BG_HEIGHT) / 2;
		toolFun.A5_X_OFFSET = 20;
		toolFun.A5_Y_OFFSET = 20;

		/**
		 * zgm for smart pen20181108 完
		 * 
		 */
		/**
		 * zgm for非低功耗蓝牙开始
		 * 
		 * 
		 */
		tableBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();// 非低功耗蓝牙适配器

		// If the adapter is null, then Bluetooth is not supported
		if (mChatService == null)
			setupChat();
		if (tableBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		/**
		 * zgm for 非低功耗蓝牙 结束
		 * 
		 */
		/**
		 * 获得输入的IP从sharedpreference中 start
		 */
		SharedPreferences preference = getSharedPreferences("inputIp",
				MODE_PRIVATE);
		// inputIp = preference.getString("Ip", "");
		if (inputIp == null) {
			Toast.makeText(Start.this, "获取不到IP", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(Start.this, "获取的IP" + inputIp, Toast.LENGTH_SHORT)
					.show();
		}

		/**
		 * 获得输入的IP从sharedpreference中 end
		 */

		try {

			File checkdb = new File("/sdcard/homework.db");
			if (!checkdb.exists()) {
				db = DatabaseOp.createDatabase();
				// Log.i("sqldb","create database");
				DatabaseOp.createTable(db);
				// Log.i("sqldb","create table");
				DatabaseOp.initDb(db);
				// Log.i("sqldb","init table");

			} else {
				Log.i("sqldb", "exists");
				db = openOrCreateDatabase("/sdcard/homework.db", 0, null);
				// 打开程序清空database cahe 719
//				DatabaseOp.clcDatabase(db);
				Log.i("sqldb", "open");
			}

			if (instance == null) {
				Log.e("Start", "!!!!!!!!!!!!!onCreate");
				dismissButtom();
				parameterDialog = new ParametersDialog(this);
				Intent intent = new Intent(
						"com.jinke.rloginservice.IReadingsLoginService");
				if (bindService(intent, isLoginConn,
						Start.context.BIND_AUTO_CREATE)) {
					// Toast.makeText(CMain.this,
					// "bindService() Success",Toast.LENGTH_LONG).show();
				} else {

				}
				chackDevice();

				// 恢复之前保存的Timer参数值(如果有的话)
				SharedPreferences settings = this.getSharedPreferences(
						ParametersDialog.FILENAME,
						android.content.Context.MODE_PRIVATE);
				int progress = settings.getInt(
						ParametersDialog.PARAM_AUTO_UPLOAD_TIME, -1);
				if (progress != -1) {
					auto_upload_time = ParametersDialog.minAutoUploadTime
							+ progress * ParametersDialog.autoUploadTimeFactor;
				}

				Log.e("state",
						"onCreate ----------"
								+ CalligraphyBackupUtil.getSimID());
				// DisplayMetrics dm = new DisplayMetrics();
				// getWindowManager().getDefaultDisplay().getMetrics(dm);
				SCREEN_WIDTH = dm.widthPixels;
				SCREEN_HEIGHT = dm.heightPixels;

				float f = getResources().getDisplayMetrics().density;
				density = f;

				context = this;
				instance = this;
				status = new EditStatus();
				LoginInfo = getSharedPreferences("LoginInfo",
						MODE_WORLD_WRITEABLE);
				autoSaveTime = LoginInfo.getInt("autosavetime", 2);

				// saveToDatebaseHandler.sendEmptyMessageDelayed(1,autoSaveTime
				// * 700000);

				if (OOM_BITMAP == null)
					OOM_BITMAP = readBitMap(context, R.drawable.oom);
				if (EMPTY_BITMAP == null)
					EMPTY_BITMAP = readBitMap(context, R.drawable.empty_word);
				// BUTTOM_LINE_BITMAP = readBitMap(context,
				// R.drawable.buttomline);
				if (RED_ARROW_BITMAP == null)
					RED_ARROW_BITMAP = readBitMap(context,
							R.drawable.red_jiantou);
				if (BLACK_ARROW_BITMAP == null)
					BLACK_ARROW_BITMAP = readBitMap(context,
							R.drawable.black_jiantou);

				Intent alarmIntent = getIntent();
				int alarmPagenum = -1;
				if (alarmIntent != null) {
					alarmPagenum = alarmIntent.getIntExtra("pagenum", -1);
				}
				PAGENUM = 0;

				// ly：可优化，减少一次Matrix对象的创建
				m = CDBPersistent.getMatrix(LoginInfo.getString("matrix",
						new Matrix().toString()));
				// ly：这个地方有什么用？
				float[] v = new float[9];
				m.getValues(v);

				c = new Calligraph(this);
				setContentView(c);
				c.setActivity(this);
				c.view.setMMMatirx(m);
				Log.e("changematrix", "change Matrix Start");
				c.view.setMMMatirx(m);
				LinearLayout statusLayout = new LinearLayout(Start.this);
				statusLayout.setOrientation(LinearLayout.VERTICAL);
				LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.CENTER_IN_PARENT);
				bar = new ProgressBar(Start.this);
				bar.setVisibility(View.VISIBLE);
				statusLayout.addView(bar, lp);

				barText = new TextView(Start.this);
				barText.setText("正在加载");
				barText.setVisibility(View.GONE);
				barText.setTextColor(Color.BLACK);
				barText.setTextSize(20);
				barText.setVerticalScrollBarEnabled(true);
				statusLayout.addView(barText, lp);

				c.addView(statusLayout, lp);

				// c.view.setFreeDrawBitmap();

				c.view.setFocusable(true);
				c.view.setSelected(true);
				Log.e("state", "isFocused:" + c.view.isFocused());
				c.view.requestFocus();
				Log.e("state", "isFocused:" + c.view.isFocused());

				if (alarmPagenum == -1) {
					PAGENUM = LoginInfo.getInt("pagenum", 1);
					Log.e("Start", "not alarm start ! alarmPagenum:"
							+ alarmPagenum);
				} else {

					PAGENUM = alarmPagenum;
					Log.e("Start", "alarm start ! alarmPagenum:" + alarmPagenum);
				}

				barText.setText("正在载入");
				barText.setVisibility(View.VISIBLE);
				c.view.cursorBitmap.updateHandwriteState();
				Log.v("startinit", "start TT thread:" + PAGENUM
						+ "set visiable");
				(new TT()).start();
				// 设置进入页面为批改模式cahe
				c.view.drawStatus = c.view.STATUS_DRAW_FREE;
				c.view.changePenState(c.view.STATUS_PEN_CALLI);
				c.view.changeStateAndSync(c.view.STATUS_DRAW_FREE);
			} else {
				Log.e("Start", "!!!!!!!!!!!!! not onCreate");
				ViewGroup vg = (ViewGroup) c.getParent();
				vg.removeView(c);
				setContentView(c);

			}

			// saveBitmapUri =
			// getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new
			// ContentValues());
			// imageFileOS =
			// getContentResolver().openOutputStream(saveBitmapUri);
			//
			c.view.setOnTouchListener(this);
			c.view.setFocusable(true);

			mGestureDetector = new GestureDetector(this, this);
			mGestureDetector.setIsLongpressEnabled(true);

			//
			// File checkdb = new File("/sdcard/homework.db");
			// if(!checkdb.exists()){
			// db = DatabaseOp.createDatabase();
			// // Log.i("sqldb","create database");
			// DatabaseOp.createTable(db);
			// // Log.i("sqldb","create table");
			// DatabaseOp.initDb(db);
			// // Log.i("sqldb","init table");
			// //
			// // db.openOrCreateDatabase("/sdcard/homework.db", null);
			// // Log.i("sqldb", "open");
			// //
			// // Cursor c =
			// db.rawQuery("select count(*) from sqlite_master where type='table';",
			// null);
			// // Log.i("sqldb", "c");
			// // while(c.moveToNext()){
			// // Log.i("sqldb", c.getString(0));
			// // }
			// } else {
			// Log.i("sqldb", "exists");
			// db = openOrCreateDatabase("/sdcard/homework.db",0 ,null);
			// Log.i("sqldb", "open");
			// // db.close();
			// // Log.i("sqldb", "close");
			// // Cursor c = db.query("usertable", null, null, null, null, null,
			// null);
			// // Log.i("sqldb", "d");
			// // Cursor cursor =
			// db.rawQuery("select * from table where qNo=0",null);
			// // Log.i("sqldb", "c");
			// // cursor.getString(2);
			// // Log.i("sqldb", "1 right"+"    "+cursor.getString(2));
			// // int result[] = DatabaseOp.readDatabase(db, 1);
			// // Log.i("sqldb", "1 right " + result[0]);

			/*
			 * zgm for smartpen
			 */

			// RelativeLayout layout = new RelativeLayout(this);
			// ImageView mImageView=new ImageView(getBaseContext());
			// RelativeLayout mreLayout=new RelativeLayout(this);
			RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			RelativeLayout.LayoutParams parampgh =new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			param.width = (int) mWidth;
			param.height = (int) mHeight;
			param.rightMargin = 1;
			param.bottomMargin = 1;
			
			parampgh.width = (int) mWidth-400;
			parampgh.height = (int) 900;
			parampgh.topMargin= 900;
			parampgh.leftMargin = 200;
			parampgh.rightMargin = 200;
			parampgh.bottomMargin = 400;
			// mreLayout.setBackgroundColor(Color.WHITE);
			// mreLayout.addView(toolFun.bDrawl[0], param);
			// c.addView(mreLayout, param);//
			c.addView(toolFun.bDrawl[0], param);
//			toolFun.bDrawl[1].setBackgroundColor(Color.GREEN);
			c.pigaihuanLayout.addView(toolFun.bDrawl[1],parampgh);
			toolFun.drawInit(toolFun.bDrawl[0]);
			toolFun.drawInit(toolFun.bDrawl[1]);
			/*
			 * zgm for smartpen
			 */
			// }

		} catch (Exception e) {
			e.printStackTrace();

		}

//		showDownLoadDialog(); // 2017.6.7
//		inPutIPDialog();

	}

	static class TT extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			c.view.cursorBitmap.clearDataBitmap();
			// Log.e("create", "------TT Start");
			// CalligraphyVectorUtil.initParsedWordList(Start.getPageNum());

			// 暂时不考虑换页切换模板的问题
			// CDBPersistent db = new CDBPersistent(context);
			// db.open();
			// int template_byPage = db.getTemplateByPage(Start.getPageNum());
			// c.view.doChangeBackground(WolfTemplateUtil
			// .getTypeByID(template_byPage));
			// db.close();
			ScaleSave.getInstance().newPage();
			// caoheng 1101不知道为嘛
			// for (int i = 0; i <
			// CursorDrawBitmap.listEditableCalligraphy.size(); i++) {
			// CursorDrawBitmap.listEditableCalligraphy.get(i)
			// .initDatabaseCharList();v
			// }
			barTextHandler.sendEmptyMessage(START_FINISH);
		}

	}

	private static void dialogNet() {

		builder = new Builder(Start.context);
		builder.setMessage("当前wifi尚未打开，请点击 \"设置\" 选择网络。");
		// builder.setMessage(getString(R.string.Dialog_Login_Msg).toString());
		// builder.setTitle(getString(R.string.Dialog_Login_Title).toString());
		builder.setTitle("网络链接");
		// builder.setNegativeButton(getString(R.string.Button_exit).toString(),
		// new DialogInterface.OnClickListener() {
		builder.setNegativeButton("下次再说",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		// builder.setPositiveButton(getString(R.string.Button_ok).toString(),
		// new DialogInterface.OnClickListener() {
		builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				c.closeADHoc();
				Start.context.startActivity(new Intent(
						Settings.ACTION_WIRELESS_SETTINGS));// 进入无线网络配置界面
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	private static boolean checkNetworkInfo() {
		ConnectivityManager conMan = (ConnectivityManager) Start.context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// mobile 3G Data Network
		// ly
		// State mobile =
		// conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

		// wifi
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		// Toast.makeText(main.this,
		// "3G:"+mobile.toString()+"\nwifi:"+wifi.toString(),
		// Toast.LENGTH_SHORT).show(); //显示3G网络连接状态
		if (wifi != State.CONNECTED) {
			// dialogNet();
			return false;

		} else {
			// Toast.makeText(this, "网络连接成功", Toast.LENGTH_SHORT).show();
			return true;
		}
	}

	// 点击Menu时，系统调用当前Activity的onCreateOptionsMenu方法，并传一个实现了一个Menu接口的menu对象供你使用
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * CDBPersistent db = new CDBPersistent(this); db.open(); //
		 * totlePageNum =
		 * db.getTotalPageNumByTemplateID(c.view.mTemplate.getId());
		 * totlePageNum = db.getTotalPageNum(); db.close();
		 */
		// resetTotalPagenum();只在程序进入，保存，删除时重置应该就可以了。
		Log.e("databases", "pre totlePageNum:" + totlePageNum);
		/*
		 * add()方法的四个参数，依次是： 1、组别，如果不分组的话就写Menu.NONE,
		 * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单 3、顺序，那个菜单现在在前面由这个参数的大小决定
		 * 4、文本，菜单的显示文本
		 */

		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "前一页de").setIcon(
				android.R.drawable.ic_menu_set_as);
		// setIcon()方法为菜单设置图标，这里使用的是系统自带的图标，同学们留意一下,以
		// android.R开头的资源是系统提供的，我们自己提供的资源是以R开头的
		Log.e("databases", "create totlePageNum:" + totlePageNum);
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, PAGENUM + "/" + totlePageNum)
				.setIcon(android.R.drawable.ic_menu_help);

		menu.add(Menu.NONE, Menu.FIRST + 3, 3, "后一页").setIcon(
				android.R.drawable.ic_menu_help);

		menu.add(Menu.NONE, Menu.FIRST + 4, 4, "更新到服务器").setIcon(
				android.R.drawable.ic_menu_add);
		menu.add(Menu.NONE, Menu.FIRST + 5, 5, "同步服务器数据到本地").setIcon(
				android.R.drawable.ic_menu_info_details);
		menu.add(Menu.NONE, Menu.FIRST + 6, 6, "添加日程").setIcon(
				android.R.drawable.ic_menu_send);

		// menu.add(Menu.NONE, Menu.FIRST + 7, 7, "生成pdf到本地").setIcon(
		// android.R.drawable.ic_menu_send);
		// menu.add(Menu.NONE, Menu.FIRST + 8, 8, "制作添加翰林算子").setIcon(
		// android.R.drawable.ic_menu_send);

		// menu.add(Menu.NONE, Menu.FIRST + 7, 7, "改变刷新限制,当前:" +
		// limit_num).setIcon(
		// android.R.drawable.ic_menu_send);
		menu.add(Menu.NONE, Menu.FIRST + 7, 7, "扫描智能笔");
		menu.add(Menu.NONE, Menu.FIRST + 8, 8, "加载作业");
		menu.add(Menu.NONE, Menu.FIRST + 9, 9, "保存提交");
		menu.add(Menu.NONE, Menu.FIRST+ 10, 10,  "更新作业信息");
		// return true才会起作用
		return true;

	}

	// 菜单项被选择事件
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:

			if (delPageNum()) {
				// bar.setVisibility(View.VISIBLE);
				// barText.setText("正在载入");
				// barText.setVisibility(View.VISIBLE);
				// (new TT()).start();
				pageChangeHandler.sendEmptyMessage(0);

				// Log.e("pre", PAGENUM + " " + totlePageNum);
				// // 按page换模板
				//
				// CDBPersistent db = new CDBPersistent(this);
				// db.open();
				// int template_byPage =
				// db.getTemplateByPage(Start.getPageNum());
				// c.view.doChangeBackground(WolfTemplateUtil
				// .getTypeByID(template_byPage));
				// db.close();
				//
				// CalligraphyVectorUtil.initParsedWordList(Start.getPageNum());
				//
				// for (int i = 0; i < CursorDrawBitmap.listEditableCalligraphy
				// .size(); i++) {
				// CursorDrawBitmap.listEditableCalligraphy.get(i)
				// .initDatabaseCharList();
				// }
			} else {
				Toast.makeText(this, "最前一页", Toast.LENGTH_LONG).show();
			}
			// c.view.cursorBitmap.updateHandwriteState();
			// // c.view.setFreeDrawBitmap();
			// c.view.freeBitmap.resetFreeBitmapList();
			break;
		case Menu.FIRST + 2:

			// CalligraphyDB.getInstance(Start.context).getCurrentWordCount(5,
			// 3);
			// BitmapToFile.savaBitmap();

			break;
		case Menu.FIRST + 3:

			if (addPageNum()) {

				// 按page换模板

				// bar.setVisibility(View.VISIBLE);
				// barText.setText("正在载入");
				// barText.setVisibility(View.VISIBLE);
				// (new TT()).start();
				pageChangeHandler.sendEmptyMessage(0);
			} else
				Toast.makeText(this, "最后一页", Toast.LENGTH_LONG).show();

			break;
		case Menu.FIRST + 4:

			try {
				if (loginService.isLogin()) {
					// login
					uploadToKanbox();
				} else {
					// error
					netUploadLogin = true;
					dialog("离线状态无法使用，请先登录您的Readings帐号");
					// RequestBookActivity.this.finish();
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Intent uploadintent = new Intent();
			// uploadintent.putExtra("type", DownloadProgressActivity.UPLOAD);
			// uploadintent.setClass(Start.context,
			// DownloadProgressActivity.class);
			// Start.context.startActivity(uploadintent);
			//
			// kanboxUploadHandler.sendEmptyMessage(0);

			break;
		case Menu.FIRST + 5:

			try {
				if (loginService.isLogin()) {
					// login
					downloadFromKanbox();
				} else {
					// error
					netDownloadLogin = true;
					dialog("离线状态无法使用，请先登录您的Readings帐号");
					// RequestBookActivity.this.finish();
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Intent intent = new Intent();
			// intent.setClass(Start.context, DownloadProgressActivity.class);
			// intent.putExtra("type", DownloadProgressActivity.DOWNLOAD);
			// Start.context.startActivity(intent);
			//
			// //点击事件不属于UI主线程，没有事件循环，在其中创建AsyncTask会出错
			// kanboxDownloadHandler.sendEmptyMessage(0);

			break;
		case Menu.FIRST + 6:

			// String picName = "/extsd/calldir/bigmap_"+getPageNum()+".png";
			// c.view.saveFile(c.view.baseBitmap.bitmap, picName,"PNG");
			//
			// FTPUtil.upload(picName);
			// Toast.makeText(this, "上传", Toast.LENGTH_LONG).show();

			// FTPUtil.downloadLocalCalldir();

			// new FtpCommand().execute();

			showDialog(DATETIMESELECTOR_ID);

			break;

		// case Menu.FIRST + 7:
		//
		// Intent pdfIntent = new Intent();
		// pdfIntent.setClass(this, CloudActivity.class);
		// startActivity(pdfIntent);
		// break;
		// case Menu.FIRST + 8:
		// Intent mIntent = new Intent();
		// ComponentName comp = new ComponentName(
		// "com.studio.mindo",
		// "com.studio.mindo.MindoTestActivity");
		// mIntent.setComponent(comp);
		// mIntent.setAction("android.intent.action.VIEW");
		// startActivityForResult(mIntent, Properyt.MINDMAP_REQUEST_CODE);
		// break;

		/*
		 * zgm for smart pen
		 */
		case Menu.FIRST + 7:
			serverIntent = new Intent(this,
					com.jinke.smartpen.SelectDeviceActivity.class);
			startActivityForResult(serverIntent, REQUEST_SELECT_DEVICE);

			break;
		case Menu.FIRST + 8:
			/*
			 * serverIntent = new Intent(this,
			 * com.jinke.smartpen.DeviceListActivity.class);
			 * startActivityForResult(serverIntent,
			 * REQUEST_CONNECT_DEVICE_SECURE); setupChat(); break;
			 */
//			toolFun.SetPenColor(6, toolFun.bDrawl[0]);
		new Thread(new Runnable() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				drawsmartpenpoints(getfromFile("hha"), toolFun.bDrawl[0]);				
			}
		}).start();
		long startTime=System.currentTimeMillis();
		while(System.currentTimeMillis()-startTime<1000) {
			
		}
		toolFun.SetPenColor(1, toolFun.bDrawl[0]);
			// toolFun.SetPenColor(1);
			break;
		case Menu.FIRST + 9:
			/*
			 * serverIntent = new Intent(this,
			 * com.jinke.smartpen.DeviceListActivity.class);
			 * startActivityForResult(serverIntent,
			 * REQUEST_CONNECT_DEVICE_SECURE); setupChat(); break;
			 */
			SmartPenUnitils.save(toolFun.smartPenPage,"123-1.page");
			// final String ip="192.168.1.113";
			final String ip = "123.206.16.114";
			boolean status = false;
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					/*
					 * boolean status=UpLoad.uploadFile("http://"+ip+
					 * "/index.php/Home/Index/upload","/sdcard/-1/" +
					 * "123-1.page");
					 */
					dealingSomeThing=true;
					boolean status = UpLoad.uploadFile("http://" + ip
							+ "/jxyv1/index.php/Home/Index/smart_pen_upload",
							"/sdcard/-1/" + "123-1.page");
					dealingSomeThing=false;
					if (status) {
						Message message = new Message();
						message.what = 0327;
						barTextHandler.sendMessage(message);
					}
				}
			}).start();
			break;
		case Menu.FIRST + 10:
			showDownLoadDialog();
		}

		return false;
	}

	// 选项菜单被关闭事件，菜单被关闭有三种情形，menu按钮被再次点击、back按钮被点击或者用户选择了某一个菜单项
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// Toast.makeText(this, "选项菜单关闭了", Toast.LENGTH_LONG).show();
		// (new LocalCopyThread()).start();
		// getWindow().getDecorView().setSystemUiVisibility
		// (View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	// 菜单被显示之前的事件
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		/*
		 * CDBPersistent db = new CDBPersistent(this); db.open(); //
		 * totlePageNum =
		 * db.getTotalPageNumByTemplateID(c.view.mTemplate.getId());
		 * totlePageNum = db.getTotalPageNum(); db.close(); 改为先获取总页数，然后直接调用
		 */
		dismissButtom();
		resetTotalPagenum();

		Log.e("pre", "prepare " + PAGENUM);
		menu.getItem(Menu.FIRST).setTitle(PAGENUM + "/" + totlePageNum);
		return true;
	}

	private void uploadToKanbox() {
		Intent uploadintent = new Intent();
		uploadintent.putExtra("type", DownloadProgressActivity.UPLOAD);
		uploadintent.setClass(Start.context, DownloadProgressActivity.class);
		Start.context.startActivity(uploadintent);

		kanboxUploadHandler.sendEmptyMessage(0);
	}

	private void downloadFromKanbox() {
		Intent intent = new Intent();
		intent.setClass(Start.context, DownloadProgressActivity.class);
		intent.putExtra("type", DownloadProgressActivity.DOWNLOAD);
		Start.context.startActivity(intent);

		// 点击事件不属于UI主线程，没有事件循环，在其中创建AsyncTask会出错
		kanboxDownloadHandler.sendEmptyMessage(0);
	}

	public static int getPageNum() {
		return PAGENUM;
	}

	public static boolean addPageNum() {
		if (PAGENUM + 1 <= totlePageNum) {
			// caoheng 2015.11.24不让乱保存
			// c.view.saveDatebase();
			PAGENUM++;
			return true;
		} else {
			return false;
		}

	}

	public static boolean delPageNum() {
		Log.e("pre", "deletepagenum : " + PAGENUM);
		if (PAGENUM - 1 >= 1) {
			Log.e("pre", "deletepagenum : PAGENUM--" + PAGENUM);
			// caoheng 2015.11.24不让乱保存
			// c.view.saveDatebase();
			PAGENUM--;
			Log.e("pre", "见过" + PAGENUM);
			return true;
		} else {
			return false;
		}

	}

	public static String getDate() {
		if (TextUtils.isEmpty(date))
			date = CDBPersistent.getCurrent();

		return date;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e("adhoc", "start call onDestroy");
		// if(!paused)
		// c.view.saveDatebase();

		Log.e("databases", "before destory -------------:");
		int page = totlePageNum;
		if (PAGENUM > totlePageNum)
			page = totlePageNum;
		else
			page = PAGENUM;

		LoginInfo
				.edit()
				.putInt("templateid",
						WolfTemplateUtil.getCurrentTemplate().getId())
				.putInt("pagenum", PAGENUM)
				.putString("templatetype",
						WolfTemplateUtil.getCurrentTemplate().getName())
				.putString("matrix", c.view.getMMMatrix().toString())
				.putInt("autosavetime", autoSaveTime).commit();
		// caoheng 2015.11.24不让乱保存
		// c.view.saveDatebase();

		ScaleSave.getInstance().close();
		Log.e("adhoc", "start call closeAdhoc");
		c.closeADHoc();
		c.destroy();

		c.view.cursorBitmap.clearDataBitmap();
		// unbindService(isLoginConn);

		Log.e("Start", "sleep");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("Start", "killed");
		android.os.Process.killProcess(android.os.Process.myPid());

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();

		Log.e("finish", "finish");
	}

	public static void resetTotalPagenum() {
		CDBPersistent db = new CDBPersistent(context);
		db.open();
		totlePageNum = db.getTotalPageNum();
		db.close();
		Log.e("db", "resetTotalPagenum close");
	}

	public static void resetDate() {
		date = "";
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.e("Start", "!!!!!!!!!!!!!onstart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.e("Start", "!!!!!!!!!!!!!stop");
		super.onStop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		Log.e("Start", "!!!!!!!!!!!!!pause");
		// if(!Calligraph.wifiandadhocPause){
		// c.closeADHoc();
		// c.view.saveDatebase();
		// }
		Calligraph.wifiandadhocPause = false;
		paused = true;
		super.onPause();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		Log.e("changed", "configuration changed !!!!!!!!!!!  do nothing");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.e("Start", "!!!!!!!!!!!!!resume");
		super.onResume();
		dismissButtom();

		// LoginInfo = getSharedPreferences("LoginInfo", MODE_WORLD_WRITEABLE);
		// Boolean isUpdate = LoginInfo.getBoolean("update", false);
		// LoginInfo.edit().putBoolean("update", false);
		// String code = "";
		// if(isUpdate){
		// //是由Kanbox授权页面返回的，读取code，进一步获取授权码，并上传
		// code = LoginInfo.getString("code", "");
		// if("".equals(code)){
		// //授权过程出错，code为空
		// }else{
		// //上传
		// Log.e("content", "code:" + code
		// +"\n 继续");
		// }
		//
		// }

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.e("Start", "!!!!!!!!!!!!!restart");
		super.onRestart();

		if (netDownloadLogin || netUploadLogin) {
			try {
				if (loginService.isLogin()) {
					// login
					UserInfo userInfo = loginService.getUserInfo();
					if (userInfo != null)
						username = userInfo.getUsername();

					if (netUploadLogin) {
						uploadToKanbox();
						netUploadLogin = false;
					}
					if (netDownloadLogin) {
						downloadFromKanbox();
						netDownloadLogin = false;
					}
				} else {
					Toast.makeText(context, "没有登录，离线状态", Toast.LENGTH_LONG)
							.show();
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Intent intent = new Intent(
		// "com.jinke.rloginservice.IReadingsLoginService");
		// if (bindService(intent, isLoginConn, Start.context.BIND_AUTO_CREATE))
		// {
		// // Toast.makeText(CMain.this,
		// // "bindService() Success",Toast.LENGTH_LONG).show();
		// } else {
		//
		// }

	}

	private DateSlider.OnDateSetListener mDateTimeSetListener = new DateSlider.OnDateSetListener() {
		public void onDateSet(DateSlider view, Calendar selectedDate) {
			// update the dateText view with the corresponding date
			int minute = selectedDate.get(Calendar.MINUTE)
					/ TimeLabeler.MINUTEINTERVAL * TimeLabeler.MINUTEINTERVAL;
			// dateText.setText(String.format("The chosen date and time:%n%te. %tB %tY%n%tH:%02d",
			// selectedDate, selectedDate, selectedDate, selectedDate, minute));
			// 28. 12月 2011 20：30

			selectedDate.set(Calendar.MINUTE, minute);

			String date = String.format("%tY/%tm/%te", selectedDate,
					selectedDate, selectedDate);
			String datetime = String.format("%tH:%02d", selectedDate, minute);

			// Toast.makeText(Start.this, "选择的日期：	"+
			// String.format("%tY年%tm月%te日	 %tH:%02d",
			// selectedDate, selectedDate, selectedDate,selectedDate, minute)
			// , Toast.LENGTH_LONG).show();

			c.view.cursorBitmap.insertAlarmItem(date, datetime);

			GoogleCalendarUtil util = new GoogleCalendarUtil(Start.this);
			boolean flag = util.addToGoogleCalendar("翰林-云记事", "翰林-云记事-第 "
					+ PAGENUM + " 份需要您的关注！", selectedDate);

			if (!flag) {
				Toast.makeText(Start.this, "该设备尚未绑定google帐号，请您先登录google账户",
						Toast.LENGTH_LONG).show();
				Log.e("alarm", "do not add alarm!");
				// return;
			}

			Log.e("alarm", "add alarm!");
			// 获得AlarmManager实例
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

			// 实例化Intent
			Intent intent = new Intent();
			// 设置Intent action属性
			intent.setAction(BC_ACTION);

			intent.putExtra("pagenum", PAGENUM);
			intent.putExtra("msg", "翰林-云记事，第 " + PAGENUM + " 份内容需要您关注");
			// 实例化PendingIntent
			PendingIntent pi = PendingIntent.getBroadcast(Start.this, 0,
					intent, 0);
			// 获得系统时间
			long time = System.currentTimeMillis() + 10 * 1000;

			time = selectedDate.getTimeInMillis();

			// am.setRepeating(AlarmManager.RTC_WAKEUP, time,8
			// * 1000, pi);

			am.set(AlarmManager.RTC_WAKEUP, time, pi);

		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		// this method is called after invoking 'showDialog' for the first time
		// here we initiate the corresponding DateSlideSelector and return the
		// dialog to its caller

		final Calendar c = Calendar.getInstance();
		switch (id) {
		case DATETIMESELECTOR_ID:
			return new DateTimeSlider(this, mDateTimeSetListener, c, c);
		}
		return null;
	}

	public static Bitmap createScaledBitmap(Bitmap src, int w, int h) {
		// RuntimeException: Canvas: trying to use a recycled bitmap
		// android.graphics.Bitmap@2ac8bd20

		// float bitmapW = src.getWidth();
		// float bitmapH = src.getHeight();
		float wc = src.getWidth() / (float) w;
		float hc = src.getHeight() / (float) h;
		if (wc > hc)
			wc = hc;
		Log.i("BitmapScale", "Width " + (int) (src.getWidth() / wc)
				+ " height  " + (int) (src.getHeight() / wc));
		Bitmap tmp = null;
		try {
			tmp = Bitmap.createScaledBitmap(src, (int) (src.getWidth() / wc),
					(int) (src.getHeight() / wc), true);
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			Log.e("ispic", "Start createScaledBitmap OOM");
			// ERROR/dalvikvm-heap(20557): external allocation too large f
			// 捕获不到异常
		}
		// return Bitmap.createScaledBitmap(src, (int)(src.getWidth()/wc),
		// (int)(src.getHeight()/wc), true);
		return tmp;
	}

	// ly

	public static void rotaingImageView(int angle) {
		// 旋转图片 动作
		Bitmap bitmap = BitmapFactory.decodeFile(TempImgFilePath);
		Matrix matrix = new Matrix();
		;
		matrix.postRotate(angle);
		// System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		try {
			File myFile = new File(TempImgUpPath);
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(myFile));
			resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return resizedBitmap;
	}

	/*
	 * 查看图片旋转角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return degree;
	}

	// 作业上传服务器
	// private String server = "http://192.168.1.109/upload.aspx";
	private String server = Config.UPLOAD_HOMEWORK;

	/**
	 * 2013-12-23 ly 用于作业批改系统，当拍照成功后上传到服务器
	 * 
	 * @param file
	 *            :文件名
	 */
	private void uploadImage(String file) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";

		Looper.prepare();

		try {
			URL url = new URL(server);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();

			// 设置传输流大小，此方法用于在预先不知道内容长度时启用没有进行内部缓冲的HTTP请求正文的流
			httpURLConnection.setChunkedStreamingMode(128 * 1024);
			// 允许输入输出流
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);

			// 使用POST
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd-HH-mm-ss");

			DataOutputStream dos = new DataOutputStream(
					httpURLConnection.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + end);
			String str = (dateFormat.format(date))
					+ file.substring(file.lastIndexOf("."));
			dos.writeBytes("Content-Disposition: form-data; name=\"file1\"; filename=\""
					// + file.substring(file.lastIndexOf("/") + 1)
					+ (dateFormat.format(date))
					+ file.substring(file.lastIndexOf(".")) + "\"" + end);
			dos.writeBytes(end);

			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[8192];// 8K
			int count = 0;
			// 读取文件
			while ((count = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
			}
			fis.close();
			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String result = br.readLine();

			Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT)
					.show();

			Log.e("!!!!", result);

			dos.close();
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT)
					.show();
		}
		Looper.loop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// 智能笔设备选择处理用switch
		Log.w("zgm", "想干嘛？");
		switch (requestCode) {
		case REQUEST_SELECT_DEVICE:
			Log.w("zgm", "要链接了");
			// When the DeviceListActivity return, with the selected device
			// address
			if (resultCode == Activity.RESULT_OK && data != null) {
				String deviceAddress = data
						.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.w("zgm", "连接是否成功 ：" + deviceAddress);
				// Log.w("zgm", "mService ："+mService.toString());
				try {
					boolean flag = mService.connect(deviceAddress);
					Log.w("zgm", "连接是否成功" + flag);
					if (flag) {
						showSound(R.raw.smartconnected);
					}
					// TODO spp
					// bleManager.setSppConnect(deviceAddress);
				} catch (Exception e) {
					Log.i("zgm", "connect-----" + e.toString());
				}
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(this, "Bluetooth has turned on ",
						Toast.LENGTH_SHORT).show();
			} else {
				// User did not enable Bluetooth or an error occurred
				Toast.makeText(this, "Problem in BT Turning ON ",
						Toast.LENGTH_SHORT).show();
				finish();
			}
			break;
		case REQUEST_CONNECT_DEVICE_SECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {// 结果码
				connectDevice(data, true);
			}
			break;
		case GET_FILEPATH_SUCCESS_CODE:
			if (resultCode == Activity.RESULT_OK) {
				String path = "";
				Uri uri = data.getData();
				/*
				 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { String
				 * pathFromURI = null; try { pathFromURI =
				 * getRealPathFromURI(uri); } catch (Exception e) {
				 * e.printStackTrace(); } String[] split =
				 * pathFromURI.split("/"); path = split[split.length - 1]; }
				 * else if (Build.VERSION.SDK_INT ==
				 * Build.VERSION_CODES.LOLLIPOP_MR1) { String uriPath =
				 * uri.getPath(); String[] split = uriPath.split("/"); path =
				 * split[split.length - 1]; }
				 */
				final String str = path;
				// Log.i(TAG, "onActivityResult: path="+str);
				new Thread(new Runnable() {
					@Override
					public void run() {
						bleManager.readTestData(str);
					}
				}).start();
			}
			break;
		default:
			Log.e("zgm", "not smartpen");

			/*
			 * Toast.makeText(context, "back:" + resultCode, Toast.LENGTH_SHORT)
			 * .show();
			 */
			c.view.cursorBitmap.picFlag = false;
			Log.i("caoheng", "resultCode: " + resultCode + " requestCode: "
					+ requestCode);

			// //添加涂鸦背景，caoheng，10.25
			// if(resultCode == -1 && requestCode == 0) {
			// Log.i("caoheng", "onActivityResult1");
			// //Uri imageFileUri =
			// getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new
			// ContentValues());
			// Uri imageFileUri = data.getData();
			// Log.i("uri_imagefileuri",imageFileUri.toString());
			// Log.e("addmorepic", "uri = " + imageFileUri);
			// picList.add(imageFileUri);
			// picListIndex = 0;
			//
			// Cursor cursor =
			// getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
			// null, null, null, null);
			// //cursor.moveToFirst();
			//
			// String targetPath = "/storage/emulated/0/mypic";
			// String path;
			// Uri picUri;
			// int index;
			// String name;
			// picCursor.clear();
			// picName.clear();
			// picList.clear();
			//
			// //caoheng 11.07， 获取在targetPath目录下的所有图片Uri
			// for(cursor.moveToFirst(); !cursor.isAfterLast();
			// cursor.moveToNext())
			// {
			// //Log.e("addmorepic", "inside for");
			// path = cursor.getString(1);
			// path = path.substring(0, 25);
			// Log.i("uri_path",path);
			// Log.e("addmorepic", path);
			// Log.e("addmorepic","path"+cursor.getString(1));
			// Log.e("addmorepic", "1" + cursor.getString(0));
			// Log.e("addmorepic", "2"+ cursor.getString(1));
			// Log.e("addmorepic", "3" +cursor.getString(2));
			// Log.e("addmorepic", "4" +cursor.getString(3)); //////file name,
			// e.g. 1.jpg
			// Log.e("addmorepic", "5" +cursor.getString(4));
			// Log.e("addmorepic", "6" +cursor.getString(5));
			//
			// if(imageFileUri.toString().contains(path));
			// {
			// //Log.e("addmorepic", "found it, path = " + cursor.getString(1));
			// //Log.e("addmorepic", "name = " + cursor.getString(0));
			// index = cursor.getColumnIndex(Images.ImageColumns._ID);
			// index = cursor.getInt(index);
			// name = cursor.getString(3);
			// Log.e("addmorepic", "index = " + index);
			// picUri = Uri.parse("content://media/external/images/media/" +
			// index);
			// if(picUri.equals(imageFileUri))
			// {
			// picName.add(name);
			// picCursor.add(cursor);
			// }
			// }
			// }
			//
			// for(cursor.moveToFirst(); !cursor.isAfterLast();
			// cursor.moveToNext())
			// {
			// //Log.e("addmorepic", "inside for");
			// path = cursor.getString(1);
			// path = path.substring(0, 25);
			// //Log.e("addmorepic", path);
			// Log.i("uri_path",path);
			// if(imageFileUri.toString().contains(path));
			// {
			// //Log.e("addmorepic", "found it, path = " + cursor.getString(1));
			// //Log.e("addmorepic", "name = " + cursor.getString(0));
			// index = cursor.getColumnIndex(Images.ImageColumns._ID);
			// index = cursor.getInt(index);
			// name = cursor.getString(3);
			// Log.e("addmorepic", "index = " + index);
			// picUri = Uri.parse("content://media/external/images/media/" +
			// index);
			// if(!picUri.equals(imageFileUri))
			// {
			// picList.add(picUri);
			// picCursor.add(cursor);
			// picName.add(name);
			// }
			// }
			// }
			//
			// cursor.close();
			// 添加涂鸦背景，caoheng，11.11
			// 添加涂鸦背景，caoheng，10.25
			if (resultCode == -1 && requestCode == 0) {
				Log.i("caoheng", "onActivityResult1");
				// Uri imageFileUri =
				// getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new
				// ContentValues());
				Uri imageFileUri = data.getData();
				Log.e("addmorepic", "uri = " + imageFileUri);
				Cursor chooseCursor = managedQuery(imageFileUri, null, null,
						null, null);
				chooseCursor.moveToLast();
				String choosePath = chooseCursor.getString(1);
				String chooseName = chooseCursor.getString(3);
				int choosePathLength = choosePath.length();
				int chooseNameLength = chooseName.length();

				String targetPath = choosePath.substring(0, choosePathLength
						- chooseNameLength);
				Log.e("addmorepic", "targetPath = " + targetPath);

				Cursor cursor = getContentResolver().query(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
						null, null, MediaStore.Images.Media.DATE_MODIFIED);
				// cursor.moveToFirst();

				String path;
				Uri picUri;
				int index;
				String name;
				picList.clear();
				picCursor.clear();
				picName.clear();

				picListIndex = 0;
				picCursorIndex = 0;
				Log.e("zgm", "123————onActivityResult");
				picNameIndex = 0;

				// caoheng 11.11， 获取在targetPath目录下的所有图片Uri
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					// Log.e("addmorepic", "inside for");
					path = cursor.getString(1);
					path = path.substring(0, choosePathLength
							- chooseNameLength);
					Log.e("addmorepic", path);
					Log.e("addhomework", "1 " + cursor.getString(0));
					Log.e("addhomework", "2 " + cursor.getString(1));
					Log.e("addhomework", "3 " + cursor.getString(2));
					Log.e("addhomework", "4 " + cursor.getString(3)); // ////file
																		// name,
																		// e.g.
																		// 1.jpg
					Log.e("addhomework", "5 " + cursor.getString(4));
					Log.e("addhomework", "6 " + cursor.getString(5));

					if (path.equalsIgnoreCase(targetPath)) {
						// Log.e("addmorepic", "found it, path = " +
						// cursor.getString(1));
						// Log.e("addmorepic", "name = " + cursor.getString(0));
						index = cursor.getColumnIndex(Images.ImageColumns._ID);
						index = cursor.getInt(index);
						name = cursor.getString(3);
						Log.e("addmorepic", "index = " + index);
						picUri = Uri
								.parse("content://media/external/images/media/"
										+ index);
						if (picUri.equals(imageFileUri)) {

							picList.add(picUri);
							picCursor.add(cursor);
							picName.add(name);

						}
					}
				}

				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					// Log.e("addmorepic", "inside for");
					path = cursor.getString(1);
					path = path.substring(0, choosePathLength
							- chooseNameLength);
					// Log.e("addmorepic", path);

					if (path.equalsIgnoreCase(targetPath)) {
						// Log.e("addmorepic", "found it, path = " +
						// cursor.getString(1));
						// Log.e("addmorepic", "name = " + cursor.getString(0));
						index = cursor.getColumnIndex(Images.ImageColumns._ID);
						index = cursor.getInt(index);
						name = cursor.getString(3);
						Log.e("addmorepic", "index = " + index);
						picUri = Uri
								.parse("content://media/external/images/media/"
										+ index);
						if (!picUri.equals(imageFileUri)) {

							picList.add(picUri);
							picCursor.add(cursor);
							picName.add(name);

						}
					}
				}

				cursor.close();

				// Uri testUri;
				// for(int i=0; i<picList.size(); i++)
				// {
				// testUri = picList.get(i);
				// Log.e("addmorepic", "uri = " + testUri);
				// }
				//

				BitmapFactory.Options bmFactoryOptions = new BitmapFactory.Options();
				try {
					Log.i("caoheng", "change freeBitmap");
					backgroundBitmap = BitmapFactory.decodeStream(
							getContentResolver().openInputStream(imageFileUri),
							null, bmFactoryOptions);
					Bitmap bg = Bitmap.createScaledBitmap(backgroundBitmap,
							1600, 2560, true);
					c.view.freeBitmap.resetFreeBitmapList();
					c.view.freeBitmap.addBgPic(bg);
					c.view.changeStateAndSync(0);
					// c.view.freeBitmap.drawFreeBitmapSync();
					// c.view.addFreeBg(backgroundBitmap);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 相机返回图片
			if (resultCode == RESULT_OK && requestCode == AddCameraRequest) {
				Log.i("caoheng", "1");

				// Toast.makeText(getApplicationContext(), TempImgFilePath,
				// Toast.LENGTH_SHORT).show();

				// 直接上传
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// 为了使用Toast，这个地方加上事件循环

						int degree = readPictureDegree(TempImgFilePath);
						rotaingImageView(degree);
						uploadImage(TempImgUpPath);
					}

				}).start();

				// Uri uri = Uri.fromFile(new File(TempImgFilePath));
				// // 拷到资源文件夹
				// uri = c.view.savePicBitmapRandom(uri);
				//
				// ContentResolver cr = this.getContentResolver();
				// try {
				// Bitmap bitmap;
				//
				// try {
				// BitmapFactory.Options options = new BitmapFactory.Options();
				// options.inJustDecodeBounds = true;
				// bitmap = BitmapFactory.decodeStream(
				// cr.openInputStream(uri), new Rect(-1, -1, -1, -1),
				// options); // 此时返回bm为空
				// BitmapCount.getInstance().createBitmap("Start AddCameraRequest decodeStream");
				// options.inJustDecodeBounds = false;
				// // 缩放比
				// int be = 1;
				// if (options.outHeight > 300 || options.outWidth > 300) {
				// be = options.outHeight / 300;
				// int t = options.outWidth / 300;
				// if (be < t)
				// be = t;
				// }
				// options.inSampleSize = be;
				//
				// bitmap = BitmapFactory.decodeStream(
				// cr.openInputStream(uri), new Rect(-1, -1, -1, -1),
				// options);
				// BitmapCount.getInstance().createBitmap("Start AddCameraRequest decodeStream");
				//
				// } catch (OutOfMemoryError o) {
				//
				// // TODO: handle exception
				// Log.e("addpic", "decode file failed ");
				// bitmap = Start.OOM_BITMAP;
				// }
				// Bitmap myBitmap;
				//
				// if (bitmap.getWidth() < 300 && bitmap.getHeight() < 300) {
				// myBitmap = bitmap;
				// } else {
				// try {
				//
				// myBitmap = createScaledBitmap(bitmap, 280, 280);
				// } catch (OutOfMemoryError o) {
				// // TODO: handle exception
				// Log.e("addpic", "scale bitmap failed ");
				// myBitmap = Start.OOM_BITMAP;
				// }
				// if(myBitmap != Start.OOM_BITMAP){
				// bitmap.recycle();
				// BitmapCount.getInstance().recycleBitmap("Start onActivityResult bitmap");
				// }
				// }
				//
				// c.view.cursorBitmap.insertImageBitmap(myBitmap, uri);
				//
				// } catch (FileNotFoundException e) {
				//
				// Log.e("Exception", e.getMessage(), e);
				// }

				// 添加本地图片
			} else if (resultCode == 0 && requestCode == AddCameraRequest) {
				Log.i("caoheng", "2");
				Log.v("renkai", "wifi");
				netStatus = WIFI;

				startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
				c.startTransCameraPic();

			} else if (resultCode == 1 && requestCode == AddCameraRequest) {
				Log.i("caoheng", "3");
				Log.v("renkai", "adhoc");
				netStatus = ADHOC;
				c.startTransCameraPic();

			}

			else if (resultCode == RESULT_OK
					&& requestCode == AddPictureRequest) {
				Log.i("caoheng", "4");
				Uri uri = data.getData();
				if (uri == null) {
					Toast.makeText(this, "图片不存在或格式不识别", Toast.LENGTH_LONG)
							.show();
				} else {
					Log.e("addpic",
							"before savePicBitmap uri::" + uri.toString()
									+ " path:" + uri.getPath());
					// 拷到资源文件夹
					uri = c.view.savePicBitmap(uri, null);
					if (uri == null) {
						Toast.makeText(this, "图片不存在或格式不识别", Toast.LENGTH_LONG)
								.show();
					} else {
						Log.e("addpic", "after save:" + uri.toString());
						Log.e("addpic", "path" + uri.getPath());
						createAndSavePic(uri);
					}
				}
			} else if (requestCode == AddVideoRequest
					|| requestCode == AddAudioRequest) {
				Log.i("caoheng", "5");
				if (resultCode == 0) {
					netStatus = WIFI;
					Log.v("renkai", "wifi");
				} else if (resultCode == 1) {
					netStatus = ADHOC;
					Log.v("renkai", "adhoc");
				}
				c.startTransCameraPic();
			} else if (requestCode == 10) {
				c.restartGetIP();
			} else if (requestCode == Properyt.MINDMAP_REQUEST_CODE) {
				Log.i("caoheng", "6");
				if (resultCode == RESULT_OK) {
					Log.e("mindmap", "uri:" + data.getData().toString());
					// 拷到资源文件夹
					Uri uri = data.getData();
					uri = c.view.savePicBitmap(uri, null);
					createAndSavePic(uri);
				} else {
					Log.e("mindmap", "mind map result error");
				}
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void chackDevice() {
		Log.e("environment", "data dir:"
				+ Environment.getDataDirectory().getAbsolutePath());

		// storagePath = Environment.getExternalStorageDirectory()
		// .getAbsolutePath();
		storagePath = "/mnt/sdcard";

		Log.e("environment", "root dir:"
				+ Environment.getRootDirectory().getAbsolutePath());
	}

	public static String getStoragePath() {
		return storagePath;
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		// opt.inPreferredConfig = Bitmap.Config.ARGB_4444;
		opt.inPreferredConfig = Bitmap.Config.ARGB_4444;// 这句话使图片压缩，节省内存
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		BitmapCount.getInstance().createBitmap("Start readBitMap decodeStream");
		return BitmapFactory.decodeStream(is, null, opt);
	}

	private static String TAG = "Start";
	private IReadingsLoginService loginService;
	public static String username = "003399";// 设置默认用户名，测试没有登录readings的手机时使用
	private ServiceConnection isLoginConn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			Log.v(TAG, "onServiceDisconnected() called");
			loginService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Log.v(TAG, "onServiceConnected() called");
			loginService = IReadingsLoginService.Stub.asInterface(service);
			try {
				if (loginService.isLogin()) {
					// login
					UserInfo userInfo = loginService.getUserInfo();
					if (userInfo != null)
						username = userInfo.getUsername();

					// username = "jinketest";
					Log.e(TAG, "username:" + username);

					// usernameTextView.setText(username_sp);
				} else {
					// error
					dialog("请先登录您的Readings帐号");
					// RequestBookActivity.this.finish();
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};


	private void dialog(String title) {

		builder = new Builder(Start.this);
		builder.setMessage("您尚未登录");
		builder.setTitle(title);
		builder.setNegativeButton("离线使用",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.setPositiveButton("登录", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					loginService.loginActivity();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				dialog.dismiss();
			}
		});
		// builder.setOnKeyListener(new OnKeyListener() {
		//
		// @Override
		// public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent
		// event) {
		// // TODO Auto-generated method stub
		// if(keyCode == KeyEvent.KEYCODE_BACK){
		// dialog.dismiss();
		// Start.this.finish();
		// }
		// return false;
		// }
		// });

		builder.create().show();
	}

	public static int createActualPixels(float f) {
		return (int) (f * density + 0.5f);
	}

	public static Handler backupHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			/*
			 * CalligraphyVectorUtil.initParsedWordList(Start.getPageNum());
			 * 
			 * for (int i = 0; i < CursorDrawBitmap.listEditableCalligraphy
			 * .size(); i++) { CursorDrawBitmap.listEditableCalligraphy.get(i)
			 * .initDatabaseCharList(); }
			 * c.view.cursorBitmap.updateHandwriteState();
			 */

			// c.view.setFreeDrawBitmap();
			// Toast.makeText(Start.this, "同步服务器数据到本地，覆盖本地数据",
			// Toast.LENGTH_LONG).show();

			// bar.setVisibility(View.GONE);
			// barText.setVisibility(View.GONE);
			Log.v("flipper",
					"                      backupHandler updateHandwriteState!! ");
			c.view.cursorBitmap.updateHandwriteState();

		}
	};

	public static void createAndSavePic(Uri uri) {
		// Bitmap myBitmap = BitmapUtils.getBitmapFromUri(uri);
		Bitmap myBitmap = BitmapUtils.getInstance().getBitmapFromUri(uri);
		/*
		 * 已经存到本地了，不用再存uri，只需要保存文件名
		 */
		c.view.cursorBitmap.insertImageBitmap(myBitmap, uri);

	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	// 11.29caoheng gesture开启
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		// if(c.GESTURE_MODE == c.GESTURE_MODE_OFF){
		// Log.i("slide", "gesture mode off");
		// return false;
		// } else {
		// Log.i("slide", "gesture mode on");
		// //return true;
		// return mGestureDetector.onTouchEvent(arg1);
		// }
		return false;

	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		if (arg2 < 0) {
			Log.i("slide", "next page");
			c.view.addNextPic();
		}
		if (arg2 > 0) {
			Log.i("slide", "previous page");
			c.view.addPreviousPic();
		}
		return true;
		// return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @author zgm
	 * @time: 20170630
	 * @describe:输入将要下载或上传的服务器的IP地址 @ node: start
	 */
	private void inPutIPDialog() {
		/*
		 * @setView 装入inPutIPDialog的输入框的的view ==> R.layout.inputIpDialog.xml
		 * R.layout.inputIpDialog.xml只放置了一个EditView，
		 * R.layout.inputIpDialog.xml可自定义更复杂的View
		 */

		AlertDialog.Builder customizeDialog = new AlertDialog.Builder(
				Start.this);
		final View dialogView = LayoutInflater.from(Start.this).inflate(
				R.layout.input_ip_dialog, null);
		customizeDialog.setTitle("请输入要连接设备的IP地址");
		customizeDialog.setView(dialogView);
		final EditText edit_text = (EditText) dialogView
				.findViewById(R.id.edit_text);
		if (inputIp != null) {
			edit_text.setText(inputIp);
			Log.v("ceshi", "不空");

		} else {
			edit_text.setText("还没有要连接的设备");
			Log.v("ceshi", "空的");
		}
		Log.v("ceshi", "你好");

		customizeDialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences.Editor editor = getSharedPreferences(
								"inputIp", MODE_PRIVATE).edit();

						// 获取EditView中的输入内容
						Toast.makeText(Start.this,
								edit_text.getText().toString(),
								Toast.LENGTH_SHORT).show();
						String getInPut = edit_text.getText().toString();
						if (getInPut != null) {
							editor.putString("Ip", getInPut);
							editor.commit();

						} else
							return;

					}
				});

		customizeDialog.show();

	}

	public void showDownLoadDialog() {
		new AlertDialog.Builder(this).setTitle("确认").setMessage("是否下载？")
				.setPositiveButton("是", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Log.d(TAG, "onClick 1 = " + which);
						doDownLoadWork();
					}
				}).setNegativeButton("否", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Log.d(TAG, "onClick 2 = " + which);
					}
				}).show();
	}

	public void showUnzipDialog() {
		new AlertDialog.Builder(this).setTitle("确认").setMessage("是否解压？")
				.setPositiveButton("是", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Log.d(TAG, "onClick 1 = " + which);
						doZipExtractorWork();
					}
				}).setNegativeButton("否", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Log.d(TAG, "onClick 2 = " + which);
					}
				}).show();
	}

	public void doZipExtractorWork() {
		// ZipExtractorTask task = new
		// ZipExtractorTask("/storage/usb3/system.zip",
		// "/storage/emulated/legacy/", this, true);
		ZipExtractorTask task = new ZipExtractorTask(
				"/storage/emulated/0/xyz/download.php.zip",
				"/storage/emulated/0/xyz", this, true);
		task.execute();
		Calligraph.nameText.setText("您有新的作业需要批改");//0419
		
		Uri data = Uri.parse("file://storage/emulated/0/");
		Start.context.sendBroadcast(new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
		Calligraph.pageTotal  = Calligraph.getFileNumber("/sdcard/xyz/");
//		pageTotal = 0;
		Calligraph.pageText.setText("共"+Calligraph.pageTotal+"份");
		Calligraph.pageText.setVisibility(View.VISIBLE);

	}

	private void doDownLoadWork() {
		/*
		 * DownLoaderTask task = new DownLoaderTask("http://" + inputIp +
		 * "/jxyv1/Public/homework.zip", "/storage/emulated/0/testzip", this);
		 */
		/*
		 * DownLoaderTask task = new DownLoaderTask("http://" + inputIp +
		 * "/Public/Uploads/123.page", "/sdcard/xyz/", this);
		 */
		// DownLoaderTask task = new DownLoaderTask("http://" + inputIp
		// + "/jxyv1//Public/SmartPenUploads/123.page", "/sdcard/xyz/",
		// this);
		// 20190413测试下载接口
		DownLoaderTask task = new DownLoaderTask(
				"http://118.24.109.3/Public/smartpen/download.php",
				"/sdcard/xyz/", this);

		// DownLoaderTask task = new
		// DownLoaderTask("http://192.168.9.155/johnny/test.h264",
		// getCacheDir().getAbsolutePath()+"/", this);
		task.execute();
	}

	/**
	 * Method for非低功耗蓝牙 zgm
	 */
	private void setupChat() {
		Log.d(TAG, "setupChat()");

		// Initialize the array adapter for the conversation thread
		// mConversationArrayAdapter = new ArrayAdapter<String>(this,
		// R.layout.message);
		// mConversationView = (ListView) findViewById(R.id.in);
		// mConversationView.setAdapter(mConversationArrayAdapter);

		// Initialize the compose field with a listener for the return key
		// mOutEditText = (EditText) findViewById(R.id.edit_text_out);
		// mOutEditText.setOnEditorActionListener(mWriteListener);

		// Initialize the send button with a listener that for click events
		// mSendButton = (Button) findViewById(R.id.button_send);

		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new com.jinke.smartpen.BluetoothChatService(this,
				mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}

	private void connectDevice(Intent data, boolean secure) {
		// Get the device MAC address
		String address = data.getExtras().getString(
				com.jinke.smartpen.DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		// Get the BluetoothDevice object
		BluetoothDevice device = tableBluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the device
		mChatService.connect(device, secure);
		// String message= "had";
		// sendMessage(message);
	}

	// 确保设备能够被发现
	private void ensureDiscoverable() {

		Log.d(TAG, "ensure discoverable");
		if (tableBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            A string of text to send.
	 */

	private void sendMessage(String message) {
		Log.e("zgm", "Dot信息,:" + message);
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != com.jinke.smartpen.BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mChatService.write(send);

			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);
			// mOutEditText.setText(mOutStringBuffer);
		}
	}

	private void mSendMessage(byte[] send) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != com.jinke.smartpen.BluetoothChatService.STATE_CONNECTED) {
			// Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
			// .show();
			return;
		}

		// Get the message bytes and tell the BluetoothChatService to write

		mChatService.write(send);

		// Reset out string buffer to zero and clear the edit text field
		mOutStringBuffer.setLength(0);
		// mOutEditText.setText(mOutStringBuffer);
	}

	
	//从学生作业中读取数据显示
	public void drawsmartpenpoints(final SmartPenPage sPenPage, final DrawView mDrawView) {
		if (sPenPage == null) {
//			Toast.makeText(context, "文件不存在或不可读", Toast.LENGTH_SHORT).show();
			Log.i("name", "0615:null");
			return;
		}
		Log.i("name", "0615:sPenPage.getAllPoints().size():"+sPenPage.getAllPoints().size());
//		toolFun.SetPenColor(6, mDrawView);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				// TODO Auto-generated method stub
				for (int i = 0; i < sPenPage.getAllPoints().size(); i++) {
					for (Dot dot : sPenPage.getAllPoints().get((long) (i + 1))) {
						Log.i("zgm", "0615:c1"+i);
						studentDotsContainer.put(dot.PageID,dot);
						toolFun.ProcessEachDot(dot, mDrawView);
					}
				}				
			}
		});
	}
	
	public void drawsmartpenpointsfromteacher(final SmartPenPage sPenPage, final DrawView mDrawView) {
		if (sPenPage == null) {
//			Toast.makeText(context, "文件不存在或不可读", Toast.LENGTH_SHORT).show();
			Log.i("name", "0615:null");
			return;
		}
		Log.i("name", "0615:sPenPage.getAllPoints().size():"+sPenPage.getAllPoints().size());
//		toolFun.SetPenColor(6, mDrawView);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				// TODO Auto-generated method stub
				for (int i = 0; i < sPenPage.getAllPoints().size(); i++) {
					for (Dot dot : sPenPage.getAllPoints().get((long) (i + 1))) {
						Log.i("zgm", "0615:c1"+i);
						teacherDotsContainer.put(dot.PageID,dot);
						toolFun.ProcessEachDot(dot, mDrawView);
					}
				}				
			}
		});
	}
	
	

	public SmartPenPage getfromFile(String pathString) {
		SmartPenPage aPage = null;
		try {
//			aPage = SmartPenUnitils.load("sdcard/xyz/00-001.page");
			aPage = SmartPenUnitils.load(pathString);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return aPage;
	}

	/**
	 * 语音提示
	 * 
	 * @param raw
	 */
	public void showSound(int raw) {
		if (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		Context aContext = getApplicationContext();
		mediaPlayer = MediaPlayer.create(getApplicationContext(), raw);
		mediaPlayer.setVolume(1.0f, 1.0f);
		mediaPlayer.start();


	}	
	public  String getCurrentPageName(String path, int bookid,int pageid){
		String bookID = ""+bookid;
		String pageID = ""+pageid;
		String fileName = null;
		File[] fileArray = null;

		String file;
		String[] id = null;
		List<String> filePathList = new ArrayList<String>();
		File files = new File(path);
		fileArray = files.listFiles();
		for(int i=0;i<fileArray.length;i++){
			String filepathname = fileArray[i].getPath();
			String ori = filepathname.substring(filepathname.lastIndexOf("/"));
			Log.i("getfile", "ori====="+ori);
			ori = ori.substring(1,ori.length());
		    file  = ori.substring(0,ori.lastIndexOf("."));
			id = file.split("-");
			
		    if(id[0].equals("NONE")&&bookID.equals(id[2])&&pageID.equals(id[3])){
		    	fileName = ori;	
		    	break;
		    }
		    else fileName = "NONE-0000-0-0-0.page";
		
			
		}
		if (fileName==null) {
			fileName = "NONE-0000-0-0-0.page";
		}
		Log.i("getfile", "filename====="+fileName);
		

		
		String tema="";
		if(id!=null&&id.length>2) {
			tema=id[1];
			switch(Integer.parseInt(id[1])){
			case 944:
				gCurName = "陈程";
				break;
			case 945:
				gCurName = "王洪亮";
				break;
			case 946:
				gCurName = "李亚芳";
				break;
			case 947:
				gCurName = "苏星辰";
			case 948:
				gCurName = "路涛";
				break;
			default:
				gCurName = "";
				break;
			}
		}
		final String sid =tema;
		runOnUiThread( new Runnable() {
			public void run() {
				Calligraph.setNameTextbyPageID(sid,gCurName);
				Calligraph.pigaihuanPingyuText.setText("");
				Calligraph.pigaihuanPingyuText.setVisibility(View.GONE);
			}
		});
		
		return fileName;
	
	}
	public void doDrawFromFile(String fileName , DrawView drawView) {
			new DrawFromFileTask(fileName,drawView, this).execute();		

	} 
	public void doDrawFromFile1(String fileName , DrawView drawView) {
		new DrawFromFileTask1(fileName,drawView, this).execute();		
} 
	
public void doUpLoadTask(final String srcPath) {
		upLoadTask=new UpLoadTask(srcPath, this);
	upLoadTask.dialogmessage="存储文件，请不要进行其他操作";
	upLoadTask.execute();
	reupLoadCounter=0;
	
		
		
	TimerTask task = new TimerTask() {  
	    @Override  
	    public void run() { 
			
//	    	if (upLoadTask.mresult.equals("The file "+srcPath.substring(srcPath.lastIndexOf("/")+1)+" has been uploaded")) {
//	    		showToast("云端存储文件成功");
	    	if (upLoadTask.mresult.equals(upLoadTask.md5)) {
	    		if (timerInStartActivity!=null) {
					timerInStartActivity.cancel();
					timerInStartActivity.purge();
					timerInStartActivity=null;	
					this.cancel();
				}

			}else {
				upLoadTask.timer.cancel();
				upLoadTask.con.disconnect();
				if(upLoadTask.mDialog!=null&&upLoadTask.mDialog.isShowing()){
					upLoadTask.mDialog.dismiss();
					dealingSomeThing=false;
					}
				upLoadTask.cancel(true);
				reupLoadCounter++;
				if (reupLoadCounter<4) {
					showSound(R.raw.reupload);
					Log.i("md5","reupload");
					upLoadTask=new UpLoadTask(srcPath, context);
					upLoadTask.dialogmessage="存储失败，重新存储第"+reupLoadCounter+"次";
					Log.i("md5","reupload"+reupLoadCounter);
					upLoadTask.execute();
					
				}else {
//					timerInStartActivity.cancel();
					timerInStartActivity.cancel();
					timerInStartActivity.purge();
					timerInStartActivity=null;
					upLoadTask.mDialog.dismiss();
					showToast("最小微课录制失败，请重新录制");
					//删除存储的录音文件，重新录制0507
					deleteRecordFile("/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3");
					showSound(R.raw.upload_fail);
					reupLoadCounter=0;
					dealingSomeThing=false;
				   this.cancel();
				}
			}
	    }  
	};
	if (timerInStartActivity==null) {
		timerInStartActivity=new java.util.Timer();
	}	//timerInStartActivity.schedule(task, 0, 1000);
	timerInStartActivity.schedule(task, 3000, 3000);
	dealingSomeThing=false;
//		//0507
//		
//	    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//        executorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//            	if (upLoadTask.mresult.equals("The file "+srcPath.substring(srcPath.lastIndexOf("/")+1)+" has been uploaded")) {
//	    		showToast("云端存储文件成功");
//	    	if (upLoadTask.mresult.equals(upLoadTask.md5)) {
//	    		if (timerInStartActivity!=null) {
//					timerInStartActivity.cancel();
//					timerInStartActivity.purge();
//					timerInStartActivity=null;	
////					this.cancel();
//				}
//
//			}else {
//				upLoadTask.timer.cancel();
//				upLoadTask.con.disconnect();
//				if(upLoadTask.mDialog!=null&&upLoadTask.mDialog.isShowing()){
//					upLoadTask.mDialog.dismiss();
//					dealingSomeThing=false;
//					}
//				upLoadTask.cancel(true);
//				reupLoadCounter++;
//				if (reupLoadCounter<4) {
//					showSound(R.raw.reupload);
//					upLoadTask=new UpLoadTask(srcPath, context);
//					upLoadTask.dialogmessage="存储失败，重新存储第"+reupLoadCounter+"次";
//					upLoadTask.execute();
//					
//				}else {
////					timerInStartActivity.cancel();
//					timerInStartActivity.cancel();
//					timerInStartActivity.purge();
//					timerInStartActivity=null;
//					upLoadTask.mDialog.dismiss();
//					showToast("最小微课录制失败，请重新录制");
//					//删除存储的录音文件，重新录制0507
//					deleteRecordFile("/sdcard/-1/" + "001-"+currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+uploadTAG+".mp3");
//					showSound(R.raw.upload_fail);
//					reupLoadCounter=0;
//					dealingSomeThing=false;
////				   this.cancel();
//				}
//			}
//	    }  
//            }
//        }, 0, 4000, TimeUnit.MILLISECONDS);
//		
//		
//        dealingSomeThing=false;
	

}
	public void doDrawFromContainer(ArrayListMultimap<Integer, Dot>container,DrawView dv) {
		new DrawfromPigaihuanContainer(container, dv, this).execute();//开启异步任务，执行的是下面这个函数
	}
	//处理pigaihuanContainer中的数据，并画在指定的画布上
	public void drawfromContainer(final ArrayListMultimap<Integer, Dot>almm, final DrawView dwView) {
		//画在bdrawl[0]上
		
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				dealingSomeThing=true;
				for(Dot dot:almm.get(gCurPageID)) {
					if(c.pigaihuanLayout.getVisibility()==View.VISIBLE) {
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
					{
						switch(dot.type) {
						case  PEN_DOWN:type=0;break;
						case  PEN_MOVE:type=1;break;
						case  PEN_UP:type=2;break;
						default: type=0;break;
						}
					}
					Log.i("gx","x="+x+"-------y="+y+"------type="+type);
					if(almm.equals(pigaihuanDotsContainer)) 
						dwView.paint.setColor(Color.BLACK);
					else dwView.paint.setColor(Color.RED);
					if(x>0&&y>0)
					toolFun.drawSubFountainPen2(dwView, 1, 0,
							0, 6, x, (float)(y*1.12), dot.force, type);
					
				}
			dealingSomeThing=false;
			}
		});
		
		
		
		

	}
	
	/*
	 * 此函数用于智能笔 获取批改底图,，新建一个文件夹pagebackground用来存放20页底图0425
	 */
	
	public void setSmartpenPageBackground(int bookNumber,int pageNumber) {
		Bitmap bgBitmap = null;
		int pageID = 0;
		switch(bookNumber) {
		case 0: pageID=pageNumber%20;break;
		case 1: pageID=pageNumber%8+100;break;
		default:break;
		
		}
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.outWidth = 1600;
		options.outHeight = 2400;
		options.inJustDecodeBounds = false;

		bgBitmap = BitmapFactory.decodeFile("/storage/emulated/0/pagebackground/"+pageID+".jpg").copy(
				Bitmap.Config.ARGB_4444, true);
		if(bgBitmap==null)return;
		Bitmap bg = null;
		if (bgBitmap.getWidth() <= 1600) {
			try {
//		        Log.i("bgoom","bg:"+(bg==null));
				System.gc();
				bg = Bitmap.createScaledBitmap(bgBitmap, 1600, 2400, true);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			float ratio = bgBitmap.getWidth() / (float) 1600;
			bg = Bitmap.createScaledBitmap(bgBitmap, 1600,
					(int) ((int) bgBitmap.getHeight() / ratio), true);
		}
		c.view.freeBitmap.resetFreeBitmapList();
		c.view.freeBitmap.addBgPic(bg);
//		c.view.setBitmap(bg);
		c.view.changeStateAndSync(0);
		bgBitmap.recycle();
	}
	
	public void showToast(final String message) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), message,Toast.LENGTH_SHORT).show();
				
			}});
	}
	public boolean deleteRecordFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }
	
	
	public void RunReplay(final ArrayListMultimap<Integer, Dot>container,final DrawView dv) {
        if (gCurPageID < 0) {
            toolFun.bIsReply = false;
            return;
        }

        toolFun.drawInit(toolFun.bDrawl[1]);
        toolFun.bDrawl[1].canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                ReplayCurrentPage(container,dv);
            }
        }).start();
    }

    public void ReplayCurrentPage(ArrayListMultimap<Integer, Dot>container,DrawView dv) {
    	int type = 0;

        if (container.isEmpty()) {
            toolFun.bIsReply = false;
            return;
        }

        Set<Integer> keys = container.keySet();
        for (int key : keys) {
            //Log.i(TAG, "=========pageID=======" + PageID + "=====Key=====" + key);
            toolFun.bIsReply = true;
            if (key == Start.gCurPageID) {
            	for(Dot dot:container.get(gCurPageID)) {
            		{
						switch(dot.type) {
						case  PEN_DOWN:type=0;break;
						case  PEN_MOVE:type=1;break;
						case  PEN_UP:type=2;break;
						}
					}
                    //Log.i(TAG, "=========pageID1111=======" + dot.pointX + "====" + dot.pointY + "===" + dot.ntype);
            		toolFun.drawSubFountainPen1(dv, 1, 0, 0, 6, dot.x, (float)(dot.y*1.12), dot.force, type,Color.BLACK);
                    //drawSubFountainPen3(bDrawl[0], gScale, gOffsetX, gOffsetY, dot.penWidth, dot.pointX, dot.pointY, dot.force);

                    toolFun.bDrawl[1].postInvalidate();
                    SystemClock.sleep(20);
                }
            }
   
	
	
	
        }
    }
	
public void showAlertDialog() {
	runOnUiThread(new Runnable() {
		
		@Override
		public void run() {
			if (Looper.myLooper()==null) {
				Looper.prepare();
			}
			// TODO Auto-generated method stub
		synchronized (this) {
			if (alertDialog==null) {
				alertDialog = new Builder(context).create();
			}			
		}	
			alertDialog.setMessage("处理事务中，不处理任何笔迹数据");
			alertDialog.setTitle("请注意");
			//0507cahe先取消掉alertdialog
//			alertDialog.show();			
		}
	});


}	
	//wsk 2019.6.18
     private void writeinput(Dot tempDot,String parg) throws IOException {
	  File file = new File(parg);
	  BufferedWriter out=new BufferedWriter(new OutputStreamWriter(
	             new FileOutputStream(file)));
	  out.write(tempDot.x+" "+tempDot.y+" "+tempDot.type+"");
	  out.close();
	 }

	/**
	 * Method for非低功耗蓝牙,结束 zgm
	 */
    /**
     * 检查上传标记，确定上传是否成功，否则重新上传一次
     * @param statsus
     */
    public void checkIsUploadSucess(boolean statsus) {

    	
    }
    public float getDistance(int x1,int x2,int y1,int y2) {
        float d=0;
        d=(float) Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2, 2));
        return d;
          
       }
    
    
}//activity的结束
