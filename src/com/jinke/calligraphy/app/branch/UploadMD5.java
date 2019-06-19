package com.jinke.calligraphy.app.branch;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class UploadMD5 {
	private  final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f' };
	public ProgressDialog mDialog;
	public String dialogmessage="云端存储文件中，任何操作均无效";
	private Context mContext;
	String uploadUrl="http://118.24.109.3/Public/smartpen/uploadmd5ver.php";
	String srcPath;
	public volatile  String mresult="1";
	public FileInputStream fis;
//	public volatile  String md5="0";
//	public boolean isUpLoadSucess=false;
	public  HttpURLConnection con;
//	public java.util.Timer  timer = new  java.util.Timer();
	public  UploadMD5( String filePath) {
//		md5=md5sum(filePath);
	}
	public  boolean uploadFile(String uploadUrl, String srcPath, String md5Local)
	  {
//		  判断上传的图片是否存在
		  boolean uploadStaus=true;
		  File file = new File(srcPath);
		  if (file.exists()) {
			Log.v("clickButton", srcPath);
		}
		  else {
			Log.v("clickButton", "文件不存在");
			uploadStaus=false;//文件不存在默认
			return uploadStaus;
		}
		  
	    String end = "\r\n";
	    String twoHyphens = "--";
	    String boundary = "******";
	    try
	    {
	    	
	      URL url = new URL(uploadUrl);
	      Log.v("clickButton", "123："+url);
//	      HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
	       con=(HttpURLConnection)url.openConnection();    
	      
	     
	           
	      // 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
	      // 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
//	      con.setChunkedStreamingMode(128 * 1024);// 128K
	      // 允许输入输出流
	      con.setDoInput(true);
	      con.setDoOutput(true);
	      con.setUseCaches(false);
	      // 使用POST方法
	      con.setRequestMethod("POST");
	      con.setRequestProperty("Connection", "Keep-Alive");
	      con.setConnectTimeout(10000);
	      con.setReadTimeout(10000);
	      con.setRequestProperty("Charset", "UTF-8");
	      con.setRequestProperty("Content-Type",
	          "multipart/form-data;boundary=" + boundary);
//	      con.connect();

	      DataOutputStream dos = new DataOutputStream(
	    		  con.getOutputStream());

	      dos.writeBytes(twoHyphens + boundary + end);//发送分界符
	      dos.writeBytes("Content-Disposition: form-data; name=\"md5\""+end+end+md5Local+end);
	      dos.writeBytes(twoHyphens + boundary + end);//发送分界符
	      dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
	          + srcPath.substring(srcPath.lastIndexOf("/") + 1)
	          + "\""
	          + end);
	      dos.writeBytes(end);//发送结束符
	      
	      Log.v("clickButton", "执行过链接代码啦");
	      
	   //   Log.v("clickButton", ""+dos.toString());

	       fis = new FileInputStream(file);//读取文件到内存的流
	     // Log.v("clickButton", "下面读取文件："+fis.toString());
	      byte[] buffer = new byte[1024]; // 8k
	      int count = 0;
	      // 读取文件
	      while ((count = fis.read(buffer)) != -1)
	      {
	   
/*		      Log.v("clickButton", "++++"+buffer.length);
		      Log.v("clickButton", buffer.toString());*/
//		      Thread.sleep(100);
		      dos.write(buffer, 0, count);
	  
	      //  Log.v("clickButton", "++++"+dos.size());
	      }
	      
	      Log.v("clickButton","++++"+dos.toString());
	      fis.close();
	      Log.v("clickButton","哈哈1");
	      
	      dos.writeBytes(end);
	      Log.v("clickButton","哈哈2");
	      dos.writeBytes(twoHyphens + boundary + twoHyphens + end);//发送完成，下面的流是接受反馈	
	      dos.flush();
	      Log.v("clickButton", "状态码："+con.getResponseCode());
	      InputStream is = con.getInputStream();//获得接收流
	      InputStreamReader isr = new InputStreamReader(is, "utf-8");
	      BufferedReader br = new BufferedReader(isr);
	      mresult = br.readLine();
	      Log.i("clickButton","result:"+mresult); 
	      
	      //增加上传成功的判断逻辑
	     
	      if(mresult.equals(md5Local))uploadStaus=true;
	      else uploadStaus=false;
	      con.disconnect();
	      
//       Toast.makeText(this, "成功了", Toast.LENGTH_SHORT).show();
//	      Toast.makeText(, result, Toast.LENGTH_LONG).show();
	      
	      
	      
	      
	      
//     定义BufferedReader输入流来读取URL的响应
//    BufferedReader reader = new BufferedReader(new InputStreamReader(
//            con.getInputStream()));
//    String line = null;
//    while ((line = reader.readLine()) != null) {
//        System.out.println(line);
//        Log.i("clickButton",line);            }
	      dos.close();
	      is.close();
	      Log.v("clickButton","哈哈");
	      
	    } catch (Exception e)
	    {
	    	uploadStaus=false;
	        Log.e("你好", "出错了");
   	    return uploadStaus;
	 //     setTitle(e.getMessage());
	    }
	    return uploadStaus;
	  }	
	 
	 public  String toHexString(byte[] b) {
		    StringBuilder sb = new StringBuilder(b.length * 2);
		    for (int i = 0; i < b.length; i++) {
		        sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
		        sb.append(HEX_DIGITS[b[i] & 0x0f]);
		    }
		    return sb.toString();
		}

		public String md5sum(String filename) {
		    InputStream fis;
		    byte[] buffer = new byte[1024];
		    int numRead = 0;
		    MessageDigest md5;
		    try{
		        fis = new FileInputStream(filename);
		        md5 = MessageDigest.getInstance("MD5");
		        while((numRead=fis.read(buffer)) > 0) {
		            md5.update(buffer,0,numRead);
		        }
		        fis.close();
		        return toHexString(md5.digest());   
		    } catch (Exception e) {
		        System.out.println("error");
		        return null;
		    }
		}	
	 
	 
	 
}
