package com.jinke.smartpen;

import android.graphics.RectF;

public class GesturePlaceAndResource{
	String gestureNameString="";
	String resourcePathString="";
	long    resourceElpased=0;
    final	RectF rectF;
	
	GesturePlaceAndResource(String gestureNameString,String resourcePathString,long resourceElpased,RectF rectF){
		this.gestureNameString=gestureNameString;
		this.resourcePathString=resourcePathString;
		this.resourceElpased=resourceElpased;//播放时长
		this.rectF=rectF;
	}
public String getGestureNameString(){
	return gestureNameString;
}
public String getResourcePath(){
	return resourcePathString;
}
public RectF getGesturePlace(){
	return this.rectF;
}

public long getresourceElpased(){
	return resourceElpased;
}
}
