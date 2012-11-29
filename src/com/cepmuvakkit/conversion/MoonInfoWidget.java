package com.cepmuvakkit.conversion;

import java.text.DecimalFormat;
import java.util.Calendar;
import com.cepmuvakkit.conversion.R;
import com.cepmuvakkit.conversion.hicricalendar.HicriCalendar;
import com.cepmuvakkit.conversion.settings.ApplicationConstants;
import com.cepmuvakkit.conversion.settings.LunarCalendarSettings;
import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.LunarPosition;
import com.cepmuvakkit.times.posAlgo.SunMoonPosition;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class MoonInfoWidget extends AppWidgetProvider {
	
	private int mYear, mMonth, mDay, mHour, mMinute, mSecond, temperature,
	pressure, altitude;
	private double mLatitude, mLongitude, mTimeZone;
	private double moonAge, jd; // Julian Day
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
				R.layout.widget_moon);
		returnCurrentJulianDay();
		double ΔT = AstroLib.calculateTimeDifference(jd);
		LunarPosition lunar = new LunarPosition();
		mLatitude=LunarCalendarSettings.getInstance().getLatitude();
		mLongitude=LunarCalendarSettings.getInstance().getLongitude();
		mTimeZone = LunarCalendarSettings.getInstance().getTimezone();
		temperature = LunarCalendarSettings.getInstance().getTemperature();
		pressure = LunarCalendarSettings.getInstance().getPressure();
		altitude = LunarCalendarSettings.getInstance().getAltitude();
			
		double[] moonRiseSet = lunar.calculateMoonRiseTransitSet(jd, mLatitude,
				mLongitude, mTimeZone, temperature, pressure, altitude);
		sunMoonPosition = new SunMoonPosition(jd, mLatitude, mLongitude,
				altitude, ΔT);
		updateViews.setTextViewText(R.id.moonRiseTxtView, AstroLib.getStringHHMM(moonRiseSet[0]));
		updateViews.setTextViewText(R.id.moonSetTxtView, AstroLib.getStringHHMM(moonRiseSet[2]));
		updateViews.setTextViewText(R.id.litTxtView,oneDigit.format(sunMoonPosition. getMoonIllimunated()*100)+" %");
		
		
	//	HegiraCalendar hicriCalendar = new HegiraCalendar(jd, false);
		HicriCalendar hicriCalendar = new HicriCalendar(jd, mTimeZone,18, ΔT);
		moonAge = hicriCalendar.getMoonAge();
		updateViews.setTextViewText(R.id.ageTxtView,oneDigit.format(moonAge) + "d");
		int whichImage = (int) Math.floor(Math.abs(moonAge) * 35.0
				/ ApplicationConstants.synmonth);
		
	int resID = context.getResources().getIdentifier("moon" + pad(whichImage),
				"drawable", "com.cepmuvakkit.conversion");
		updateViews.setImageViewResource(R.id.moonPhaseImage, resID);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, HijriCalendarTab.class), 0);
		updateViews.setOnClickPendingIntent(R.id.widget_moon, pendingIntent);
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
	
	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
	
	
	
	
}
