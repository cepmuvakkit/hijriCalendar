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

public class MoonObserveRegion {

	static SolarPosition spa;
	LunarPosition lunar;

	public static void main(String[] args) {
		int temperature = 10, pressure = 1010;
		int altitude = 0;
		double longitude = -155, latitude = 55, timezone = 0;// ANKARA
		SolarPosition solar = new SolarPosition();
		LunarPosition lunar = new LunarPosition();
		Ecliptic moonPosEc, sunPosEc;
		Equatorial moonPosEq, sunEq;
		double jd;
		jd = AstroLib.calculateJulianDay(2009, 3, 27, 4, 41, 0, 0);
		System.out.println("Date" + AstroLib.fromJulianToCalendarStr(jd));
		System.out.println("Jd :" + jd);
		double ΔT = AstroLib.calculateTimeDifference(jd);
		System.out.println("ΔT :" + ΔT);
		System.out.println("---SOLAR POSITIONS------------");
		sunPosEc = solar.calculateSunEclipticCoordinatesAstronomic(jd, ΔT);
		System.out.println("SOLAR Apperant Longitude λ :" + sunPosEc.λ);
		System.out.println("SOLAR Latitude β :" + sunPosEc.β);
		System.out.println("SOLAR Distance  :" + sunPosEc.Δ);
		sunEq = solar.calculateSunEquatorialCoordinates(jd, ΔT);
		System.out.println("SOLAR Right Ascension α :" + sunEq.α);
		System.out.println("SOLAR Declination δ :" + sunEq.δ);
		Horizontal horizontalSun = sunEq.Equ2Topocentric(longitude, latitude,
				0, jd, ΔT);
		System.out
				.println("Topocentric Pos Azimuth  Solar:" + horizontalSun.Az);
		System.out.println("Topocentric Pos Altitude Solar :"
				+ (horizontalSun.h));

		System.out.println("---LUNAR POSITIONS------------");
		moonPosEc = lunar.calculateMoonEclipticCoordinates(jd, ΔT);
		System.out.println("Lunar Apperant Longitude λ :" + moonPosEc.λ);
		System.out.println("Lunar Latitude β :" + moonPosEc.β);
		System.out.println("Lunar Distance  :" + moonPosEc.Δ);
		moonPosEq = lunar.calculateMoonEquatorialCoordinates(jd, ΔT);
		System.out.println("Lunar Right Ascension α :" + moonPosEq.α);
		System.out.println("Lunar Declination δ :" + moonPosEq.δ);
		SunMoonPosition sunMoonPosition;
		sunMoonPosition = new SunMoonPosition(jd, latitude, longitude,
				altitude, ΔT);
		System.out.println("Lunar Elevation :"
				+ sunMoonPosition.getMoonPosition().getElevation());

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
		lunar.calculateMoonRiseTransitSet(jd, latitude, longitude, timezone,
				10, 1010, 0);
		for (int lon = -90; lon> -170; lon--) {
			for (int lat = 0; lat < 60; lat++) {
				horizontalMoon = moonPosEq.Equ2Topocentric(lon,
						lat, altitude, jd, ΔT);
				if (horizontalMoon.h>5)
				{
					//System.out.print(" " + lat);
					System.out.println(" (" + lat + ',' + lon+")"+"="+horizontalMoon.h);
				}
				
			}
		}
	}
}
