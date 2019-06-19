package com.jinke.calligraphy.app.branch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import android.R.integer;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;

import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.CaseFormat;
import com.jinke.adhocNDK.Jni;
import com.jinke.calligraphy.database.CDBPersistent;
import com.jinke.calligraphy.database.CalligraphyDB;
import com.jinke.calligraphy.fliplayout.FlipHorizontalLayout;
import com.jinke.calligraphy.template.Available;
import com.jinke.calligraphy.template.WolfTemplate;
import com.jinke.calligraphy.template.WolfTemplateUtil;
import com.jinke.calligraphy.touchmode.HandWriteMode;
import com.jinke.calligraphy.touchmode.SideDownMode;
import com.jinke.calligraphy.touchmode.TouchMode;
import com.jinke.downloadanddecompression.DownLoaderTask;
import com.jinke.horizontallistview.HorizontalListView;
import com.jinke.horizontallistview.HorizontalListViewAdapter;
import com.jinke.mindmap.MindMapItem;
import com.jinke.mywidget.FlipImageView;
import com.jinke.mywidget.interpolator.EasingType.Type;
import com.jinke.mywidget.interpolator.ElasticInterpolator;
import com.jinke.mywidget.widget.Panel;
import com.jinke.mywidget.widget.Panel.OnPanelListener;
import com.jinke.single.BitmapCount;
import com.jinke.single.LogUtil;
import com.jinke.single.ScaleSave;
import com.jinke.smartpen.DrawView;
import com.jinke.smartpen.ToolFun;

import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

