/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cepmuvakkit.test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.EarthPosition;
import com.cepmuvakkit.times.posAlgo.HigherLatitude;
import com.cepmuvakkit.times.posAlgo.Methods;
import com.cepmuvakkit.times.posAlgo.PTimes;

/**
 * 
 * @author mehmetrg
 */
public class PrayerTimesTests2 implements Methods, HigherLatitude {

	static double[] salat = new double[6];

	public static void main(String[] args) {

		GregorianCalendar[] times = new GregorianCalendar[7];
		byte[] estMethod = { NO_ESTIMATION, NO_ESTIMATION };
		byte calculationMethod = TURKISH_RELIGOUS;
		double jd = AstroLib.calculateJulianDay(2012, 1, 1, 0, 0, 0, 0);

		System.out.println("Date" + AstroLib.fromJulianToCalendarStr(jd));
		EarthPosition loc = new EarthPosition(39.95f, 32.85f, 3.0f, 0, 10, 1010);
		PTimes ptimes = new PTimes(jd, loc, calculationMethod, estMethod);
		times = ptimes.getSalatinGregorian();

		System.out.println("---PRAYER---------------");
		System.out.println("FAJR      :"
				+ AstroLib.getHHMMSSfromGreCal(times[0]));
		System.out.println("SunRise   :"
				+ AstroLib.getHHMMSSfromGreCal(times[1]));
		System.out.println("Transit   :"
				+ AstroLib.getHHMMSSfromGreCal(times[2]));
		System.out.println("ASR       :"
				+ AstroLib.getHHMMSSfromGreCal(times[3]));

		System.out.println("SunSet    :"
				+ AstroLib.getHHMMSSfromGreCal(times[4]));
		System.out.println("ISHA      :"
				+ AstroLib.getHHMMSSfromGreCal(times[5]));

		System.out.println("DATE                    " + "FAJR      "
				+ "SunRise   " + "Transit    " + "ASR        " + "MAHGRIB    "
				+ "ISHA  ");

		for (int date = 0; date < 365; date++) {
			jd = AstroLib.calculateJulianDay(2012, 1, 1, 0, 0, 0, 0);
			ptimes = new PTimes(jd + date, loc, calculationMethod, estMethod);
			times = ptimes.getSalatinGregorian();

			System.out.print(AstroLib.fromJulianToCalendarStr(jd + date)
					+ "   ");
			System.out
					.print(AstroLib.getHHMMSSfromGreCal(times[0]));
			System.out.print("   "
					+ AstroLib.getHHMMSSfromGreCal(times[1]));
			System.out.print("   "
					+ AstroLib.getHHMMSSfromGreCal(times[2]));
			System.out.print("   "
					+ AstroLib.getHHMMSSfromGreCal(times[3]));
			System.out.print("   "
					+ AstroLib.getHHMMSSfromGreCal(times[4]));
			System.out.println("   "
					+ AstroLib.getHHMMSSfromGreCal(times[5]));
		}

	}

	public static double getGMTOffset() {
		Calendar now = new GregorianCalendar();
		int gmtOffset = now.getTimeZone().getOffset(now.getTimeInMillis());
		return gmtOffset / 3600000;
	}
}
