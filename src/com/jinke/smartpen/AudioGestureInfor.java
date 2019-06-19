package com.jinke.smartpen;

import android.graphics.RectF;

/**
 * 语音注入和提取的手势的信息，继承自GestureInfor
 * @author nkxm
 *
 */
public class AudioGestureInfor extends GestureInfor {
	private int size = -1;
	private int bookID = -1;
	private int pageID = -1;
	private int questionIndex = -1;

	public AudioGestureInfor(int gestureIndex) {
		super(gestureIndex);
		// TODO Auto-generated constructor stub
	}

	public boolean setSize(int size) {

		if (this.size == -1) {
			this.size = size;
			return true;
		} else {
			return false;
		}

	}

	public boolean setBookIDAndPageID(int bookID, int pageID) {
		if (this.bookID == -1 || this.pageID == -1) {
			if (this.bookID == -1) {
				this.bookID = bookID;

			}
			if (this.pageID == -1) {
				this.pageID = pageID;
				return true;
			}
			return true;
		} else
			return false;

	}

	public boolean setQuestionIndex(int questionIndex) {

		if (this.questionIndex == -1) {
			this.questionIndex = questionIndex;
			return true;
		} else {
			return false;
		}
	}

	public int getSize() {
		return size;
	}

	public int getBookID() {
		return bookID;
	}

	public int getpageID() {
		return pageID;
	}

	public int getquestionIndex() {
		return questionIndex;
	}

}
