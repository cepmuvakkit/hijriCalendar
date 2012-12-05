package com.cepmuvakkit.conversion;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import com.cepmuvakkit.conversion.hicricalendar.HicriCalendar;
import com.cepmuvakkit.conversion.phaseEvents.MonthPhases;
import com.cepmuvakkit.conversion.settings.ApplicationConstants;
import com.cepmuvakkit.conversion.settings.LunarCalendarSettings;
import com.cepmuvakkit.conversion.settings.MoonCalendarPreferenceActivity;
import com.cepmuvakkit.conversion.views.MoonCanvasView;
import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.LunarPosition;
import com.cepmuvakkit.times.posAlgo.SolarPosition;
import com.cepmuvakkit.times.posAlgo.SunMoonPosition;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class HijriCalendarTab extends Activity {
	private MoonCanvasView moonCanvasView;
	private int temperature, pressure, altitude;
	private double mLatitude, mLongitude, mTimeZone, moonPhase, timezoneinDay;;
	private String mLocationName;
	private Button mPickDate, mPickTime, mPreviousButton, mNextButton;
	private TextView mDateHijri, mPositionValues, mCityName, mMoonRise,
			mMoonTransit, mMoonSet, mLit, mAzimuth, mAltitude, mMoonAge,
			mLunation, mSunSet, mJulianDate, mMoonDistance, mNewMoon,
			mNewCrescent, mFirstQuarter, mFullMoon, mLastQuarter,
			mSolarEclipse, mLunarEclipse, mSunElevation, mMoonStatus;
	// private TextView mPosAngleAxis;
	private ImageView mImageViewMoon;
	private SunMoonPosition sunMoonPosition;
	private double moonAge, jd, mSunsetHour, ΔT; // Julian Day
	private DecimalFormat twoDigitFormat, oneDigit, twoDigit;
	private DateFormat dfTr, dfTime, dfDate;
	private SharedPreferences preferences;
	public final static String EXTRA_MESSAGE = "com.cepmuvakkit.conversion.JULIANDAY";
	private int w, h;
	private double[] moonPhasesJd;
	private int[] eclipses;
	private GPSTracker gps;

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {

		Display display = getWindowManager().getDefaultDisplay();
		w = display.getWidth();
		h = display.getHeight();

		twoDigitFormat = new DecimalFormat("#0.00°");
		twoDigit = new DecimalFormat("#0.00");
		oneDigit = new DecimalFormat("#0.0");
		dfTr = DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT);
		dfTime = DateFormat.getTimeInstance(DateFormat.SHORT);
		dfDate = DateFormat.getDateInstance(DateFormat.SHORT);

		super.onCreate(savedInstanceState);
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		LunarCalendarSettings.load(preferences);
		returnCurrentJulianDay();
		getLocation();
		setContentView(R.layout.activity_android_draw);

		mPickDate = (Button) findViewById(R.id.dateButton);
		mPickTime = (Button) findViewById(R.id.timeButton);
		mPreviousButton = (Button) findViewById(R.id.PrevButton);
		mNextButton = (Button) findViewById(R.id.nextButton);
		mDateHijri = (TextView) findViewById(R.id.hicriDisplayTxtView);
		mCityName = (TextView) findViewById(R.id.citynameTxtView);
		mPositionValues = (TextView) findViewById(R.id.posLatLongTxtView);
		mMoonRise = (TextView) findViewById(R.id.moonRiseTxtView);
		mMoonTransit = (TextView) findViewById(R.id.transitTxtView);
		mMoonSet = (TextView) findViewById(R.id.moonSetTxtView);
		mLit = (TextView) findViewById(R.id.litTxtView);
		mAzimuth = (TextView) findViewById(R.id.azimuthTxtView);
		mAltitude = (TextView) findViewById(R.id.altitudeTxtView);
		mMoonAge = (TextView) findViewById(R.id.ageTxtView);
		mLunation = (TextView) findViewById(R.id.lunationTxtView);
		mSunSet = (TextView) findViewById(R.id.sunsetTxtView);
		mJulianDate = (TextView) findViewById(R.id.julianDateTxtView);
		mSunElevation = (TextView) findViewById(R.id.sunElevation);
		mMoonStatus = (TextView) findViewById(R.id.MoonStatTxtView);
		// mPosAngleAxis = (TextView) findViewById(R.id.posAngleAxis);
		mMoonDistance = (TextView) findViewById(R.id.distanceTxtView);
		mImageViewMoon = (ImageView) this.findViewById(R.id.imageViewMoon);
		moonCanvasView = new MoonCanvasView(this);

		mNewMoon = (TextView) findViewById(R.id.NewMoonTextView);
		mNewCrescent = (TextView) findViewById(R.id.NewCrescentTextView);
		mFirstQuarter = (TextView) findViewById(R.id.FirstQuarterTextView);
		mFullMoon = (TextView) findViewById(R.id.FullMoonTextView);
		mLastQuarter = (TextView) findViewById(R.id.LastQaurterTextView);
		mSolarEclipse = (TextView) findViewById(R.id.SolarEclipseTextView);
		mLunarEclipse = (TextView) findViewById(R.id.LunarEclipseTextView);
		updateLocationInfo();
		updateMoonInformation();
		updateDisplayDate();
		updateDisplayTime();
		updateHijriDisplay(getBaseContext());
		updatePhaseAndEclipses();

		// double latitude = 39.95;
		// double longitude = 32.85;
		// double jd = 2456220.33542;
		// double ΔT = 0;
		// jd = AstroLib.calculateJulianDay(2012, 10, 27, 0, 0, 0, 0);

		// add a click listener to the button
		mPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(ApplicationConstants.DATE_DIALOG_ID);
			}
		});
		// add a click listener to the button
		mPickTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(ApplicationConstants.TIME_DIALOG_ID);
			}
		});

		mPreviousButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				jd--;
				LunarCalendarSettings.getInstance().setJulianDay(jd);
				updateDisplayDate();
				updateMoonInformation();
				updateHijriDisplay(getBaseContext());
				if (jd < moonPhasesJd[0])
					updatePhaseAndEclipses();
			}
		});

		mNextButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				jd++;

				LunarCalendarSettings.getInstance().setJulianDay(jd);
				updateDisplayDate();
				updateMoonInformation();
				updateHijriDisplay(getBaseContext());
				if (jd > (moonPhasesJd[0] + 29.5))
					updatePhaseAndEclipses();
			}
		});
		// get the current date

	}

	// updates the date we display in the TextView
	private void updateDisplayDate() {
		timezoneinDay = LunarCalendarSettings.getInstance().getTimezone() / 24.0;
		mPickDate.setText(dfDate.format(AstroLib.convertJulian2Gregorian(
				jd + timezoneinDay).getTime()));
	}

	protected void updateHijriDisplay(Context context) {

		HicriCalendar hicriCalendar = new HicriCalendar(jd, mTimeZone,
				mSunsetHour, ΔT);

		String hijriDate = hicriCalendar.getHicriTakvim(context) + " "
				+ hicriCalendar.getDay(context) + " "
				+ hicriCalendar.checkIfHolyDay(context, false);
		mDateHijri.setText(hijriDate);
		moonAge = hicriCalendar.getMoonAge();
		mMoonAge.setText(oneDigit.format(moonAge) + "d");
		mLunation.setText(hicriCalendar.getLunation() + "");

	}

	// updates the time we display in the TextView
	private void updateDisplayTime() {

		mPickTime.setText(dfTime.format(AstroLib.convertJulian2Gregorian(
				jd + timezoneinDay).getTime()));

	}

	private void updatePhaseAndEclipses() {
		MonthPhases phasesofMonth = new MonthPhases(jd);
		timezoneinDay = LunarCalendarSettings.getInstance().getTimezone() / 24.0;
		eclipses = phasesofMonth.getEclipses();
		moonPhasesJd = phasesofMonth.getMoonPhasesJd();
		// lunation = phasesofMonth.getLunation();

		mNewMoon.setText(dfTr.format(AstroLib.convertJulian2Gregorian(
				moonPhasesJd[0] + timezoneinDay).getTime()));
		mNewCrescent.setText(dfTr.format(AstroLib.convertJulian2Gregorian(
				moonPhasesJd[1] + timezoneinDay).getTime()));
		mFirstQuarter.setText(dfTr.format(AstroLib.convertJulian2Gregorian(
				moonPhasesJd[2] + timezoneinDay).getTime()));
		mFullMoon.setText(dfTr.format(AstroLib.convertJulian2Gregorian(
				moonPhasesJd[3] + timezoneinDay).getTime()));
		mLastQuarter.setText(dfTr.format(AstroLib.convertJulian2Gregorian(
				moonPhasesJd[4] + timezoneinDay).getTime()));
		mSolarEclipse.setText(getText(eclipses[2]));
		mLunarEclipse.setText(getText(eclipses[3]));

		// mLunation.setText(getText(R.string.lunation) + " : " + lunation);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		int[] julian = AstroLib.getYMDHMSfromJulian(jd + mTimeZone / 24);
		switch (id) {
		case ApplicationConstants.DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, julian[0],
					julian[1] - 1, julian[2]);
		case ApplicationConstants.TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, julian[3],
					julian[4], false);

		}
		return null;
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			int[] julian = AstroLib.getYMDHMSfromJulian(jd + mTimeZone / 24);
			jd = AstroLib.calculateJulianDay(year, monthOfYear + 1, dayOfMonth,
					julian[3], julian[4], julian[5], mTimeZone);
			LunarCalendarSettings.getInstance().setJulianDay(jd);
			updateDisplayDate();
			updateMoonInformation();
			updateHijriDisplay(getBaseContext());
			if ((jd < moonPhasesJd[0]) || (jd > (moonPhasesJd[0] + 29.5)))
				updatePhaseAndEclipses();

		}
	};

	// the callback received when the user "sets" the time in the dialog
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			int[] julian = AstroLib.getYMDHMSfromJulian(jd + mTimeZone / 24);
			jd = AstroLib.calculateJulianDay(julian[0], julian[1], julian[2],
					hourOfDay, minute, julian[5], mTimeZone);
			updateMoonInformation();
			updateDisplayTime();
			updateMoonInformation();
			updateHijriDisplay(getBaseContext());
			if ((jd < moonPhasesJd[0]) || (jd > (moonPhasesJd[0] + 29.5)))
				updatePhaseAndEclipses();

		}
	};

	private void returnCurrentJulianDay() {
		final Calendar c = Calendar.getInstance();
		jd = AstroLib.calculateJulianDay(c);
	
		mTimeZone = c.getTimeZone().getOffset(c.getTimeInMillis()) / 3600000;
		timezoneinDay = mTimeZone / 24.0;
		LunarCalendarSettings.getInstance().setJulianDay(jd);	
		if (LunarCalendarSettings.getInstance().isManualInput() == false) {
			LunarCalendarSettings.getInstance().setTimezone(mTimeZone);
		}
		 LunarCalendarSettings.save(preferences);
	}

	protected void updateLocationInfo() {
		LunarCalendarSettings.load(preferences);
		mLocationName = LunarCalendarSettings.getInstance().getCustomCity();
		mLatitude = LunarCalendarSettings.getInstance().getLatitude();
		mLongitude = LunarCalendarSettings.getInstance().getLongitude();
		temperature = LunarCalendarSettings.getInstance().getTemperature();
		pressure = LunarCalendarSettings.getInstance().getPressure();
		altitude = LunarCalendarSettings.getInstance().getAltitude();
		mTimeZone = LunarCalendarSettings.getInstance().getTimezone();
		mCityName.setText(mLocationName);
		mPositionValues.setText(twoDigitFormat.format(mLatitude) + ", "
				+ twoDigitFormat.format(mLongitude));

	}

	private void updateMoonInformation() {

		ΔT = AstroLib.calculateTimeDifference(jd);
		LunarPosition lunar = new LunarPosition();
		SolarPosition solar = new SolarPosition();
		sunMoonPosition = new SunMoonPosition(jd, mLatitude, mLongitude,
				altitude, ΔT);
		moonPhase = sunMoonPosition.getMoonIllimunated();
		mLit.setText(oneDigit.format(moonPhase * 100) + "%");
		mAzimuth.setText(twoDigitFormat.format(sunMoonPosition
				.getMoonPosition().getAzimuth()) + "");
		mAltitude.setText(twoDigitFormat.format(sunMoonPosition
				.getMoonPosition().getElevation()) + "");
		double[] moonRiseSet = lunar.calculateMoonRiseTransitSet(jd, mLatitude,
				mLongitude, mTimeZone, temperature, pressure, altitude);
		double[] sunRiseSet = solar.calculateSunRiseTransitSet(jd, mLatitude,
				mLongitude, mTimeZone, ΔT);
		mMoonRise.setText(AstroLib.getStringHHMM(moonRiseSet[0]));
		mMoonTransit.setText(AstroLib.getStringHHMM(moonRiseSet[1]));
		mMoonSet.setText(AstroLib.getStringHHMM(moonRiseSet[2]));
		mSunsetHour = sunRiseSet[2];
		mSunSet.setText(AstroLib.getStringHHMM(mSunsetHour));
		mJulianDate.setText(twoDigit.format(jd));
		moonCanvasView.setParameters(mLatitude, mLongitude, jd, ΔT, moonPhase,
				moonAge > ApplicationConstants.synmonth / 2, w, h);
		mImageViewMoon.setImageBitmap(moonCanvasView.getBitmapImage());
		mSunElevation.setText(twoDigitFormat.format(sunMoonPosition
				.getTopocentricSunAltitude()) + "");
		mMoonDistance.setText(oneDigit.format(sunMoonPosition.getDistance())
				+ "km");
		mMoonStatus.setText(null);

	}

	private void getLocation() {
		gps = new GPSTracker(HijriCalendarTab.this);

		if (LunarCalendarSettings.getInstance().isManualInput() == true) {
			mLongitude = LunarCalendarSettings.getInstance().getLongitude();
			mLatitude = LunarCalendarSettings.getInstance().getLatitude();
			altitude = LunarCalendarSettings.getInstance().getAltitude();
			mLocationName = LunarCalendarSettings.getInstance().getCustomCity();
			mTimeZone = LunarCalendarSettings.getInstance().getTimezone();
		} else {
			if (LunarCalendarSettings.getInstance().isDataFromGPS() == true) {
				mLongitude = LunarCalendarSettings.getInstance().getLongitude();
				mLatitude = LunarCalendarSettings.getInstance().getLatitude();
				altitude = LunarCalendarSettings.getInstance().getAltitude();
				mLocationName = LunarCalendarSettings.getInstance()
						.getCustomCity();
				mTimeZone = LunarCalendarSettings.getInstance().getTimezone();
				Toast.makeText(
						this,
						getText(R.string.last_good_known),
						Toast.LENGTH_LONG).show();
			} else {
				if (gps.canGetLocation()) {

					mLongitude = gps.getLongitude();
					mLatitude = gps.getLatitude();
					mLocationName = gps.getLocationName(mLatitude, mLongitude);
					altitude = (int) gps.getAltitude();

					LunarCalendarSettings.getInstance().setLatitude(mLatitude);
					LunarCalendarSettings.getInstance()
							.setLongitude(mLongitude);
					LunarCalendarSettings.getInstance().setAltitude(altitude);
					LunarCalendarSettings.getInstance().setCustomCity(
							mLocationName);
					// LunarCalendarSettings.getInstance().setTimezone(mTimezone);
					LunarCalendarSettings.save(preferences);
//
//					Toast.makeText(
//							this,
//							"Your Position: "
//									+ twoDigitFormat.format(mLatitude)
//									+ twoDigitFormat.format(mLongitude) + " ",
//							Toast.LENGTH_SHORT).show();

				} else {

					Toast.makeText(
							this,getText(R.string.no_location),
					Toast.LENGTH_LONG).show();
					
					gps.showSettingsAlert();
				}
			}
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		//Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
		updateLocationInfo();
		

	}

	@Override
	protected void onResume() {
		super.onResume();
		//	Toast.makeText(this, "onResume()", Toast.LENGTH_SHORT).show();
		updateLocationInfo();
	
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		//Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onRestart() {
		
		super.onPause();
		getLocation();
		updateLocationInfo();
		updateMoonInformation();
		//Toast.makeText(this, " onRestart()", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onDestroy() {
		//Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();

		super.onDestroy();
		gps.stopUsingGPS();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	protected void onStop() {
	//	Toast.makeText(this, "onStop()", Toast.LENGTH_SHORT).show();
		super.onStop();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		int itemId = item.getItemId();
		if (itemId == R.id.refresh) {
			returnCurrentJulianDay();
			updateDisplayDate();
			updateMoonInformation();
			updateHijriDisplay(getBaseContext());
			updateDisplayTime();
			updatePhaseAndEclipses();
		} else if (itemId == R.id.use_GPS) {
			LunarCalendarSettings.getInstance().setDataFromGPS(false);
			LunarCalendarSettings.getInstance().setManualInput(false);
			getLocation();
			updateLocationInfo();
			updateMoonInformation();
			updateDisplayDate();
			updateDisplayTime();
			updateHijriDisplay(getBaseContext());
			updatePhaseAndEclipses();
		} else if (itemId == R.id.settings) {
			intent = new Intent(getApplicationContext(),
					MoonCalendarPreferenceActivity.class);
			startActivityForResult(intent, 2);
		} else if (itemId == R.id.more_applications) {
			startActivity(new Intent(
					"android.intent.action.VIEW",
					Uri.parse("market://search?q=pub:%22Mehmet%20Mahmudoglu%22")));
		} else if (itemId == R.id.menu_help) {
			SpannableString s = new SpannableString(getText(R.string.help_text));
			Linkify.addLinks(s, Linkify.WEB_URLS);
			LinearLayout help = (LinearLayout) getLayoutInflater().inflate(
					R.layout.help, null);
			help = (LinearLayout) getLayoutInflater().inflate(R.layout.help,
					null);
			TextView message = (TextView) help.findViewById(R.id.help);
			message.setText(s);
			message.setMovementMethod(LinkMovementMethod.getInstance());
			new AlertDialog.Builder(this).setTitle(R.string.help).setView(help)
					.setPositiveButton(android.R.string.ok, null).create()
					.show();
		} else if (itemId == R.id.about_item) {
			SpannableString s2 = new SpannableString(
					getText(R.string.about_text));
			Linkify.addLinks(s2, Linkify.WEB_URLS);
			LinearLayout about = (LinearLayout) getLayoutInflater().inflate(
					R.layout.about, null);
			about = (LinearLayout) getLayoutInflater().inflate(R.layout.about,
					null);
			TextView message2 = (TextView) about.findViewById(R.id.help);
			message2.setText(s2);
			message2.setMovementMethod(LinkMovementMethod.getInstance());
			new AlertDialog.Builder(this).setTitle(R.string.about)
					.setView(about)
					.setPositiveButton(android.R.string.ok, null).create()
					.show();
		}
		return super.onOptionsItemSelected(item);
	}

	// updates the date we display in the TextView

	/** Called when the user clicks the Send button */
	public void openConversion(@SuppressWarnings("unused") View view) {
		Intent intent = new Intent(this, HijriConversion.class);
		intent.putExtra(EXTRA_MESSAGE, jd + "");
		startActivity(intent);
	}

	/** Called when the user clicks the New Moon */
	public void setTimetoNewMoon(@SuppressWarnings("unused") View view) {

		jd = moonPhasesJd[0];
		LunarCalendarSettings.getInstance().setJulianDay(jd);
		updateDisplayDate();
		updateDisplayTime();
		updateMoonInformation();
		updateHijriDisplay(getBaseContext());

		mMoonStatus.setText(getText(eclipses[0]));
		String blank = new String("");

		if (getText(eclipses[0]).equals(blank))
			mMoonStatus.setText(R.string.new_moon);
	}

	/** Called when the user clicks the New Crescent */
	public void setTimetoNewCrescent(@SuppressWarnings("unused") View view) {

		jd = moonPhasesJd[1];
		LunarCalendarSettings.getInstance().setJulianDay(jd);
		updateDisplayDate();
		updateDisplayTime();
		updateMoonInformation();
		updateHijriDisplay(getBaseContext());
		mMoonStatus.setText(R.string.new_crescent);
	}

	/** Called when the user clicks the FirstQuarter */
	public void setTimetoFirstQuarter(@SuppressWarnings("unused") View view) {

		jd = moonPhasesJd[2];
		LunarCalendarSettings.getInstance().setJulianDay(jd);
		updateDisplayDate();
		updateDisplayTime();
		updateMoonInformation();
		updateHijriDisplay(getBaseContext());
		mMoonStatus.setText(R.string.first_quarter);
	}

	/** Called when the user clicks the FullMoon */
	public void setTimetoFullMoon(@SuppressWarnings("unused") View view) {

		jd = moonPhasesJd[3];
		LunarCalendarSettings.getInstance().setJulianDay(jd);
		updateDisplayDate();
		updateDisplayTime();
		updateMoonInformation();
		updateHijriDisplay(getBaseContext());

		mMoonStatus.setText(getText(eclipses[1]));

		String blank = new String("");

		if (getText(eclipses[0]).equals(blank))
			mMoonStatus.setText(R.string.full_moon);

	}

	/** Called when the user clicks the Last Quarter */
	public void setTimetoLastQuarter(@SuppressWarnings("unused") View view) {

		jd = moonPhasesJd[4];
		LunarCalendarSettings.getInstance().setJulianDay(jd);
		updateDisplayDate();
		updateDisplayTime();
		updateMoonInformation();
		updateHijriDisplay(getBaseContext());
		mMoonStatus.setText(R.string.last_quarter);
	}

	/** Called when the user clicks the TimetoReal */
	public void setTimetoReal(@SuppressWarnings("unused") View view) {

		returnCurrentJulianDay();
		LunarCalendarSettings.getInstance().setJulianDay(jd);
		updateDisplayDate();
		updateDisplayTime();
		updateMoonInformation();
		updateHijriDisplay(getBaseContext());
		updatePhaseAndEclipses();

	}

	/** Called when the user clicks the TimetoReal */
	public void setTimetoSunSet(@SuppressWarnings("unused") View view) {

		int[] HHMM = AstroLib.convertHour2HHMM(mSunsetHour);
		int[] julian = AstroLib.getYMDHMSfromJulian(jd + mTimeZone / 24);
		jd = AstroLib.calculateJulianDay(julian[0], julian[1], julian[2],
				HHMM[0], HHMM[1], julian[5], mTimeZone);
		updateMoonInformation();
		updateDisplayTime();
		updateMoonInformation();
		updateHijriDisplay(getBaseContext());
		mMoonStatus.setText(R.string.sunset);
	}

}
