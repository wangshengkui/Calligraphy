package com.jinke.calligraphy.app.branch;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PghComments {
	public static String[][] getPghString(String fileName){
		String comments[][] = new String[15][5];
		String path = "/sdcard/pghxml/";
		File file  = new File(path+fileName);
		if(!file.exists())return null;
		Document doc = null;
		try 
    	{
    		doc = Jsoup.parse(file, "UTF-8");
    	 }
    	catch (IOException e) 
    	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
		Element item = doc.getElementsByTag("item").get(0);
//   		comments[0][0] = item.getElementsByTag("k"+15).get(0).getElementsByTag("ksub"+3).get(0).text().toString();
   		for(int i=1;i<=15;i++)
   			for(int j=1;j<=5;j++) {
   				comments[i-1][j-1]=item.getElementsByTag("k"+i).get(0).getElementsByTag("ksub"+j).get(0).text().toString();
   			}
//		doc.remove();
		return comments;
	}
	
	
	
	
}
