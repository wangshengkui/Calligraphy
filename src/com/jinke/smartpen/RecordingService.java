package com.jinke.smartpen;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

import com.jinke.calligraphy.app.branch.R;
import com.jinke.calligraphy.app.branch.Start;

/**
 * 录音的 Service
 *
 * Created by developerHaoz on 2017/8/12.
 */

public class RecordingService extends Service {

    private static final String LOG_TAG = "RecordingService";

    private String mFileName = null;
    private String mFilePath = null;

    private MediaRecorder mRecorder = null;
    private MyBinder mBinder = null;
    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;
    private TimerTask mIncrementTimerTask = null;

 public class MyBinder extends Binder{
     public RecordingService getService() {
         return RecordingService.this;
     }
	 
 }   
    
    
    @Override
    public IBinder onBind(Intent intent) { 
    	mBinder=new MyBinder();
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mRecorder != null) {
            stopRecording();
        }
        super.onDestroy();
    }

    public void startRecording() {
        setFileNameAndPath();
if (mRecorder!=null) {
	mRecorder.release();	
}
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFilePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioChannels(1);
        mRecorder.setAudioSamplingRate(44100);
        mRecorder.setAudioEncodingBitRate(192000);

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartingTimeMillis = System.currentTimeMillis();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
            mRecorder.release();
        }
    }

    public void setFileNameAndPath(){
        int count = 0;
        File f = null;
        
     
            count++;
//            mFileName = getString(R.string.default_file_name)
//                    + "_" + (System.currentTimeMillis()) + ".3gp";
            //固定录音文件的名字
//            mFileName = "recordAudio-1"+ ".3gp";
//            mFileName = "recordAudio-1"+ ".3gp";
            Log.e("zgm", "currentPageName:"+Start.currentPageName);
            Log.i("tag0", ":"+Start.tag.get(0));
            mFileName ="001-"+Start.currentPageName.substring(5,Start.currentPageName.lastIndexOf("-")+1)+Start.tag.get(0)+".mp3";
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFilePath += "/-1/" + mFileName;   
            f = new File(mFilePath);
            if(f.exists())f.delete();
            
     
           
       
    }

    public void stopRecording() {
        mRecorder.stop();
        mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);
        mRecorder.release();
        Log.e("zgm","1223: stopRecording-mElapsedMillis="+mElapsedMillis );
/*
        getSharedPreferences("sp_name_audio", MODE_PRIVATE)
                .edit()
                .putString("audio_path", mFilePath)
                .putLong("elpased", mElapsedMillis)
                .apply();*/
        if (mIncrementTimerTask != null) {
            mIncrementTimerTask.cancel();
            mIncrementTimerTask = null;
        }

        mRecorder = null;
    }
public String getMFilePath(){
	return mFilePath;
}
public String getMFileName(){
	return mFileName;
}
public long getMFileElpased(){
	return mElapsedMillis;
	
}
}

