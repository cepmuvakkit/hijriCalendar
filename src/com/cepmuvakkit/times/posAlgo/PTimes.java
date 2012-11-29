/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cepmuvakkit.times.posAlgo;

import java.util.GregorianCalendar;


/**
 *
 * @author mgeden
 */
public class PTimes implements Methods, HigherLatitude {

    private final byte FAJR = 0, SUNRISE = 1, SUNTRANSIT = 2, ASR_SHAFI = 3, ASR_HANEFI = 4, SUNSET = 5, ISHA = 6, SUN_COUNT = 7;
    private final byte ISHA_ = 1;
    private double dayLength, nightLength;
    private boolean applyToAll;
    private boolean[] useEstAlways;
    private double jd;
    SolarPosition solar;
  //  byte[] offsetSalat = {-1, -6, 7, 5, 5, 9, 2};
    int[] offsetSalat;
    private double[] salat;
    private double[] salatTemp;
  
    private double dawnAngle, duskAngle;
    public PTimes() {
    	
    }

    public PTimes(double jd, EarthPosition loc, byte calculationMethod, byte[] estMethod) {
        salat = new double[SUN_COUNT];
        salatTemp = new double[SUN_COUNT];
        useEstAlways = new boolean[2];
        solar = new SolarPosition();
        getDawnDuskAngle(calculationMethod);
        this.jd=jd;
        salat = solar.calculateSalatTimes(jd, loc.getLatitude(), loc.getLongitude(), loc.getTimezone(), loc.getTemperature(), loc.getPressure(), loc.getAltitude(), dawnAngle, duskAngle);
        if (duskAngle==0) salat[ISHA] = salat[SUNSET]+1.5; 	
        boolean[] isInvalid = {salat[FAJR] == 0, salat[SUNRISE] == 0, salat[SUNTRANSIT] == 0, salat[ASR_SHAFI] == 0, salat[ASR_HANEFI] == 0, salat[SUNSET] == 0, salat[ISHA] == 0};
       // boolean isOneOfInvalid = isInvalid[FAJR] | isInvalid[SUNRISE] | isInvalid[SUNTRANSIT] | isInvalid[ASR_SHAFI] | isInvalid[ASR_HANEFI] | isInvalid[SUNSET] | isInvalid[ISHA];
       /* useEstAlways[FAJR] = VARIABLE.settings.getBoolean("useEstAlwaysFajr",false);
        useEstAlways[ISHA_] = VARIABLE.settings.getBoolean("useEstAlwaysIsha",false);
        applyToAll= VARIABLE.settings.getBoolean("applytoAll", false);*/
        useEstAlways[FAJR] = false;
        useEstAlways[ISHA_] = false;
        applyToAll= false;
        switch (estMethod[FAJR]) {
            case NO_ESTIMATION:
                break;
            case NEAREST_LAT:
                if (useEstAlways[FAJR] | isInvalid[FAJR]) {
                    salatTemp = solar.calculateSalatTimes(jd, getBaseLatitude(), loc.getLongitude(), loc.getTimezone(), loc.getTemperature(), loc.getPressure(), loc.getAltitude(), dawnAngle, duskAngle);
                    salat[FAJR] = salatTemp[FAJR];
                    if (applyToAll) {
                        salat = salatTemp;
                    }
                }
                break;
            case NEAREST_DAY:
                if (useEstAlways[FAJR] | isInvalid[FAJR]) {
                    double jdNearestDay = firstDayofYearInJulian(jd) + getApproxNearestGoodDay(loc.getLatitude(), dawnAngle) - 1;
                    salatTemp = solar.calculateSalatTimes(jdNearestDay, loc.getLatitude(), loc.getLongitude(), loc.getTimezone(), loc.getTemperature(), loc.getPressure(), loc.getAltitude(), dawnAngle, duskAngle);
                    salat[FAJR] = salatTemp[FAJR];
                    if (applyToAll) {
                        salat = salatTemp;
                    }
                }
                break;
            case ONE_THIRD_OF_NIGHT:
                  if (useEstAlways[FAJR] | isInvalid[FAJR]) {
                    dayLength = salat[SUNSET] - salat[SUNRISE];
                    nightLength = 24.0-dayLength;
                    salat[FAJR] = salat[SUNRISE] - nightLength / 3.0;
                    }
                break;
            case ONE_THIRD_OF_DAY:
                  if (useEstAlways[FAJR] | isInvalid[FAJR]) {
                    dayLength = salat[SUNSET] - salat[SUNRISE];
                    salat[FAJR] = salat[SUNRISE] - dayLength / 3.0;
                    }
                break;
            case ONE_SEVENTH_OF_NIGHT:
                  if (useEstAlways[FAJR] | isInvalid[FAJR]) {
                    dayLength = salat[SUNSET] - salat[SUNRISE];
                    nightLength = 24.0-dayLength;
                    salat[FAJR] = salat[SUNRISE] - nightLength / 7.0;
                    }
                break;
            case ONE_SEVENTH_OF_DAY:
                  if (useEstAlways[FAJR] | isInvalid[FAJR]) {
                    dayLength = salat[SUNSET] - salat[SUNRISE];
                    salat[FAJR] = salat[SUNRISE] - dayLength / 7.0;
                    }
                break;
            case MIDDLE_OF_NIGHT:
                  if (useEstAlways[FAJR] | isInvalid[FAJR]) {
                    dayLength = salat[SUNSET] - salat[SUNRISE];
                    nightLength = 24.0-dayLength;
                    salat[FAJR] = salat[SUNRISE] - nightLength / 2.0;
                    }
                break;
            case FIXED_TIME:
                  if (useEstAlways[FAJR] | isInvalid[FAJR]) {
                    salat[FAJR] = salat[SUNRISE]-getFixedTime();
                    }
                break;
        }

        switch (estMethod[ISHA_]) {
            case NO_ESTIMATION:
                break;
            case NEAREST_LAT:
                if (useEstAlways[ISHA_] | isInvalid[ISHA]) {
                    salatTemp = solar.calculateSalatTimes(jd, getBaseLatitude(), loc.getLongitude(), loc.getTimezone(), loc.getTemperature(), loc.getPressure(), loc.getAltitude(), dawnAngle, duskAngle);
                    salat[ISHA] = salatTemp[ISHA];
                    if (applyToAll) {
                        salat = salatTemp;
                    }
                }
                break;
            case NEAREST_DAY:
                if (useEstAlways[ISHA_] | isInvalid[ISHA]) {
                    double jdNearestDay = firstDayofYearInJulian(jd) + getApproxNearestGoodDay(loc.getLatitude(), duskAngle) - 1;
                    salatTemp = solar.calculateSalatTimes(jdNearestDay, loc.getLatitude(), loc.getLongitude(), loc.getTimezone(), loc.getTemperature(), loc.getPressure(), loc.getAltitude(), dawnAngle, duskAngle);
                    salat[ISHA] = salatTemp[ISHA];
                    if (applyToAll) {
                        salat = salatTemp;
                    }
                }
                break;
            case ONE_THIRD_OF_NIGHT:
                if (useEstAlways[ISHA_] | isInvalid[ISHA]) {
                    dayLength = salat[SUNSET] - salat[SUNRISE];
                    nightLength = 24.0-dayLength;
                    salat[ISHA] = salat[SUNSET] + nightLength / 3.0;
                }
                break;
            case ONE_THIRD_OF_DAY:
                if (useEstAlways[ISHA_] | isInvalid[ISHA]) {
                    dayLength = salat[SUNSET] - salat[SUNRISE];
                    salat[ISHA] = salat[SUNSET] + dayLength / 3.0;
                }
                break;
            case ONE_SEVENTH_OF_NIGHT:
                  if (useEstAlways[ISHA_] | isInvalid[ISHA]) {
                    dayLength = salat[SUNSET] - salat[SUNRISE];
                    nightLength = 24.0-dayLength;
                    salat[ISHA] = salat[SUNSET] +nightLength / 7.0;
                }
                break;
            case ONE_SEVENTH_OF_DAY:
                 if (useEstAlways[ISHA_] | isInvalid[ISHA]) {
                    dayLength = salat[SUNSET] - salat[SUNRISE];
                    salat[ISHA] = salat[SUNSET]+dayLength / 7.0;
                }
                break;
            case MIDDLE_OF_NIGHT:
                 if (useEstAlways[ISHA_] | isInvalid[ISHA]) {
                    dayLength = salat[SUNSET] - salat[SUNRISE];
                    nightLength = 24.0-dayLength;
                    salat[ISHA] = salat[SUNSET] + nightLength / 2.0;
                }
                break;
            case FIXED_TIME:
                  if (useEstAlways[ISHA_] | isInvalid[ISHA]) {
                  salat[ISHA] = salat[SUNSET]+getFixedTime();
                }
                break;
            case TURKISH_ISHA_METHOD:
                  if (useEstAlways[ISHA_] | isInvalid[ISHA]) {
                  salat[ISHA] = salat[SUNSET]+(24-salat[SUNSET]+salat[FAJR])/3;
                }
                break;
        }

        offsetSalat=getOffsetSalat();
        salat[FAJR] = salat[FAJR] + offsetSalat[FAJR] / 60.0;
        salat[SUNRISE] = salat[SUNRISE] + offsetSalat[SUNRISE] / 60.0;
        salat[SUNTRANSIT] = salat[SUNTRANSIT] + offsetSalat[SUNTRANSIT] / 60.0;
        salat[ASR_SHAFI] = salat[ASR_SHAFI] + offsetSalat[ASR_SHAFI] / 60.0;
        salat[ASR_HANEFI] = salat[ASR_HANEFI] + offsetSalat[ASR_HANEFI] / 60.0;
        salat[SUNSET] = salat[SUNSET] + offsetSalat[SUNSET] / 60.0;
        salat[ISHA] = salat[ISHA] + offsetSalat[ISHA] / 60.0;
    }

