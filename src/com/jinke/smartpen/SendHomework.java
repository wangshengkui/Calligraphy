package com.jinke.smartpen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;

import com.google.common.collect.ArrayListMultimap;

public class SendHomework {
	
	public static testxml testnewxml = new testxml();
	static Document doc;
    static String pagexml;
    static ArrayList<Integer> zuoyeitem = new ArrayList<Integer>();
	
	//wsk 20190117 均值
    public static float Meany(List<points> temp)
    {
    	float mean = 0;
    	float sumy = 0;
    	for(int i = 0;i<temp.size();i++)
    	{
    		sumy = sumy + temp.get(i).pointy;
    	}
    	mean = sumy/temp.size();
    	return mean;
    }
    
    //wsk 20190117 方差
    public float Vary(List<points> temp,float mean)
    {
    	float sum = 0;
    	float var = 0;
    	for(int i = 0;i<temp.size();i++)
    	{
    		sum = sum + (temp.get(i).pointy-mean)*(temp.get(i).pointy-mean);
    	}
    	var = sum/temp.size();
    	return var;
    }
    
    //wsk 20190117 均值
    public static float Meanx(List<points> temp)
    {
    	float mean = 0;
    	float sumy = 0;
    	for(int i = 0;i<temp.size();i++)
    	{
    		sumy = sumy + temp.get(i).pointx;
    	}
    	mean = sumy/temp.size();
    	return mean;
    }
	
    public static float OverGesture(List<points> temp)
    {
    	if(temp.size()>0)
    	{
    		return Math.abs(temp.get(temp.size()-1).pointx-temp.get(0).pointx);
    	}
    	else return 0;
    }
    
	static public ArrayList<Integer> ReceiveSendHomenworkGestureDots(ArrayListMultimap<Integer, points> points_number,int bookID,int pageID)
	{
		ArrayList<Integer> tag = new ArrayList<Integer>();
		ArrayList<Integer> TiHao = new ArrayList<Integer>();
    	List<points> points = points_number.get(bookID);
    	int zuoyeitem;
    	boolean IsSend = false;
    	
    	pagexml = "book_"+bookID+"_page_"+(pageID%20)+".xml";
    	
    	if(points.size() == 0)
    	{
    		return null;
    	}
    	
    	File file = new File("/sdcard/xml/" +pagexml);
    	try 
    	{
    		doc = Jsoup.parse(file, "UTF-8");
    	
    	 }
    	catch (IOException e) 
    	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    	//得到区域，如果是题号区，在这里布置作业
    	tag = testxml.test(Meanx(points),Meany(points), bookID,pageID);
    	float meanyy = Meany(points);
    	float meanxx = Meanx(points);
    	if(tag == null)
    	{
    		return null;
    	}
    	
    	else
    	{
    		//1为组号
    		if(tag.get(1) == 1)
    		{
    			if(OverGesture(points)>3.5)
    			{
    				zuoyeitem = tag.get(0);
        			//调用发送作业函数
        			TiHao.add(HomeworkContnet(zuoyeitem,bookID, pageID));
        			return TiHao;
    			}
    			else
    			{
    				return null;
				}
    		}
    		//2为题号区
    		else if(tag.get(1) == 2)
    		{
    			zuoyeitem = tag.get(0);
    			//调用发送作业函数
    			TiHao.add(HomeworkContnet(zuoyeitem, bookID,pageID));
    			//如果是录音手势，发送作业，播放语音
    			if(OverGesture(points)>3.5)
        		{
        			TiHao.add(1);
        			TiHao.add(JudgeGroup(meanxx,meanyy,bookID,pageID));
        			return TiHao;
        		}
        		
    			//如果是圆圈，将当前作业加入队列
        		else
        		{
        			TiHao.add(0);
        			TiHao.add(JudgeGroup(meanxx,meanyy,bookID,pageID));
        			return TiHao;
        		}
    		}
    	}
    	return null;
	}
	
	static Document doc2;
    static String pagexml2;
	private static Integer JudgeGroup(float meanxx, float meanyy, int bookID, int pageID) {
		// TODO Auto-generated method stub
        pagexml2 = "book_"+bookID+"_page_"+(pageID%20)+".xml";
    	
    	File file = new File("/sdcard/xml/" +pagexml2);
    	try 
    	{
    		doc2 = Jsoup.parse(file, "UTF-8");
    	
    	 }
    	catch (IOException e) 
    	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	Elements element = doc2.getElementsByTag("itemnumber");
    	Elements quyu = doc2.getElementsByTag("quyu");
    	if(meanyy > Double.valueOf(quyu.get(1).getElementsByTag("y2").text().toString()))
    	{
    		if(element.get(1).text().toString().equals("A") == true)
    		{
    			return -1;
    		}
    		else
    		{
    			return -2;
    		}
    	}
    	
    	else
    	{
    		if(element.get(1).text().toString().equals("A") == true)
    		{
    			return -2;
    		}
    		else
    		{
    			return -1;
    		}
    	}
	}

	static Document doc1;
    static String pagexml1;
    static ArrayList<Integer> timu = new ArrayList<Integer>();
    @SuppressLint("SdCardPath")
	@SuppressWarnings({ "unlikely-arg-type", "unused", "null" })
	static public int HomeworkContnet(int temp,int BookID,int PageID)
    {
    	
    	pagexml1 = "book_"+BookID+"_page_"+(PageID%20)+".xml";
    	
    	File file = new File("/sdcard/xml/" +pagexml1);
    	try 
    	{
    		doc1 = Jsoup.parse(file, "UTF-8");
    	
    	 }
    	catch (IOException e) 
    	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    	Elements element = doc1.getElementsByTag("itemnumber");
    	
    	
    	float tihao;
    	
    	//返回0代表页眉
    	if(temp == 0)
    	{
    		return 0;
    	}
    	
    	//-1代表A组，-2代表B组
    	else if(temp == 1)
    	{
    		if(element.get(1).text().toString().equals("A") == true)
    		{
    			return -1;
    		}
    		else
    		{
    			return -2;
    		}
    	}
    	else
    	{
    		tihao = Float.valueOf(element.get(temp).text().toString());
    	}
    	
    	
    	return (int)tihao;
    }


}
