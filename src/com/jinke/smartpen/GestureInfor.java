package com.jinke.smartpen;

import java.util.ArrayList;

import android.graphics.RectF;

/**
 * @author nkxm 手势识别后返回的手势信息 建议有额外属性的手势信息继承该类
 */
public class GestureInfor {
	private int gestureIndex = -1;// 该变量只能赋值一次，后面处理gesture时可以用switch-case结构
	private String gestureName = null;// 该变量只能赋值一次
	private String gestureAreaName = null;
	private  ArrayList<Integer> tag = null;
	private Point centerPoint = null;
	private RectF gestureBoundingBox = null;
	/**
	 * 根据传入的索引值来确定手势的名称,传入量只有一个gestureIndex是为了便于控制gestureIndex和手势名称的映射关系
	 * 
	 * @param gestureIndex
	 */
	public GestureInfor(int gestureIndex) {
		this.gestureIndex = gestureIndex;
		switch (this.gestureIndex) {
		case -1:
			gestureName="书写";
			break;
		case 0:// 这个几乎用不到
			gestureName = "指令控制符";
			break;
		case 1:
			gestureName = "录音";
			break;
		case 2:
			gestureName = "分享";
			break;
		case 3:
			gestureName = "减号";
			break;
		case 4:
			gestureName = "单击";
			break;
		case 5:
			gestureName = "双击";
			break;
		case 6:
			gestureName = "对号";
			break;
		case 7:
			gestureName = "错号";
			break;
		case 8:
			gestureName = "圈题";
			break;
		case 9:
			gestureName = "选A";
			break;
		case 10:
			gestureName = "选B";
			break;
		case 11:
			gestureName = "选C";
			break;
		case 12:
			gestureName = "选D";
			break;
		case 13:
			gestureName = "选E";
			break;
		case 14:
			gestureName = "选F";
			break;
		case 15:
			gestureName = "减1";
			break;
		case 16:
			gestureName = "减2";
			break;
		case 17:
			gestureName = "减3";
			break;
		case 18:
			gestureName = "减4";
			break;
		case 19:
			gestureName = "减5";
			break;
		case 20:
			gestureName = "减6";
			break;
		case 21:
			gestureName = "减7";
			break;
		case 22:
			gestureName = "减8";
			break;
		case 23:
			gestureName = "减9";
			break;
		case 24:
			gestureName = "减10";
			break;
		case 25:
			gestureName = "减11";
			break;
		case 26:
			gestureName = "减12";
			break;
		case 27:
			gestureName = "减13";
			break;
		case 28:
			gestureName = "减14";
			break;
		case 29:
			gestureName = "减15";
			break;
		case 30:
			gestureName = "减16";
			break;
		case 31:
			gestureName = "减17";
			break;
		case 32:
			gestureName = "减18";
			break;
		case 33:
			gestureName = "减19";
			break;
		case 34:
			gestureName = "减20";
			break;
		case 35:
			gestureName = "半对";
			break;
		case 36:
			gestureName = "半对1";
			break;
		case 37:
			gestureName = "半对2";
			break;
		default:
			gestureName = null;
			break;
		}

	}

	/**
	 * 
	 * @param gestureAreaName
	 * @return true:表示信息修改成功，false:信息已经有值，禁止修改
	 */
	public boolean setGestureAreaName(String gestureAreaName) {
		if (this.gestureAreaName == null) {
			this.gestureAreaName = gestureAreaName;
			return true;
		} else
			return false;
	}

	/**
	 * 
	 * @param centerX
	 * @param centerY
	 * @return true:表示信息修改成功，false:信息已经有值，禁止修改
	 */
	public boolean setGestureCenter(float centerX, float centerY) {
		if (centerPoint == null) {
			centerPoint = new Point(centerX, centerY);
			return true;
		} else {
			return false;
		}

	}

	public int getGestureIndex() {
		return this.gestureIndex;
	}

	public String getGestureName() {
		return this.gestureName;
	}

	public String getGestureAreaName() {
		return this.gestureAreaName;
	}
 
    public  Point  getCenter() {
		return centerPoint;
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
   
	public boolean setTag(ArrayList<Integer> tag) {
		if (tag==null||this.tag!=null) {
			return false;
		}else {
			this.tag=tag;
			return true;
		}
	}
	public ArrayList<Integer> getTag(){
		return this.tag;
		
		
	}

}
