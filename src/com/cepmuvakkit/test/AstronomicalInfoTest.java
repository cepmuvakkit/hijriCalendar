/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cepmuvakkit.test;

import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.Ecliptic;
import com.cepmuvakkit.times.posAlgo.Equatorial;
import com.cepmuvakkit.times.posAlgo.Horizontal;
import com.cepmuvakkit.times.posAlgo.LunarPosition;
import com.cepmuvakkit.times.posAlgo.SolarPosition;

//import java.util.Calendar;
/**
 *
 * @author mgeden
 */
public class AstronomicalInfoTest  {

    static SolarPosition spa;
    LunarPosition lunar;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // double L,beta,alpha,epsilon,epsilon0,jme,jce,jd,delta_epsilon,eot,M,del_psi,R,theta,lamda, delta_tau;
        double[] Salat = new double[6],Salat2 = new double[6],SPA = new double[3], kerahat = new double[8],kerahat1 = new double[8];

        boolean isValid=true;
   //    final byte FAJR_ = 0, ISRAK = 1, SUNTRANSIT_ = 2, ASRHANEFI = 3, ISFIRAR = 4, SUNSET_ = 5, KERAHAT_COUNT = 6, DUHA = 7, ISTIVA = 8;
        //double longitude = 16.5, latitude = 69.5,timezone=1, fajrAngle = -18, ishaAngle = -17, israkIsfirarAngle = 5;
        int temperature=10, pressure=1010;
        //int[] offsetSalat = new int[5];
        byte[] offsetSalat ={-1,-6,7,5,9,2,10};
       // double  temkinFajr=-1/60.0, temkinSunrise=-6/60.0, temkinZuhr=7/60.0, temkinAsr=5/60.0,temkinMagrib=9/60.0, temkinIsha=2/60.0, temkinIsrak=10/60.0;
         double longitude = 32.85, latitude = 39.95,timezone=2;//ANKARA position
         double  fajrAngle = -18, ishaAngle = -17, israkIsfirarAngle = 5;
        SolarPosition solar = new SolarPosition();
        LunarPosition lunar = new LunarPosition();
        Ecliptic moonPosEc, sunPosEc;
        Equatorial moonPosEq, sunEq;

       // double longitude =-116.8625, latitude =33.356111111111112, timezone = 0;
        double jd;
      //  for (int i=1; i<30; i++){
        jd = AstroLib.calculateJulianDay(2010,2,8, 0, 0, 0, 0);
        System.out.println("Date" + AstroLib.fromJulianToCalendarStr(jd));
        System.out.println("Date" + AstroLib.fromJulianToCalendarStr(jd));
        System.out.println("Jd :" + jd);
        double ΔT = AstroLib.calculateTimeDifference(jd);
        System.out.println("ΔT :" + ΔT );
        System.out.println("---SOLAR POSITIONS------------");
        sunPosEc=solar.calculateSunEclipticCoordinatesAstronomic(jd, ΔT);
        System.out.println("SOLAR Apperant Longitude λ :" +  sunPosEc.λ);
        System.out.println("SOLAR Latitude β :" +  sunPosEc.β);
        System.out.println("SOLAR Distance  :" +  sunPosEc.Δ);
        sunEq=solar.calculateSunEquatorialCoordinates(jd, ΔT);
        System.out.println("SOLAR Right Ascension α :" + sunEq.α);
        System.out.println("SOLAR Declination δ :" + sunEq.δ);
        Horizontal horizontalSun=sunEq.Equ2Topocentric(longitude, latitude,0,jd,ΔT);
        System.out.println("Topocentric Pos Azimuth  Solar:" +horizontalSun.Az );
        System.out.println("Topocentric Pos Altitude Solar :" +(horizontalSun.h));

