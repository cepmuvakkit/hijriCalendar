package com.cepmuvakkit.conversion.settings;

import java.text.DecimalFormat;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
public class LunarCalendarSettings {
	private static LunarCalendarSettings instance = new LunarCalendarSettings();
	private boolean isManualInput;
	private boolean isDataFromGPS;
	//private boolean isLastGoodKnownLocation;
	private String customCity;
	private double longitude,latitude,timezone,julianDay;
	private int temperature,elongation,pressure,altitude;
	
	public LunarCalendarSettings() {
	}
	public double getJulianDay() {
		return julianDay;
	}
	public void setJulianDay(double julianDay) {
		this.julianDay = julianDay;
	}
	public boolean isDataFromGPS() {
		return isDataFromGPS;
	}
	
	public void setDataFromGPS(boolean isDataFromGPS) {
		this.isDataFromGPS = isDataFromGPS;
	}

	public static void setInstance(LunarCalendarSettings instance) {
		LunarCalendarSettings.instance = instance;
	}

	public void setManualInput(boolean isManualInput) {
		this.isManualInput = isManualInput;
	}


	public DecimalFormat twoDigitFormat;
	


	public static LunarCalendarSettings getInstance() {
		return instance;
	}
	public String getCustomCity() {
		return customCity;
	}

	public void setCustomCity(String customCity) {
		this.customCity = customCity;
	}

	public double getTimezone() {
		return timezone;
	}

	public void setTimezone(double timezone) {
		this.timezone = timezone;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getElongation() {
		return elongation;
	}

	public void setElongation(int elongation) {
		this.elongation = elongation;
	}

	public int getPressure() {
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}


	public boolean isManualInput() {
		return isManualInput;
	}
	
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	
	public static void load(SharedPreferences preferences) {
		
		//instance.isDataFromGPS=preferences.getBoolean(ApplicationConstants.PREF_IS_GPS_DATA, false);
		instance.isManualInput=preferences.getBoolean(ApplicationConstants.PREF_IS_MANUAL_INPUT, false);
		instance.customCity=preferences.getString(ApplicationConstants.PREF_CUSTOM_CITY,"DefCustomCity");
		instance.latitude= Double.parseDouble(preferences.getString(ApplicationConstants.PREF_LATITUDE,"0"));
		instance.longitude= Double.parseDouble(preferences.getString(ApplicationConstants.PREF_LONGITUDE,"0"));
		instance.timezone=Double.parseDouble(preferences.getString(ApplicationConstants.PREF_TIMEZONE,"0"));
		instance.elongation=Integer.parseInt(preferences.getString(ApplicationConstants.PREF_ELONGATION,"8"));// get yaparken string gibi yapmak gerekebilir.
		instance.temperature=Integer.parseInt(preferences.getString(ApplicationConstants.PREF_TEMPERATURE, "10"));
		instance.pressure=Integer.parseInt(preferences.getString(ApplicationConstants.PREF_PRESSURE,"1010"));
		instance.altitude=Integer.parseInt(preferences.getString(ApplicationConstants.PREF_ALTITUDE,"0"));
		instance.isDataFromGPS=preferences.getBoolean(ApplicationConstants.PREF_IS_GPS_DATA, instance.customCity.equals("DefCustomCity")?false:true);

		}

	public static void save(SharedPreferences preferences) {
		
		Editor editor = preferences.edit();
		
		editor.putBoolean(ApplicationConstants.PREF_IS_MANUAL_INPUT,instance.isManualInput);
		editor.putString(ApplicationConstants.PREF_CUSTOM_CITY,instance.customCity);
		editor.putString(ApplicationConstants.PREF_LATITUDE,instance.latitude+"");
		editor.putString(ApplicationConstants.PREF_LONGITUDE,instance.longitude+"");
		editor.putString(ApplicationConstants.PREF_TIMEZONE,instance.timezone+"");
		editor.putString(ApplicationConstants.PREF_ELONGATION, instance.elongation+"");
		editor.putString(ApplicationConstants.PREF_TEMPERATURE, instance.temperature+"");
		editor.putString(ApplicationConstants.PREF_PRESSURE, instance.pressure+"");
		editor.putString(ApplicationConstants.PREF_ALTITUDE, instance.altitude+"");
		instance.isDataFromGPS=instance.customCity.equals("DefCustomCity")?false:true;
		editor.putBoolean(ApplicationConstants.PREF_IS_GPS_DATA,instance.isDataFromGPS);
		editor.commit();
	}

	
}
