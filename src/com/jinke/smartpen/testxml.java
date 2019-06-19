package com.jinke.smartpen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.widget.FrameLayout;

public class testxml {
	
	static Document doc;
    static String pagexml;
    public static ArrayList<Integer> test(float x,float y, int bookid,int pageid)
    {
    	switch(bookid)
    	{
    	case 0:pagexml = "book_"+bookid+"_page_"+(pageid%20)+".xml";
    	break;
    	case 1:pagexml = "book_"+bookid+"_page_"+(pageid%8)+".xml";
    	break;
    	default:break;
    	}
    	int i = 0;
    	int ystart;
    	int yend;
    	
    	int count = 2;//计数器,页眉和组只有quyu属性，在使用其他属性时要减去模板中的页眉和组的数量
    	
    	ArrayList<Integer> tag = new ArrayList<Integer>();
    	
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
    
    	if (doc==null) {
			return null;
		}
    	Elements item = doc.getElementsByTag("item");
    	Elements type = doc.getElementsByTag("type");
    	Elements quyu = doc.getElementsByTag("quyu");
    	Elements tihaoqu = doc.getElementsByTag("tihaoqu");
    	Elements tiganqu = doc.getElementsByTag("tiganqu");
    	Elements datiqu = doc.getElementsByTag("datiqu");
    	
    	
    	//0-页眉  1-组  2-题号区  3-答题区  4-题干区  5-留白区
    	for(i = 0;i<item.size();i++)
    	{
    		//根据区域坐标判断属于哪个区域（题目/页眉/组）
    		if(y>Double.valueOf(quyu.get(i).getElementsByTag("y1").text().toString()) && y<Double.valueOf(quyu.get(i).getElementsByTag("y2").text().toString()) && x>Double.valueOf(quyu.get(i).getElementsByTag("x1").text().toString()) && x<Double.valueOf(quyu.get(i).getElementsByTag("x2").text().toString()))
    		{
    			//判断页眉
    			if(type.get(i).text().toString().equals("页眉") == true)
    			{
    				tag.add(i);
    				tag.add(0);
    				break;
    			}
    			
    			//判断组
    			else if(type.get(i).text().toString().equals("组") == true)
    			{
    				tag.add(i);
    				tag.add(1);
    				break;
    			}
    			
    			//判断题目区域
    			else
    			{
    				//判断题号区
    				if(x>Double.valueOf(tihaoqu.get(i-count).getElementsByTag("x1").text().toString()) && x<Double.valueOf(tihaoqu.get(i-count).getElementsByTag("x2").text().toString()) && y > Double.valueOf(tihaoqu.get(i-count).getElementsByTag("y1").text().toString()) && y < Double.valueOf(tihaoqu.get(i-count).getElementsByTag("y2").text().toString()))
    				{
    					tag.add(i);
        				tag.add(2);
    					break;
    				}
    				
    				//判断答题区
    				if(Double.valueOf(datiqu.get(i-count).getElementsByTag("count").text().toString()) == 2)
					{
						if(x > Double.valueOf(datiqu.get(i-count).getElementsByTag("x1").text().toString()) && x < Double.valueOf(datiqu.get(i-count).getElementsByTag("x2").text().toString()) && y > Double.valueOf(datiqu.get(i-count).getElementsByTag("y1").text().toString()) && y < Double.valueOf(datiqu.get(i-count).getElementsByTag("y2").text().toString()))
						{
							tag.add(i);
		    				tag.add(3);
							break;
						}
					}
					
					if(Double.valueOf(datiqu.get(i-count).getElementsByTag("count").text().toString()) == 4)
					{
						if(x> Double.valueOf(datiqu.get(i-count).getElementsByTag("x1").text().toString()) && x < Double.valueOf(datiqu.get(i-count).getElementsByTag("x2").text().toString()) && y > Double.valueOf(datiqu.get(i-count).getElementsByTag("y1").text().toString()) && y < Double.valueOf(datiqu.get(i-count).getElementsByTag("y2").text().toString()))
						{
							tag.add(i);
		    				tag.add(3);
							break;
						}
						
						else if(x > Double.valueOf(datiqu.get(i-count).getElementsByTag("x3").text().toString()) && x < Double.valueOf(datiqu.get(i-count).getElementsByTag("x4").text().toString()) && y > Double.valueOf(datiqu.get(i-count).getElementsByTag("y3").text().toString()) && y < Double.valueOf(datiqu.get(i-count).getElementsByTag("y4").text().toString()))
						{
							tag.add(i);
		    				tag.add(3);
							break;
						}
					}
					
					if(Double.valueOf(datiqu.get(i-count).getElementsByTag("count").text().toString()) == 6)
					{
						if(x > Double.valueOf(datiqu.get(i-count).getElementsByTag("x1").text().toString()) && x < Double.valueOf(datiqu.get(i-count).getElementsByTag("x2").text().toString()) && y > Double.valueOf(datiqu.get(i-count).getElementsByTag("y1").text().toString()) && y < Double.valueOf(datiqu.get(i-count).getElementsByTag("y2").text().toString()))
						{
							tag.add(i);
		    				tag.add(3);
							break;
						}
						
						else if(x > Double.valueOf(datiqu.get(i-count).getElementsByTag("x3").text().toString()) && x < Double.valueOf(datiqu.get(i-count).getElementsByTag("x4").text().toString()) && y > Double.valueOf(datiqu.get(i-count).getElementsByTag("y3").text().toString()) && y < Double.valueOf(datiqu.get(i-count).getElementsByTag("y4").text().toString()))
						{
							tag.add(i);
		    				tag.add(3);
							break;
						}
						
						else if(x > Double.valueOf(datiqu.get(i-count).getElementsByTag("x5").text().toString()) && x < Double.valueOf(datiqu.get(i-count).getElementsByTag("x6").text().toString()) && y > Double.valueOf(datiqu.get(i-count).getElementsByTag("y5").text().toString()) && y < Double.valueOf(datiqu.get(i-count).getElementsByTag("y6").text().toString()))
						{
							tag.add(i);
		    				tag.add(3);
							break;
						}
					}
    				
    				//判断题干区
					if(Double.valueOf(tiganqu.get(i-count).getElementsByTag("count").text().toString()) == 2)
					{
						if(x > Double.valueOf(tiganqu.get(i-count).getElementsByTag("x1").text().toString()) && x < Double.valueOf(tiganqu.get(i-count).getElementsByTag("x2").text().toString()) && y > Double.valueOf(tiganqu.get(i-count).getElementsByTag("y1").text().toString()) && y < Double.valueOf(tiganqu.get(i-count).getElementsByTag("y2").text().toString()))
						{
							
							tag.add(i);
		    				tag.add(4);
							break;
						}
					}
					
					if(Double.valueOf(tiganqu.get(i-count).getElementsByTag("count").text().toString()) == 4)
					{
						if(x > Double.valueOf(tiganqu.get(i-count).getElementsByTag("x1").text().toString()) && x < Double.valueOf(tiganqu.get(i-count).getElementsByTag("x2").text().toString()) && y > Double.valueOf(tiganqu.get(i-count).getElementsByTag("y1").text().toString()) && y < Double.valueOf(tiganqu.get(i-count).getElementsByTag("y2").text().toString()))
						{
							
							tag.add(i);
		    				tag.add(4);
							break;
						}
						
						else if(x > Double.valueOf(tiganqu.get(i-count).getElementsByTag("x3").text().toString()) && x < Double.valueOf(tiganqu.get(i-count).getElementsByTag("x4").text().toString()) && y > Double.valueOf(tiganqu.get(i-count).getElementsByTag("y3").text().toString()) && y < Double.valueOf(tiganqu.get(i-count).getElementsByTag("y4").text().toString()))
						{
							
							tag.add(i);
		    				tag.add(4);
							break;
						}
					}
					
					
					if(Double.valueOf(tiganqu.get(i-count).getElementsByTag("count").text().toString()) == 6)
					{
						if(x > Double.valueOf(tiganqu.get(i-count).getElementsByTag("x1").text().toString()) && x < Double.valueOf(tiganqu.get(i-count).getElementsByTag("x2").text().toString()) && y > Double.valueOf(tiganqu.get(i-count).getElementsByTag("y1").text().toString()) && y < Double.valueOf(tiganqu.get(i-count).getElementsByTag("y2").text().toString()))
						{
							
							tag.add(i);
		    				tag.add(4);
							break;
						}
						
						else if(x > Double.valueOf(tiganqu.get(i-count).getElementsByTag("x3").text().toString()) && x < Double.valueOf(tiganqu.get(i-count).getElementsByTag("x4").text().toString()) && y> Double.valueOf(tiganqu.get(i-count).getElementsByTag("y3").text().toString()) && y < Double.valueOf(tiganqu.get(i-count).getElementsByTag("y4").text().toString()))
						{
							
							tag.add(i);
		    				tag.add(4);
							break;
						}
						
						else if(x > Double.valueOf(tiganqu.get(i-count).getElementsByTag("x5").text().toString()) && x < Double.valueOf(tiganqu.get(i-count).getElementsByTag("x6").text().toString()) && y > Double.valueOf(tiganqu.get(i-count).getElementsByTag("y5").text().toString()) && y < Double.valueOf(tiganqu.get(i-count).getElementsByTag("y6").text().toString()))
						{
							tag.add(i);
		    				tag.add(4);
							break;
						}
					}
    				
    				//留白区
    				tag.add(i);
        			tag.add(5);
        			break;
    				
    			}
    		}
    	}
    	
    	
    	if(tag.size()<2)
    	{
    		return null;
    	}
    	
    	else 
    	{
    		if(i>=item.size())
    		{
    			return null;
    		}
//    		ystart = (int)Integer.valueOf(quyu.get(i).getElementsByTag("y1").text().toString());
//    		yend = (int)Integer.valueOf(quyu.get(i).getElementsByTag("y2").text().toString());
    		ystart = (int)Double.parseDouble(quyu.get(i).getElementsByTag("y1").text().toString());
    		yend = (int)Double.parseDouble(quyu.get(i).getElementsByTag("y2").text().toString());
    		tag.add(ystart);
    		tag.add(yend);
    		if(i>1)
    		{
    			tag.add(i-2);
    		}
    		else
    		{
    			tag.add(0);
    		}
    		return tag;
    	}
    }


}