public class Calligraph extends RelativeLayout implements OnPanelListener,
		OnClickListener {
	Start activity;
	final static int POPUP_VIEW_FACE = 1;
	final static int POPUP_VIEW_WEATHER = 2;
	private final File mStoreFile = new File(
			Environment.getExternalStorageDirectory(), "gestures");
	private static final String PHOTOSHARE = "PhotoShare";
	private static final String VIDEOSHARE = "VideoShare";
	private static final String AUDIOSHARE = "AudioShare";

	// public static final int GESTURE_MODE_ON = 1;
	// public static final int GESTURE_MODE_OFF = 0;
	// public static int GESTURE_MODE = GESTURE_MODE_OFF;

	final private String str = "com.jinke.wifiadhoc.select.wifioradhoc";

	private String shareType = PHOTOSHARE;

	private String[] savepath = null;

	private Context mContext = null;
	private WolfTemplate mTemplate;// 模板信息
	private static final String TAG = "Calligraph";

	// -------------------------------------------
	private Button expend_AlarmBtn;
	private Button expend_FaceBtn;
	private Button expend_WeatherBtn;
	private Button expend_AudioBtn;
	private Button expend_AddpicBtn;
	private Button expend_AddCameraBtn;
	private Button expend_AddVideoBtn;
	// -------------------------------------------
	static final int ALERT_MUSIC_DLG = 0;
	static final int ALERT_RECORD_DLG = 1;
	private DialogRecordListener dbrListener;
	private Dialog mRecordAlertDlg;
	private static MediaRecorder mAudioRecorder;
	private static String recorderPath;
	private static int recordAid;
	private static int recordIid;
	private static boolean isRecording;
	private void initRecord() {
		dbrListener = new DialogRecordListener();
		// 获得sdcard路径
		int recordCount = 0;
		// 设置录音文件路径
		mAudioRecorder = new MediaRecorder();
		isRecording = false;

	}

	public static GestureView gestures;
	public static GestureView pigaihuanGestures;// 批改环手势识别层
	public static float yy;// 设置toast显示位置

	// 抬头作业信息
	private LinearLayout personalInfoDisplayLayout;
	private LinearLayout pageInfoDisplayLayout;
	public LinearLayout staticInfoDisplayLayout;
	public LinearLayout pingyuDisplayLayout;
	public LinearLayout pingyuDisplayLayout1;
	public LinearLayout tounaoDisplayLayout;
	public FrameLayout gesLayout;
	public RelativeLayout transParentStatisticLayout;
	public static RelativeLayout pigaihuanLayout;
	public static ImageView ansHintIV;
	public static TextView nameText;
	public static TextView pageText;
	public static TextView staticText;
	public static TextView pigaihuanSubText;
	public static TextView pigaihuanPingyuText;
	public static TextView sumText;//和分
	public static ImageView pingyuText;
	public static ImageView pingyuText1;
	public static ImageView tounaoText;
	public static DragAndPaintView pBgImage;
	public static DrawView drawView;
	public static TextView totalTimeTv;
	public Button replayBtn;
	public String inputIP="123.206.16.114";//用户输入的IP地址
   
	

	public int clickCount = 0;// 历史图片点击次数（双击or单击）

	public static BorderTextView[] statisticTextView;//透明统计条
	public static TextView[] commentsTv;//0426
	public  TextView recordTime;//0426
	public static String keshiName = "第五章相交线与平行线 ";
	public static String name = "0944 姓名：陈程";
	public static int pageNum = 1;
	public int indexTiMu = 1;
	public static int pageTotal = 4;
    public static int unrevisedCount = 3;
	public static int currentItem = 0;
	private GestureLibrary gestureLib;// 创建一个手势仓库
	public static String comments = "";//0426用于保存批改环点选的评语，显示在主界面上

	public static ImageView pigaiResultImageView;
	// public static HorizontalListView hListView1;
	public static HorizontalListView[] hListView = new HorizontalListView[5];

	final static int unclick = 0xff18499d;
	final static int click = 0xff000080;
	
	//wsk 2019.5.30 总分
	public static int sum = 100;

	/*
	 * zgm 20170521
	 */

	String[] rightpicturesPath;
	String[] almostpicturesPath;
	String[] wrong1picturesPath;
	String[] wrong2picturesPath;
	String[] wrongpicturesPath;
	public String[][] imageList = { rightpicturesPath, almostpicturesPath,
			wrong1picturesPath, wrong2picturesPath, wrongpicturesPath

	};

	// 传入一个文件名获取他的图片，返回的是图片路径数组

	private String[] getFile(File files) {
		Log.v("zgm", "这是测试");
		String[] returnStrings = null;
		ArrayList<String> dirAllStrArr = new ArrayList<String>();
		try {
			// 如果手机插入了SD卡，而且应用程序具有访问SD卡的权限
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 获取SD卡的目录
				File sdCardDir = Environment.getExternalStorageDirectory();
				File newFile = new File(sdCardDir + File.separator + "mynewimg"
						+ File.separator + files);
			//	Log.v("zgm", "这是测试1" + newFile);
				File[] filesArry= newFile.listFiles();

			//	Log.v("zgm", "这是测试" + filesArry.length);
//				Log.v("zgm", "布尔值：" + filesArry[0].isDirectory());
				for (File file : filesArry) {
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

	// 录音到指定路径
	private static boolean startRecord(String r_path) {

		// 设置音频源
		mAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// 设置输出格式
		mAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		// 设置编码格式
		mAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		// 设置文件输出路径
		mAudioRecorder.setOutputFile(r_path);
		try {
			// 录音准备

			mAudioRecorder.prepare();
			mAudioRecorder.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			Log.e("record", "recordException", e);
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("record", "recordException", e);
			return false;
		}

		isRecording = true;

		Log.e("record", "set isRecording:" + isRecording);
		return true;
	}

	// 停止录音
	private static void stopRecording() {
		if (isRecording) {
			mAudioRecorder.stop();
			mAudioRecorder.reset();
			isRecording = false;
			Log.e("record", "set isRecording:" + isRecording);
		}

		// mAudioRecorder.release();
	}

	// 生成对话框//根据对话框类型
	protected Dialog onCreatDialog(int dlg_id) {
		return initRecordAlertDlg();
	}

	// 录音对话框事件处理
	public static void positiveDialogOnClick() {

		// TODO Auto-generated method stub
		if (isRecording) {
			my_toast("录音完成");
			stopRecording();
			Uri cameraUri = Uri.parse(recorderPath);

			Bitmap newBitmap = null;
			double dur = MediaPlayerUtil.getInstance().getDuration(cameraUri) / 1000;// s
			String duration = Math.floor(dur / 60) + "分"
					+ Math.ceil((dur / 60 - Math.floor(dur / 60)) * 60) + "秒";
			Log.e("media", "duration:" + duration);
			try {
				newBitmap = BitmapFactory.decodeResource(
						Start.context.getResources(), R.drawable.audio_stop)
						.copy(Config.ARGB_4444, true);

				BitmapCount.getInstance().createBitmap(
						"BaseBitmap decode R.drawable.audio_playing");
				BitmapCount.getInstance().createBitmap(
						"BaseBitmap decode R.drawable.audio_stop");

				Canvas canvas = new Canvas();
				canvas.setBitmap(newBitmap);
				Paint p = new Paint();
				p.setTextSize(20);
				canvas.drawText(duration, 145f, 30f, p);

			} catch (OutOfMemoryError e) {
				// TODO: handle exception
				Toast.makeText(Start.context, "内存不足，不能插入", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			EditableCalligraphyItem item = Start.c.view.cursorBitmap.listEditableCalligraphy
					.get(recordAid).getCharsList().get(recordIid);
			// view.cursorBitmap.listEditableCalligraphy.get(aid).getCharsList().get(iid).resetAudioUri(newBitmap,new
			// Matrix(),cameraUri);
			item.resetAudioUri(newBitmap, new Matrix(), cameraUri);
			item.setStopBitmap();
			CalligraphyDB.getInstance(Start.context).updateAudioUri(
					Start.getPageNum(), recordAid, item.getItemID(), cameraUri,
					newBitmap);

			Start.c.view.cursorBitmap.updateHandwriteState();
			WorkQueue.getInstance().endFlipping();
		} else {

			try {
				Start.c.view.cursorBitmap.insertAudioBitmap(BitmapFactory
						.decodeResource(Start.context.getResources(),
								R.drawable.audio_unfinish), null);
				BitmapCount.getInstance().createBitmap(
						"Calligraph decode insertAudioBitmap audio_unfinish");

			} catch (OutOfMemoryError e) {
				// TODO: handle exception
				Toast.makeText(Start.context, "内存不足，不能插入", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			Uri uri = Uri.parse("file:///android_asset/audio.png");// 默认等待图片
			Log.e("camera", uri.toString());

			recordAid = Start.c.view.cursorBitmap.cal_current.getID();
			recordIid = Start.c.view.cursorBitmap.cal_current.currentpos - 1;

			recorderPath = Start.getStoragePath() + "/calldir/free_"
					+ Start.getPageNum() + "/a" + recordAid + "i" + recordIid
					+ ".3gp";
			LogUtil.getInstance().e("record", "path:" + recorderPath);
			my_toast("开始录音");

			startRecord(recorderPath);
		}
	}

	// 初始化录音对话框
	private Dialog initRecordAlertDlg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(Start.context);
		Log.e("record", "initRecordAlertDlg:" + isRecording);
		if (isRecording) {
			builder.setMessage("完成录音?");
		} else {
			builder.setMessage("开始录音?");
		}

		builder.setCancelable(false);
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				positiveDialogOnClick();
				dialog.cancel();

			}
		});
		builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		return builder.create();
	}

	// 按钮点击事件-----点击显示对话框
	class DialogRecordListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// 点击显示对话框
			mRecordAlertDlg.show();
		}
	}

	// ------------------------------------------

	// private HandWriteEditLayout handwriteMenuLayout;
	private LinearLayout handwriteMenuLayout;
	private LinearLayout handwriteControlLayout;
	public static Button mPenStatusChangeBtn;
	public static Button mHandwriteBackwardBtn;
	public static Button mHandwriteForwardBtn;

	public static Button mHandwriteEndofLineBtn;

	public static Button mHandwriteInsertSpaceBtn;
	public static Button mHandwriteInsertEnSpaceBtn;

	public static Button rightBtn;
	public static Button leftBtn;
	public static Button rightDownBtn;

	public static Button mHandWriteUndoBtn;
	public static Button mHandwriteDelBtn;
	public static Button mDrawStatusChangeBtn;
	public static Button mHandwriteNewBtn;
	public static Button mMicrophoneBtn;
	public static Button mCameraBtn;
	// add download button, caoheng, 10.24
	public static Button mDownloadBtn;

	// ly
	public static Button mNextBtn;
	// end

	public static HandWriteEditLayout flipblockLayout;
	public static FlipHorizontalLayout flipblockHLayout;

	public static Button mDragEnableBtn;

	public MyView view;
	public Bitmap mBitmap; // 主图层，900*Start.SCREEN_HEIGHT
	public static Bitmap mScreenLayerBitmap;
	// 用来放大缩小的bitmap
	public static Bitmap mScaleBitmap;
	public static Bitmap mScaleTransparentBitmap;

	private String drawText;
	private String penText;

	private static final int DRAW_STATUS_BTN_ID = 1;
	private static final int PEN_STATUS_BTN_ID = 2;
	public static int sceneSituation = 0;

	public static Button flipblockBtn;
	public static Button flipblockHBtn;

	/**
	 * 2016.12.12 zgm pigaihuan view
	 * 
	 * 
	 */
	// ************************按钮变量区***************************************

	// private Button[] history=new
	// Button[]{hisrory11,history12,history13,hisrory14,history15};

	private Button history1;
	private Button history2;
	private Button history3;
	private Button history4;
	private Button history5;
	private Button knowledge1;
	private Button base;
	private Button conceptation;
	private Button memory;
	private Button textbook;
	private Button enlighten;
	private Button incentive;
	private Button exception;
	private Button drawback;
	private Button method;
	private Button write;
	private Button expression;
	private Button trick;
	private Button unstandard;
	private Button analyze;

	// ****************************************************************almost（半错1）图片***************************************************
	public ImageView rightHistorys;
	public ImageView almostHistorys;
	public ImageView wrong1Historys;
	public ImageView wrong2Historys;
	public ImageView wrongHistorys;
	public ImageView[] historyImages;// 历史图片堆，在ListView下面
	public BigImageView bigimage;

	public static Canvas pigaihuanSaveCanvas;// 批改环批改痕迹保存画布

	public Bitmap savePigaihuanBitmap;

	// ************************************ 整个view控制区
	// ********************************
	private float blankx = 1200;// 空白区域的宽
	private float blanky = 900;// 空白区域的高
	private float biLiYinZi = 1; // 批改环宽度比例因子
	private float band = 200 / biLiYinZi;// 批改环的环的宽度
	private float px = 0 + (200 - band);// 批改环中心的坐标x的偏移量
	private float py = 800 + (200 - band);// 批改环中心的坐标y的偏移量
	private float pghX = (blankx + 2 * band) / 2 + px;// 批改环中心的坐标x
	private float pghY = (blanky + 2 * band) / 2 + py;// 批改环中心的坐标y

	// ***********************************批改环变量区****************************************
	/**
	 * halfblankx:x方向空白区域宽度的一半 halfblanky:y方向空白区域高度度的一半 halfband: 批改环宽度的一半
	 * insideLX: 空白区域左边x方向的坐标 insideLY: 空白区域上边y方向的坐标 insideRX: 空白区域右边x方向的坐标
	 * insideRY: 空白区域下边y方向的坐标 outsidelx: 批改环外围左边x方向的坐标 outsidely: 批改环外围上边边y方向的坐标
	 * outsiderx: 批改环外围右边x方向的坐标 outsidery: 批改环外围下边y方向的坐标
	 * 
	 */
	private float halfblankx = blankx / 2;
	private float halfblanky = blanky / 2;
	private float halfband = band / 2;
	private float insideLX = pghX - halfblankx;
	private float insideLY = pghY - halfblanky;
	private float insideRX = pghX + halfblankx;
	private float insideRY = pghY + halfblanky;
	// private float outsidelx = pghX - halfblankx - band;
	// private float outsidely = pghY - halfblanky - band;
	// private float outsiderx = pghX + halfblankx + band;
	// private float outsidery = pghY + halfblanky + band;

	/**
	 * 2016.12.20 zgm
	 */
	private int subMenuTextColor = 0Xfffffff8;
	private int subMenuBackgroundColor = 0xff18499d;


	// *******************************批改环上表格历史判断类型计数器****************************************
	private int rightNumber = 0; // 该计数器的值来源于 rightCounter；
	private int halfWrong1Number = 0;
	private int halfWrong2Number = 0;
	private int halfWrong3Number = 0;
	private int wrongNumber = 0;
	// *********************************批改环手势识别计数器,记录当次批改次数*********************************************
	private int rightCounter = 0; // 当批改环手势识别为对时，该计数器更新（即+1）；
	private int halfwrong1Counter = 0; // 当批改环手势识别为半对1时，该计数器更新（即+1）；
	private int halfwrong2Counter = 0; // 当批改环手势识别为半对2时，该计数器更新（即+1）；
	private int halfwrong3Counter = 0; // 当批改环手势识别为半对3时，该计数器更新（即+1）；
	private int wrongCounter = 0; // 当批改环手势识别为错时，该计数器更新（即+1）；

	// *************************历史信息初始化***************************************************************************

	private String[] judgeHistory = new String[] {
			"历史统计：" + "15" + "\n" + "本次统计：" + rightCounter,
			"历史统计：" + "4" + "\n" + "本次统计：" + halfwrong1Counter,
			"历史统计：" + "3" + "\n" + "本次统计：" + halfwrong2Counter,
			"历史统计：" + "2" + "\n" + "本次统计：" + halfwrong3Counter,
			"历史统计：" + "2" + "\n" + "本次统计：" + wrongCounter };

	// *****************************************judgeresultlabel判决结果对应计数器加1，对应标签置为1*************************************
	private int[] judgeresultlabel = new int[] { 0, 0, 0, 0, 0 };

	static int clickedFlag = 1;

	// ***************************************二级菜单控制变量区变量区****************************
	private int subMenudelaytime = 3000; // 二级菜单显示时间

	// ******************************************二级菜单按钮变量***************************************************

	// *************************************************subMenuBtnContentVersion0**************************************************
	public static Button[] subMenuBtnLK_array = new Button[5];
	// private String[] subContentString[0] = new String[] { "二次函数定义不清",
	// "a的作用不懂",
	// "b的作用不懂", "c的作用不懂", "二次函数的形式不会区分" };

	public static Button[] subMenuBtnLB_array = new Button[5];
	// private String[] subContentString[1] = new String[] { "自变量定义不懂",
	// "因变量定义不会",
	// "等式变换不熟", "乘法运算法则不熟", "加法运算法则不熟" };

	public static Button[] subMenuBtnLC_array = new Button[5];
	// private String[] subContentString[2] = new String[] { "自变量和因变量混淆",
	// "左移右移符号混淆", "a,b作用混淆",
	// "a,c作用混淆", "b,c作用混淆" };

	public static Button[] subMenuBtnLM_array = new Button[5];
	// private String[] subContentString[3] = new String[] { "开口方向没记住",
	// "是否过原点没记住",
	// "单调性没记住", "图像缩放没记住", "图像平移没记住" };

	public static Button[] subMenuBtnLT_array = new Button[5];
	// private String[] subContentString[4] = new String[] { "哪三种形式", "h的作用是什么",
	// "k的作用是什么", "参数的关系是什么", "函数零点与方程的解什么关系" };

	public static Button[] subMenuBtnBE_array = new Button[5];
	// private String[] subContentString[5] = new String[] { "a大于零图像会怎样",
	// "如何解方程",
	// "b=0函数图像会怎样", "对称轴怎么求", "c的作用是什么" };

	public static Button[] subMenuBtnBI_array = new Button[5];
	// private String[] subContentString[6] = new String[] { "加油", "你很聪明",
	// "很有潜力", "很棒", "大红花" };

	public static Button[] subMenuBtnBEx_array = new Button[5];
	// private String[] subContentString[7] = new String[] { "仔细审题", "认真听课",
	// "灵活运用", "再接再厉", "要有耐心" };

	public static Button[] subMenuBtnBD_array = new Button[5];
	// private String[] subContentString[8] = new String[] { "要有耐心", "弄懂参数的关系",
	// "图象的位置", "两个交点会怎样", "一个交点会怎样" };

	public static Button[] subMenuBtnBM_array = new Button[5];
	// private String[] subContentString[9] = new String[] { "多做题", "将概念吃透",
	// "认真看书", "多多交流", "学习方法" };

	public static Button[] subMenuBtnRW_array = new Button[5];
	// private String[] subContentString[10] = new String[] { "字体潦草", "书写要认真",
	// "需要练字",
	// "写字要横平竖直", "字迹不清楚清楚" };

	public static Button[] subMenuBtnRE_array = new Button[5];
	// private String[] subContentString[11] = new String[] { "表述不清", "字句不通",
	// "要组织好语言", "想好在下笔", "看书上怎写" };

	public static Button[] subMenuBtnRT_array = new Button[5];
	// private String[] subContentString[12] = new String[] { "多做题", "方法细节不会",
	// "多总结", "勤加练习", "请教他人" };

	public static Button[] subMenuBtnRU_array = new Button[5];
	// private String[] subContentString[13] = new String[] { "答题完整", "结果要化简",
	// "注意格式", "认真书写", "结果化成小数" };

	public static Button[] subMenuBtnRA_array = new Button[5];

	// private String[] subContentString[14] = new String[] { "仔细审题", "重视每一个条件",
	// "沉下心去",
	// "沉着应对", "做就做好" };

	// *************************************************subMenuBtnContentVersion1**************************************************

	// private String[] subMenuBtnNameLK_arry = new String[] { "方程的根是什么",
	// "一元一次方程的定义不清",
	// "系数定义不清", "项的定义不清", "一元中的“元”是什么" };
	//
	// private Button[] subMenuBtnLB_array = new Button[5];
	// private String[] subMenuBtnNameLB_arry = new String[] { "方程化简不会",
	// "方程的形式不懂",
	// "等式变换不会", "乘法运算不熟", "加法运算不熟" };
	//
	// private Button[] subMenuBtnLC_array = new Button[5];
	// private String[] subMenuBtnNameLC_arry = new String[] { "与二元一次方程混淆",
	// "与一元二次方程混淆", "常数项和一次项搞混",
	// "一次项系数能否为零混淆", "乘法法则与加法法则搞混" };
	//
	// private Button[] subMenuBtnLM_array = new Button[5];
	// private String[] subMenuBtnNameLM_arry = new String[] { "乘法法则没记熟",
	// "加法法则没记熟",
	// "一元一次函数性质记得不熟", "次数定义不熟", "项的定义不熟" };
	//
	// private Button[] subMenuBtnLT_array = new Button[5];
	// private String[] subMenuBtnNameLT_arry = new String[] { "三种形式不熟",
	// "一次项系数的作用不熟",
	// "方程化简不熟", "基本概念不熟", "函数与等式关系不熟" };
	//
	// private Button[] subMenuBtnBE_array = new Button[5];
	// private String[] subMenuBtnNameBE_arry = new String[] { "一次项系数能否为零？",
	// "如何解方程",
	// "移动是否改号", "乘法分配律是什么", "一次的次是什么意思" };
	//
	// private Button[] subMenuBtnBI_array = new Button[5];
	// private String[] subMenuBtnNameBI_arry = new String[] { "加油", "你很聪明",
	// "很有潜力", "很棒", "大红花" };
	//
	// private Button[] subMenuBtnBEx_array = new Button[5];
	// private String[] subMenuBtnNameBEx_arry = new String[] { "仔细审题", "认真听课",
	// "灵活运用", "再接再厉", "要有耐心" };
	//
	// private Button[] subMenuBtnBD_array = new Button[5];
	// private String[] subMenuBtnNameBD_arry = new String[] { "要有耐心",
	// "弄懂参数的关系",
	// "图象的位置", "两个交点", "一个交点" };
	//
	// private Button[] subMenuBtnBM_array = new Button[5];
	// private String[] subMenuBtnNameBM_arry = new String[] { "多做题", "将概念吃透",
	// "认真看书", "多多交流", "学习方法" };
	//
	// private Button[] subMenuBtnRW_array = new Button[5];
	// private String[] subMenutextRW_arry = new String[] { "字体潦草", "书写认真",
	// "练字",
	// "横平竖直", "字迹清楚" };
	//
	// private Button[] subMenuBtnRE_array = new Button[5];
	// private String[] subMenutextRE_arry = new String[] { "如何表达", "字句不通",
	// "组织语言", "想好下笔", "看书怎写" };
	//
	// private Button[] subMenuBtnRT_array = new Button[5];
	// private String[] subMenutextRT_arry = new String[] { "多做题", "方法细节不会",
	// "多总结", "勤加练习", "请教他人" };
	//
	// private Button[] subMenuBtnRU_array = new Button[5];
	// private String[] subMenutextRU_arry = new String[] { "答题完整", "结果化成小数",
	// "注意格式", "认真书写", "积极起来" };
	//
	// private Button[] subMenuBtnRA_array = new Button[5];
	// private String[] subMenutextRA_arry = new String[] { "仔细审题", "先把概念吃透",
	// "沉下心去",
	// "沉着应对", "做就要做好" };

	public static String[][] subContentString = new String[][] {
			{ "二次函数定义不清", "a的作用不懂", "b的作用不懂", "c的作用不懂", "二次函数的形式不会区分" },
			{ "自变量定义不懂", "因变量定义不会", "等式变换不熟", "乘法运算法则不熟", "加法运算法则不熟" },
			{ "自变量和因变量混淆", "左移右移符号混淆", "a,b作用混淆", "a,c作用混淆", "b,c作用混淆" },
			{ "开口方向没记住", "是否过原点没记住", "单调性没记住", "图像缩放没记住", "图像平移没记住" },
			{ "哪三种形式", "h的作用是什么", "k的作用是什么", "参数的关系是什么", "函数零点与方程的解什么关系" },
			{ "a大于零图像会怎样", "如何解方程", "b=0函数图像会怎样", "对称轴怎么求", "c的作用是什么" },
			{ "加油", "你很聪明", "很有潜力", "很棒", "大红花" },
			{ "仔细审题", "认真听课", "灵活运用", "再接再厉", "要有耐心" },
			{ "要有耐心", "弄懂参数的关系", "图象的位置", "两个交点会怎样", "一个交点会怎样" },
			{ "多做题", "将概念吃透", "认真看书", "多多交流", "学习方法" },
			{ "字体潦草", "书写要认真", "需要练字", "写字要横平竖直", "字迹不清楚清楚" },
			{ "表述不清", "字句不通", "要组织好语言", "想好在下笔", "看书上怎写" },
			{ "多做题", "方法细节不会", "多总结", "勤加练习", "请教他人" },
			{ "答题完整", "结果要化简", "注意格式", "认真书写", "结果化成小数" },
			{ "仔细审题", "重视每一个条件", "沉下心去", "沉着应对", "做就做好" } };

	public static String[][] subMenuBtnContent1 = new String[][] {
			{ "二次函数定义不清", "a的作用不懂", "b的作用不懂", "c的作用不懂", "二次函数的形式不会区分" },
			{ "自变量定义不懂", "因变量定义不会", "等式变换不熟", "乘法运算法则不熟", "加法运算法则不熟" },
			{ "自变量和因变量混淆", "左移右移符号混淆", "a,b作用混淆", "a,c作用混淆", "b,c作用混淆" },
			{ "开口方向没记住", "是否过原点没记住", "单调性没记住", "图像缩放没记住", "图像平移没记住" },
			{ "哪三种形式", "h的作用是什么", "k的作用是什么", "参数的关系是什么", "函数零点与方程的解什么关系" },
			{ "a大于零图像会怎样", "如何解方程", "b=0函数图像会怎样", "对称轴怎么求", "c的作用是什么" },
			{ "加油", "你很聪明", "很有潜力", "很棒", "大红花" },
			{ "仔细审题", "认真听课", "灵活运用", "再接再厉", "要有耐心" },
			{ "要有耐心", "弄懂参数的关系", "图象的位置", "两个交点会怎样", "一个交点会怎样" },
			{ "多做题", "将概念吃透", "认真看书", "多多交流", "学习方法" },
			{ "字体潦草", "书写要认真", "需要练字", "写字要横平竖直", "字迹不清楚清楚" },
			{ "表述不清", "字句不通", "要组织好语言", "想好在下笔", "看书上怎写" },
			{ "多做题", "方法细节不会", "多总结", "勤加练习", "请教他人" },
			{ "答题完整", "结果要化简", "注意格式", "认真书写", "结果化成小数" },
			{ "仔细审题", "重视每一个条件", "沉下心去", "沉着应对", "做就做好" } };
	// *************************************************subMenuBtnContentVersion1**************************************************

	public static String[][] subMenuBtnContent2 = new String[][] {
			{ "方程的根是什么", "一元一次方程的定义不清", "系数定义不清", "项的定义不清", "一元中的“元”是什么" },
			{ "方程化简不会", "方程的形式不懂", "等式变换不会", "乘法运算不熟", "加法运算不熟" },
			{ "与二元一次方程混淆", "与一元二次方程混淆", "常数项和一次项搞混", "一次项系数能否为零混淆",
					"乘法法则与加法法则搞混" },
			{ "乘法法则没记熟", "加法法则没记熟", "一元一次函数性质记得不熟", "次数定义不熟", "项的定义不熟" },
			{ "三种形式不熟", "一次项系数的作用不熟", "方程化简不熟", "基本概念不熟", "函数与等式关系不熟" },
			{ "一次项系数能否为零？", "如何解方程", "移动是否改号", "乘法分配律是什么", "一次的次是什么意思" },
			{ "加油", "你很聪明", "很有潜力", "很棒", "大红花" },
			{ "仔细审题", "认真听课", "灵活运用", "再接再厉", "要有耐心" },
			{ "要有耐心", "弄懂参数的关系", "图象的位置", "两个交点", "一个交点" },
			{ "多做题", "将概念吃透", "认真看书", "多多交流", "学习方法" },
			{ "字体潦草", "书写认真", "练字", "横平竖直", "字迹清楚" },
			{ "如何表达", "字句不通", "组织语言", "想好下笔", "看书怎写" },
			{ "多做题", "方法细节不会", "多总结", "勤加练习", "请教他人" },
			{ "答题完整", "结果化成小数", "注意格式", "认真书写", "积极起来" },
			{ "仔细审题", "先把概念吃透", "沉下心去", "沉着应对", "做就要做好" } };

	public static int subMenuBtnContentVersion = 1;
	


	// 动画变量 ttt cahe
	private TextView sideText;
	private TextView bottomText;
	
	private TextView finalTv;

	TranslateAnimation tAniX;
	TranslateAnimation tAniY;

	Boolean animateFlagX = false;
	Boolean animateFlagY = false;

	String pingyuLine = "";
	String pingyuRow = "";

	// 批改环历史图片ListView可见性标志
	int historyListViewVisibilityFlag = 0;

	public int loopCounter = 0;// 添加listview的循环
	public int historyImageLoopCounter = 0;// 添加historyImage的循环

	// ********************************************************历史参考答案大图***************************************************************

	private ImageView bigHistoryQuestion;

	// *********************************************方法声明区****************************************************
	// *************************************添加button的方法，没有监听事件*******************************************

	/**
	 * 
	 * @param buttonName
	 * @param top
	 * @param left
	 * @param bottom
	 * @param right
	 * @param text
	 * @param textcolor
	 * @param backgroundcolor
	 */

	private void addButton(Button buttonName, float top, float left,
			float bottom, float right, String text, int textcolor,
			int backgroundcolor) {

		buttonName.setText(text);
		buttonName.setPadding(0, 0, 0, 0);
		//
		// buttonName.setTextColor(0xfffffffe);
		buttonName.setTextColor(textcolor);
		buttonName.setTextSize(15);
		// buttonName.setBackgroundColor(0x55135901);
		buttonName.setBackgroundColor(backgroundcolor);
		RelativeLayout.LayoutParams buttonNamep = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		buttonNamep.topMargin = (int) (top);
		buttonNamep.leftMargin = (int) left;
		buttonNamep.rightMargin = (int) (1600 - right);
		buttonNamep.bottomMargin = (int) (2400 - bottom);

		pigaihuanLayout.addView(buttonName, buttonNamep);// 这句决定了该方法只能在pigaihuanlayout所在的文件里调用

	}

	// **************************************添加图片的方法******************************************************
	private void addImage(ImageView imageName, float top, float left,
			float bottom, float right, int imageId) {
		imageName.setImageResource(imageId);
		RelativeLayout.LayoutParams imageNamep = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		imageNamep.topMargin = (int) top;
		imageNamep.leftMargin = (int) left;
		imageNamep.bottomMargin = (int) (2400 - bottom);
		imageNamep.rightMargin = (int) (1600 - right);
		pigaihuanLayout.addView(imageName, imageNamep);

	}
	private void addView(View v,float top, float left,
			float bottom, float right) {
		RelativeLayout.LayoutParams viewLayoutInParent = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		viewLayoutInParent.topMargin = (int) top;
		viewLayoutInParent.leftMargin = (int) left;
//		imageNamep.bottomMargin = (int) (2400 - bottom);
//		imageNamep.rightMargin = (int) (1600 - right);
		pigaihuanLayout.addView(v, viewLayoutInParent);
		
	}

	// private void historypicturearry(){
	// int i=0;
	// for(i=0;i<4;i++)
	// addImage(refAlmostIv, insideLY - band-200, 0, insideLY
	// - band, 450*i, R.drawable.almosthistory);
	// refAlmostIv.setVisibility(View.GONE);
	// }
	private long[] mHits = new long[] { 0, 0 };

	List<Integer> historyImageList = new ArrayList<Integer>();

	// public static Button TestButton;
	// public static FlipImageView TestButton;

	// private String path;//使用bgPath替换，bgPath 从WolfTemplateUtil中静态获得
	private String bgPath;
	private static Paint mPaint;
	public BasePointsImpl mBaseImpl;
	private Canvas mCanvas;

	// public static List<Command> undoList;
	public static List<Command> undoList;

	public PopupWindow popupWindow;
	public View popupView;

	public Panel panel;
	public static boolean wifiandadhocPause = false;
	public boolean firstTransformPicFromMobile = true;
	//0122将judge改为static   cahe
	public static String judge = "weird";
	public static int situation = 0;
	private long start = 0;
	private long end = 0;
	public static int totalQuestion = 0;
	public static Document doc;
	public static String pagecXML = "demo.xml";
//	public static String pagecXML = "demo2.2.xml";
	public static File file = new File("/sdcard/" + pagecXML);
	public static double [] pos;
	public  Question question[];//0425
	  Elements elementResult;
	  Elements elementComment;
	  public MyClickListener mClickListener=new MyClickListener();
	  //0122
	// 2016.4.11评语句子
	public static String[] commentString = { "再深入了解下函数二次项系数性质，你会做得更好",
			"作业整体非常好，对一般式要再深入了解下", "知识把握很好，要特别注意等号两边都是整式",
			"计算中移项要变号哦，相信你下次能做的更好", "对概念理解不深，二次项系数可不能等0啊",
			"移项的过程中注意变号，相信你可以做的更好" };
	public static String[] commentStringReplacement1 = { "对概念理解不深，二次项系数可不能等0啊",
			"再深入了解下函数二次项系数性质，你会做得更好", "知识把握很好，要特别注意等号两边都是整式",
			"计算中移项要变号哦，相信你下次能做的更好", "作业整体非常好，对一般式要再深入了解下",
			"移项的过程中注意变号，相信你可以做的更好" };
	public static String[] commentStringReplacement2 = { "计算要认真点，题目准确率会更高",
			"对角线概念要清晰，再碰到类似题目可以多画图", "要热爱学习，再复习下数形结合的知识",
			"熟记多边形性质并熟练应用，下次肯定能做的更好", "可以试试带入法，思维要开阔", "计算结果要化简，学习要认真起来" };
	public static String[] str1 = { "学习数学要有严谨的作风，你的思路非常清晰",
			"你的字如果能横平竖直就更好了，这次又进步了", "希望你刻苦学习的精神能保持下去，期待你更大进步",
			"数学作业中每一个细节都不能忽略，相信你下次肯定能做的更好", "马虎的问题依然存在，如果能更细心一些就更好了",
			"一定要端正学习态度，改善学习方法" };// 页评

	static {
		try {
			System.loadLibrary("adhocNDK");
			System.loadLibrary("cal_parser");// 装载jni库
			System.loadLibrary("pdc_prs"); // 装载jni库
		} catch (UnsatisfiedLinkError e) {
			e.printStackTrace();
		}
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public Calligraph(Context context) {
		super(context);
		mContext = context;

		onCreate(context);

		// 2016.3.17 gestures caoheng

		//
		// LayoutInflater inflater1 = (LayoutInflater)
		// Start.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View gview = inflater1.inflate(R.layout.gestures, null);
		gesLayout = new FrameLayout(context);
		LayoutParams gLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		LayoutParams geLp = new LayoutParams(1400, 2300);
		geLp.setMargins(130, 0, 0, 0);
		// 加上就不行 gestures.setFadeEnabled(false);
		gestures = new GestureView(context);
		gestures.setFadeOffset(2000); // 多笔画每两次的间隔时间
		gestures.setGestureColor(Color.RED);// 画笔颜色
		gestures.setUncertainGestureColor(Color.RED);// 未完成颜色
		// gestures.setBackgroundColor(Color.CYAN);
		gestures.setGestureStrokeWidth(1);// 画笔粗细值
		gestures.setGestureStrokeType(GestureView.GESTURE_STROKE_TYPE_MULTIPLE);
		// this.addView(gesLayout,geLp);
		this.addView(gestures, geLp);
		// this.addView(gesLayout,gLp);
		// gesLayout.bringToFront();
		// gestures.bringToFront();
		// gestures.setVisibility(View.GONE);
		gestures.invalidate();
		view.postInvalidate();

		// FileOutputStream fos=null;

		Log.i("pageXML", "pageC:" + pagecXML);
//解析xml模板		
		try {
			doc = Jsoup.parse(file, "UTF-8");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("pageXML", "" + view.pageXML);
		Elements element = doc.getElementsByTag("ystart");//解析xml模板
//		final Elements elementResult = doc.getElementsByTag("result");
//		final Elements elementComment = doc.getElementsByTag("comment");
		elementResult = doc.getElementsByTag("result");
	    elementComment = doc.getElementsByTag("comment");
		//0122
		totalQuestion = element.size();
		
		// Log.i("totalquestion",""+element);
//		final double [] pos = new double[totalQuestion];
		 pos = new double[totalQuestion];
		//0122
//		final Question question[] = new Question[totalQuestion];
		 question = new Question[totalQuestion];
		Log.i("sqldb", "tq = " + totalQuestion);
		for (int i = 0; i < totalQuestion; i++) {
			question[i] = new Question();
			Log.i("sqldb", "init question " + question[i].right);
			int[] result = { 15, 4, 3, 2, 2 };
//			result = DatabaseOp.readDatabase(Start.db, Start.gCurPageID,i);
			question[i].right = result[0];
			question[i].wrong = result[1];
			question[i].weird = result[2];
			question[i].weird1 = result[3];
			question[i].weird2 = result[4];
			Log.i("sqldb", "" + i + " :right=" + result[0] + " wrong="
					+ result[1] + " weird=" + result[2]);

		}
		for (int i = 0; i < totalQuestion; i++) {
			pos[i] = Integer.valueOf(element.get(i).text().toString());
			pos[i] *= 11.0;
			Log.i("totalquestion", "" + totalQuestion + "\n");
		}

		// 2016.3.30统计层 caoheng
		transParentStatisticLayout = new RelativeLayout(context);
		LayoutParams tpsLp = new LayoutParams((int)Start.mWidth, (int) Start.mHeight);

		// transParentStatisticLayout.setBackgroundColor(Color.BLUE);
		statisticTextView = new BorderTextView[totalQuestion];
		commentsTv = new TextView[totalQuestion];//0426
		for (int i = 0; i < totalQuestion-1; i++) {
			statisticTextView[i] = new BorderTextView(context);
			commentsTv[i] = new TextView(context);//0426
			LayoutParams stvLp = new LayoutParams(900, 100);
			// nameLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			stvLp.setMargins(700,
					(int)((Integer.valueOf(element.get(i).text().toString()))*13.5), 1500,
					2600 - (int)(Integer.valueOf(element.get(i).text().toString())*11.0));
			// stvLp.topMargin =
			// Integer.valueOf(element.get(i).text().toString());
			statisticTextView[i].setGravity(Gravity.CENTER_VERTICAL);
//			statisticTextView[i].setText("--------"+i+"----------");
			//透明统计条一直可见
			statisticTextView[i].setBackgroundResource(R.drawable.st);

			statisticTextView[i].setVisibility(View.GONE);
			// 2016.4.1 统计条效果
			// if(i%2==0)statisticTextView[i].setBackgroundColor(Color.RED);
			// if(i==5)statisticTextView[i].setTextColor(Color.BLUE);
			// statisticTextView[i].setText(String.valueOf(i)+"");
			// statisticTextView[i].setTextSize(i+20);
			transParentStatisticLayout.addView(statisticTextView[i], stvLp);
		}
		this.addView(transParentStatisticLayout, tpsLp);

		// gestures.setGestureStrokeType(GestureView.GESTURE_STROKE_TYPE_SINGLE);
		gestures.setUncertainGestureColor(Color.RED);

		gestures.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				yy = gestures.y;
				float originalX = event.getX();
				float originalY = event.getY();
				// 这里+130后在cursor态会崩溃，如果不加的话手势层和绘图层存在差异，这个130是手势层左边的margin值
				event.setLocation(originalX + 130, originalY);
				return view.onTouchEvent(event);

			}
		});

		// 手势识别的监听器0122
//		gestures.addOnGestureListener(null);
		gestures.addOnGestureListener(mClickListener);
/*		gestures.addOnGestureListener(new GestureView.OnGestureListener() {

			// 2016.4.12解决不同笔画数手势问题
			int lastStrokeCount;
			public GestureOverlayView overlay;
			public MotionEvent event;
			Handler handler = new Handler();

			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (event.getAction() == MotionEvent.ACTION_UP
							&& (overlay.getGesture().getLength() > 10)) {
						handler.postDelayed(this, 1000);
						lastStrokeCount = overlay.getGesture()
								.getStrokesCount();

						// Log.i("strokeevent",
						// String.valueOf(lastStrokeCount));
						switch (lastStrokeCount) {
						case 1:

							ArrayList<Prediction> predictionswr = gestureLib
									.recognize(overlay.getGesture());
							if (predictionswr.size() > 0) {
								Prediction prediction = (Prediction) predictionswr
										.get(0);//hu
								if (prediction.score > 2) {
									judge = prediction.name;
									if (judge.equals("Almost"))
										judge = "Right";
									Toast.makeText(mContext, judge,
											Toast.LENGTH_SHORT).show();
								}

								else {
									judge = "ignore";
									Toast.makeText(mContext, "写字",
											Toast.LENGTH_SHORT).show();
								}

							}
							break;
						case 2:

							ArrayList<Prediction> predictions = gestureLib
									.recognize(overlay.getGesture());
							if (predictions.size() > 0) {
								Prediction prediction1 = (Prediction) predictions
										.get(0);
								if (prediction1.score > 4) {
									judge = prediction1.name;
									if (judge.equals("Right"))
										judge = "Almost";
									Toast.makeText(mContext, judge,
											Toast.LENGTH_SHORT).show();
								}

								else
									judge = "ignore";
								Toast.makeText(mContext, "写字",
										Toast.LENGTH_SHORT).show();
							}
							// Toast.makeText(mContext, "错", Toast.LENGTH_SHORT)
							// .show();
							break;
						case 3:
							judge = "错3";
							Toast.makeText(mContext, "半对2", Toast.LENGTH_SHORT)
									.show();
							break;
						case 4:
							judge = "错4";
							Toast.makeText(mContext, "半对3", Toast.LENGTH_SHORT)
									.show();
							break;
						default:
							lastStrokeCount = 0;
						}
//switch到这结束						
						int situation = 5;

						if (judge.equals("Right"))
							situation = 0;
						else if (judge.equals("wrong"))
							situation = 1;
						else if (judge.equals("Almost"))
							situation = 2;
						else if (judge.equals("错3"))
							situation = 3;
						else if (judge.equals("错4"))
							situation = 4;

						for (currentItem = 1; currentItem < totalQuestion; currentItem++) {
							if (currentItem == 4) {
								System.arraycopy(commentStringReplacement1, 0,
										commentString, 0, 6);
								Log.i("whichcomment", ""
										+ SideDownMode.whichComment);

							}

							else
								System.arraycopy(commentStringReplacement2, 0,
										commentString, 0, 6);

							Spanned text;
							// 格式 =
							// Html.fromHtml("<font color=red><b>"+currentItem+"</b></font>");

							if ((yy > pos[currentItem - 1])
									&& (yy < pos[currentItem])) {
								Log.i("prediction", "i" + currentItem + "+"
										+ pos[currentItem]);
								switch (situation) {
								case 0:
									question[currentItem - 1].right++;
									elementResult.get(currentItem-1).text("对");
									text = Html
											.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;<font color=red><b>"
													+ question[currentItem - 1].right
													+ "</b></font>"
													+ "&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].wrong
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].weird
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].weird1
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].weird2
													+ "");
									statisticTextView[currentItem - 1]
											.setText(text);

									break;
								case 1:
									question[currentItem - 1].wrong++;
									elementResult.get(currentItem - 1)
											.text("错");
									text = Html
											.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].right
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;<font color=red><b>"
													+ question[currentItem - 1].wrong
													+ "</b></font>"
													+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].weird
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].weird1
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].weird2
													+ "");
									statisticTextView[currentItem - 1]
											.setText(text);
									break;
								case 2:
									question[currentItem - 1].weird++;
									elementResult.get(currentItem - 1).text(
											"有问题");
									text = Html
											.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].right
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].wrong
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=red><b>"
													+ question[currentItem - 1].weird
													+ "</b></font>"
													+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].weird1
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].weird2
													+ "");
									statisticTextView[currentItem - 1]
											.setText(text);
									break;
								case 3:
									question[currentItem - 1].weird1++;
									elementResult.get(currentItem - 1).text(
											"有问题1");
									text = Html
											.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].right
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].wrong
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].weird
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=red><b>"
													+ question[currentItem - 1].weird1
													+ "</b></font>"
													+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].weird2
													+ "");
									statisticTextView[currentItem - 1]
											.setText(text);
									break;
								case 4:
									question[currentItem - 1].weird2++;
									elementResult.get(currentItem - 1).text(
											"有问题2");
									text = Html
											.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].right
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].wrong
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].weird
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
													+ question[currentItem - 1].weird1
													+ ""
													+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=red><b>"
													+ question[currentItem - 1].weird2
													+ "</b></font>");
									statisticTextView[currentItem - 1]
											.setText(text);
									break;
								default:
									// question[i].weird++;
									break;

								}

								Log.i("prediction", "" + situation + " "
										+ question[7].right + "  "
										+ question[7].wrong);
								Log.i("prediction", "i" + currentItem + "+"
										+ pos[currentItem]);

								statisticTextView[currentItem - 1]
										.setVisibility(View.VISIBLE);

								statisticTextView[currentItem - 1]

								.setBackgroundColor(Color.argb(75, 99, 99, 99));

								statisticTextView[currentItem - 1]
										.setTextSize(21);
								statisticTextView[currentItem - 1]
										.setTextColor(Color.BLACK);
								// statisticTextView[currentItem -
								// 1].setBackgroundResource(R.drawable.corner_textview);
								statisticTextView[currentItem - 1]
										.setBackgroundResource(R.drawable.st);

								Log.i("sqldb", "update");
								DatabaseOp.update(Start.db, currentItem - 1,
										question[currentItem - 1].right,
										question[currentItem - 1].wrong,
										question[currentItem - 1].weird,
										question[currentItem - 1].weird1,
										question[currentItem - 1].weird2);

								break;
							} else {
								// my_toast("不在判定区域");
								continue;

							}

							// my_toast("对"+String.valueOf(question[i].right));
						}

						handler.removeCallbacks(runnable);
					}

				}

			};

			@Override
			public void onGesture(GestureOverlayView arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGestureCancelled(GestureOverlayView arg0,
					MotionEvent arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGestureEnded(GestureOverlayView overlay,
					MotionEvent event) {
				// TODO Auto-generated method stub
				yy = gestures.y;

				this.overlay = overlay;
				this.event = event;
				Log.i("prediction", "y=" + yy);
				handler.postDelayed(runnable, 1500);
//0122
			}

			@Override
			public void onGestureStarted(GestureOverlayView overlay,
					MotionEvent event) {
				// TODO Auto-generated method stub

			}

		});
*/
		if (gestureLib == null) {
			gestureLib = GestureLibraries.fromFile(mStoreFile);
			gestureLib.load();
		}
		
		
		
		/** 
		 * 加入答案提示图20190418
		 */

		ansHintIV = new ImageView(context);
		Bitmap ansBg = BitmapFactory.decodeFile("/storage/emulated/0/ansHint/demodaan.png").copy(
				Bitmap.Config.ARGB_4444, true);;
	    ansHintIV.setImageBitmap(ansBg);
		ansHintIV.setVisibility(View.GONE);
		this.addView(ansHintIV);
		
		
		

		/**
		 * 2016.12.12 zgm pigaihuan
		 */
		pigaihuanLayout = new RelativeLayout(context);

		final Dwview dwview = new Dwview(context);
		RelativeLayout.LayoutParams pigaihuanLp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		// 将实例化后的的dwview加入布局
		this.addView(pigaihuanLayout);
		pigaihuanLayout.addView(dwview, pigaihuanLp);
		pigaihuanLayout.setVisibility(View.GONE);
//       DrawView dView=new DrawView(context);
//       addV
		// cahe 2016.12.3加二级字和背景图片

		// pigaihuanSubText = new TextView(context);
		// pigaihuanSubText.setVisibility(View.GONE);
		// pigaihuanSubText.setText("二次\r\n概念\r\n不清\r\n\r\n化\r\n\r\n简\r\n\r\n灵活\r\n运用\r\n\r\n\r\n移项\r\n变号\r\n\r\n\r\n设未\r\n知数\r\n");
		// pigaihuanSubText.setTextSize(20);
		// RelativeLayout.LayoutParams pghSLp = new RelativeLayout.LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// pghSLp.topMargin=1000;
		// pghSLp.leftMargin=110;
		// pigaihuanSubText.setTextColor(Color.BLACK);
		// pigaihuanLayout.addView(pigaihuanSubText,pghSLp);

		final RelativeLayout pghBgLayout = new RelativeLayout(context);
		LayoutParams pghBgLp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		pghBgLp.alignWithParent = true;

		pghBgLp.topMargin = 1000;
		pghBgLp.leftMargin = 200;
		pghBgLp.bottomMargin = 500;
		pghBgLp.rightMargin = 200;
		pghBgLayout.setBackgroundResource(R.drawable.pghbj);

		// final DragImageView pBgImage = new DragImageView(context);
		// indexTiMu=pageNum;
		
		pBgImage = new DragAndPaintView(context);
       drawView= new DrawView(context);
		// subMenuBtnContentVersion=pBgImage.setBackGroundImage(1);
		//MyView myView=new MyView();
		
		/**
		 * 0613
		 * 
		 * 
		 */
		//MyView myView=new MyView(null, null, null, null);
		//Log.v("MyView", "MyView.bgName："+myView.bgName);
//		pBgImage.setBackGroundImage("0944-0001-0000-0023-0003-0009-0022");
		pBgImage.setBackGroundImage("0944-0001-0000-0023-0003-0009-0022");
//		pBgImage.setBackGroundImage(MyView.bgName.substring(0, MyView.bgName.length()-4));
		
	//	pBgImage.setBackGroundImage(7);
		
		// subMenuBtnContentVersion=pBgImage.indexl;
		pBgImage.invalidate();
		pBgImage.setPadding(0, 0, 0, 0);
		drawView.setScaleType(ScaleType.MATRIX);
		pBgImage.setScaleType(ScaleType.MATRIX);

		RelativeLayout.LayoutParams imLp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		imLp.topMargin = 0;
		imLp.leftMargin = 0;
		imLp.bottomMargin = 0;
		imLp.rightMargin = 0;

		// Resources respgh = getResources();
		// Bitmap pghPic = BitmapFactory.decodeResource(respgh, R.drawable.ppp);
		// pBgImage.setImageBitmap(pghPic);

		pigaihuanLayout.addView(pghBgLayout, pghBgLp);
		
		//0426批改环内添加显示学生作答时间TextView
		totalTimeTv = new TextView(context);
		RelativeLayout.LayoutParams ttttLp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	
		ttttLp.topMargin = 1800;
		ttttLp.leftMargin = 350;
		ttttLp.bottomMargin = 800;
//		ttttLp.rightMargin = 0;
		pigaihuanLayout.addView(totalTimeTv,ttttLp);
		totalTimeTv.setText("此题共用时"+Start.timePerItem+"分钟");
		totalTimeTv.setTextSize(26);
		totalTimeTv.setTextColor(Color.MAGENTA);
		totalTimeTv.setVisibility(View.VISIBLE);
		

		//0430添加点击回放按钮
		replayBtn = new Button(context);
		replayBtn.setBackgroundResource(R.drawable.replaybtnbg);
//		replayBtn.setText("笔迹回放");
		RelativeLayout.LayoutParams rbtnLp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rbtnLp.topMargin = 1750;
		rbtnLp.leftMargin = 200;
		rbtnLp.bottomMargin = 500;
		rbtnLp.rightMargin = 1250;
		
		
		
		//0507cahe暂时取消回放逻辑
		replayBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = 52;
				activity.transHandler.sendMessage(msg);
				my_toast("replay button");
			}
		});
		

		// PaintView pghPaintView = new PaintView(context, 1200, 1000);
		// pghPaintView.setBackgroundColor(Color.BLUE);
		// LayoutParams pghPVLp = new RelativeLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//0430底下两行会报错，先删掉
		pghBgLayout.addView(pBgImage, imLp);
		pghBgLayout.addView(drawView, imLp);
		// pghBgLayout.addView(pghPaintView, pghPVLp);
		pBgImage.setEnabled(true);
		drawView.setEnabled(true);
		// pghPaintView.setEnabled(true);
		savePigaihuanBitmap = Bitmap.createBitmap(1200, 1000, Config.ARGB_8888);
		pigaihuanSaveCanvas = new Canvas(savePigaihuanBitmap);

		/*
		 * 将批改环里的绘制痕迹绘制到主view上
		 */

		pigaiResultImageView = new ImageView(context);
		pigaiResultImageView.setImageBitmap(savePigaihuanBitmap);
		LayoutParams resultImgLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		resultImgLp.topMargin = 2000;
		this.addView(pigaiResultImageView, resultImgLp);

		// Myview添加批改痕迹和批改评语

		pigaiResultImageView.setVisibility(View.GONE);
		pigaihuanPingyuText = new TextView(context);
		LayoutParams pghPingyuLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		pghPingyuLp.topMargin = 2200;
		pghPingyuLp.leftMargin = 700;
		this.addView(pigaihuanPingyuText, pghPingyuLp);
		// pigaihuanPingyuText.setVisibility(View.GONE);
		pigaihuanPingyuText.setTextSize(30);
		pigaihuanPingyuText.setTextColor(Color.RED);
		pigaihuanPingyuText.setVisibility(View.GONE);
		
		// pigaihuanLayout.setBackgroundColor(Color.BLACK);
		final List<Long> clickTimes = new ArrayList<Long>();
		pigaihuanLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub

				if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {

					clickTimes.add(SystemClock.uptimeMillis());
					if (clickTimes.size() == 2) {
						if (clickTimes.get(clickTimes.size() - 1)
								- clickTimes.get(0) < 200) {
							pigaihuanLayout.setVisibility(View.GONE);

							
							
							
							
							// 批改环的痕迹保存到Myview上 cahe 2017.1.7
							pigaiResultImageView.setVisibility(VISIBLE);
							DragAndPaintView.mBitmap = Bitmap.createBitmap(
									1200, 1000, Config.ARGB_8888);
							DragAndPaintView.mPath.reset();
							DragAndPaintView.mCanvas.drawColor(
									Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

							// 批改环的评语显示到Myview上
							pigaihuanPingyuText.setVisibility(View.VISIBLE);
							String  xxxString=(String) pigaihuanPingyuText.getText();//写到xml中的批改环评语
							Log.i("cahe",xxxString);
							elementComment.get(8).text(xxxString);

							invalidate();
							// 如何双击让批改环消失的时候一起清楚批改痕迹啊清除批改痕迹啊

							try {
								FileOutputStream out = new FileOutputStream(
										"/sdcard/" + "test" + ".jpg");
								savePigaihuanBitmap.compress(
										Bitmap.CompressFormat.JPEG, 100, out);
								out.flush();
								out.close();
								Log.i("save", "已经保存");

							} catch (Exception e) {
								e.printStackTrace();
							}

							clickTimes.clear();

						} else
							clickTimes.clear();
					}

					return true;
				} else
					return true;
			}
		});

		/*
		 * 批改环的手势识别层
		 */

		pigaihuanGestures = new GestureView(context);
		pigaihuanGestures.setFadeOffset(2000); // 多笔画每两次的间隔时间
		pigaihuanGestures.setGestureColor(Color.RED);// 画笔颜色
		pigaihuanGestures.setUncertainGestureColor(Color.RED);// 未完成颜色
		// pigaihuanGestures.setBackgroundColor(Color.GRAY);
		pigaihuanGestures.setGestureStrokeWidth(1);// 画笔粗细值
		pigaihuanGestures
				.setGestureStrokeType(GestureView.GESTURE_STROKE_TYPE_MULTIPLE);
		//
		// LayoutParams pgLp = new
		// LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

		pigaihuanLayout.addView(pigaihuanGestures, pghBgLp);
		//

		pigaihuanGestures.setUncertainGestureColor(Color.RED);
		pigaihuanLayout.addView(replayBtn,rbtnLp);

		// **************************************************模拟点击事件*******************************************************************

		// final MotionEvent downEvent =
		// MotionEvent.obtain(SystemClock.uptimeMillis(),
		// SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 800, 1500, 0);
		// final MotionEvent upEvent =
		// MotionEvent.obtain(SystemClock.uptimeMillis(),
		// SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,800, 1500, 0);
		// pigaihuanGestures.onTouchEvent(downEvent);
		// pigaihuanGestures.onTouchEvent(upEvent);
		//
		// downEvent.recycle();
		// upEvent.recycle();

		pigaihuanGestures.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return pBgImage.onTouchEvent(event);

			}
		});

		// 手势识别的监听器
		pigaihuanGestures
				.addOnGestureListener(new GestureView.OnGestureListener() {

					// 2016.4.12解决不同笔画数手势问题
					int lastStrokeCount;
					public GestureOverlayView overlay;
					public MotionEvent event;
					Handler handler = new Handler();

					Runnable runnable = new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (event.getAction() == MotionEvent.ACTION_UP
									&& (overlay.getGesture().getLength() > 10)) {
								handler.postDelayed(this, 300);
								lastStrokeCount = overlay.getGesture()
										.getStrokesCount();

								// Log.i("strokeevent",
								// String.valueOf(lastStrokeCount));
								switch (lastStrokeCount) {
								case 1:

									ArrayList<Prediction> predictionswr = gestureLib
											.recognize(overlay.getGesture());
									if (predictionswr.size() > 0) {
										Prediction prediction = (Prediction) predictionswr
												.get(0);
										if (prediction.score > 2) {
											judge = prediction.name;
											if (judge.equals("Almost"))
												judge = "Right";

											/**
											 * 历史判断次数更新
											 */
											// rightCounter=rightNumber;
											rightCounter++;
											rightNumber = rightCounter;
											history1.setText("历史统计：" + "15"
													+ "\n" + "本次统计："
													+ rightCounter);
											history1.setTextSize(14);
											// judgeRsultLabel[0]=1;

											Toast.makeText(mContext,
													judge + rightNumber,
													Toast.LENGTH_SHORT).show();

										}

										else {
											judge = "ignore";
											Toast.makeText(mContext, "写字",
													Toast.LENGTH_SHORT).show();
										}

									}
									break;
								case 2:

									ArrayList<Prediction> predictions = gestureLib
											.recognize(overlay.getGesture());
									if (predictions.size() > 0) {
										Prediction prediction1 = (Prediction) predictions
												.get(0);
										if (prediction1.score > 4) {
											judge = prediction1.name;
											if (judge.equals("Right"))
												judge = "Almost";
											if(judge.equals("Almost")){

											// 统计数据更新
											halfwrong1Counter++;
											halfWrong1Number = halfwrong1Counter;
											history2.setText("历史统计：" + "4"
													+ "\n" + "本次统计："
													+ halfwrong1Counter);
											history2.setTextSize(14);
											}

											else if (judge.equals("wrong")) {
												wrongCounter++;
												wrongNumber = wrongCounter;
												history5.setText("历史统计：" + "12"
														+ "\n" + "本次统计："
														+ wrongCounter);
												history5.setTextSize(14);

											}

											Toast.makeText(mContext, judge,
													Toast.LENGTH_SHORT).show();

										}

										else
											judge = "ignore";
										Toast.makeText(mContext, "写字",
												Toast.LENGTH_SHORT).show();
									}
									// Toast.makeText(mContext, "错",
									// Toast.LENGTH_SHORT)
									// .show();
									break;
								case 3:
									judge = "错3";

									// 统计数据更新
									halfwrong2Counter++;
									halfWrong2Number = halfwrong2Counter;
									history3.setText("历史统计：" + "3" + "\n"
											+ "本次统计：" + halfwrong2Counter);
									history3.setTextSize(14);

									Toast.makeText(mContext, "半对2",
											Toast.LENGTH_SHORT).show();
									break;
								case 4:
									judge = "错4";

									// 统计数据更新
									halfwrong3Counter++;
									halfWrong3Number = halfwrong3Counter;
									history4.setText("历史统计：" + "2" + "\n"
											+ "本次统计：" + halfwrong3Counter);
									history4.setTextSize(14);

									Toast.makeText(mContext, "半对3",
											Toast.LENGTH_SHORT).show();
									break;
								default:
									lastStrokeCount = 0;
									// 统计数据更新
									// wrongCounter++;
									// wrongNumber=wrongCounter;
									// history5.setText("历史统计："+"7"+"\n"+"本次统计："+wrongCounter);
									// history5.setTextSize(14);
								}
								int situation = 5;

								if (judge.equals("Right"))
									situation = 0;
								else if (judge.equals("wrong"))
									situation = 1;
								else if (judge.equals("Almost"))
									situation = 2;
								else if (judge.equals("错3"))
									situation = 3;
								else if (judge.equals("错4"))
									situation = 4;
								/*
								 * switch(situation) case 0: break; case1 ;
								 */

								// for (currentItem = 1; currentItem <
								// totalQuestion; currentItem++) {
								// if (currentItem == 4) {
								// System.arraycopy(
								// commentStringReplacement1, 0,
								// commentString, 0, 6);
								// Log.i("whichcomment", ""
								// + SideDownMode.whichComment);
								//
								// }
								//
								// else
								// System.arraycopy(
								// commentStringReplacement2, 0,
								// commentString, 0, 6);
								//
								// Spanned text;
								//
								// }

								handler.removeCallbacks(runnable);
							}

						}

					};

					@Override
					public void onGesture(GestureOverlayView arg0,
							MotionEvent arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onGestureCancelled(GestureOverlayView arg0,
							MotionEvent arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onGestureEnded(GestureOverlayView overlay,
							MotionEvent event) {
						// TODO Auto-generated method stub
						yy = gestures.y;

						this.overlay = overlay;
						this.event = event;
						Log.i("prediction", "y=" + yy);
						handler.postDelayed(runnable, 1500);

					}

					@Override
					public void onGestureStarted(GestureOverlayView overlay,
							MotionEvent event) {
						// TODO Auto-generated method stub

					}

				});

		// if (gestureLib == null) {
		// gestureLib = GestureLibraries.fromFile(mStoreFile);
		// gestureLib.load();
		// }
		/**
		 * 2016.12.20 zgm 说明：将以上内容注释掉了，添加新代码如下
		 */
		knowledge1 = new Button(context);
		base = new Button(context);
		conceptation = new Button(context);
		memory = new Button(context);
		textbook = new Button(context);
		analyze = new Button(context);
		unstandard = new Button(context);
		trick = new Button(context);
		expression = new Button(context);
		write = new Button(context);

		

		// 添加参考答案
		// refAlmostIv = new ImageView(context);
		// Resources ansRes = getResources();
		// refAlmostIv.setImageResource(R.drawable.almosthistory);
		// pigaihuanLayout.addView(refAlmostIv);

		// *********************************************************************历史参考答案堆图标*******************************************************
		// mHits[0] = SystemClock.uptimeMillis();
		// mHits[1] = mHits[0];

		rightHistorys = new ImageView(context);
		// rightHistorys=( ImageView)findViewById(R.drawable.almosthistory1);
		almostHistorys = new ImageView(context);
		// almostHistorys=(ImageView)findViewById(R.drawable.almosthistory2);
		wrong1Historys = new ImageView(context);
		// wrong1Historys=( ImageView)findViewById(R.drawable.almosthistory3);
		wrong2Historys = new ImageView(context);
		// wrong2Historys=( ImageView)findViewById(R.drawable.almosthistory3);
		wrongHistorys = new ImageView(context);
		// wrongHistorys=( ImageView)findViewById(R.drawable.almosthistory4);
		historyImages = new ImageView[5];
		historyImages[0] = rightHistorys;
		historyImages[1] = almostHistorys;
		historyImages[2] = wrong1Historys;
		historyImages[3] = wrong2Historys;
		historyImages[4] = wrongHistorys;

		//
		for (int i = 0; i < 5; i++) {

			addImage(historyImages[i], insideLY - band - 200, insideLX + i
					* (blankx / 5), insideLY - band, insideLX + (i + 1)
					* blankx / 5, R.drawable.historys);

		}
		// *****************************************historyImages[0] 的监听器*****************************************************
		//不让弹出hlistview
		
		for (int btnCount = 0; btnCount < 5; btnCount++) {
            final int count=btnCount;
			historyImages[count].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					my_toast(String.valueOf(count));
					if (historyListViewVisibilityFlag == 0) {
						historyListViewVisibilityFlag = 1;
						historyImages[count].setBackgroundColor(0x55008b00);
						for (int i = 0; i < 5; i++) {
							if (i != count){
								hListView[i].setVisibility(GONE);
							historyImages[i].setBackgroundColor(Color.TRANSPARENT);
							}
							else {
								hListView[i].setVisibility(VISIBLE);
								historyImages[i].setVisibility(VISIBLE);
							
							}
						}
					}
					else {
						hListView[count].setVisibility(GONE);
						historyImages[count].setBackgroundColor(Color.TRANSPARENT);
						historyListViewVisibilityFlag = 0;
//						hListView[count].setAdapter(null);
						postInvalidate();
						System.gc();
						
					}
				};
			});
		}


		
		
		// **********************************************新建button对象history1F************************************************
				history1 = new Button(context);
				addButton(history1, insideLY - halfband, insideLX, insideLY, insideLX
						+ blankx / 5, judgeHistory[0], 0xfffffffe, 0x55135901);
				// 注册监听，对history1按钮按下响应
				history1.setTextSize(14);

				history1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (historyListViewVisibilityFlag == 0) {

							hListView[0].setVisibility(VISIBLE);
							hListView[1].setVisibility(GONE);
							hListView[2].setVisibility(GONE);
							hListView[3].setVisibility(GONE);
							hListView[4].setVisibility(GONE);
							Log.i("hListView", "1：" + historyListViewVisibilityFlag);
							historyListViewVisibilityFlag = 1;
							Log.i("hListView", "2：" + historyListViewVisibilityFlag);
						}
						// Toast.makeText(mContext, "你点击了历史2",
						// Toast.LENGTH_SHORT).show();
						else {
							hListView[0].setVisibility(GONE);

							Log.i("hListView", "3：" + historyListViewVisibilityFlag);
							historyListViewVisibilityFlag = 0;
						
							Log.i("hListView", "4：" + historyListViewVisibilityFlag);
						}

					};

				});

				// ****************添加图片***********************************************
				ImageView right = new ImageView(context);
				addImage(right, insideLY - band, insideLX, insideLY - halfband,
						insideLX + blankx / 5, R.drawable.s1);

				// ***************************************************************

				// ************************************************
				// *********************************************************************新建button对象history2*************************************************
				history2 = new Button(context);
				addButton(history2, insideLY - halfband, insideLX + blankx / 5,
						insideLY, insideLX + 2 * blankx / 5, judgeHistory[1],
						0xfffffffe, 0x55135901);
				history2.setTextSize(14);
		
		
		
		// 注册监听，对history2按钮按下响应
		history2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (historyListViewVisibilityFlag == 0) {

					hListView[1].setVisibility(VISIBLE);
					hListView[0].setVisibility(GONE);
					hListView[2].setVisibility(GONE);
					hListView[3].setVisibility(GONE);
					hListView[4].setVisibility(GONE);

					Log.i("hListView", "1：" + historyListViewVisibilityFlag);
					historyListViewVisibilityFlag = 1;
					Log.i("hListView", "2：" + historyListViewVisibilityFlag);
				}
				// Toast.makeText(mContext, "你点击了历史2",
				// Toast.LENGTH_SHORT).show();
				else {
					hListView[1].setVisibility(GONE);

					Log.i("hListView", "3：" + historyListViewVisibilityFlag);
					historyListViewVisibilityFlag = 0;
					Log.i("hListView", "4：" + historyListViewVisibilityFlag);
				}

			};

		});

		// ****************添加图片***********************************************
		ImageView wrong2 = new ImageView(context);
		addImage(wrong2, insideLY - band, insideLX + blankx / 5, insideLY
				- halfband, insideLX + 2 * blankx / 5, R.drawable.s2);

		// ***************************************************************
		// ***************************************************新建button对象history3******************************************************************
		history3 = new Button(context);
		addButton(history3, insideLY - halfband, insideLX + 2 * blankx / 5,
				insideLY, insideLX + 3 * blankx / 5, judgeHistory[2],
				0xfffffffe, 0x55135901);
		history3.setTextSize(14);

		// 注册监听，对history3按钮按下响应
		history3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (historyListViewVisibilityFlag == 0) {

					hListView[2].setVisibility(VISIBLE);
					hListView[0].setVisibility(GONE);
					hListView[1].setVisibility(GONE);
					hListView[3].setVisibility(GONE);
					hListView[4].setVisibility(GONE);

					Log.i("hListView", "1：" + historyListViewVisibilityFlag);
					historyListViewVisibilityFlag = 1;
					Log.i("hListView", "2：" + historyListViewVisibilityFlag);
				}
				// Toast.makeText(mContext, "你点击了历史2",
				// Toast.LENGTH_SHORT).show();
				else {
					hListView[2].setVisibility(GONE);
					Log.i("hListView", "3：" + historyListViewVisibilityFlag);
					historyListViewVisibilityFlag = 0;
					Log.i("hListView", "4：" + historyListViewVisibilityFlag);
				}

			};

		});
		// ****************添加图片***********************************************
		ImageView wrong3 = new ImageView(context);
		addImage(wrong3, insideLY - band, insideLX + 2 * blankx / 5, insideLY
				- halfband, insideLX + 3 * blankx / 5, R.drawable.s3);

		// ***************************************************************
		// &******************************************************新建button对象history4***************************************************************
		history4 = new Button(context);
		addButton(history4, insideLY - halfband, insideLX + 3 * blankx / 5,
				insideLY, insideLX + 4 * blankx / 5, judgeHistory[3],
				0xfffffffe, 0x55135901);
		history4.setTextSize(14);

		// 注册监听，对history1按钮按下响应
		history4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (historyListViewVisibilityFlag == 0) {

					hListView[3].setVisibility(VISIBLE);
					hListView[0].setVisibility(GONE);
					hListView[1].setVisibility(GONE);
					hListView[2].setVisibility(GONE);

					hListView[4].setVisibility(GONE);

					Log.i("hListView", "1：" + historyListViewVisibilityFlag);
					historyListViewVisibilityFlag = 1;
					Log.i("hListView", "2：" + historyListViewVisibilityFlag);
				}
				// Toast.makeText(mContext, "你点击了历史2",
				// Toast.LENGTH_SHORT).show();
				else {
					hListView[3].setVisibility(GONE);

					Log.i("hListView", "3：" + historyListViewVisibilityFlag);
					historyListViewVisibilityFlag = 0;
					Log.i("hListView", "4：" + historyListViewVisibilityFlag);
				}

			};

		});

		// ****************添加图片***********************************************
		ImageView wrong4 = new ImageView(context);
		addImage(wrong4, insideLY - band, insideLX + 3 * blankx / 5, insideLY
				- halfband, insideLX + 4 * blankx / 5, R.drawable.s4);

		// ***************************************************************
		// *************************************************************
		// 新建button对象history5**********************************************************
		history5 = new Button(context);
		addButton(history5, insideLY - halfband, insideLX + 4 * blankx / 5,
				insideLY, insideRX, judgeHistory[4], 0xfffffffe, 0x55135901);
		history5.setTextSize(14);

		// 注册监听，对history1按钮按下响应
		history5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (historyListViewVisibilityFlag == 0) {

					hListView[4].setVisibility(VISIBLE);
					hListView[0].setVisibility(GONE);
					hListView[1].setVisibility(GONE);
					hListView[2].setVisibility(GONE);
					hListView[3].setVisibility(GONE);

					Log.i("hListView", "1：" + historyListViewVisibilityFlag);
					historyListViewVisibilityFlag = 1;
					Log.i("hListView", "2：" + historyListViewVisibilityFlag);
				}
				// Toast.makeText(mContext, "你点击了历史2",
				// Toast.LENGTH_SHORT).show();
				else {
					hListView[4].setVisibility(GONE);

					Log.i("hListView", "3：" + historyListViewVisibilityFlag);
					historyListViewVisibilityFlag = 0;
					Log.i("hListView", "4：" + historyListViewVisibilityFlag);
				}

			};

		});

		// ****************添加图片***********************************************
		ImageView wrong = new ImageView(context);
		addImage(wrong, insideLY - band, insideLX + 4 * blankx / 5, insideLY
				- halfband, insideRX, R.drawable.s5);
		// *****************************************************智力句段**************************************************************
		// 新建button对象knowledge1
		// knowledge1 = new Button(context);
		addButton(knowledge1, insideLY, insideLX - band, insideLY + blanky / 5,
				insideLX - halfband, "知识缺陷", 0xfffffffe, 0x55135901);
		

		
		knowledge1.setTextSize(24);
		
		
		
		// 注册监听，对knowledgel按钮按下响应
		knowledge1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View V) {

				knowledge1.setBackgroundColor(click);
				base.setBackgroundColor(unclick);
				conceptation.setBackgroundColor(unclick);
				memory.setBackgroundColor(unclick);
				textbook.setBackgroundColor(unclick);
				analyze.setBackgroundColor(unclick);
				unstandard.setBackgroundColor(unclick);
				trick.setBackgroundColor(unclick);
				expression.setBackgroundColor(unclick);
				write.setBackgroundColor(unclick);

				Toast.makeText(mContext, "你点击了知识缺陷", Toast.LENGTH_SHORT).show();

				for (int i = 0; i < subMenuBtnLK_array.length; i++) {
					subMenuBtnLK_array[i].setVisibility(View.VISIBLE);
					subMenuBtnLK_array[i].bringToFront();
					subMenuBtnLK_array[i]
							.setBackgroundResource(R.drawable.textborder);

					subMenuBtnRW_array[i].setVisibility(View.GONE);
					subMenuBtnRE_array[i].setVisibility(View.GONE);
					subMenuBtnRT_array[i].setVisibility(View.GONE);
					subMenuBtnRU_array[i].setVisibility(View.GONE);
					subMenuBtnRA_array[i].setVisibility(View.GONE);

					clickedFlag = 1;
				}

				// //
				// **************************//利用handler.postDelayed延时******************************
				// Handler handler = new Handler();
				// handler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// for (int i = 0; i < subMenuBtnLK_array.length; i++) {
				// subMenuBtnLK_array[i].setVisibility(View.GONE);
				// }
				// }
				// }, subMenudelaytime);
			}
		});

		// **********************************************************************************

		// 新建button对象base
		// base = new Button(context);
		addButton(base, insideLY + blanky / 5, insideLX - band, insideLY + 2
				* blanky / 5, insideLX - halfband, "基础不实", 0xfffffffe,
				0x55135901);
		base.setTextSize(24);

		// 注册监听，对base按钮按下响应
		base.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View V) {

				knowledge1.setBackgroundColor(unclick);
				base.setBackgroundColor(click);
				conceptation.setBackgroundColor(unclick);
				memory.setBackgroundColor(unclick);
				textbook.setBackgroundColor(unclick);
				analyze.setBackgroundColor(unclick);
				unstandard.setBackgroundColor(unclick);
				trick.setBackgroundColor(unclick);
				expression.setBackgroundColor(unclick);
				write.setBackgroundColor(unclick);

				Toast.makeText(mContext, "你点击了基础不实", Toast.LENGTH_SHORT).show();

				for (int i = 0; i < subMenuBtnLB_array.length; i++) {
					subMenuBtnLB_array[i].setVisibility(View.VISIBLE);
					subMenuBtnLB_array[i].bringToFront();
					subMenuBtnLB_array[i]
							.setBackgroundResource(R.drawable.textborder);

					subMenuBtnRW_array[i].setVisibility(View.GONE);
					subMenuBtnRE_array[i].setVisibility(View.GONE);
					subMenuBtnRT_array[i].setVisibility(View.GONE);
					subMenuBtnRU_array[i].setVisibility(View.GONE);
					subMenuBtnRA_array[i].setVisibility(View.GONE);

					clickedFlag = 1;
				}

				// //
				// **************************//利用handler.postDelayed延时******************************
				// Handler handler = new Handler();
				// handler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// for (int i = 0; i < subMenuBtnLB_array.length; i++) {
				// subMenuBtnLB_array[i].setVisibility(View.GONE);
				// }
				// }
				// }, subMenudelaytime);
			}
		});

		// *********************************************************************************************

		// 新建button对象conceptation
		// conceptation = new Button(context);
		addButton(conceptation, insideLY + 2 * blanky / 5, insideLX - band,
				insideLY + 3 * blanky / 5, insideLX - halfband, "概念混淆",
				0xfffffffe, 0x55135901);
		conceptation.setTextSize(24);
		// 注册监听，对base按钮按下响应
		conceptation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {

				knowledge1.setBackgroundColor(unclick);
				base.setBackgroundColor(unclick);
				conceptation.setBackgroundColor(click);
				memory.setBackgroundColor(unclick);
				textbook.setBackgroundColor(unclick);
				analyze.setBackgroundColor(unclick);
				unstandard.setBackgroundColor(unclick);
				trick.setBackgroundColor(unclick);
				expression.setBackgroundColor(unclick);
				write.setBackgroundColor(unclick);

				// Toast.makeText(mContext, "你点击了概念混淆",
				// Toast.LENGTH_SHORT).show();
				for (int i = 0; i < subMenuBtnLC_array.length; i++) {
					subMenuBtnLC_array[i].setVisibility(View.VISIBLE);
					subMenuBtnLC_array[i].bringToFront();
					subMenuBtnLC_array[i]
							.setBackgroundResource(R.drawable.textborder);

					subMenuBtnRW_array[i].setVisibility(View.GONE);
					subMenuBtnRE_array[i].setVisibility(View.GONE);
					subMenuBtnRT_array[i].setVisibility(View.GONE);
					subMenuBtnRU_array[i].setVisibility(View.GONE);
					subMenuBtnRA_array[i].setVisibility(View.GONE);
					clickedFlag = 1;

				}

				// **************************//利用handler.postDelayed延时******************************
				// Handler handler = new Handler();
				// handler.postDelayed(new Runnable() {
				// @Override
				// public void run() {
				// for (int i = 0; i < subMenuBtnLC_array.length; i++) {
				// subMenuBtnLC_array[i].setVisibility(View.GONE);
				// }
				// }
				// }, subMenudelaytime);
			}
		});
		// **********************************************************

		// 新建button对象memory
		// memory = new Button(context);
		addButton(memory, insideLY + 3 * blanky / 5, insideLX - band, insideLY
				+ 4 * blanky / 5, insideLX - halfband, "记忆不好", 0xfffffffe,
				0x55135901);
		memory.setTextSize(24);
		// 注册监听，对base按钮按下响应
		memory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {

				knowledge1.setBackgroundColor(unclick);
				base.setBackgroundColor(unclick);
				conceptation.setBackgroundColor(unclick);
				memory.setBackgroundColor(click);
				textbook.setBackgroundColor(unclick);
				analyze.setBackgroundColor(unclick);
				unstandard.setBackgroundColor(unclick);
				trick.setBackgroundColor(unclick);
				expression.setBackgroundColor(unclick);
				write.setBackgroundColor(unclick);

				// Toast.makeText(mContext, "你点击了记忆不好",
				// Toast.LENGTH_SHORT).show();

				for (int i = 0; i < subMenuBtnLM_array.length; i++) {
					subMenuBtnLM_array[i].setVisibility(View.VISIBLE);
					subMenuBtnLM_array[i].bringToFront();
					subMenuBtnLM_array[i]
							.setBackgroundResource(R.drawable.textborder);

					subMenuBtnRW_array[i].setVisibility(View.GONE);
					subMenuBtnRE_array[i].setVisibility(View.GONE);
					subMenuBtnRT_array[i].setVisibility(View.GONE);
					subMenuBtnRU_array[i].setVisibility(View.GONE);
					subMenuBtnRA_array[i].setVisibility(View.GONE);

					clickedFlag = 1;

				}

				// **************************//利用handler.postDelayed延时******************************
				// Handler handler = new Handler();
				// handler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// for (int i = 0; i < subMenuBtnLM_array.length; i++) {
				// subMenuBtnLM_array[i].setVisibility(View.GONE);
				// }
				// }
				// }, subMenudelaytime);
			}
		});

		// **********************************************************

		// 新建button对象textbook
		// textbook = new Button(context);
		addButton(textbook, insideLY + 4 * blanky / 5, insideLX - band,
				insideRY, insideLX - halfband, "纲本不熟", 0xfffffffe, 0x55135901);
		textbook.setTextSize(24);
		// 注册监听，对base按钮按下响应
		textbook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {

				knowledge1.setBackgroundColor(unclick);
				base.setBackgroundColor(unclick);
				conceptation.setBackgroundColor(unclick);
				memory.setBackgroundColor(unclick);
				textbook.setBackgroundColor(click);
				analyze.setBackgroundColor(unclick);
				unstandard.setBackgroundColor(unclick);
				trick.setBackgroundColor(unclick);
				expression.setBackgroundColor(unclick);
				write.setBackgroundColor(unclick);

				// Toast.makeText(mContext, "你点击了纲本不熟",
				// Toast.LENGTH_SHORT).show();

				for (int i = 0; i < subMenuBtnLT_array.length; i++) {
					subMenuBtnLT_array[i].setVisibility(View.VISIBLE);
					subMenuBtnLT_array[i].bringToFront();
					subMenuBtnLT_array[i]
							.setBackgroundResource(R.drawable.textborder);

					subMenuBtnRW_array[i].setVisibility(View.GONE);
					subMenuBtnRE_array[i].setVisibility(View.GONE);
					subMenuBtnRT_array[i].setVisibility(View.GONE);
					subMenuBtnRU_array[i].setVisibility(View.GONE);
					subMenuBtnRA_array[i].setVisibility(View.GONE);

					clickedFlag = 1;
				}

				// **************************//利用handler.postDelayed延时******************************
				// Handler handler = new Handler();
				// handler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// for (int i = 0; i < subMenuBtnLT_array.length; i++) {
				// subMenuBtnLT_array[i].setVisibility(View.GONE);
				// }
				// }
				// }, subMenudelaytime);
			}
		});

		// ********************************************************请感句段*********************************************************
		// ************************************************************************************************************************

		// 新建button对象enlighten
		enlighten = new Button(context);
		addButton(enlighten, insideRY + halfband, insideLX, insideRY + band,
				insideLX + blankx / 5, "引导启发", 0xfffffffe, 0x55135901);
		enlighten.setTextSize(24);
		// 注册监听，对base按钮按下响应
		enlighten.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {

				enlighten.setBackgroundColor(click);
				incentive.setBackgroundColor(unclick);
				exception.setBackgroundColor(unclick);
				drawback.setBackgroundColor(unclick);
				method.setBackgroundColor(unclick);

				// Toast.makeText(mContext, "你点击了引导启发",
				// Toast.LENGTH_SHORT).show();

				for (int i = 0; i < subMenuBtnBE_array.length; i++) {
					subMenuBtnBE_array[i].setVisibility(View.VISIBLE);
					subMenuBtnBE_array[i].bringToFront();
					subMenuBtnBE_array[i]
							.setBackgroundResource(R.drawable.textborder);
				}
				// **************************//利用handler.postDelayed延时******************************
				// Handler handler = new Handler();
				// handler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// for (int i = 0; i < subMenuBtnBE_array.length; i++) {
				// subMenuBtnBE_array[i].setVisibility(View.GONE);
				// }
				// }
				// }, subMenudelaytime);
			}
		});
		// **********************************************************

		// 新建button对象incentive
		incentive = new Button(context);
		addButton(incentive, insideRY + halfband, insideLX + blankx / 5,
				insideRY + band, insideLX + 2 * blankx / 5, "激励奖赏", 0xfffffffe,
				0x55135901);
		incentive.setTextSize(24);
		incentive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {

				enlighten.setBackgroundColor(unclick);
				incentive.setBackgroundColor(click);
				exception.setBackgroundColor(unclick);
				drawback.setBackgroundColor(unclick);
				method.setBackgroundColor(unclick);

				// Toast.makeText(mContext, "你点击了激励奖赏",
				// Toast.LENGTH_SHORT).show();

				for (int i = 0; i < subMenuBtnBI_array.length; i++) {
					subMenuBtnBI_array[i].setVisibility(View.VISIBLE);
					subMenuBtnBI_array[i].bringToFront();
					subMenuBtnBI_array[i]
							.setBackgroundResource(R.drawable.textborder);
				}
				// **************************//利用handler.postDelayed延时******************************
				// Handler handler = new Handler();
				// handler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// for (int i = 0; i < subMenuBtnBI_array.length; i++) {
				// subMenuBtnBI_array[i].setVisibility(View.GONE);
				// }
				// }
				// }, subMenudelaytime);
			}
		});

		// **********************************************************

		// 新建button对象exception
		exception = new Button(context);
		addButton(exception, insideRY + halfband, insideLX + 2 * blankx / 5,
				insideRY + band, insideLX + 3 * blankx / 5, "提示期待", 0xfffffffe,
				0x55135901);

		// 注册监听，对base按钮按下响应
		exception.setTextSize(24);
		exception.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {

				enlighten.setBackgroundColor(unclick);
				incentive.setBackgroundColor(unclick);
				exception.setBackgroundColor(click);
				drawback.setBackgroundColor(unclick);
				method.setBackgroundColor(unclick);

				// Toast.makeText(mContext, "你点击了提示期待",
				// Toast.LENGTH_SHORT).show();

				for (int i = 0; i < subMenuBtnBEx_array.length; i++) {
					subMenuBtnBEx_array[i].setVisibility(View.VISIBLE);
					subMenuBtnBEx_array[i].bringToFront();
					subMenuBtnBEx_array[i]
							.setBackgroundResource(R.drawable.textborder);
				}
				// **************************//利用handler.postDelayed延时******************************
				// Handler handler = new Handler();
				// handler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// for (int i = 0; i < subMenuBtnBEx_array.length; i++) {
				// subMenuBtnBEx_array[i].setVisibility(View.GONE);
				// }
				// }
				// }, subMenudelaytime);
			}
		});
		// **********************************************************

		// 新建button对象drawback
		drawback = new Button(context);
		addButton(drawback, insideRY + halfband, insideLX + 3 * blankx / 5,
				insideRY + band, insideLX + 4 * blankx / 5, "缺陷挖掘", 0xfffffffe,
				0x55135901);
		drawback.setTextSize(24);
		// 注册监听，对base按钮按下响应
		drawback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {

				enlighten.setBackgroundColor(unclick);
				incentive.setBackgroundColor(unclick);
				exception.setBackgroundColor(unclick);
				drawback.setBackgroundColor(click);
				method.setBackgroundColor(unclick);

				// Toast.makeText(mContext, "你点击了缺陷挖掘",
				// Toast.LENGTH_SHORT).show();

				for (int i = 0; i < subMenuBtnBD_array.length; i++) {
					subMenuBtnBD_array[i].setVisibility(View.VISIBLE);
					subMenuBtnBD_array[i].bringToFront();
					subMenuBtnBD_array[i]
							.setBackgroundResource(R.drawable.textborder);
				}
				// **************************//利用handler.postDelayed延时******************************
				// Handler handler = new Handler();
				// handler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// for (int i = 0; i < subMenuBtnBD_array.length; i++) {
				// subMenuBtnBD_array[i].setVisibility(View.GONE);
				// }
				// }
				// }, subMenudelaytime);
			}
		});

		// *******************************************************************************

		// 新建button对象method
		method = new Button(context);
		addButton(method, insideRY + halfband, insideLX + 4 * blankx / 5,
				insideRY + band, insideRX, "方法建议", 0xfffffffe, 0x55135901);
		method.setTextSize(24);
		// 注册监听，对base按钮按下响应
		method.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {

				enlighten.setBackgroundColor(unclick);
				incentive.setBackgroundColor(unclick);
				exception.setBackgroundColor(unclick);
				drawback.setBackgroundColor(unclick);
				method.setBackgroundColor(click);

				// Toast.makeText(mContext, "你点击了方法建议",
				// Toast.LENGTH_SHORT).show();

				for (int i = 0; i < subMenuBtnBM_array.length; i++) {
					subMenuBtnBM_array[i].setVisibility(View.VISIBLE);
					subMenuBtnBM_array[i].bringToFront();
					subMenuBtnBM_array[i]
							.setBackgroundResource(R.drawable.textborder);
				}
				// **************************//利用handler.postDelayed延时******************************
				// Handler handler = new Handler();
				// handler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// for (int i = 0; i < subMenuBtnBM_array.length; i++) {
				// subMenuBtnBM_array[i].setVisibility(View.GONE);
				// }
				// }
				// }, subMenudelaytime);
			}
		});

		// *****************************************************右边表格************************************************************

		// **********************************************************

		// 新建button对象write
		// write = new Button(context);
		addButton(write, insideLY + 4 * blanky / 5, insideRX + halfband,
				insideRY, insideRX + band, "书写不好", 0xfffffffe, 0x55135901);

		// 注册监听，对base按钮按下响应
		write.setTextSize(24);
		write.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {

				knowledge1.setBackgroundColor(unclick);
				base.setBackgroundColor(unclick);
				conceptation.setBackgroundColor(unclick);
				memory.setBackgroundColor(unclick);
				textbook.setBackgroundColor(unclick);
				write.setBackgroundColor(click);
				expression.setBackgroundColor(unclick);
				trick.setBackgroundColor(unclick);
				unstandard.setBackgroundColor(unclick);
				analyze.setBackgroundColor(unclick);

				// Toast.makeText(mContext, "你点击了书写不好",
				// Toast.LENGTH_SHORT).show();

				for (int i = 0; i < subMenuBtnRW_array.length; i++) {

					subMenuBtnRW_array[i].setVisibility(View.VISIBLE);
					subMenuBtnRW_array[i].bringToFront();
					subMenuBtnRW_array[i]
							.setBackgroundResource(R.drawable.textborder);

					subMenuBtnLK_array[i].setVisibility(View.GONE);
					subMenuBtnLB_array[i].setVisibility(View.GONE);
					subMenuBtnLC_array[i].setVisibility(View.GONE);
					subMenuBtnLM_array[i].setVisibility(View.GONE);
					subMenuBtnLT_array[i].setVisibility(View.GONE);
					clickedFlag = 0;

				}
				// ****************************************加if判断是因为非智力句段在下面，直接点智力句段的内容，无法让非智力句段内容消失********************
				if (clickedFlag == 1) {

					for (int i = 0; i < subMenuBtnRW_array.length; i++) {

						subMenuBtnRW_array[i].setVisibility(View.GONE);

					}

				} else
					clickedFlag = 0;

				// **************************//利用handler.postDelayed延时******************************
				// Handler handler = new Handler();
				// handler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// for (int i = 0; i < subMenuBtnRW_array.length; i++) {
				// subMenuBtnRW_array[i].setVisibility(View.GONE);
				// }
				// }
				// }, subMenudelaytime);
			}
		});

		// **********************************************************

		// 新建button对象expression
		// expression = new Button(context);
		addButton(expression, insideLY + 3 * blanky / 5, insideRX + halfband,
				insideLY + 4 * blanky / 5, insideRX + band, "表达有误", 0xfffffffe,
				0x55135901);

		// 注册监听，对base按钮按下响应
		expression.setTextSize(24);
		expression.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {

				knowledge1.setBackgroundColor(unclick);
				base.setBackgroundColor(unclick);
				conceptation.setBackgroundColor(unclick);
				memory.setBackgroundColor(unclick);
				textbook.setBackgroundColor(unclick);
				write.setBackgroundColor(unclick);
				expression.setBackgroundColor(click);
				trick.setBackgroundColor(unclick);
				unstandard.setBackgroundColor(unclick);
				analyze.setBackgroundColor(unclick);

				// Toast.makeText(mContext, "你点击了表达有误",
				// Toast.LENGTH_SHORT).show();

				for (int i = 0; i < subMenuBtnRE_array.length; i++) {
					subMenuBtnRE_array[i].setVisibility(View.VISIBLE);
					subMenuBtnRE_array[i].bringToFront();
					subMenuBtnRE_array[i]
							.setBackgroundResource(R.drawable.textborder);

					subMenuBtnLK_array[i].setVisibility(View.GONE);
					subMenuBtnLB_array[i].setVisibility(View.GONE);
					subMenuBtnLC_array[i].setVisibility(View.GONE);
					subMenuBtnLM_array[i].setVisibility(View.GONE);
					subMenuBtnLT_array[i].setVisibility(View.GONE);
					clickedFlag = 0;

				}
				// ****************************************加if判断是因为非智力句段在下面，直接点智力句段的内容，无法让非智力句段内容消失********************
				if (clickedFlag == 1) {

					for (int i = 0; i < subMenuBtnRW_array.length; i++) {

						subMenuBtnRW_array[i].setVisibility(View.GONE);

					}

				} else
					clickedFlag = 0;

				// **************************//利用handler.postDelayed延时******************************
				// Handler handler = new Handler();
				// handler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// for (int i = 0; i < subMenuBtnRE_array.length; i++) {
				// subMenuBtnRE_array[i].setVisibility(View.GONE);
				// }
				// }
				// }, subMenudelaytime);
			}
		});
		// **************************************************************************

		// 新建button对象trick
		// trick = new Button(context);
		addButton(trick, insideLY + 2 * blanky / 5, insideRX + halfband,
				insideLY + 3 * blanky / 5, insideRX + band, "技能不熟", 0xfffffffe,
				0x55135901);

		// 注册监听，对base按钮按下响应
		trick.setTextSize(24);
		trick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {

				knowledge1.setBackgroundColor(unclick);
				base.setBackgroundColor(unclick);
				conceptation.setBackgroundColor(unclick);
				memory.setBackgroundColor(unclick);
				textbook.setBackgroundColor(unclick);
				write.setBackgroundColor(unclick);
				expression.setBackgroundColor(unclick);
				trick.setBackgroundColor(click);
				unstandard.setBackgroundColor(unclick);
				analyze.setBackgroundColor(unclick);

				// Toast.makeText(mContext, "你点击了技能不熟",
				// Toast.LENGTH_SHORT).show();

				for (int i = 0; i < subMenuBtnRT_array.length; i++) {
					subMenuBtnRT_array[i].setVisibility(View.VISIBLE);
					subMenuBtnRT_array[i].bringToFront();
					subMenuBtnRT_array[i]
							.setBackgroundResource(R.drawable.textborder);

					subMenuBtnLK_array[i].setVisibility(View.GONE);
					subMenuBtnLB_array[i].setVisibility(View.GONE);
					subMenuBtnLC_array[i].setVisibility(View.GONE);
					subMenuBtnLM_array[i].setVisibility(View.GONE);
					subMenuBtnLT_array[i].setVisibility(View.GONE);
					clickedFlag = 0;
				}
				// ****************************************加if判断是因为非智力句段在下面，直接点智力句段的内容，无法让非智力句段内容消失********************
				if (clickedFlag == 1) {

					for (int i = 0; i < subMenuBtnRW_array.length; i++) {

						subMenuBtnRW_array[i].setVisibility(View.GONE);

					}

				}

				else
					clickedFlag = 0;

				// **************************//利用handler.postDelayed延时******************************
				// Handler handler = new Handler();
				// handler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// for (int i = 0; i < subMenuBtnRT_array.length; i++) {
				// subMenuBtnRT_array[i].setVisibility(View.GONE);
				// }
				// }
				// }, subMenudelaytime);
			}
		});

		// ****************************************************************************

		// 新建button对象unstandard
		// unstandard = new Button(context);
		addButton(unstandard, insideLY + blanky / 5, insideRX + halfband,
				insideLY + 2 * blanky / 5, insideRX + band, "答欠规范", 0xfffffffe,
				0x55135901);
		unstandard.setTextSize(24);
		// 注册监听，对base按钮按下响应
		unstandard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {

				knowledge1.setBackgroundColor(unclick);
				base.setBackgroundColor(unclick);
				conceptation.setBackgroundColor(unclick);
				memory.setBackgroundColor(unclick);
				textbook.setBackgroundColor(unclick);
				write.setBackgroundColor(unclick);
				expression.setBackgroundColor(unclick);
				trick.setBackgroundColor(unclick);
				unstandard.setBackgroundColor(click);
				analyze.setBackgroundColor(unclick);

				// Toast.makeText(mContext, "你点击了答欠规范",
				// Toast.LENGTH_SHORT).show();

				for (int i = 0; i < subMenuBtnRU_array.length; i++) {
					subMenuBtnRU_array[i].setVisibility(View.VISIBLE);
					subMenuBtnRU_array[i].bringToFront();
					subMenuBtnRU_array[i]
							.setBackgroundResource(R.drawable.textborder);

					subMenuBtnLK_array[i].setVisibility(View.GONE);
					subMenuBtnLB_array[i].setVisibility(View.GONE);
					subMenuBtnLC_array[i].setVisibility(View.GONE);
					subMenuBtnLM_array[i].setVisibility(View.GONE);
					subMenuBtnLT_array[i].setVisibility(View.GONE);
					clickedFlag = 0;

				}
				// ****************************************加if判断是因为非智力句段在下面，直接点智力句段的内容，无法让非智力句段内容消失********************
				if (clickedFlag == 1) {

					for (int i = 0; i < subMenuBtnRW_array.length; i++) {

						subMenuBtnRW_array[i].setVisibility(View.GONE);

					}

				}

				else
					clickedFlag = 0;

				// **************************//利用handler.postDelayed延时******************************
				// Handler handler = new Handler();
				// handler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// for (int i = 0; i < subMenuBtnRU_array.length; i++) {
				// subMenuBtnRU_array[i].setVisibility(View.GONE);
				// }
				// }
				// }, subMenudelaytime);
			}
		});
		// ***************************************************************************
		//添加回放按钮
		
		
		
		
		
		
		// 新建button对象analyze
		// analyze = new Button(context);
		addButton(analyze, insideLY, insideRX + halfband,
				insideLY + blanky / 5, insideRX + band, "审题有误", 0xfffffffe,
				0x55135901);
		analyze.setTextSize(24);
		// 注册监听，对base按钮按下响应
		analyze.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {

				knowledge1.setBackgroundColor(unclick);
				base.setBackgroundColor(unclick);
				conceptation.setBackgroundColor(unclick);
				memory.setBackgroundColor(unclick);
				textbook.setBackgroundColor(unclick);
				write.setBackgroundColor(unclick);
				expression.setBackgroundColor(unclick);
				trick.setBackgroundColor(unclick);
				unstandard.setBackgroundColor(unclick);
				analyze.setBackgroundColor(click);

				// Toast.makeText(mContext, "你点击了审题有误",
				// Toast.LENGTH_SHORT).show();

				for (int i = 0; i < subMenuBtnRA_array.length; i++) {
					subMenuBtnRA_array[i].setVisibility(View.VISIBLE);
					subMenuBtnRA_array[i].bringToFront();
					subMenuBtnRA_array[i]
							.setBackgroundResource(R.drawable.textborder);

					subMenuBtnLK_array[i].setVisibility(View.GONE);
					subMenuBtnLB_array[i].setVisibility(View.GONE);
					subMenuBtnLC_array[i].setVisibility(View.GONE);
					subMenuBtnLM_array[i].setVisibility(View.GONE);
					subMenuBtnLT_array[i].setVisibility(View.GONE);
					clickedFlag = 0;

				}
				// ****************************************加if判断是因为非智力句段在下面，直接点智力句段的内容，无法让非智力句段内容消失********************
				if (clickedFlag == 1) {

					for (int i = 0; i < subMenuBtnRW_array.length; i++) {

						subMenuBtnRW_array[i].setVisibility(View.GONE);

					}

				}

				else
					clickedFlag = 0;

				// **************************//利用handler.postDelayed延时******************************
				// Handler handler = new Handler();
				// handler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// for (int i = 0; i < subMenuBtnRA_array.length; i++) {
				// subMenuBtnRA_array[i].setVisibility(View.GONE);
				// }
				// }
				// }, subMenudelaytime);
			}
		});
		// *************************************二级菜单按钮区**************************************************************************
		// ************************************左边二级菜单*****************************************************************************
		for (int i = 0; i < subMenuBtnLK_array.length; i++) {
			subMenuBtnLK_array[i] = new Button(context);

			addButton(subMenuBtnLK_array[i], insideLY + i * blanky / 5,
					insideLX - halfband, insideLY + (i + 1) * blanky / 5,
					insideLX, subContentString[0][i], subMenuTextColor,
					subMenuBackgroundColor);
			subMenuBtnLK_array[i].setText(subContentString[0][i]);
			subMenuBtnLK_array[i].setVisibility(View.GONE);
		}

		// cahe 测试动画ttt
		// tAniX = new TranslateAnimation(0, 50, 0, 0);
		// tAniY = new TranslateAnimation(0, 0, 0, -450);
		sideText = new TextView(context);
		bottomText = new TextView(context);
		finalTv = new TextView(context);

		LayoutParams sideLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		sideLp.leftMargin = 200;
		sideLp.topMargin = 1000;
		LayoutParams bottomLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		bottomLp.leftMargin = 250;
		bottomLp.topMargin = 1500;

		LayoutParams finaltvLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		finaltvLp.leftMargin = 800;
		// finaltvLp.topMargin = 1400;
		finaltvLp.topMargin = (int) insideRY - 500;

		pigaihuanLayout.addView(sideText, sideLp);
		pigaihuanLayout.addView(bottomText, bottomLp);
		pigaihuanLayout.addView(finalTv, finaltvLp);

		sideText.setTextSize(25);
		bottomText.setTextSize(25);
		finalTv.setTextSize(25);
		
