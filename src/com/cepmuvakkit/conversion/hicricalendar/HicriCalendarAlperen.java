package com.cepmuvakkit.conversion.hicricalendar;

import com.cepmuvakkit.times.posAlgo.MoonPhaseAngleWithoutDeclination;

/**
 * @author http://www.cepmuvakkit.com
 */
public class HicriCalendarAlperen {

	private byte dayOfWeek;
	private String ismiSuhiri[] = { "MUHARRAM", "SAFAR", "REBIULAVVAL",
			"REBIULAHIR", "JAMIZIALAVVAL", "JAMIZIALAHIR", "RAJAB", "SHABAN",
			"RAMADHAN", "SHAVVAL", "ZILKADE", "ZILHICCE" };
	private short hijriYear, hijriMonth, hijriDay;
	private final double ILNBase = 17049.7; // İslamic Lunation Base Number at
												// the date 2000 January 28 00:00:00.0 UT  Friday
	private final double jd2000 = 2451574.5;
	private final double synmonth = 29.530588861;// Synodic Month Period
	//private final double synmonth =	29.53058868;// Synodic Month Period

	public HicriCalendarAlperen(double jd) {

		short hday;
		double k;
		dayOfWeek=(byte) ((jd+0.5)%7);
		MoonPhaseAngleWithoutDeclination moon = new MoonPhaseAngleWithoutDeclination();
		hday = -1;
		k = 0;

		do {
			hday = (short) (hday + k / 15 + 1);
			k = moon.getPhase(jd - hday);
			k = k - Math.sqrt(8 * 8 - moon.β * moon.β); // k=k-√(8^2-β^2)
			if (k < 0)
				k = k + 360;
		} while ((hday == 0) || k < 180);

		hijriDay = hday;
		hijriYear = (short) ((jd - jd2000) /synmonth + ILNBase);
		hijriMonth = (short) (hijriYear % 12 + 1);
		hijriYear /= 12;

	}

	public int getHijriYear() {
		return hijriYear;
	}

	public String getHicriTakvim() {
		return getHijriDay() + " " + getHijriMonthName() + " " + getHijriYear();
	}

	public String getHijriMonthName() {
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

}
