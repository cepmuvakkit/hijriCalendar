package com.cepmuvakkit.conversion.views;

import com.cepmuvakkit.conversion.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import android.widget.ImageView;

public class LunarPhaseIcons extends ImageView {
	private int parentWidth;
	Context paramContext;
	private int parentHeight;
	private int radius;
	private int offset = 2;
	private float strokeWidth;
	private String phase = "empty";
	private Paint paint = new Paint(1);
	private RectF rect;

	public LunarPhaseIcons(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		this.paramContext = paramContext;

		TypedArray arr = paramContext.obtainStyledAttributes(paramAttributeSet,
				R.styleable.LunarPhaseIcons);

		CharSequence phaseStr = arr
				.getString(R.styleable.LunarPhaseIcons_phase);
		// TypedArray localTypedArray =
		// paramContext.obtainStyledAttributes(paramAttributeSet, x.a);
		// String str = arr.getString(0);
		if (phaseStr != null)
			setPhase(phaseStr.toString());
		arr.recycle();
	}

	protected void onDraw(Canvas paramCanvas) {

		super.onDraw(paramCanvas);

		paint.setColor(paramContext.getResources().getColor(R.color.black));
		paint.setStyle(Paint.Style.FILL);
		paramCanvas.drawOval(rect, paint);// Siyah içi dolu bir daire çizer
		paint.setColor(paramContext.getResources().getColor(R.color.mavi_color));
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(strokeWidth);
		paramCanvas.drawOval(rect, paint);// Daire şeklinde bir çember çizer
		paint.setStyle(Paint.Style.FILL);
		if (this.phase.equals("newCrescent")) {
			paramCanvas.drawArc(rect, 270.0F, 180.0F, true, paint);// Sağ
																	// taraftaki
																	// daireyi
																	// doldurur

			Paint moonPaintOvalAccArclengtBorW;
			moonPaintOvalAccArclengtBorW = new Paint(Paint.ANTI_ALIAS_FLAG);
			moonPaintOvalAccArclengtBorW.setColor(paramContext.getResources()
					.getColor(R.color.black));

			moonPaintOvalAccArclengtBorW.setStyle(Paint.Style.FILL_AND_STROKE);
			int arcWidth = (int) (0.95*radius)+3;
			RectF moonOval=new RectF(parentWidth / 2 - arcWidth/2 , offset,
					parentWidth/2 + arcWidth/2, parentHeight - 2
							* offset);

			paramCanvas.drawArc(moonOval, 0, 360, false,
					moonPaintOvalAccArclengtBorW);
		}
		if (this.phase.equals("firstQuarter"))
			paramCanvas.drawArc(rect, 90.0F, 180.0F, true, paint);// Sol
																	// taraftaki
																	// daireyi
																	// doldurur

		if (this.phase.equals("fullMoon"))
			paramCanvas.drawArc(rect, 0.0F, 360.0F, true, paint);// Mavi içi
																	// dolu bir
																	// daire
																	// çizer

		if (this.phase.equals("lastQuarter")) {
			paramCanvas.drawArc(rect, 270.0F, 180.0F, true, paint);// Sağ
																	// taraftaki
																	// daireyi
																	// doldurur

		}

		
		/*
		 * int arcWidth = (int) ((- 0.25) * (4 * r));
		 * moonPaintO.setColor(arcWidth < 0 ? Color.BLACK : Color.WHITE);
		 * 
		 * canvas.drawArc(moonOval, 0, 360, false, moonPaintO);
		 * canvas.drawArc(moonRect, 0, 360, false, moonPaintD); switch
		 * (this.phase) { case "newMoon": ; break; case "newCrescent": ; break;
		 * case "firstQuarter": canvas.drawArc(rect, 90, 180, false, moonPaint);
		 * canvas.drawArc(rect, 270, 180, false, moonPaintB);; break; case
		 * "fullMoon": ; break; case "lastQuarter": ; break; default:; break; }
		 */

	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		parentWidth = View.MeasureSpec.getSize(widthMeasureSpec);
		parentHeight = View.MeasureSpec.getSize(heightMeasureSpec);
		radius = (Math.min(parentWidth, parentHeight) / 2 - 2);
		strokeWidth = (10 * radius / 100);
		rect = new RectF(offset, offset, parentWidth - 2 * offset, parentHeight
				- 2 * offset);
	}

	public void setPhase(String paramString) {
		this.phase = paramString;
		invalidate();
	}
}
