/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cepmuvakkit.times.posAlgo;

/**
 * 
 * @author mehmetrg
 */
public class SunMoonPosition {

	private Horizontal sunPosition, moonPosition;
	private double moonPhase,E;
	private SolarPosition solar;
	private LunarPosition lunar;
	private Ecliptic moonPosEc, solarPosEc;
	private Equatorial moonPosEq, solarPosEq;

	public SunMoonPosition(double jd, double latitude, double longitude,
			double altitude, double ΔT) {
		solar = new SolarPosition();
		lunar = new LunarPosition();

		double tau_Sun = 8.32 / (1440.0); // 8.32 min [cy]
		moonPosEc = lunar.calculateMoonEclipticCoordinates(jd, ΔT);
		solarPosEc = solar.calculateSunEclipticCoordinatesAstronomic(jd
				- tau_Sun, ΔT);

		E = Math.toRadians(solarPosEc.λ - moonPosEc.λ);

		moonPosEq = lunar.calculateMoonEqutarialCoordinates(moonPosEc, jd, ΔT);
		solarPosEq = solar
				.calculateSunEquatorialCoordinates(solarPosEc, jd, ΔT);
		moonPosition = moonPosEq.Equ2Topocentric(longitude, latitude, altitude,
				jd, ΔT);// az=183.5858
		sunPosition = solarPosEq.Equ2Topocentric(longitude, latitude, altitude,
				jd, ΔT);
		System.out.println("Lunar Equatorial Distance :" + moonPosEq.Δ);
		// moonPhase = (1 + cos(pi - E)) / 2;
		moonPhase = (1 + Math.cos(Math.PI - E)) / 2;// 48694254279852139 e-17
		// System.out.println(qiblaInfo.getKiloMetres());
	}

	public Ecliptic getMoonPosEcliptic() {
		return moonPosEc;
	}
	public double getDistance()
	{
		return moonPosEq.Δ;
	}

	public Equatorial getMoonPosEquatorial() {
		return moonPosEq;
	}

	public Ecliptic getSolarPosEc() {
		return solarPosEc;
	}
	
	public Horizontal getSunPosition() {
		return sunPosition;
	}

	public Horizontal getMoonPosition() {
		return moonPosition;
	}

	public double getMoonIllimunated() {
		return moonPhase;
	}
	public double getTopocentricSunAltitude() {
		return sunPosition.h;
	}

		
	/**
	 * Selenocentric elongation of the Earth from Sun called phase angle (i).
	 * Selenocentric means as seen from the center of the moon. //
	 * cosψ=sinδosinδ+cosδocosδcos(αo-α) actually return ψ in degrees
	 */
	public double getMoonPhaseAngle() {
		double ψ, δo, δ, αo, α;
		δo = Math.toRadians(solarPosEq.δ);
		δ = Math.toRadians(moonPosEq.δ);
		αo = Math.toRadians(solarPosEq.α);
		α = Math.toRadians(moonPosEq.α);
		// cosψ=sinδosinδ+cosδocosδcos(αo-α)
		ψ = Math.toDegrees(Math.acos((Math.sin(δo) * Math.sin(δ) + Math.cos(δo)
				* Math.cos(δ) * Math.cos(αo - α))));
		return MapTo0To360Range(ψ);
	}
	public double getMoonPhaseAngleWithoutDeclination(double jd) {
		
		return 360-MapTo0To360Range(Math.toDegrees(E));
	}

	public EarthHeading getQiblaAngle(double latitude, double longitude) {
		EarthPosition earth = new EarthPosition();
		return earth.toEarthHeading(new EarthPosition(21.416666667, 39.816666));
	}

	static double MapTo0To360Range(double Degrees) {
		double Value = Degrees;

		// map it to the range 0 - 360
		while (Value < 0)
			Value += 360;
		while (Value > 360)
			Value -= 360;

		return Value;
	}

	public double illumunatedFractionofMoon(double jd, double ΔT) {
		return moonPhase;

	}

	

	
}
