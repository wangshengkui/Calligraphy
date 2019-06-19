package com.jinke.calligraphy.app.branch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class StudentInfoParser {

/*
 * 该类没用到
 *
	private static Document document;

	static{
		File file = new File("/sdcard/stu.xml");

		try {
			document = Jsoup.parse(file, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public static String stuName(){
    	return document.select("stuname").text();
    }
    public static List<String> value(){
    	List<String> list = new ArrayList<String>();
    	Elements listrens = document.select("list").select("value");
    	for(Element listren:listrens){
			String text=listren.text();
			list.add(text);
		}
    	return list;
    }
*/
}






