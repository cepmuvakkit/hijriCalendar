package com.cepmuvakkit.test;

import com.cepmuvakkit.conversion.hicricalendar.HicriCalendar;
import com.cepmuvakkit.conversion.hicricalendar.HicriCalendarAlperen;
import com.cepmuvakkit.times.posAlgo.AstroLib;

/**
 * 
 * @author http://www.cepmuvakkit.com
 */
public class HicriCalendarAlpTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		System.out
				.println("  Gregorian to Hijri Converter This program calculates  Hijri date according to the global moonsighting criteria\n"
						+ "Please keep in mind hijri dates starts with magrib prayer, this converter checks only the gregorian days after 12:00 pm.\n"
						+ "***This  code cannot used unless  resource is stated which is www.cepmuvakkit.com and cannot used for commercial purposes without  permission***");

		double jd, ΔT, mLatitude = 39.95, mLongitude = 32.85, altitude = 0, mTimeZone = 3; // Julian
		int mYear = 2012, mMonth = 8, mDay = 18, mHour = 16, mMinute = 41, mSecond = 0;

		String ht_alp, ht_my;
		// jd = AstroLib.calculateJulianDay(mYear, mMonth, mDay, mHour,
		// mMinute,mSecond, mTimeZone);
		jd = AstroLib.calculateJulianDay(2012, 12, 14, 0, 0, 0, 0);
		
		
		double jdnew;
		HicriCalendarAlperen hc_alp = new HicriCalendarAlperen(jd);
		ht_alp = hc_alp.getHicriTakvim() + " " + hc_alp.getDay();
		System.out.println(AstroLib.fromJulianToCalendarStr(jd)
				+ " : " + ht_alp);
		
		jd = AstroLib.calculateJulianDay(2010, 1, 1, 0, 0, 0, 0);
		
		for (int i = 0; i < 365*3; i++) {
			jdnew = jd + i;
			hc_alp = new HicriCalendarAlperen(jdnew);
			ΔT = AstroLib.calculateTimeDifference(jdnew);
			HicriCalendar hc_my = new HicriCalendar(jdnew, 0, 18.0, ΔT);
			ht_alp = hc_alp.getHicriTakvim() + " " + hc_alp.getDay();
     		ht_my = hc_my.getHicriTakvim() + " " + hc_my.getDay();
			System.out.println(AstroLib.fromJulianToCalendarStr(jdnew)
					+ " : " + ht_alp);
			
			/*if ((hc_alp.getHijriDay()) != (hc_my.getHijriDay())& ((hc_alp.getHijriDay()==1)||(hc_my.getHijriDay()==1))) {
				System.out.println(AstroLib.fromJulianToCalendarStr(jdnew)
						+ " : " + ht_my + " : ALP :" + ht_alp);
			}*/
		}
	}
}