//改个字体
		Typeface fontFace = Typeface.createFromAsset(getResources().getAssets(),"fonts/sxzt.ttf");
		finalTv.setTypeface(fontFace);
		sideText.setTypeface(fontFace);
		bottomText.setTypeface(fontFace);
		pigaihuanPingyuText.setTypeface(fontFace);
//		for(int i=0;i<totalQuestion;i++)
//		commentsTv[i].setTypeface(fontFace);

		
		
		
		

		for (int i = 0; i < subMenuBtnLK_array.length; i++) {
			final int index = i;
			subMenuBtnLK_array[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pingyuLine = subContentString[0][index];
					Toast.makeText(mContext,
							subContentString[0][index] + "index",
							Toast.LENGTH_SHORT).show();

					sideText.setText(subContentString[0][index]);
					sideText.setTextColor(Color.RED);
					for (int j = 0; j < subMenuBtnLK_array.length; j++) {
						if (j == index)
							subMenuBtnLK_array[j]
									.setBackgroundColor(Color.GRAY);
						else
							subMenuBtnLK_array[j]
									.setBackgroundResource(R.drawable.textborder);
					}

					tAniX = new TranslateAnimation(0, 0, index * 200,
							index * 200);
					// tAniY = new TranslateAnimation(0, 0, 0, -450);

					tAniX.setDuration(2000);
					tAniX.setFillAfter(true);
					Toast.makeText(mContext, "start animation",
							Toast.LENGTH_SHORT).show();
					sideText.startAnimation(tAniX);
					animateFlagX = true;

					tAniX.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub

							if (animateFlagY == true) {
								animateFlagY = false;
								bottomText.clearAnimation();
								bottomText.setVisibility(View.GONE);
								bottomText.invalidate();
								sideText.clearAnimation();
								sideText.setVisibility(View.GONE);
								sideText.invalidate();
								finalTv.setText(pingyuLine + "," + pingyuRow);
								pigaihuanPingyuText.setText(pingyuLine
										+ pingyuRow);
								finalTv.setTextColor(Color.RED);
								finalTv.setVisibility(View.VISIBLE);
								comments = pingyuLine+","+pingyuRow;
							}
						}
					});
				}
			});

		}

		for (int i = 0; i < subMenuBtnLB_array.length; i++) {
			subMenuBtnLB_array[i] = new Button(context);

			addButton(subMenuBtnLB_array[i], insideLY + i * blanky / 5,
					insideLX - halfband, insideLY + (i + 1) * blanky / 5,
					insideLX, subContentString[1][i], subMenuTextColor,
					subMenuBackgroundColor);
			subMenuBtnLB_array[i].setText(subContentString[1][i]);
			subMenuBtnLB_array[i].setVisibility(View.GONE);
		}

		// cahe 测试动画ttt

		for (int i = 0; i < subMenuBtnLB_array.length; i++) {
			final int index = i;
			subMenuBtnLB_array[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pingyuLine = subContentString[1][index];
					Toast.makeText(mContext,
							subContentString[1][index] + "index",
							Toast.LENGTH_SHORT).show();

					sideText.setText(subContentString[1][index]);
					sideText.setTextColor(Color.RED);
					for (int j = 0; j < subMenuBtnLB_array.length; j++) {
						if (j == index)
							subMenuBtnLB_array[j]
									.setBackgroundColor(Color.GRAY);
						else
							subMenuBtnLB_array[j]
									.setBackgroundResource(R.drawable.textborder);
					}

					tAniX = new TranslateAnimation(0, 0, index * 200,
							index * 200);
					// tAniY = new TranslateAnimation(0, 0, 0, -450);

					tAniX.setDuration(2000);
					tAniX.setFillAfter(true);
					Toast.makeText(mContext, "start animation",
							Toast.LENGTH_SHORT).show();
					sideText.startAnimation(tAniX);
					animateFlagX = true;

					tAniX.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub

							if (animateFlagY == true) {
								animateFlagY = false;
								bottomText.clearAnimation();
								bottomText.setVisibility(View.GONE);
								bottomText.invalidate();
								sideText.clearAnimation();
								sideText.setVisibility(View.GONE);
								sideText.invalidate();
								finalTv.setText(pingyuLine + "," + pingyuRow);
								pigaihuanPingyuText.setText(pingyuLine
										+ pingyuRow);
								finalTv.setTextColor(Color.RED);
								finalTv.setVisibility(View.VISIBLE);
								comments = pingyuLine+","+pingyuRow;
							}
						}
					});
				}
			});

		}

		for (int i = 0; i < subMenuBtnLC_array.length; i++) {
			subMenuBtnLC_array[i] = new Button(context);

			addButton(subMenuBtnLC_array[i], insideLY + i * blanky / 5,
					insideLX - halfband, insideLY + (i + 1) * blanky / 5,
					insideLX, subContentString[2][i], subMenuTextColor,
					subMenuBackgroundColor);
			subMenuBtnLC_array[i].setText(subContentString[2][i]);
			subMenuBtnLC_array[i].setVisibility(View.GONE);
		}

		// cahe 测试动画ttt

		for (int i = 0; i < subMenuBtnLC_array.length; i++) {
			final int index = i;
			subMenuBtnLC_array[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pingyuLine = subContentString[2][index];
					Toast.makeText(mContext,
							subContentString[2][index] + "index",
							Toast.LENGTH_SHORT).show();

					sideText.setText(subContentString[2][index]);
					sideText.setTextColor(Color.RED);
					for (int j = 0; j < subMenuBtnLC_array.length; j++) {
						if (j == index)
							subMenuBtnLC_array[j]
									.setBackgroundColor(Color.GRAY);
						else
							subMenuBtnLC_array[j]
									.setBackgroundResource(R.drawable.textborder);
					}

					tAniX = new TranslateAnimation(0, 0, index * 200,
							index * 200);
					// tAniY = new TranslateAnimation(0, 0, 0, -450);

					tAniX.setDuration(2000);
					tAniX.setFillAfter(true);
					Toast.makeText(mContext, "start animation",
							Toast.LENGTH_SHORT).show();
					sideText.startAnimation(tAniX);
					animateFlagX = true;

					tAniX.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub

							if (animateFlagY == true) {
								animateFlagY = false;
								bottomText.clearAnimation();
								bottomText.setVisibility(View.GONE);
								bottomText.invalidate();
								sideText.clearAnimation();
								sideText.setVisibility(View.GONE);
								sideText.invalidate();
								finalTv.setText(pingyuLine + "," + pingyuRow);
								pigaihuanPingyuText.setText(pingyuLine
										+ pingyuRow);
								finalTv.setTextColor(Color.RED);
								finalTv.setVisibility(View.VISIBLE);
								comments = pingyuLine+","+pingyuRow;
							}
						}
					});
				}
			});

		}

		for (int i = 0; i < subMenuBtnLM_array.length; i++) {
			subMenuBtnLM_array[i] = new Button(context);

			addButton(subMenuBtnLM_array[i], insideLY + i * blanky / 5,
					insideLX - halfband, insideLY + (i + 1) * blanky / 5,
					insideLX, subContentString[3][i], subMenuTextColor,
					subMenuBackgroundColor);
			subMenuBtnLM_array[i].setText(subContentString[3][i]);
			subMenuBtnLM_array[i].setVisibility(View.GONE);
		}

		// cahe 测试动画ttt

		for (int i = 0; i < subMenuBtnLM_array.length; i++) {
			final int index = i;
			subMenuBtnLM_array[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pingyuLine = subContentString[3][index];
					Toast.makeText(mContext,
							subContentString[3][index] + "index",
							Toast.LENGTH_SHORT).show();

					sideText.setText(subContentString[3][index]);
					sideText.setTextColor(Color.RED);
					for (int j = 0; j < subMenuBtnLM_array.length; j++) {
						if (j == index)
							subMenuBtnLM_array[j]
									.setBackgroundColor(Color.GRAY);
						else
							subMenuBtnLM_array[j]
									.setBackgroundResource(R.drawable.textborder);
					}

					tAniX = new TranslateAnimation(0, 0, index * 200,
							index * 200);
					// tAniY = new TranslateAnimation(0, 0, 0, -450);

					tAniX.setDuration(2000);
					tAniX.setFillAfter(true);
					Toast.makeText(mContext, "start animation",
							Toast.LENGTH_SHORT).show();
					sideText.startAnimation(tAniX);
					animateFlagX = true;

					tAniX.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub

							if (animateFlagY == true) {
								animateFlagY = false;
								bottomText.clearAnimation();
								bottomText.setVisibility(View.GONE);
								bottomText.invalidate();
								sideText.clearAnimation();
								sideText.setVisibility(View.GONE);
								sideText.invalidate();
								finalTv.setText(pingyuLine + "," + pingyuRow);
								pigaihuanPingyuText.setText(pingyuLine
										+ pingyuRow);
								finalTv.setTextColor(Color.RED);
								finalTv.setVisibility(View.VISIBLE);
								comments = pingyuLine+","+pingyuRow;
							}
						}
					});
				}
			});

		}

		for (int i = 0; i < subMenuBtnLT_array.length; i++) {
			subMenuBtnLT_array[i] = new Button(context);

			addButton(subMenuBtnLT_array[i], insideLY + i * blanky / 5,
					insideLX - halfband, insideLY + (i + 1) * blanky / 5,
					insideLX, subContentString[4][i], subMenuTextColor,
					subMenuBackgroundColor);

			subMenuBtnLT_array[i].setText(subContentString[4][i]);
			subMenuBtnLT_array[i].setVisibility(View.GONE);
		}

		// cahe 测试动画ttt

		for (int i = 0; i < subMenuBtnLT_array.length; i++) {
			final int index = i;
			subMenuBtnLT_array[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pingyuLine = subContentString[4][index];
					Toast.makeText(mContext,
							subContentString[4][index] + "index",
							Toast.LENGTH_SHORT).show();

					sideText.setText(subContentString[4][index]);
					sideText.setTextColor(Color.RED);
					for (int j = 0; j < subMenuBtnLT_array.length; j++) {
						if (j == index)
							subMenuBtnLT_array[j]
									.setBackgroundColor(Color.GRAY);
						else
							subMenuBtnLT_array[j]
									.setBackgroundResource(R.drawable.textborder);
					}

					tAniX = new TranslateAnimation(0, 0, index * 200,
							index * 200);
					// tAniY = new TranslateAnimation(0, 0, 0, -450);

					tAniX.setDuration(2000);
					tAniX.setFillAfter(true);
					Toast.makeText(mContext, "start animation",
							Toast.LENGTH_SHORT).show();
					sideText.startAnimation(tAniX);
					animateFlagX = true;

					tAniX.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub

							if (animateFlagY == true) {
								animateFlagY = false;
								bottomText.clearAnimation();
								bottomText.setVisibility(View.GONE);
								bottomText.invalidate();
								sideText.clearAnimation();
								sideText.setVisibility(View.GONE);
								sideText.invalidate();
								finalTv.setText(pingyuLine + "," + pingyuRow);
								pigaihuanPingyuText.setText(pingyuLine
										+ pingyuRow);
								finalTv.setTextColor(Color.RED);
								finalTv.setVisibility(View.VISIBLE);
								comments = pingyuLine+","+pingyuRow;
							}
						}
					});
				}
			});

		}

		// ************************************下边二级菜单*************************************************************************************************************************

		for (int i = 0; i < subMenuBtnBE_array.length; i++) {
			subMenuBtnBE_array[i] = new Button(context);

			addButton(subMenuBtnBE_array[i], insideRY, insideLX + i * blankx
					/ 5, insideRY + halfband, insideLX + (i + 1) * blankx / 5,
					subContentString[5][i], subMenuTextColor,
					subMenuBackgroundColor);
			subMenuBtnBE_array[i].setText(subContentString[5][i]);
			subMenuBtnBE_array[i].setVisibility(View.GONE);
		}

		// cahe 测试动画ttt
		for (int i = 0; i < subMenuBtnBE_array.length; i++) {
			final int index = i;
			subMenuBtnBE_array[index].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Log.i("animation", "here" + subContentString[5][index]);
					// TODO Auto-generated method stub
					pingyuRow = subContentString[5][index];
					Toast.makeText(mContext, subContentString[5][index],
							Toast.LENGTH_SHORT).show();
					subMenuBtnBE_array[index].setBackgroundColor(Color.GRAY);
					bottomText.setText(subContentString[5][index]);
					bottomText.setTextColor(Color.RED);
					for (int j = 0; j < subMenuBtnBE_array.length; j++) {
						if (j == index)
							subMenuBtnBE_array[j]
									.setBackgroundColor(Color.GRAY);
						else
							subMenuBtnBE_array[j]
									.setBackgroundResource(R.drawable.textborder);
					}

					tAniY = new TranslateAnimation(index * 200, index * 200, 0,
							-250);
					tAniY.setDuration(2000);
					tAniY.setFillAfter(true);
					bottomText.startAnimation(tAniY);
					animateFlagY = true;
					tAniY.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub
							if (animateFlagX == true) {
								animateFlagX = false;
								sideText.clearAnimation();
								sideText.setVisibility(View.GONE);
								sideText.invalidate();
								bottomText.clearAnimation();
								bottomText.setVisibility(View.GONE);
								bottomText.invalidate();
								finalTv.setText(pingyuLine + "," + pingyuRow);
								pigaihuanPingyuText.setText(pingyuLine
										+ pingyuRow);
								finalTv.setTextColor(Color.RED);
								finalTv.setVisibility(View.VISIBLE);
								comments = pingyuLine+","+pingyuRow;
							}
						}
					});

				}
			});

		}

		for (int i = 0; i < subMenuBtnBI_array.length; i++) {
			subMenuBtnBI_array[i] = new Button(context);

			addButton(subMenuBtnBI_array[i], insideRY, insideLX + i * blankx
					/ 5, insideRY + halfband, insideLX + (i + 1) * blankx / 5,
					subContentString[6][i], subMenuTextColor,
					subMenuBackgroundColor);

			subMenuBtnBI_array[i].setText(subContentString[6][i]);
			subMenuBtnBI_array[i].setVisibility(View.GONE);
		}

		// cahe 测试动画ttt
		for (int i = 0; i < subMenuBtnBI_array.length; i++) {
			final int index = i;
			subMenuBtnBI_array[index].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Log.i("animation", "here" + subContentString[6][index]);
					// TODO Auto-generated method stub
					pingyuRow = subContentString[6][index];
					Toast.makeText(mContext, subContentString[6][index],
							Toast.LENGTH_SHORT).show();
					subMenuBtnBI_array[index].setBackgroundColor(Color.GRAY);
					bottomText.setText(subContentString[6][index]);
					bottomText.setTextColor(Color.RED);
					for (int j = 0; j < subMenuBtnBI_array.length; j++) {
						if (j == index)
							subMenuBtnBI_array[j]
									.setBackgroundColor(Color.GRAY);
						else
							subMenuBtnBI_array[j]
									.setBackgroundResource(R.drawable.textborder);
					}

					tAniY = new TranslateAnimation(index * 200, index * 200, 0,
							-250);
					tAniY.setDuration(2000);
					tAniY.setFillAfter(true);
					bottomText.startAnimation(tAniY);
					animateFlagY = true;
					tAniY.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub
							if (animateFlagX == true) {
								animateFlagX = false;
								sideText.clearAnimation();
								sideText.setVisibility(View.GONE);
								sideText.invalidate();
								bottomText.clearAnimation();
								bottomText.setVisibility(View.GONE);
								bottomText.invalidate();
								finalTv.setText(pingyuLine + "," + pingyuRow);
								pigaihuanPingyuText.setText(pingyuLine
										+ pingyuRow);
								finalTv.setTextColor(Color.RED);
								finalTv.setVisibility(View.VISIBLE);
								comments = pingyuLine+","+pingyuRow;
							}
						}
					});

				}
			});

		}

		for (int i = 0; i < subMenuBtnBEx_array.length; i++) {
			subMenuBtnBEx_array[i] = new Button(context);

			addButton(subMenuBtnBEx_array[i], insideRY, insideLX + i * blankx
					/ 5, insideRY + halfband, insideLX + (i + 1) * blankx / 5,
					subContentString[7][i], subMenuTextColor,
					subMenuBackgroundColor);
			subMenuBtnBEx_array[i].setText(subContentString[7][i]);
			subMenuBtnBEx_array[i].setVisibility(View.GONE);
		}

		// cahe 测试动画ttt
		for (int i = 0; i < subMenuBtnBEx_array.length; i++) {
			final int index = i;
			subMenuBtnBEx_array[index]
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Log.i("animation", "here"
									+ subContentString[7][index]);
							// TODO Auto-generated method stub
							pingyuRow = subContentString[7][index];
							Toast.makeText(mContext,
									subContentString[7][index],
									Toast.LENGTH_SHORT).show();
							subMenuBtnBEx_array[index]
									.setBackgroundColor(Color.GRAY);
							bottomText.setText(subContentString[7][index]);
							bottomText.setTextColor(Color.RED);
							for (int j = 0; j < subMenuBtnBEx_array.length; j++) {
								if (j == index)
									subMenuBtnBEx_array[j]
											.setBackgroundColor(Color.GRAY);
								else
									subMenuBtnBEx_array[j]
											.setBackgroundResource(R.drawable.textborder);
							}

							tAniY = new TranslateAnimation(index * 200,
									index * 200, 0, -250);
							tAniY.setDuration(2000);
							tAniY.setFillAfter(true);
							bottomText.startAnimation(tAniY);
							animateFlagY = true;
							tAniY.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation arg0) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationRepeat(Animation arg0) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationEnd(Animation arg0) {
									// TODO Auto-generated method stub
									if (animateFlagX == true) {
										animateFlagX = false;
										sideText.clearAnimation();
										sideText.setVisibility(View.GONE);
										sideText.invalidate();
										bottomText.clearAnimation();
										bottomText.setVisibility(View.GONE);
										bottomText.invalidate();
										finalTv.setText(pingyuLine + ","
												+ pingyuRow);
										pigaihuanPingyuText.setText(pingyuLine
												+ pingyuRow);
										finalTv.setTextColor(Color.RED);
										finalTv.setVisibility(View.VISIBLE);
										comments = pingyuLine+","+pingyuRow;
									}
								}
							});

						}
					});

		}

		for (int i = 0; i < subMenuBtnBD_array.length; i++) {
			subMenuBtnBD_array[i] = new Button(context);

			addButton(subMenuBtnBD_array[i], insideRY, insideLX + i * blankx
					/ 5, insideRY + halfband, insideLX + (i + 1) * blankx / 5,
					subContentString[8][i], subMenuTextColor,
					subMenuBackgroundColor);
			subMenuBtnBD_array[i].setText(subContentString[8][i]);
			subMenuBtnBD_array[i].setVisibility(View.GONE);
		}

		// cahe 测试动画ttt
		for (int i = 0; i < subMenuBtnBD_array.length; i++) {
			final int index = i;
			subMenuBtnBD_array[index].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Log.i("animation", "here" + subContentString[8][index]);
					// TODO Auto-generated method stub
					pingyuRow = subContentString[8][index];
					Toast.makeText(mContext, subContentString[8][index],
							Toast.LENGTH_SHORT).show();
					subMenuBtnBD_array[index].setBackgroundColor(Color.GRAY);
					bottomText.setText(subContentString[8][index]);
					bottomText.setTextColor(Color.RED);
					for (int j = 0; j < subMenuBtnBD_array.length; j++) {
						if (j == index)
							subMenuBtnBD_array[j]
									.setBackgroundColor(Color.GRAY);
						else
							subMenuBtnBD_array[j]
									.setBackgroundResource(R.drawable.textborder);
					}

					tAniY = new TranslateAnimation(index * 200, index * 200, 0,
							-250);
					tAniY.setDuration(2000);
					tAniY.setFillAfter(true);
					bottomText.startAnimation(tAniY);
					animateFlagY = true;
					tAniY.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub
							if (animateFlagX == true) {
								animateFlagX = false;
								sideText.clearAnimation();
								sideText.setVisibility(View.GONE);
								sideText.invalidate();
								bottomText.clearAnimation();
								bottomText.setVisibility(View.GONE);
								bottomText.invalidate();
								finalTv.setText(pingyuLine + "," + pingyuRow);
								pigaihuanPingyuText.setText(pingyuLine
										+ pingyuRow);
								finalTv.setTextColor(Color.RED);
								finalTv.setVisibility(View.VISIBLE);
								comments = pingyuLine+","+pingyuRow;
							}
						}
					});

				}
			});

		}

		for (int i = 0; i < subMenuBtnBM_array.length; i++) {
			subMenuBtnBM_array[i] = new Button(context);

			addButton(subMenuBtnBM_array[i], insideRY, insideLX + i * blankx
					/ 5, insideRY + halfband, insideLX + (i + 1) * blankx / 5,
					subContentString[9][i], subMenuTextColor,
					subMenuBackgroundColor);
			subMenuBtnBM_array[i].setText(subContentString[9][i]);
			subMenuBtnBM_array[i].setVisibility(View.GONE);
		}
		// cahe 测试动画ttt
		for (int i = 0; i < subMenuBtnBM_array.length; i++) {
			final int index = i;
			subMenuBtnBM_array[index].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Log.i("animation", "here" + subContentString[9][index]);
					// TODO Auto-generated method stub
					pingyuRow = subContentString[9][index];
					Toast.makeText(mContext, subContentString[9][index],
							Toast.LENGTH_SHORT).show();
					subMenuBtnBM_array[index].setBackgroundColor(Color.GRAY);
					bottomText.setText(subContentString[9][index]);
					bottomText.setTextColor(Color.RED);
					for (int j = 0; j < subMenuBtnBM_array.length; j++) {
						if (j == index)
							subMenuBtnBM_array[j]
									.setBackgroundColor(Color.GRAY);
						else
							subMenuBtnBM_array[j]
									.setBackgroundResource(R.drawable.textborder);
					}

					tAniY = new TranslateAnimation(index * 200, index * 200, 0,
							-250);
					tAniY.setDuration(2000);
					tAniY.setFillAfter(true);
					bottomText.startAnimation(tAniY);
					animateFlagY = true;
					tAniY.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub
							if (animateFlagX == true) {
								animateFlagX = false;
								sideText.clearAnimation();
								sideText.setVisibility(View.GONE);
								sideText.invalidate();
								bottomText.clearAnimation();
								bottomText.setVisibility(View.GONE);
								bottomText.invalidate();
								finalTv.setText(pingyuLine + "," + pingyuRow);
								pigaihuanPingyuText.setText(pingyuLine
										+ pingyuRow);
								finalTv.setTextColor(Color.RED);
								finalTv.setVisibility(View.VISIBLE);
								comments = pingyuLine+","+pingyuRow;
							}
						}
					});

				}
			});

		}

		// ************************************右边二级菜单*****************************************************************************
		for (int i = 0; i < subMenuBtnRW_array.length; i++) {
			subMenuBtnRW_array[i] = new Button(context);

			addButton(subMenuBtnRW_array[i], insideLY + i * blanky / 5,
					insideRX, insideLY + (i + 1) * blanky / 5, insideRX
							+ halfband, subContentString[10][i],
					subMenuTextColor, subMenuBackgroundColor);
			subMenuBtnRW_array[i].setText(subContentString[10][i]);
			subMenuBtnRW_array[i].setVisibility(View.GONE);
		}

		// cahe 测试动画ttt

		for (int i = 0; i < subMenuBtnRW_array.length; i++) {
			final int index = i;
			subMenuBtnRW_array[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pingyuLine = subContentString[10][index];
					Toast.makeText(mContext,
							subContentString[10][index] + "index",
							Toast.LENGTH_SHORT).show();

					sideText.setText(subContentString[10][index]);
					sideText.setTextColor(Color.RED);
					for (int j = 0; j < subMenuBtnRW_array.length; j++) {
						if (j == index)
							subMenuBtnRW_array[j]
									.setBackgroundColor(Color.GRAY);
						else
							subMenuBtnRW_array[j]
									.setBackgroundResource(R.drawable.textborder);
					}

					tAniX = new TranslateAnimation(1000, 1000, index * 200,
							index * 200);
					// tAniY = new TranslateAnimation(0, 0, 0, -450);

					tAniX.setDuration(2000);
					tAniX.setFillAfter(true);
					Toast.makeText(mContext, "start animation",
							Toast.LENGTH_SHORT).show();
					sideText.startAnimation(tAniX);
					animateFlagX = true;

					tAniX.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub

							if (animateFlagY == true) {
								animateFlagY = false;
								bottomText.clearAnimation();
								bottomText.setVisibility(View.GONE);
								bottomText.invalidate();
								sideText.clearAnimation();
								sideText.setVisibility(View.GONE);
								sideText.invalidate();
								finalTv.setText(pingyuLine + "," + pingyuRow);
								pigaihuanPingyuText.setText(pingyuLine
										+ pingyuRow);
								finalTv.setTextColor(Color.RED);
								finalTv.setVisibility(View.VISIBLE);
								comments = pingyuLine+","+pingyuRow;
							}
						}
					});
				}
			});

		}

		for (int i = 0; i < subMenuBtnRE_array.length; i++) {
			subMenuBtnRE_array[i] = new Button(context);

			addButton(subMenuBtnRE_array[i], insideLY + i * blanky / 5,
					insideRX, insideLY + (i + 1) * blanky / 5, insideRX
							+ halfband, subContentString[11][i],
					subMenuTextColor, subMenuBackgroundColor);
			subMenuBtnRE_array[i].setText(subContentString[11][i]);
			subMenuBtnRE_array[i].setVisibility(View.GONE);
		}

		// cahe 测试动画ttt

		for (int i = 0; i < subMenuBtnRE_array.length; i++) {
			final int index = i;
			subMenuBtnRE_array[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pingyuLine = subContentString[11][index];
					Toast.makeText(mContext,
							subContentString[11][index] + "index",
							Toast.LENGTH_SHORT).show();

					sideText.setText(subContentString[11][index]);
					sideText.setTextColor(Color.RED);
					for (int j = 0; j < subMenuBtnRE_array.length; j++) {
						if (j == index)
							subMenuBtnRE_array[j]
									.setBackgroundColor(Color.GRAY);
						else
							subMenuBtnRE_array[j]
									.setBackgroundResource(R.drawable.textborder);
					}

					tAniX = new TranslateAnimation(1000, 1000, index * 200,
							index * 200);
					// tAniY = new TranslateAnimation(0, 0, 0, -450);

					tAniX.setDuration(2000);
					tAniX.setFillAfter(true);
					Toast.makeText(mContext, "start animation",
							Toast.LENGTH_SHORT).show();
					sideText.startAnimation(tAniX);
					animateFlagX = true;

					tAniX.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub

							if (animateFlagY == true) {
								animateFlagY = false;
								bottomText.clearAnimation();
								bottomText.setVisibility(View.GONE);
								bottomText.invalidate();
								sideText.clearAnimation();
								sideText.setVisibility(View.GONE);
								sideText.invalidate();
								finalTv.setText(pingyuLine + "," + pingyuRow);
								pigaihuanPingyuText.setText(pingyuLine
										+ pingyuRow);
								finalTv.setTextColor(Color.RED);
								finalTv.setVisibility(View.VISIBLE);
								comments = pingyuLine+","+pingyuRow;
							}
						}
					});
				}
			});

		}

		for (int i = 0; i < subMenuBtnRT_array.length; i++) {
			subMenuBtnRT_array[i] = new Button(context);

			addButton(subMenuBtnRT_array[i], insideLY + i * blanky / 5,
					insideRX, insideLY + (i + 1) * blanky / 5, insideRX
							+ halfband, subContentString[12][i],
					subMenuTextColor, subMenuBackgroundColor);
			subMenuBtnRT_array[i].setText(subContentString[12][i]);
			subMenuBtnRT_array[i].setVisibility(View.GONE);
		}

		// cahe 测试动画ttt

		for (int i = 0; i < subMenuBtnRT_array.length; i++) {
			final int index = i;
			subMenuBtnRT_array[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pingyuLine = subContentString[12][index];
					Toast.makeText(mContext,
							subContentString[12][index] + "index",
							Toast.LENGTH_SHORT).show();

					sideText.setText(subContentString[12][index]);
					sideText.setTextColor(Color.RED);
					for (int j = 0; j < subMenuBtnRT_array.length; j++) {
						if (j == index)
							subMenuBtnRT_array[j]
									.setBackgroundColor(Color.GRAY);
						else
							subMenuBtnRT_array[j]
									.setBackgroundResource(R.drawable.textborder);
					}

					tAniX = new TranslateAnimation(1000, 1000, index * 200,
							index * 200);
					// tAniY = new TranslateAnimation(0, 0, 0, -450);

					tAniX.setDuration(2000);
					tAniX.setFillAfter(true);
					Toast.makeText(mContext, "start animation",
							Toast.LENGTH_SHORT).show();
					sideText.startAnimation(tAniX);
					animateFlagX = true;

					tAniX.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub

							if (animateFlagY == true) {
								animateFlagY = false;
								bottomText.clearAnimation();
								bottomText.setVisibility(View.GONE);
								bottomText.invalidate();
								sideText.clearAnimation();
								sideText.setVisibility(View.GONE);
								sideText.invalidate();
								finalTv.setText(pingyuLine + "," + pingyuRow);
								pigaihuanPingyuText.setText(pingyuLine
										+ pingyuRow);
								finalTv.setTextColor(Color.RED);
								finalTv.setVisibility(View.VISIBLE);
								comments = pingyuLine+","+pingyuRow;
							}
						}
					});
				}
			});

		}

		for (int i = 0; i < subMenuBtnRU_array.length; i++) {
			subMenuBtnRU_array[i] = new Button(context);

			addButton(subMenuBtnRU_array[i], insideLY + i * blanky / 5,
					insideRX, insideLY + (i + 1) * blanky / 5, insideRX
							+ halfband, subContentString[13][i],
					subMenuTextColor, subMenuBackgroundColor);
			subMenuBtnRU_array[i].setText(subContentString[13][i]);
			subMenuBtnRU_array[i].setVisibility(View.GONE);
		}

		// cahe 测试动画ttt

		for (int i = 0; i < subMenuBtnRU_array.length; i++) {
			final int index = i;
			subMenuBtnRU_array[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pingyuLine = subContentString[13][index];
					Toast.makeText(mContext,
							subContentString[13][index] + "index",
							Toast.LENGTH_SHORT).show();

					sideText.setText(subContentString[13][index]);
					sideText.setTextColor(Color.RED);
					for (int j = 0; j < subMenuBtnRU_array.length; j++) {
						if (j == index)
							subMenuBtnRU_array[j]
									.setBackgroundColor(Color.GRAY);
						else
							subMenuBtnRU_array[j]
									.setBackgroundResource(R.drawable.textborder);
					}

					tAniX = new TranslateAnimation(1000, 1000, index * 200,
							index * 200);
					// tAniY = new TranslateAnimation(0, 0, 0, -450);

					tAniX.setDuration(2000);
					tAniX.setFillAfter(true);
					Toast.makeText(mContext, "start animation",
							Toast.LENGTH_SHORT).show();
					sideText.startAnimation(tAniX);
					animateFlagX = true;

					tAniX.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub

							if (animateFlagY == true) {
								animateFlagY = false;
								bottomText.clearAnimation();
								bottomText.setVisibility(View.GONE);
								bottomText.invalidate();
								sideText.clearAnimation();
								sideText.setVisibility(View.GONE);
								sideText.invalidate();
								finalTv.setText(pingyuLine + "," + pingyuRow);
								pigaihuanPingyuText.setText(pingyuLine
										+ pingyuRow);
								finalTv.setTextColor(Color.RED);
								finalTv.setVisibility(View.VISIBLE);
								comments = pingyuLine+","+pingyuRow;
							}
						}
					});
				}
			});

		}

		for (int i = 0; i < subMenuBtnRA_array.length; i++) {
			subMenuBtnRA_array[i] = new Button(context);

			addButton(subMenuBtnRA_array[i], insideLY + i * blanky / 5,
					insideRX, insideLY + (i + 1) * blanky / 5, insideRX
							+ halfband, subContentString[14][i],
					subMenuTextColor, subMenuBackgroundColor);
			subMenuBtnRA_array[i].setText(subContentString[14][i]);
			subMenuBtnRA_array[i].setVisibility(View.GONE);
		}

		// cahe 测试动画ttt

		for (int i = 0; i < subMenuBtnRA_array.length; i++) {
			final int index = i;
			subMenuBtnRA_array[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pingyuLine = subContentString[14][index];
					Toast.makeText(mContext,
							subContentString[14][index] + "index",
							Toast.LENGTH_SHORT).show();

					sideText.setText(subContentString[14][index]);
					sideText.setTextColor(Color.RED);
					for (int j = 0; j < subMenuBtnRA_array.length; j++) {
						if (j == index)
							subMenuBtnRA_array[j]
									.setBackgroundColor(Color.GRAY);
						else
							subMenuBtnRA_array[j]
									.setBackgroundResource(R.drawable.textborder);
					}

					tAniX = new TranslateAnimation(1000, 1000, index * 200,
							index * 200);
					// tAniY = new TranslateAnimation(0, 0, 0, -450);

					tAniX.setDuration(2000);
					tAniX.setFillAfter(true);
					Toast.makeText(mContext, "start animation",
							Toast.LENGTH_SHORT).show();
					sideText.startAnimation(tAniX);
					animateFlagX = true;

					tAniX.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub

							if (animateFlagY == true) {
								animateFlagY = false;
								bottomText.clearAnimation();
								bottomText.setVisibility(View.GONE);
								bottomText.invalidate();
								sideText.clearAnimation();
								sideText.setVisibility(View.GONE);
								sideText.invalidate();
								finalTv.setText(pingyuLine + "," + pingyuRow);
								pigaihuanPingyuText.setText(pingyuLine
										+ pingyuRow);
								finalTv.setTextColor(Color.RED);
								finalTv.setVisibility(View.VISIBLE);
								comments = pingyuLine+","+pingyuRow;
							}
						}
					});
				}
			});

		}

		// addImage(bigimage, 0, 0, insideRY, insideRY,
		// R.drawable.bigalmosthistory4);
		// bigimage.setVisibility(View.GONE);

		// *************************************更新nameText***********************************************************

		personalInfoDisplayLayout = new LinearLayout(context);
		nameText = new TextView(context);
		sumText = new TextView(context);
		LayoutParams nameLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// nameLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		nameLp.leftMargin = 300;
		nameLp.rightMargin = 200;
		nameLp.topMargin = 50;

		LayoutParams sumLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// nameLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		sumLp.leftMargin = 300;
		sumLp.rightMargin = 200;
		sumLp.topMargin = 200;
		
		nameText.setText("学科：数学  " + " 章节：" + keshiName + "\n学号：" + name);
		nameText.setTextSize(23);
		Log.i("addText", "add text name");
		nameText.setGravity(Gravity.LEFT);
		nameText.setTextColor(Color.BLUE);
		nameText.setBackgroundColor(Color.alpha(Color.GRAY));
		nameText.bringToFront();
		nameText.setVisibility(View.GONE);
		sumText.setVisibility(View.GONE);
		personalInfoDisplayLayout.addView(nameText, nameLp);
		personalInfoDisplayLayout.addView(sumText, sumLp);
		this.addView(personalInfoDisplayLayout, nameLp);
		
		
		//	批改环初始化显示内容
		for(int i=0;i<5;i++) {
			subMenuBtnLK_array[i].setVisibility(View.VISIBLE);
			subMenuBtnLK_array[i].setBackgroundColor(0xff18499d);
			subMenuBtnBD_array[i].setVisibility(View.VISIBLE);
			subMenuBtnBD_array[i].setBackgroundColor(0xff18499d);
			subMenuBtnRA_array[i].setVisibility(View.VISIBLE);
			subMenuBtnRA_array[i].setBackgroundColor(0xff18499d);
		}
		
		

		nameText.invalidate();
		view.postInvalidate();

		// **************************************************************跟新页数信息*****************************************************

		pageInfoDisplayLayout = new LinearLayout(context);
		pageText = new TextView(context);
		LayoutParams pageLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		pageLp.setMargins(1200, 2320, 0, 100);

		
		//0421取消作业编号
		
