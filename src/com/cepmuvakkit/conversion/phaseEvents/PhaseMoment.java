/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cepmuvakkit.conversion.phaseEvents;

import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.Ecliptic;
import com.cepmuvakkit.times.posAlgo.LunarPosition;

public class PhaseMoment {

    private double jd;
    private boolean[] isFound;
    private double tMoonPhase, tCrescent; //Calculated time  for the New Moon and  the Crescent Moon in JulianDays UTC
    final double dt = 7;           // Step (1 week)
    final double dtc = 3.0;        // Step (3 days)
    final double acc = (0.5 / 1440.0);  // Desired Accuracy (0.5 min)
    // private short NewMoon=0, NewCrescent=8, FirstQuarter=90, FullMoon=180, LastQuarter=270;

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

    public PhaseMoment(int Year) {
        //System.out.println("   NewMoon                NewCrescent          FirstQuarter          FullMoon               LastQuarter   ");
    	 System.out.println(" NewCrescent      ");
        
    	int Lunation;
        @SuppressWarnings("unused")
		String eclipse;
        jd = AstroLib.calculateJulianDay(Year, 1, 1, 0, 0, 0, 0);
        double t0, t1;
        double D0, D1;
        MoonPhases phases = new MoonPhases();
        t0 = jd;
        t1 = t0 + dt;
        tMoonPhase = t0;
        // Check 13 months
        for (Lunation = 0; Lunation <= 11; Lunation++) {

            // Correct for difference of ephemeris time and universal time
            double ΔT = AstroLib.calculateTimeDifference(t1);
            // Search for phases
            for (Phases phase : Phases.values()) {

                isFound = new boolean[1];
                isFound[0] = false;
                // Bracket desired phase event
                D0 = phases.searchPhaseEvent(t0, ΔT, phase.getAngle());
                D1 = phases.searchPhaseEvent(t1, ΔT, phase.getAngle());

                while ((D0 * D1 > 0.0) || (D1 < D0)) {
                    t0 = t1;
                    D0 = D1;
                    t1 += dt;   // increase 1 week
                    D1 = phases.searchPhaseEvent(t1, ΔT, phase.getAngle());//Finds correct week for iteration
                }
                // Iterate time of Moon phase
                tMoonPhase = AstroLib.Pegasus(phases, t0, t1, ΔT, acc, isFound, phase.getAngle());

                // Eclipse check
                if (phase == Phases.NewMoon) {
                    eclipse = SolarEclipseFlag(tMoonPhase, ΔT);
                    //System.out.print(AstroLib.fromJulianToCalendar(tMoonPhase));
                } else if (phase == Phases.FullMoon) {
                    eclipse = LunarEclipseFlag(tMoonPhase, ΔT);
                } else {
                    eclipse = " ";  // First or Last Quarter
                }
              //  System.out.print(AstroLib.fromJulianToCalendarStr(tMoonPhase) + eclipse);
                if ((phase == Phases.NewMoon) && isFound[0]) {
                	int crescentElongationAngle=8;
                    tCrescent = AstroLib.Pegasus(phases, tMoonPhase, tMoonPhase + dtc, ΔT, acc, isFound, crescentElongationAngle);
                    // System.out.print(isFound[0]);
                  
                    System.out.print(AstroLib.fromJulianToCalendarStr(tCrescent));
                }
                // Move search interval by one week
                t0 = tMoonPhase;
                t1 = t0 + dt;
            }

            System.out.println();
        }
    }

    /**
     * SolarEclipseFlag: Returns a 2 char. string indicating
     * the possibility of
     * a solar eclipse
     * @param  tNewMoon  Julian  Day when the New Moon occurs
     * @param  ΔT parameter delta-T (ΔT)
     * @return Eclipse flag
     */
    String SolarEclipseFlag(double tNewMoon, double ΔT) {
        Ecliptic moonPos;
        LunarPosition lunar = new LunarPosition();
        moonPos = lunar.calculateMoonEclipticCoordinates(tNewMoon, ΔT);
        double b = Math.toRadians(Math.abs(moonPos.β));// b Ecliptic latitude of the Moon in [rad]


        if (b > 0.027586) {
            return "  ";   // No solar eclipse possible
        }
        if (b < 0.015223) {
            return "c ";   // Central eclipse certain
        }
        if (b < 0.018209) {
            return "c?";   // Possible central eclipse
        }
        if (b < 0.024594) {
            return "p ";   // Partial solar eclipse certain
        }
        return "p?";        // Possible partial solar eclipse
    }

    /**
     * LunarEclipseFlag: Returns a 2 char. string indicating
     * the possibility of a lunar eclipse
     * a solar eclipse
     * @param  tFullMoon  Julian  Day when the Full  Moon happen
     * @param  ΔT parameter delta-T (ΔT)
     * @return Eclipse flag
     */
    String LunarEclipseFlag(double tFullMoon, double ΔT) {
        Ecliptic moonPos;
        LunarPosition lunar = new LunarPosition();
        moonPos = lunar.calculateMoonEclipticCoordinates(tFullMoon, ΔT);
        double b = Math.toRadians(Math.abs(moonPos.β));// b Ecliptic latitude of the Moon in [rad]



        if (b > 0.028134) {
            return "  ";   // No lunar eclipse possible
        }
        if (b < 0.006351) {
            return "t ";   // Total lunar eclipse certain
        }
        if (b < 0.009376) {
            return "t?";   // Possible total eclipse
        }
        if (b < 0.015533) {
            return "p ";   // Partial lunar eclipse certain
        }
        if (b < 0.018568) {
            return "p?";   // Possible partial eclipse
        }
        if (b < 0.025089) {
            return "P ";   // Penumbral lunar eclipse certain
        }
        return "P?";      // Possible penumbral lunar eclipse
    }
}


