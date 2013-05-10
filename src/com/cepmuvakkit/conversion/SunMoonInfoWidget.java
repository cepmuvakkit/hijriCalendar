package com.cepmuvakkit.conversion;

import java.text.DecimalFormat;
import java.util.Calendar;
import com.cepmuvakkit.conversion.R;
import com.cepmuvakkit.conversion.phaseEvents.MoonPhases;
import com.cepmuvakkit.conversion.settings.ApplicationConstants;
import com.cepmuvakkit.conversion.settings.LunarCalendarSettings;
import com.cepmuvakkit.conversion.views.MoonCanvasView;
import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.LunarPosition;
import com.cepmuvakkit.times.posAlgo.SolarPosition;
import com.cepmuvakkit.times.posAlgo.SunMoonPosition;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class SunMoonInfoWidget extends AppWidgetProvider {
	private MoonCanvasView moonCanvasView;
	private int mYear, mMonth, mDay, mHour, mMinute, mSecond, temperature,
	pressure, altitude;
	private double mLatitude, mLongitude, mTimeZone;
	private double jd, ΔT; // Julian Day
	private SunMoonPosition sunMoonPosition;
	private DecimalFormat oneDigit;
	private SharedPreferences preferences;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {	
		preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		LunarCalendarSettings.load(preferences);

		oneDigit = new DecimalFormat("#0.0");
		RemoteViews updateViews = new RemoteViews(context.getPackageName(),
				R.layout.solar_lunar_widget);
		returnCurrentJulianDay();
		ΔT = AstroLib.calculateTimeDifference(jd);
		LunarPosition lunar = new LunarPosition();
		SolarPosition solar = new SolarPosition();
		mLatitude=LunarCalendarSettings.getInstance().getLatitude();
		mLongitude=LunarCalendarSettings.getInstance().getLongitude();
		mTimeZone = LunarCalendarSettings.getInstance().getTimezone();
		temperature = LunarCalendarSettings.getInstance().getTemperature();
		pressure = LunarCalendarSettings.getInstance().getPressure();
		altitude = LunarCalendarSettings.getInstance().getAltitude();
			
		double[] sunRiseSet = solar.calculateSunRiseTransitSet(jd, mLatitude,
				mLongitude, mTimeZone, ΔT);
		sunMoonPosition = new SunMoonPosition(jd, mLatitude, mLongitude,
				altitude, ΔT);
		double moonPhase = sunMoonPosition.getMoonIllimunated();
		
		

		double[] monRiseSetJdFrac = lunar.calculateMoonRiseTransitSetJulian(jd,
				mLatitude, mLongitude, temperature, pressure, altitude);
		byte[] isPOR = AstroLib.isPreceedingOrFollowingDay(monRiseSetJdFrac,
				mTimeZone);
		
		updateViews.setTextViewText(
				R.id.moonRiseTxtView,
				AstroLib.getStringHHMMfromDayFrac(monRiseSetJdFrac[0],
						mTimeZone)
						+ AstroLib.PrecedOrFollowStr(context, isPOR[0]));
		updateViews.setTextViewText(R.id.moonSetTxtView, AstroLib.getStringHHMMfromDayFrac(monRiseSetJdFrac[2],
				mTimeZone)
				+ AstroLib.PrecedOrFollowStr(context, isPOR[2]));
		updateViews.setTextViewText(R.id.litTxtView,oneDigit.format(moonPhase*100)+" %");
		updateViews.setTextViewText(R.id.SunRiseTextView, AstroLib.getStringHHMM(sunRiseSet[1]));
		updateViews.setTextViewText(R.id.SunSetTextView , AstroLib.getStringHHMM(sunRiseSet[2]));
		moonCanvasView = new MoonCanvasView(context);
		double moonAgeConjuction=moonAgeConjuction();
		moonCanvasView.setParameters(mLatitude, mLongitude, jd, ΔT, moonPhase,
				moonAgeConjuction > ApplicationConstants.synmonth / 2, 70, 70);


		updateViews.setImageViewBitmap(R.id.imageMoonWidgetRevised, moonCanvasView.getBitmapImage());
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, HijriCalendarTab.class), 0);
		updateViews.setOnClickPendingIntent(R.id.solarmoonFrameLayout, pendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, updateViews);

	}
	

	private void returnCurrentJulianDay() {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH) + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		mSecond = c.get(Calendar.SECOND);
		mTimeZone = c.getTimeZone().getOffset(c.getTimeInMillis()) / 3600000;


		jd = AstroLib.calculateJulianDay(mYear, mMonth, mDay, mHour, mMinute,
				mSecond, mTimeZone);
		LunarCalendarSettings.getInstance().setJulianDay(jd);
		
	}
	
	
	private double moonAgeConjuction() {
		final double dt = 7.0; // Step (1 week)
		final double acc = (0.5 / 1440.0); // Desired Accuracy (0.5 min)
		boolean[] isFound;
		double tnow, t0, t1;
		double D0, D1;
		tnow = jd;
		t1 = tnow;
		t0 = t1 - dt; // decrease 1 week
		isFound = new boolean[1];
		isFound[0] = false;
		// Search for phases bracket desired phase event
		MoonPhases phases = new MoonPhases();
		D0 = phases.searchPhaseEvent(t0, ΔT, 0);
		D1 = phases.searchPhaseEvent(t1, ΔT, 0);
		while ((D0 * D1 > 0.0) || (D1 < D0)) {
			t1 = t0;
			D1 = D0;
			t0 -= dt;
			D0 = phases.searchPhaseEvent(t0, ΔT, 0);// Finds correct week for
													// iteration
		}
		// Iterate NewMoon time
		double tNewMoon = AstroLib.Pegasus(phases, t0, t1, ΔT, acc, isFound, 0);
		return  jd - tNewMoon;
	}

	
	
	
	
}