//		pageText.setText("" + pageNum + " / " + pageTotal);

		pageText.setVisibility(View.GONE);

		// pBgImage.setBackGroundImage(pageNum);
		pBgImage.invalidate();
		//清空画布
		pageNum++;
		// indexTiMu=pageNum;

		pageText.setTextSize(20);
		pageText.setGravity(Gravity.CENTER_VERTICAL);
		pageText.setTextColor(Color.BLACK);
		pageText.setBackgroundColor(Color.TRANSPARENT);
		pageText.bringToFront();
		pageText.setVisibility(View.GONE);
		pageInfoDisplayLayout.addView(pageText, pageLp);
		this.addView(pageInfoDisplayLayout, pageLp);

		pageText.invalidate();
		view.postInvalidate();

		staticInfoDisplayLayout = new LinearLayout(context);
		staticText = new TextView(context);
		LayoutParams staticLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		staticLp.setMargins(100, 1506, 400, 1500);

		staticText.setTextSize(20);
		staticText.setGravity(Gravity.CENTER_VERTICAL);

		staticText.setTextColor(Color.BLACK);
		staticText.setBackgroundColor(Color.TRANSPARENT);
		staticText.bringToFront();
		staticText.setVisibility(View.GONE);
		staticInfoDisplayLayout.addView(staticText, staticLp);
		this.addView(staticInfoDisplayLayout, staticLp);

		staticText.invalidate();
		view.postInvalidate();

		// *******************************************评语信息层*************************************************************************

		pingyuDisplayLayout = new LinearLayout(context);
		pingyuDisplayLayout1 = new LinearLayout(context);
		pingyuText1 = new DragImageView(context);
		pingyuText = new DragImageView(context);
		pingyuText.setScaleType(ScaleType.MATRIX);
		pingyuText1.setScaleType(ScaleType.MATRIX);
		LayoutParams pDLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		pDLp.setMargins(200, 1050, 300, 1200);
		LayoutParams pDLp1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		pDLp1.setMargins(200, 1400, 200, 400);

		Resources res = getResources();

		Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.ziti);
		Bitmap bmp2 = BitmapFactory.decodeResource(res, R.drawable.ziti1);
		pingyuText.setImageBitmap(bmp);
		pingyuText1.setImageBitmap(bmp2);

		pingyuText.setVisibility(View.GONE);
		pingyuText1.setVisibility(View.GONE);
		// pingyuText.setVisibility(View.VISIBLE);
		// pingyuText1.setVisibility(View.VISIBLE);
		LayoutParams pingyuLp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		LayoutParams pingyuLp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		pingyuDisplayLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				return view.onTouchEvent(event);
			}
		});

		// ****************************************************************历史图片的ListView**************************************************************

		File rightpicturesfiles = new File("rightpicturesfiles");
		rightpicturesPath = getFile((File) rightpicturesfiles);
		imageList[0] = rightpicturesPath;
		Log.v("zgm", "测试imageList赋值是否成功" + rightpicturesPath.length);
		File almostpicturesfiles = new File("almostpicturesfiles");
		almostpicturesPath = getFile((File) almostpicturesfiles);
		imageList[1] = almostpicturesPath;
		File wrong1picturesfiles = new File("wrong1picturesfiles");
		wrong1picturesPath = getFile((File) wrong1picturesfiles);
		imageList[2] = wrong1picturesPath;
		File wrong2picturesfiles = new File("wrong2picturesfiles");
		wrong2picturesPath = getFile((File) wrong2picturesfiles);
		imageList[3] = wrong2picturesPath;
		File wrongpicturesfiles = new File("wrongpicturesfiles");
		wrongpicturesPath = getFile((File) wrongpicturesfiles);
		imageList[4] = wrongpicturesPath;
		// Log.v("zgm", ""+rightpicturesPath.length);

		for (loopCounter = 0; loopCounter < 5; loopCounter++) {
			Log.i("historyinitstatus", "历史图片组初始化完毕ok？");
			// List<ImageView> historyImageList = new ArrayList<ImageView>();
			// initHistorylist();
			hListView[loopCounter] = new HorizontalListView(context);// 新建控件
			Log.v("zgm", "创建hlistview,&" + loopCounter);
			Log.v("zgm", "创建hlistview,&" + imageList[loopCounter].length);
			final HorizontalListViewAdapter historyImageListAdapter = new HorizontalListViewAdapter(
					mContext, imageList[loopCounter]);// 新建适配器

			RelativeLayout.LayoutParams historylistParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);// 控件布局参数

			historylistParams.topMargin = (int) (insideLY - band - 400);
			historylistParams.leftMargin = 10;
			historylistParams.rightMargin = 10;
			historylistParams.bottomMargin = (int) (2400 - (insideLY - band - 200));
			hListView[loopCounter].setAdapter(historyImageListAdapter);
			Log.v("zgm", "适配器创建成功2");
			pigaihuanLayout.addView(hListView[loopCounter], historylistParams);
			Log.v("zgm", "加载到批改环上");
		

			hListView[loopCounter].setVisibility(GONE);
		}
		bigimage = new BigImageView(context);
		bigimage.setVisibility(View.GONE);
		// **********************************************hListView[0]的点击事件***************************************************************
		hListView[0].setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.i("hisposition", "position_click" + position);
				Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT)
						.show();
				// addImage(bigimage,200, 20,
				// 1800, 20,imageList[position]);

				bigimage.setImageBitmap(BitmapFactory
						.decodeFile(imageList[0][position]));

				// bigimage.setImageResource(imageList[0][position]);
				bigimage.setVisibility(View.VISIBLE);
				// historyImageListAdapter.setSelectIndex(position);
				// historyImageListAdapter.notifyDataSetChanged();
				Log.i("hListView", "弹出大图");

			}
		});

		// **********************************************hListView[1]的点击事件***************************************************************
		hListView[1].setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.i("hisposition", "position_click" + position);
				Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT)
						.show();
				// addImage(bigimage,200, 20,
				// 1800, 20,imageList[position]);
				bigimage.setImageBitmap(BitmapFactory
						.decodeFile(imageList[1][position]));
				// bigimage.setImageResource(imageList[1][position]);
				bigimage.setVisibility(View.VISIBLE);
				// historyImageListAdapter.setSelectIndex(position);
				// historyImageListAdapter.notifyDataSetChanged();
				Log.i("hListView", "弹出大图");

			}
		});
		// **********************************************hListView[1]的点击事件***************************************************************
		hListView[2].setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.i("hisposition", "position_click" + position);
				Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT)
						.show();
				// addImage(bigimage,200, 20,
				// 1800, 20,imageList[position]);
				bigimage.setImageBitmap(BitmapFactory
						.decodeFile(imageList[2][position]));
				// bigimage.setImageResource(imageList[2][position]);
				bigimage.setVisibility(View.VISIBLE);
				// historyImageListAdapter.setSelectIndex(position);
				// historyImageListAdapter.notifyDataSetChanged();
				Log.i("hListView", "弹出大图");

			}
		});

		// **********************************************hListView[3]的点击事件***************************************************************
		hListView[3].setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.i("hisposition", "position_click" + position);
				Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT)
						.show();
				// addImage(bigimage,200, 20,
				// 1800, 20,imageList[position]);
				bigimage.setImageBitmap(BitmapFactory
						.decodeFile(imageList[3][position]));
				// bigimage.setImageResource(imageList[3][position]);
				bigimage.setVisibility(View.VISIBLE);
				// historyImageListAdapter.setSelectIndex(position);
				// historyImageListAdapter.notifyDataSetChanged();
				Log.i("hListView", "弹出大图");

			}
		});

		// **********************************************hListView[4]的点击事件***************************************************************
		hListView[4].setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.i("hisposition", "position_click" + position);
				Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT)
						.show();
				// addImage(bigimage,200, 20,
				// 1800, 20,imageList[position]);
				bigimage.setImageBitmap(BitmapFactory
						.decodeFile(imageList[4][position]));
				// bigimage.setImageResource(imageList[4][position]);
				bigimage.setVisibility(View.VISIBLE);
				// historyImageListAdapter.setSelectIndex(position);
				// historyImageListAdapter.notifyDataSetChanged();
				Log.i("hListView", "弹出大图");
				

			}
		});

		// 大图的点击事件
		bigimage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				bigimage.setVisibility(GONE);

			};

		});

		RelativeLayout.LayoutParams bigImageParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		bigImageParams.topMargin = 1000;
		 bigImageParams.leftMargin = 100;
		bigImageParams.bottomMargin = 500;
		bigImageParams.rightMargin = 100;

		pigaihuanLayout.addView(bigimage, bigImageParams);
		// pigaihuanLayout.addView(bigimage);
		// pigaihuanLayout.addView(hListView, historylistParams);

		// bigimage.setOnClickListener(l)

		pingyuDisplayLayout.addView(pingyuText, pingyuLp);
		pingyuDisplayLayout1.addView(pingyuText1, pingyuLp1);
		this.addView(pingyuDisplayLayout, pDLp);
		this.addView(pingyuDisplayLayout1, pDLp1);

		pingyuText.invalidate();
		view.postInvalidate();

		tounaoDisplayLayout = new LinearLayout(context);
		tounaoText = new DragImageView(context);
		tounaoText.setScaleType(ScaleType.MATRIX);
		LayoutParams tounaoLp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		// tounaoLp.topMargin = 100;
		// tounaoLp.leftMargin = 1000;
		Resources res1 = getResources();
		Bitmap bmp1 = BitmapFactory.decodeResource(res1, R.drawable.tounao);

		tounaoText.setImageBitmap(bmp1);

		tounaoLp.setMargins(200, 100, 200, 1800);
		// pingyuText.bringToFront();
		tounaoText.setVisibility(View.GONE);
		tounaoDisplayLayout.addView(tounaoText, tounaoLp);
		this.addView(tounaoDisplayLayout, tounaoLp);

		tounaoText.invalidate();
		view.postInvalidate();

		view.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Log.v("long", "long click!!!!!!!!!");
				return false;
			}
		});
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v("long", "click!!!!!!!!!");
			}
		});
		recordTime=new TextView(context);
		RelativeLayout.LayoutParams recordLp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 recordLp.leftMargin=650;
		 recordLp.rightMargin=700;
		 recordLp.bottomMargin=800;
		 recordLp.topMargin=1000;