    public double[] getDawnDuskAngle (int calculationMethod) {

        switch (calculationMethod) {
            case TURKISH_RELIGOUS:
                dawnAngle = -18;
                duskAngle = -17;
                break;
            case NORTH_AMERICA:
                dawnAngle = -15;
                duskAngle = -15;
                break;
            case MUSLIM_LEAGUE:
                dawnAngle = -18;
                duskAngle = -17;
                break;
            case EGYPT_SURVEY:
                dawnAngle = -19.5;
                duskAngle = -17.5;
                break;
            case KARACHI:
                dawnAngle = -18;
                duskAngle = -18;
                break;
            case UMM_ALQURRA:
                dawnAngle = -19;
                duskAngle = 0;
                break;
            case CUSTOM_DAWN_DUSK:
        /*        dawnAngle =  VARIABLE.settings.getFloat("dawnAngle", -20f);
                duskAngle = VARIABLE.settings.getFloat("duskAngle", -19.5f);*/
                dawnAngle = -20f;
                duskAngle = -19.5f;
                break;
        }
        return new double[] {dawnAngle,duskAngle};
    }

    private int[] getOffsetSalat () {
    	/* int[] offsetSalat={
    			 VARIABLE.settings.getInt("offsetFajr", -1), 
    			 VARIABLE.settings.getInt("offsetSunrise", -6),
    			 VARIABLE.settings.getInt("offsetDhur", 7),
    			 VARIABLE.settings.getInt("offsetAsr", 5), 
    			 VARIABLE.settings.getInt("offsetAsr", 5), 
    			 VARIABLE.settings.getInt("offsetMagrib", 9), 
    			 VARIABLE.settings.getInt("offsetIsha", 2)}; */
    	 int[] offsetSalat={
    			-1,-6,7,5, 5,9,2}; 
    
    	return offsetSalat;
      
    }

