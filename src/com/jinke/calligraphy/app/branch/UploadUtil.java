package com.jinke.calligraphy.app.branch;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

import org.apache.http.client.HttpClient;

import android.util.Log;


public class UploadUtil {
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10*1000;   //超时时间
    private static final String CHARSET = "utf-8"; //设置编码
    
    
    private static String srcPath ;  
    
    
    /**
     * android上传文件到服务器
     * @param file  需要上传的文件
     * @param RequestURL  请求的rul
     * @return  返回响应的内容
     */
    public static String uploadFile(File file,String RequestURL)
    {
        String result = null;
        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--" , LINE_END = "\r\n"; 
        String CONTENT_TYPE = "multipart/form-data";   //内容类型
       Log.e(TAG, "HAHA");
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");   
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY); 

//          int res = conn.getResponseCode();  
//          Log.e("nihao", "response code:"+res);         
            
            
            
            
            if(file!=null)
            {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的   比如:abc.png  
                 */

                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""+file.getName()+"\""+LINE_END); 
                sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while((len=is.read(bytes))!=-1)
                {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流  
                 */
             int res = conn.getResponseCode();  
              Log.e(TAG, "response code:"+res);
             if(res==200)
               {
                    Log.e(TAG, "request success");
                    InputStream input =  conn.getInputStream();
                    StringBuffer sb1= new StringBuffer();
                    int ss ;
                    while((ss=input.read())!=-1)
                    {
                        sb1.append((char)ss);
                    }
                    result = sb1.toString();
                    Log.e(TAG, "result : "+ result);                }
               else{
                   Log.e(TAG, "request error");               }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
   public static void uploadFile1(String fileName) {
    	
    	
    	  File file = new File(fileName);
          if(file!=null)
          Log.i("upload","existed!!!!!!!"+fileName);
          else Log.i("upload","not existed!!!!!!!"+fileName);
        try {

            // 换行符
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            final String fileFormName ="file";
            // 定义数据分隔线
            String BOUNDARY = "========7d4a6d158c9"; 
            // 服务器的域名
//            URL url = new URL("http://192.168.1.111/jxyv1/index.php/Home/Index/checkedHomeWorkUpload/filename/"+fileName.substring(8, fileName.length()-4));
            URL url = new URL("http://192.168.1.111/jxyv1/Public/index.php");
//       
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
//            conn.setConnectTimeout(5000);
//            conn.setReadTimeout(5 * 1000);
            // 设置请求头参数
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            Log.i("upload",""+conn);
//            Log.i("upload",""+conn.getResponseCode());
            Log.i("upload","1"+fileName);


            
            
            
            
            
            
            
            
            // 上传文件
          
            
     
//          定义BufferedReader输入流来读取URL的响应
//         BufferedReader reader = new BufferedReader(new InputStreamReader(
//                 conn.getInputStream()));
//         String line = null;
//         while ((line = reader.readLine()) != null) {
//             System.out.println(line);
//             Log.i("upload",line);            }
            
     
            StringBuilder sb = new StringBuilder();
    
//            sb.append(boundaryPrefix);
//            sb.append(BOUNDARY);
//            sb.append(newLine);
//            // 文件参数,photo参数名可以随意修改
//            sb.append("Content-Disposition: form-data;name=\"\";filename=\"" + fileName
//                    + "\"" + newLine);
//            sb.append("Content-Type:application/octet-stream");
//            // 参数头设置完以后需要两个换行，然后才是参数内容
//            sb.append(newLine);
//            sb.append(newLine);
//            Log.i("upload",sb.toString());
            
            
            
            

            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"");
            sb.append(fileFormName);
            sb.append("\"; filename=\"");
            sb.append(fileName);
            sb.append("\"");
            sb.append("\r\n");
            sb.append("Content-Type: ");
            sb.append("application/octet-stream");
            sb.append("\r\n");
            sb.append("\r\n");
            
//            Log.i("upload","sb=="+sb.length());

            Log.i("upload","smg"+conn.getOutputStream().toString());
            try {
				
			
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            Log.i("upload","outsb"+out.toString().length());
            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());
           
            // 数据输入流,用于读取文件数据
            DataInputStream in = new DataInputStream(new FileInputStream(
                    file));
            byte[] bufferOut = new byte[8192];
            int bytes = 0;
            // 每次读1KB数据,并且将文件数据写入到输出流中
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
//                Log.i("upload",""+in.toString().length());
            }
            // 最后添加换行
            
            out.write(newLine.getBytes());
            in.close();
            Log.i("upload", "step1");
            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)
                    .getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();
            Log.i("upload", "step3");
            
            
            
//          定义BufferedReader输入流来读取URL的响应
         BufferedReader reader = new BufferedReader(new InputStreamReader(
                 conn.getInputStream()));
         String line = null;
         while ((line = reader.readLine()) != null) {
             System.out.println(line);
             Log.i("upload",line);            }
            
            

            } catch (Exception e) {
				// TODO: handle exception
			}

        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
    
public static void uploadFile2(String uploadFile)  
    {  
      String end = "\r\n";  
      String twoHyphens = "--";  
      String boundary = "******";  
      
      
      srcPath = uploadFile;
//      String uploadUrl="http://192.168.1.111/jxyv1/index.php/Home/Index/checkedHomeWorkUpload/filename/"+uploadFile.substring(8, uploadFile.length()-4);
      String uploadUrl="http://192.168.1.111/jxyv1/Public/index.php";
       try  
      {  
        URL url = new URL(uploadUrl);  
        HttpURLConnection httpURLConnection = (HttpURLConnection) url  
            .openConnection();  
        // 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃  
        // 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。  
        httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K  
        // 允许输入输出流  
        httpURLConnection.setDoInput(true);  
        httpURLConnection.setDoOutput(true);  
        httpURLConnection.setUseCaches(false);  
        // 使用POST方法  
        httpURLConnection.setRequestMethod("POST");  
        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");  
        httpURLConnection.setRequestProperty("Charset", "UTF-8");  
        httpURLConnection.setRequestProperty("Content-Type",  
            "multipart/form-data;boundary=" + boundary);  
    
        DataOutputStream dos = new DataOutputStream(  
            httpURLConnection.getOutputStream());  
        dos.writeBytes(twoHyphens + boundary + end);  
        dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""  
            + srcPath.substring(srcPath.lastIndexOf("/") + 1)  
            + "\""  
            + end);  
        dos.writeBytes(end);  
    
        FileInputStream fis = new FileInputStream(srcPath);  
        byte[] buffer = new byte[8192]; // 8k  
        int count = 0;  
        // 读取文件  
        while ((count = fis.read(buffer)) != -1)  
        {  
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
    

        dos.close();  
        is.close();  
    
      } catch (Exception e)  
      {  
        e.printStackTrace();  
 
      }  
    }  
  
}
