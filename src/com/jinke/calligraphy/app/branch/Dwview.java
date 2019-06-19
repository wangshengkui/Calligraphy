package com.jinke.calligraphy.app.branch;


import com.jinke.horizontallistview.HorizontalListView;
import com.jinke.horizontallistview.HorizontalListViewAdapter;

import android.app.Notification.Action;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Dwview extends View {
	// 获得屏幕的尺寸,三星平板屏幕为：1600x2560（单位px)
	// private float maxwidth=480;//批改环的总宽，单位默认为sp
	// private float maxheight=280;//批改环的总高，单位默认为sp
	// private float tabwidth=30;//表格宽度，单位默认为sp
	// 方法1 Android获得屏幕的宽和高
	//private float a=2;
	/**
	 *zgm2016.12.3 
	 * 
	 * 
	 */
	private int huabitoumingdu;	
	private int color2;
	
	
	
	
	
	
	
	private float blankx=1200;//空白区域的宽
	private float blanky=900;//空白区域的高
	private float b=1;      // 批改环宽度比例因子
	private float band=200/b;//批改环的环的宽度
	private float px=0+(200-band);//批改环中心的坐标x的偏移量
	private float py=800+(200-band);//批改环中心的坐标y的偏移量
	private float x=(blankx+2*band)/2+px;//批改环中心的坐标x
    private float y=(blanky+2*band)/2+py;//批改环中心的坐标y
	
	
	private float halfblankx=blankx/2;
	private float halfblanky=blanky/2;
	private float halfband=band/2;
	
    
    private float insidelx=x-halfblankx;
    private float insidely=y-halfblanky;
    private float insiderx=x+halfblankx;
    private float insidery=y+halfblanky;
    
    private float outsidelx=x-halfblankx-band;
    private float outsidely=y-halfblanky-band;
    private float outsiderx=x+halfblankx+band;
    private float outsidery=y+halfblanky+band;
	//private static final int A0F = 0;
	private Button btn;
    public int a=0;
	
	//public void btn(){
	//btn = new Button(getContext());  
    //btn.setLayoutParams(layoutParamsButtonOK);
    //btn.addView(btn);  
    //btn.setMaxLines(50);  
    //btn.setTextSize(100);  
    //btn.setText("确定");  
    //btn.setId(5);
   //Context con=getContext();
    //Log.d(VIEW_LOG_TAG, "btn");
    //}
  

	
	public Dwview(Context context) {
		super(context);
	}
	
	

	
	
	
	
	@Override
	// 重写该方法，进行绘图
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	canvas.clipRect(0, 799-a, 1600, 2100);
        //this.btn();
		// 获得屏幕的尺寸,三星平板屏幕为：1600x2560（单位px)
		
		
	 
		
		 DisplayMetrics dm2 = getResources().getDisplayMetrics();
//		 float heightPixels=dm2.heightPixels;
//		 float widthPixels =dm2.widthPixels;
		// 将px转化为dp
		// final float scale =dm2.density;
		// float widthdp=widthPixels/scale + 0.5f;
          
	     System.out.println("widthPixels： " +dm2.widthPixels);
		 System.out.println("heightPixels " +dm2.heightPixels);

		// 把整张画布绘制成灰色
		canvas.drawColor(Color.WHITE);//绘制透明色  
//		canvas.setAlpha(0);
		
		
		Paint mpaint = new Paint();
		
		
		//cahe 2016.12.13
	
		
		

		mpaint.setAntiAlias(true);// // 去锯齿
		// mpaint.setColor(Color.BLUE);
		//mpaint.setStyle(Paint.Style.STROKE);// 设置
		//mpaint.setStrokeWidth(3);

		// 绘制批改环绿色带
		mpaint.setColor(0xee153013);// 设置画笔颜色
		mpaint.setStrokeWidth((float) band);// 设置线宽,300为绿色带的边宽，表格宽为200；
		mpaint.setStyle(Style.STROKE);// 设置画笔为空心
		// canvas.drawCircle(40, 40, 30,mpaint);//画实验圆
		canvas.drawRect(insidelx-halfband, insidely-halfband, insiderx+halfband,insidery+halfband, mpaint);// 画绿色带，矩形AB1436=1600-100-64；1500=1600-100；1200=200+900+100

		// 抹去条带上边的100
		// mpaint.setColor(Color.WHITE);//设置画笔颜色
		// mpaint.setStrokeWidth((float) 100.0);//设置线宽
		// canvas.drawLine(300,250,1236（1300） ,250,
		// mpaint);//画直线CD;1236=1600-364=1600-64-150-150；1300=1600-150-150；(减64是因为画布没有完全占据屏幕宽，其值约为64）,将xml文件中的RelativeLayout改成LinearLayout即可将画布改成完全占据屏宽
		// 225=100+50

		// 空白区域高900，宽1136=1600-64-200-200；（改过后1200=1600-200-200）

		// 表格边线颜色和宽度的设置
		mpaint.setColor(Color.BLUE);// 设置画笔颜色
		mpaint.setStrokeWidth((float) 4);// 设置线宽4

		// 整个表格外边线，一个矩形
		canvas.drawRect(outsidelx+2, outsidely+2,  outsiderx-2, outsidery-2, mpaint);// 1534=1600-64-2；1598=1600-2；1298=900+200+200-2

		// 上边表格内线EF
		canvas.drawLine(insidelx-band, insidely,  outsiderx,insidely, mpaint);// 1536=1600-64；

		// 左表格内线IJ:
		canvas.drawLine(insidelx, outsidely,insidelx ,insidely, mpaint);
		canvas.drawLine(insidelx- halfband,insidely, insidelx- halfband, insidery, mpaint);// 1100=900+200
		canvas.drawLine(insidelx, insidery, insidelx, outsidery, mpaint);// 

		// 下表格内线KL
		canvas.drawLine(outsidelx, insidery, insidelx, insidery, mpaint);// 1100=900+200；
		canvas.drawLine(insidelx, insidery+halfband, insiderx, insidery+halfband, mpaint);// 1336=1600-64-200；1400=1600-200
		canvas.drawLine(insiderx, insidery,outsiderx,insidery, mpaint);// 1536=1600-64；

		// 右表格内线MN
		canvas.drawLine(insiderx, outsidery, insiderx, insidery, mpaint);// 1336=1600-200-64；1400=1600-200
		canvas.drawLine(insiderx+halfband, insidery, insiderx+halfband, insidely, mpaint);// 1436=1600-200-64+100；1500=1600-200+100
		canvas.drawLine(insiderx,insidely, insiderx,outsidely, mpaint);// 1336=1600-200-64；1400=1600-200
		// 左上角表头斜线
		canvas.drawLine(outsidelx, outsidely, insidelx,insidely, mpaint);

		// 左下角表头斜线
		canvas.drawLine(outsidelx,outsidery,insidelx, insidery, mpaint);

		// 右下角表头斜线
		canvas.drawLine(insiderx, insidery, outsiderx, outsidery, mpaint);// 1336=166-64-200;1400=1600-200

		// 右上角表头斜线
		canvas.drawLine(insiderx,insidely, outsiderx,outsidely, mpaint);// 1336=166-64-200;1400=1600-200

		// 对表格划分单元格
		// mpaint.setColor(Color.rgb(100,100,200));//设置划分线的颜色
		mpaint.setColor(Color.BLUE);// 设置划分线的颜色
		mpaint.setStrokeWidth((float) 2);// 设置线宽

		// 对上边的表格划分单元格，每个格宽blankx/5
		canvas.drawLine(insidelx+blankx/5,   outsidely,   insidelx+blankx/5,   insidely, mpaint);// 427=200+227
		canvas.drawLine(insidelx+2*blankx/5, outsidely, insidelx+2*blankx/5, insidely, mpaint);// 654=427+227
		canvas.drawLine(insidelx+3*blankx/5, outsidely, insidelx+3*blankx/5, insidely, mpaint);// 881=654+227
		canvas.drawLine(insidelx+4*blankx/5, outsidely,  insidelx+4*blankx/5, insidely, mpaint);// 1108=881+227

		// 对左边的表格划分单元格，每个格宽blanky/5
		canvas.drawLine(outsidelx, insidely+blanky/5,    insidelx-halfband, insidely+blanky/5, mpaint);// 380=200+180
		canvas.drawLine(outsidelx, insidely+2*blanky/5,  insidelx-halfband, insidely+2*blanky/5, mpaint);// 560=380+180
		canvas.drawLine(outsidelx, insidely+3*blanky/5,  insidelx-halfband, insidely+3*blanky/5, mpaint);// 720=560+180
		canvas.drawLine(outsidelx, insidely+4*blanky/5,  insidelx-halfband, insidely+4*blanky/5, mpaint);// 900=720+180

		// 对下边的表格划分单元格，每个格宽227,最右边的一个228
		canvas.drawLine(insidelx+blankx/5,  insidery+halfband, insidelx+blankx/5, outsidery, mpaint);// 427=200+227
		canvas.drawLine(insidelx+2*blankx/5,insidery+halfband, insidelx+2*blankx/5, outsidery, mpaint);// 654=427+227
		canvas.drawLine(insidelx+3*blankx/5,insidery+halfband,insidelx+3*blankx/5, outsidery, mpaint);// 881=654+227
		canvas.drawLine(insidelx+4*blankx/5,insidery+halfband, insidelx+4*blankx/5, outsidery, mpaint);// 1108=881+227

		// 对右边的表格划分单元格，每个格宽200
		canvas.drawLine(insiderx+halfband, insidely+blanky/5,  outsiderx, insidely+blanky/5, mpaint);// 380=200+180
		canvas.drawLine(insiderx+halfband, insidely+2*blanky/5, outsiderx, insidely+2*blanky/5, mpaint);// 560=380+180
		canvas.drawLine(insiderx+halfband, insidely+3*blanky/5, outsiderx,insidely+3*blanky/5, mpaint);// 720=560+180
		canvas.drawLine(insiderx+halfband, insidely+4*blanky/5, outsiderx, insidely+4*blanky/5, mpaint);// 900=720+180

		mpaint.setColor(Color.WHITE);
		mpaint.setTextSize(band*45/200);
		// 画出上边表格表头字符串
		canvas.drawText("缺陷等", outsidelx+band*65/200,  outsidely+band*50/200, mpaint);
		canvas.drawText("级手", outsidelx+band*110/200,  outsidely+band*100/200, mpaint);
		canvas.drawText("势", outsidelx+band*155/200,   outsidely+band*150/200, mpaint);

		// 画出左边边表格表头字符串
		canvas.drawText("智", outsidelx+band*30/200,  outsidely+band*125/200, mpaint);
		canvas.drawText("力句段", outsidelx+band*10/200, outsidely+band*180/200, mpaint);

		// 画出下边边表格表头字符串
		canvas.drawText("情", insiderx+band*30/200, insidery+band*125/200, mpaint);// 1366=1336+30;1225=1100+125
		canvas.drawText("感句段",insiderx+band*10/200, insidery+band*180/200, mpaint);// 1346=1336+10;1280=1100+180

		// 画出右边边表格表头字符串
		canvas.drawText("非智力", insiderx+band*64/200, insidery+band*50/200, mpaint);// 1406=1336+64;1150=1100+50
		canvas.drawText("句段",  insiderx+band*104/200, insidery+band*100/200, mpaint);// 1430=1336+104；1200=1100+100
	    
		
		
		// *****************************************************//左边表格从上到下二级菜单1（知识残缺）**************************************
		mpaint.setStrokeWidth((float) 4);// 设置线宽
		
		mpaint.setColor(color2);

		
		 
		mpaint.setAlpha(huabitoumingdu);// 设置透明度
		mpaint.setStyle(Style.STROKE);// 设置画笔为空心
		canvas.drawRect(insidelx - halfband, insidely, insidelx, insidery,
				mpaint);
		
		// 的表格划分为单元格，每个格宽blanky/5
		canvas.drawLine(insidelx - halfband, insidely + blanky / 5,
				insidelx, insidely + blanky / 5, mpaint);
		canvas.drawLine(insidelx - halfband, insidely + 2 * blanky / 5,
				insidelx, insidely + 2 * blanky / 5, mpaint);
		canvas.drawLine(insidelx - halfband, insidely + 3 * blanky / 5,
				insidelx, insidely + 3 * blanky / 5, mpaint);
		canvas.drawLine(insidelx - halfband, insidely + 4 * blanky / 5,
				insidelx, insidely + 4 * blanky / 5, mpaint);

		
		

	/**
	 * zgm
	 *2016.12.13 	
	 */
		
		

	}

	private LayoutInflater getLayoutInflater() {
		// TODO Auto-generated method stub
		return null;
	}

	public void transparency(int huabitoumingdu) {
		// TODO Auto-generated method stub

		this.huabitoumingdu=huabitoumingdu;
		
	}

	public void setcolor(int color) {
		// TODO Auto-generated method stub
		 color2 = color;
	}
	
	
	
	
	
	
	
	
	
	}






