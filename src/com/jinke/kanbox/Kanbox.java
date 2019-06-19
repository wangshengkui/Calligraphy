package com.jinke.kanbox;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Kanbox {
	private static Kanbox mKanbox;
	
	private Kanbox() {
	}
	
	public static Kanbox getInstance() {
		if(mKanbox == null) {
			mKanbox = new Kanbox();
		}
		return mKanbox;
	}
	
	/**
	 * 获取帐号信息
	 * @param token
	 * @param listener
	 */
	public void getAccountInfo(Token token, RequestListener listener) {
		String getAccountInfoUrl = "https://api.kanbox.com/0/info";
		HttpRequestBase httpMethod = KanboxHttp.doGet(getAccountInfoUrl, token);
		new KanboxAsyncTask(null, httpMethod, listener, RequestListener.OP_GET_ACCCOUNT_INFO).execute();
	}
	
	/**
	 * 获取文件列表
	 * @param token
	 * @param path:要请求列表的路径
	 * @param listener
	 */
	public void getFileList(Token token, String path, RequestListener listener) {
		String getFileListUrl = "https://api.kanbox.com/0/list";
		HttpRequestBase httpMethod = KanboxHttp.doGet(getFileListUrl + path, token);
		new KanboxAsyncTask(null, httpMethod, listener, RequestListener.OP_GET_FILELIST).execute();
	}
	
	/**
	 * 移动文件
	 * @param token
	 * @param sourcePath：源路径
	 * @param desPath：目标路径
	 * @param listener
	 */
	public void moveFile(Token token, String sourcePath, String desPath, RequestListener listener) {
		String moveUrl = "https://api.kanbox.com/0/move";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("path", sourcePath);
		params.put("destination_path", desPath);
		
		HttpRequestBase httpMethod = KanboxHttp.doGet(moveUrl, params, token);
		new KanboxAsyncTask(null, httpMethod, listener, RequestListener.OP_MOVE).execute();
	}
	
	/**
	 * 复制文件
	 * @param token
	 * @param sourcePath：源路径
	 * @param desPath：目标路径
	 * @param listener
	 */
	public void copyFile(Token token, String sourcePath, String desPath, RequestListener listener) {
		String moveUrl = "https://api.kanbox.com/0/copy";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("path", sourcePath);
		params.put("destination_path", desPath);
		
		HttpRequestBase httpMethod = KanboxHttp.doGet(moveUrl, params, token);
		new KanboxAsyncTask(null, httpMethod, listener, RequestListener.OP_COPY).execute();
	}
	
	/**
	 * 删除文件
	 * @param token
	 * @param path：文件路径
	 * @param listener
	 */
	public void deleteFile(Token token, String path, RequestListener listener) {
		String moveUrl = "https://api.kanbox.com/0/delete";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("path", path);
		
		HttpRequestBase httpMethod = KanboxHttp.doGet(moveUrl, params, token);
		new KanboxAsyncTask(null, httpMethod, listener, RequestListener.OP_DELETE).execute();
	}
	
	/**
	 * 创建文件夹
	 * @param token
	 * @param path：文件路径
	 * @param listener
	 */
	public void makeDir(Token token, String path, RequestListener listener) {
		String moveUrl = "https://api.kanbox.com/0/create_folder";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("path", path);
	
		HttpRequestBase httpMethod = KanboxHttp.doGet(moveUrl, params, token);
		new KanboxAsyncTask(null, httpMethod, listener, RequestListener.OP_MAKE_DIR).execute();
		
	}
	
	/**
	 * 创建共享文件夹
	 * @param emails：邀请 邮件地址列表
	 * @param token
	 * @param path：共享文件夹路径
	 * @param listener
	 * @throws UnsupportedEncodingException 
	 */
	public void makeShareDir(ArrayList<String> emails, Token token, String path, RequestListener listener) throws UnsupportedEncodingException {
		String makeShareDirUrl = "https://api.kanbox.com/0/share";
		JSONArray params = new JSONArray();
		for (String string : emails) {
			params.put(string);
		}

		HttpRequestBase httpMethod = KanboxHttp.doPost(makeShareDirUrl + path, params.toString(), token);
		new KanboxAsyncTask(null, httpMethod, listener, RequestListener.OP_MAKE_SHARE_DIR).execute();
	}
	
	/**
	 * 获取共享邀请列表
	 * @param token
	 * @param listener
	 */
	public void getShareInviteList(Token token, RequestListener listener) {
		String getShareInviteUrl = "https://api.kanbox.com/0/pendingshares";
		HttpRequestBase httpMethod = KanboxHttp.doGet(getShareInviteUrl, token);
		new KanboxAsyncTask(null, httpMethod, listener, RequestListener.OP_GET_SHARE_INVITE_LIST).execute();
	}
	
	/**
	 * 处理共享请求
	 * @param shareDir：共享目录路径
	 * @param inviter：邀请者 邮箱地址
	 * @param accept：是否接受共享:0:拒绝， 1：接受
	 * @param token
	 * @param listener
	 * @throws JSONException 
	 * @throws UnsupportedEncodingException 
	 */
	public void handleShareInvite(String shareDir, String inviter, boolean accept, Token token, RequestListener listener) throws JSONException, UnsupportedEncodingException {
		String handleShareInviteUrl = "https://api.kanbox.com/0/pendingshares";
		JSONObject params = new JSONObject();
		params.put("path", shareDir);
		params.put("user", inviter);
		params.put("accept", accept);
		String strParams = params.toString();
//		strParams.replaceAll("\\\\/", "/");
		Log.e("test", strParams);
		HttpRequestBase httpMethod = KanboxHttp.doPost(handleShareInviteUrl, strParams, token);
		new KanboxAsyncTask(null, httpMethod, listener, RequestListener.OP_HANDLE_SHARE_INVITE).execute();
	}
	
	/**
	 * 是否是自己创建的共享文件夹
	 * @param token
	 */
	public void checkSharedOwner(Token token, String path, RequestListener listener) {
		String url = "https://api.kanbox.com/0/checkowner";
		HttpRequestBase httpMethod = KanboxHttp.doGet(url + path, token);
		new KanboxAsyncTask(null, httpMethod, listener, RequestListener.OP_SHARED_BY_SELF).execute();
	}
	
	/**
	 * 下载文件
	 * @param path：文件路径
	 * @param destPath：要下载的本地路径
	 * @param token
	 * @param listener
	 */
	public void download(String path, String destPath, Token token, RequestListener listener) {
		String downloadUrl = "https://api.kanbox.com/0/download";
		
		Log.e("kanbox", "download url:" + downloadUrl + path);
		Log.e("kanbox", "before doGet");
		HttpRequestBase httpMethod = KanboxHttp.doGet(downloadUrl + path, token);
		Log.e("kanbox", "after doGet");
//		new KanboxAsyncTask(destPath, httpMethod, listener, RequestListener.OP_DOWNLOAD).execute();
		new KanboxAsyncTask(path, destPath, httpMethod, listener, RequestListener.OP_DOWNLOAD).execute();
	}
	/**
	 * 下载IndexBitmap
	 * @param path：文件路径
	 * @param destPath：要下载的本地路径
	 * @param token
	 * @param listener
	 */
	public void downloadIndexBitmap(String path, String destPath, Token token, RequestListener listener) {
		String downloadUrl = "https://api.kanbox.com/0/download";
		
		Log.e("kanbox", "download url:" + downloadUrl + path);
		Log.e("kanbox", "before doGet");
		HttpRequestBase httpMethod = KanboxHttp.doGet(downloadUrl + path, token);
		Log.e("kanbox", "after doGet");
//		new KanboxAsyncTask(destPath, httpMethod, listener, RequestListener.OP_DOWNLOAD).execute();
		new KanboxAsyncTask(path, destPath, httpMethod, listener, RequestListener.OP_DOWNLOAD_BITMAP).execute();
	}
	
	/**
	 * 上传文件
	 * @param localPath：要上传文件的本地路径
	 * @param destPath：服务器路径
	 * @param token
	 * @param listener
	 * @throws IOException
	 */
	public void upload(String localPath, String destPath, Token token, RequestListener listener) throws IOException {
		String uploadUrl = "https://api-upload.kanbox.com/0/upload";
		
		HttpPost httpMethod = KanboxHttp.doPost(uploadUrl + destPath, null, token);
		InputStream is = new FileInputStream(localPath);
		httpMethod.setEntity(new InputStreamEntity(is, is.available()));
//		new KanboxAsyncTask(destPath, httpMethod, listener, RequestListener.OP_UPLOAD).execute();
		new KanboxAsyncTask(localPath,destPath, httpMethod, listener, RequestListener.OP_UPLOAD).execute();
	}
	
	/**
	 * 上传 指定的本地文件夹(F7是：/extsd/calldir/)   下的所有文件
	 * @param localPath：要上传文件的本地路径
	 * @param destPath：服务器路径
	 * @param token
	 * @param listener
	 * @throws IOException
	 */
	public void uploadLocalDir(String localPath, String destPath, Token token, RequestListener listener) throws IOException {
		String uploadUrl = "https://api-upload.kanbox.com/0/upload";
		
		HttpPost httpMethod = KanboxHttp.doPost(uploadUrl + destPath, null, token);
		InputStream is = new FileInputStream(localPath);
		httpMethod.setEntity(new InputStreamEntity(is, is.available()));
		new KanboxAsyncTask(destPath, httpMethod, listener, RequestListener.OP_UPLOAD).execute(); 
	}
	
}
