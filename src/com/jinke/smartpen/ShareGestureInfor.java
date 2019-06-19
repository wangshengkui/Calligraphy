package com.jinke.smartpen;

import android.graphics.RectF;

public class ShareGestureInfor extends GestureInfor {
	private RectF gestureBoundingBox = null;

	public ShareGestureInfor(int gestureIndex) {
		super(gestureIndex);
		// TODO Auto-generated constructor stub
	}

	public RectF getGestureBoundingBox() {
		return gestureBoundingBox;
	}

	public boolean setGestureBoundingBox(RectF boundingBox) {
		if (gestureBoundingBox == null) {
			gestureBoundingBox = boundingBox;
			return true;
		} else {
			return false;
		}

	}

}