    public double getBaseLatitude() {
        return  48.5f;     
    }

    /*  public int getFajrEstMethod() {
    return fajrEstMethod;
    }*/
  /*  public void setIshaEstMethod(int fajrEstMethod) {
        fajrEstMethod = 2;
    }*/

    /**
     * Calculates Approximately Nearest Good Day Number
     * dec=-23.45 cos(2pi/365(N+10)) Approximate expression for sun declination
     * If below  inequality fulfilled someone should get true night days (N)
     * 90-δ(N)-ϕlat-depressAngle>0
     * http://en.wikipedia.org/wiki/Declination
     * @param  latitude  is latitude of place
     * @param  depressionAngle  is  defined fajr or isha angle values
     * @return day number of last true night in a year.
     */
    private double getApproxNearestGoodDay(double latitude, double depressionAngle) {
        return Math.floor((365 / (2 * Math.PI)) * MATH.acos(-(90 - latitude + depressionAngle) / 23.45) - 10);
       }

    /**
     * Calculates first day of  current year in julian days.
     * @param  jd is current date in julians
     * @return  start day of current year in julian days.
     */
    private double firstDayofYearInJulian(double jd) {
        int[] julian = AstroLib.getYMDHMSfromJulian(jd);
        return AstroLib.calculateJulianDay(julian[0], 1, 0, 0, 0, 0, 0);
    }

    public double[] getSalat() {
        return salat;
    }
    public GregorianCalendar[] getSalatinGregorian() {
    	 
    	 GregorianCalendar[] schedule = new GregorianCalendar[7];
    	 schedule[0]=AstroLib.convertJulian2Gregorian(jd+salat[FAJR]/24.0);
    	 schedule[1]=AstroLib.convertJulian2Gregorian(jd+salat[SUNRISE]/24.0);
    	 schedule[2]=AstroLib.convertJulian2Gregorian(jd+salat[SUNTRANSIT]/24.0);
    	// schedule[CONSTANT.ASR]=AstroLib.convertJulian2Gregorian(jd+salat[(VARIABLE.settings.getBoolean("isHanafiMathab", false)? ASR_HANEFI : ASR_SHAFI)]/24.0);
    	 schedule[3]=AstroLib.convertJulian2Gregorian(jd+salat[ASR_SHAFI]/24.0);
    	 schedule[4]=AstroLib.convertJulian2Gregorian(jd+salat[SUNSET]/24.0);
    	 schedule[5]=AstroLib.convertJulian2Gregorian(jd+salat[ISHA]/24.0);
    	//schedule[CONSTANT.NEXT_FAJR].add(Calendar.DAY_OF_MONTH, 1); // Next fajr*/
        return schedule; 
    }

    private double getFixedTime() {
    	
    	return  (90)/60;

    }
    
    /*public void setFajrEstMethod();
    public double getBaseLatitude();
    public void  setBaseLatitude();
    public int getIntervalTime();
    public void setIntervalTime();*/
}
