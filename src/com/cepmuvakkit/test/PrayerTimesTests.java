/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cepmuvakkit.test;

import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.EarthPosition;
import com.cepmuvakkit.times.posAlgo.HigherLatitude;
import com.cepmuvakkit.times.posAlgo.Methods;
import com.cepmuvakkit.times.posAlgo.PTimes;

/**
 *
 * @author mehmetrg
 */
public class PrayerTimesTests implements Methods, HigherLatitude {

    static double[] salat = new double[6];
    public static void main(String[] args) {

         //byte[] offsetSalat = {-1, -6, 7, 5, 5, 9, 2};//default
         //byte[] offsetSalat = {-4, -6, 7, 5, 5, 8, 4};// 8.7.2009
       //  byte[] offsetSalat = {-4, -6, 7, 5, 5, 6, 3};
//        byte[] offsetSalat = {0, 0, 0, 0, 0, 0, 0};
       // double longitude = 0.0, latitude =69.5, timezone = 0;
        byte FAJR = 0, SUNRISE = 1, SUNTRANSIT = 2, ASR_SHAFI = 3, ASR_HANEFI = 4, SUNSET = 5, ISHA = 6;//, SUN_COUNT = 7;
       // private final byte FAJR = 0, SUNRISE = 1, SUNTRANSIT = 2, ASR_SHAFI = 3, ASR_HANEFI = 4, SUNSET = 5, ISHA = 6, SUN_COUNT = 7;

      
      //  double longitude =8.207281, latitude =60.533412 , timezone =2; //NORVEC GEİLO
        double latitude =39.95,longitude =32.85, timezone =3; //Ankara
      // double longitude =-0.023003, latitude =51.579203, timezone =1;//MASJID-E-UMER TRUST
        int altitude=0, temperature = 10, pressure = 1010;
        double jd;

        jd = AstroLib.calculateJulianDay(2010,4,27, 0, 0, 0, 0);
    	//jd = AstroLib.calculateJulianDay(mYear, mMonth, mDay, mHour, mMinute,mSecond, mTimeZone);
        System.out.println("Date" + AstroLib.fromJulianToCalendarStr(jd));
        EarthPosition loc = new EarthPosition(latitude, longitude, timezone, altitude, temperature, pressure);
        byte[] estMethod = {NO_ESTIMATION,NO_ESTIMATION};
        byte calculationMethod = TURKISH_RELIGOUS;
        PTimes ptimes = new PTimes(jd, loc, calculationMethod, estMethod);

        salat = new double[6];
        salat=ptimes.getSalat();
      /*  salat[FAJR] = salat[FAJR] + offsetSalat[FAJR] / 60.0;
        salat[SUNRISE] = salat[SUNRISE] + offsetSalat[SUNRISE] / 60.0;
        salat[SUNTRANSIT] = salat[SUNTRANSIT] + offsetSalat[SUNTRANSIT] / 60.0;
        salat[ASR_SHAFI] = salat[ASR_SHAFI] + offsetSalat[ASR_SHAFI] / 60.0;
        salat[ASR_HANEFI] = salat[ASR_HANEFI] + offsetSalat[ASR_HANEFI] / 60.0;
        salat[SUNSET] = salat[SUNSET] + offsetSalat[SUNSET] / 60.0;
        salat[ISHA] = salat[ISHA] + offsetSalat[ISHA] / 60.0;*/
        System.out.println("---PRAYER---------------");
        System.out.println("FAJR      :" + AstroLib.getStringHHMMSSS(salat[0]));
        System.out.println("SunRise   :" + AstroLib.getStringHHMMSSS(salat[1]));
        System.out.println("Transit   :" + AstroLib.getStringHHMMSSS(salat[2]));
        System.out.println("ASR SAFI  :" + AstroLib.getStringHHMMSSS(salat[3]));
        System.out.println("ASR HANEFI:" + AstroLib.getStringHHMMSSS(salat[4]));
        System.out.println("SunSet    :" + AstroLib.getStringHHMMSSS(salat[5]));
        System.out.println("ISHA      :" + AstroLib.getStringHHMMSSS(salat[6]));

    }
}
