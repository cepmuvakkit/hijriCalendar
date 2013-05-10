package com.cepmuvakkit.conversion;

import java.text.DateFormat;
import java.text.DecimalFormat;

import com.cepmuvakkit.conversion.R;
import com.cepmuvakkit.conversion.hicricalendar.HicriCalendar;
import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.Ecliptic;
import com.cepmuvakkit.times.posAlgo.Equatorial;
import com.cepmuvakkit.times.posAlgo.Horizontal;
import com.cepmuvakkit.times.posAlgo.LunarPosition;
import com.cepmuvakkit.times.posAlgo.SolarPosition;
import com.cepmuvakkit.times.posAlgo.SunMoonPosition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AstronomicalDetail extends Activity {
	private double jd, longitude, latitude, timezone;

	private int temperature, pressure, altitude;// ANKARA position
	private double ΔT;
	private DecimalFormat oneDigit, twoDigit;
	private DateFormat dfTr;
	private TextView mDate, mTimeDiff, mJulianDay, mEclipticLongitude,
			mEclipticLatitude, mRightAscension, mDeclination, mDistance,
			mMoonRise, mMoonTransit, mMoonSet, mAge, mIlluminated,
			mLunarAzimuth, mLunarElevation, mLunarElevationAtmosphere,
			mIslamicLunation, mBrownLunation, mSunRise, mZenith, mSunset,
			mSolarEclipticLongitude, mSolarEclipticLatitude,
			mSolarEquatorialDistance, mSolarRightAscension, mSolarDeclination,
			mSolarAzimuth, mSolarElevation;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String jdString = intent.getStringExtra(HijriCalendarTab.JULIAN_DAY);
		String latitudeStr = intent.getStringExtra(HijriCalendarTab.LATITUDE);
		String longitudeStr = intent.getStringExtra(HijriCalendarTab.LONGITUDE);
		String timezoneStr = intent.getStringExtra(HijriCalendarTab.TIMEZONE);
		String temperatureStr = intent
				.getStringExtra(HijriCalendarTab.TEMPERATURE);
		String pressureStr = intent.getStringExtra(HijriCalendarTab.PRESSURE);
		String altitudeStr = intent.getStringExtra(HijriCalendarTab.ALTITUDE);
		twoDigit = new DecimalFormat("#0.00");
		oneDigit = new DecimalFormat("#0.0");
		dfTr = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.MEDIUM);

		jd = Double.parseDouble(jdString);
		latitude = Double.parseDouble(latitudeStr);
		longitude = Double.parseDouble(longitudeStr);
		timezone = Double.parseDouble(timezoneStr);
		temperature = Integer.parseInt(temperatureStr);
		pressure = Integer.parseInt(pressureStr);
		altitude = Integer.parseInt(altitudeStr);
		ΔT = AstroLib.calculateTimeDifference(jd);

		SolarPosition solar = new SolarPosition();
		LunarPosition lunar = new LunarPosition();
		SunMoonPosition sunMoonPosition;
		Ecliptic moonPosEc, sunPosEc;
		Equatorial moonPosEq, sunEq;
		sunPosEc = solar.calculateSunEclipticCoordinatesAstronomic(jd, ΔT);
		sunEq = solar.calculateSunEquatorialCoordinates(jd, ΔT);
		Horizontal horizontalSun = sunEq.Equ2Topocentric(longitude, latitude,
				altitude, jd, ΔT);

		moonPosEc = lunar.calculateMoonEclipticCoordinates(jd, ΔT);
		moonPosEq = lunar.calculateMoonEquatorialCoordinates(jd, ΔT);
		sunMoonPosition = new SunMoonPosition(jd, latitude, longitude,
				altitude, ΔT);

		Horizontal horizontalMoon = moonPosEq.Equ2Topocentric(longitude,
				latitude, altitude, jd, ΔT);
		double elevationCorrected = horizontalMoon.h
				+ AstroLib.getAtmosphericRefraction(horizontalMoon.h)
				* AstroLib
						.getWeatherCorrectionCoefficent(temperature, pressure);

		setContentView(R.layout.moreinfo);

		mDate = (TextView) findViewById(R.id.DateTxtFld);

		mTimeDiff = (TextView) findViewById(R.id.TimeDifferenceTxtFld);
		mJulianDay = (TextView) findViewById(R.id.JulianDayTxt);
		mEclipticLongitude = (TextView) findViewById(R.id.EclipticLongitudeTxtFld);
		mEclipticLatitude = (TextView) findViewById(R.id.EclipticLatitudeTxtFld);
		mRightAscension = (TextView) findViewById(R.id.RightAscensionTxtFld);
		mDeclination = (TextView) findViewById(R.id.DeclinationTxtFld);
		mDistance = (TextView) findViewById(R.id.DistanceTxtFld);

		mMoonRise = (TextView) findViewById(R.id.MoonRiseTxtFld);
		mMoonTransit = (TextView) findViewById(R.id.MoonTransitTxtFld);
		mMoonSet = (TextView) findViewById(R.id.MoonSetTxtFld);
		mAge = (TextView) findViewById(R.id.AgeTxtFld);
		mIlluminated = (TextView) findViewById(R.id.IlluminatedTxtFld);
		mLunarAzimuth = (TextView) findViewById(R.id.LunarAzimuthTxtFld);
		mLunarElevation = (TextView) findViewById(R.id.LunarElevationTxtFld);
		mLunarElevationAtmosphere = (TextView) findViewById(R.id.LunarElevationAtmosphereTxtFld);
		mIslamicLunation = (TextView) findViewById(R.id.IslamicLunationTxtFld);
		mBrownLunation = (TextView) findViewById(R.id.BrownLunationTxtFld);
		mSunRise = (TextView) findViewById(R.id.SunRiseTxtFld);
		mZenith = (TextView) findViewById(R.id.ZenithTxtFld);
		mSunset = (TextView) findViewById(R.id.SunsetTxtFld);
		mSolarEclipticLongitude = (TextView) findViewById(R.id.SolarEclipticLongitudeTxtFld);
		mSolarEclipticLatitude = (TextView) findViewById(R.id.SolarEclipticLatitudeTxtFld);
		mSolarRightAscension = (TextView) findViewById(R.id.SolarRightAscensionTxtFld);
		mSolarDeclination = (TextView) findViewById(R.id.SolarDeclinationTxtFld);
		mSolarEquatorialDistance = (TextView) findViewById(R.id.SunDistanceTxtFld);
		mSolarAzimuth = (TextView) findViewById(R.id.SolarAzimuthTxtFld);
		mSolarElevation = (TextView) findViewById(R.id.SolarElevationTxtFld);
		// mSolarElevationAtmoshpere = (TextView)
		// findViewById(R.id.SolarElevationAtmoshpereTxtFld);
		mSolarRightAscension = (TextView) findViewById(R.id.SolarRightAscensionTxtFld);

		mDate.setText(dfTr.format(AstroLib.convertJulian2Gregorian(
				jd + timezone / 24).getTime()));
		mTimeDiff.setText(twoDigit.format(ΔT) + "''");
		mJulianDay.setText(twoDigit.format(jd));
		mEclipticLongitude.setText(AstroLib.getSexagesimalStr(moonPosEc.λ));
		mEclipticLatitude.setText(AstroLib.getSexagesimalStr(moonPosEc.β));
		mRightAscension.setText(AstroLib.getSexagesimalStr(moonPosEq.α));
		mDeclination.setText(AstroLib.getSexagesimalStr(moonPosEq.δ));
		mDistance.setText(twoDigit.format(moonPosEq.Δ) + "km");

		double[] monRiseSetJdFrac = lunar.calculateMoonRiseTransitSetJulian(jd,
				latitude, longitude, temperature, pressure, altitude);
		byte[] isPOR = AstroLib.isPreceedingOrFollowingDay(monRiseSetJdFrac,
				timezone);
		mMoonRise.setText(AstroLib.getStringHHMMfromDayFrac(
				monRiseSetJdFrac[0], timezone)
				+ AstroLib.PrecedOrFollowStr(getBaseContext(), isPOR[0]));
		mMoonTransit.setText(AstroLib.getStringHHMMfromDayFrac(
				monRiseSetJdFrac[1], timezone)
				+ AstroLib.PrecedOrFollowStr(getBaseContext(), isPOR[1]));
		mMoonSet.setText(AstroLib.getStringHHMMfromDayFrac(monRiseSetJdFrac[2],
				timezone)
				+ AstroLib.PrecedOrFollowStr(getBaseContext(), isPOR[2]));

		mLunarAzimuth.setText(AstroLib.getSexagesimalStr(horizontalMoon.Az));
		mLunarElevation.setText(AstroLib.getSexagesimalStr(horizontalMoon.h));
		mLunarElevationAtmosphere.setText(AstroLib
				.getSexagesimalStr(elevationCorrected));

		double[] sunRiseSet = solar.calculateSunRiseTransitSet(jd, latitude,
				longitude, timezone, ΔT);

		HicriCalendar hicriCalendar = new HicriCalendar(jd, timezone,
				sunRiseSet[2], ΔT);

		double moonAgeConjuction = hicriCalendar.getMoonAge();
		mAge.setText(oneDigit.format(moonAgeConjuction));

		mIlluminated.setText(oneDigit.format(sunMoonPosition
				.getMoonIllimunated() * 100) + "%");
		mIslamicLunation.setText(hicriCalendar.getLunation() + "");
		mBrownLunation.setText(hicriCalendar.getLunation() - 16096 + "");
		mSunRise.setText(AstroLib.getStringHHMMSSS(sunRiseSet[1]));
		mZenith.setText(AstroLib.getStringHHMMSSS(sunRiseSet[0]));
		mSunset.setText(AstroLib.getStringHHMMSSS(sunRiseSet[2]));
		mSolarEclipticLongitude.setText(AstroLib.getSexagesimalStr(sunPosEc.λ));
		mSolarEclipticLatitude.setText(AstroLib.getSexagesimalStr(sunPosEc.β));
		// mSolarEclipticDistance.setText(sunPosEc.Δ+"");
		mSolarRightAscension.setText(AstroLib.getSexagesimalStr(sunEq.α));
		mSolarDeclination.setText(AstroLib.getSexagesimalStr(sunEq.δ));
		mSolarEquatorialDistance.setText(oneDigit.format(sunEq.Δ) + "km");
		// mSolarDeclination.setText(sunEq.δ+"");
		mSolarAzimuth.setText(AstroLib.getSexagesimalStr(horizontalSun.Az));
		mSolarElevation.setText(AstroLib.getSexagesimalStr(horizontalSun.h));

	}

}