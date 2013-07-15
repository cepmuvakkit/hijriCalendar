package com.cepmuvakkit.test;

import com.cepmuvakkit.conversion.hicricalendar.HicriCalendar;
import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.SolarPosition;


/**
 * 
 * @author http://www.cepmuvakkit.com
 */
public class HicriCalTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		System.out
				.println("  Gregorian to Hijri Converter This program calculates  Hijri date according to the global moonsighting criteria\n"
						+ "Please keep in mind hijri dates starts with magrib prayer, this converter checks only the gregorian days after 12:00 pm.\n"
						+ "***This  code cannot used unless  resource is stated which is www.cepmuvakkit.com and cannot used for commercial purposes without  permission***");


		int year, month, day, hourOfDay, minute;
		double moonAge,jd,mSunsetHour,ΔT,mLatitude=39.95,mLongitude=32.85,mTimeZone=3; // Julian Day
		int mYear=2012, mMonth=8, mDay=17, mHour=20, mMinute=20, mSecond=0;
		
		String hd;
		HicriCalendar hc;
		jd = AstroLib.calculateJulianDay(mYear, mMonth, mDay, mHour, mMinute,
				mSecond, mTimeZone);
		ΔT = AstroLib.calculateTimeDifference(jd);
		SolarPosition solar = new SolarPosition();
		double[] sunRiseSet = solar.calculateSunRiseTransitSet(jd, mLatitude,
				mLongitude, mTimeZone, ΔT);
		mSunsetHour = sunRiseSet[2];
//		// 47°36′35″N 122°19′59″W

//
				hc = new HicriCalendar(jd, mTimeZone, mSunsetHour, ΔT,0);
				hd = hc.getHicriTakvim() + " " + hc.getDay();
				System.out.println(hd);
	

	}
}

