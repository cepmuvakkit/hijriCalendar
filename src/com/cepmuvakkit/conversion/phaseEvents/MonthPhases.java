/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cepmuvakkit.conversion.phaseEvents;

import com.cepmuvakkit.conversion.R;
import com.cepmuvakkit.conversion.settings.ApplicationConstants;

import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.Ecliptic;
import com.cepmuvakkit.times.posAlgo.LunarPosition;

public class MonthPhases {

	private double jd;
	private boolean[] isFound;
	
	private double[] moonPhasesJd;
	private int[] eclipses;
	private double tMoonPhase, tCrescent; // Calculated time for the New Moon
											// and the Crescent Moon in
											// JulianDays UTC
	final double dt = 7; // Step (1 week)
	final double dtc = 3.0; // Step (3 days)
	final double acc = (0.5 / 1440.0); // Desired Accuracy (0.5 min)

	// private short NewMoon=0, NewCrescent=8, FirstQuarter=90, FullMoon=180,
	// LastQuarter=270;

	enum Phases {

		NewMoon(0), FirstQuarter(90), FullMoon(180), LastQuarter(270);
		private int angle;

		private Phases(int angle) {
			this.angle = angle;
		}

		public int getAngle() {
			return angle;
		}
	};

	public MonthPhases(double jd) {
	//	String eclipse;
		double t0, t1;
		double D0, D1;
		moonPhasesJd=new double[5];
		eclipses=new int[4];
		MoonPhases phases = new MoonPhases();
		t0 = jd-ApplicationConstants.synmonth;
		t1 = t0 + dt;
		tMoonPhase = t0;
		// Check 13 months

		// Correct for difference of ephemeris time and universal time
		double ΔT = AstroLib.calculateTimeDifference(t1);
		// Search for phases
		int i=0;
		for (Phases phase : Phases.values()) {

			isFound = new boolean[1];
			isFound[0] = false;
			// Bracket desired phase event
			D0 = phases.searchPhaseEvent(t0, ΔT, phase.getAngle());
			D1 = phases.searchPhaseEvent(t1, ΔT, phase.getAngle());
			
			while ((D0 * D1 > 0.0) || (D1 < D0)) {
				t0 = t1;
				D0 = D1;
				t1 += dt; // increase 1 week
				D1 = phases.searchPhaseEvent(t1, ΔT, phase.getAngle());// Finds
																		// correct
																		// week
																		// for
																		// iteration
			}
			// Iterate time of Moon phase
			tMoonPhase = AstroLib.Pegasus(phases, t0, t1, ΔT, acc, isFound,
					phase.getAngle());
			moonPhasesJd[i++]=tMoonPhase;
			//moonPhasesJd
		
			// Eclipse check
			if (phase == Phases.NewMoon) {
				eclipses[0] = SolarEclipseFlag(tMoonPhase, ΔT,false);
				eclipses[2] = SolarEclipseFlag(tMoonPhase, ΔT,true);
			// System.out.print(AstroLib.fromJulianToCalendar(tMoonPhase));
			} else if (phase == Phases.FullMoon) {
				eclipses[1] = LunarEclipseFlag(tMoonPhase, ΔT,false);
				eclipses[3] = LunarEclipseFlag(tMoonPhase, ΔT,true);
			} 
			
//			else {
//				eclipse = " "; // First or Last Quarter
//			}
////			System.out.print(AstroLib.fromJulianToCalendarStr(tMoonPhase)
//					+ eclipse);
//			System.out.print(tMoonPhase
//					+ eclipse);
			
		
			;
			if ((phase == Phases.NewMoon) && isFound[0]) {
				int crescentElongationAngle = 8;
				tCrescent = AstroLib.Pegasus(phases, tMoonPhase, tMoonPhase
						+ dtc, ΔT, acc, isFound, crescentElongationAngle);
				// System.out.print(isFound[0]);
				//moonPhasesJd[i++]=AstroLib.convertJulian2Gregorian(tCrescent);
				moonPhasesJd[i++]=tCrescent;
				//System.out.print(AstroLib.fromJulianToCalendarStr(tCrescent));
			}
			// Move search interval by one week
			t0 = tMoonPhase;
			t1 = t0 + dt;
		}

	}
	

