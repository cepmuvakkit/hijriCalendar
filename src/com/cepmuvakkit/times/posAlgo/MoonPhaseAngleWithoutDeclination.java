/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cepmuvakkit.times.posAlgo;

/**
 * 
 * @author mehmetrg
 */
public class MoonPhaseAngleWithoutDeclination {

	private double tau_Sun, E;
	public double β;
	private SolarPosition solar;
	private LunarPosition lunar;
	private Ecliptic moonPosEc, solarPosEc;
	//public Ecliptic moonPosEc, solarPosEc;

	public MoonPhaseAngleWithoutDeclination() {
		solar = new SolarPosition();
		lunar = new LunarPosition();
		tau_Sun = 8.32 / (1440.0); // 8.32 min [cy]
		
	}

	public double getPhase(double jd) {

		double ΔT = AstroLib.calculateTimeDifference(jd);
		moonPosEc = lunar.calculateMoonEclipticCoordinates(jd, ΔT);
		solarPosEc = solar.calculateSunEclipticCoordinatesAstronomic(jd
				- tau_Sun, ΔT);
		β = moonPosEc.β;

		E = Math.toRadians(solarPosEc.λ - moonPosEc.λ);
		return 360 - mapTo0To360Range(Math.toDegrees(E));
	}

	public double getBetaM() {
		return β;
	}

	static double mapTo0To360Range(double Degrees) {
		double Value = Degrees;

		// map it to the range 0 - 360
		while (Value < 0)
			Value += 360;
		while (Value > 360)
			Value -= 360;

		return Value;
	}

}
