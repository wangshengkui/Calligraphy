package com.jinke.calligraphy.app.branch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

public class BigImageView extends ImageView {

	Bitmap bitmap;
	Resources res = getResources();

	public BigImageView(Context context) {
		super(context);

		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Rect rec = canvas.getClipBounds();
		RectF rectF = new RectF(rec);
		Paint paint = new Paint();
		bitmap = BitmapFactory.decodeResource(res, R.drawable.blur);
		
//		bitmap = FastBlurUtil.doBlur(bitmap, 15, false);
		canvas.drawBitmap(bitmap, getMatrix(), paint);
		// canvas.drawColor(Color.GRAY);

		rec.bottom--;
		rec.right--;

		paint.setStrokeWidth(20);
		paint.setColor(0x5500868b);
		paint.setStyle(Paint.Style.STROKE);
			
		 canvas.drawRoundRect(rectF, 100,100, paint);
//		  paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  	
//		 canvas.drawRect(rec, paint);
//		 canvas.drawBitmap(bitmap, rec, rec, paint); 
		super.onDraw(canvas);
	}

}
