package com.jinke.calligraphy.app.branch;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class FileUploadTask extends AsyncTask<String, Integer, Void>{

	private ProgressDialog dialog = null;
	HttpURLConnection connection = null;
	DataOutputStream outputStream = null;
	DataInputStream inputStream = null;
    public String fileName ;
	String urlServer = "http://192.168.1.115/jxyv1/index.php/Home/Index/checkedHomeWorkUpload/filename/"+fileName.substring(8, fileName.length()-4);
	//the file path to upload

	public FileUploadTask(String fileName){
		super();
		this.fileName=fileName;
		Log.i("upload",fileName);
		
	}
	
	
	
	//the server address to process uploaded file

	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary = "*****";

	File uploadFile = new File(fileName);
	long totalSize = uploadFile.length(); // Get size of file, bytes

	@Override
	protected void onPreExecute() {
//		dialog = new ProgressDialog(UploadtestActivity.this);
		dialog.setMessage("正在上传...");
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setProgress(0);
		dialog.show();
	}

	protected Void doInBackground(Object... arg0) {

		long length = 0;
		int progress;
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 256 * 1024;// 256KB

		try {
			FileInputStream fileInputStream = new FileInputStream(new File(
					fileName));

			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();

			// Set size of every block for post
			connection.setChunkedStreamingMode(256 * 1024);// 256KB

			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			outputStream = new DataOutputStream(
					connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream
					.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
							+ fileName + "\"" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				length += bufferSize;
				progress = (int) ((length * 100) / totalSize);
				publishProgress(progress);

				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);
			publishProgress(100);

			// Responses from the server (code and message)
			int serverResponseCode = connection.getResponseCode();
			String serverResponseMessage = connection.getResponseMessage();

			/* 将Response显示于Dialog */
			// Toast toast = Toast.makeText(UploadtestActivity.this, ""
			// + serverResponseMessage.toString().trim(),
			// Toast.LENGTH_LONG);
			// showDialog(serverResponseMessage.toString().trim());
			/* 取得Response内容 */
			// InputStream is = connection.getInputStream();
			// int ch;
			// StringBuffer sbf = new StringBuffer();
			// while ((ch = is.read()) != -1) {
			// sbf.append((char) ch);
			// }
			//
			// showDialog(sbf.toString().trim());

			fileInputStream.close();
			outputStream.flush();
			outputStream.close();

		} catch (Exception ex) {
			// Exception handling
			// showDialog("" + ex);
			// Toast toast = Toast.makeText(UploadtestActivity.this, "" +
			// ex,
			// Toast.LENGTH_LONG);

		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		dialog.setProgress(progress[0]);
	}

	@Override
	protected void onPostExecute(Void result) {
		try {
			dialog.dismiss();
			// TODO Auto-generated method stub
		} catch (Exception e) {
		}
	}

	@Override
	protected Void doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
	