        System.out.println("---LUNAR POSITIONS------------");
        moonPosEc = lunar.calculateMoonEclipticCoordinates(jd, ΔT);
        System.out.println("Lunar Apperant Longitude λ :" + moonPosEc.λ);
        System.out.println("Lunar Latitude β :" + moonPosEc.β);
        System.out.println("Lunar Distance  :" + moonPosEc.Δ);
        moonPosEq = lunar.calculateMoonEquatorialCoordinates(jd, ΔT);
        System.out.println("Lunar Right Ascension α :" + moonPosEq.α);
        System.out.println("Lunar Declination δ :" + moonPosEq.δ);
        Horizontal horizontalMoon=moonPosEq.Equ2Topocentric(longitude, latitude,0,jd,ΔT);
        System.out.println("Lunar Topocentric Pos Azimuth :" +horizontalMoon.Az );
        System.out.println("Lunar Topocentric Pos elevation:" +horizontalMoon.h);
        double elevationCorrected=horizontalMoon.h+AstroLib.getAtmosphericRefraction(horizontalMoon.h)*AstroLib.getWeatherCorrectionCoefficent(temperature, pressure);
        System.out.println("Lunar Topocentric Pos elevation with Atmospheric correction:" +elevationCorrected);
        System.out.println("---MOONRISESET------------");
        lunar.calculateMoonRiseTransitSet(jd, latitude, longitude, timezone,10, 1010, 0);
        solar.calculateSunRiseTransitSet(SPA, jd); 
        Salat = solar.calculateSalatTimes(jd, latitude, longitude, timezone, 10, 1010,0, fajrAngle, ishaAngle);
       // Salat2 = solar.calculateSalatTimes(jd, latitude, longitude, timezone, 10, 1010,0, fajrAngle, ishaAngle,isValid);
        solar.calculateSunRiseTransitSet(SPA, jd);
        kerahat = solar.calculateKerahetTimes(jd, latitude, longitude, timezone, 10, 1010, 0, fajrAngle, israkIsfirarAngle);
     //   kerahat1 = sTimes.calculateKerahetTimes(jd, latitude, longitude, timezone, fajrAngle, israkIsfirarAngle, 10, 1010, 0.0);
    //    Salat2 = sTimes.calculateSalatTimes(jd, latitude, longitude, timezone, fajrAngle, ishaAngle, 10, 1010, 0.0);

        //final int FAJR_=0,ISR
        //solar.calculateTimeDifference(jd);
        //final int FAJR_=0,ISRAK=1,SUNTRANSIT_=2,ASRHANEFI=3,ISFIRAR=4,SUNSET_=5,KERAHAT_COUNT=6,DUHA=7,ISTIVA=8;*/
    System.out.println("---SUNRISESET---------------");
        System.out.println("SunRise SPA    :" + AstroLib.getStringHHMMSSS( SPA[1]));
        System.out.println("Transit SPA    :" + AstroLib.getStringHHMMSSS( SPA[0]));
        System.out.println("SunSet  SPA    :" + AstroLib.getStringHHMMSSS( SPA[2]));
     /*      System.out.println("---PRAYER---------------");
      System.out.println("---KERAHAT---------------");
      //  System.out.println("FAJR        :" + AstroLib.SecTime(kerahat[FAJR_]));
        System.out.println("ISRAK       :" + AstroLib.SecTime(kerahat[ISRAK]+offsetSalat[6]/60.0));
        System.out.println("DUHA        :" + AstroLib.SecTime(kerahat[DUHA]));
        System.out.println("ISTIVA      :" + AstroLib.SecTime(kerahat[ISTIVA]));
        System.out.println("ASR HANEFI  :" + AstroLib.SecTime(kerahat[ASRHANEFI]+offsetSalat[3]/60.0));
        System.out.println("ISFIRAR     :" + AstroLib.SecTime(kerahat[ISFIRAR]));
        //double deltaT=  solar.calculateTimeDifference(jd);
        // System.out.println("ΔT : 2009  : " +deltaT);*/


    }
}
