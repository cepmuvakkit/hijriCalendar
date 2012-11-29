package com.cepmuvakkit.conversion.settings;

import com.cepmuvakkit.conversion.R;
import com.cepmuvakkit.conversion.R.xml;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class MoonCalendarPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Toast.makeText(this, "Preference Activity Running",
		// Toast.LENGTH_LONG).show();

		// Inflate preference screen
		addPreferencesFromResource(R.xml.mooncalendar_preferences);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