//		 recordLp.height=50;
//		 recordLp.width=200;
		 recordTime.setTextSize(150);
		 recordTime.setText("5");
		 recordTime.setTextColor(0xffff0000);
		// 将实例化后的的dwview加入布局
		this.addView(recordTime,recordLp);
		 recordTime.setVisibility(View.GONE);
	}

	// *********************************************************************************************************************************************************
	// **********************************************************初始化历史图片arralist*******************************************************
	// private void initHistorylist() {
	// // TODO Auto-generated method stub
	// int firstimage = R.drawable.nanhuaijin;
	// historyImageList.add(firstimage);
	// Log.i("click", "" + R.drawable.nanhuaijin);
	// int secondimage = R.drawable.nanhuaijin_biguan;
	// historyImageList.add(secondimage);
	//
	// }

	// **********************************************************以上是初始化历史图片arralist*******************************************************
	//wsk,2018.11.23 解析xml，获得学生信息
		public static void setNameText(String chapter, String name) {
			if (chapter.equals("0021")) {

				keshiName = "第二十一章 一元一次方程";
			} else if (chapter.equals("0022")) {
				keshiName = "第二章 整式";
			} else if (chapter.equals("0023")) {
				keshiName = "第二十三章 旋转";
			}
			
//	 		String[] str = {"myxml/0944-0001-0000-0023-0000-0009-0022.xml","myxml/0945-0001-0000-0023-0000-0009-0022.xml",
//			                "myxml/0946-0001-0000-0023-0000-0009-0022.xml","myxml/0947-0001-0000-0023-0000-0009-0022.xml",
//			               "myxml/0948-0001-0000-0023-0000-0009-0022.xml","myxml/0949-0001-0000-0023-0000-0009-0022.xml"};
			String tempid[] = new String[6];//存放学生学号
	        String tempname[] = new String[6];//存放学生姓名
			String number = "";
			int i = 0;
			
			
			File file = new File("/sdcard/studentinfoxml");//路径：学生信息xml文件所在目录
			File[] files=file.listFiles();        
	     	if (files == null){Log.e("error","空目录");}        
			List<String> s = new ArrayList<String>();        
			for(int m =0;m<files.length;m++)
			{            
				s.add(files[m].getAbsolutePath());//s里存放了每一个xml文件的绝对路径        
			}
			
			
			for(int k = 0;k<s.size();k++)
	        {
				File filetemp = new File(s.get(k));
				try 
				{
	        		doc = Jsoup.parse(filetemp, "UTF-8");
	        	} 
				catch (IOException e) 
				{
	        		// TODO Auto-generated catch block
	        		e.printStackTrace();
	       		}
	        	Elements elementstudentid = doc.getElementsByTag("studentid");//根据标签读出学生学号
	        	Elements elementstudentname = doc.getElementsByTag("studentname");//根据标签读出学生姓名
	        	tempid[i] = elementstudentid.text().toString().substring(0,elementstudentid.text().toString().length());//存放学号
	        	tempname[i] = elementstudentname.text().toString().substring(0,elementstudentname.text().toString().length());//存放姓名
	        	i++;
	        }
		
			for(int j = 0;j<s.size();j++)
			{
				if(name.equals(tempid[j]))//将传入的id与读出的id比较，找出学生信息
				{
					number = name.substring(0,name.length());
					name = tempname[j].substring(0,tempname[j].length());
					//nameText.setText("学科：数学  " + " 章节：" + keshiName + "\n" + "学号：" + number + " "+ "姓名：" + name);
					break;
				}
			}
/*			if(name.equals("陈程"))number = "0944";
			else if(name.equals("王洪亮"))number = "0945";
			else if(name.equals("李亚芳"))number = "0946";
			else if(name.equals("苏星辰"))number = "0947";*/
			nameText.setText("学科：数学  " + " 章节：" + keshiName + "\n" + "学号：" + number + " "+ "姓名：" + name);
			//nameText.setText("学科：数学  " + " 章节：" + keshiName + "\n" + "姓名：" + name);
		}
		public static void setNameTextbyPageID(String sid,String name ) {
//			String number = "";
//			if(name.equals("陈程"))number = "0944";
//			else if(name.equals("王洪亮"))number = "0945";
//			else if(name.equals("李亚芳"))number = "0946";
//			else if(name.equals("苏星辰"))number = "0947";
			nameText.setText("学科：数学  " + " 章节：" + keshiName + "\n" + "学号：" + "0"+sid + " "+ "姓名：" + name+"答题时间：3分钟");
		}
