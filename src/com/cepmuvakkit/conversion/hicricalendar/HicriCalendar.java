package com.cepmuvakkit.conversion.hicricalendar;

import android.content.Context;

import com.cepmuvakkit.conversion.R;
import com.cepmuvakkit.conversion.phaseEvents.MoonPhases;
import com.cepmuvakkit.times.posAlgo.AstroLib;

/**
 * @author http://www.cepmuvakkit.com
 */
public class HicriCalendar {

	private byte dayOfWeek;
	private String ismiSuhiri[] = { "MUHARRAM", "SAFAR", "REBIULAVVAL",
			"REBIULAHIR", "JAMIZIALAVVAL", "JAMIZIALAHIR", "RAJAB", "SHABAN",
			"RAMADHAN", "SHAVVAL", "ZILKADE", "ZILHICCE" };
	private int Lunation;
	private int hijriYear, hijriMonth, hijriDay;
	private boolean[] isFound;
	private double tNewMoon;// Calculated time for the New Moon in JulianDays
							// UTC
	private double moonAgeConjuction;
	private double tCrescent; // Calculated time for the Crescent Moon in
								// JulianDays UTC
	final double synmonth = 29.530588861;// Synodic Month Period
	final double dt = 7.0; // Step (1 week)
	final double dtc = 3.0; // Step (3 days)
	final double acc = (0.5 / 1440.0); // Desired Accuracy (0.5 min)
	final double LunatBase = 1948083.1284733997;

	/*
	 * 1948083.1284733997 Start of New moon on July 26, 621 Hijri Year 0.
	 * Actually islamic calendar started from the year 1. Hegira event occured
	 * at the year 1. Start of New moon at the the Islamic Calendar on July 15,
	 * 622 according the 8 degrees elongation. (New Crescent Time : 14/07/622
	 * 20:25:25 Moon Visibility time )
	 */

	public HicriCalendar(double jd, double timezone, double sunset, double ΔT, int adjust) {

		double jdShifted = jd + (timezone + 24 - sunset) / 24.0;
		int jdShifted0h = (int) Math.round(jdShifted);
		dayOfWeek = (byte) (jdShifted0h % 7);
		double tnow, t0, t1;
		double D0, D1;
		tnow = jd;
		t1 = tnow;
		t0 = t1 - dt; // decrease 1 week
		isFound = new boolean[1];
		isFound[0] = false;

		// Search for phases bracket desired phase event
		MoonPhases phases = new MoonPhases();
		D0 = phases.searchPhaseEvent(t0, ΔT, 0);
		D1 = phases.searchPhaseEvent(t1, ΔT, 0);
		while ((D0 * D1 > 0.0) || (D1 < D0)) {
			t1 = t0;
			D1 = D0;
			t0 -= dt;
			D0 = phases.searchPhaseEvent(t0, ΔT, 0);// Finds correct week for
													// iteration
		}
		// Iterate NewMoon time
		tNewMoon = AstroLib.Pegasus(phases, t0, t1, ΔT, acc, isFound, 0);
		if (isFound[0]) {
			tCrescent = AstroLib.Pegasus(phases, tNewMoon, tNewMoon + dtc, ΔT,
					acc, isFound, 8);
		}
		moonAgeConjuction = jd - tNewMoon;
		int tCrescentRound = (int) Math.round(tCrescent - 0.2208333333333);
		// 0.720830 is equal to 5:18 am
		Lunation = (int) Math.floor((tCrescent + 7 - LunatBase) / synmonth);
		hijriYear = (int) Math.floor(Lunation / 12.0);
		// Returns 1 for Muharrem, 2 for Safer .... 12 for Zilhicce
		hijriMonth = Lunation % 12;
		if (hijriMonth < 0)
			hijriMonth = hijriMonth + 12;
		hijriMonth = hijriMonth + 1;

		hijriDay = jdShifted0h - tCrescentRound;
		if (hijriDay == 0) {
			tCrescent = AstroLib.Pegasus(phases, tNewMoon - synmonth, tNewMoon
					- synmonth + dtc, ΔT, acc, isFound, 8);
			tCrescentRound = (int) Math.round(tCrescent - 0.2208333333333);
			hijriDay = jdShifted0h - tCrescentRound;
			hijriMonth--;
			if (hijriMonth == 0)
				hijriMonth = 12;

		}

		/*For adjustment purposes*/
		//int adjust=-1;
		hijriDay=hijriDay+adjust;
		if (hijriDay == 0) {
			tCrescent = AstroLib.Pegasus(phases, tNewMoon - synmonth, tNewMoon
					- synmonth + dtc, ΔT, acc, isFound, 8);
			tCrescentRound = (int) Math.round(tCrescent - 0.2208333333333);
			hijriDay = jdShifted0h+adjust-tCrescentRound;
			if (hijriMonth == 0)
				hijriMonth = 12;
			hijriMonth--;
			
		};
		if (hijriMonth == 0) {
			hijriMonth=12;
			hijriYear--;
		}

	}

