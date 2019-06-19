package com.jinke.calligraphy.app.branch;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



public class DatabaseOp {
	public static int proNo = 500;
	public static String path = "/sdcard/homework.db";
	
	public static SQLiteDatabase createDatabase() {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path, null);
		return db;
	}
	
	public static void createTable(SQLiteDatabase db){
		String check_table = "create table if not exists usertable(_id integer primary key autoincrement, " +
				"pageNo INTEGER, quesNo INTEGER, a INTEGER, b INTEGER, c INTEGER, d INTEGER, e INTEGER)";
		db.execSQL(check_table);

//		
//		ContentValues[] cValue = new ContentValues[proNo];
//		for(int i=0; i<proNo; i++) {
//			cValue[i].put("quesNo", i+1);
//			cValue[i].put("a", 0);
//			cValue[i].put("b", 0);
//			cValue[i].put("c", 0);
//			cValue[i].put("d", 0);
//			cValue[i].put("e", 0);
//			db.insert("check_table", null, cValue[i]);
//		}		
	}
	
	public static void initDb(SQLiteDatabase db) {
		Log.i("sqldb", "init");
		ContentValues cValue[] = new ContentValues[proNo];
		for(int i=0; i<proNo; i++) {
			cValue[i]= new ContentValues();
			cValue[i].put("pageNo", 0);//0425
			cValue[i].put("quesNo", 0);
			cValue[i].put("a", 0);
			cValue[i].put("b", 0);
			cValue[i].put("c", 0);
			cValue[i].put("d", 0);
			cValue[i].put("e", 0);
			db.insert("usertable", null, cValue[i]);
		}
	}
	
	public static void update(SQLiteDatabase db, int pNo,int qNo, int a, int b, int c, int d, int e) {
		Log.i("sqldb", "update in DatabaseOp" + qNo + " " + a + " " + b + c+d+e);
		ContentValues cValue = new ContentValues();
		cValue.put("pageNo", pNo );//0425
		cValue.put("quesNo", qNo);
		Log.i("sqldb", "qNo in update " + qNo);
		cValue.put("a", a);
		cValue.put("b", b);
		cValue.put("c", c);
		cValue.put("d", d);
		cValue.put("e", e);
		
		Cursor cursor = db.query("usertable", null, "PageNo=? and QuesNo=?", new String[] {String.valueOf(pNo),String.valueOf(qNo)}, null, null, null);
		if(cursor!=null) {
		String whereClause = "pageNo=? and quesNo=?";
		String[] whereArgs = new String[] {String.valueOf(pNo),String.valueOf(qNo)};
		db.update("usertable", cValue, whereClause, whereArgs);
		}
		else {
			db.insert("usertable", null, cValue);
		}
	}
	
	public static void clcDatabase(SQLiteDatabase db){
		ContentValues[] cValue = new ContentValues[proNo];
		for(int i=0; i<proNo; i++) {
			cValue[i] = new ContentValues();
			Log.i("sqldb", " clcDatabase "  + i + "proNo" + proNo);
			cValue[i].put("pNo", 0);//0425
			cValue[i].put("quesNo", 0);
			Log.i("sqldb", " quesNo");
			cValue[i].put("a", 0);
			cValue[i].put("b", 0);
			cValue[i].put("c", 0);
			cValue[i].put("d", 0);
			cValue[i].put("e", 0);
			String whereClause = "_id=?";
			String[] whereArgs = {String.valueOf(i+1)};
			db.update("usertable", cValue[i], whereClause, whereArgs);
		}		
	}
	
	public static int[] readDatabase(SQLiteDatabase db, int pNo,int qNo) {
		int result[] = {0, 0, 0,0,0};
//		Cursor cursor = db.query("usertable", null, null, null, null, null, null);
		Cursor cursor = db.query("usertable", null, "PageNo=? and QuesNo=?", new String[] {String.valueOf(pNo),String.valueOf(qNo)}, null, null, null);

		if(cursor != null ){
			cursor.moveToFirst();
			result[0] = cursor.getInt(cursor.getColumnIndex("a"));
			result[1] = cursor.getInt(cursor.getColumnIndex("b"));
			result[2] = cursor.getInt(cursor.getColumnIndex("c"));
			result[3] = cursor.getInt(cursor.getColumnIndex("d"));
			result[4] = cursor.getInt(cursor.getColumnIndex("e"));
			}


		return result;
	}
}