/*	
public static void setNameText(String chapter, String name) {
		if (chapter.equals("0021")) {

			keshiName = "第二十一章 一元一次方程";
		} else if (chapter.equals("0022")) {
			keshiName = "第二十二章 二次函数";
		} else if (chapter.equals("0023")) {
			keshiName = "第二十三章 旋转";
		}

		if (name.equals("0944")) {
			name = "0944 姓名： 陈程";
		} else if (name.equals("0945")) {
			name = "0945 姓名： 王洪亮";
		} else if (name.equals("0946")) {
			name = "0946 姓名： 李亚芳";
		} else if (name.equals("0947")) {
			name = "0947 姓名： 蘇星辰";
		} else if (name.equals("0948")) {
			name = "0948 姓名： 路濤";
		} else if (name.equals("0949")) {
			name = "0949 姓名： 田芳";
		} else if (name.equals("0955")) {
			name = "0955 姓名： 周志飞";
		} 
		nameText.setText("学科：数学  " + " 章节：" + keshiName + "\n" + "学号：" + name);
	}
*/
	// ********************************************************************************************************************************************************************
	public static void setPageXML(String str) {
		pagecXML = str;
		Log.i("pageXML", "pageC:" + pagecXML);

	}

	public static void setPageText() {
		pageText.setText("" + pageNum + " / " + pageTotal);
		pageNum++;
	}

	public void destroy() {
		// mindmapEditFinish();
		mScreenLayerBitmap.recycle();
		BitmapCount.getInstance().recycleBitmap(
				"Calligraph destroy mScreenLayerBitmap");

		mBitmap.recycle();
		BitmapCount.getInstance().recycleBitmap("Calligraph destroy mBitmap");

		mScaleBitmap.recycle();
		BitmapCount.getInstance().recycleBitmap(
				"Calligraph destroy mScaleBitmap");

		view.destroy();
	}

	// ********************************************************************************************************************************************************************
	public Handler flipHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Log.v("flipper",
					"                      flipHandler updateHandwriteState!! ");
			if (msg.what == FlipImageView.FLIP_WHAT)
				view.cursorBitmap.updateHandwriteState();
			else
				view.cursorBitmap.updateHandwriteState();

			if (msg.what == 0) {
				// 任务队列,缩放动作，执行结束，释放屏幕外资源
				view.cursorBitmap.recycleOutScreenBitmap();
			}

			if (msg.what == FlipImageView.FLIP_UP_WHAT)
				view.cursorBitmap.updateHandwriteStateFlip();
			/*
			 * add by mouse
			 * 每次移动时，同时清空(硬笔时为mPath.reset(),毛笔时为bPointsList.clear())
			 */
			view.baseImpl.clear();
			/*
			 * add by mouse 每次滑动时，将透明层上的内容分别更新在不同的bitmap中
			 */
			// view.cursorBitmap.updateTransparent();
		}
	};

	// **********************************************************************************************************************************************************************

	Handler flipHandler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Canvas canvas = new Canvas();
			canvas.setBitmap(mBitmap);

			canvas.drawBitmap(view.cursorBitmap.bitmap, Start.SCREEN_WIDTH, 0,
					new Paint());// 不能住掉 有点用 删除时重绘底图
		}
	};

	Handler flip_Horizonal_Handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Log.v("flipper",
					"                      flip_Horizonal_Handler updateHandwriteState!! ");
			view.cursorBitmap.updateHandwriteState();

			view.invalidate();
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%% flipHandler t:"
					+ msg.what);

		}
	};

	// ***********************************************************************************************************************************************************************
	Handler upHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			flipblockLayout.setVisibility(View.VISIBLE);
			flipblockLayout.setLayout(flipblockLayout.temp);
		}
	};

	// *******************************************************************************************************************************************************************
	protected void onCreate(Context context) {
		initRecord();

		// ly
		// TestButton = new FlipImageView(Start.context,flipHandler);
		// TestButton.setBackgroundResource(R.drawable.flipblock_vertical);

		flipblockBtn = new Button(context);
		flipblockBtn.setVisibility(View.GONE);
		flipblockLayout = new HandWriteEditLayout(context, flipHandler); // 新建手写布局

		flipblockHBtn = new Button(context);
		flipblockHBtn.setVisibility(View.GONE);
		flipblockHLayout = new FlipHorizontalLayout(context,
				flip_Horizonal_Handler);

		mTemplate = WolfTemplateUtil.getCurrentTemplate(); // 得到模板

		// System.out.println("!!!!!!!!!!!name:"+ mTemplate.getName());
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(7);
		mPaint.setColor(Color.RED);

		mCanvas = new Canvas(); // 新建画布

		if (mBitmap == null) {
			// mBitmap = Bitmap.createBitmap(Start.SCREEN_WIDTH * 2,
			// Start.SCREEN_HEIGHT, Bitmap.Config.ARGB_8888);
			// ly
			// 这里为什么要乘以2呢？
			// mBitmap = Bitmap.createBitmap(Start.SCREEN_WIDTH * 2,
			// Start.SCREEN_HEIGHT, Bitmap.Config.ARGB_4444);
			mBitmap = Bitmap.createBitmap(Start.SCREEN_WIDTH * 2,
					Start.SCREEN_HEIGHT, Bitmap.Config.ARGB_4444);
			BitmapCount.getInstance().createBitmap("Calligraph Create mBitmap");
		}

		if (mScaleBitmap == null) {
			mScaleBitmap = Bitmap.createBitmap(Start.SCREEN_WIDTH * 2,
					Start.SCREEN_HEIGHT * 2, Bitmap.Config.ARGB_4444);
			BitmapCount.getInstance().createBitmap(
					"Calligraph Create mScaleBitmap");
		}

		if (mScaleTransparentBitmap == null) {
			mScaleTransparentBitmap = Bitmap.createBitmap(
					Start.SCREEN_WIDTH * 2, Start.SCREEN_HEIGHT * 2,
					Bitmap.Config.ARGB_4444);
			BitmapCount.getInstance().createBitmap(
					"Calligraph Create mScaleTransparentBitmap");
		}

		final Canvas canvas = new Canvas();
		canvas.setBitmap(mBitmap);

		if (mScreenLayerBitmap == null) {
			mScreenLayerBitmap = Bitmap.createBitmap(Start.SCREEN_WIDTH,
					Start.SCREEN_HEIGHT, Bitmap.Config.ARGB_4444);
			BitmapCount.getInstance().createBitmap(
					"Calligraph Create mScreenLayerBitmap");
		}
		// path = WolfTemplateUtil.TEMPLATE_PATH +
		// mTemplate.getName()+"/"+mTemplate.getBackground();
		bgPath = WolfTemplateUtil.getTemplateBgPath();
       Log.e("zgm","bgPath"+bgPath);
		// ly
		// test
		// 注释掉，涂鸦态不加模板
		// if(mTemplate.getFormat() == MyView.STATUS_DRAW_FREE){
		//
		// System.out.println("picPath```````````````" + bgPath);
		//
		// Bitmap bgBitmap = null;
		// try {
		// bgBitmap =
		// BitmapFactory.decodeFile(bgPath).copy(Bitmap.Config.ARGB_4444, true);
		// BitmapCount.getInstance().createBitmap("Calligraph decode bgBitmap");
		//
		// canvas.drawBitmap(bgBitmap, 0, 0, mPaint);
		// bgBitmap.recycle();
		// BitmapCount.getInstance().recycleBitmap("Calligraph onCreate bgBitmap");
		// } catch (OutOfMemoryError e) {
		// // TODO: handle exception
		// Log.e("AndroidRuntime", "Calligraph OnCreate OOM!!!");
		// }
		//
		//
		// }

		// ly
		if (mTemplate.getFormat() == MyView.STATUS_DRAW_CURSOR) {
			Bitmap mHandBgBitmap = null;

			try {
				mHandBgBitmap = BitmapFactory.decodeFile(bgPath).copy(
						Bitmap.Config.ARGB_4444, true);
				BitmapCount.getInstance().createBitmap(
						"Calligraph decode mHandBgBitmap");

				canvas.drawBitmap(mHandBgBitmap, Start.SCREEN_WIDTH, 0, mPaint);

				mHandBgBitmap.recycle();
				BitmapCount.getInstance().recycleBitmap(
						"Calligraph onCreate mHandBgBitmap");
			} catch (OutOfMemoryError e) {
				// TODO: handle exception
				Log.e("AndroidRuntime", "Calligraph OnCreate() 2 OOM!!!");
			}
		}

		// mScalBitmap:w*2,h*2,底图改成mScaleBitmap了
		// 之后又把原始的mBitmap画到了mScaleBitmap上
		// canvas.setBitmap(mScaleBitmap);
		// canvas.drawBitmap(mBitmap, new Rect(0, 0, Start.SCREEN_WIDTH,
		// Start.SCREEN_HEIGHT),
		// new Rect(0, 0, Start.SCREEN_WIDTH, Start.SCREEN_HEIGHT), new
		// Paint());

		view = new MyView(context, mBitmap, mScreenLayerBitmap, mTemplate);
		this.addView(view);
	

		undoList = new LinkedList<Command>();

		mBaseImpl = view.baseImpl;
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		mPenStatusChangeBtn = new Button(context);
		mPenStatusChangeBtn.setId(PEN_STATUS_BTN_ID);
		mPenStatusChangeBtn.setOnClickListener(new PenStatusBtnListener());

		// ---------------------------------------------------------

		LayoutParams jump_lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		jump_lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		jump_lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

		LayoutInflater inflater = (LayoutInflater) Start.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mview = inflater.inflate(R.layout.panel_main, null);
		panel = (Panel) mview.findViewById(R.id.bottomPanel);
		panel.setId(111);
		panel.setOnPanelListener(this);
		panel.setInterpolator(new ElasticInterpolator(Type.OUT, 1.0f, 0.3f));

		expend_AlarmBtn = (Button) mview.findViewById(R.id.expend_alarmBtn);
		expend_AudioBtn = (Button) mview.findViewById(R.id.expend_audioBtn);
		expend_FaceBtn = (Button) mview.findViewById(R.id.expend_faceBtn);
		expend_WeatherBtn = (Button) mview.findViewById(R.id.expend_weatherBtn);
		expend_AddpicBtn = (Button) mview.findViewById(R.id.expend_addPicBtn);
		expend_AddCameraBtn = (Button) mview
				.findViewById(R.id.expend_cameraBtn);
		expend_AddVideoBtn = (Button) mview.findViewById(R.id.expend_videoBtn);

		expend_AlarmBtn.setOnClickListener(this);
		expend_AudioBtn.setOnClickListener(this);
		expend_FaceBtn.setOnClickListener(this);
		expend_WeatherBtn.setOnClickListener(this);
		expend_AddpicBtn.setOnClickListener(this);
		expend_AddCameraBtn.setOnClickListener(this);
		expend_AddVideoBtn.setOnClickListener(this);
		this.addView(panel, jump_lp);

		// ---------------------------------------------------------

		// handwriteMenuLayout = new HandWriteEditLayout(context);
		handwriteMenuLayout = new LinearLayout(context);
		LayoutParams handwriteMenuLp = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		handwriteMenuLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		// handwriteMenuLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
		// RelativeLayout.TRUE);
		handwriteMenuLp.addRule(RelativeLayout.RIGHT_OF, 111);

		HandwriteBtnListener handwriteListener = new HandwriteBtnListener();

		LayoutParams lp_edit = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		// mHandwriteBackwardBtn = new Button(context);
		// mHandwriteBackwardBtn.setText("后退");
		// mHandwriteBackwardBtn.setOnClickListener(handwriteListener);
		// mHandwriteBackwardBtn.setVisibility(View.GONE);
		// handwriteMenuLayout.addView(mHandwriteBackwardBtn,lp_edit);
		//
		// mHandwriteForwardBtn = new Button(context);
		// mHandwriteForwardBtn.setText("前进");
		// mHandwriteForwardBtn.setVisibility(View.GONE);
		// mHandwriteForwardBtn.setOnClickListener(handwriteListener);
		// handwriteMenuLayout.addView(mHandwriteForwardBtn,lp_edit);

		mHandwriteEndofLineBtn = new Button(context);
		// mHandwriteEndofLineBtn.setText("换行");
		mHandwriteEndofLineBtn.setOnClickListener(handwriteListener);
		mHandwriteEndofLineBtn.setBackgroundResource(R.drawable.enter);
		handwriteMenuLayout.addView(mHandwriteEndofLineBtn, lp_edit);

		mHandwriteInsertSpaceBtn = new Button(context);
		// mHandwriteInsertSpaceBtn.setText("空格");
		mHandwriteInsertSpaceBtn.setOnClickListener(handwriteListener);
		mHandwriteInsertSpaceBtn.setBackgroundResource(R.drawable.space);
		handwriteMenuLayout.addView(mHandwriteInsertSpaceBtn, lp_edit);

		mHandwriteInsertEnSpaceBtn = new Button(context);
		// mHandwriteInsertSpaceBtn.setText("空格");
		mHandwriteInsertEnSpaceBtn.setOnClickListener(handwriteListener);
		mHandwriteInsertEnSpaceBtn.setBackgroundResource(R.drawable.enspace);
		handwriteMenuLayout.addView(mHandwriteInsertEnSpaceBtn, lp_edit);

		mHandWriteUndoBtn = new Button(context);
		// mUndoBtn.setText("撤销");
		mHandWriteUndoBtn.setOnClickListener(handwriteListener);
		mHandWriteUndoBtn.setBackgroundResource(R.drawable.undo);
		handwriteMenuLayout.addView(mHandWriteUndoBtn, lp_edit);

		mHandwriteDelBtn = new Button(context);
		// mHandwriteDelBtn.setText("删除");
		mHandwriteDelBtn.setOnClickListener(handwriteListener);
		mHandwriteDelBtn.setBackgroundResource(R.drawable.backspace);
		handwriteMenuLayout.addView(mHandwriteDelBtn, lp_edit);

		// ly
		// 下一张作业的图片
		mNextBtn = new Button(context);
		mNextBtn.setClickable(false);
		mNextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ly
				// 此处为点击获取下一页图片
				Start.bar.setVisibility(View.VISIBLE);
				Start.barText.setVisibility(View.VISIBLE);
				mNextBtn.setClickable(false);

				// mBitmap.eraseColor(Color.WHITE);

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						view.getNextHomework();
					}

				}).start();

				// end
			}
		});
		mNextBtn.setBackgroundResource(R.drawable.next);
		mNextBtn.setVisibility(View.INVISIBLE);
		handwriteMenuLayout.addView(mNextBtn, lp_edit);
		// end

		// mDragEnableBtn = new Button(context);
		// mDragEnableBtn.setText("画笔模式");
		// mDragEnableBtn.setVisibility(View.GONE);
		// mDragEnableBtn.setOnClickListener(handwriteListener);
		// handwriteMenuLayout.addView(mDragEnableBtn);

		this.addView(handwriteMenuLayout, handwriteMenuLp);

		LayoutParams rightLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rightLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rightLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rightBtn = new Button(Start.context);
		rightBtn.setBackgroundResource(R.drawable.rightpagedown2);
		rightBtn.setId(1);
		rightBtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				view.touchMode = view.sideDownMode;
				// Log.i("bbb","x:"+ event.getX() +" y:"+ event.getY());
				// Log.i("bbb","rawx:"+ event.getRawX() +" rawy:"+
				// event.getRawY());
				// event.setLocation(event.getX()+ 560, event.getY());
				// ly
				// 这个是右上角下滑按钮
				event.setLocation(event.getRawX() - 300, event.getRawY() - 150);
				return view.onTouchEvent(event);
			}
		});
		this.addView(rightBtn, rightLp);

		LayoutParams buttonlp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		buttonlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		buttonlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		buttonlp.topMargin = FlipImageView.TOP_LIMIT;

		// this.addView(TestButton, buttonlp);

		LayoutParams leftLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		leftLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		leftLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		leftBtn = new Button(Start.context);
		leftBtn.setBackgroundResource(R.drawable.leftpagedown2);
		leftBtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				view.touchMode = view.sideDownMode;
				// Log.i("bbb","x:"+ event.getX() +" y:"+ event.getY());
				// Log.i("bbb","rawx:"+ event.getRawX() +" rawy:"+
				// event.getRawY());
				event.setLocation(event.getX() - 40, event.getY());
				return view.onTouchEvent(event);
			}
		});
		this.addView(leftBtn, leftLp);

		// -------------------------添加竖向滑块
		LayoutParams flipblockMenuLp = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		flipblockBtn.setBackgroundResource(R.drawable.flipblock_vertical);

		flipblockMenuLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		flipblockLayout.addView(flipblockBtn, flipblockMenuLp);

		LayoutParams fliplp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		fliplp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		fliplp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

		// this.addView(flipblockLayout, fliplp);
		view.invalidate();

		// ------------------------------------------

		// -------------------------添加横向滑块
		LayoutParams flipblockHMenuLp = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		flipblockHBtn.setBackgroundResource(R.drawable.flipblock_horizontal);
		// flipblockHMenuLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		flipblockHLayout.addView(flipblockHBtn, flipblockHMenuLp);

		LayoutParams flipHlp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		flipHlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		flipHlp.bottomMargin = 70;
		flipHlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

		this.addView(flipblockHLayout, flipHlp);
		view.invalidate();

		handwriteControlLayout = new LinearLayout(context);
		LayoutParams handwriteControlLp = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		handwriteControlLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		handwriteControlLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
				RelativeLayout.TRUE);

		mHandwriteNewBtn = new Button(context);
		// mHandwriteNewBtn.setText("新建");
		mHandwriteNewBtn.setOnClickListener(handwriteListener);
		mHandwriteNewBtn.setBackgroundResource(R.drawable.newpaper);
		mHandwriteNewBtn.setId(2);
		handwriteControlLayout.addView(mHandwriteNewBtn, lp_edit);

		mMicrophoneBtn = new Button(context);
		mMicrophoneBtn.setOnClickListener(handwriteListener);
		mMicrophoneBtn.setBackgroundResource(R.drawable.microphone);
		// handwriteControlLayout.addView(mMicrophoneBtn, lp_edit);

		mDrawStatusChangeBtn = new Button(context);
		mDrawStatusChangeBtn.setOnClickListener(handwriteListener);
		mDrawStatusChangeBtn.setId(8);
		if (MyView.drawStatus == MyView.STATUS_DRAW_FREE)
			mDrawStatusChangeBtn
					.setBackgroundResource(R.drawable.status_cursorsel);
		else
			mDrawStatusChangeBtn
					.setBackgroundResource(R.drawable.status_tuyasel);
		// handwriteControlLayout.addView(mDrawStatusChangeBtn,new
		// LayoutParams(110,LayoutParams.WRAP_CONTENT));
		handwriteControlLayout.addView(mDrawStatusChangeBtn, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		LayoutParams rightDownLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		;
		rightDownLp.setMargins(1470, 2200, 0, 100);
		rightDownBtn = new Button(Start.context);
		rightDownBtn.setBackgroundResource(R.drawable.downpageup);
		this.addView(rightDownBtn, rightDownLp);

		// rightDownBtn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// // Start.instance.startActivity(new Intent(Start.context,
		// SelectPopWindow.class));
		// }
		// });
		rightDownBtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				view.touchMode = view.sideDownMode;
				Log.e("bbb", "x:" + event.getX() + " y:" + event.getY());
				Log.e("bbb",
						"rawx:" + event.getRawX() + " rawy:" + event.getRawY());
				// event.setLocation(event.getX()+ 560, event.getY());
				// ly
				// 这个是右上角下滑按钮
				// my_toast("touch");
				event.setLocation(event.getRawX() - 300, event.getRawY());
				return view.onTouchEvent(event);
			}
		});

		mCameraBtn = new Button(context);
		mCameraBtn.setOnClickListener(handwriteListener);
		mCameraBtn.setBackgroundResource(R.drawable.camera);
		// handwriteControlLayout.addView(mCameraBtn, lp_edit);
		this.addView(handwriteControlLayout, handwriteControlLp);

		if (MyView.penStatus == MyView.STATUS_PEN_CALLI)
			penText = "Hard Pen"; // 硬笔
		else
			penText = "Brush Pen"; // 毛笔

		if (MyView.drawStatus == MyView.STATUS_DRAW_FREE) {
			drawText = "Hand Write"; // 光标
		} else {
			drawText = "Free Draw"; // 涂鸦
		}

		// mDrawStatusChangeBtn.setText(drawText);
		mPenStatusChangeBtn.setText(penText);

		initPopupWindow();
	}

	void addNewPage() {
		// view.saveFile(mBitmap, MyView.FILE_PATH_HEADER + "/add1.png","PNG");
		// Log.i(TAG, "handwrite new line");
		// if(Start.PAGENUM <= Start.totlePageNum){
		// 不保存，页数就不增加
		// 当前页小于等于总页数，当前没有修改就不回保存，也应改新建一页
		// Start.PAGENUM++;不应改是当前页加1，造成混乱，应该变到总页数加1
		// if(view.saveDatebase())
		// Start.PAGENUM = Start.totlePageNum + 2;
		// else
		// Start.PAGENUM = Start.totlePageNum + 1;

		// ly
		// 刷新涂鸦态底图前的准备
		// bad
		if (view.drawStatus == MyView.STATUS_DRAW_FREE) {
			view.changeStateAndSync(MyView.STATUS_DRAW_CURSOR);
			view.changeStateAndSync(MyView.STATUS_DRAW_FREE);
		}
		// end

		// caoheng 2015.11.24不让乱保存
		// view.saveDatebase();
		// Start.c.view.saveDrawLine();
		Start.resetTotalPagenum();
		Start.PAGENUM = Start.totlePageNum + 1;
		File newDir = new File(Start.getStoragePath() + "/calldir/free_"
				+ Start.getPageNum());
		if (!newDir.exists()) {
			newDir.mkdir();
		}
		// }

		ScaleSave.getInstance().newPage();
//		view.freeBitmap.resetFreeBitmapList();//这里应该刷新？

		mScaleTransparentBitmap.eraseColor(Color.TRANSPARENT);// 清理涂鸦态底图
		for (int i = 0; i < view.cursorBitmap.listEditableCalligraphy.size(); i++) {
			view.cursorBitmap.listEditableCalligraphy.get(i)
					.resetCurrentCount();
		}
		view.cursorBitmap.clearDataBitmap();

		view.doChangeBackground(WolfTemplateUtil.getCurrentTemplate().getName());

		Canvas canvas = new Canvas();
		bgPath = WolfTemplateUtil.getTemplateBgPath();

		// Bitmap mHandBgBitmap =
		// BitmapFactory.decodeFile(bgPath).copy(Bitmap.Config.ARGB_4444, true);
		Bitmap mHandBgBitmap = null;
		try {
			mHandBgBitmap = BitmapFactory.decodeFile(bgPath).copy(
					Bitmap.Config.ARGB_4444, true);
			BitmapCount.getInstance().createBitmap(
					"Calligraph decode mHandBgBitmap");

			canvas.setBitmap(view.cursorBitmap.bitmap);
			canvas.drawBitmap(mHandBgBitmap, 0, 0, mPaint);
			//
			canvas.setBitmap(mBitmap);
			canvas.drawBitmap(mHandBgBitmap, 0, 0, mPaint);
			canvas.drawBitmap(mHandBgBitmap, Start.SCREEN_WIDTH, 0, mPaint);

			// canvas.setBitmap(view.cursorBitmap.transparentBitmap);
			// canvas.drawBitmap(mHandBgBitmap, 0, 0, mPaint);
			// //切换背景，第一个字保留上一张背景，所以住掉。

			// view.saveFile(view.cursorBitmap.transparentBitmap, "scale.jpg");
			// view.cursorBitmap.getTopBitmap().eraseColor(Color.TRANSPARENT);
			mHandBgBitmap.recycle();
			BitmapCount.getInstance().recycleBitmap(
					"Calligraph onCreate mHandBgBitmap");
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			Log.e("AndroidRuntime", "Calligraph addNewPage() OOM!!!");
		}

		// view.cursorBitmap.cal_current.clear();
		// Canvas canvas = new Canvas();
		// canvas.setBitmap(mBitmap);
		// path = WolfTemplateUtil.TEMPLATE_PATH +
		// view.mTemplate.getName()+"/"+view.mTemplate.getBackground();
		// System.out.println("================bgPath:"+path);
		// path = "/sdcard/template/diary/diary_bg.png";
		// Bitmap bgBitmap =
		// BitmapFactory.decodeFile("/sdcard/template/notebook_add_bg.png");
		// // if(view.mTemplate.getFormat() == 0){
		// // //0:涂鸦 1:光标
		// //// canvas.drawBitmap(bgBitmap, 0, 0, mPaint);
		// // }else{
		// canvas.drawBitmap(bgBitmap, Start.SCREEN_WIDTH, 0,
		// view.baseBitmap.paint);
		//
		// // }
		// bgBitmap.recycle();

		EditableCalligraphy.flip_bottom = 800;
		EditableCalligraphy.flip_dst = 0;

		EditableCalligraphy.flip_Horizonal_bottom = Start.SCREEN_WIDTH;
		EditableCalligraphy.flip_Horizonal_dst = 0;
		Start.resetDate();
		view.cursorBitmap.initDate(WolfTemplateUtil.getCurrentTemplate());

		flipblockBtn.setVisibility(View.GONE);
		view.cursorBitmap.updateHandwriteState();
		view.invalidate();

		ImageLimit.instance().resetImageCount();
		WordLimit.getInstance().resetWordCount();

		// ly
		// 新建涂鸦态之后要加入一个背景图
		view.isLoad = false;
//		Log.e("zgm","123————addNewPage");
		view.addFreeBg(0);//如果是第一次加载，第一页对应数组中的0项，没问题，如果是翻页动作，输入任何在范围内的参数都不影响应该显示的页
		// end

		// ly
		// 新建完成之后把涂鸦态背景图刷出来
		if (view.drawStatus == MyView.STATUS_DRAW_FREE)
			view.freeBitmap.drawFreeBitmapSync();
		// end

	}

	public void mindmapEditFinish() {
		if (((HandWriteMode) view.getTouchMode()).isMindMapEditableStatus()) {
			view.cursorBitmap.endMindmapEdit();
			((HandWriteMode) view.getTouchMode()).setMindMapEditStatusFalse();

			// view.cursorBitmap.insertEndOfLine();
			view.cursorBitmap.endInsertOfLine();
		}
	}

	public void changeMindmapStatus() {
		if (view.getTouchMode() instanceof HandWriteMode) {
			if (((HandWriteMode) view.getTouchMode()).isMindMapEditableStatus()) {
				LogUtil.getInstance().e("mindmap", "mindmap edit finish");
				mindmapEditFinish();
				setNotMindMapStatus();
			} else {
				setMindMapStatus();

				// ly
				// 刷屏
				view.mBitmap.eraseColor(Color.WHITE);
				// end

				view.cursorBitmap.addNewMindMap();
			}
		}
	}

	public void setMindMapStatus() {
		mHandwriteInsertEnSpaceBtn.setBackgroundResource(R.drawable.enspace_in);
	}

	public void setNotMindMapStatus() {
		mHandwriteInsertEnSpaceBtn.setBackgroundResource(R.drawable.enspace);
	}

	class HandwriteBtnListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (MyView.drawStatus == MyView.STATUS_DRAW_CURSOR) {
				try {// zk20121109
					if (v == mHandwriteDelBtn) {
						// Log.i(TAG, "handwrite del character");

						// ly
						// 清一下屏吧
						view.mBitmap.eraseColor(Color.WHITE);
						// view.mBitmap.eraseColor(Color.RED);
						// end

						view.cursorBitmap.delCharacter();
						view.cursorBitmap.cal_current.setFlipDst(true,
								"HandwriteBtnListener");
						Start.status.modified("delete");
						WorkQueue.getInstance().endFlipping();

					}
					if (v == mHandwriteBackwardBtn) {
						Log.i(TAG, "handwrite back");
						view.cursorBitmap.backward();
					}
					if (v == mHandwriteForwardBtn) {
						Log.i(TAG, "handwrite front");
						view.cursorBitmap.forward();
					}
					if (v == mHandwriteInsertSpaceBtn) {
						Log.i(TAG, "handwrite insert space");
						Start.status.modified("insert space");
						view.cursorBitmap.insertSpace();
					}
					if (v == mHandwriteInsertEnSpaceBtn) {
						/*
						 * 导图按钮 // Log.i(TAG, "handwrite insert space"); //
						 * view.cursorBitmap.insertSpace();
						 * view.cursorBitmap.insertEnSpace();
						 * Start.status.modified("insert space");
						 */
						changeMindmapStatus();

					}
					if (v == mHandwriteEndofLineBtn) {
						// Log.i(TAG, "handwrite new line");
						// if(!view.cursorBitmap.endofLine)
						// Start.saveHandler.sendEmptyMessage(0);
						view.cursorBitmap.insertEndOfLine();
						Start.status.modified("insert EndofLine");
					}

					if (v == mHandWriteUndoBtn) {
						// * 暂时改做翰林算子button
						Log.i(TAG, "handwrite new undo");
						int len = undoList.size();
						if (len > 0) {
							undoList.get(len - 1).undo(mBitmap);
							undoList.remove(len - 1);
							view.cursorBitmap.updateHandwriteState();// 将刷新图片的工作交给图片的持有者来做
							Start.status.modified("undo");
						} else
							Toast.makeText(mContext, "No Need To Undo",
									Toast.LENGTH_SHORT).show();
					}

				} catch (Exception e) {

				}
			}
			// 2015.11.11 caoheng
			if (MyView.drawStatus == MyView.STATUS_DRAW_FREE) {
				if (v == mHandwriteInsertSpaceBtn) {
					Log.e("addmorepic", "add next pic.");
					// view.saveDatebase();
//					view.addNextPic(); 0426
					//在这里清除/xyz和/-1
					deleteDir("/sdcard/xyz/");
					deleteDir("/sdcard/-1/");
					
				}
				if (v == mHandwriteEndofLineBtn) {
					Log.e("addmorepic", "add previous pic.");
					view.addPreviousPic();
					Toast.makeText(mContext, "上一张", Toast.LENGTH_SHORT).show();
				}
				if (v == mHandwriteInsertEnSpaceBtn) {
					sceneSituation = 1;
					doDownLoadTask(mContext);
//					ansHintIV.setVisibility(View.VISIBLE);
					nameText.setText("               获取待批改作业中");
					nameText.setVisibility(View.VISIBLE);
					
					invalidate();
					System.gc();
					
				}

				if (v == mHandwriteDelBtn) {
					
					//0506
//					ansHintIV.setVisibility(View.GONE);
//					Calligraph.pageTotal  = Calligraph.getFileNumber("/sdcard/xyz/");
//					Calligraph.pageText.setText("共"+Calligraph.pageTotal+"份");
//					Calligraph.pageText.setVisibility(View.VISIBLE);
					deleteDir("/sdcard/-1/");
					sceneSituation = 2;
//					transParentStatisticLayout.removeAllViews();
//					transParentStatisticLayout.addView(statisticTextView[3]);
//					statisticTextView[3].setVisibility(View.VISIBLE);
					
//					Log.i("sqldb", "clc database");
//					DatabaseOp.clcDatabase(Start.db);
//					Toast.makeText(mContext, "订正完成一人",
//							Toast.LENGTH_SHORT).show();
//					unrevisedCount--;
//					if(unrevisedCount<=0)unrevisedCount=0;
//					Spanned text1;
//					text1 = Html
//							.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
//									+ question[currentItem - 1].right
//									+ ""
//									+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
//									+ question[currentItem - 1].wrong
//									+ ""
//									+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
//									+ unrevisedCount +"/"+question[currentItem - 1].weird
//									+ ""
//									+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
//									+ question[currentItem - 1].weird1
//									+ ""
//									+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=white><b>"
//									+ question[currentItem - 1].weird2
//									+ "</b></font>");
//					statisticTextView[7].setText(text1);
					
					
					
					
					
					
					

					// Log.i("addIndex", "show pic index.");
					// // view.addPreviousPic();
					// // Toast.makeText(mContext, "show index",
					// // Toast.LENGTH_SHORT).show();
					// AlertDialog.Builder builder = new AlertDialog.Builder(
					// view.getContext());
					// builder.setTitle("二十章正确率统计");
					// builder.setMessage("1：对 3  错  0  半对  0\n"
					// + "2：对 2  错  1  半对  0\n" + "3：对 1  错  1  半对  1\n"
					// + "4：对 1  错  1  半对  1\n" + "5：对 1  错  2  半对  0\n");
					//
					// builder.setPositiveButton("提交批改",
					// new DialogInterface.OnClickListener() {
					//
					// @Override
					// public void onClick(DialogInterface arg0,
					// int arg1) {
					// // TODO Auto-generated method stub
					// Toast.makeText(view.getContext(), "批改已提交.",
					// Toast.LENGTH_SHORT).show();
					// view.DemoChangeBg1();
					// arg0.cancel();
					//
					// }
					// });
					//
					// builder.create().show();
				}

				if (v == mHandWriteUndoBtn) {
					// * 暂时改做翰林算子button

					Log.i("slide", "undo to change mode");
					Toast.makeText(mContext, "保存", Toast.LENGTH_SHORT).show();
					v.invalidate();
					saveScreen();
//					File uploadFile  = new File("/sdcard/" + view.pageXML + ".png");
//					Toast.makeText(mContext, "/sdcard/" + view.pageXML + ".png"+"    "+uploadFile, Toast.LENGTH_SHORT).show();
//					UploadUtil.uploadFile(uploadFile, "http://192.168.1.111/jxyv1/index.php/Home/Index/checkedHomeWorkUpload/filename/"+view.pageXML + ".png");
					v.invalidate();

					// v.postInvalidate();
					// caoheng, 11.28,undo按钮切换操作模式：滑屏<->画笔

					// if(GESTURE_MODE == GESTURE_MODE_ON) {
					// GESTURE_MODE = GESTURE_MODE_OFF;
					// // view.hardImpl.paint.setColor(Color.RED);
					// } else {
					// GESTURE_MODE = GESTURE_MODE_ON;
					// //取消画笔````
					// view.changePenState(MyView.STATUS_PEN_HARD);
					// view.hardImpl.paint.setAlpha(100);
					//
					// }

					// Log.i("slide", "gesture mode = " + GESTURE_MODE);

				}

			}
			if(v == mHandwriteNewBtn) 
			{
				sumText.setVisibility(View.VISIBLE);
				sumText.setTextSize(23);
				sumText.setTextColor(Color.RED);
				sumText.setText("总分:"+sum);
				sum = 100;
			}
			

			// caoheng 1.13 新建按钮弹出listView
			// if(v == mHandwriteNewBtn) {
			//
			// //ly
			// //新建的时候要刷屏
			// //
			//
			// my_toast("xinjian");
			//
			//
			// // view.mBitmap.eraseColor(Color.WHITE);
			// // //end
			// //
			// // mindmapEditFinish();
			// // MindMapItem.resetMindMapCount();
			// // addNewPage();
			//
			//
			//
			//
			//
			// }
			/*
			 * if(v == mDragEnableBtn){ Log.i(TAG, "Drag Button Click");
			 * if(MyView.MODE_DRAG == false) { view.syncMainToScale();
			 * view.sMatrix.reset(); mDragEnableBtn.setText("拖拽模式");
			 * MyView.MODE_DRAG = true; } else { view.syncScaleToMain();
			 * mDragEnableBtn.setText("画笔模式"); MyView.MODE_DRAG = false; }
			 * view.invalidate(); }
			 */
			if (v == mDrawStatusChangeBtn) {

				// Start.saveHandler.sendEmptyMessage(0);

				// ly
				// view.mBitmap.eraseColor(Color.WHITE);
				// end

				// Start.status.modified("tuya");
				if (MyView.drawStatus == MyView.STATUS_DRAW_FREE) {

					// ly
					// 切换至光表态，隐藏下一页按钮
					// mNextBtn.setVisibility(View.INVISIBLE);
					// end

					view.changeStateAndSync(MyView.STATUS_DRAW_CURSOR);
//					20190414让透明统计条一直出现
					transParentStatisticLayout.setVisibility(View.GONE);
					gestures.removeOnGestureListener(null);
					// gestures.setVisibility(view.INVISIBLE);
					view.cursorBitmap.initDate(WolfTemplateUtil
							.getCurrentTemplate());
				} else {

					// ly
					// 切换至光表态，隐藏下一页按钮
					// mNextBtn.setVisibility(View.VISIBLE);
					// end

					// ly
					// 刷一下屏看看
					// mBitmap.eraseColor(Color.WHITE);
					// end
					Log.i("caoheng", "btnClickListener");
					view.changeStateAndSync(MyView.STATUS_DRAW_FREE);
					transParentStatisticLayout.setVisibility(View.VISIBLE);
					gesLayout.setVisibility(View.VISIBLE);
					gestures.setVisibility(view.VISIBLE);
				}
			}

			if (v == mMicrophoneBtn) {
				// Toast toast = new Toast(mContext);
				// ImageView tv = new ImageView(mContext);
				// tv.setBackgroundResource(R.drawable.icon);
				// toast.setView(tv);
				// toast.setDuration(Toast.LENGTH_SHORT);
				// toast.show();
				my_toast("录音功能正在建设中");
				// Toast.makeText(mContext, "正在建设中", Toast.LENGTH_LONG).show();
			}
			if (v == mCameraBtn) {
				Toast.makeText(mContext, "您的设备目前不支持", Toast.LENGTH_LONG).show();
			}
		}

	}

	class DrawStatusBtnListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.i(TAG, "drawStatus:" + MyView.drawStatus);
			if (MyView.drawStatus == MyView.STATUS_DRAW_FREE) {
				// view.paperFlag = 1;
				mDrawStatusChangeBtn.setText("Free Draw"); // 涂鸦
				view.changeDrawState(MyView.STATUS_DRAW_CURSOR);
			} else {
				// view.paperFlag = 0;
				mDrawStatusChangeBtn.setText("Hand Write"); // 光标
				view.changeDrawState(MyView.STATUS_DRAW_FREE);
				view.cursorBitmap.updateHandwriteState();
			}
		}
	}

	class PenStatusBtnListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.i(TAG, "penStatus:" + MyView.penStatus);
			if (MyView.penStatus == MyView.STATUS_PEN_CALLI) {
				// view.paperFlag = 1;
				mPenStatusChangeBtn.setText("Brush Pen"); // 毛笔
				view.changePenState(MyView.STATUS_PEN_HARD);
			} else {
				// view.paperFlag = 0;
				mPenStatusChangeBtn.setText("Hard Pen"); // 硬笔
				view.changePenState(MyView.STATUS_PEN_CALLI);
			}
		}
	}

	class SlideBtnListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (MyView.drawStatus == MyView.STATUS_DRAW_CURSOR) {
				CurInfo cur = view.baseBitmap.bCurInfo;
				if (cur.mPosLeft > -Start.SCREEN_WIDTH)
					cur.mPosLeft--;
				view.invalidate();
				// view.draw();
			}

		}

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		Log.i(TAG, "onLayout");
		// int ll = handwriteMenuLayout.ml;
		// int tt = handwriteMenuLayout.mt;
		// int rr = handwriteMenuLayout.mr;
		// int bb = handwriteMenuLayout.mb;
		// Log.i(TAG, ll + " " + tt + " " + rr + " " + bb);
		// if(ll != 0 && tt != 0 && rr != 0 && bb != 0)
		// handwriteMenuLayout.layout(ll, tt, rr, bb);
	}

	public static void my_toast(String msg) {
		LayoutInflater inflater = (Start.instance).getLayoutInflater();
		View layout = inflater
				.inflate(R.layout.toast, (ViewGroup) Start.instance
						.findViewById(R.id.toast_layout_root));

		ImageView image = (ImageView) layout.findViewById(R.id.image);
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(msg);

		Toast toast = new Toast((Start.instance).getApplicationContext());
		toast.setGravity(Gravity.TOP | Gravity.LEFT, 50, (int) (yy + 200));
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		// try
		// {
		// // 从Toast对象中获得mTN变量
		// Field field = toast.getClass().getDeclaredField("mTN");
		// field.setAccessible(true);
		// Object obj = field.get(toast);
		// // TN对象中获得了show方法
		// Method method = obj.getClass().getDeclaredMethod("show", null);
		// // 调用show方法来显示Toast信息提示框
		// method.invoke(obj, null);
		// }
		// catch (Exception e)
		// {
		// }
		toast.show();
	}

	public static boolean opened = false;

	@Override
	public void onPanelClosed(Panel panel) {
		// TODO Auto-generated method stub
		opened = false;

		Runnable r = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				upHandler.sendEmptyMessage(0);
			}
		};
		(new Thread(r)).start();
	}

	@Override
	public void onPanelOpened(Panel panel) {
		// TODO Auto-generated method stub
		opened = true;
		Log.e("panel", "open listen top:" + flipblockLayout.getTop());
	}

	public void startTransCameraPic() {

		jni = new Jni();
		// 建立adhoc
		// 绑定service
		Intent intent = new Intent().setClass(Start.context,
				TransmitProtocolService.class);
		Start.context.bindService(intent, conn, Context.BIND_AUTO_CREATE);

		recvThreadFlag = true;

		(new RecvRequest()).start();// 与手机端握手链接
		(new GetIP()).start();// 获取本地ip
		(new HandleTramsmit()).start();// 开始传输，等待接收完毕

	}

	public void restartGetIP() {
		(new GetIP()).start();// 获取本地ip
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (panel.isFlipping()) {
			return;
		}
		if (v == expend_AlarmBtn) {
			Start.instance.showDialog(Start.DATETIMESELECTOR_ID);
			panel.openOrClose();
			// Toast.makeText(Start.context, "alarm",
			// Toast.LENGTH_SHORT).show();
		} else if (v == expend_AudioBtn) {
			// my_toast("录音功能正在建设中");
			// mRecordAlertDlg = onCreatDialog(ALERT_RECORD_DLG);
			//
			// dbrListener.onClick(v);
			positiveDialogOnClick();
			panel.openOrClose();

			/*
			 * shareType = AUDIOSHARE;
			 * 
			 * if(MyView.drawStatus == MyView.STATUS_DRAW_FREE) {
			 * Toast.makeText(Start.context, "涂鸦态下不可用",
			 * Toast.LENGTH_SHORT).show(); return; } if(MyView.drawStatus ==
			 * MyView.STATUS_DRAW_CURSOR) { // disable insert picture String
			 * name =
			 * view.cursorBitmap.cal_current.getAvailable().getControltype();
			 * if(Available.AVAILABLE_SUBJECT.equals(name) ||
			 * Available.AVAILABLE_NUMBER.equals(name) ||
			 * Available.AVAILABLE_DATE.equals(name)){ Toast.makeText(mContext,
			 * "不能插入音频", Toast.LENGTH_SHORT).show(); return; } }
			 * 
			 * try { view.cursorBitmap.insertAudioBitmap(
			 * BitmapFactory.decodeResource(getResources(),
			 * R.drawable.audio_unfinish) ,null);
			 * BitmapCount.getInstance().createBitmap
			 * ("Calligraph decode insertAudioBitmap audio_unfinish");
			 * 
			 * } catch (OutOfMemoryError e) { // TODO: handle exception
			 * Toast.makeText(Start.context, "内存不足，不能插入",
			 * Toast.LENGTH_SHORT).show(); return; }
			 * 
			 * Uri uri = Uri.parse("file:///android_asset/audio.png");//默认等待图片
			 * Log.e("camera", uri.toString());
			 * 
			 * cameraPicPage = Start.getPageNum(); cameraPicAvailableID =
			 * view.cursorBitmap.cal_current.getID(); cameraPicItemID =
			 * view.cursorBitmap.cal_current.currentpos -1;
			 * 
			 * Log.e("camera", "page:" + cameraPicPage + " availableID:" +
			 * cameraPicAvailableID + " itemID:" + cameraPicItemID);
			 * 
			 * wifiandadhocPause = true; Log.e("adhoc",
			 * "before start WIFI and ADHOC activity");
			 * 
			 * send3PhotoShare();
			 * 
			 * // Intent eintent = new Intent(); // eintent.setComponent(new
			 * ComponentName("com.jinke.wifiadhoc.select", //
			 * "com.jinke.wifiadhoc.select.wifioradhoc")); //
			 * Start.instance.startActivityForResult
			 * (eintent,Start.AddAudioRequest); setAdhocMode();
			 * startTransCameraPic();
			 * 
			 * // Toast.makeText(Start.context, "audio",
			 * Toast.LENGTH_SHORT).show();
			 */
		} else if (v == expend_AddCameraBtn) {
			/*
			 * my_toast("照片功能正在建设中");
			 */
			panel.openOrClose();

			// my_toast("拍照");
			File imgFile = new File(Start.TempImgFilePath);
			if (!imgFile.exists()) {
				File dir = imgFile.getParentFile();
				dir.mkdirs();
			}

			Uri uri = Uri.fromFile(imgFile);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			Start.instance.startActivityForResult(intent,
					Start.AddCameraRequest);//Start.AddCameraRequest=2

			// shareType = PHOTOSHARE;
			//
			// if(!ImageLimit.instance().canInsertImage()){
			// Toast.makeText(Start.context, "最多插入" + ImageLimit.LIMIT_NUMBER +
			// "张图片", Toast.LENGTH_SHORT).show();
			// return;
			// }
			//
			// if(MyView.drawStatus == MyView.STATUS_DRAW_FREE)
			// {
			// Toast.makeText(Start.context, "涂鸦态下不可用",
			// Toast.LENGTH_SHORT).show();
			// return;
			// }
			// if(MyView.drawStatus == MyView.STATUS_DRAW_CURSOR) {
			// // disable insert picture
			// String name =
			// view.cursorBitmap.cal_current.getAvailable().getControltype();
			// if("subject".equals(name) || "number".equals(name) ||
			// "date".equals(name)){
			// Toast.makeText(mContext, "不能插入图片", Toast.LENGTH_SHORT).show();
			// return;
			// }
			// }
			// File imgFile = new File(Start.TempImgFilePath);
			// if(!imgFile.exists()){
			// File dir = imgFile.getParentFile();
			// dir.mkdirs();
			// }
			//
			// if(!"123456".equals(CalligraphyBackupUtil.getSimID())){
			// //有sim卡,认为没有摄像头,插入默认图片，开启adhoc，等待传输成功后刷新
			//
			// try {
			// EditableCalligraphyItem item =
			// view.cursorBitmap.insertImageBitmap(
			// BitmapFactory.decodeResource(getResources(),
			// R.drawable.photo_adhoc)
			// ,Uri.parse("android.resource://" +
			// Start.context.getApplicationContext().getPackageName() + "/" +
			// R.drawable.photo_adhoc));
			// item.setWifiOrAdhoc();
			// BitmapCount.getInstance().createBitmap("Calligraph decode photo.png");
			// firstTransformPicFromMobile = true;
			// } catch (OutOfMemoryError e) {
			// // TODO: handle exception
			// Toast.makeText(Start.context, "内存不足，不能插入",
			// Toast.LENGTH_SHORT).show();
			// return;
			// }
			//
			// Uri uri = Uri.parse("file:///android_asset/photo.png");//默认等待图片
			// Log.e("camera", uri.toString());
			//
			// cameraPicPage = Start.getPageNum();
			// cameraPicAvailableID = view.cursorBitmap.cal_current.getID();
			// cameraPicItemID = view.cursorBitmap.cal_current.currentpos -1;
			//
			// Log.e("camera", "page:" + cameraPicPage + " availableID:" +
			// cameraPicAvailableID + " itemID:" + cameraPicItemID);
			//
			// wifiandadhocPause = true;
			// Log.e("adhoc", "before start WIFI and ADHOC activity");
			//
			// (new send3PhotoShareThread()).start();
			// try {
			// Thread.sleep(100);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//
			// // Intent eintent = new Intent();
			// // eintent.setComponent(new
			// ComponentName("com.jinke.wifiadhoc.select",
			// // "com.jinke.wifiadhoc.select.wifioradhoc"));
			// //
			// Start.instance.startActivityForResult(eintent,Start.AddCameraRequest);
			//
			// setAdhocMode();
			// startTransCameraPic();
			//
			// }else{
			// //没有sim卡，认为有摄像头，直接打开摄像头拍照
			// Uri uri = Uri.fromFile(imgFile);
			// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			// Start.instance.startActivityForResult(intent,
			// Start.AddCameraRequest);
			// }

		} else if (v == expend_FaceBtn) {
			if (MyView.drawStatus == MyView.STATUS_DRAW_FREE) {
				Toast.makeText(Start.context, "涂鸦态下不可用", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			int[] faceImageRes = new int[] { R.drawable.icon_mood1,
					R.drawable.icon_mood2, R.drawable.icon_mood3,
					R.drawable.icon_mood4, R.drawable.icon_mood5,
					R.drawable.icon_mood6, R.drawable.icon_mood7,
					R.drawable.icon_mood8, R.drawable.icon_mood9,
					R.drawable.icon_mood10, R.drawable.icon_mood11,
					R.drawable.icon_mood12 };
			showPopupView(faceImageRes);
			panel.openOrClose();
			// my_toast("表情功能正在建设中");
			// Toast.makeText(Start.context, "face", Toast.LENGTH_SHORT).show();
		} else if (v == expend_WeatherBtn) {
			if (MyView.drawStatus == MyView.STATUS_DRAW_FREE) {
				Toast.makeText(Start.context, "涂鸦态下不可用", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			int[] weatherImageRes = new int[] { R.drawable.icon_weather1,
					R.drawable.icon_weather2, R.drawable.icon_weather3,
					R.drawable.icon_weather4, R.drawable.icon_weather5,
					R.drawable.icon_weather6, R.drawable.icon_weather7,
					R.drawable.icon_weather8 };
			showPopupView(weatherImageRes);
			panel.openOrClose();
			// my_toast("天气功能正在建设中");
			// Toast.makeText(Start.context, "weather",
			// Toast.LENGTH_SHORT).show();
		} else if (v == expend_AddpicBtn) {

			panel.openOrClose();

			if (!ImageLimit.instance().canInsertImage()) {
				Toast.makeText(Start.context,
						"最多插入" + ImageLimit.LIMIT_NUMBER + "张图片",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (MyView.drawStatus == MyView.STATUS_DRAW_FREE) {
				// 插入打开相册功能，caoheng, 10.25
				int CHOOSEPICTURE_REQUESTCODE = 0;
				Intent choosePictureIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				// 啟動該activity，并在該activity結束返回數據,所以調用startActivityForResult()方法
				Start.instance.startActivityForResult(choosePictureIntent,
						CHOOSEPICTURE_REQUESTCODE);//CHOOSEPICTURE_REQUESTCODE = 0
				// Toast.makeText(Start.context, "打开相册插入图片",
				// Toast.LENGTH_SHORT).show();
				return;
			}
			if (MyView.drawStatus == MyView.STATUS_DRAW_CURSOR) {
				// disable insert picture
				String name = view.cursorBitmap.cal_current.getAvailable()
						.getControltype();
				if (Available.AVAILABLE_SUBJECT.equals(name)
						|| Available.AVAILABLE_NUMBER.equals(name)
						|| Available.AVAILABLE_DATE.equals(name)) {
					Toast.makeText(mContext, "不能插入图片", Toast.LENGTH_SHORT)
							.show();
					return;
				}
			}
			view.cursorBitmap.picFlag = true;
			systemScan();
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			Start.instance.startActivityForResult(intent,
					Start.AddPictureRequest);//AddPictureRequest = 1

		} else if (v == expend_AddVideoBtn) {
			my_toast("视频功能正在建设中");
			panel.openOrClose();

			/*
			 * shareType = VIDEOSHARE; if(MyView.drawStatus ==
			 * MyView.STATUS_DRAW_FREE) { Toast.makeText(Start.context,
			 * "涂鸦态下不可用", Toast.LENGTH_SHORT).show(); return; }
			 * if(MyView.drawStatus == MyView.STATUS_DRAW_CURSOR) { // disable
			 * insert picture String name =
			 * view.cursorBitmap.cal_current.getAvailable().getControltype();
			 * if(Available.AVAILABLE_SUBJECT.equals(name) ||
			 * Available.AVAILABLE_NUMBER.equals(name) ||
			 * Available.AVAILABLE_DATE.equals(name)){ Toast.makeText(mContext,
			 * "不能插入视频", Toast.LENGTH_SHORT).show(); return; } }
			 * 
			 * try { view.cursorBitmap.insertVideoBitmap(
			 * BitmapFactory.decodeResource(getResources(),
			 * R.drawable.video_unfinish) ,null);
			 * BitmapCount.getInstance().createBitmap
			 * ("Calligraph decode video_unfinish.png"); } catch
			 * (OutOfMemoryError e) { // TODO: handle exception
			 * Toast.makeText(Start.context, "内存不足，不能插入",
			 * Toast.LENGTH_SHORT).show(); return; }
			 * 
			 * // Uri uri =
			 * Uri.parse("file:///android_asset/video.png");//默认等待图片 //
			 * Log.e("camera", uri.toString());
			 * 
			 * cameraPicPage = Start.getPageNum(); cameraPicAvailableID =
			 * view.cursorBitmap.cal_current.getID(); cameraPicItemID =
			 * view.cursorBitmap.cal_current.currentpos -1;
			 * 
			 * Log.e("camera", "page:" + cameraPicPage + " availableID:" +
			 * cameraPicAvailableID + " itemID:" + cameraPicItemID);
			 * 
			 * wifiandadhocPause = true; Log.e("adhoc",
			 * "before start WIFI and ADHOC activity");
			 * 
			 * send3PhotoShare();
			 * 
			 * // Intent eintent = new Intent(); // eintent.setComponent(new
			 * ComponentName("com.jinke.wifiadhoc.select", //
			 * "com.jinke.wifiadhoc.select.wifioradhoc")); //
			 * Start.instance.startActivityForResult
			 * (eintent,Start.AddVideoRequest); setAdhocMode();
			 * startTransCameraPic();
			 */
		}

	}

	private Handler updateCameraHandler = new Handler() {
		public void handleMessage(Message msg) {
			int page = msg.arg1;
			int aid = msg.arg2;
			int iid = msg.what;
			EditableCalligraphyItem item = null;

			if (savepath != null && PHOTOSHARE.equals(shareType)) {
				for (int i = 0; i < savepath.length; i++) {
					Log.e("uri", "savepath [" + i + "] :" + savepath[i]);
					if (i == 0 && firstTransformPicFromMobile) {
						firstTransformPicFromMobile = false;
						Uri cameraUri = Uri.parse(savepath[i]);
						Log.e("uri:", "cameraUri:" + (cameraUri == null));
						CDBPersistent db = new CDBPersistent(Start.context);
						Bitmap newBitmap = null;
						newBitmap = db.getBitmapFromUri(cameraUri, page);
						Log.e("photo", "reset:a" + aid + " item:" + iid);
						item = view.cursorBitmap.listEditableCalligraphy
								.get(aid).getCharsList().get(iid);
						view.cursorBitmap.listEditableCalligraphy
								.get(aid)
								.getCharsList()
								.get(iid)
								.resetCharBitmap(newBitmap, new Matrix(),
										cameraUri);
						CalligraphyDB.getInstance(Start.context)
								.updateCameraPicUri(page, aid,
										item.getItemID(), cameraUri);
					} else {
						try {
							Uri cameraUri = Uri.parse(savepath[i]);

							CDBPersistent db = new CDBPersistent(Start.context);
							Bitmap newBitmap = null;
							newBitmap = db.getBitmapFromUri(cameraUri, page);

							EditableCalligraphyItem e = view.cursorBitmap
									.insertImageBitmap(newBitmap, cameraUri);

						} catch (OutOfMemoryError e) {
							// TODO: handle exception
							Toast.makeText(Start.context, "内存不足，不能插入",
									Toast.LENGTH_SHORT).show();
							return;
						}
					}
				}
			}

			Bitmap newBitmap = null;
			if (VIDEOSHARE.equals(shareType)) {
				Uri cameraUri = Uri.parse(savepath[0]);

				double dur = MediaPlayerUtil.getInstance().getDuration(
						cameraUri) / 1000;// s
				String duration = Math.floor(dur / 60) + "分"
						+ Math.ceil((dur / 60 - Math.floor(dur / 60)) * 60)
						+ "秒";
				Log.e("media", "duration:" + duration);

				try {
					newBitmap = BitmapFactory.decodeResource(getResources(),
							R.drawable.video).copy(Config.ARGB_4444, true);
					Canvas canvas = new Canvas();
					canvas.setBitmap(newBitmap);
					Paint p = new Paint();
					p.setTextSize(20);
					canvas.drawText(duration, 145f, 30f, p);

					BitmapCount.getInstance().createBitmap(
							"BaseBitmap decode R.drawable.video");
				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					Toast.makeText(Start.context, "内存不足，不能插入",
							Toast.LENGTH_SHORT).show();
					return;
				}
				item = view.cursorBitmap.listEditableCalligraphy.get(aid)
						.getCharsList().get(iid);
				view.cursorBitmap.listEditableCalligraphy.get(aid)
						.getCharsList().get(iid)
						.resetVideoUri(newBitmap, new Matrix(), cameraUri);
				CalligraphyDB.getInstance(Start.context).updateCameraPicUri(
						page, aid, item.getItemID(), cameraUri);
				CalligraphyDB.getInstance(Start.context).updatePictrueItem(
						Start.getPageNum(), 3, item.getItemID(), item);
			}

			if (AUDIOSHARE.equals(shareType)) {
				Uri cameraUri = Uri.parse(savepath[0]);

				double dur = MediaPlayerUtil.getInstance().getDuration(
						cameraUri) / 1000;// s
				String duration = Math.floor(dur / 60) + "分"
						+ Math.ceil((dur / 60 - Math.floor(dur / 60)) * 60)
						+ "秒";
				Log.e("media", "duration:" + duration);
				try {
					newBitmap = BitmapFactory.decodeResource(getResources(),
							R.drawable.audio_stop).copy(Config.ARGB_4444, true);

					BitmapCount.getInstance().createBitmap(
							"BaseBitmap decode R.drawable.audio_playing");
					BitmapCount.getInstance().createBitmap(
							"BaseBitmap decode R.drawable.audio_stop");

					Canvas canvas = new Canvas();
					canvas.setBitmap(newBitmap);
					Paint p = new Paint();
					p.setTextSize(20);
					canvas.drawText(duration, 145f, 30f, p);

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					Toast.makeText(Start.context, "内存不足，不能插入",
							Toast.LENGTH_SHORT).show();
					return;
				}
				item = view.cursorBitmap.listEditableCalligraphy.get(aid)
						.getCharsList().get(iid);
				// view.cursorBitmap.listEditableCalligraphy.get(aid).getCharsList().get(iid).resetAudioUri(newBitmap,new
				// Matrix(),cameraUri);
				item.resetAudioUri(newBitmap, new Matrix(), cameraUri);
				item.setStopBitmap();
				CalligraphyDB.getInstance(Start.context).updateCameraPicUri(
						page, aid, item.getItemID(), cameraUri);
				CalligraphyDB.getInstance(Start.context).updatePictrueItem(
						Start.getPageNum(), 3, item.getItemID(), item);
			}

			ImageLimit.instance().addImageCount();

			// String picName = "p"+page + "a" + aid + "i" + iid;
			// Uri cameraUri = view.savePicBitmap(saveuri, picName);
			// Log.e("uri:", "cameraUri:" + (cameraUri == null));
			// CDBPersistent db = new CDBPersistent(Start.context);
			// db.open();
			// Bitmap newBitmap = null;
			// if(VIDEOSHARE.equals(shareType)){
			//
			// try {
			// newBitmap = BitmapFactory.decodeResource(getResources(),
			// R.drawable.video);
			// BitmapCount.getInstance().createBitmap("BaseBitmap decode R.drawable.video");
			// } catch (OutOfMemoryError e) {
			// // TODO: handle exception
			// Toast.makeText(Start.context, "内存不足，不能插入",
			// Toast.LENGTH_SHORT).show();
			// return;
			// }
			// item =
			// view.cursorBitmap.listEditableCalligraphy.get(aid).getCharsList().get(iid);
			// view.cursorBitmap.listEditableCalligraphy.get(aid).getCharsList().get(iid).resetVideoUri(newBitmap,new
			// Matrix(),cameraUri);
			// CalligraphyDB.getInstance(Start.context).updateCameraPicUri(page,
			// aid, item.getItemID(), cameraUri);
			// }
			// if(AUDIOSHARE.equals(shareType)){
			// try {
			// newBitmap = BitmapFactory.decodeResource(getResources(),
			// R.drawable.audio);
			// BitmapCount.getInstance().createBitmap("BaseBitmap decode R.drawable.audio");
			// } catch (OutOfMemoryError e) {
			// // TODO: handle exception
			// Toast.makeText(Start.context, "内存不足，不能插入",
			// Toast.LENGTH_SHORT).show();
			// return;
			// }
			// item =
			// view.cursorBitmap.listEditableCalligraphy.get(aid).getCharsList().get(iid);
			// view.cursorBitmap.listEditableCalligraphy.get(aid).getCharsList().get(iid).resetAudioUri(newBitmap,new
			// Matrix(),cameraUri);
			// CalligraphyDB.getInstance(Start.context).updateCameraPicUri(page,
			// aid, item.getItemID(), cameraUri);
			// }

			view.cursorBitmap.updateHandwriteState();
			WorkQueue.getInstance().endFlipping();
			end = System.currentTimeMillis();
			Log.e("wifioradhoc", "using time:" + (end - start) + " ms");
			Start.status.modified("complete transform pic from telephone");
			// Start.saveHandler.sendEmptyMessage(0);
			// db.close();

			// jni.closeAdhoc();//可以改放到用wifi网络时再关闭
			// recvThreadFlag = false;
			// m_RecvHost.close();
			// m_handletransmit.close();
			// m_RecvHost = null;
			// m_handletransmit = null;

		}
	};

	private void initPopupWindow() {
		popupView = Start.instance.getLayoutInflater().inflate(
				R.layout.icon_popup_window, null);
		// popupWindow = new PopupWindow(popupView, 500, 270);
		popupWindow = new PopupWindow(popupView, Start.SCREEN_WIDTH * 5 / 6,
				Start.SCREEN_HEIGHT * 1 / 4);

		popupWindow.setOutsideTouchable(false);
		popupWindow.setFocusable(true);// 默认false，不会响应itemClickListener

	}

	public void showPopupView(final int[] res) {

		GridView icon_gridview = (GridView) popupView
				.findViewById(R.id.icon_popup_gridView);

		icon_gridview.setAdapter(new IconPopupAdapter(Start.context, res));

		icon_gridview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

						view.cursorBitmap.insertImageItem(res[arg2]);
						popupWindow.dismiss();

					}
				});

		Button cancleButton = (Button) popupView
				.findViewById(R.id.icon_popup_cancleBtn);
		// cancleButton.setTextSize(Start.SCREEN_WIDTH / 30);
		cancleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
			}
		});

		popupWindow.showAtLocation(Start.c.view, Gravity.NO_GRAVITY, 85, 640);

		// ly
		// popupWindow.showAtLocation(Start.c.view, Gravity.NO_GRAVITY,
		// Start.SCREEN_WIDTH * (85 / 600), Start.SCREEN_HEIGHT *(700/1024));
		popupWindow.showAtLocation(Start.c.view, Gravity.NO_GRAVITY,
				Start.SCREEN_WIDTH * (85 / 1600), Start.SCREEN_HEIGHT
						* (700 / 2560));
	}

	private TransmitProtocolService sservice = null;
	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			sservice = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			sservice = ((TransmitProtocolService.ShareBind) service)
					.getService();
		}
	};

	private boolean recvThreadFlag = true;
	DatagramSocket m_RecvHost = null;
	DatagramSocket m_handletransmit = null;
	public int SearchPort = 9000;
	public int waitport = 9002;
	private static int transmitport = 9004;
	public String localip = null;
	public boolean transmitting = false;

	private String saveuri = Environment.getExternalStorageDirectory()
			+ "/temp";

	Jni jni = null;

	int cameraPicPage;
	int cameraPicAvailableID;
	int cameraPicItemID;

	class RecvRequest extends Thread {
		public void run() {
			while (recvThreadFlag) {
				byte[] ba = new byte[1024];
				DatagramPacket packet = new DatagramPacket(ba, ba.length);
				try {
					if (m_RecvHost == null)
						m_RecvHost = new DatagramSocket(SearchPort);
					m_RecvHost.receive(packet);
					String message = new String(packet.getData());
					message = message.trim();
					String[] mm = message.split(";");
					if (mm[0].equals("1") && localip != null
							&& mm[1].equals(shareType)) {
						Log.e("localip", localip);
						DatagramSocket sck = null;
						try {
							sck = new DatagramSocket();
							InetAddress destadd = packet.getAddress();

							String content = "2;" + shareType + ";" + localip;
							byte[] baa = content.getBytes();
							DatagramPacket pack = new DatagramPacket(baa,
									baa.length, destadd, SearchPort);
							sck.send(pack);
							Log.e("getip", "send content localip" + content);
						} catch (SocketException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // UDP，通过ServerPort端口发送消息
						catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	class GetIP extends Thread {
		public void run() {
			while (recvThreadFlag) {
				Log.e("getip", "status:" + Start.netStatus);
				if (Start.ADHOC.equals(Start.netStatus))
					localip = jni.getLocalHost();
				else if (Start.WIFI.equals(Start.netStatus))
					localip = getLocalIpAddress();

				Log.e("getip", "localip:" + localip);

				if (localip != null)
					break;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public String getLocalIpAddress() {
			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface
						.getNetworkInterfaces(); en.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf
							.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							return inetAddress.getHostAddress().toString();
						}
					}
				}
			} catch (SocketException ex) {
				Log.e("WifiPreference IpAddress", ex.toString());
			}
			return null;
		}

	}

	DatagramSocket sck = null;

	private class HandleTramsmit extends Thread {
		public void run() {
			int port = 9002;
			String[] m = new String[] { "" };

			while (recvThreadFlag) {
				Log.e("wifioradhoc", "recvThreadFlag:" + recvThreadFlag);
				byte[] ba = new byte[1024];
				DatagramPacket packet = new DatagramPacket(ba, ba.length);
				try {
					if (m_handletransmit == null)
						m_handletransmit = new DatagramSocket(port);
					m_handletransmit.receive(packet);

					start = System.currentTimeMillis();
					Log.e("wifioradhoc", "start:" + start);
					String message = new String(packet.getData());
					message = message.trim();
					Log.v("renkai", "message=" + message);
					m = message.split(";");
					Log.v("renkai",
							"savepathnum=" + String.valueOf(m.length - 2));
					if (m[0].equals("1")) {
						Log.e("wifioradhoc", "recv message:" + message);
						savepath = new String[m.length - 2];
						for (int i = 0; i != savepath.length; i++) {
							savepath[i] = "/sdcard/calldir/free_"
									+ Start.getPageNum() + "/";
							savepath[i] += m[i + 2];
							Log.v("renkai", "savepath=" + savepath[i]);
						}
						DatagramSocket sck = null;
						try {
							sck = new DatagramSocket();
							InetAddress destadd = packet.getAddress();
							String content = "2";
							byte[] baa = content.getBytes();
							DatagramPacket packet1 = new DatagramPacket(baa,
									baa.length, destadd, port);
							sck.send(packet1);
							Log.e("wifioradhoc", "send 2!!!!");
						} catch (SocketException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // UDP，通过ServerPort端口发送消息
						catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						if (m[0].equals("4")) {
							long end = System.currentTimeMillis();
							System.out.println("时间："
									+ String.valueOf((long) end));
							Log.v("renkai", "recv 4");

							String ip = packet.getAddress().toString();
							ip = ip.substring(1, ip.length());
							Log.v("renkai", "duifangip=" + ip);
							sservice.wgetfiles(savepath, ip, transmitport);

							new UpdateBar(cameraPicPage, cameraPicAvailableID,
									cameraPicItemID).start();
						}
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * 收取单个文件 class HandleTramsmit extends Thread { public void run() {
	 * 
	 * while(recvThreadFlag){ byte[] ba = new byte[1024]; DatagramPacket packet
	 * = new DatagramPacket(ba,ba.length); try{ if(m_handletransmit == null)
	 * 
	 * m_handletransmit = new DatagramSocket(waitport);
	 * 
	 * m_handletransmit.receive(packet); String message = new
	 * String(packet.getData()); message = message.trim();
	 * if(message.equals("1") && localip != null){ Log.e("adhoc",
	 * "recv message:" + message); //DatagramSocket sck = null;
	 * 
	 * sck = new DatagramSocket(); InetAddress destadd = packet.getAddress();
	 * String content; if(transmitting){ content = "3"; byte[] baa =
	 * content.getBytes(); DatagramPacket pack = new
	 * DatagramPacket(baa,baa.length,destadd,waitport); sck.send(pack); } else {
	 * Message msg = new Message(); Bundle bun = new Bundle();
	 * bun.putString("ip", destadd.toString()); String string =
	 * (destadd.toString()).substring(1); Log.v("renkai",string);
	 * msg.setData(bun); //
	 * getContext().BeginTransmitHandler.sendMessage(msg);//询问是否开始传输
	 * //不用询问，直接开始传输
	 * 
	 * 
	 * // sckk = new DatagramSocket(); // InetAddress destadd1 =
	 * InetAddress.getByName(string); String content1 = "2";
	 * 
	 * byte[] baa = content1.getBytes(); DatagramPacket pack = new
	 * DatagramPacket(baa,baa.length,destadd,waitport); Log.e("adhoc",
	 * "before send------------------------------"); // sckk.send(pack);
	 * sck.send(pack);
	 * 
	 * Log.e("adhoc", "after send------------------------------");
	 * 
	 * transmitting = true;
	 * 
	 * 
	 * try { Thread.sleep(500); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } // String content2 =
	 * "250"; // // byte[] baaa = content1.getBytes(); // DatagramPacket pack2 =
	 * new DatagramPacket(baaa,baaa.length,destadd,waitport); sck.send(pack);
	 * 
	 * sservice.wgetfile(saveuri, string, transmitport);
	 * 
	 * // tv.setText(getResources().getString(R.string.transmitting)); new
	 * UpdateBar(cameraPicPage,cameraPicAvailableID,cameraPicItemID).start();
	 * 
	 * 
	 * }
	 * 
	 * 
	 * } } catch (SocketException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } }//while }//run }//class
	 */

	public class UpdateBar extends Thread {

		int cameraPicPage;
		int cameraPicAvailableID;
		int cameraPicItemID;

		public UpdateBar(int page, int aid, int itemid) {
			this.cameraPicAvailableID = aid;
			this.cameraPicItemID = itemid;
			this.cameraPicPage = page;
		}

		public void run() {
			while (recvThreadFlag) {

				if (sservice.getProgress() == 100) {
					// 传输完毕,文件已经到/temp.jpg
					transmitting = false;
					Message msg = new Message();
					msg.arg1 = this.cameraPicPage;
					msg.arg2 = this.cameraPicAvailableID;
					msg.what = this.cameraPicItemID;
					updateCameraHandler.sendMessage(msg);
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	class send3PhotoShareThread extends Thread {
		public void send3PhotoShare() {
			DatagramSocket sck = null;
			try {
				sck = new DatagramSocket();
				InetAddress destadd = InetAddress.getByName("10.0.0.255");
				String content = "3;" + shareType + ";" + localip;
				byte[] ba = content.getBytes();
				DatagramPacket packet = new DatagramPacket(ba, ba.length,
						destadd, SearchPort);
				sck.send(packet);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // UDP，通过ServerPort端口发送消息
			catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			send3PhotoShare();
		}
	};

	public void closeADHoc() {
		Log.e("adhoc", " call closeAdhoc");

		if (Start.ADHOC.equals(Start.netStatus)) {

			(new send3PhotoShareThread()).start();
			Log.e("adhoc", "closeAdhoc netStatus " + (Start.netStatus));

			Log.e("adhoc", "closeAdhoc jni " + (jni == null));
			if (jni != null) {
				jni.closeAdhoc();// 可以改放到用wifi网络时再关闭
				Log.e("adhoc", "closeAdhoc");
			}
			recvThreadFlag = false;
			firstTransformPicFromMobile = true;
			if (m_RecvHost != null)
				m_RecvHost.close();
			if (m_handletransmit != null)
				m_handletransmit.close();
			if (m_RecvHost != null)
				m_RecvHost = null;
			if (m_handletransmit != null)
				m_handletransmit = null;
		}

	}

	public void setWifiMode() {
		WifiOrAdhoc wifiOrAdhoc = new WifiOrAdhoc();
		wifiOrAdhoc.setWifiMode();
		Start.netStatus = Start.WIFI;
	}

	public void setAdhocMode() {
		WifiOrAdhoc wifiOrAdhoc = new WifiOrAdhoc();
		wifiOrAdhoc.setAdhocMode();
		Start.netStatus = Start.ADHOC;
	}

	public void systemScan() {
		Start.context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
				.parse("file://" + Environment.getExternalStorageDirectory())));
	}
//截取整个calligraph的内容
	public boolean saveScreen() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",
				Locale.US);
//		String fname = "/sdcard/pigairesult/" + view.pageXML + ".jpg";
		//测试存储
		String fname = "/sdcard/pigairesult/" + "001-0944-0-0-0" + ".jpg";
		
		View dView = Start.c;
		dView.getRootView();
		dView.setDrawingCacheEnabled(true);
		dView.buildDrawingCache();
		
		Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
		
		

		if (bitmap != null) {
			System.out.println("bitmap got!");
			try {
				FileOutputStream out = new FileOutputStream(fname);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
				System.out.println("file " + fname + "outputdonezhuan .");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("bitmap is NULL!");
		}
		Uri data = Uri.parse("file://storage/emulated/0/");
		Start.context.sendBroadcast(new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
		
		
		
		
		
		return true;
	}
	
	
	
	// 截屏代码 caoheng 2015.12.15
//	public boolean saveScreen() {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",
//				Locale.US);
//		String fname = "/sdcard/" + view.pageXML + ".jpg";
//		view.invalidate();
//		view.setDrawingCacheEnabled(true);
//		view.buildDrawingCache();
//
//		pingyuDisplayLayout.invalidate();
//		pingyuDisplayLayout.setDrawingCacheEnabled(true);
//		pingyuDisplayLayout.buildDrawingCache();
//
//		pingyuDisplayLayout1.invalidate();
//		pingyuDisplayLayout1.setDrawingCacheEnabled(true);
//		pingyuDisplayLayout1.buildDrawingCache();
//
//		tounaoDisplayLayout.invalidate();
//		tounaoDisplayLayout.setDrawingCacheEnabled(true);
//		tounaoDisplayLayout.buildDrawingCache();
//
//		pigaihuanPingyuText.invalidate();
//		pigaihuanPingyuText.setDrawingCacheEnabled(true);
//		pigaihuanPingyuText.buildDrawingCache();
//
//		
//
//		pigaiResultImageView.invalidate();
//		pigaiResultImageView.setDrawingCacheEnabled(true);
//		pigaiResultImageView.buildDrawingCache();
//
//		Bitmap bitmap = view.getDrawingCache();
//		Bitmap bitmap1 = pingyuDisplayLayout.getDrawingCache();
//		Bitmap bitmap2 = pingyuDisplayLayout1.getDrawingCache();
//		Bitmap bitmap3 = tounaoDisplayLayout.getDrawingCache();
//		Bitmap bitmap4 = pigaihuanPingyuText.getDrawingCache();
//		Bitmap bitmap5 = pigaiResultImageView.getDrawingCache();
//
//		Canvas canvas = new Canvas(bitmap);
//		Matrix drawMatrix = new Matrix();
//		// drawMatrix = pingyuDisplayLayout.getMatrix();
//		drawMatrix.set(pingyuDisplayLayout.getMatrix());
//		drawMatrix.setTranslate(200, 1050);
//
//		Matrix drawMatrix1 = new Matrix();
//		// drawMatrix = pingyuDisplayLayout.getMatrix();
//		drawMatrix1.set(pingyuDisplayLayout1.getMatrix());
//		drawMatrix1.setTranslate(200, 1400);
//
//		Matrix drawMatrix2 = new Matrix();
//		drawMatrix2.set(pingyuDisplayLayout1.getMatrix());
//		drawMatrix2.setTranslate(200, 200);
//
//		Matrix drawMatrix3 = new Matrix();
//		drawMatrix3.set(pingyuDisplayLayout1.getMatrix());
//		drawMatrix3.setTranslate(800, 2200);
//
//		Matrix drawMatrix4 = new Matrix();
//		drawMatrix4.set(pigaiResultImageView.getMatrix());
//		drawMatrix4.setTranslate(100, 2000);
//
//		if (bitmap1 != null)
//			canvas.drawBitmap(bitmap1, drawMatrix, null);
//		if (bitmap2 != null)
//			canvas.drawBitmap(bitmap2, drawMatrix1, null);
//		if (bitmap3 != null)
//			canvas.drawBitmap(bitmap3, drawMatrix2, null);
//		// if(bitmap1!=null)canvas.drawBitmap(bitmap1, pingyuText, null);
//		// if(bitmap2!=null)canvas.drawBitmap(bitmap2, new Matrix(), null);
//
//		if (bitmap4 != null)
//			canvas.drawBitmap(bitmap4, drawMatrix3, null);
//		if (bitmap5 != null)
//			canvas.drawBitmap(bitmap5, drawMatrix4, null);
//
//		if (bitmap != null) {
//			System.out.println("bitmap got!");
//			try {
//				FileOutputStream out = new FileOutputStream(fname);
//				bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
//				System.out.println("file " + fname + "outputdonezhuan .");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else {
//			System.out.println("bitmap is NULL!");
//		}
//		Uri data = Uri.parse("file://storage/emulated/0/");
//		Start.context.sendBroadcast(new Intent(
//				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
//		// Intent intent = new Intent();
//		// intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
//		// Uri data = Uri.parse("/storage/emulated/0");
//		// intent.setData(data);
//		// Start.context.sendBroadcast(intent);
//
//		//2017.6.19 cahe upload homework pic
//
//		
//		
//		
//		
//		return true;
//	}

	// try {
	// if (!file.exists()) {//文件不存在则创建
	// file.createNewFile();
	// }
	// fos=new FileOutputStream(file,false);
	//
	// fos.write(doc.html().getBytes());//写入文件内容
	// fos.flush();
	// } catch (IOException e) {
	// System.err.println("文件创建失败");
	// }finally{
	// if (fos!=null) {
	// try {
	// fos.close();
	// } catch (IOException e) {
	// System.err.println("文件流关闭失败");
	// }
	// }
	// }
	// 2016.3.24保存批改结果xml
	public static void saveXML(File file) {
		FileOutputStream fos = null;
		try {
			if (!file.exists()) {// 文件不存在则创建
				file.createNewFile();
			}
			fos = new FileOutputStream(file, false);

			fos.write(doc.html().getBytes());// 写入文件内容
			fos.flush();
		} catch (IOException e) {
			System.err.println("文件创建失败");
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					System.err.println("文件流关闭失败");
				}
			}
		}
	}


public class MyClickListener implements GestureView.OnGestureListener{
	// 2016.4.12解决不同笔画数手势问题
	int lastStrokeCount;
	public GestureOverlayView overlay;
	public MotionEvent event;
	Handler handler = new Handler();

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_UP
					&& (overlay.getGesture().getLength() > 10)) {
				handler.postDelayed(this, 1000);
				lastStrokeCount = overlay.getGesture()
						.getStrokesCount();

				// Log.i("strokeevent",
				// String.valueOf(lastStrokeCount));
				switch (lastStrokeCount) {
				case 1:

					ArrayList<Prediction> predictionswr = gestureLib
							.recognize(overlay.getGesture());
					if (predictionswr.size() > 0) {
						Prediction prediction = (Prediction) predictionswr
								.get(0);//hu
						if (prediction.score > 2) {
							judge = prediction.name;
							if (judge.equals("Almost"))
								judge = "Right";
							Toast.makeText(mContext, judge,
									Toast.LENGTH_SHORT).show();
						}

						else {
							judge = "ignore";
							Toast.makeText(mContext, "写字",
									Toast.LENGTH_SHORT).show();
						}

					}
					break;
				case 2:

					ArrayList<Prediction> predictions = gestureLib
							.recognize(overlay.getGesture());
					if (predictions.size() > 0) {
						Prediction prediction1 = (Prediction) predictions
								.get(0);
						if (prediction1.score > 4) {
							judge = prediction1.name;
							if (judge.equals("Right"))
								judge = "Almost";
							Toast.makeText(mContext, judge,
									Toast.LENGTH_SHORT).show();
						}

						else
							judge = "ignore";
						Toast.makeText(mContext, "写字",
								Toast.LENGTH_SHORT).show();
					}
					// Toast.makeText(mContext, "错", Toast.LENGTH_SHORT)
					// .show();
					break;
				case 3:
					judge = "错3";
					Toast.makeText(mContext, "半对2", Toast.LENGTH_SHORT)
							.show();
					break;
				case 4:
					judge = "错4";
					Toast.makeText(mContext, "半对3", Toast.LENGTH_SHORT)
							.show();
					break;
				default:
					lastStrokeCount = 0;
				}
//switch到这结束						
				int situation = 5;

				if (judge.equals("Right"))
					situation = 0;
				else if (judge.equals("wrong"))
					situation = 1;
				else if (judge.equals("Almost"))
					situation = 2;
				else if (judge.equals("错3"))
					situation = 3;
				else if (judge.equals("错4"))
					situation = 4;

				for (currentItem = 1; currentItem < totalQuestion; currentItem++) {
					if (currentItem == 4) {
						System.arraycopy(commentStringReplacement1, 0,
								commentString, 0, 6);
						Log.i("whichcomment", ""
								+ SideDownMode.whichComment);

					}

					else
						System.arraycopy(commentStringReplacement2, 0,
								commentString, 0, 6);

					Spanned text;
					// 格式 =
					// Html.fromHtml("<font color=red><b>"+currentItem+"</b></font>");

					if ((yy > pos[currentItem - 1])
							&& (yy < pos[currentItem])) {
						Log.i("prediction", "0122:i" + currentItem + "+"
								+ pos[currentItem]);
						Log.i("prediction", "0122:y="+yy);
						switch (situation) {
						case 0:
							question[currentItem - 1].right++;
							elementResult.get(currentItem-1).text("对");
							text = Html
									.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=yellow><b>"
											+ question[currentItem - 1].right
											+ "</b></font>"
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].wrong
											+ ""
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].weird
											+ ""
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].weird1
											+ ""
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].weird2
											+ "");
							statisticTextView[currentItem - 1]
									.setText(text);
							break;
						case 1:
							question[currentItem - 1].wrong++;
							elementResult.get(currentItem - 1)
									.text("错");
							text = Html
									.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].right
											+ ""
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=white><b>"
											+ question[currentItem - 1].wrong
											+ "</b></font>"
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].weird
											+ ""
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].weird1
											+ ""
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].weird2
											+ "");
							statisticTextView[currentItem - 1]
									.setText(text);
							break;
						case 2:
							question[currentItem - 1].weird++;
							unrevisedCount++;
							elementResult.get(currentItem - 1).text(
									"有问题");
							text = Html
									.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].right
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].wrong
											+ ""
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=yellow><b>"
											+ question[currentItem - 1].weird
											+"</b></font>"
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].weird1
											+ ""
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].weird2
											+ "");
							statisticTextView[currentItem - 1]
									.setText(text);
							break;
						case 3:
							question[currentItem - 1].weird1++;
							elementResult.get(currentItem - 1).text(
									"有问题1");
							text = Html
									.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].right
											+ ""
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].wrong
											+ ""
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].weird
											+ ""
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=white><b>"
											+ question[currentItem - 1].weird1
											+ "</b></font>"
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].weird2
											+ "");
							statisticTextView[currentItem - 1]
									.setText(text);
							break;
						case 4:
							question[currentItem - 1].weird2++;
							elementResult.get(currentItem - 1).text(
									"有问题2");
							text = Html
									.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].right
											+ ""
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].wrong
											+ ""
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].weird
											+ ""
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
											+ question[currentItem - 1].weird1
											+ ""
											+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=white><b>"
											+ question[currentItem - 1].weird2
											+ "</b></font>");
							statisticTextView[currentItem - 1]
									.setText(text);
							break;
						default:
							// question[i].weird++;
							break;

						}

						Log.i("prediction", "" + situation + " "
								+ question[7].right + "  "
								+ question[7].wrong);
						Log.i("prediction", "i" + currentItem + "+"
								+ pos[currentItem]);

						statisticTextView[currentItem - 1]
								.setVisibility(View.VISIBLE);

						statisticTextView[currentItem - 1]

						.setBackgroundColor(Color.argb(75, 99, 99, 99));

						statisticTextView[currentItem - 1]
								.setTextSize(21);
						statisticTextView[currentItem - 1]
								.setTextColor(Color.WHITE);
						// statisticTextView[currentItem -
						// 1].setBackgroundResource(R.drawable.corner_textview);
						statisticTextView[currentItem - 1]
								.setBackgroundResource(R.drawable.st_);

						Log.i("sqldb", "update");
