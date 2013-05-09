package com.cepmuvakkit.conversion;

//import android.app.Activity;
//import android.os.Bundle;
//import android.widget.TextView;
//
//public class HijriConversion extends Activity {
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		TextView textview= new TextView(this);
//		textview.setText("This is the ALBUMS tab");
//		setContentView(textview);
//	}
//}

import java.util.Calendar;
import com.cepmuvakkit.conversion.hicricalendar.HicriCalendar;
import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.conversion.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class HijriConversion extends Activity {
	private EditText dayText, yearText;
	private TextView mDateDisplay;
	private Button mConvertButton;
	private CheckBox isAfterMagribChkBx;
	private Button mPreviousButton, mNextButton;
	private Spinner s1;
	private int mYear, mMonth, mDay;
	private double jd; // Julian Day
	private boolean isAfterMagrib;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH) + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		// isPM=(c.get(Calendar.AM_PM)==1);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.conversionabs);
		// Get the message from the intent
		/*
		 * Intent intent = getIntent(); String julianDayTxt = intent
		 * .getStringExtra(HijriCalendarTab.EXTRA_MESSAGE);
		 */
		dayText = (EditText) findViewById(R.id.day);
		// monthText= (EditText) findViewById(R.id.month);
		yearText = (EditText) findViewById(R.id.year);
		mConvertButton = (Button) findViewById(R.id.convert);
		mPreviousButton = (Button) findViewById(R.id.previousDay);
		mNextButton = (Button) findViewById(R.id.nextDay);
		mDateDisplay = (TextView) findViewById(R.id.hicriDisplay);
		isAfterMagribChkBx = (CheckBox) findViewById(R.id.after_magrib);
		isAfterMagribChkBx
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						isAfterMagrib = false;
						if (isChecked) {
							isAfterMagrib = true; //
							updateHijriDisplay(getBaseContext());

						}
						updateHijriDisplay(getBaseContext());

					}
				});

		String[] monthStrings = getMonthStrings(); // get month names
		s1 = (Spinner) findViewById(R.id.monthSpinner);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.my_spinner_style, monthStrings) {

			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);

				((TextView) v).setTextSize(16);
				((TextView) v).setTextColor(getResources().getColorStateList(
						R.color.white));
				return v;
			}

			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				View v = super.getDropDownView(position, convertView, parent);
				v.setBackgroundResource(R.drawable.btn_selector);

				((TextView) v).setTextColor(getResources().getColorStateList(
						R.color.white));

				((TextView) v).setGravity(Gravity.CENTER);

				return v;
			}
		};
		s1.setAdapter(adapter);
		updateGregorianDisplay();
		updateHijriDisplay(getBaseContext());
		mPreviousButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				jd--;
				int[] julian = AstroLib.fromJulian(jd);
				mDay = julian[2];
				mMonth = julian[1];
				mYear = julian[0];
				updateHijriDisplay(getBaseContext());
				updateGregorianDisplay();
			}
		});
		mConvertButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				mDay = Integer.parseInt(dayText.getText().toString());
				mMonth = s1.getSelectedItemPosition() + 1;
				// mMonth=Integer.parseInt(monthText.getText().toString());
				mYear = Integer.parseInt(yearText.getText().toString());
				updateGregorianDisplay();
				updateHijriDisplay(getBaseContext());
				// mMonth=s1.getSelectedItemPosition;

				// TODO Auto-generated method stub

			}
		});
		mNextButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				jd++;
				int[] julian = AstroLib.fromJulian(jd);
				mDay = julian[2];
				mMonth = julian[1];
				mYear = julian[0];
				updateGregorianDisplay();
				updateHijriDisplay(getBaseContext());
				// mMonth=s1.getSelectedItemPosition;

				// TODO Auto-generated method stub

			}
		});
		/*
		 * AdManager.setTestDevices( new String[] { AdManager.TEST_EMULATOR, //
		 * Android emulator "0D188670F8C55615B5BD351CB7907D1D", // My T-Mobile
		 * G1 Test Phone } );
		 */
	}

	// updates the date we display in the TextView
	private void updateHijriDisplay(Context context) {

		// HegiraCalendar hicriCalendar = new HegiraCalendar(jd,isAfterMagrib);
		double ΔT = AstroLib.calculateTimeDifference(jd);
		HicriCalendar hicriCalendar;
		if (isAfterMagrib)
			hicriCalendar = new HicriCalendar(jd + 1, 2, 17, ΔT);
		else
			hicriCalendar = new HicriCalendar(jd, 2, 17, ΔT);

		String hijriDate = hicriCalendar.getHicriTakvim(context) + "\n"
				+ hicriCalendar.getDay(context) + "\n"
				+ hicriCalendar.checkIfHolyDay(context, false);
		mDateDisplay.setText(hijriDate);
	}

	private void updateGregorianDisplay() {
		dayText.setText(mDay + "");
		s1.setSelection(mMonth - 1);
		yearText.setText(mYear + "");
		jd = AstroLib.calculateJulianDay(mYear, mMonth, mDay, 0, 0, 0, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setCancelable(true);

		int itemId = item.getItemId();
		if (itemId == R.id.about_item) {
			// dialogBuilder.setMessage(R.string.about_text);
			// dialogBuilder.create().show();
			SpannableString s = new SpannableString(
					getText(R.string.about_text));
			Linkify.addLinks(s, Linkify.WEB_URLS);
			LinearLayout help = (LinearLayout) getLayoutInflater().inflate(
					R.layout.about, null);
			TextView message = (TextView) help.findViewById(R.id.help);
			message.setText(s);
			message.setMovementMethod(LinkMovementMethod.getInstance());
			new AlertDialog.Builder(this).setTitle("About").setView(help)
					.setPositiveButton(android.R.string.ok, null).create()
					.show();
		} else if (itemId == R.id.more_applications) {
			startActivity(new Intent(
					"android.intent.action.VIEW",
					Uri.parse("market://search?q=pub:%22Mehmet%20Mahmudoglu%22")));
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * DateFormatSymbols returns an extra, empty value at the end of the array
	 * of months. Remove it.
	 */
	static protected String[] getMonthStrings() {
		String[] months = new java.text.DateFormatSymbols().getMonths();
		int lastIndex = months.length - 1;

		if (months[lastIndex] == null || months[lastIndex].length() <= 0) { // last
																			// item
																			// empty
			String[] monthStrings = new String[lastIndex];
			System.arraycopy(months, 0, monthStrings, 0, lastIndex);
			return monthStrings;
		} else { // last item not empty
			return months;
		}
	}

}