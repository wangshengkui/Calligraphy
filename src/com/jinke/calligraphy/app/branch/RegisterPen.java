package com.jinke.calligraphy.app.branch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingFormatArgumentException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.R;
import android.R.string;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.widget.TextView;
import android.app.Activity;


public class RegisterPen {
	
	Document doc2;
    String pagexml2;
    String currentPen = null;
    int currentOID = 0;
    private TextView showInftTextView;
    
    @SuppressLint("SdCardPath")
	@SuppressWarnings({ "unlikely-arg-type", "unused", "null" })
    
	public ArrayList<Integer> Register(int CodeNumber ,String penAddress) throws IOException
    {
    	currentPen = penAddress;
    	currentOID = CodeNumber;
    	
    	ArrayList<Integer> tag = new ArrayList<Integer>();
    	
    	pagexml2 = "一班注册表.xml";
    	File file = new File("/sdcard/" +pagexml2);
    	
    	try 
    	{
    		doc2 = Jsoup.parse(file, "UTF-8");
    	
    	 }
    	catch (IOException e) 
    	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	Elements item = doc2.getElementsByTag("item");
    	Elements personalID = doc2.getElementsByTag("personalID");
    	Elements type = doc2.getElementsByTag("type");
    	Elements name = doc2.getElementsByTag("name");
    	Elements dianduma = doc2.getElementsByTag("dianduma");
    	Elements penID = doc2.getElementsByTag("penID");
    	
    	for(int j = 0;j<penID.size();j++)
		{
			if(penID.get(j).text().toString().equals(penAddress))
			{
				tag.add(0);
			}
		}
    	
    	for(int i = 0;i<dianduma.size();i++)
    	{
    		
    		float b = (Float.valueOf(dianduma.get(i).text()));
    		
    		if(CodeNumber == (int)b)
    		{
    			if(penID.get(i).text().toString().isEmpty() == true)
    			{
    				penID.get(i).text(penAddress);
    				tag.add(2);
    				tag.add(i);
    				//main.runOnUIThread("注册成功"+"\n"+"姓名："+name.get(i).text().toString()+"\n"+"ID:"+personalID.get(i).text().toString()+"\n"+"penID:"+penAddress);
    			}
    			
    			else 
    			{
    				tag.add(1);
    			}
    		}
    	}
    	
    	FileOutputStream fos;
		try {
			fos = new FileOutputStream(file, false);
			fos.write(doc2.html().getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	pagexml2 = "二班注册表.xml";
    	File file2 = new File("/sdcard/" +pagexml2);
    	
    	try 
    	{
    		doc2 = Jsoup.parse(file2, "UTF-8");
    	
    	 }
    	catch (IOException e) 
    	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	Elements item2 = doc2.getElementsByTag("item");
    	Elements personalID2 = doc2.getElementsByTag("personalID");
    	Elements type2 = doc2.getElementsByTag("type");
    	Elements name2 = doc2.getElementsByTag("name");
    	Elements dianduma2 = doc2.getElementsByTag("dianduma");
    	Elements penID2 = doc2.getElementsByTag("penID");
    	
    	for(int j = 0;j<penID2.size();j++)
		{
			if(penID2.get(j).text().toString().equals(penAddress))
			{
				tag.add(0);
			}
		}
    	
    	for(int i = 0;i<dianduma2.size();i++)
    	{
    		
    		float b = (Float.valueOf(dianduma2.get(i).text()));
    		
    		if(CodeNumber == (int)b)
    		{
    			if(penID2.get(i).text().toString().isEmpty() == true)
    			{
    				penID2.get(i).text(penAddress);
    				tag.add(2);
    				tag.add(i);
    				//main.runOnUIThread("注册成功"+"\n"+"姓名："+name2.get(i).text().toString()+"\n"+"ID:"+personalID2.get(i).text().toString()+"\n"+"penID:"+penAddress);
    			}
    			
    			else 
    			{
    				tag.add(1);
    			}
    		}
    	}
    	
    	FileOutputStream fos2;
		try {
			fos2 = new FileOutputStream(file2, false);
			fos2.write(doc2.html().getBytes());
			fos2.flush();
			fos2.close();
		} catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(tag.size()<1)
		{
			return null;
		}
		else return tag;
		
    }
    
    //重新注册--笔
    public void RefushPen() throws IOException
    {
    	pagexml2 = "一班注册表.xml";
    	File file = new File("/sdcard/" +pagexml2);
    	
    	try 
    	{
    		doc2 = Jsoup.parse(file, "UTF-8");
    	
    	 }
    	catch (IOException e) 
    	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}

    	Elements penID = doc2.getElementsByTag("penID");
    	
    	for(int j = 0;j<penID.size();j++)
		{
			if(penID.get(j).text().toString().equals(currentPen))
			{
				penID.get(j).text("");
			}
		}
    	
    	FileOutputStream fos;
		try {
			fos = new FileOutputStream(file, false);
			fos.write(doc2.html().getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pagexml2 = "二班注册表.xml";
    	File file2 = new File("/sdcard/" +pagexml2);
    	
    	try 
    	{
    		doc2 = Jsoup.parse(file2, "UTF-8");
    	
    	 }
    	catch (IOException e) 
    	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}

    	Elements penID2 = doc2.getElementsByTag("penID");
    	
    	for(int j = 0;j<penID2.size();j++)
		{
			if(penID2.get(j).text().toString().equals(currentPen))
			{
				penID2.get(j).text("");
			}
		}
    	
    	FileOutputStream fos2;
		try {
			fos2 = new FileOutputStream(file2, false);
			fos2.write(doc2.html().getBytes());
			fos2.flush();
			fos2.close();
		} catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //重新注册--人
    public void RefushPersonal() throws IOException
    {
    	pagexml2 = "一班注册表.xml";
    	File file = new File("/sdcard/" +pagexml2);
    	
    	try 
    	{
    		doc2 = Jsoup.parse(file, "UTF-8");
    	
    	 }
    	catch (IOException e) 
    	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}

    	Elements penID = doc2.getElementsByTag("penID");
    	Elements dianduma = doc2.getElementsByTag("dianduma");
    	
    	for(int i = 0;i<dianduma.size();i++)
    	{
    		
    		float b = (Float.valueOf(dianduma.get(i).text()));
    		
    		if(currentOID == (int)b)
    		{
    			penID.get(i).text("");
    		}
    	}
    	
    	FileOutputStream fos;
		try {
			fos = new FileOutputStream(file, false);
			fos.write(doc2.html().getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pagexml2 = "二班注册表.xml";
    	File file2 = new File("/sdcard/" +pagexml2);
    	
    	try 
    	{
    		doc2 = Jsoup.parse(file2, "UTF-8");
    	
    	 }
    	catch (IOException e) 
    	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}

    	Elements penID2 = doc2.getElementsByTag("penID");
    	Elements dianduma2 = doc2.getElementsByTag("dianduma");
    	
    	for(int i = 0;i<dianduma2.size();i++)
    	{
    		
    		float b = (Float.valueOf(dianduma2.get(i).text()));
    		
    		if(currentOID == (int)b)
    		{
    			penID2.get(i).text("");
    		}
    	}
    	
    	FileOutputStream fos2;
		try {
			fos2 = new FileOutputStream(file2, false);
			fos2.write(doc2.html().getBytes());
			fos2.flush();
			fos2.close();
		} catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
  //屏幕输出显示线程
//    public void runOnUIThread(final String str) 
//    {
//		runOnUiThread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				showInftTextView.setText(str);
//			}
//
//		});
//
//	}
    
}