//						DatabaseOp.update(Start.db, Start.gCurPageID,currentItem - 1,
//								question[currentItem - 1].right,
//								question[currentItem - 1].wrong,
//								question[currentItem - 1].weird,
//								question[currentItem - 1].weird1,
//								question[currentItem - 1].weird2);

						break;
					} else {
						// my_toast("不在判定区域");
						continue;

					}

					// my_toast("对"+String.valueOf(question[i].right));
				}

				handler.removeCallbacks(runnable);
			}

		}

	};
	
	@Override
	public void onGesture(GestureOverlayView overlay, MotionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
		// TODO Auto-generated method stub
		yy = gestures.y;
		
		this.overlay = overlay;
		this.event = event;
		Log.i("prediction", "y=" + yy);
		handler.postDelayed(runnable, 1500);
	}

	
	
	@Override
	public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
		// TODO Auto-generated method stub
		
	}
	
}
public void setTrans(int situation, float y){
	// 2016.4.12解决不同笔画数手势问题
	
	Spanned text;
	float yy;
	yy = (float) (y);
		Log.i("prediction","totalQuestion = "+totalQuestion);
		Log.i("prediction","pos0="+pos[0]);
		Log.i("prediction","pos1="+pos[1]);
		Log.i("prediction","pos2="+pos[2]);
	Log.i("prediction", "0122:yy="+yy);
	if (situation==13) {//在批改环出现的情况下双击
		pigaihuanLayout.setVisibility(View.GONE);
		pigaiResultImageView.setVisibility(View.GONE);
//		pigaihuanPingyuText.setVisibility(View.VISIBLE);//0426
		return;
	}
	else if (situation ==12) {//双击，弹出批改环，注意要修改双击位置的逻辑
		pigaihuanLayout.setVisibility(View.VISIBLE);
		int minutetemp=(int) (+Start.timePerItem /1000/60);	
		int secondtemp=(int) (+Start.timePerItem/1000)%60;
		totalTimeTv.setText("此题耗时"+minutetemp+"'"+secondtemp+"\""+"当前排名2名");
//		totalTimeTv.setText("此题共用时"+Start.timePerItem+"分钟");
		return;
	}
	if(sceneSituation == 0)return;
	for (currentItem = 1; currentItem < totalQuestion; currentItem++) {
         Log.i("crash","current="+currentItem+"||pos-1="+(pos[currentItem-1]/11.0));
         Log.i("crash","current="+currentItem+"||pos="+(pos[currentItem]/11.0));
		if ((yy > pos[currentItem - 1]/11.0)&& (yy < pos[currentItem]/11.0)) {
			
//			statisticTextView[2].setText("pppppppppppppppp");
//			statisticTextView[currentItem-1].setText("iiiiiiiiiiiiiiiiiii");
			Log.i("prediction", "0122:" + currentItem + "+"
					+ pos[currentItem]);
			Log.i("prediction", "0122:y="+yy);
			Log.i("crash","c||"+situation+"c||"+pos[currentItem-1]+"c||"+yy+"c||"+currentItem);
			switch (situation) {
			case 0:
				question[currentItem - 1].right++;
				elementResult.get(currentItem-1).text("对");
				text = Html
						.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=yellow><b>"
								+ question[currentItem - 1].right
								+ "</b></font>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird1
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird2
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].wrong
								+ "");
				statisticTextView[currentItem - 1]
						.setText(text);
				
				
				
				
				setTransparentStatisticView(statisticTextView[currentItem - 1]);
				

				break;
			case 2:
				question[currentItem - 1].weird++;
				elementResult.get(currentItem - 1)
						.text("有问题");
				text = Html
						.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].right
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=yellow><b>"
								+ question[currentItem - 1].weird
								+ "</b></font>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird1
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird2
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].wrong
								+ "");
				statisticTextView[currentItem - 1]
						.setText(text);
				setTransparentStatisticView(statisticTextView[currentItem - 1]);
				break;
			case 3:
				question[currentItem - 1].weird1++;
				unrevisedCount++;
				elementResult.get(currentItem - 1).text(
						"有问题1");
				text = Html
						.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].right
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=yellow><b>"
								+ question[currentItem - 1].weird1
								+"</b></font>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird2
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].wrong
								+ "");
				statisticTextView[currentItem - 1]
						.setText(text);
				setTransparentStatisticView(statisticTextView[currentItem - 1]);
				break;
			case 4:
				question[currentItem - 1].weird2++;
				elementResult.get(currentItem - 1).text(
						"有问题2");
				text = Html
						.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].right
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird1
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=yellow><b>"
								+ question[currentItem - 1].weird2
								+ "</b></font>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].wrong
								+ "");
				statisticTextView[currentItem - 1]
						.setText(text);
				setTransparentStatisticView(statisticTextView[currentItem - 1]);
				break;
			case 1:
				question[currentItem - 1].wrong++;
				elementResult.get(currentItem - 1).text(
						"错");
				text = Html
						.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].right
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird1
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird2
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=yellow><b>"
								+ question[currentItem - 1].wrong
								+ "</b></font>");
				statisticTextView[currentItem - 1]
						.setText(text);
				setTransparentStatisticView(statisticTextView[currentItem - 1]);
				break;
			case 12://在客观题区域双击，弹出批改环
                pigaihuanLayout.setVisibility(View.VISIBLE);
                pigaiResultImageView.setVisibility(View.GONE);
                pigaihuanPingyuText.setVisibility(View.GONE);
				break;

			case 21:
				my_toast("-1");
				break;
			case 22:
				my_toast("-2");
				text = Html
						.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=yellow><b>"
								+ question[currentItem - 1].right
								+ "</b></font>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird1
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird2
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].wrong
								+ "");
				statisticTextView[currentItem - 1]
						.setText(text);
				setTransparentStatisticView1(statisticTextView[currentItem - 1]);
				break;
			case 23:
				my_toast("-3");
				break;
			case 24:
				my_toast("-4");
				text = Html
						.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].right
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=yellow><b>"
								+ question[currentItem - 1].weird
								+ "</b></font>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird1
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird2
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].wrong
								+ "");
				statisticTextView[currentItem - 1]
						.setText(text);
				setTransparentStatisticView1(statisticTextView[currentItem - 1]);
				break;
			case 25:
				my_toast("-5");
				break;
			case 26:
				my_toast("-6");
				text = Html
						.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].right
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=yellow><b>"
								+ question[currentItem - 1].weird1
								+"</b></font>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird2
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].wrong
								+ "");
				statisticTextView[currentItem - 1]
						.setText(text);
				setTransparentStatisticView1(statisticTextView[currentItem - 1]);
				break;
			case 27:
				my_toast("-7");
				break;
			case 28:
				my_toast("-8");
				text = Html
						.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].right
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].weird1
								+ ""
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=yellow><b>"
								+ question[currentItem - 1].weird2
								+ "</b></font>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ question[currentItem - 1].wrong
								+ "");
				statisticTextView[currentItem - 1]
						.setText(text);
				setTransparentStatisticView1(statisticTextView[currentItem - 1]);
				break;
			case 29:
				my_toast("-9");
				break;
			
			default:
				// question[i].weird++;
				break;

			}

			

			Log.i("sqldb", "update");//0415