	public int getHijriYear() {
		return hijriYear;
	}

	public String getHicriTakvim(Context context) {
		return getHijriDay() + " " + getHijriMonthName(context) + " "
				+ getHijriYear();
	}

	public String getHijriMonthName(Context context) {
		String ismiSuhiri[] = { context.getString(R.string.MUHARRAM),
				context.getString(R.string.SAFAR),
				context.getString(R.string.REBIULAVVAL),
				context.getString(R.string.REBIULAHIR),
				context.getString(R.string.JAMIZIALAVVAL),
				context.getString(R.string.JAMIZIALAHIR),
				context.getString(R.string.RAJAB),
				context.getString(R.string.SHABAN),
				context.getString(R.string.RAMADHAN),
				context.getString(R.string.SHAVVAL),
				context.getString(R.string.ZILKADE),
				context.getString(R.string.ZILHICCE) };
		return ismiSuhiri[(hijriMonth - 1)];
	}

	public int getHijriMonth() {
		return hijriMonth;
	}

	public int getHijriDay() {
		return hijriDay;
	}

	/**
	 * 1 Muharrem=Hijri New Year 10 Muharrem= Day of Ashura 11/12 Rebiulevvel=
	 * Mawlid-al Nabi 1 Recep=Start of Holy Months 1st Cuma day on Recep=
	 * Lailatul-Raghaib 27 Recep=Lailatul-Me'rac 14/15 Nisfu-Sha'aban 1
	 * Ramadhan=1. Day of Ramadhan 27 Ramadhan= Lailatul-Qadr 1 Sevval=1. Day of
	 * Eid-al-Fitr 2 Sevval=2. Day of Eid-al-Fitr 3 Sevval=3. Day of Eid-al-Fitr
	 * 9 ZiLHiCCE= A'rafa 10 Zilhicce= 1. Day of Eid-al-Adha 11 Zilhicce= 2. Day
	 * of Eid-al-Adha 12 Zilhicce= 3. Day of Eid-al-Adha 13 Zilhicce= 4. Day of
	 * Eid-al-Adha
	 * 
	 * @return
	 */
	public String checkIfHolyDay(Context context, boolean isBeforeMagrib) {
		String holyDay = "";
		isBeforeMagrib = !isBeforeMagrib;
		switch (hijriMonth) {
		case 1:
			if (hijriDay == 1) {
				holyDay = context.getString(R.string.NEWYEAR);
			} else if (hijriDay == 10) {
				holyDay = context.getString(R.string.ASHURA);
			}
			break;
		case 3:
			if ((hijriDay == 11) && (isBeforeMagrib)) {
				holyDay = context.getString(R.string.tonight) + " "
						+ context.getString(R.string.MAWLID);
			}
			if (hijriDay == 12) {
				holyDay = context.getString(R.string.MAWLID);
			}
			break;
		case 7:
			if ((hijriDay == 1) && (hijriMonth == 7)) {
				holyDay = context.getString(R.string.HOLYMONTHS);
			}

			if ((dayOfWeek == 3) && (hijriDay < 7) && (isBeforeMagrib)) {
				holyDay = context.getString(R.string.tonight) + " "
						+ context.getString(R.string.RAGHAIB);
			}
			if ((dayOfWeek == 4) && (hijriDay < 7)) {
				holyDay = context.getString(R.string.RAGHAIB);
			}
			if ((hijriDay == 26) && (isBeforeMagrib)) {
				holyDay = context.getString(R.string.tonight) + " "
						+ context.getString(R.string.MERAC);
			}
			if (hijriDay == 27) {
				holyDay = context.getString(R.string.MERAC);
			}
			break;
		case 8:
			if ((hijriDay == 14) && (isBeforeMagrib)) {
				holyDay = context.getString(R.string.tonight) + " "
						+ context.getString(R.string.BARAAT);
			}
			if (hijriDay == 15) {
				holyDay = context.getString(R.string.BARAAT);
			}
			break;
		case 9:
			if ((hijriDay == 26) && (isBeforeMagrib)) {
				holyDay = context.getString(R.string.tonight) + " "
						+ context.getString(R.string.QADR);
			}
			if ((hijriDay == 27)) {
				holyDay = context.getString(R.string.QADR);
			}
			break;
		case 10:
			if ((hijriDay == 1) || (hijriDay == 2) || (hijriDay == 3)) {
				holyDay = hijriDay + " "
						+ context.getString(R.string.DAYOFEIDFITR);

			}
			break;
		case 12:
			if (hijriDay == 9) {
				holyDay = context.getString(R.string.AREFE);
			}
			if ((hijriDay == 10) || (hijriDay == 11) || (hijriDay == 12)
					|| (hijriDay == 13)) {
				holyDay = (hijriDay - 9) + " "
						+ context.getString(R.string.DAYOFEIDAHDA);
			}
			break;
		}
		return holyDay;
	}

