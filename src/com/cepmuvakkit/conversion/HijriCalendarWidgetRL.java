package com.cepmuvakkit.conversion;

import java.util.Calendar;
import com.cepmuvakkit.conversion.R;
import com.cepmuvakkit.conversion.hicricalendar.HicriCalendar;
import com.cepmuvakkit.conversion.settings.LunarCalendarSettings;
import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.SolarPosition;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class HijriCalendarWidgetRL extends AppWidgetProvider {
	private SharedPreferences preferences;
	private int mYear, mMonth, mDay, mHour, mMinute, mSecond;
	private double mLatitude, mLongitude, mTimeZone;
	//private double currentTimeHour;
	private double mSunsetHour, jd; // Julian Day
	private boolean isAfterMagrib = false;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		LunarCalendarSettings.load(preferences);

		returnCurrentJulianDay();

		mLatitude = LunarCalendarSettings.getInstance().getLatitude();
		mLongitude = LunarCalendarSettings.getInstance().getLongitude();
		// mTimeZone = LunarCalendarSettings.getInstance().getTimezone();

		double ΔT = AstroLib.calculateTimeDifference(jd);
		SolarPosition solar = new SolarPosition();
		double[] sunRiseSet = solar.calculateSunRiseTransitSet(jd, mLatitude,
				mLongitude, mTimeZone, ΔT);

		mSunsetHour = sunRiseSet[2];
//		isAfterMagrib = false;
//		if (currentTimeHour > mSunsetHour) {
//			isAfterMagrib = true;
//		}
		
		HicriCalendar hicriCalendar = new HicriCalendar(jd, mTimeZone, mSunsetHour, ΔT,LunarCalendarSettings.getInstance().getAdjusment());
//		HegiraCalendar hicriCalendar = new HegiraCalendar(
//				Math.floor(jd + 0.5) - 0.5, isAfterMagrib);

		RemoteViews updateViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_hijri_relative);

		updateViews.setTextViewText(R.id.Month,
				hicriCalendar.getHijriMonthName(context));

		updateViews
				.setTextViewText(R.id.DayName, hicriCalendar.getDay(context));
		updateViews.setTextViewText(R.id.DayNumber, hicriCalendar.getHijriDay()
				+ "");

		updateViews.setTextViewText(R.id.hijriYear,
				hicriCalendar.getHijriYear() + "");
		updateViews.setTextViewText(R.id.IsHolyDay,
				hicriCalendar.checkIfHolyDay(context, isAfterMagrib));

		updateViews.setTextViewText(R.id.LocAlertTxtVvw, "");
		if ((mLatitude == 0) && (mLongitude == 0)) {
			updateViews.setTextViewText(R.id.LocAlertTxtVvw,
					context.getString(R.string.SetLocation));

		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, HijriCalendarTab.class), 0);
		updateViews.setOnClickPendingIntent(R.id.widget_hijri_relativelayout,
				pendingIntent);
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

		//currentTimeHour = mHour + mMinute / 60.0;
		jd = AstroLib.calculateJulianDay(mYear, mMonth, mDay, mHour, mMinute,
				mSecond, mTimeZone);
		LunarCalendarSettings.getInstance().setJulianDay(jd);

	}

}
