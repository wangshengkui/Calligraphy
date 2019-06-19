package com.jinke.smartpen;

import java.io.Serializable;

import android.R.integer;

public class SimplePoint implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final float x;
	public final float y;
	public final double timestamp;
	public final int force;
	public final int ownerID;
	public final int pageID;
	public final int sectionID;
	public final int BookID;
	public final int color;
	public final int angle;
	public final int counter;

	
public  SimplePoint(float x,float y,double timestamp,int force,int ownerID,int pageID,
		int sectionID,int BookID,int color,int angle,int counter){
	this.x=x;
	this.y=y;
	this.timestamp=timestamp;
	this.force=force;
	this.ownerID=ownerID;
	this.pageID=pageID;
	this.sectionID=sectionID;
	this.BookID=BookID;
	this.color=color;
	this.angle=angle;
	this.counter=counter;
}
}