	public String getDay(Context context) {
		String daysName[] = { context.getString(R.string.MONDAY),
				context.getString(R.string.TUESDAY),
				context.getString(R.string.WEDNESDAY),
				context.getString(R.string.THURSDAY),
				context.getString(R.string.FRIDAY),
				context.getString(R.string.SATURDAY),
				context.getString(R.string.SUNDAY) };
		return daysName[dayOfWeek];

	}

	public String getHicriTakvim() {
		return getHijriDay() + " " + getHijriMonthName() + " " + getHijriYear();
	}

	public String getHijriMonthName() {
		return ismiSuhiri[(hijriMonth - 1)];
	}

	/**
	 * 1 Muharrem=Hijri New Year 10 Muharrem= Day of Ashura 11/12 Rebiulevvel=
	 * Mawlid-al Nabi 1 Recep=Start of Holy Months 1st Cuma day on Recep=
	 * Lailatul-Raghaib 27 Recep=Lailatul-Me'rac 14/15 Nisfu-Sha'aban 1
	 * Ramadhan=1. Day of Ramadhan 27 Ramadhan= Lailatul-Qadr 1 Sevval=1. Day of
	 * Eid-al-Fitr 2 Sevval=2. Day of Eid-al-Fitr 3 Sevval=3. Day of Eid-al-Fitr
	 * 9 ZiLHiCCE= A'rafa 10 Zilhicce= 1. Day of Eid-al-Adha 11 Zilhicce= 2. Day
	 * of Eid-al-Adha 12 Zilhicce= 3. Day of Eid-al-Adha 13 Zilhicce= 4. Day of
	 * Eid-al-Adha
	 * 
	 * @return
	 */
	public String checkIfHolyDay(boolean isBeforeMagrib) {
		String holyDay = "";
		isBeforeMagrib = !isBeforeMagrib;
		switch (hijriMonth) {
		case 1:
			if (hijriDay == 1) {
				holyDay = "NEWYEAR";
			} else if (hijriDay == 10) {
				holyDay = "ASHURA";
			}
			break;
		case 3:
			if ((hijriDay == 11) && (isBeforeMagrib)) {
				holyDay = "tonight" + " " + "MAWLID";
			}
			if (hijriDay == 12) {
				holyDay = "MAWLID";
			}
			break;
		case 7:
			if ((hijriDay == 1) && (hijriMonth == 7)) {
				holyDay = "HOLYMONTHS";
			}

			if ((dayOfWeek == 3) && (hijriDay < 7) && (isBeforeMagrib)) {
				holyDay = "tonight" + " " + "RAGHAIB";
			}
			if ((dayOfWeek == 4) && (hijriDay < 7)) {
				holyDay = "RAGHAIB";
			}
			if ((hijriDay == 26) && (isBeforeMagrib)) {
				holyDay = "tonight" + " " + "MERAC";
			}
			if (hijriDay == 27) {
				holyDay = "MERAC";
			}
			break;
		case 8:
			if ((hijriDay == 14) && (isBeforeMagrib)) {
				holyDay = "tonight" + " " + "BARAAT";
			}
			if (hijriDay == 15) {
				holyDay = "BARAAT";
			}
			break;
		case 9:
			if ((hijriDay == 26) && (isBeforeMagrib)) {
				holyDay = "tonight" + " " + "QADR";
			}
			if ((hijriDay == 27)) {
				holyDay = "QADR";
			}
			break;
		case 10:
			if ((hijriDay == 1) || (hijriDay == 2) || (hijriDay == 3)) {
				holyDay = hijriDay + " " + "DAYOFEIDFITR";

			}
			break;
		case 12:
			if (hijriDay == 9) {
				holyDay = "AREFE";
			}
			if ((hijriDay == 10) || (hijriDay == 11) || (hijriDay == 12)
					|| (hijriDay == 13)) {
				holyDay = (hijriDay - 9) + " " + "DAYOFEIDAHDA";
			}
			break;
		}
		return holyDay;
	}

	public String getDay() {

		String daysName[] = { "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY",
				"FRIDAY", "SATURDAY", "SUNDAY" };
		return daysName[dayOfWeek];

	}

	public int getLunation() {
		return Lunation;
	}

	public double getMoonAge() {
		return moonAgeConjuction;
	}

}
