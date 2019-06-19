package com.jinke.smartpen;

import android.gesture.Gesture;
import android.graphics.RectF;

public class SmartPenGesture extends Gesture{
public  SmartPenGesture(){
	super();
}	
public void SmartPenGestureClearAllStroke( ) {
            getStrokes().clear();
}
public void SmartPenGestureClearmBoundingBox( ) {
    getBoundingBox().setEmpty();
}
public RectF getGestureBoundBoxRect() {
	// TODO Auto-generated method stub
	return getBoundingBox();
}
}
