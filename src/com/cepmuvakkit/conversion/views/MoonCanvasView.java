package com.cepmuvakkit.conversion.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.cepmuvakkit.conversion.R;
import com.cepmuvakkit.conversion.R.drawable;
import com.cepmuvakkit.conversion.libration.MoonPositionAngle;
import com.cepmuvakkit.times.posAlgo.SunMoonPosition;

public class MoonCanvasView extends View {
	private SunMoonPosition sunMoonPosition;
	private Drawable mCustomImage;
	private double moonPhase, latitude, longitude, jd, ΔT;
	private Paint paint, paintFullMoon;
	private Bitmap fullMoonImg, moonMask, moonCanvasBmp;
	private Canvas canvas;
	private float rotation, posAngleAxis;
	private boolean afterFullMoon;

	public float getPosAngleAxis() {
		return posAngleAxis;
	}

	private Context context;
	//private int w = 476, h = 516, diameter;
	private int w=100, h=100, diameter;
	public MoonCanvasView(Context context) {
		super(context);
		this.context = context;
		initMoonCanvasView(context);
	}

	public MoonCanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initMoonCanvasView(context);
	}

	public MoonCanvasView(Context context, AttributeSet ats, int defaultStyle) {
		super(context, ats, defaultStyle);
		this.context = context;
		initMoonCanvasView(context);
	}



	private void initMoonCanvasView(Context context) {

		sunMoonPosition = new SunMoonPosition(jd, latitude, longitude, 0, ΔT);
		MoonPositionAngle posAngle = new MoonPositionAngle(jd, latitude,
				longitude);
		posAngleAxis = (float) posAngle.getPositionAngleAxis();
		this.rotation = 360-posAngleAxis;
		moonPhase = sunMoonPosition.getMoonIllimunated();
		PorterDuffXfermode portermode = new PorterDuffXfermode(
				PorterDuff.Mode.MULTIPLY);
		paint = new Paint();
		paint.setXfermode(portermode);
		mCustomImage = context.getResources()
				.getDrawable(R.drawable.moonhrfull);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(getBitmapImage(), 0, 0, null);
	}

	public Bitmap getBitmapImage() {
		

		int minlength = Math.min(w, h);
		diameter = minlength - minlength / 10;
		moonCanvasBmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(moonCanvasBmp);
		fullMoonImg = makeFullMoon(mCustomImage, rotation);
		moonMask = makeMoonMask(moonPhase, rotation);
		canvas.drawBitmap(fullMoonImg, 0, 0, paintFullMoon);
		canvas.drawBitmap(moonMask, 0, 0, paint);
		return moonCanvasBmp;

	}

	Bitmap makeFullMoon(Drawable mCustomImage, float rotation) {
		Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		paintFullMoon = new Paint();
		// paintFullMoon.setColor(0x880000FF);
		Rect moonPng = new Rect(w / 2 - diameter / 2, h / 2 - diameter / 2, w
				/ 2 + diameter / 2, h / 2 + diameter / 2);

		mCustomImage.setBounds(moonPng);
		Canvas canvasFullMoon = new Canvas(bm);
		canvasFullMoon.rotate(rotation, w / 2, h / 2);
		mCustomImage.draw(canvasFullMoon);
		return bm;
	}

	// create a bitmap with a rect, used for the "src" image
	Bitmap makeMoonMask(double moonPhase, float rotation) {
		Paint moonPaintHalfWhiteCircle, moonPaintHalfBlackCircle, moonPaintOvalAccArclengtBorW;// ,
																								// moonPaintPerimeter;
		// Draws a half slice circle as white color.
		int c = (int) (30 - 30 * moonPhase) + 30;//
		int maskDaVinciColor = Color.argb(255, c, c, c);// Da Vinci Glow Color

		moonPaintHalfWhiteCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
		moonPaintHalfWhiteCircle.setColor(Color.WHITE);
		moonPaintHalfWhiteCircle.setStyle(Paint.Style.FILL_AND_STROKE);
		// Draws a half slice circle as gray color.
		moonPaintHalfBlackCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
		moonPaintHalfBlackCircle.setColor(maskDaVinciColor);
		moonPaintHalfBlackCircle.setStyle(Paint.Style.FILL_AND_STROKE);
		// Paints for the 2 Arcs at the middle
		// of the moon according to moon phase<0.5
		// moon Paint for Oval
		moonPaintOvalAccArclengtBorW = new Paint(Paint.ANTI_ALIAS_FLAG);
		moonPaintOvalAccArclengtBorW.setColor(maskDaVinciColor);
		moonPaintOvalAccArclengtBorW.setStyle(Paint.Style.FILL_AND_STROKE);
		// Draws a perimeter for the moon shape as contour
		/*
		 * moonPaintPerimeter = new Paint(Paint.ANTI_ALIAS_FLAG);
		 * moonPaintPerimeter.setColor(Color.GRAY);
		 * moonPaintPerimeter.setStyle(Paint.Style.STROKE);
		 */
		Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvasMoonMask = new Canvas(bm);
		canvasMoonMask.rotate(rotation, w / 2, h / 2);
		RectF moonRect = new RectF(w / 2 - diameter / 2, h / 2 - diameter / 2,
				w / 2 + diameter / 2, h / 2 + diameter / 2);
		canvasMoonMask.drawArc(moonRect, 90, 180, false,
				afterFullMoon?moonPaintHalfWhiteCircle:moonPaintHalfBlackCircle);
		canvasMoonMask.drawArc(moonRect, 270, 180, false,
				afterFullMoon?moonPaintHalfBlackCircle:moonPaintHalfWhiteCircle);
		int arcWidth = (int) ((moonPhase - 0.5) * (2 * diameter));
		moonPaintOvalAccArclengtBorW.setColor(arcWidth < 0 ? maskDaVinciColor
				: Color.WHITE);
		RectF moonOval = new RectF(w / 2 - Math.abs(arcWidth) / 2, h / 2
				- diameter / 2, w / 2 + Math.abs(arcWidth) / 2, h / 2
				+ diameter / 2);
		canvasMoonMask.drawArc(moonOval, 0, 360, false,
				moonPaintOvalAccArclengtBorW);

		// canvas.drawBitmap(moonCanvasBmp, 0,0,null);
		// canvas.drawArc(moonRect, 0, 360, false, moonPaintPerimeter);

		return bm;
	}

	public void setParameters(double latitude, double longitude, double jd,
			double ΔT, double moonPhase,boolean afterFullMoon,int screenWidth,int screenHeight) {
		this.moonPhase = moonPhase;
		this.latitude = latitude;
		this.longitude = longitude;
		this.afterFullMoon=afterFullMoon;
		this.jd = jd;
		this.ΔT = ΔT;
		this.w=screenWidth-20*12;
		this.h=screenHeight;
		h=w;
		initMoonCanvasView(context);

	}

}