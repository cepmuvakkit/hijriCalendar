/*
 * To compile and run this source code you have to select text file encoding UTF-8
 * otherwise you can see so many "Invalid Character"  errors at the source code.
 * In Eclipse IDE right click on the Project --> Properties--> Resource--> 
 * Text File Encoding--> Other-->UTF-8 
 */
package com.cepmuvakkit.test;

import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.Ecliptic;
import com.cepmuvakkit.times.posAlgo.Equatorial;
import com.cepmuvakkit.times.posAlgo.Horizontal;
import com.cepmuvakkit.times.posAlgo.LunarPosition;
import com.cepmuvakkit.times.posAlgo.SolarPosition;

/**
 * 
 * @author mgeden
 */
public class MoonRiseSetTest {
	static SolarPosition spa;
	LunarPosition lunar;

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		double longitude = 32.85, latitude = 39.95, timezone = 2;
		int temperature = 10, pressure = 1010;// ANKARA position
		LunarPosition lunar = new LunarPosition();
		Ecliptic moonPosEc;
		Equatorial moonPosEq;
		double jd = AstroLib.calculateJulianDay(2012, 12, 1, 0, 0, 0, 0);
		double ΔT = AstroLib.calculateTimeDifference(jd);
		System.out.println("---LUNAR POSITIONS------------");
		moonPosEc = lunar.calculateMoonEclipticCoordinates(jd, ΔT);
		System.out.println("Lunar Apperant Longitude λ :" + moonPosEc.λ);
		System.out.println("Lunar Latitude β :" + moonPosEc.β);
		System.out.println("Lunar Distance  :" + moonPosEc.Δ);
		moonPosEq = lunar.calculateMoonEquatorialCoordinates(jd, ΔT);
		System.out.println("Lunar Right Ascension α :" + moonPosEq.α);
		System.out.println("Lunar Declination δ :" + moonPosEq.δ);
		Horizontal horizontalMoon = moonPosEq.Equ2Topocentric(longitude,
				latitude, 0, jd, ΔT);
		System.out.println("Lunar Topocentric Pos Azimuth :"
				+ horizontalMoon.Az);
		System.out.println("Lunar Topocentric Pos elevation:"
				+ horizontalMoon.h);
		double elevationCorrected = horizontalMoon.h
				+ AstroLib.getAtmosphericRefraction(horizontalMoon.h)
				* AstroLib
						.getWeatherCorrectionCoefficent(temperature, pressure);
		System.out
				.println("Lunar Topocentric Pos elevation with Atmospheric correction:"
						+ elevationCorrected);
		System.out.println("---MOONRISESET------------");
		double[] monRiseSet,monRiseSetJdFrac;
		byte[] isPrecedingOrFollowing = new byte[3];
		System.out.print("Date" + AstroLib.fromJulianToDDMMYYYY(jd) + "  ");

		monRiseSet = lunar.calculateMoonRiseTransitSet(jd, latitude, longitude,
				timezone, 10, 1010, 0);
		System.out.println("Rise: " + AstroLib.getStringHHMM(monRiseSet[0])
				+ " Transit:" + AstroLib.getStringHHMM(monRiseSet[1]) + " Set:"
				+ AstroLib.getStringHHMM(monRiseSet[2]));

		monRiseSetJdFrac = lunar.calculateMoonRiseTransitSetJulian(jd, latitude,
				longitude, 10, 1010, 0);

		isPrecedingOrFollowing[1] = (byte) Math.floor(monRiseSetJdFrac[1] + timezone
				/ 24.0);
		isPrecedingOrFollowing[0] = (byte) Math.floor(monRiseSetJdFrac[0] + timezone
				/ 24.0);
		isPrecedingOrFollowing[2] = (byte) Math.floor(monRiseSetJdFrac[2] + timezone
				/ 24.0);
	
	/*	System.out.println("Rise: " + AstroLib.getStringHHMMfromDayFrac(monRiseSetJdFrac[0],timezone) 
				+AstroLib.isPreceedingOrFollowingDay(isPrecedingOrFollowing[0]) + " Transit:"
				+ AstroLib.getStringHHMMfromDayFrac(monRiseSetJdFrac[1],timezone) 
				+ AstroLib.isPreceedingOrFollowingDay(isPrecedingOrFollowing[1]) + " Set:"
				+ AstroLib.getStringHHMMfromDayFrac(monRiseSetJdFrac[2],timezone) 
				+ AstroLib.isPreceedingOrFollowingDay(isPrecedingOrFollowing[2]));
		*/
		System.out.println("Date   " + AstroLib.fromJulianToDDMMYYYY(jd));
		System.out.println("Rise   : " + AstroLib.getStringHHMMfromDayFrac(monRiseSetJdFrac[0],timezone) 
				+AstroLib.PORStr(isPrecedingOrFollowing[0]) );
		System.out.println("Transit: " + AstroLib.getStringHHMMfromDayFrac(monRiseSetJdFrac[1],timezone) 
				+AstroLib.PORStr(isPrecedingOrFollowing[1]) );
		System.out.println("Set    : " + AstroLib.getStringHHMMfromDayFrac(monRiseSetJdFrac[2],timezone) 
				+AstroLib.PORStr(isPrecedingOrFollowing[2]) );
		
		
		

		// for (int i = 0; i <= 10; i++) {
		// System.out.print("Date" + AstroLib.fromJulianToDDMMYYYY(jd + i)
		// + "  ");
		// monRiseSet = lunar.calculateMoonRiseTransitSet(jd + i, latitude,
		// longitude, timezone, 10, 1010, 0);
		// System.out.println("Rise: " + AstroLib.getStringHHMM(monRiseSet[0])+
		// " Transit:" + AstroLib.getStringHHMM(monRiseSet[1])
		// + " Set:" + AstroLib.getStringHHMM(monRiseSet[2]));
		// }

	}
}
