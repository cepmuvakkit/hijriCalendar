/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cepmuvakkit.times.posAlgo;

public interface HigherLatitude {

    /**
     *
     * At certain locations and times of year, some prayer times do not occur
     * or otherwise are impossible to precisely calculate using conventional
     * means. These methods generally apply to locations with High latitudes
     * (near or above 49 degrees) or locations of Extreme proportion (near or
     * above 66 degrees).
     *
     * Method Category Information:<ul>
     *
     *         <li> Nearest Latitude (Aqrab Al-Bilaad): Calculate a prayer time
     *           using a safe latitude value. The recommended latitude by
     *           many schools of Fiqh is 48.5 degrees, but you can customize
     *           this by setting the "HigherLatitude.setNearestLat()" method.
     *           This principle isdue to the fellow jurists of the Shafi school.
     *           The diffrenece of time between the
     *           beginning of twilight and sunrise at 48o latitude is calculated.
     *           The same difference is maintained between the end of Sahur time
     *           and the sunrise for the locality for which the Sahurtime is to be
     *           evaluated.
     *          </li>
     *         <li> Nearest Good Day (Aqrab Al-Ayyam): The library determines
     *           the closest previous or next day that the Fajr and Ishaa
     *           times occur and are both valid.
     *           This principle is due to the following jurists of the Hanafi school.
     *           The principle is, for those places where during a period of time the
     *           twilight does not end, the time for Sahur for this “abnormal” period
     *           will be taken as the time for the last day on which twilight ended.
     *           For example in Aberdeen the last day on which twilight ends in the
     *           night and then begins in the morning (at 1.20 am) is 30th April.
     *           Hence throughout the abnormal period (1st May to 12thAugust) the
     *           limit time for Sahur will remain at 1.20 am.
     *
     *         </li>
     *         <li> An [amount] of Night and Day: Unlike the above mentioned
     *           methods, the multiple methods in this category have no proof
     *           in traditional Shari'a (Fiqh) resources. These methods were
     *           introduced by modern day Muslim scholars and scientists for
     *           practical reasons only.
     *         </li>
     *         <li> Minutes from Shurooq/Maghrib: Use an interval time to
     *           calculate Fajr and Ishaa. This will set the values of Fajr
     *           and Ishaa to the same as the computed Shurooq and Maghrib
     *           respectively, then add or subtract the amount of minutes
     *           found in the "Method.getFajrInv" and "Method.getIshaaInv"
     *           methods.
     *          </li>
     *          </ul>
     */
    public static final byte NO_ESTIMATION = 0;
    /**
     * Nearest latitude method for estimation  applied to the
     * all prayer times all the time.
     */
    public static final byte NEAREST_LAT = 1;
    /**
     * Nearest Good Day:
     * Apply to Fajr and Ishaa times but only if
     * the library has detected that the current
     * fajr or Ishaa time is invalid. This is the
     * default method. (Default)
     */
    public static final byte NEAREST_DAY = 2;
    /**
     * 1/3th of Night: Apply to Fajr and Ishaa times
     */
    public static final byte ONE_THIRD_OF_NIGHT = 3;
    /**
     * 1/3th of Night: Apply to Fajr and Ishaa times
     */
    public static final byte ONE_THIRD_OF_DAY = 4;
    /**
     * 1/7th of Night: Apply to Fajr and Ishaa times
     */
    public static final byte ONE_SEVENTH_OF_NIGHT = 5;
    /**
     * 1/7th of Night: Apply to Fajr and Ishaa times
     */
    public static final byte ONE_SEVENTH_OF_DAY = 6;
    /**
     * Half of the Night: Apply to Fajr and Ishaa times always.
     */
    public static final byte MIDDLE_OF_NIGHT = 7;
    /**
     * FIXED munites used for Isha and Fajr
     * Minutes from Shurooq/Maghrib: Use an interval time to
     *           calculate Fajr and Ishaa. This will set the values of Fajr
     *           and Ishaa to the same as the computed Shurooq and Maghrib
     *           respectively, then add or subtract the amount of minutes
     */
    public static final byte FIXED_TIME = 8;
    /**
     * This Method is specific for isha time calculation
     * used by Turkish Religious Affairs of Turkey.
     * It is  calculated as follows;
     * Isha=SunSet+(24-SunSet+Fajr)/3;
     */
    public static final byte TURKISH_ISHA_METHOD = 9;
    
}
