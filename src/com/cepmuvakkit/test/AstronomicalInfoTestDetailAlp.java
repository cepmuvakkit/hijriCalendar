package com.cepmuvakkit.test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.Ecliptic;
import com.cepmuvakkit.times.posAlgo.Equatorial;
import com.cepmuvakkit.times.posAlgo.Horizontal;
import com.cepmuvakkit.times.posAlgo.LunarPosition;
import com.cepmuvakkit.times.posAlgo.SolarPosition;
import com.cepmuvakkit.times.posAlgo.SunMoonPosition;
import com.cepmuvakkit.times.posAlgo.MoonPhaseAngleWithoutDeclination;

public class AstronomicalInfoTestDetailAlp {

	static SolarPosition spa;
	LunarPosition lunar;

	public static void main(String[] args) {
		int temperature = 10, pressure = 1010;
		int altitude = 0;
		// double longitude = 32.85, latitude = 39.95, timezone = 2;// ANKARA
		double longitude = 32.85, latitude = 39.95, timezone = 3;// ANKARA
		// double longitude =-155, latitude =55, timezone =0;// ANKARA
		SolarPosition solar = new SolarPosition();
		LunarPosition lunar = new LunarPosition();
		Ecliptic moonPosEc, sunPosEc;
		Equatorial moonPosEq, sunEq;
		double jd;
		// jd = AstroLib.calculateJulianDay(2012, 8, 18, 16,50, 0, 0);
		jd = AstroLib.calculateJulianDay(2012, 7, 21, 0, 0, 0, 0);
		// jd = AstroLib.calculateJulianDay(2012,8,18, 3,0, 0, 3);
		System.out.println("Date" + AstroLib.fromJulianToCalendarStr(jd));
		System.out.println("Jd :" + jd);
		double ΔT = AstroLib.calculateTimeDifference(jd);
		System.out.println("ΔT :" + ΔT);
		System.out.println("---SOLAR POSITIONS------------");
		sunPosEc = solar.calculateSunEclipticCoordinatesAstronomic(jd, ΔT);
		System.out.println("SOLAR Apperant Ecliptic Geocentric Longitude λ :"
				+ sunPosEc.λ);
		System.out.println("SOLAR Ecliptic Geocentric Latitude β :"
				+ sunPosEc.β);
		System.out
				.println("SOLAR Ecliptic Geocentric Distance  :" + sunPosEc.Δ);
		sunEq = solar.calculateSunEquatorialCoordinates(jd, ΔT);
		System.out.println("SOLAR Right Ascension α :" + sunEq.α);
		System.out.println("SOLAR Declination δ :" + sunEq.δ);
		Horizontal horizontalSun = sunEq.Equ2Topocentric(longitude, latitude,
				altitude, jd, ΔT);
		System.out
				.println("Topocentric Pos Azimuth  Solar:" + horizontalSun.Az);
		System.out.println("Topocentric Pos Altitude Solar :"
				+ (horizontalSun.h));

		System.out.println("---LUNAR POSITIONS------------");
		moonPosEc = lunar.calculateMoonEclipticCoordinates(jd, ΔT);
		System.out.println("Lunar Ecliptic Geocentric Apperant Longitude λ :"
				+ moonPosEc.λ);
		System.out.println("Lunar Ecliptic Geocentric Latitude β :"
				+ moonPosEc.β);
		System.out.println("Lunar Ecliptic Geocentric Distance  :"
				+ moonPosEc.Δ);
		moonPosEq = lunar.calculateMoonEquatorialCoordinates(jd, ΔT);
		System.out
				.println("Lunar Equatorial Right Ascension α :" + moonPosEq.α);
		System.out.println("Lunar Equatorial Declination δ :" + moonPosEq.δ);
		System.out.println("Lunar Equatorial Distance :" + moonPosEq.Δ);
		MoonPhaseAngleWithoutDeclination sunMoonPosition;
		sunMoonPosition = new MoonPhaseAngleWithoutDeclination();
		
		System.out.println("---------------------------------");
		System.out.println("Moon Phase Angle :"
				+ sunMoonPosition.getPhase(jd));
		System.out.println("---------------------------------");


		Horizontal horizontalMoon = moonPosEq.Equ2Topocentric(longitude,
				latitude, altitude, jd, ΔT);
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
		// System.out.println("---MOONRISESET------------");

	/*	for (int i = 0; i < 30; i++) {

			ΔT = AstroLib.calculateTimeDifference(jd + i);
			sunMoonPosition = new SunMoonPositionAlp(jd + i, latitude, longitude,
					altitude, ΔT);
			System.out.println(AstroLib.fromJulianToCalendarStr(jd + i)+' '+':'+' '+
					+ sunMoonPosition.getMoonPhaseAngleWithoutDeclination(jd));
		}
*/


	}
}