	/**
	 * SolarEclipseFlag: Returns a 2 char. string indicating the possibility of
	 * a solar eclipse
	 * 
	 * @param tNewMoon
	 *            Julian Day when the New Moon occurs
	 * @param ΔT
	 *            parameter delta-T (ΔT)
	 * @return Eclipse flag
	 */
	int  SolarEclipseFlag(double tNewMoon, double ΔT,boolean isAbbreviated) {
		Ecliptic moonPos;
		LunarPosition lunar = new LunarPosition();
		moonPos = lunar.calculateMoonEclipticCoordinates(tNewMoon, ΔT);
		double b = Math.toRadians(Math.abs(moonPos.β));// b Ecliptic latitude of
														// the Moon in [rad]

		if (b > 0.027586) {
			return R.string.blank; // No solar eclipse possible
		}
		if (b < 0.015223) {
			return isAbbreviated?R.string.central_eclipse_abr:R.string.central_eclipse; // Central eclipse certain c
		}
		if (b < 0.018209) {
			return isAbbreviated?R.string.possible_eclipse_abr:R.string.possible_eclipse; // Possible central eclipse c?
		}
		if (b < 0.024594) {
			return isAbbreviated?R.string.partial_eclipse_abr:R.string.partial_eclipse; // Partial solar eclipse certain p
		}
		return isAbbreviated?R.string.possible_partial_eclipse_abr:R.string.possible_partial_eclipse; // Possible partial solar eclipse p?
	}
	
    


	/**
	 * LunarEclipseFlag: Returns a 2 char. string indicating the possibility of
	 * a lunar eclipse a solar eclipse
	 * 
	 * @param tFullMoon
	 *            Julian Day when the Full Moon happen
	 * @param ΔT
	 *            parameter delta-T (ΔT)
	 * @return Eclipse flag
	 */
	int LunarEclipseFlag(double tFullMoon, double ΔT,boolean isAbbreviated) {
		Ecliptic moonPos;
		LunarPosition lunar = new LunarPosition();
		moonPos = lunar.calculateMoonEclipticCoordinates(tFullMoon, ΔT);
		double b = Math.toRadians(Math.abs(moonPos.β));// b Ecliptic latitude of
														// the Moon in [rad]

		if (b > 0.028134) {
			return R.string.blank; // No lunar eclipse possible 
		}
		if (b < 0.006351) {
			return isAbbreviated?R.string.lunar_eclipse_abr:R.string.lunar_eclipse; // Total lunar eclipse certain t
		}
		if (b < 0.009376) {
			return isAbbreviated?R.string.possible_lunar_eclipse_abr:R.string.possible_lunar_eclipse; // Possible total eclipse t?
		}
		if (b < 0.015533) {
			return isAbbreviated?R.string.partial_lunar_eclipse_abr:R.string.partial_lunar_eclipse; // Partial lunar eclipse certain
		}
		if (b < 0.018568) {
			return isAbbreviated?R.string.possible_partial_lunar_eclipse_abr:R.string.possible_partial_lunar_eclipse; // Possible partial eclipse
		}
		if (b < 0.025089) {
			return isAbbreviated?R.string.penumbral_eclipse_abr:R.string.penumbral_eclipse; // Penumbral lunar eclipse certain
		}
		return isAbbreviated?R.string.possible_penumbral_eclipse_abr:R.string.possible_penumbral_eclipse; // Possible penumbral lunar eclipse
	}
	
	public double[] getMoonPhasesJd() {
	
		return moonPhasesJd;
	}
	public  int[] getEclipses() {
		
		return eclipses;
	}
	public int getLunation ()
	{
		final double LunatBase = 1948083.1284733997;
		int Lunation = (int) Math.floor((moonPhasesJd[0] + 7 - LunatBase) / ApplicationConstants.synmonth);
		return Lunation;
	}
	public double getMoonAgeConjuction() {
		return jd -moonPhasesJd[0];
	}
	
	}