//			DatabaseOp.update(Start.db, Start.gCurPageID, currentItem - 1,
//					question[currentItem - 1].right,
//					question[currentItem - 1].wrong,
//					question[currentItem - 1].weird,
//					question[currentItem - 1].weird1,
//					question[currentItem - 1].weird2);

			break;
		} else {
			// my_toast("不在判定区域");
			continue;

		}
	}
}
public void setTransparentStatisticView(TextView v) {
	//setTransparentStatisticView(statisticTextView[currentItem - 1]);
	v.setVisibility(View.VISIBLE);
	v.setBackgroundColor(Color.argb(75, 99, 99, 99));
	v.setTextSize(21);
	v.setTextColor(Color.BLACK);
	v.setBackgroundResource(R.drawable.st);
}

//2019.5.30
public void setTransparentStatisticView1(TextView v) {
	//setTransparentStatisticView(statisticTextView[currentItem - 1]);
	v.setVisibility(View.VISIBLE);
	v.setBackgroundColor(Color.argb(75, 99, 99, 99));
	v.setTextSize(21);
	v.setTextColor(Color.BLACK);
	v.setBackgroundResource(R.drawable.st_);
}

public void doDownLoadTask(final Context context ) {
	new AlertDialog.Builder(context).setTitle("确认").setMessage("是否下载？")
	.setPositiveButton("是", new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onClick 1 = " + which);
			doDownLoadWork(context);
			
		}
	}).setNegativeButton("否", new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onClick 2 = " + which);
		}
	}).show();
	
}
public void doDownLoadWork(Context context) {
	DownLoaderTask task = new DownLoaderTask(
			"http://118.24.109.3/Public/smartpen/download.php",
			"/sdcard/xyz/", context);

	// DownLoaderTask task = new
	// DownLoaderTask("http://192.168.9.155/johnny/test.h264",
	// getCacheDir().getAbsolutePath()+"/", this);
	task.execute();
}
//统计文件夹下文件数目
public static int getFileNumber(String filePath) {
	int number = 0;
	String filename;
	File file = new File(filePath);
	if(file.exists()) {
	File[] listFile = file.listFiles();

	for(int i = 0;i<listFile.length;i++) {
		String filepathname = listFile[i].getPath();
		String ori = filepathname.substring(filepathname.lastIndexOf("/"));
		filename  = ori.substring(ori.lastIndexOf(".")+1,ori.length());

		if(filename.equals("page"))
		number++;
		
	}
	}
	
	else number = 1;
	
	return number;
	
}

//清空目录
public static boolean deleteDir(String path){
	File file = new File(path);
	if(!file.exists()){//判断是否待删除目录是否存在
		System.err.println("The dir are not exists!");
		return false;
	}
	
	String[] content = file.list();//取得当前目录下所有文件和文件夹
	for(String name : content){
		File temp = new File(path, name);
		if(temp.isDirectory()){//判断是否是目录
			deleteDir(temp.getAbsolutePath());//递归调用，删除目录里的内容
			temp.delete();//删除空目录
		}else{
			if(!temp.delete()){//直接删除文件
				System.err.println("Failed to delete " + name);
			}
		}
	}
	return true;
}



public void setActivity(Start activity) {
	this.activity = activity;
}




}




